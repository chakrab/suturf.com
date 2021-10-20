import asyncio
from dateutils import DateUtils
from timeit import default_timer as timer
from mongocomn import MongoCommon


class AsyncCaller:
    async def fetch_data(self, start_date, end_date, que, num):
        dct = {}
        client = None
        try:
            print('Starting task [%d]' % num)
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
            await que.put(dct)
            que.task_done()
        except Exception as e:
            print(e)
        if client:
            MongoCommon.close_connection(client)

    async def fetch_data_as(self, start_date, end_date):
        processes = []
        queue = asyncio.Queue()
        dtarr = DateUtils.split_date(start_date, end_date)
        cnt = 0
        for arr in dtarr:
            cnt += 1
            p = asyncio.create_task(self.fetch_data(arr[0], arr[1], queue, cnt))
            processes.append(p)
        await asyncio.gather(*processes)
        await queue.join()

        cfind = []
        while not queue.empty():
            cfind.append(await queue.get())
        return cfind


if __name__ == '__main__':
    st = AsyncCaller()
    tstart = timer()
    try:
        cfind = asyncio.run(st.fetch_data_as(
            '2011-01-01T00:00:00.000Z',
            '2020-01-01T00:00:00.000Z'
        ))
        print(cfind)
    except Exception as e:
        print(e)

    tend = timer()
    print('\nElapsed Time: %3f' % (tend - tstart))