import math


def f(x, y, z):
	return math.exp(x)+y-z


def g(x, y, z):
	return z


def rk(f, a, b, n, isPrint=False):
	h = (b-a) / n
	x_n = [a + i * h for i in range(n+1)]
	y = []
	y_cur = z_cur = 1

	for x in x_n:
		y.append(y_cur)
		if isPrint:
			print(str(y_cur) + " " + str(math.exp(x) - y_cur))

		k1 = h * f(x, y_cur, z_cur)
		k2 = h * f(x + 0.5 * h, y_cur + 0.5 * k1, z_cur + 0.5 * k1)
		k3 = h * f(x + 0.5 * h, y_cur + 0.5 * k2, z_cur + 0.5 * k2)
		k4 = h * f(x + h, y_cur + k3, z_cur + k3)

		y_cur += (k1 + 2 * k2 + 2 * k3 + k4) / 6
		z_cur += (k1 + 2 * k2 + 2 * k3 + k4) / 6


	return y

if __name__ == "__main__":
	result = [rk(f, 0, 1, 20, True), rk(g, 0, 1, 20)]

	print()
	for i in range(len(result[0])):
		print(str(result[0][i]) + " " + str(result[1][i]))

