def f(x_values):
	return 50 * (x_values[0] ** 2 - x_values[1]) ** 2 + 2 * (x_values[0] - 1) ** 2 + 10


def f_limit1(x_values):
	return x_values[0] ** 2 + x_values[1] ** 2 - 1 <= 0


def f_limit2(x_values):
	return -x_values[0] <= 0


def f_limit3(x_values):
	return -x_values[1] <= 0


def f_limit(x_values):
	return f_limit1(x_values) and f_limit2(x_values) and f_limit3(x_values)




if __name__ == "__main__":
	print(f([1, 1]))