import numpy as np
from optimization_methods import optimize_multidimential_function \
	as minimize


def penalty_method(f, x0, limit_funcs, r0, z, eps, disp=False):
	"""
    Solving of conditional minimization problem using \
    penalty method
    ---------
    Arguments
    ---------
    f : function
        Function for minimization
    x0 : list
        Start point
    limit_funcs : dict
        equalities : list,
        inequalities : list
    r0 : float
        Start value of penalty. Must be > 0
    z : float
        Coefficient of mult r0
    eps : float
        Tolerance. Must be > 0
    """

	def psi_(f, x):
		return min(0, f(x)) ** 2
		# return -min(0, f(x))

	def psi(f, x):
		return f(x) ** 2
		# return abs(f(x))

	x = np.array([x0], dtype=float)
	r = np.array([r0], dtype=float)

	k = 0
	while k < 1000:
		if disp:
			print('\nIteration: %d' % k, end='\n\n')

		def P(x):
			def sum_fvals(f, funclist):
				return np.sum([f(func, x[:-1]) for func in funclist])

			return x[-1] * (sum_fvals(psi_, limit_funcs['inequalities']) +
			                sum_fvals(psi, limit_funcs['equalities']))

		def F(x):
			return f(x[:-1]) + P(x)

		_x = [x[k][i] for i in range(np.size(x, 1))]
		_x.append(r[k])
		results = None
		try:
			results = minimize(F, 'fr', _x,
			                   [1 for i in range(len(_x))],
			                   eps, True)
		except Exception as error:
			print(error)
			raise Exception(
				'Failed to find minimum for multidimential function')
		if disp:
			print('Minimum = ', results[0])

		_x = [results[0][i] for i in range(np.size(results[0]))]
		_x[-1] = r[k]
		penalty = P(_x)
		if disp:
			print('Penalty = %f' % penalty)

		x_min = results[0][:-1]
		if penalty < eps:
			return (x_min, f(x_min))
		else:
			r = np.append(r, z * r[k])
			x = np.append(x, [x_min], 0)
			k += 1


def f(x_values):
	# return x[0]**2 + x[1]**2  # 1.1
	# return ((x[0] + 1)**3) / 3 + x[1]  # 2.1
	# return 4*x[0]**2 + 4*x[0] + x[1]**2 - 8*x[1] + 5  # 2.7
	# return x[0]**2 + x[1]**2  # 2.16
	return 50 * (x_values[0] ** 2 - x_values[1]) ** 2 + 2 * (x_values[0] - 1) ** 2 + 10


def main():
	results = None
	try:
		lim_funcs = {
			'inequalities': [
				# lambda x: x[0] - 1,  # 2.1
				# lambda x: x[1]       # 2.1
				# lambda x: -x[0]**2 + x[1]**2 + 1,  # 2.16
				# lambda x: x[0] - 2                # 2.16
				lambda x_values: x_values[0] ** 2 + x_values[1] ** 2 - 1,
				lambda x_values: -x_values[0],
				lambda x_values: -x_values[1]
			],
			'equalities': [
				# lambda x: 2 * x[0] - x[1] - 6  # 2.7
			]
		}
		results = penalty_method(f, [0, 1], lim_funcs,
		                         10000, 1, 0.01,
		                         True)
	except Exception as error:
		print(error)
		print('ERROR: Failed to find minimum')
	else:
		print('x_min = ', results[0])
		print('f(x_min) = %f' % results[1])


if __name__ == '__main__':
	main()
