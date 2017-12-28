import numpy

import BlosumReader
import Util


# задаем значения
class Constant:
	def __init__(self, GAP_START, GAP_EXTEND, BLOSUM_MATRIX, dic, seq_1, seq_2):
		self.GAP_START = GAP_START
		self.GAP_EXTEND = GAP_EXTEND
		self.LEN_SEQ_1 = len(seq_1) + 1
		self.LEN_SEQ_2 = len(seq_2) + 1
		self.MIN = -float("inf")
		self.BLOSUM_MATRIX = BLOSUM_MATRIX
		self.LETTER_DICT = dic


# определим метод для инициализации матриц
def init_matrix(matrix_name, constant):
	matrix = numpy.zeros(shape=(constant.LEN_SEQ_1, constant.LEN_SEQ_2))

	if matrix_name == 'A':
		matrix[0, :] = [i * constant.MIN for i in range(constant.LEN_SEQ_2)]
		matrix[:, 0] = [i * constant.MIN for i in range(constant.LEN_SEQ_1)]

	elif matrix_name == 'B':
		for i in range(constant.LEN_SEQ_1):
			matrix[i, :] = [constant.GAP_START + constant.GAP_EXTEND * j for j in range(constant.LEN_SEQ_2)]
		matrix[:, 0] = [i * constant.MIN for i in range(constant.LEN_SEQ_1)]

	elif matrix_name == 'C':
		for i in range(constant.LEN_SEQ_2):
			matrix[:, i] = [constant.GAP_START + constant.GAP_EXTEND * j for j in range(constant.LEN_SEQ_1)]
		matrix[0, :] = [i * constant.MIN for i in range(constant.LEN_SEQ_2)]

	matrix[0, 0] = 0
	return matrix


# сравниваем две буквы
def compare(letter_1, letter_2, constants):
	index_i = constants.LETTER_DICT[letter_1]
	index_j = constants.LETTER_DICT[letter_2]
	return constants.BLOSUM_MATRIX[index_i][index_j]


# if letter_1 == letter_2:
	# 	return constant.MATCH
	# else:
	# 	return constant.MISMATCH


''' 
	Работа алгоритма основана на трех матрицах X, Y, M
	поэтому необходимо их инициализировать:
'''
def calculate(seq_1, seq_2):
	const_input = input("\nВведите значения для GAP_START GAP_EXTEND (-10 -2)\n").split(" ")

	blosum, dic = BlosumReader.load_matrix()
	constant = Constant(int(const_input[0]), int(const_input[1]), blosum, dic, seq_1, seq_2)
	print("\nCalculating...\n")

	matrix_A = init_matrix('A', constant)
	matrix_B = init_matrix('B', constant)
	matrix_C = init_matrix('C', constant)

	# print(matrix_A)
	# print(matrix_B)
	# print(matrix_C)

	# заполним матрицы значениями
	for i in range(1, constant.LEN_SEQ_1):
		for j in range(1, constant.LEN_SEQ_2):
			matrix_B[i][j] = max(constant.GAP_START + constant.GAP_EXTEND + matrix_A[i][j - 1],
			                     constant.GAP_EXTEND + matrix_B[i][j - 1],
			                     constant.GAP_START + constant.GAP_EXTEND + matrix_C[i][j - 1])

			matrix_C[i][j] = max(constant.GAP_START + constant.GAP_EXTEND + matrix_A[i - 1][j],
			                     constant.GAP_START + constant.GAP_EXTEND + matrix_B[i - 1][j],
			                     constant.GAP_EXTEND + matrix_C[i - 1][j])

			matrix_A[i][j] = max(compare(seq_1[i - 1], seq_2[j - 1], constant) + matrix_A[i - 1, j - 1],
			                     matrix_B[i, j],
			                     matrix_C[i, j])

	# начинаем идти с нижней правой ячейки
	align1, align2 = '', ''
	i, j = len(seq_1), len(seq_2)
	score = -float("inf")

	while i > 0 and j > 0:
		if score == -float("inf"):
			score = matrix_A[i][j]

		if matrix_A[i][j] == matrix_A[i - 1][j - 1] + compare(seq_1[i - 1], seq_2[j - 1], constant):
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

	# print(align1[::-1])
	# print(align2[::-1])

	Util.print_sequences(align1[::-1], align2[::-1])
	print("\tScore: " + str(score))
