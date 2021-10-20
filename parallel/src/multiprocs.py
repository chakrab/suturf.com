from dateutils import DateUtils
from timeit import default_timer as timer
from multiprocessing import Process, Queue
from mongocomn import MongoCommon


class MultiProcessors:
    def fetch_data(self, start_date, end_date, que, num):
        dct = {}
        client = None
        try:
            print('Starting process [%d]' % num)
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
            que.put(dct)
        except Exception as e:
            print(e)
        if client:
            MongoCommon.close_connection(client)

    def fetch_data_mp(self, start_date, end_date):
        processes = []
        queue = Queue()
        dtarr = DateUtils.split_date(start_date, end_date)
        cnt = 0
        for arr in dtarr:
            cnt += 1
            p = Process(target=self.fetch_data, args=(arr[0], arr[1], queue, cnt))
            processes.append(p)
            p.start()
        for proc in processes:
            proc.join()

        cfind = []
        while not queue.empty():
            cfind.append(queue.get())
        return cfind


if __name__ == '__main__':
    st = MultiProcessors()
    tstart = timer()
    try:
        cfind = st.fetch_data_mp(
            '2011-01-01T00:00:00.000Z',
            '2020-01-01T00:00:00.000Z'
        )
        print(cfind)
    except Exception as e:
        print(e)

    tend = timer()
    print('\nElapsed Time: %3f' % (tend - tstart))