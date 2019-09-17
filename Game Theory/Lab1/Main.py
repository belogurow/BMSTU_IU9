import numpy as np
from SimplexMatrix import SimplexMatrix
from SimplexMethod import SimplexMethod

c = np.array([2, 5, 3])
A = np.array([[2, 1, 2],
              [1, 2, 0],
              [0, 0.5, 1]])
b = np.array([[6],
              [6],
              [2]])

if __name__ == "__main__":
    matrix = SimplexMatrix(A, b, c)
    method = SimplexMethod(matrix)

    result = method.start()
    iterations = len(method.iterations)

    print('Result {0}\nIterations {1}'.format(result, iterations))
