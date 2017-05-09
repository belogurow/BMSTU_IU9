import numpy

matrix_f = [[1,2,0,0], [2,-1,1,0], [0,1,-1,1], [0,0,1,1]]
matrix = [[1,2,0,0], [2,-1,1,0], [0,1,-1,1], [0,0,1,1]]

d_1 = [5,3,3,7]
d = [5,3,3,7]

def printMatrix():
	for j in range(len(matrix)):
		print(str(matrix[j]) + "  " + str(d[j]))
	print()

def maxElement(j_cur, i_cur):
	j = j_cur
	max = j_max = -1
	for j in range(j_cur, len(matrix)):
		if abs(matrix[j][i_cur]) > max and matrix[j][i_cur] != 0:
			max = matrix[j][i_cur]
			j_max = j

	if j_max != j_cur:
		matrix[j_max], matrix[j_cur] = matrix[j_cur], matrix[j_max]
		d[j_max], d[j_cur] = d[j_cur], d[j_max]


# ---------------------------------------------------
# reduction to triangular form
j_cur = i_cur = 0

while True:
	if j_cur == len(matrix) or i_cur == len(matrix[j_cur]):
		break

	maxElement(j_cur, i_cur)
	printMatrix()

	
	k = (matrix[j_cur][i_cur])
	if k == 0:
		i_cur += 1
		continue
	for i in range(i_cur, len(matrix[j_cur])):
		matrix[j_cur][i] /= k
	d[j_cur] /= k

	
	for j in range(j_cur + 1, len(matrix)):
		k = - matrix[j][i_cur] / matrix[j_cur][i_cur]
		for i in range(i_cur, len(matrix[j_cur])):
			matrix[j][i] += matrix[j_cur][i] * k
		d[j] += d[j_cur] * k

	printMatrix()

	j_cur += 1
	i_cur += 1

# ---------------------------------------------------
# the calculation of the vector x_2
x_2 = [0 for i in range(0, len(d))]

j_cur = len(x_2)-1
i_cur = len(matrix[j_cur]) - 1

for j in range(j_cur, -1, -1):
	sum = 0
	k = j + 1
	for i in range(i_cur + 1, len(matrix[j])):
		sum += matrix[j][i] * x_2[k]
		k += 1
	try:
		x_2[j] = (d[j] - sum)/matrix[j][i_cur]
	except ZeroDivisionError:
		print("Coefficient X[" + j + "] may be any")
		exit(0)
	i_cur -= 1

print("X_2: " + str(x_2))

# ---------------------------------------------------
# matrix product to find D_2
d_2 = [0 for i in range(0, len(d))]

for j in range(0, len(matrix_f)):
	sum = 0
	k = 0
	for i in range(0, len(matrix_f[j])):
		sum += matrix_f[j][i] * x_2[k]
		k += 1
	d_2[j] = sum

print("D_2: " + str(d_2))

# ---------------------------------------------------
# finding the difference D_2 - D = r

r = [d_2[i] - d_1[i] for i in range(0, len(matrix_f))]
print("r: " + str(r))
print()

# ---------------------------------------------------
# inverse matrix matrix_rev
matrix_rev = numpy.matrix(matrix_f).I
matrix_rev = matrix_rev.tolist()

# ---------------------------------------------------
# error e = matrix_rev * r
e = [0 for i in range(0, len(d))]

for j in range(0, len(matrix_rev)):
	sum = 0
	k = 0
	for i in range(0, len(matrix_rev[j])):
		sum += matrix_rev[j][i] * r[k]
		k += 1
	e[j] = sum

print("e: " + str(e))

# ---------------------------------------------------
# X = X_2 - e
x = [x_2[i] - e[i] for i in range(0, len(matrix_f))]
print("X: " + str(x))