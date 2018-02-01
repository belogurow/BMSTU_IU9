import numpy as np
from numpy import linalg as lg


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
    return matrix[n_k:len(matrix), 0:n_k]


def calculate_matrix_l(matrix, matrix_t, matrix_b, n_k):
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


def calc_result_matrix_p(matrixes_p, size):
    p = np.eye(size)

    while len(matrixes_p) != 0:
        p_new = matrixes_p.pop()
        p = np.dot(p, p_new)

    return p.T


def solve_linear_system(b_matrix, l_matrix, p_matrix, t_matrix):
    z_matrix = lg.inv(l_matrix).dot(np.asarray(p_matrix.T)).dot(b_matrix)
    w_matrix = lg.inv(t_matrix).dot(z_matrix)
    y_matrix = lg.inv(np.asarray(l_matrix.T)).dot(w_matrix)
    x_matrix = np.asarray(p_matrix).dot(y_matrix)

    return np.asmatrix(x_matrix)

