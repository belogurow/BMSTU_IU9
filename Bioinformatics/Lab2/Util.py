def print_sequences(seq_1: str, seq_2: str):
	sequence_len = len(seq_1)

	start = 0
	step = 80
	end = start + step

	while start < sequence_len:
		print(seq_1[start:end])
		print("|" * len(seq_1[start:end]))
		print(seq_2[start:end])
		print()

		start += step
		end += step