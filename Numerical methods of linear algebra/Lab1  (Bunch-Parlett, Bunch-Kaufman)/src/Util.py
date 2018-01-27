import numpy as np


def swap_columns(matrix, idx1, idx2):
    if idx1 == idx2:
        return
    matrix[:, [idx1, idx2]] = matrix[:, [idx2, idx1]]


def swap_rows(matrix, idx1, idx2):
    if idx1 == idx2:
        return
    matrix[[idx1, idx2]] = matrix[[idx2, idx1]]
