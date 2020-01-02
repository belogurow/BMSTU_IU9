from itertools import combinations
from Lab7.CharacteristicFunction import CharacteristicFunction
import math

N = 4

FUNCTIONS = [
    CharacteristicFunction([], 0),
    CharacteristicFunction([1], 4),
    CharacteristicFunction([2], 3),
    CharacteristicFunction([3], 2),
    CharacteristicFunction([4], 3),
    CharacteristicFunction([1, 2], 8),
    CharacteristicFunction([1, 3], 6),
    CharacteristicFunction([1, 4], 7),
    CharacteristicFunction([2, 3], 5),
    CharacteristicFunction([2, 4], 7),
    CharacteristicFunction([3, 4], 6),
    CharacteristicFunction([1, 2, 3], 10),
    CharacteristicFunction([1, 2, 4], 12),
    CharacteristicFunction([1, 3, 4], 10),
    CharacteristicFunction([2, 3, 4], 9),
    CharacteristicFunction([1, 2, 3, 4], 13)
]

CORRECTED_FUNCTIONS = [
    CharacteristicFunction([], 0),
    CharacteristicFunction([1], 2),
    CharacteristicFunction([2], 2),
    CharacteristicFunction([3], 1),
    CharacteristicFunction([4], 3),
    CharacteristicFunction([1, 2], 5),
    CharacteristicFunction([1, 3], 7),
    CharacteristicFunction([1, 4], 8),
    CharacteristicFunction([2, 3], 6),
    CharacteristicFunction([2, 4], 8),
    CharacteristicFunction([3, 4], 7),
    CharacteristicFunction([1, 2, 3], 10),
    CharacteristicFunction([1, 2, 4], 12),
    CharacteristicFunction([1, 3, 4], 11),
    CharacteristicFunction([2, 3, 4], 11),
    CharacteristicFunction([1, 2, 3, 4], 16)
]


def is_additive(functions, all_combinations, n):
    result = True
    print('\nПроверка на супераддитивность:')

    for i in all_combinations:
        for j in all_combinations:
            if not i.intersection(j):
                value_i = CharacteristicFunction.get_value(functions, i)
                value_j = CharacteristicFunction.get_value(functions, j)
                value_ij = CharacteristicFunction.get_value(functions, i.union(j))
                is_additive_value = value_ij > value_i + value_j
                print(f'\tv{i.union(j)} > v{i} + v{j} : {value_ij} > {value_i} + {value_j} : {is_additive_value}')

                result &= is_additive_value

    value_all = CharacteristicFunction.get_value(functions, {1, 2, 3, 4})
    value_sum = sum([CharacteristicFunction.get_value(functions, {i}) for i in range(1, n + 1)])
    is_additive_value = value_all > value_sum
    print(f'\tv{{1, 2, 3, 4}} > v{{1}} + v{{2}} + v{{3}} + v{{4}} : {value_all} > {value_sum} : {is_additive_value}')

    result &= is_additive_value
    return result


def is_convexity(functions, all_combinations, n):
    result = True
    print('\nПроверка на выпуклость:')

    for i in all_combinations:
        for j in all_combinations:
            union = i.union(j)
            intersection = i.intersection(j)

            if 0 < len(intersection) < len(i) and len(j) > len(intersection):
                value_union = CharacteristicFunction.get_value(functions, union)
                value_intersection = CharacteristicFunction.get_value(functions, intersection)

                value_i = CharacteristicFunction.get_value(functions, i)
                value_j = CharacteristicFunction.get_value(functions, j)

                is_convexity_value = value_union + value_intersection < value_i + value_j
                print(
                    f'\tv{union} + v{intersection} < v{i} + v{j} : {value_union} + {value_intersection} < {value_i} + {value_j} : {is_convexity_value}')

                result &= is_convexity_value

    return result


def find_vector_sheply(functions, all_combinations, n):
    print("\nВычисление вектора Шепли:")
    vector = [0] * (n + 1)

    for i in range(1, n + 1):
        for j in all_combinations:
            value_j = CharacteristicFunction.get_value(functions, j)
            value_diff_j = CharacteristicFunction.get_value(functions, j.difference({i}))

            vector[i] += math.factorial(len(j) - 1) * math.factorial(n - len(j)) * (value_j - value_diff_j)

    vector = list(map(lambda x: round(x / math.factorial(n), 3), vector[1:5]))
    print(f'\t{vector}')

    return vector


def start_game(functions, n):
    print("\nНачало игры для:")
    for fun in functions:
        print(fun)

    all_combinations = []
    for i in range(1, n + 1):
        comb_tuple = list(map(set, combinations([1, 2, 3, 4], i)))
        all_combinations.extend(comb_tuple)

    is_additive_value = is_additive(functions, all_combinations, n)
    if is_additive_value:
        print('Игра супераддитивна')
    else:
        print('Игра не является супераддитивной, требуется коррекция!')
        return

    is_convexity_value = is_convexity(functions, all_combinations, n)
    if is_convexity_value:
        print('Игра выпуклая')
    else:
        print('Игра не является выпуклой, требуется коррекция!')

    vector_sheply = find_vector_sheply(functions, all_combinations, n)

    print('\nПроверка на групповую рационализацию:')
    print(f'\tsum{vector_sheply} = {sum(vector_sheply)} : v{{I}} = 16')

    print('\nПроверка на индивидуальную рационализацию:')
    for idx, i in enumerate(vector_sheply):
        print(f'\tx{idx} = {i} >= v{idx} = {CharacteristicFunction.get_value(functions, {idx + 1})}')


if __name__ == '__main__':
    start_game(FUNCTIONS, 4)
    start_game(CORRECTED_FUNCTIONS, 4)
