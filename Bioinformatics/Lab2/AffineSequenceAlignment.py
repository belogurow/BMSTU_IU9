import numpy

# читаем последовательности из файлов
seq_1 = open("sequence1").read()
seq_2 = open("sequence2").read()

# задаем значения
class Constant:
	MATCH = 4
	MISMATCH = -1
	GAP_START = -10
	GAP_EXTEND = -2
	LEN_SEQ_1 = len(seq_1) + 1
	LEN_SEQ_2 = len(seq_2) + 1
	MIN = -float("inf")


# определим метод для инициализации матриц
def init_matrix(matrix_name='A'):
	matrix = numpy.zeros(shape=(Constant.LEN_SEQ_1, Constant.LEN_SEQ_2))

	if matrix_name == 'A':
		matrix[0, :] = [i * Constant.MIN for i in range(Constant.LEN_SEQ_2)]
		matrix[:, 0] = [i * Constant.MIN for i in range(Constant.LEN_SEQ_1)]

	elif matrix_name == 'B':
		for i in range(Constant.LEN_SEQ_1):
			matrix[i, :] = [Constant.GAP_START + Constant.GAP_EXTEND * j for j in range(Constant.LEN_SEQ_2)]
		matrix[:, 0] = [i * Constant.MIN for i in range(Constant.LEN_SEQ_1)]

	elif matrix_name == 'C':
		for i in range(Constant.LEN_SEQ_2):
			matrix[:, i] = [Constant.GAP_START + Constant.GAP_EXTEND * j for j in range(Constant.LEN_SEQ_1)]
		matrix[0, :] = [i * Constant.MIN for i in range(Constant.LEN_SEQ_2)]

	matrix[0, 0] = 0
	return matrix


# сравниваем две буквы
def compare(letter_1, letter_2):
	if letter_1 == letter_2:
		return Constant.MATCH
	else:
		return Constant.MISMATCH


if __name__ == '__main__':
	''' 
	Работа алгоритма основана на трех матрицах X, Y, M
	поэтому необходимо их инициализировать:
	'''
	matrix_A = init_matrix('A')
	matrix_B = init_matrix('B')
	matrix_C = init_matrix('C')

	# print(matrix_A)
	# print(matrix_B)
	# print(matrix_C)

	# заполним матрицы значениями
	for i in range(1, Constant.LEN_SEQ_1):
		for j in range(1, Constant.LEN_SEQ_2):
			matrix_B[i][j] = max(Constant.GAP_START + Constant.GAP_EXTEND + matrix_A[i][j - 1],
			                     Constant.GAP_EXTEND + matrix_B[i][j - 1],
			                     Constant.GAP_START + Constant.GAP_EXTEND + matrix_C[i][j - 1])

			matrix_C[i][j] = max(Constant.GAP_START + Constant.GAP_EXTEND + matrix_A[i - 1][j],
			                     Constant.GAP_START + Constant.GAP_EXTEND + matrix_B[i - 1][j],
			                     Constant.GAP_EXTEND + matrix_C[i - 1][j])

			matrix_A[i][j] =  + max(compare(seq_1[i - 1], seq_2[j - 1]) + matrix_A[i - 1, j - 1],
			                                                           matrix_B[i, j],
			                                                           matrix_C[i, j])
	# начинаем идти с нижней правой ячейки
	align1, align2 = '', ''
	i, j = len(seq_1), len(seq_2)
	while i > 0 and j > 0:
		if matrix_A[i][j] == matrix_A[i - 1][j - 1] + compare(seq_1[i - 1], seq_2[j - 1]):
			align1 += seq_1[i - 1]
			align2 += seq_2[j - 1]
			i -= 1
			j -= 1
		elif matrix_A[i][j] == matrix_B[i][j]:
			align1 += '-'
			align2 += seq_2[j - 1]
			j -= 1
		elif matrix_A[i][j] == matrix_C[i][j]:
			align1 += seq_1[i - 1]
			align2 += '-'
			i -= 1

	print(align1[::-1])
	print(align2[::-1])
