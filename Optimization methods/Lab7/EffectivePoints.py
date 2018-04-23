import numpy as np
import math as mt
from scipy.optimize import minimize


def function_cut(a):
	if a > 0:
		return a
	return 0


def function_1(x):
	return x[0] ** 2 + x[1] ** 2


def function_2(x):
	return 4 * (x[0] - 5) ** 2 + 2 * (x[1] - 6) ** 4


def cond_1(x):
	return x[1] - x[0] + 1


def cond_2(x):
	return x[0] + x[1] - 2


def effective_points(w1, w2, x1, x2):
	def F(x, r):
		return (w1 * (function_1(x) - function_1(x1)) + w2 * (function_2(x) - function_2(x2)) + (r / 2.) * (
				(function_cut(cond_1(x)) ** 2) + (function_cut(cond_2(x)) ** 2)))

	def P(x, r):
		return (r / 2.) * ((function_cut(cond_1(x)) ** 2) + (function_cut(cond_2(x)) ** 2))

	r0 = 1.0
	accelerator = 2.0
	epsilon = 10 ** -3
	k = 0

	def barrier_method(r, k, x0):
		res = (minimize(lambda x: F(x, r), x0, method='CG'))
		newx = res.x

		if np.fabs(P(newx, r)) <= epsilon:
			return newx
		else:
			return barrier_method(accelerator * r, k + 1, newx)

	result = barrier_method(r0, k, [0.0, 0.0])
	print('w1 = {}, w2 = {}, \nx = {}, f_1(x) = {:.2f}, f_2(x) = {:.2f}\n'.format(w1, w2, result, function_1(result), function_2(result)) )


if __name__ == '__main__':
	start_point = [0.0, 0.0]

	min_1 = minimize(function_1, start_point, method='CG').x
	print(min_1)
	min_2 = minimize(function_2, start_point, method='CG').x
	print(min_2)

	for i in range(1, 5):
		w1 = i
		w2 = 1.0 / i
		effective_points(w1, w2, min_1, min_2)
