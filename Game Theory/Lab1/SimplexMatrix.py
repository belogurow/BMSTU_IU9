import numpy as np
from copy import deepcopy
import pandas as pd


class SimplexMatrix:
    def __init__(self, A, b, c):
        self.A = np.array(A)
        self.b = np.array(b)
        self.c = np.array(c)

        # Канонический вид x = b - A
        self.canonic = np.hstack((b, A))
        self.f = np.insert(-c, 0, 0)
        self.canonic_with_f = np.vstack((self.canonic, self.f))

        self.rows, self.cols = self.canonic_with_f.shape

        self.col = ['x' + str(self.rows - 1 + i) for i in range(1, self.rows)] + ['f']  # Свободные
        self.row = ['s'] + ['x' + str(i) for i in range(1, self.cols)]  # Базис

        self.resolving_row_idx = None  # Индекс разрещающей строки
        self.resolving_col_idx = None  # Индекс разрещающего столбца
        self.resolving_element = None

        self.iteration = 0

    def set_resolving_elements(self, resolving_row_idx, resolving_col_idx):
        self.resolving_row_idx = resolving_row_idx
        self.resolving_col_idx = resolving_col_idx
        self.resolving_element = self.canonic_with_f[resolving_row_idx, resolving_col_idx]

    def clear_resolving_elements(self):
        self.resolving_row_idx = None
        self.resolving_col_idx = None
        self.resolving_element = None

    def create_new_matrix(self):
        new_matrix = deepcopy(self)
        new_matrix.clear_resolving_elements()

        for i in range(self.rows):
            for j in range(self.cols):
                if (i, j) == (self.resolving_row_idx, self.resolving_col_idx):
                    new_matrix.canonic_with_f[
                        self.resolving_row_idx, self.resolving_col_idx] = 1 / self.resolving_element
                elif i == self.resolving_row_idx:
                    new_matrix.canonic_with_f[i, j] = self.canonic_with_f[i, j] / self.resolving_element
                elif j == self.resolving_col_idx:
                    new_matrix.canonic_with_f[i, j] = - self.canonic_with_f[i, j] / self.resolving_element
                else:
                    new_matrix.canonic_with_f[i, j] = self.canonic_with_f[i, j] - \
                                                      self.canonic_with_f[i, self.resolving_col_idx] * \
                                                      self.canonic_with_f[self.resolving_row_idx, j] \
                                                      / self.resolving_element

        new_matrix.canonic = new_matrix.canonic_with_f[0:new_matrix.rows - 1, :]
        new_matrix.f = new_matrix.canonic_with_f[new_matrix.rows - 1, :]

        new_matrix.col[self.resolving_row_idx], new_matrix.row[self.resolving_col_idx] = self.row[
                                                                                             self.resolving_col_idx], \
                                                                                         self.col[
                                                                                             self.resolving_row_idx]
        new_matrix.iteration += 1

        print(new_matrix)

        return new_matrix

    def __repr__(self):
        df = pd.DataFrame(self.canonic_with_f, columns=self.row, index=self.col)
        return str(df) + "\n"