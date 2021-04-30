import dateutil.parser
from timeit import default_timer as timer
from mongocomn import MongoCommon


class SingleThreader:
    def __init__(self):
        pass

    def fetch_data(self, start_date, end_date):
        dct = {}
        client = None
        try:
            client, conn = MongoCommon.get_db_connection('mongodb://localhost:27017', 'truckies')
            col = conn['workitems']
            for x in col.find({
                'tripPlans.0.estimatedArrival': {
                    '$gte': start_date,
                    '$lt': end_date
                }}):
                idx = '%04d%02d' % ((x['tripPlans'][0]['estimatedArrival']).year, (x['tripPlans'][0]['estimatedArrival']).month)
                cnt_trailers = x['trailerCount']
                if idx in dct:
                    dct[idx] = dct[idx] + cnt_trailers
                else:
                    dct[idx] = cnt_trailers
        except Exception as e:
            print(e)
        if client:
            MongoCommon.close_connection(client)
        return dct


if __name__ == '__main__':
    st = SingleThreader()
    tstart = timer()
    try:
        cfind = st.fetch_data(
            dateutil.parser.parse('2011-01-01T00:00:00.000Z'),
            dateutil.parser.parse('2021-01-01T00:00:00.000Z')
        )
        print(cfind)
    except Exception as e:
        print(e)

    tend = timer()
    print('\nElapsed Time: %3f' % (tend - tstart))