"""
This uses a special case of spherical trigonometry known as the laws of haversines to calculate the
great circle distance (as the crow flies)
"""
from math import radians, sin, cos, asin, sqrt
from constants import MEAN_RADIUS


class Haversine:
    def __init__(self, point_a: tuple, point_b: tuple):
        self.point_A_lat = radians(point_a[0])
        self.point_A_lon = radians(point_a[1])
        self.point_B_lat = radians(point_b[0])
        self.point_B_lon = radians(point_b[1])

    def calc_distance(self):
        delta_lat = self.point_B_lat - self.point_A_lat
        delta_lon = self.point_B_lon - self.point_A_lon
        haversine_val = (sin(delta_lat * 0.5) ** 2) + (sin(delta_lon * 0.5) ** 2) * \
            cos(self.point_A_lat) * cos(self.point_B_lat)
        dist = 2 * MEAN_RADIUS * asin(sqrt(haversine_val))
        print('Distance by Haversine: %.2f m or %.2f miles' % (dist, dist * 0.0006213712))
        return dist


if __name__ == '__main__':
    lc = Haversine((48.8534100, 2.3488000), (45.4642700, 9.1895100))
    lc.calc_distance()
