"""
This is different from the others discussed, as it assumes earth to be a oblate spheroid. This will give a more accurate
result for geodesic distance.
"""
from math import radians, sqrt, sin, cos, tan, atan, atan2
from constants import FLAT_CORRECT, EQTR_RADIUS, POLR_RADIUS


class Vincenty:
    max_iters = 200
    tolerance = 10 ** -12

    def __init__(self, point_a: tuple, point_b: tuple):
        self.point_A_lat = radians(point_a[0])
        self.point_A_lon = radians(point_a[1])
        self.point_B_lat = radians(point_b[0])
        self.point_B_lon = radians(point_b[1])

    def calc_distance(self):
        val_l = self.point_B_lon - self.point_A_lon
        val_u1 = atan((1 - FLAT_CORRECT) * tan(self.point_A_lat))
        val_u2 = atan((1 - FLAT_CORRECT) * tan(self.point_B_lat))

        val_sin_u1 = sin(val_u1)
        val_cos_u1 = cos(val_u1)
        val_sin_u2 = sin(val_u2)
        val_cos_u2 = cos(val_u2)

        val_lambda = val_l

        cos_sq_alpha = None
        val_sigma = None
        sin_sigma = None
        cos_sigma = None
        cos2_sigma_m = None
        for i in range(0, Vincenty.max_iters):
            i += 1

            sin_lambda = sin(val_lambda)
            cos_lambda = cos(val_lambda)
            sin_sigma = sqrt((val_cos_u2 * sin(val_lambda)) ** 2 +
                                  (val_cos_u1 * val_sin_u2 - val_sin_u1 * val_cos_u2 * cos_lambda) ** 2)
            cos_sigma = val_sin_u1 * val_sin_u2 + val_cos_u1 * val_cos_u2 * cos_lambda
            val_sigma = atan2(sin_sigma, cos_sigma)
            sin_alpha = (val_cos_u1 * val_cos_u2 * sin_lambda) / sin_sigma
            cos_sq_alpha = 1 - sin_alpha ** 2
            cos2_sigma_m = cos_sigma - ((2 * val_sin_u1 * val_sin_u2) / cos_sq_alpha)
            val_c = (FLAT_CORRECT / 16) * cos_sq_alpha * (4 + FLAT_CORRECT * (4 - 3 * cos_sq_alpha))
            val_lambda_prv = val_lambda
            val_lambda = val_l + (1 - val_c) * FLAT_CORRECT * sin_alpha * \
                (val_sigma + val_c * sin_sigma * (cos2_sigma_m + val_c * cos_sigma * (-1 + 2 * cos2_sigma_m ** 2)))

            val_diff = abs(val_lambda_prv - val_lambda)
            if val_diff <= Vincenty.tolerance:
                break

        val_u_sq = cos_sq_alpha * ((EQTR_RADIUS ** 2 - POLR_RADIUS ** 2) / POLR_RADIUS ** 2)
        val_a = 1 + (val_u_sq / 16384) * (4096 + val_u_sq * (-768 + val_u_sq * (320 - 175 * val_u_sq)))
        val_b = (val_u_sq / 1024) * (256 + val_u_sq * (-128 + val_u_sq * (74 - 47 * val_u_sq)))
        delta_sig = val_b * sin_sigma * (cos2_sigma_m + 0.25 * val_b * (
                    cos_sigma * (-1 + 2 * cos2_sigma_m ** 2) - (1 / 6) * val_b * cos2_sigma_m * (
                        -3 + 4 * sin_sigma ** 2) * (-3 + 4 * cos2_sigma_m ** 2)))
        dist = POLR_RADIUS * val_a * (val_sigma - delta_sig)
        print('Distance by Vincenty: %.2f m or %.2f miles' % (dist, dist * 0.0006213712))
        return dist


if __name__ == '__main__':
    lc = Vincenty((48.8534100, 2.3488000), (45.4642700, 9.1895100))
    lc.calc_distance()
