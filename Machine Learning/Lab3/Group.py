import Point


class Group:
    def __init__(self, points=None):
        if points is None:
            points = []

        self.points = points

    def add_point(self, point: Point):
        self.points.append(point)
