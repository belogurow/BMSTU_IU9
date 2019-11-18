import matplotlib.pyplot as plt
from sklearn.datasets.samples_generator import make_blobs

from HyperSVM import HyperSVM
from Point import Point
from Vector import Vector


def plot_vector(vector: Vector):
    plt.plot([vector.point_a.x, vector.point_b.x], [vector.point_a.y, vector.point_b.y])


def create_new_long_vector(vector: Vector, x_min, x_max):
    k, b = vector.get_vector_equation()

    new_y_first = Vector.get_next_point_y(k, b, x_min)
    new_y_last = Vector.get_next_point_y(k, b, x_max)

    return Vector(Point(x_min, new_y_first), Point(x_max, new_y_last))


def plot_result(result_vector_a: Vector, result_vector_b: Vector, main_vector: Vector, all_points: list):
    # Продлеваем вектора до [x_min; x_max]
    x_min = min(all_points[:, 0])
    x_max = max(all_points[:, 0])

    result_vector_a = create_new_long_vector(result_vector_a, x_min, x_max)
    result_vector_b = create_new_long_vector(result_vector_b, x_min, x_max)
    main_vector = create_new_long_vector(main_vector, x_min, x_max)

    plot_vector(result_vector_a)
    plot_vector(result_vector_b)
    plot_vector(main_vector)


if __name__ == "__main__":
    X, Y = make_blobs(n_samples=300, centers=2,
                      random_state=4, cluster_std=0.40)
    plt.scatter(X[:, 0], X[:, 1], c=Y, s=50, cmap='spring')

    svm = HyperSVM(X, Y)
    svm.fit()
    print(svm.predict(Point(9, 3.5)))

    plot_result(svm.result_vector_a, svm.result_vector_b, svm.main_vector, X)
    # plt.show()
    plt.savefig('output.png')
