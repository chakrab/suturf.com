from concurrent.futures import ThreadPoolExecutor, as_completed
from dateutils import DateUtils
from timeit import default_timer as timer
from mongocomn import MongoCommon


class MultiThreader:
    def fetch_data(self, start_date, end_date, num):
        dct = {}
        client = None
        try:
            print('Starting thread [%d]' % num)
            client, conn = MongoCommon.get_db_connection('mongodb://localhost:27017', 'truckies')
            col = conn['workitems']
            for x in col.find({
                'tripPlans.0.estimatedArrival': {
                    '$gte': start_date,
                    '$lt': end_date
                }}):
                idx = '%04d%02d' % (
                (x['tripPlans'][0]['estimatedArrival']).year, (x['tripPlans'][0]['estimatedArrival']).month)
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

    def fetch_data_mt(self, start_date, end_date):
        cfind = []
        dtarr = DateUtils.split_date(start_date, end_date)
        with ThreadPoolExecutor(max_workers=5) as etor:
            futures = []
            cnt = 0
            for arr in dtarr:
                cnt += 1
                futures.append(etor.submit(self.fetch_data, start_date=arr[0], end_date=arr[1], num=cnt))
            for future in as_completed(futures):
                cfind.append(future.result())
        return cfind


if __name__ == '__main__':
    st = MultiThreader()
    tstart = timer()
    try:
        cfind = st.fetch_data_mt(
            '2011-01-01T00:00:00.000Z',
            '2021-01-01T00:00:00.000Z'
        )
        print(cfind)
    except Exception as e:
        print(e)

    tend = timer()
    print('\nElapsed Time: %3f' % (tend - tstart))

