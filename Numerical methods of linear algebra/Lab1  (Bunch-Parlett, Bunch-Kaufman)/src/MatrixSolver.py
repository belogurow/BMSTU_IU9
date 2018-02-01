# coding=utf-8
import math
import argparse
from Util import *

alpha = (1 + math.sqrt(17)) / 8
n = [0]
m = [0]
blocks = []
matrixes_p = []


def bunch_kaufman(k, matrix):
    # matrix_size - размерность матрицы
    matrix_size = len(matrix)

    # max_diag_element - max диагональый элемент по модулю матрицы A
    # idx_max_diag_element - его индекс
    (max_diag_element, idx_max_diag_element) = (None, None)

    # 1 шаг - ищем наибольший диагональный элемент,
    # и при необходимости перемещаем его на позицию [0, 0]
    for i in range(matrix_size):
        if max_diag_element is None or max_diag_element < abs(matrix[i, i]):
            (max_diag_element, idx_max_diag_element) = (abs(matrix[i, i]), (i, i))

    if idx_max_diag_element != (0, 0):
        p = np.eye(matrix_size)

        swap_columns(p, 0, idx_max_diag_element[0])

        matrix = np.dot(p, matrix)
        matrix = np.dot(matrix, p.T)

        idx_max_diag_element = (0, 0)

    # 2 шаг - находим наибольший элемент в первом столбце
    (elem_lambda, idx_elem_lambda) = (None, None)

    for i in range(matrix_size):
        if elem_lambda is None or elem_lambda < abs(matrix[i, 0]):
            (elem_lambda, idx_elem_lambda) = (abs(matrix[i, 0]), (i, 0))

    # 3 шаг - определяем размерность блока n[k] и матрицу перестановок P
    if k > 1:
        row_index = m[k - 1] - calc_block_size(blocks)
    else:
        row_index = m[k - 1]

    if max_diag_element >= alpha * elem_lambda:
        # n[k] = 1
        n.append(1)
        swap_numbers = [(0, 0)]
    else:
        (elem_sigma, idx_elem_sigma) = (None, None)

        for i in range(1, matrix_size):
            if elem_sigma is None or elem_sigma < abs(matrix[i, 0]):
                (elem_sigma, idx_elem_sigma) = (abs(matrix[i, 0]), (i, 0))

        if elem_sigma * max_diag_element >= alpha * (elem_lambda ** 2):
            # n[k] = 1
            n.append(1)
            swap_numbers = [(0, 0)]
        else:
            if max_diag_element >= alpha * elem_sigma:
                # n[k] = 1
                n.append(1)
                swap_numbers = [(row_index, idx_max_diag_element[0])]
            else:
                # n[k] = 2
                n.append(2)
                swap_numbers = [(row_index + 1, idx_max_diag_element[0]), (row_index, idx_max_diag_element[1])]

    p = np.eye(len(matrix))
    while len(swap_numbers) != 0:
        numbers = swap_numbers.pop(0)

        p_new = np.eye(len(matrix))
        swap_columns(p_new, numbers[0], numbers[1])

        p = np.dot(p, p_new)

    return p


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

    # 2 шаг - опрделеяем размерность блока n[k] и матрицу перастановок P
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


def find_ldl(matrix, method=bunch_parlett):
    source_matrix_size = len(matrix)
    source_matrix = matrix
    source_matrix_l = np.eye(source_matrix_size)

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

        m.append(m[k - 1] + n[k])

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

    result_matrix_t = calc_result_diagonal_matrix(blocks)
    result_matrix_p = calc_result_matrix_p(matrixes_p, source_matrix_size)

    # print(np.dot(np.dot(np.dot(np.dot(lg.inv(result_matrix_p), source_matrix_l), result_matrix_t), source_matrix_l.T), lg.inv(result_matrix_p.T)))

    return source_matrix, result_matrix_p, result_matrix_t, source_matrix_l


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('method_name', help='Name of LTL-method: \'Bunch-Kaufman\' or \'Bunch-Parlett\'')
    parser.add_argument('matrix_a', help='Name of file where exists symmetry matrix A')
    parser.add_argument('matrix_b', help='Name of file where exists matrix B')

    method_name = parser.parse_args().method_name
    assert method_name != 'Bunch-Parlett' or method_name != 'Bunch-Kaufman', 'Method must be \'Bunch-Kaufman\' or \'Bunch-parlett\'!'

    if method_name == 'Bunch-Parlett':
        method = bunch_parlett
    else:
        method = bunch_kaufman

    matrix_a = np.asmatrix(np.loadtxt(parser.parse_args().matrix_a, delimiter=' '))
    assert (matrix_a.T == matrix_a).all(), 'Matrix A must be symmetric!'

    matrix_b = np.asmatrix(np.loadtxt(parser.parse_args().matrix_b, delimiter=' ')).T
    assert len(matrix_a) == len(matrix_b), 'Matrixes A and B must be the same size!'

    random_matrixes = False
    if random_matrixes:
        size = 100

        matrix_a = np.random.random_integers(-50, 50, size=(size, size))
        matrix_a = (matrix_a + matrix_a.T)

        matrix_b = np.random.random_integers(-50, 50, size=(size, 1))

        method = bunch_parlett


    (matrix_a, matrix_p, matrix_t, matrix_l) = find_ldl(matrix_a, method)
    print("my - solution\n", solve_linear_system(matrix_b, matrix_l, matrix_p, matrix_t))
    print("\nsolution\n", lg.solve(matrix_a, matrix_b))