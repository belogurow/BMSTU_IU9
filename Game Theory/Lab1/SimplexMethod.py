import numpy as np
from SimplexMatrix import SimplexMatrix

INF = float('inf')


def find_idx_of(condition):
    idx_list = np.where(condition)[0]
    if len(idx_list) == 0:
        return -1
    else:
        return idx_list[0]


def replace_negative(array, replace_element=INF):
    return np.where(array < 0, INF, array)


class SimplexMethod:
    def __init__(self, matrix: SimplexMatrix):
        self.iterations = [matrix]
        self.result = None

    def start(self):
        current_matrix = self.iterations[0]
        print("Start simplex method \n{}".format(current_matrix))

        first_col = current_matrix.canonic[:, 0]
        negative_element_idx = find_idx_of(np.min(first_col < 0))

        if negative_element_idx == -1:
            # Если в столбце S нет отрицательных элементов, то имеем опорное решение
            # В последней строке ищем первый отрицательный элемент
            while True:
                resolving_col_idx = find_idx_of(current_matrix.f < 0)

                if resolving_col_idx == -1:
                    # Отрицательных элементов нет, значит нашли оптимальное решение
                    break

                # Найти минимальное положительное отношение элемента свободных членов
                # к соотвествющему элементу в разрешающем столбце
                first_col = current_matrix.canonic[:, 0]
                resolving_col = current_matrix.canonic[:, resolving_col_idx]
                divison = first_col / resolving_col
                # resolving_row_idx = find_idx_of(np.min(divison > 0))
                resolving_row_idx = np.argmin(replace_negative(divison))

                print('Замена базисной переменной {} на свободную {}'.format(current_matrix.row[resolving_col_idx],
                                                                             current_matrix.col[resolving_row_idx]))
                current_matrix.set_resolving_elements(resolving_row_idx, resolving_col_idx)

                new_matrix = current_matrix.create_new_matrix()
                self.iterations.append(new_matrix)

                current_matrix = new_matrix
        else:
            # TODO поиск отрицательного элемента в столбце s -> замена базиса
            assert negative_element_idx != -1

        self.result = current_matrix.f[0]
        return self.result
