import math


class Point:
    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y

    def distance_to(self, another_point):
        return math.hypot(another_point.x - self.x, another_point.y - self.y)

    def find_middle_point_to(self, another_point):
        middle_x = (self.x + another_point.x) / 2
        middle_y = (self.y + another_point.y) / 2

        return Point(middle_x, middle_y)

    def __str__(self):
        return f'({self.x}, {self.y})'
