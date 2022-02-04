import string
from cosines import LawOfCosines
from haversine import Haversine
from vincenty import Vincenty


class CompareGeodesics:
    def run_all_algorithm(self, s1: tuple, s2: tuple, msg: string):
        print('\nCalculating distance between %s [%s -> %s]:' % (msg, s1, s2))
        lc = LawOfCosines(s1, s2)
        lc.calc_distance()
        hv = Haversine(s1, s2)
        hv.calc_distance()
        vc = Vincenty(s1, s2)
        vc.calc_distance()


if __name__ == '__main__':
    cg = CompareGeodesics()
    cg.run_all_algorithm((48.8534100, 2.3488000), (45.4642700, 9.1895100), 'Milan & Paris')
    cg.run_all_algorithm((40.712776, -74.005974), (28.613939, 77.209023), 'NY & New Delhi')
    cg.run_all_algorithm((53.726669, -127.647621), (24.453884, 54.377342), 'BC & Abu Dhabi')
    cg.run_all_algorithm((88.363895, 22.572646), (52.520008, 13.404954), 'Kolkata & Berlin')
    cg.run_all_algorithm((37.906193, -0.023559), (174.763331, -36.848460), 'Kenya & Auckland')