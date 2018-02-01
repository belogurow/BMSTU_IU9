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
