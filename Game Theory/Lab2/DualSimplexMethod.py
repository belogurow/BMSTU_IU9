import numpy as np

from Lab1.Conditions import opposite_f_condition, opposite_a_condition_b
from Lab1.SimplexMatrix import SimplexMatrix
from Lab1.SimplexMethod import SimplexMethod


class DualSimplexMethod(SimplexMethod):
    def __init__(self, matrix: SimplexMatrix):
        # Транспонируем матрицы
        new_A = matrix.A.T
        new_b = np.array([matrix.c]).T
        new_c = matrix.b.T[0]

        # Переворачиваем условия
        new_f_condition = opposite_f_condition(matrix.f_condition)
        new_a_condition_b = opposite_a_condition_b(matrix.a_condition_b)

        dual_matrix = SimplexMatrix(new_A, new_b, new_c, new_f_condition, new_a_condition_b)

        super().__init__(dual_matrix)

