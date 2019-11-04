import matplotlib.pyplot as plt
import numpy as np


def get_default_signal():
    # Исходный сигнал
    K = 100
    k = np.arange(0, K)
    x_min = 0
    x_max = np.pi
    x = x_min + k * (x_max - x_min) / K
    f = np.sin(x) + 0.5

    return K, k, x_min, x_max, x, f


def get_noise_amplitude():
    # Амплитуда равномерного шума
    a = 0.5 / 2
    return a


def get_convolution_weight_discretization():
    # Дискретизация веса свертки
    L = 10
    l = np.arange(0, L)
    lambda_val = l / L
    return lambda_val


def get_probability_extremum():
    # Вероятность попадания в окрестность экстремума
    P = 0.95
    return P


def get_uncertainty_interval():
    # Интервал неопределенности
    epsilon = 0.01
    return epsilon


def get_sliding_window_size():
    # Размер скользящего окна
    r_3 = 3
    r_5 = 5
    return r_3, r_5


def cacl_f_weight_mean(r, f_with_sigma, alpha=None):
    # Считаем взвешенное скользящее среднее
    M = int((r - 1) / 2)
    if alpha is None:
        alpha = [1 / r] * r

    f_weight_mean = []
    for k in range(M, len(f_with_sigma) - M):
        f_temp = []
        for j in range(k - M, k + M + 1):
            f_temp.append(f_with_sigma[j] ** alpha[j + M - k])

        f_weight_mean.append(np.prod(f_temp))

    return f_weight_mean


def calc_noisy_criterion(f_with_sigma):
    # Считаем критерий зашумленности по "Манхеттонской" метрике
    omega = 0
    for k in range(1, len(f_with_sigma)):
        omega += abs(f_with_sigma[k] - f_with_sigma[k - 1])

    return omega


def calc_proximity_criterion(f_with_sigma, f_weight_mean):
    # Считаем критерий близости по "Манхеттонской" метрике
    delta = 0
    for k in range(1, len(f_weight_mean)):
        delta += abs(f_with_sigma[k] - f_weight_mean[k])

    return delta / len(f_with_sigma)


if __name__ == "__main__":
    np.set_printoptions(precision=2)

    K, k, x_min, x_max, x, f = get_default_signal()
    plt.grid()
    plt.plot(k, f, label='Исходный сигнал')
    plt.legend()
    plt.show()

    # Добавляем шум
    a = get_noise_amplitude()
    sigma = np.random.uniform(low=-a, high=a, size=(K,))
    f_with_sigma = f + sigma

    plt.grid()
    plt.plot(k, f, label='Исходный сигнал', alpha=0.3)
    plt.plot(k, f_with_sigma, label='Исходный сигнал плюс шум')
    plt.legend()
    plt.show()

    # Среднее геометрическое для окна r = 3 и r = 5
    r_3, r_5 = get_sliding_window_size()
    f_weight_mean_3 = cacl_f_weight_mean(r_3, f_with_sigma)
    f_weight_mean_5 = cacl_f_weight_mean(r_5, f_with_sigma)

    plt.grid()
    plt.plot(k, f, label='Исходный сигнал', alpha=0.3)
    plt.plot(k, f_with_sigma, label='Исходный сигнал плюс шум', alpha=0.3)
    plt.plot(k[1:-1], f_weight_mean_3, label='$\widetilde{f_k}$ с окном $r=3$')
    plt.plot(k[2:-2], f_weight_mean_5, label='$\widetilde{f_k}$ с окном $r=5$')
    plt.legend()
    plt.show()

    # Подбираем alpha
    J = []
    omegas = []
    deltas = []
    dists = []
    lambdas = get_convolution_weight_discretization()

    for lambda_val in lambdas:
        alpha_center = lambda_val
        alpha_prev_next = (1 - alpha_center) / 2
        alpha = np.around([alpha_prev_next, alpha_center, alpha_prev_next], decimals=2)

        f_weight_mean_3_alpha = cacl_f_weight_mean(r_3, f_weight_mean_3, alpha)
        omega = calc_noisy_criterion(f_weight_mean_3_alpha)
        delta = calc_proximity_criterion(f_with_sigma, f_weight_mean_3_alpha)
        dist = abs(omega) + abs(delta)

        J_local = lambda_val * delta + (1 - lambda_val) * delta

        J.append(J_local)
        omegas.append(omega)
        deltas.append(delta)
        dists.append(dist)

        plt.figure(figsize=(20, 3))
        plt.grid()
        plt.title(
            f'$\\alpha =$ {alpha}, $\omega = {omega:.3f}$, $\delta = {delta:.3f}$, dist = {dist:.3f}, J = {J_local:.3f}')
        plt.plot(k, f_with_sigma, label='Исходный сигнал плюс шум', alpha=0.3)
        plt.plot(k[2:-2], f_weight_mean_3_alpha, label='Взвешенный сигнал')
        plt.show()

    # Зависимость sigma, delta от lambda
    plt.grid()
    plt.title(f'Зависимость $(\omega, \delta)$ от $\lambda$')
    plt.plot(lambdas, omegas, label='$\omega$')
    plt.plot(lambdas, deltas, label='$\delta$')
    plt.legend()
    plt.ylabel('$(\omega, \delta)$')
    plt.xlabel('$(\lambda)$')
    plt.show()

    # Зависимость расстояния от lambda
    plt.grid()
    plt.title(f'Зависимость расстояния от $\lambda$')
    plt.plot(lambdas, dists)
    plt.ylabel('dists')
    plt.xlabel('$\lambda$')
    plt.show()


    P = get_probability_extremum()
    epsilon = get_uncertainty_interval()
    N = np.log(1 - P) / np.log(1 - (epsilon / (x_max - x_min)))

    print(f'Число испытаний = {N}')
    print(f'Минимальное значение расстояния = {min(np.around(dists, 2))}')
    test = 0
