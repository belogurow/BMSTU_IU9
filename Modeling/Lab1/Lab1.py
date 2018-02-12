import math

radius = 0.05
density = 11340
v_0 = 75
g = 9.8
alpha = 30
windage = 0.15


def calc_mass(radius, density):
	volume = 4/3 * math.pi * (radius ** 3)
	return volume * density


# windage - сопротивление воздуха
def calc_betta(density, windage, radius):
	return windage * density * math.pi * (radius ** 2) / 2


def calculate_rk(speed, radius, density, angle, windage):
	# h - step
	h = 0.1
	mass = calc_mass(radius, density)
	betta = calc_betta(density, windage, radius)

	speed = [(calc_speed_x(angle, 0), calc_speed_y(angle, 0))]
	coordinates = [(0, 0)]

	while coordinates[0][0] >= 0:
		cur_speed = speed.pop()
		cur_speed_x = cur_speed[0]
		cur_speed_y = cur_speed[1]

		k1_x = h * calc_speed_x_der(betta, mass, cur_speed_x, cur_speed_y)
		k1_y = h * calc_speed_y_der(betta, mass, cur_speed_x, cur_speed_y)

		k2_x = h * calc_speed_x_der(betta, mass, cur_speed_x + k1_x / 2, cur_speed_y + k1_y / 2)
		k2_y = h * calc_speed_y_der(betta, mass, cur_speed_x + k1_x / 2, cur_speed_y + k1_y / 2)

		k3_x = h * calc_speed_x_der(betta, mass, cur_speed_x + k2_x / 2, cur_speed_y + k2_y / 2)
		k3_y = h * calc_speed_y_der(betta, mass, cur_speed_x + k2_x / 2, cur_speed_y + k2_y / 2)

		k4_x = h * calc_speed_x_der(betta, mass, cur_speed_x + k3_x, cur_speed_y + k3_y)
		k4_y = h * calc_speed_y_der(betta, mass, cur_speed_x + k3_x, cur_speed_y + k3_y)

		cur_speed_x += (k1_x + 2 * k2_x + 2 * k3_x + k4_x) / 6
		cur_speed_y += (k1_y + 2 * k2_y + 2 * k3_y + k4_y) / 6

		speed.append((cur_speed_x, cur_speed_y))

		cur_coord = coordinates.pop()
		cur_coord_x = cur_coord[0] + h * cur_speed_x
		cur_coord_y = cur_coord[1] + h * cur_speed_y

		coordinates.append((cur_coord_x, cur_coord_y))

		print(cur_coord_x, cur_coord_y)



def calc_speed_x(angle, t):
	"""
	Calculate speed for X-axis
	:param angle: angle in degrees
	:param t: time
	:return: speed
	"""
	return v_0 * math.cos(math.radians(angle))


def calc_speed_y(angle, t):
	"""
	Calculate speed for Y-axis
	:param angle: angle in degrees
	:param t: time
	:return: speed
	"""
	return v_0 * math.sin(math.radians(angle)) - g * t


def calc_speed_x_der(betta, mass, speed_x, speed_y):
	return -betta * speed_x * math.sqrt(speed_x + speed_y) / mass


def calc_speed_y_der(betta, mass, speed_x, speed_y):
	return -betta * speed_y * math.sqrt(speed_x + speed_y) / mass - g


if __name__ == "__main__":
	calculate_rk(v_0, radius, density, alpha, windage)