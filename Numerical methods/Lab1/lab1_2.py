from math import *

matrix_a = [[4, 1, 0, 0],
            [1, 4, 1, 0],
            [0, 1, 4, 1],
            [0, 0, 1, 4]]
matrix_b = [[0 for i in range(len(matrix_a))] 
	for j in range(len(matrix_a[0]))]

d = [5, 6, 6, 5]
x = [0 for i in range(len(d))]
x_n = [0 for i in range(len(d))]
c = [0 for i in range(len(d))]

e = 0.001

def test():
	sum = 0
	for i in range(len(x_n)):
		sum += (x_n[i] - x[i]) ** 2
	if sqrt(sum) <= e:
		return True
	return False


for j in range(len(matrix_b)):
	for i in range(len(matrix_b[j])):
		if i != j:
			matrix_b[j][i] = - matrix_a[j][i] / matrix_a[j][j]
	c[j] = d[j] / matrix_a[j][j]

#print(matrix_b)

x_test = 0
iteration = 0
while True:
	x_n = x.copy()
	iteration += 1
	for k in range(len(matrix_b)):
		j_cur = k
		sum = 0
		for i in range(len(matrix_b[j_cur])):
			if k > i:
				x_test = x_n[i]
			else:
				x_test = x[i]
			sum += matrix_b[j_cur][i] * x_test
		x_n[k] = sum + c[k]
	print(x_n)
	if test():
		break

	x = x_n

print(iteration)
