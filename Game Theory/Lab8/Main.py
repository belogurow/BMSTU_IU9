import random
import numpy as np
from sinkhorn_knopp import sinkhorn_knopp as skp

N = 7
epsilon = 0.001  # погрешность


def generate_stochastic_matrix():
    sk = skp.SinkhornKnopp()

    return sk.fit(np.random.rand(N, N))


def generate_random_vector():
    return random.sample(range(1, 20), N)


def calculate_mentions(matrix, vector):
    i = 0

    while True:
        i += 1
        vector_pred = vector.copy()
        vector = np.dot(matrix, vector)
        vector_diff = np.abs(vector_pred - vector)

        if np.average(vector_diff) < epsilon:
            break

    print(f"Результирующие мнения игроков {vector}", )


if __name__ == "__main__":
    trust_matrix = generate_stochastic_matrix()
    print(f"Матрица доверия \n{trust_matrix}")

    mentions_vector = generate_random_vector()
    print(f"Мнения агентов \n{mentions_vector}")

    calculate_mentions(trust_matrix, mentions_vector)