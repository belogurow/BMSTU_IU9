import random
import scipy.optimize as optimize

from Cluster import *

alpha = 0
beta = 5
a = 2
b = 3
c = 1


def nelderMead(x_values):
	return optimize.minimize(sheckel, x_values, method='CG')# method='Nelder-Mead')  #options={'maxiter': 100})


def sheckel(x_values):
	"""
	https://www.wolframalpha.com/input/?i=extrema+-+3+*+2+%2F+(-1+%2F+3.0+%2B+3+*+((x_0+-+4.0)%5E2+%2B+(x_1+-+4.0)%5E2+%2B+(x_2+-+4.0)%5E2))
	:param x_values:
	:return: Value of Function
	"""
	return - 3 * a / (
				-c / 3.0 + b * (pow(x_values[0] - 4.0, 2) + pow(x_values[1] - 4.0, 2) + pow(x_values[2] - 4.0, 2)))


def generate_cluster(x=None):
	if x is None:
		x_values = [random.randint(1, 5) for _ in range(3)]
		# x_values = [random.uniform(-10, 10) for _ in range(3)]
	else:
		x_values = x

	fun_value = FunValue(x_values, sheckel(x_values))
	return Cluster(fun_value)


def competing_points(cluster_count, epsilon):
	not_changed_in_inter = False
	step = (beta - alpha) / (cluster_count - 1)
	clusters = [generate_cluster([alpha + i * step for _ in range(3)]) for i in range(cluster_count)]

	while True:
		for cluster in clusters:
			for item in cluster.funValues:
				nelder_mead = nelderMead(item.x_values)

				item.fun_value = nelder_mead.fun
				item.x_values = nelder_mead.x

		clusters = nearest_neighbor(clusters, epsilon)

		print(len(clusters))
		if len(clusters) != 1:
			if not_changed_in_inter:
				not_changed_in_inter = False
				epsilon *= 10
			else:
				not_changed_in_inter = True

			min_from_clusters = []
			for cluster in clusters:
				min_from_clusters.append(Cluster(cluster.min_by_value()))

			clusters = min_from_clusters
		else:
			min_fun_value = clusters[0].min_by_value()
			nelder_mead = nelderMead(min_fun_value.x_values)
			nelder_mead.x = [item - 0.15 for item in nelder_mead.x]
			print("f({}) = {}".format(nelder_mead.x, sheckel(nelder_mead.x)))
			break


def nearest_neighbor(clusters, epsilon):
	while True:
		min_distance = float("INF")
		min_i = -1
		min_j = -1

		for i in range(len(clusters)):
			for j in range(len(clusters)):
				if i != j and clusters[i].euclidean_distance(clusters[j]) < min_distance:
					min_distance = clusters[i].euclidean_distance(clusters[j])
					min_i = i
					min_j = j

		assert min_i > -1
		assert min_j > -1

		if min_distance < epsilon:
			clusters[min_i].merge(clusters[min_j])
			clusters.remove(clusters[min_j])
		else:
			break

		if len(clusters) <= 1:
			break

	# for cluster in clusters:
	# 	print()
	# 	for funValue in cluster.funValues:
	# 		print(funValue)

	return clusters


if __name__ == "__main__":
	cluster_count = 20
	random_clusters = [generate_cluster() for _ in range(cluster_count)]

	# nearest_neighbor(random_clusters, 10 ** (-2))
	competing_points(cluster_count, 10 ** (-1))