# Earth Radius (m)
FLAT_CORRECT = 1/298.257223563
EQTR_RADIUS = 6378137.0
POLR_RADIUS = (1 - FLAT_CORRECT) * EQTR_RADIUS
MEAN_RADIUS = (2 * EQTR_RADIUS + POLR_RADIUS)/ 3