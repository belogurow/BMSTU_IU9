import math


class FunValue:
	def __init__(self, x_values, fun_value):
		self.x_values = x_values
		self.fun_value = fun_value

	def __str__(self):
		return "{}, {}".format(self.x_values, self.fun_value)


class Cluster:
	def __init__(self, fun_values=None):
		if fun_values is None:
			fun_values = []
		self.funValues = [fun_values]

	def euclidean_distance(self, another_cluster):
		min_distance = float("INF")
		for i in self.funValues:
			distance = 0
			for j in another_cluster.funValues:
				distance += pow(i.fun_value - j.fun_value, 2)

			if distance < min_distance:
				min_distance = distance

		return math.sqrt(min_distance)

	def merge(self, another_cluster):
		self.funValues += another_cluster.funValues

	def min_by_value(self):
		min_fun_value = None

		for item in self.funValues:
			if min_fun_value is None:
				min_fun_value = item
			elif item.fun_value < min_fun_value.fun_value:
				min_fun_value = item

		assert min_fun_value is not None
		return min_fun_value

	def __str__(self):
		return "{}".format(self.funValues)
