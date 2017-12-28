import numpy
import Util
import BlosumReader


# задаем значения
class Constants:
	def __init__(self, GAP_PENALTY, BLOSUM_MATRIX, dic, seq_1, seq_2):
		self.GAP_PENALTY = GAP_PENALTY
		self.LEN_SEQ_1 = len(seq_1) + 1
		self.LEN_SEQ_2 = len(seq_2) + 1
		self.BLOSUM_MATRIX = BLOSUM_MATRIX
		self.LETTER_DICT = dic


class Direction:
	END = 0
	UP = 1
	LEFT = 2
	DIAGONAL = 3


# сравниваем две буквы
def compare(letter_1, letter_2, constants):
	index_i = constants.LETTER_DICT[letter_1]
	index_j = constants.LETTER_DICT[letter_2]
	return constants.BLOSUM_MATRIX[index_i][index_j]
	#
	# if letter_1 == letter_2:
	# 	return constants.MATCH
	# else:
	# 	return constants.MISMATCH


def calculate(seq_1, seq_2):
	const_input = input("\nВведите значения для GAP_PENALTY (-5)\n").split(" ")

	blosum, dic = BlosumReader.load_matrix()
	constants = Constants(int(const_input[0]), blosum, dic, seq_1, seq_2)
	print("\nCalculating...\n")

	# создаем табличку для значений ячеек
	score_grid = numpy.zeros(shape=(constants.LEN_SEQ_1, constants.LEN_SEQ_2))
	# создаем табличку для отслеживания пути
	trace_grid = numpy.zeros(shape=(constants.LEN_SEQ_1, constants.LEN_SEQ_2))

	# переменная для максимального значения
	max_score = 0
	# индексы максимального значения
	max_i = 0
	max_j = 0

	for i in range(1, constants.LEN_SEQ_1):
		for j in range(1, constants.LEN_SEQ_2):
			score_diagonal_cell = score_grid[i - 1][j - 1] + compare(seq_1[i - 1], seq_2[j - 1], constants)
			score_up_cell = score_grid[i][j - 1] + constants.GAP_PENALTY
			score_left_cell = score_grid[i - 1][j] + constants.GAP_PENALTY
			score_grid[i][j] = max(0, score_diagonal_cell, score_up_cell, score_left_cell)

			# заполняем табличку пути
			if score_grid[i][j] == 0:
				trace_grid[i][j] = Direction.END
			if score_grid[i][j] == score_left_cell:
				trace_grid[i][j] = Direction.UP
			if score_grid[i][j] == score_up_cell:
				trace_grid[i][j] = Direction.LEFT
			if score_grid[i][j] == score_diagonal_cell:
				trace_grid[i][j] = Direction.DIAGONAL

			if score_grid[i][j] >= max_score:
				max_i = i
				max_j = j
				max_score = score_grid[i][j]

	align1, align2 = '', ''
	i, j = max_i, max_j
	score = score_grid[i][j]

	# print(score_grid)
	# print(trace_grid.max())


	# находим ответ
	while trace_grid[i][j] != Direction.END:
		if trace_grid[i][j] == Direction.DIAGONAL:
			align1 += seq_1[i - 1]
			align2 += seq_2[j - 1]
			i -= 1
			j -= 1
		elif trace_grid[i][j] == Direction.LEFT:
			align1 += '-'
			align2 += seq_2[j - 1]
			j -= 1
		elif trace_grid[i][j] == Direction.UP:
			align1 += seq_1[i - 1]
			align2 += '-'
			i -= 1

	# print(align1[::-1])
	# print(align2[::-1])

	Util.print_sequences(align1[::-1], align2[::-1])
	print("\tScore: " + str(score))