import numpy as np

from Lab1.SimplexMatrix import SimplexMatrix, FCondition

INF = float('inf')


def find_idx_of(condition):
    idx_list = np.where(condition)[0]
    if len(idx_list) == 0:
        return -1
    else:
        return idx_list[0]


def replace_positive(array, replace_element=INF):
    return np.where(array >= 0, INF, array)


def replace_negative(array, replace_element=INF):
    return np.where(array < 0, INF, array)


class SimplexMethod:
    def __init__(self, matrix: SimplexMatrix):
        self.iterations = [matrix]
        self.result = None

    def start(self):
        current_matrix = self.iterations[0]
        current_matrix.prepare_variables()
        print("Start simplex method \n{}".format(current_matrix))

        # Если в столбце S нет отрицательных элементов, то имеем опорное решение
        # В последней строке ищем первый отрицательный элемент
        while True:
            first_col = current_matrix.canonic[:, 0]
            negative_element_idx = find_idx_of(first_col < 0)
            remove_positive = False

            if negative_element_idx != -1:
                resolving_col_idx = find_idx_of(current_matrix.canonic[negative_element_idx][1:] < 0)
                remove_positive = True
                if resolving_col_idx == -1:
                    raise RuntimeError("Не найден отрицательный элемент в строке {}".format(negative_element_idx))
            else:
                resolving_col_idx = find_idx_of(current_matrix.f[1:] < 0)
                if resolving_col_idx == -1:
                    # Отрицательных элементов нет, значит нашли оптимальное решение
                    break
                else:
                    # Находим отрицательный элемент, который будет минимален по модулю
                    resolving_col_idx = np.argmin(abs(replace_positive(current_matrix.f[1:])))

                    # Необходимо добавить столбeц s
            resolving_col_idx += 1

            # Найти минимальное положительное отношение элемента свободных членов
            # к соотвествющему элементу в разрешающем столбце
            resolving_col = current_matrix.canonic[:, resolving_col_idx]
            if remove_positive:
                divison = replace_positive(first_col) / resolving_col
            else:
                divison = first_col / resolving_col

            resolving_row_idx = np.argmin(replace_negative(divison))

            print('Замена базисной переменной {} на свободную {}'.format(current_matrix.row[resolving_col_idx],
                                                                         current_matrix.col[resolving_row_idx]))
            current_matrix.set_resolving_elements(resolving_row_idx, resolving_col_idx)

            new_matrix = current_matrix.create_new_matrix()
            self.iterations.append(new_matrix)

            current_matrix = new_matrix

        self.result = current_matrix.f[0]

        # Данная реализация всегда ищет максимум, если необходимо найти минимум
        # то умножаем результат на -1
        if current_matrix.f_condition == FCondition.MIN:
            self.result *= -1
            print('Так как ищем MIN, то умножаем резульат на -1 -> {}'.format(self.result))

        return self.result
