import math

import numpy as np

from Lab1.Conditions import FCondition, AConditionB
from Lab1.SimplexMatrix import SimplexMatrix
from Lab1.SimplexMethod import SimplexMethod


def is_none(array):
    return list(map(lambda x: x is None, array))


def is_nan(array):
    return np.isnan(array)


def idx_of_none_int(numbers):
    not_is_int = lambda x: not x.is_integer()
    idx_list = np.where(list(map(not_is_int, numbers)))[0]
    if len(idx_list) == 0:
        return -1
    else:
        return idx_list[0]


class BranchAndBoundSimplexMethod(SimplexMethod):
    def __init__(self, matrix: SimplexMatrix):
        self.current_matrix = matrix
        super().__init__(matrix)

    def start(self):
        simplex_method = SimplexMethod(self.current_matrix)
        simplex_method.start()  # Находим первое решение
        while True:
            last_iteration = simplex_method.iterations[-1]
            idx_of_non_int = idx_of_none_int(last_iteration.canonic[:, 0])
            if idx_of_non_int == -1:
                # Все переменные целочисленные -> найдено решение
                self.result = simplex_method.result
                return self.result

            # Разделяем на две ветви и ищем оптимальное решение
            non_int_number = last_iteration.canonic[idx_of_non_int, 0]
            left_branch_number = math.floor(non_int_number)
            right_branch_number = math.floor(non_int_number) + 1

            # Номер переменной, по которой будет проходить ветвление
            # ('x1' -> 0)
            element_of_branching = int(last_iteration.col[idx_of_non_int][1:]) - 1
            new_vector = np.array([0] * len(last_iteration.c))
            new_vector[element_of_branching] = 1

            if last_iteration.f_condition != FCondition.MAX or last_iteration.a_condition_b != AConditionB.LESS_OR_EQUAL:
                raise ValueError("Unknown conditions!")

            left_simplex_method = None
            left_simplex_result = None

            right_simplex_method = None
            right_simplex_result = None

            try:
                print("Старт симплекс метода для левой ветви:\n")
                A_left = np.vstack((last_iteration.A, new_vector))
                b_left = np.vstack((last_iteration.b, left_branch_number))
                left_matrix = SimplexMatrix(A_left, b_left, last_iteration.c)
                left_simplex_method = SimplexMethod(left_matrix)
                left_simplex_result = left_simplex_method.start()
            except RuntimeError:
                print("Левая ветвь не имеет решения!")

            try:
                print("Старт симплекс метода для правой ветви:\n")
                A_right = np.vstack((last_iteration.A, -new_vector))
                b_right = np.vstack((last_iteration.b, -right_branch_number))
                right_matrix = SimplexMatrix(A_right, b_right, last_iteration.c)
                right_simplex_method = SimplexMethod(right_matrix)
                right_simplex_result = right_simplex_method.start()
            except RuntimeError:
                print("Правая ветвь не имеет решения!")

            print("Левая ветвь [{}] : Правая ветвь [{}]".format(left_simplex_result, right_simplex_result))
            result = np.array([left_simplex_result, right_simplex_result], dtype=np.float64)
            bool_result = np.logical_or(is_nan(result), is_none(result))

            if any(bool_result):
                if all(bool_result):
                    # Оба решения не существуют, берем родителя
                    self.result = simplex_method.result
                    return result
                else:
                    # Левая или правая ветвь не имеют решения
                    error_element_idx = np.where(bool_result)[0][0]
                    # Выбираем существующее
                    self.result = result[(error_element_idx + 1) % 2]

                return self.result
            elif left_simplex_result > right_simplex_result:
                # Выбираем левую ветвь
                simplex_method = left_simplex_method
            else:
                # Выбираем правую ветвь
                simplex_method = right_simplex_method
