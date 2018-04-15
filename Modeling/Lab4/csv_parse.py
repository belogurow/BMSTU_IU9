import csv

result = []
temp = []
is_first = True

with open('test.csv', 'r') as csvfile:
	spamreader = csv.reader(csvfile, delimiter=';')


	for row in spamreader:
		if len(temp) == 3:
			result.append(temp)
			temp = []

		if is_first:
			is_first = False
			temp.append(int(row[0]))
		else:
			temp.append(float(row[0]))

print(result)
