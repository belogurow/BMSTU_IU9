import numpy

# читаем последовательности из файлов
seq_1 = open("sequence1").read()
seq_2 = open("sequence2").read()


# задаем значения
class Scores:
	MATCH = 10
	MISMATCH = -5
	GAP_PENALTY = -5


class Direction:
	END = 0
	UP = 1
	LEFT = 2
	DIAGONAL = 3


# сравниваем две буквы
def compare(letter_1, letter_2):
	if letter_1 == letter_2:
		return Scores.MATCH
	else:
		return Scores.MISMATCH


if __name__ == '__main__':
	# создаем табличку для значений ячеек
	score_grid = numpy.zeros(shape=(len(seq_1) + 1, len(seq_2) + 1))
	# создаем табличку для отслеживания пути
	trace_grid = numpy.zeros(shape=(len(seq_1) + 1, len(seq_2) + 1))

	# переменная для максимального значения
	max_score = 0
	# индексы максимального значения
	max_i = 0
	max_j = 0

	for i in range(1, len(seq_1) + 1):
		for j in range(1, len(seq_2) + 1):
			score_diagonal_cell = score_grid[i - 1][j - 1] + compare(seq_1[i - 1], seq_2[j - 1])
			score_up_cell = score_grid[i][j - 1] + Scores.GAP_PENALTY
			score_left_cell = score_grid[i - 1][j] + Scores.GAP_PENALTY
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

	print(align1[::-1])
	print(align2[::-1])