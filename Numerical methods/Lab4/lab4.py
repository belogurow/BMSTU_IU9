import numpy
import math

phi = (1 + math.sqrt(5)) / 2


def bisection(a, b, eps, f):
	if f(a) == 0:
		return a
	if f(b) == 0:
		return b

	iteration = 0
	while (b - a) > 2 * eps:
		iteration += 1
		dx = (b - a)/2
		x = a + dx

		if numpy.sign(f(a)) != numpy.sign(f(x)):
			b = x
		else:
			a = x

	return [(a + b)/2, iteration]


def golden_section(a, b, eps, f):
	iteration = 0

	while (b - a) > 2 * eps:
		iteration += 1
		x_1 = b - (b - a) / phi
		x_2 = a + (b - a) / phi

		if numpy.sign(f(a)) != numpy.sign(f(x_1)):
			b = x_1
			continue

		if numpy.sign(f(x_1)) != numpy.sign(f(x_2)):
			a = x_1
			b = x_2
			continue

		if numpy.sign(f(x_2)) != numpy.sign(f(b)):
			a = x_2
			continue

	return [(a + b) / 2, iteration]


if __name__ == "__main__":
	f_x = numpy.poly1d([5, -8, -8, 5])
	eps = 0.001

	print("Bisection:")
	print(bisection(-1.5, 0, eps, f_x))
	print(bisection(0, 1.5, eps, f_x))
	print(bisection(1.5, 3, eps, f_x))

	print("\nGolden section:")
	print(golden_section(-1.5, 0, eps, f_x))
	print(golden_section(0, 1.5, eps, f_x))
	print(golden_section(1.5, 3, eps, f_x))
