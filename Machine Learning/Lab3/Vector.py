import math

from copy import deepcopy
from Point import Point


class Vector:
    def __init__(self, point_a: Point, point_b: Point):
        self.point_a = deepcopy(point_a)
        self.point_b = deepcopy(point_b)

    def rotate(self, degrees):
        radians = math.radians(degrees)

        # rotate around point 'a'
        around_point = self.point_a

        ox, oy = around_point.x, around_point.y
        px, py = self.point_b.x, self.point_b.y

        qx = ox + math.cos(radians) * (px - ox) - math.sin(radians) * (py - oy)
        qy = oy + math.sin(radians) * (px - ox) + math.cos(radians) * (py - oy)

        self.point_b.x = qx
        self.point_b.y = qy

    def get_line_equation(self):
        # Ax + By + C = 0
        A = self.point_a.y - self.point_b.y
        B = self.point_b.x - self.point_a.x
        C = self.point_a.x * self.point_b.y - self.point_b.x * self.point_a.y

        return A, B, C

    def get_vector_equation(self):
        # y = kx + b
        k = (self.point_b.y - self.point_a.y) / (self.point_b.x - self.point_a.x)
        b = self.point_a.y - k * self.point_a.x

        return k, b

    @staticmethod
    def get_next_point_y(k, b, next_point_x):
        next_point_y = k * next_point_x + b
        return next_point_y

    @staticmethod
    def distance_between_parallel_lines(vector_a, vector_b):
        A_1, B_1, C_1 = vector_a.get_line_equation()
        A_2, B_2, C_2 = vector_b.get_line_equation()

        return abs(C_2 + C_1) / math.sqrt(A_1 ** 2 + B_1 ** 2)

    def points_satisfies(self, points):
        signs = []
        for point in points:
            signed_val = (self.point_b.x - self.point_a.x) * (point.y - self.point_a.y) - (self.point_b.y - self.point_a.y) * (point.x - self.point_a.x)
            signs.append(signed_val)
        return all(sign >= 0 for sign in signs) or all(sign <= 0 for sign in signs)
