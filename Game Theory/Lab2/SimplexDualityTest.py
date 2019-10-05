import unittest

from Lab1.SimplexMatrix import SimplexMatrix, FCondition, AConditionB
from Lab1.SimplexMethod import SimplexMethod
from Lab2.DualSimplexMethod import DualSimplexMethod


def from_manual():
    c = [-4, -18, -30, -5]
    A = [[3, 1, -4, -1],
         [-2, -4, -1, 1]]
    b = [[-3],
         [-3]]

    result = -36
    return A, b, c, result


def var_2():
    c = [2, 5, 3]
    A = [[2, 1, 2],
         [1, 2, 0],
         [0, 0.5, 1]]
    b = [[6],
         [6],
         [2]]

    result = 16.75
    return A, b, c, result


class SimplexDualityTest(unittest.TestCase):

    def test_simplex_from_manual(self):
        A, b, c, result = from_manual()

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        simplex_result = SimplexMethod(matrix).start()
        dual_simplex_result = DualSimplexMethod(matrix).start()

        self.assertEqual(result, round(simplex_result, 5))
        self.assertEqual(round(simplex_result, 5), round(dual_simplex_result, 5))

    def test_var_2(self):
        A, b, c, result = var_2()

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        simplex_result = SimplexMethod(matrix).start()
        dual_simplex_result = DualSimplexMethod(matrix).start()

        self.assertEqual(result, round(simplex_result, 2))
        self.assertEqual(simplex_result, dual_simplex_result)
