def find_ltl(matrix, method=bunch_parlett):
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

        blocks.append(matrix[0:n[k], 0:n[k]])

        matrix = matrix[n[k]:len(matrix), n[k]:len(matrix)]

        size_l = len(source_matrix_l) - len(matrix_l)
        source_matrix_l[size_l:len(source_matrix_l), size_l:len(source_matrix_l)] = matrix_l

        # переходим на следующую итерацию
        k += 1

    blocks.append(matrix)

    result_matrix_t = calc_result_diagonal_matrix(blocks)
    result_matrix_p = calc_result_matrix_p(matrixes_p, source_matrix_size)

    return source_matrix, result_matrix_p, result_matrix_t, source_matrix_l
