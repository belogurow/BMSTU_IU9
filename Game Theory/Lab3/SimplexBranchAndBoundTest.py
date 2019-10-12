import unittest

from Lab1.Conditions import AConditionB, FCondition
from Lab1.SimplexMatrix import SimplexMatrix
from Lab3.BranchAndBoundSimplexMethod import BranchAndBoundSimplexMethod
from Lab3.BruteForceMethod import BruteForceMethod


def from_manual():
    A = [[6, -1],
         [2, 5]]
    b = [[12],
         [20]]
    c = [12, -1]

    result = 24.0
    return A, b, c, result


def var_2():
    c = [2, 5, 3]
    A = [[2, 1, 2],
         [1, 2, 0],
         [0, 0.5, 1]]
    b = [[6],
         [6],
         [2]]

    result = 15.0
    return A, b, c, result


class SimplexBranchAndBoundTest(unittest.TestCase):

    def test_from_manual(self):
        A, b, c, result = from_manual()

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        branch_and_bound_simplex_result = BranchAndBoundSimplexMethod(matrix).start()

        self.assertEquals(result, branch_and_bound_simplex_result)

    def test_var_2(self):
        A, b, c, result = var_2()

        matrix = SimplexMatrix(A, b, c, FCondition.MAX, AConditionB.LESS_OR_EQUAL)
        branch_and_bound_simplex_result = BranchAndBoundSimplexMethod(matrix).start()

        self.assertEquals(result, branch_and_bound_simplex_result)

    def test_from_manual_brute_force(self):
        A, b, c, result = from_manual()

        matrix = SimplexMatrix(A, b, c)
        brute_force_method = BruteForceMethod(matrix)
        result = brute_force_method.start()

        self.assertEquals(result, result)

    def test_var_2_brute_force(self):
        A, b, c, result = var_2()

        matrix = SimplexMatrix(A, b, c)
        brute_force_method = BruteForceMethod(matrix)
        result = brute_force_method.start()

        self.assertEquals(result, result)
