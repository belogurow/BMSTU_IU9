import math
import numpy as np

thA0 = [16.551, 16.810, 14.434, 20.891, 13.773, 14.739, 24.713, 10.127, 14.689, 13.047, 16.487, 14.345]
rA0 = [14.899, 14.292, 13.046, 18.696, 12.468, 13.313, 22.040, 9.278, 13.269, 11.833, 14.843, 12.968]
thA1 = [30.746, 22.558, 28.001, 32.958, 28.277, 36.763, 34.650, 33.590, 12.239, 35.848, 38.451, 18.573]
rA1 = [27.320, 20.155, 24.916, 29.255, 25.159, 32.398, 30.735, 29.808, 11.126, 31.784, 34.061, 16.668]
thB0 = [32.822, 25.314, 36.918, 46.677, 16.909, 21.889, 34.998, 23.285, 21.561, 37.778, 29.376, 32.822]
rB0 = [29.553, 22.567, 32.720, 41.259, 15.212, 19.569, 31.040, 20.791, 19.282, 33.472, 26.120, 29.553]
thB1 = [21.002, 40.022, 35.118, 20.283, 41.746, 40.458, 19.478, 22.974, 25.348, 25.336, 23.743, 29.751]
rB1 = [18.793, 35.436, 31.145, 18.164, 36.944, 35.817, 17.460, 21.353, 23.430, 22.586, 22.025, 27.282]
thC0 = [17.084, 29.096, 38.639, 23.690, 29.087, 21.993, 30.082, 18.776, 34.808, 26.192, 18.230, 37.085]
rC0 = [15.365, 25.876, 34.226, 21.145, 25.868, 20.494, 26.738, 17.263, 31.290, 23.751, 16.784, 33.283]
thC1 = [4.544, 17.519, 38.841, 37.324, 16.717, 40.099, 42.244, 22.099, 40.895, 17.519, 38.841, 37.324]
rC1 = [3.118, 16.162, 34.819, 33.492, 15.461, 35.920, 37.797, 20.170, 36.617, 16.162, 34.819, 33.492]

IIA0 = [[4.252, 5.158, 4.907], [4.349, 5.242, 4.995], [3.550, 4.458, 4.209], [5.704, 6.605, 6.357],
        [3.348, 4.239, 3.984], [3.654, 4.556, 4.309], [6.973, 7.872, 7.626], [2.113, 3.018, 2.762],
        [3.636, 4.532, 4.286], [3.089, 3.989, 3.739], [4.235, 5.135, 4.885], [3.521, 4.421, 4.171]]
IIA1 = [[4.902, 6.006, 5.252], [3.275, 4.278, 3.570], [4.369, 5.365, 4.663], [5.350, 6.659, 5.804],
        [4.419, 5.418, 4.713], [6.119, 7.117, 6.416], [5.695, 6.692, 5.991], [5.434, 6.475, 5.778],
        [1.209, 2.212, 1.504], [5.929, 6.929, 6.229], [6.450, 7.454, 6.750], [2.474, 3.474, 2.772]]
IIB0 = [[6.965, 7.195], [5.088, 5.318], [7.989, 8.219], [10.429, 10.659], [2.987, 3.217], [4.232, 4.462],
        [7.509, 7.739], [4.581, 4.811], [4.15, 4.38], [8.204, 8.434], [6.104, 6.334], [6.965, 7.195]]
IIB1 = [[4.07, 3.84], [8.825, 8.595], [7.599, 7.369], [3.8907, 3.6607], [9.256, 9.026], [8.934, 8.704],
        [3.689, 3.459], [4.563, 4.333], [5.156, 4.927], [5.154, 4.923], [4.755, 4.525], [6.257, 6.021]]
IIC0 = [[4.344, 5.574], [8.348, 9.578], [11.529, 12.759], [6.546, 7.776], [8.345, 9.575], [5.985, 7.211],
        [8.672, 9.907], [4.908, 6.138], [10.252, 11.482], [7.382, 8.61], [4.726, 5.956], [11.011, 12.241]]
IIC1 = [[1.164, 1.394], [5.489, 5.719], [12.594, 12.827], [12.091, 12.321], [5.222, 5.452], [13.016, 13.242],
        [10.211, 10.441, 10.526], [5.178, 5.404, 5.489], [9.873, 10.103, 10.188], [4.029, 4.252, 4.344],
        [9.36, 9.592, 9.675], [8.981, 9.211, 9.296]]

FDLA0 = [[2.215, 1.197, 0.841], [2.668, 1.423, 1.067], [2.543, 1.36, 1.004], [2.264, 1.221, 0.865],
         [2.71, 1.444, 1.088], [2.587, 1.382, 1.026], [1.864, 1.021, 0.665], [2.318, 1.248, 0.892],
         [2.194, 1.186, 0.83], [2.941, 1.56, 1.204], [3.392, 1.785, 1.429], [3.268, 1.723, 1.367],
         [1.696, 0.716, 0.936], [2.142, 0.939, 1.159], [2.014, 0.875, 1.095], [1.849, 0.793, 1.013],
         [2.3, 1.018, 1.238], [2.177, 0.956, 1.176], [3.509, 1.622, 1.842], [3.958, 1.847, 2.067],
         [3.785, 2.023, 1.819], [1.029, 0.644, 0.44], [1.481, 0.871, 0.667], [1.353, 0.807, 0.603],
         [1.79, 1.025, 0.821], [2.238, 1.249, 1.045], [2.115, 1.188, 0.984], [1.517, 0.888, 0.684],
         [1.967, 1.113, 0.909], [1.842, 1.051, 0.847], [2.09, 1.175, 0.971], [2.54, 1.4, 1.196], [2.415, 1.337, 1.133],
         [1.733, 0.996, 0.792], [2.183, 1.221, 1.017], [2.058, 1.159, 0.955]]
FDLA1 = [[2.54, 1.359, 1.003], [3.092, 1.635, 1.279], [2.715, 1.447, 1.091], [1.727, 0.952, 0.596],
         [2.228, 1.203, 0.847], [1.874, 1.026, 0.67], [2.274, 1.226, 0.87], [2.772, 1.475, 1.119],
         [2.421, 1.299, 0.943], [2.764, 1.471, 1.115], [3.419, 1.798, 1.442], [2.991, 1.585, 1.229],
         [2.232, 0.984, 1.204], [2.731, 1.234, 1.454], [2.379, 1.057, 1.277], [3.082, 1.409, 1.629],
         [3.581, 1.658, 1.878], [3.23, 1.483, 1.703], [2.87, 1.303, 1.523], [3.368, 1.552, 1.772], [2.968, 1.614, 1.41],
         [2.689, 1.475, 1.271], [3.21, 1.735, 1.531], [2.861, 1.561, 1.357], [0.577, 0.418, 0.214],
         [1.078, 0.669, 0.465], [0.724, 0.492, 0.288], [2.937, 1.598, 1.394], [3.437, 1.848, 1.644],
         [3.087, 1.673, 1.469], [3.197, 1.729, 1.525], [3.699, 1.98, 1.776], [3.347, 1.804, 1.6], [1.209, 0.735, 0.531],
         [1.709, 0.985, 0.781], [1.358, 0.809, 0.605]]
FDLB0 = [[3.511, 1.635, 1.819], [3.626, 1.693, 1.877], [2.572, 1.166, 1.35], [2.687, 1.224, 1.407],
         [4.023, 1.891, 2.075], [4.138, 1.949, 2.133], [5.243, 2.501, 2.685], [5.358, 2.559, 2.743],
         [1.522, 0.641, 0.825], [1.637, 0.698, 0.882], [2.144, 0.952, 1.136], [2.259, 1.01, 1.193],
         [3.783, 1.771, 1.955], [3.898, 1.829, 2.013], [2.319, 1.039, 1.223], [2.434, 1.097, 1.281],
         [2.103, 0.932, 1.115], [2.218, 0.989, 1.173], [4.13, 1.945, 2.129], [4.245, 2.003, 2.186], [3.08, 1.42, 1.604],
         [3.195, 1.478, 1.661], [3.511, 1.635, 1.819], [3.626, 1.693, 1.877]]
FDLB1 = [[2.063, 0.912, 1.095], [1.948, 0.854, 1.038], [4.441, 2.1, 2.284], [4.326, 2.043, 2.227],
         [3.828, 1.794, 1.978], [3.713, 1.736, 1.92], [1.973, 0.867, 1.051], [1.858, 0.809, 0.993],
         [4.656, 2.208, 2.392], [4.541, 2.151, 2.334], [4.495, 2.128, 2.311], [4.38, 2.07, 2.254], [1.873, 0.816, 1.0],
         [1.758, 0.759, 0.943], [2.31, 1.035, 1.219], [2.195, 0.977, 1.161], [2.606, 1.183, 1.367],
         [2.492, 1.126, 1.31], [2.605, 1.183, 1.366], [2.49, 1.125, 1.309], [2.406, 1.083, 1.267],
         [2.291, 1.025, 1.209], [3.157, 1.458, 1.642], [3.039, 1.399, 1.583]]
FDLC0 = [[2.2, 0.98, 1.164], [2.815, 1.288, 1.471], [4.202, 1.981, 2.165], [4.817, 2.289, 2.472], [5.793, 2.776, 2.96],
         [6.408, 3.084, 3.268], [3.301, 1.531, 1.714], [3.916, 1.838, 2.022], [4.201, 1.98, 2.164],
         [4.816, 2.288, 2.472], [3.021, 1.39, 1.574], [3.634, 1.697, 1.881], [4.364, 2.062, 2.246],
         [4.982, 2.371, 2.555], [2.482, 1.121, 1.305], [3.097, 1.429, 1.612], [5.154, 2.457, 2.641],
         [5.769, 2.765, 2.948], [3.719, 1.74, 1.923], [4.333, 2.047, 2.23], [2.391, 1.076, 1.259],
         [3.006, 1.383, 1.567], [5.534, 2.647, 2.831], [6.149, 2.954, 3.138]]
FDLC1 = [[0.671, 0.425, 0.069], [0.725, 0.243, 0.426], [2.773, 1.266, 1.45], [2.888, 1.324, 1.508],
         [6.325, 3.043, 3.226], [6.442, 3.101, 3.285], [6.074, 2.917, 3.101], [6.189, 2.974, 3.158],
         [2.639, 1.2, 1.383], [2.754, 1.257, 1.441], [6.536, 3.148, 3.332], [6.649, 3.205, 3.388],
         [5.134, 2.447, 2.631], [5.249, 2.504, 2.688], [5.291, 2.526, 2.709], [2.617, 1.189, 1.372],
         [2.73, 1.245, 1.429], [2.773, 1.266, 1.45], [4.965, 2.362, 2.546], [5.08, 2.42, 2.604], [5.122, 2.441, 2.625],
         [2.043, 0.901, 1.085], [2.043, 0.901, 1.085], [2.11, 1.112, 1.03], [2.156, 1.135, 1.053],
         [4.664, 2.389, 2.307], [4.78, 2.447, 2.365], [4.822, 2.468, 2.386], [4.475, 2.294, 2.212], [4.59, 2.352, 2.27]]

F_30 = 24
F_90 = 32
F_24 = 110
F_72 = 310

D_30 = 12
D_90 = 16
D_24 = 55
D_72 = 310


def x_avg(x_values):
	return (1 / len(x_values)) * sum(x_values)


def average(x_30, x_90, x_24, x_72):
	return x_30 / 0.5 + x_90 / 1.5 + x_24 / 24 + x_72 / 72


def dispersion(x_values):
	x_average = x_avg(x_values)
	new_values = [(x - x_average) ** 2 for x in x_values]

	return (1 / len(new_values)) * sum(new_values)


def alpha_betta(psi_1, psi_2):
	psi_1 = list(map(math.log, psi_1))
	psi_2 = list(map(math.log, psi_2))

	betta = math.sqrt(dispersion(psi_1) / dispersion(psi_2))
	alpha = math.exp(x_avg(psi_1) - betta * x_avg(psi_2))

	return alpha, betta


def get_sections(list_x, max_x=50):
	sections = [i * 5 for i in range(11)]

	# print(sections)

	result = []
	for i in range(len(sections) - 1):
		result.append(list(filter(lambda x: sections[0] < x < sections[i + 1], list_x)))

	return result


def partition(data, sections_number):
	return np.array_split(np.asarray(data), sections_number)


if __name__ == "__main__":
	# 1) -------------------
	print("\t--1--")
	# work
	psi_1w = thA0 + thB0 + thC0
	psi_2w = rA0 + rB0 + rC0
	alpha, betta = alpha_betta(psi_1w, psi_2w)

	print("psi_1: a =", alpha, "b =", betta)

	# control
	psi_1c = thA1 + thB1 + thC1
	psi_2c = rA1 + rB1 + rC1

	# _ = *
	psi_2_ = psi_2c
	psi_1_ = [alpha * (psi_2_[i] ** betta) for i in range(len(psi_2_))]

	psi_1c.sort()
	psi_1_.sort()

	print(get_sections(psi_1c))
	print(get_sections(psi_1_))

	psi_1c_len = [len(data) / 36 for data in get_sections(psi_1c)]
	psi_1__len = [len(data) / 36 for data in get_sections(psi_1_)]

	result = [abs(psi_1c_len[i] - psi_1__len[i]) for i in range(len(psi_1c_len))]

	if max(result) < 0.05:
		print("Ok")
		print(max(result))
		print()

	# 2) --------------------
	print("\t--2--")
	I0 = IIA0 + IIB0 + IIC0

	# work
	ksi_w1 = []
	for value_list in I0:
		# ksi_w1.append(sum(value_list))
		ksi_w1.append(sum(map(int, value_list)))

	ksi_w2 = []
	for value_list in I0:
		if len(value_list) == 3:
			ksi_w2.append(value_list[0] + value_list[1])
		else:
			ksi_w2.append(value_list[0])

	ksi_w3 = []
	for value_list in I0:
		ksi_w3.append(value_list[len(value_list) - 1])

	# control
	ksi_c1 = []
	for value_list in I0:
		# ksi_c1.append(sum(value_list))
		ksi_c1.append(sum(map(int, value_list)))

	ksi_c2 = []
	for value_list in I0:
		if len(value_list) == 3:
			ksi_c2.append(value_list[0] + value_list[1])
		else:
			ksi_c2.append(value_list[0])

	I1 = IIA1 + IIB1 + IIC1

	ksi_c3 = []
	for value_list in IIA1:
		ksi_c3.append(value_list[len(value_list) - 1])

	a_12, b_12 = alpha_betta(ksi_w1, ksi_w2)
	a_23, b_23 = alpha_betta(ksi_w2, ksi_w3)

	a, b = alpha_betta(ksi_w1, ksi_w3)
	print("a_12 = {}, b_12 = {}".format(a_12, b_12))
	print("a_23 = {}, b_23 = {}".format(a_23, b_23))

	ksi_calc3 = [(x ** (1 / b_23)) / a_23 for x in ksi_c2]
	ksi_temp = [a_23 * (x ** b_23) for x in ksi_calc3]
	ksi_calc1 = [a_12 * (x ** b_12) for x in ksi_temp]

	ksi_c1.sort()
	ksi_calc1.sort()
	dif = []
	n = len(ksi_w1)

	for i in range(0, 30, 10):
		func_control = len([x for x in ksi_c1 if x < i]) / n
		func_calc = len([x for x in ksi_calc1 if x < i]) / n
		dif.append(abs(func_control - func_calc))
	print("dif ", max(dif))
	print()

	# 3) --------------------
	print("\t--3--")
	# work
	FDL0 = np.asarray(FDLA0 + FDLB0 + FDLC0)
	# FDL1 = FDLA1 + FDLB1 + FDLC1

	# F - взрослый
	# D - десткий
	# L - льготный

	count_tickets_F = sum(FDL0[:, 0])
	count_tickets_D = sum(FDL0[:, 1])
	count_tickets_L = sum(FDL0[:, 2])

	avg_F = average(F_30, F_90, F_24, F_72)
	avg_D = avg_L = average(D_30, D_90, D_24, D_72)

	cost_all_tickets = avg_F * count_tickets_F + avg_D * count_tickets_D + avg_L * count_tickets_L
	average_cost = cost_all_tickets / (count_tickets_F + count_tickets_D + count_tickets_L)
	print("Average - {}".format(average_cost / 3))

# print()
# a, b = alpha_betta(thA0, rA0)
# print("thA0, rA0: a =", a, "b =", b)
# a, b = alpha_betta(thA1, rA1)
# print("thA1, rA1: a =", a, "b =", b)
# a, b = alpha_betta(thB0, rB0)
# print("thB0, rB0: a =", a, "b =", b)
# a, b = alpha_betta(thB1, rB1)
# print("thB1, rB1: a =", a, "b =", b)
# a, b = alpha_betta(thC0, rC0)
# print("thC0, rC0: a =", a, "b =", b)
# a, b = alpha_betta(thC1, rC1)
# print("thC1, rC1: a =", a, "b =", b)