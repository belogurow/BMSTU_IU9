import numpy as np
import math
import itertools

from Lab1.SimplexMatrix import SimplexMatrix


class BruteForceMethod():
    def __init__(self, matrix: SimplexMatrix):
        self.A = matrix.A
        self.b = matrix.b
        self.c = matrix.c

        self.result = None

    def start(self):
        print("Start brute force method:")
        A_transpose = np.transpose(self.A)
        b_transpose = np.transpose(self.b)[0]
        values = []
        for (row_idx, row) in enumerate(A_transpose):
            elem_values = b_transpose / row
            elem_values[np.isinf(elem_values)] = 0
            max_value = 0 if np.max(elem_values) < 0 else np.max(elem_values)
            if max_value == 0:
                values.append([0])
            else:
                # Сами добавляем + 1, так как range не включает верхнюю границу
                values.append(list(range(math.ceil(max_value) + 1)))

        value_combinations = list(itertools.product(*values))
        brute_force_result = {}
        for combination in value_combinations:
            np_combination = np.array(combination)
            condition = True
            for (row_idx, row) in enumerate(self.A):
                condition &= (np.sum(row * np_combination) <= self.b[row_idx])[0]

            if condition:
                f_value = np.sum(np.array(self.c) * np_combination)
                brute_force_result[f_value] = np_combination

        for f_value, combination in brute_force_result.items():
            print("F = {}, X = {}".format(f_value, combination))

        max_f = max(brute_force_result, key=int)
        print("\nmax(F) = {}, X = {}".format(max_f, brute_force_result[max_f]))

        self.result = max_f
        return self.result
