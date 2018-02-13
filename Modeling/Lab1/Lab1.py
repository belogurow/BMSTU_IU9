import math

radius = 0.05
density = 11340
air_density = 1.29
v_0 = 50
g = 9.81
alpha = 30


def calc_mass(radius, density):
	"""
	Calculate the mass of an object using its radius and density
	:param radius: radius of object
	:param density: density of object
	:return: mass of object
	"""
	volume = 4/3 * math.pi * (radius ** 3)
	return volume * density


def calc_betta(radius, windage=0.15, air_density=1.29):
	"""
	Calculate coefficient of air resistance
	:param radius: radius of object
	:param windage: windage(= 0.15)
	:param air_density: density of air
	:return: coefficient of air resistance
	"""
	return windage * air_density * math.pi * (radius ** 2) / 2


def galilee_model(speed, angle):
	"""
	Calculate X-coord of Galilee model
	:param speed: start speed of object
	:param angle: start angle in degrees
	:return: x-coord
	"""
	angle_rad = math.radians(angle)
	return 2 * math.tan(angle_rad) * (math.cos(angle_rad) * speed) ** 2 / g


def newton_model(speed, radius, density, angle, h=0.01, is_betta_need=True):
	"""
	Calculate X-coord of Newton model using Runge–Kutta method
	:param speed: start speed of object
	:param radius: radius of object
	:param density: density of object
	:param angle: start angle in degrees
	:param h: step
	:param is_betta_need:
	:return: x-coord
	"""
	mass = calc_mass(radius, density)

	if is_betta_need:
		betta = calc_betta(radius)
	else:
		betta = 0

	speed = [(calc_speed_x(speed, angle, 0), calc_speed_y(speed, angle, 0))]
	coordinates = [(0, 0)]

	while coordinates[-1][1] >= 0:
		cur_speed = speed[-1]
		cur_speed_x = cur_speed[0]
		cur_speed_y = cur_speed[1]

		k1_vx = h * calc_speed_x_der(betta, mass, cur_speed_x, cur_speed_y)
		k1_vy = h * calc_speed_y_der(betta, mass, cur_speed_x, cur_speed_y)

		k2_vx = h * calc_speed_x_der(betta, mass, cur_speed_x + k1_vx / 2, cur_speed_y + k1_vy / 2)
		k2_vy = h * calc_speed_y_der(betta, mass, cur_speed_x + k1_vx / 2, cur_speed_y + k1_vy / 2)

		k3_vx = h * calc_speed_x_der(betta, mass, cur_speed_x + k2_vx / 2, cur_speed_y + k2_vy / 2)
		k3_vy = h * calc_speed_y_der(betta, mass, cur_speed_x + k2_vx / 2, cur_speed_y + k2_vy / 2)

		k4_vx = h * calc_speed_x_der(betta, mass, cur_speed_x + k3_vx / 2, cur_speed_y + k3_vy / 2)
		k4_vy = h * calc_speed_y_der(betta, mass, cur_speed_x + k3_vx / 2, cur_speed_y + k3_vy / 2)

		cur_speed_x += (k1_vx + 2 * k2_vx + 2 * k3_vx + k4_vx) / 6
		cur_speed_y += (k1_vy + 2 * k2_vy + 2 * k3_vy + k4_vy) / 6

		speed.append((cur_speed_x, cur_speed_y))

		cur_coord = coordinates[-1]
		cur_coord_x = cur_coord[0] + h * cur_speed_x
		cur_coord_y = cur_coord[1] + h * cur_speed_y

		coordinates.append((cur_coord_x, cur_coord_y))

	return coordinates[-1][0]


def calc_speed_x(speed, angle, t):
	"""
	Calculate speed for X-axis
	:param angle: angle in degrees
	:param t: time
	:return: speed
	"""
	return speed * math.cos(math.radians(angle))


def calc_speed_y(speed, angle, t):
	"""
	Calculate speed for Y-axis
	:param angle: angle in degrees
	:param t: time
	:return: speed
	"""
	return speed * math.sin(math.radians(angle)) - g * t


def calc_speed_x_der(betta, mass, speed_x, speed_y):
	"""
	Calculate function for speed_x Runge–Kutta method
	:param betta: coefficient of air resistance
	:param mass: mass of object
	:param speed_x: speed of x-axis
	:param speed_y: speed of y-axis
	:return: function
	"""
	return -betta * speed_x * math.sqrt(speed_x ** 2 + speed_y ** 2) / mass


def calc_speed_y_der(betta, mass, speed_x, speed_y):
	"""
	Calculate function for speed_y Runge–Kutta method
	:param betta: coefficient of air resistance
	:param mass: mass of object
	:param speed_x: speed of x-axis
	:param speed_y: speed of y-axis
	:return: function
	"""
	return -betta * speed_y * math.sqrt(speed_x ** 2 + speed_y ** 2) / mass - g


if __name__ == "__main__":
	print("Галилей\n", galilee_model(v_0, alpha))
	print("Ньютон\n", newton_model(v_0, radius, density, alpha, is_betta_need=True))
	print("Ньютон b=0\n", newton_model(v_0, radius, density, alpha, is_betta_need=False))