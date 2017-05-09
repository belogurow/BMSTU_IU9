import random

coordY = [(i + 1) + random.uniform(-1, 1) * 0.1
          for i in range(10)]
coordX = [(i + 1) + random.uniform(-1, 1) * 0.1
          for i in range(10)]
n = len(coordX)

print(coordY)
print(coordX)

A = 0
for item in coordX:
	A += item ** 2

B = sum(coordX)

C = 0
for i in range(len(coordX)):
	C += coordX[i] * coordY[i]

D = sum(coordY)

a = (C * n - D * B) / (n * A - B ** 2)
b = (D - a * B) / n

print("a: " + str(a))
print("b: " + str(b))