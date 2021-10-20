from dateutil.parser import parse
from dateutil.relativedelta import relativedelta


class DateUtils:
    @staticmethod
    def split_date(start_date, end_date):
        arr_dt = []
        rd = relativedelta(parse(end_date), parse(start_date))
        delta = rd.years
        ndate = parse(start_date)
        for x in range(delta):
            odate = ndate + relativedelta(years=1)
            arr_dt.append((ndate, odate))
            ndate = odate

        return arr_dt


if __name__ == '__main__':
    print(DateUtils.split_date('2010-01-01T00:00:00.000Z', '2020-01-01T00:00:00.000Z'))