from copy import deepcopy

from Group import Group
from Point import Point
from Vector import Vector


class HyperSVM:
    def __init__(self, X, Y):
        self.X = X
        self.Y = Y

        self.group_positive = None
        self.group_negative = None

        self.result_vector_a = None
        self.result_vector_b = None
        self.middle_point = None
        self.main_vector = None

        self.create_groups()

    def create_groups(self):
        self.group_positive = Group()
        self.group_negative = Group()

        for (point, y) in zip(self.X, self.Y):
            if y == 0:
                self.group_negative.add_point(Point(point[0], point[1]))
            else:
                self.group_positive.add_point(Point(point[0], point[1]))

    def fit(self):
        opposite_pos = None
        opposite_neg = None
        min_distance = float('INF')

        # finding a central point between groups
        for point_pos in self.group_positive.points:
            for point_neg in self.group_negative.points:
                length = point_pos.distance_to(point_neg)
                if length < min_distance:
                    min_distance = length
                    opposite_pos = point_pos
                    opposite_neg = point_neg

        self.middle_point = opposite_pos.find_middle_point_to(opposite_neg)

        vector_a = Vector(opposite_neg, opposite_pos)
        vector_b = Vector(opposite_pos, opposite_neg)

        step_degree = 1
        current_degrees = 0

        max_margin = float("-inf")
        degrees_result = 0


        while current_degrees < 360:
            current_degrees += step_degree

            vector_a.rotate(step_degree)
            vector_b.rotate(step_degree)

            distance_between_vectors = Vector.distance_between_parallel_lines(vector_a, vector_b)
            points_satisfies_neg = vector_a.points_satisfies(self.group_negative.points)
            points_satisfies_pos = vector_b.points_satisfies(self.group_positive.points)

            if distance_between_vectors > max_margin and points_satisfies_neg and points_satisfies_pos:
                max_margin = distance_between_vectors
                degrees_result = deepcopy(current_degrees)
                self.result_vector_a = deepcopy(vector_a)
                self.result_vector_b = deepcopy(vector_b)

            print(f'{current_degrees} {distance_between_vectors} {points_satisfies_pos} {points_satisfies_neg}')

        # create main vector
        main_vector_pos = Vector(self.middle_point, opposite_pos)
        main_vector_neg = Vector(self.middle_point, opposite_neg)
        main_vector_pos.rotate(degrees_result)
        main_vector_neg.rotate(degrees_result)
        self.main_vector = Vector(main_vector_pos.point_b, main_vector_neg.point_b)

    def predict(self, point: Point):
        signed_val = (self.main_vector.point_b.x - self.main_vector.point_a.x) * (point.y - self.main_vector.point_a.y) - (self.main_vector.point_b.y - self.main_vector.point_a.y) * (point.x - self.main_vector.point_a.x)
        selected_group = 0 if signed_val >= 0 else 1
        return selected_group
