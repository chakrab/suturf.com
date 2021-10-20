from pprint import pprint
from datetime import timedelta
from timeit import default_timer as timer
from faker import Faker
from mongocomn import MongoCommon


class HardCodedValues:
    trailer_types = (
        'Automotive Hauler', 'Boat Hauler', 'Dry Goods', 'Flat Bed', 'Furniture', 'Livestock', 'Reefer', 'Tanker'
    )
    truck_types = (
        'Class 7', 'Class 8'
    )


"""
    [{
        Driver ID,
        Driver Name,
        Driver License,
        Truck Type,
        Trailer Count,
        Trailer Type [],
        Origin,
        Destination,
        Contract: {
            Bill of Lading,
            Agreement ID,
            Customs Form ID
        },
        Trip Plan: [
            {
                Location: {
                    Name,
                    Address Line,
                    City,
                    Country,
                    Postal
                },
                Stop Type: {
                    [PICKUP], [STOP], [DROPOFF]
                },
                EstimatedArrival,
                Estimated Departure
            }
        ]
    }]
"""
class PopulateDummyData:
    def populate_one_record(self):
        fake = Faker()
        arec = dict()
        arec['driverId'] = fake.pyint(min_value=10001, max_value=99999)
        arec['driverName'] = fake.name()
        arec['driverLicense'] = fake.pystr_format('?###-######-##')
        arec['truckType'] = fake.random.choice(HardCodedValues.truck_types)
        arec['trailerCount'] = fake.pyint(min_value=1, max_value=2)
        trailers = list()
        for i in range(arec['trailerCount']):
            trailers.append(fake.random.choice(HardCodedValues.trailer_types))
        arec['trailerType'] = trailers
        origin = fake.company()
        destination = fake.company()
        arec['origin'] = origin
        arec['destination'] = destination
        contract = dict()
        contract['billOfLading'] = fake.pystr_format('???#-########')
        contract['agreementFormId'] = fake.pystr_format('?#-######-###')
        contract['customsFormId'] = fake.pystr_format('?###')
        arec['contract'] = contract
        tripplans = list()
        stops = fake.pyint(min_value=2, max_value=4)
        otime = fake.date_time_between(start_date='-10y', end_date='now')
        for i in range(stops):
            tripplan = dict()
            location = dict()
            location['address'] = fake.street_address()
            location['city'] = fake.city()
            location['country'] = fake.current_country_code()
            location['postal'] = fake.postcode()
            # Origin
            if i == 0:
                location['name'] = origin
                tripplan['stopType'] = 'PICKUP'
                tripplan['estimatedArrival'] = otime
                otime = otime + timedelta(hours=fake.pyint(min_value=1, max_value=3))
                tripplan['estimatedDeparture'] = otime
                tripplan['location'] = location
            # Destination
            elif i == (stops - 1):
                location['name'] = destination
                tripplan['stopType'] = 'DROPOFF'
                otime = otime + timedelta(hours=fake.pyint(min_value=10, max_value=40))
                tripplan['estimatedArrival'] = otime
                otime = otime + timedelta(hours=fake.pyint(min_value=1, max_value=3))
                tripplan['estimatedDeparture'] = otime
                tripplan['location'] = location
            else:
                location['name'] = fake.company()
                tripplan['stopType'] = 'STOP'
                otime = otime + timedelta(hours=fake.pyint(min_value=5, max_value=20))
                tripplan['estimatedArrival'] = otime
                otime = otime + timedelta(hours=fake.pyint(min_value=1, max_value=3))
                tripplan['estimatedDeparture'] = otime
                tripplan['location'] = location
            tripplans.append(tripplan)
        arec['tripPlans'] = tripplans
        return arec

    def insert_dummy_records(self, cnt, dbc):
        col = dbc['workitems']
        for i in range(cnt):
            if i % 1000 == 0:
                if i % 5000 == 0:
                    print('|', end='', flush=True)
                else:
                    print('.', end='', flush=True)
            jsn = self.populate_one_record()
            col.insert_one(jsn)


if __name__ == '__main__':
    client = None
    tstart = timer()
    try:
        client, conn = MongoCommon.get_db_connection('mongodb://localhost:27017', 'truckies')

        pd = PopulateDummyData()
        pd.insert_dummy_records(28900, conn)
    except Exception as e:
        pprint(e)

    if client:
        MongoCommon.close_connection(client)
    tend = timer()
    print('\nElapsed Time: %3f' % (tend - tstart))
