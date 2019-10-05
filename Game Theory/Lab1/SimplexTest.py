import unittest

import numpy as np

from Lab1.Conditions import FCondition, AConditionB
from Lab1.SimplexMatrix import SimplexMatrix
from Lab1.SimplexMethod import SimplexMethod


def var_2():
    c = np.array([2, 5, 3])
    A = np.array([[2, 1, 2],
                  [1, 2, 0],
                  [0, 0.5, 1]])
    b = np.array([[6],
                  [6],
                  [2]])

    result = 16.75
    return A, b, c, result


def from_manual():
    c = np.array([-1, 1])
    A = np.array([[1, -2],
                  [-2, 1],
                  [1, 1]])
    b = np.array([[2],
                  [-2],
                  [5]])

    result = 3
    return A, b, c, result


class SimplexDualityTest(unittest.TestCase):

    def test_var_2_max_less_or_equals(self):
        A, b, c, result = var_2()

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        method = SimplexMethod(matrix)

        simplex_result = method.start()

        self.assertEqual(result, simplex_result)
        self.assertEqual(6, len(method.iterations))

    def test_var_2_min_less_or_equals(self):
        A, b, c, result = var_2()

        c = -c
        result = -result

        matrix = SimplexMatrix(A, b, c, FCondition.MIN, AConditionB.LESS_OR_EQUAL)
        method = SimplexMethod(matrix)

        simplex_result = method.start()

        self.assertEqual(result, simplex_result)
        self.assertEqual(6, len(method.iterations))

    def test_var_2_max_greater_or_equals(self):
        A, b, c, result = var_2()

        A = -A
        b = -b

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.GREATER_OR_EQUAL)
        method = SimplexMethod(matrix)

        simplex_result = method.start()

        self.assertEqual(result, simplex_result)
        self.assertEqual(6, len(method.iterations))

    def test_var_2_min_greater_or_equals(self):
        A, b, c, result = var_2()

        A = -A
        b = -b
        c = -c
        result = -result

        matrix = SimplexMatrix(A, b, c, FCondition.MIN, AConditionB.GREATER_OR_EQUAL)
        method = SimplexMethod(matrix)

        simplex_result = method.start()

        self.assertEqual(result, simplex_result)
        self.assertEqual(6, len(method.iterations))

    def test_from_manual(self):
        A, b, c, result = from_manual()

        result = -result

        matrix = SimplexMatrix(A, b, c, FCondition.MIN, AConditionB.LESS_OR_EQUAL)
        method = SimplexMethod(matrix)

        simplex_result = method.start()

        self.assertEqual(result, simplex_result)
