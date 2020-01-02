import numpy as np

var_2 = np.array([[4, 4, 0, 6, 12],
                   [1, 14, 14, 13, 11],
                   [17, 6, 14, 4, 3],
                   [18, 16, 13, 15, 16]])

matrix_manual = np.array([[5, 8, 7, 5, 4],
                          [1, 10, 5, 5, 6],
                          [2, 4, 3, 6, 2],
                          [3, 5, 4, 12, 3]])

if __name__ == "__main__":
    matrix = var_2

    bernulli = [np.sum(row) / len(row) for row in matrix]
    print(f"bernulli {bernulli}")

    vald = [np.min(row) for row in matrix]
    print(f"vald {vald}")

    alhpa = 0.5
    gurvits = [alhpa * np.min(row) + (1 - alhpa) * np.max(row) for row in matrix]
    print(f"gurvits {gurvits}")

    optimal = [np.max(row) for row in matrix]
    print(f"optimal {optimal}")

    maxes = [np.max(row) for row in matrix.T]
    savidge = ([maxes - row for row in matrix])
    print(f"savidge {savidge}")

