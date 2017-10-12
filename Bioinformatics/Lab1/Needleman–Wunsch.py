import numpy

# читаем последовательности из файлов
seq_1 = open("sequence1").read()
seq_2 = open("sequence2").read()


# задаем значения
class Scores:
	MATCH = 1
	MISMATCH = -1
	GAP_PENALTY = -5


# сравниваем две буквы
def compare(letter_1, letter_2):
	if letter_1 == letter_2:
		return Scores.MATCH
	else:
		return Scores.MISMATCH


if __name__ == '__main__':
	# создаем табличку, которая заполнена нулями
	score_grid = numpy.zeros(shape=(len(seq_1) + 1, len(seq_2) + 1))

	# заполняем табличку согласно алгоритму
	for i in range(0, len(seq_1) + 1):
		for j in range(0, len(seq_2) + 1):
			if i == 0:
				score_grid[0, j] = Scores.GAP_PENALTY * j
			elif j == 0:
				score_grid[i, 0] = Scores.GAP_PENALTY * i
			else:
				match_score = score_grid[i - 1, j - 1] + compare(seq_1[i - 1], seq_2[j - 1])
				delete_score = score_grid[i - 1, j] + Scores.GAP_PENALTY
				insert_score = score_grid[i, j - 1] + Scores.GAP_PENALTY
				score_grid[i, j] = max(match_score, delete_score, insert_score)

	align1, align2 = '', ''
	# начинаем идти с нижней правой ячейки
	i, j = len(seq_1), len(seq_2)
	while i > 0 and j > 0:
		current_cell = score_grid[i][j]
		diagonal_cell = score_grid[i - 1][j - 1]
		up_cell = score_grid[i][j - 1]
		left_cell = score_grid[i - 1][j]

		if current_cell == diagonal_cell + compare(seq_1[i - 1], seq_2[j - 1]):
			align1 += seq_1[i - 1]
			align2 += seq_2[j - 1]
			i -= 1
			j -= 1
		elif current_cell == left_cell + Scores.GAP_PENALTY:
			align1 += seq_1[i - 1]
			align2 += '-'
			i -= 1
		elif current_cell == up_cell + Scores.GAP_PENALTY:
			align1 += '-'
			align2 += seq_2[j - 1]
			j -= 1

	#if (len(align1) % 50 )
	print(align1[::-1])
	print(align2[::-1])