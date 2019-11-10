import numpy as np

from Lab1.Conditions import FCondition, AConditionB
from Lab1.SimplexMatrix import SimplexMatrix
from Lab1.SimplexMethod import SimplexMethod


def create_simplex_matrix(default_matrix):
    A = default_matrix
    rows, cols = A.shape

    c = np.ones(cols)
    b = np.ones((rows, 1))

    return A, b, c


class MixedStrategyMethod:
    def __init__(self, matrix):
        # player А
        A_1, b_1, c_1 = create_simplex_matrix(matrix.T)
        self.simplex_matrix_A = SimplexMatrix(A_1, b_1, c_1, FCondition.MIN, AConditionB.GREATER_OR_EQUAL)
        self.simplex_method_A = SimplexMethod(self.simplex_matrix_A)
        self.result_A = None

        # player B
        A_2, b_2, c_2 = create_simplex_matrix(matrix)
        self.simplex_matrix_B = SimplexMatrix(A_2, b_2, c_2, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        self.simplex_method_B = SimplexMethod(self.simplex_matrix_B)
        self.result_B = None

    @staticmethod
    def find_optimal_strategy(simplex_method: SimplexMethod):
        opposite_result = 1.0 / simplex_method.result
        s_variables = simplex_method.iterations[-1].canonic[:, 0]
        s_variables_name = simplex_method.iterations[-1].col

        # remove F name
        s_variables_name.remove('f')
        # remove first letter 'x'
        s_variables_name = list(map(lambda name: int(name[1:]), s_variables_name))

        optimal_strategy = np.zeros(simplex_method.iterations[-1].cols - 1)

        for index, _ in enumerate(optimal_strategy):
            if index + 1 in s_variables_name:
                optimal_strategy[index] = opposite_result * s_variables[s_variables_name.index(index + 1)]

        return optimal_strategy

    def start(self):
        self.simplex_method_A.start()
        self.simplex_method_B.start()

        self.result_A = MixedStrategyMethod.find_optimal_strategy(self.simplex_method_A)
        self.result_B = MixedStrategyMethod.find_optimal_strategy(self.simplex_method_B)

        print(f'Оптимальная смешанная стратегия игрока A = {self.result_A}')
        print(f'Оптимальная смешанная стратегия игрока B = {self.result_B}')
