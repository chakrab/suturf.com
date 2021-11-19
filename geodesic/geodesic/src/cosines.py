"""
This is a crude way of calculating the distance between two points on a sphere. The assumption in this case that
earth is a sphere. As compared in https://www.themathdoctors.org/distances-on-earth-2-the-haversine-formula/,
this is not very accurate for short distances (versus Haversine, which also assumes spherical earth).
"""
from math import radians, sin, cos, acos
from constants import MEAN_RADIUS


class LawOfCosines:
    def __init__(self, point_a: tuple, point_b: tuple):
        self.point_A_lat = radians(point_a[0])
        self.point_A_lon = radians(point_a[1])
        self.point_B_lat = radians(point_b[0])
        self.point_B_lon = radians(point_b[1])

    def calc_distance(self):
        ang_A = sin(self.point_A_lat) * sin(self.point_B_lat)
        ang_B = cos(self.point_A_lat) * cos(self.point_B_lat) * cos(self.point_B_lon - self.point_A_lon)
        ang_C = acos(ang_A + ang_B)
        dist = MEAN_RADIUS * ang_C
        print('Distance by Law of Cosines: %.4f m or %.4f miles' % (dist, dist * 0.0006213712))
        return dist


if __name__ == '__main__':
    # Paris to Milan
    lc = LawOfCosines((48.8534100, 2.3488000), (45.4642700, 9.1895100))
    lc.calc_distance()
