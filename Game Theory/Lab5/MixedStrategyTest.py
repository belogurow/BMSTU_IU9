import unittest

import numpy as np

from Lab5.MixedStategyMethod import MixedStrategyMethod


def var_2():
    return np.array([[4, 4, 0, 6, 12],
                     [1, 14, 14, 13, 11],
                     [17, 6, 14, 4, 3],
                     [18, 16, 13, 15, 16]])


def from_manual():
    return np.array([[1, 3, 9, 6],
                     [2, 6, 2, 3],
                     [7, 2, 6, 5]])


class MixedStrategyTest(unittest.TestCase):

    def test_from_manual(self):
        matrix = var_2()

        mixed = MixedStrategyMethod(matrix)
        mixed.start()

        self.assertEqual(1, round(np.sum(mixed.result_A)))
        self.assertEqual(1, round(np.sum(mixed.result_B)))
