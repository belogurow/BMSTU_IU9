def bunch_parlett(k, matrix):
    # matrix_size - размерность матрицы
    matrix_size = len(matrix)

    # max_element - max элемент по модулю матрицы A
    # idx_max_element - его индекс
    (max_element, idx_max_element) = (None, None)

    # max_diag_element - max диагональый элемент по модулю матрицы A
    # idx_max_diag_element - его индекс
    (max_diag_element, idx_max_diag_element) = (None, None)

    # 1 шаг - находим элементы, определенные выше
    for j in range(matrix_size):
        for i in range(j, matrix_size):
            if max_element is None or max_element < abs(matrix[i, j]):
                (max_element, idx_max_element) = (abs(matrix[i, j]), (i, j))

    for i in range(matrix_size):
        if max_diag_element is None or max_diag_element < abs(matrix[i, i]):
            (max_diag_element, idx_max_diag_element) = (abs(matrix[i, i]), (i, i))

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
