from Bio import SeqIO
import NeedlemanWunschAffine
import NeedlemanWunsch
import SmithWaterman
import sys


def input_seq(fasta_file):
	i = 0
	seq_1 = seq_2 = ""
	for sequence in SeqIO.parse(fasta_file, "fasta"):
		if i == 0:
			seq_1 = sequence.seq
		elif i == 1:
			seq_2 = sequence.seq

		i += 1
	return seq_1, seq_2

if __name__ == '__main__':
	fasta_file = ''.join(sys.argv[1])

	seq_1, seq_2 = input_seq(fasta_file)

	while True:
		print("""Введите цифру алгоритма, который необходимо воспроизвести:
		1. Smith-Waterman
		2. Needleman-Wunsch
		3. Needleman-Wunsch Affine""")

		alg_number = input()

		if alg_number == '1':
			SmithWaterman.calculate(seq_1, seq_2)
		elif alg_number == '2':
			NeedlemanWunsch.calculate(seq_1, seq_2)
		elif alg_number == '3':
			NeedlemanWunschAffine.calculate(seq_1, seq_2)
		else:
			raise Exception("Ожидается 1, 2 или 3")

		print("-" * 100)


