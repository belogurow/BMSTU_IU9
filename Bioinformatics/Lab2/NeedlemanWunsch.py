import numpy

import BlosumReader
import Util


# задаем значения
class Constants:
	def __init__(self, GAP_PENALTY, BLOSUM_MATRIX, dic, seq_1, seq_2):
		self.GAP_PENALTY = GAP_PENALTY
		self.LEN_SEQ_1 = len(seq_1) + 1
		self.LEN_SEQ_2 = len(seq_2) + 1
		self.BLOSUM_MATRIX = BLOSUM_MATRIX
		self.LETTER_DICT = dic


# сравниваем две буквы
def compare(letter_1, letter_2, constants):
	index_i = constants.LETTER_DICT[letter_1]
	index_j = constants.LETTER_DICT[letter_2]
	return constants.BLOSUM_MATRIX[index_i][index_j]

	# if letter_1 == letter_2:
	# 	return constants.MATCH
	# else:
	# 	return constants.MISMATCH


def calculate(seq_1, seq_2):
	const_input = input("\nВведите значения для GAP_PENALTY (-5)\n").split(" ")

	blosum, dic = BlosumReader.load_matrix()
	constants = Constants(int(const_input[0]), blosum, dic, seq_1, seq_2)
	print("\nCalculating...\n")

	# создаем табличку, которая заполнена нулями
	score_grid = numpy.zeros(shape=(constants.LEN_SEQ_1, constants.LEN_SEQ_2))

	# заполняем табличку согласно алгоритму
	for i in range(0, constants.LEN_SEQ_1):
		for j in range(0, constants.LEN_SEQ_2):
			if i == 0:
				score_grid[0, j] = constants.GAP_PENALTY * j
			elif j == 0:
				score_grid[i, 0] = constants.GAP_PENALTY * i
			else:
				match_score = score_grid[i - 1, j - 1] + compare(seq_1[i - 1], seq_2[j - 1], constants)
				delete_score = score_grid[i - 1, j] + constants.GAP_PENALTY
				insert_score = score_grid[i, j - 1] + constants.GAP_PENALTY
				score_grid[i, j] = max(match_score, delete_score, insert_score)

	align1, align2 = '', ''
	# начинаем идти с нижней правой ячейки
	i, j = len(seq_1), len(seq_2)
	score = -float("inf")

	# print(score_grid)

	while i > 0 and j > 0:
		if score == -float("inf"):
			score = score_grid[i][j]

		current_cell = score_grid[i][j]
		diagonal_cell = score_grid[i - 1][j - 1]
		up_cell = score_grid[i][j - 1]
		left_cell = score_grid[i - 1][j]

		if current_cell == diagonal_cell + compare(seq_1[i - 1], seq_2[j - 1], constants):
			align1 += seq_1[i - 1]
			align2 += seq_2[j - 1]
			i -= 1
			j -= 1
		elif current_cell == left_cell + constants.GAP_PENALTY:
			align1 += seq_1[i - 1]
			align2 += '-'
			i -= 1
		elif current_cell == up_cell + constants.GAP_PENALTY:
			align1 += '-'
			align2 += seq_2[j - 1]
			j -= 1

	# print(align1[::-1])
	# print(align2[::-1])

	Util.print_sequences(align1[::-1], align2[::-1])
	print("\tScore: " + str(score))