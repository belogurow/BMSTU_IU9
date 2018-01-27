# coding=utf-8
import numpy as np
from numpy import linalg as lg
import math

alpha = (1 + math.sqrt(17)) / 8
n = [0]
m = [0]
blocks = []
matrixes_p = []


def bunch_kaufman(k, matrix):
    return None


def bunch_parlett(k, matrix):
    # matrix_size - размерность матрицы
    matrix_size = len(matrix)

    # max_element - μ0, max элемент по модулю матрицы A
    # idx_max_element - его индекс
    (max_element, idx_max_element) = (None, None)

    # max_diag_element - μ1, max диагональый элемент по модулю матрицы A
    # idx_max_diag_element - его индекс
    (max_diag_element, idx_max_diag_element) = (None, None)

    # 1 шаг - находим μ0 и μ1
    for j in range(matrix_size):
        for i in range(j, matrix_size):
            if max_element is None or max_element < abs(matrix[i, j]):
                (max_element, idx_max_element) = (abs(matrix[i, j]), (i, j))

    for i in range(matrix_size):
        if max_diag_element is None or max_diag_element < abs(matrix[i, i]):
            (max_diag_element, idx_max_diag_element) = (abs(matrix[i, i]), (i, i))

    # print(max_element, idx_max_element)
    # print(max_diag_element, idx_max_diag_element)

    # 2 шаг - опрделеяем размерность диагонального блока n_k[i]: 1x1 или 2x2
    if k > 1:
        row_index = m[k - 1] - calc_block_size(blocks)
    else:
        row_index = m[k - 1]

    if max_diag_element >= max_element * alpha:
        # n[k] = 1
        n.append(1)
        swap_numbers = [(row_index, idx_max_diag_element[0])]
    else:
        # n[k] = 2
        n.append(2)
        swap_numbers = [(row_index + 1, idx_max_element[0]), (row_index, idx_max_element[1])]

    p = np.eye(len(matrix))
    while len(swap_numbers) != 0:
        numbers = swap_numbers.pop(0)

        p_new = np.eye(len(matrix))
        swap_columns(p_new, numbers[0], numbers[1])

        p = np.dot(p, p_new)

    return p


def find_ldl(matrix, method_name='bunch-parlett'):
    source_matrix_size = len(matrix)
    source_matrix = matrix
    source_matrix_l = np.eye(source_matrix_size)

    if method_name == 'bunch-parlett':
        method = bunch_parlett
    else:
        method = bunch_kaufman

    # k - шаг алгоритма, начинаем с 1
    k = 1

    while len(matrix) > 1:
        matrix_p = method(k, matrix)

        # Определяем матрицы перестановок, на основе которых будет строиться конечная
        new_matrix_p = np.eye(source_matrix_size)
        new_matrix_p[m[k-1]:source_matrix_size, m[k-1]:source_matrix_size] = matrix_p
        matrixes_p.append(new_matrix_p)

        if k > 1:
            source_matrix_l = np.dot(matrixes_p[k-1], source_matrix_l)
            source_matrix_l = np.dot(source_matrix_l, matrixes_p[k-1])

        matrix = np.dot(matrix_p, matrix)
        matrix = np.dot(matrix, matrix_p.T)

        # print(matrix)

        m.append(m[k - 1] + n[k])
        block_size = source_matrix_size - m[k]

        matrix_t = get_matrix_t(matrix, n[k])
        matrix_b = get_matrix_b(matrix, n[k])
        matrix_l = calculate_matrix_l(matrix, matrix_t, matrix_b, n[k])

        matrix = np.dot(lg.inv(matrix_l), matrix)
        matrix = np.dot(matrix, lg.inv(matrix_l.T))

        # print(matrix)

        blocks.append(matrix[0:n[k], 0:n[k]])

        matrix = matrix[n[k]:len(matrix), n[k]:len(matrix)]

        size_l = len(source_matrix_l) - len(matrix_l)
        source_matrix_l[size_l:len(source_matrix_l), size_l:len(source_matrix_l)] = matrix_l

        # переходим на следующую итерацию
        k += 1

    blocks.append(matrix)

    print(blocks)
    t = calc_result_diagonal_matrix(blocks)
    p = calc_reuslt_matrix_p(matrixes_p, source_matrix_size)
    print("T\n", t)
    print("L\n", source_matrix_l)
    print("P\n", p)

    print(np.dot(np.dot(np.dot(np.dot(lg.inv(p), source_matrix_l), t), source_matrix_l.T), lg.inv(p.T)))


# solve_linear_system(np.array([0, 2]), source_matrix_l, np.eye(len(source_matrix_l)), calc_diagonal_matrix(blocks))


def swap_columns(matrix, idx1, idx2):
    if idx1 == idx2:
        return
    matrix[:, [idx1, idx2]] = matrix[:, [idx2, idx1]]


def swap_rows(matrix, idx1, idx2):
    if idx1 == idx2:
        return
    matrix[[idx1, idx2]] = matrix[[idx2, idx1]]


def get_matrix_t(matrix, n_k):
    return matrix[0:n_k, 0:n_k]


def get_matrix_b(matrix, n_k):
    # assert len(matrix) - block_size == block_size, 'Проблема с размерностью матрицы TAT\''
    return matrix[n_k:len(matrix), 0:n_k]


def calculate_matrix_l(matrix, matrix_t, matrix_b, n_k):
    # assert len(matrix) - block_size == block_size, 'Проблема с размерностью матрицы TAT\''
    matrix_l = np.eye(len(matrix))

    matrix_t = np.asmatrix(matrix_t)
    matrix_b = np.asmatrix(matrix_b)

    temp = np.dot(matrix_b, lg.inv(matrix_t))

    matrix_l[n_k:len(matrix), 0:n_k] = temp
    return matrix_l


def calc_block_size(blocks):
    size = 0
    for block in blocks:
        size += len(block)

    return size


def calc_result_diagonal_matrix(blocks):
    blocks_size = calc_block_size(blocks)
    matrix = np.zeros((blocks_size, blocks_size))

    start_index = 0
    end_index = 0

    for block in blocks:
        end_index += len(block)

        matrix[start_index:end_index, start_index:end_index] = block
        start_index += len(block)

    return matrix


def calc_reuslt_matrix_p(matrixes_p, size):
    p = np.eye(size)

    while len(matrixes_p) != 0:
        p_new = matrixes_p.pop()
        p = np.dot(p, p_new)

    return p


def solve_linear_system(b_matrix, l_matrix, p_matrix, d_matrix):
    z_matrix = lg.inv(l_matrix).dot(np.asarray(p_matrix).transpose()).dot(b_matrix)
    w_matrix = lg.inv(d_matrix).dot(z_matrix)
    y_matrix = lg.inv(np.asarray(l_matrix).transpose()).dot(w_matrix)
    x_matrix = np.asarray(p_matrix).dot(y_matrix)

    # print(z_matrix, w_matrix, y_matrix, x_matrix, sep="\n")
    print(x_matrix)
    return x_matrix



if __name__ == '__main__':
    matrix = np.matrix([[6, 12, 3, -6],
                        [12, -8, -13, 4],
                        [3, -13, -7, 1],
                        [-6, 4, 1, 6]])

    # matrix = np.array([[2, -1, 0], [-1, 2, -1], [0, -1, 1]])

    # matrix = np.array([[4, 1], [1, 2]])

    # matrix = np.matrix([[1, 2], [3, 4]])

    matrix = np.random.random_integers(-50, 50, size=(7, 7))
    matrix = (matrix + matrix.T)

    print(matrix)
    find_ldl(matrix)

    # l = np.dot(np.matrix([[1, 0, 0, 0], [0, 1, 0, 0], [-0.39, -0.13, 1, 0], [1.16, 0.38, 0, 1]]), np.matrix([[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 1.09, 1]]))
    # p = np.dot(np.matrix([[0, 1, 0, 0], [0, 0, 1, 0], [1, 0, 0, 0], [0, 0, 0, 1]]), np.matrix([[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 0, 1], [0, 0, 1, 0]]))
    # t = calc_diagonal_matrix(blocks)
    #
    # result = np.dot(np.dot(np.dot(np.dot(p, l), t), l.T), p.T)

