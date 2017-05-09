import math


# Quadrature trapezoidal formula
def method_of_trapezoid(a=0, b=1, n=2):
    h = (b - a) / n
    list_of_x = [a + i * h for i in range(1, n)]

    series_of_sum = 0
    for i in range(0, n - 1):
        series_of_sum += math.exp(list_of_x[i])

    return h * ((math.exp(a) + math.exp(b)) / 2 + series_of_sum)


# Simpson's rule
def method_of_simpson(a=0, b=1, n=2):
    h = (b - a)/n
    list_of_x1 = [a + h * i  - h/2 for i in range(1, n+1)]
    list_of_x2 = [a + h * i for i in range(1, n)]

    series_of_sum = 0
    for i in range(0, n):
        series_of_sum += 4 * math.exp(list_of_x1[i])
        if i < n-1:
            series_of_sum += 2 * math.exp(list_of_x2[i])

    return h/6 * (math.exp(a) + math.exp(b) + series_of_sum)


# Method of medium rectangles
def method_of_rectangles(a=0, b=1, n=2):
    h = (b - a)/n

    result = 0
    for i in range(0, n):
        result += h * math.exp(a + h * i + h/2)

    return result


# Clarification on Richardson
def get_richardson(i_n, i_n2, k):
    return (i_n - i_n2) / (2 ** k - 1)


def calc_integral(epsilon, method, k):
    for i in range(len(epsilon)):
        print("\nEpsilon: " + str(epsilon[i]))
        n = 1
        richardson = float('inf')
        iteration = i_n = 0

        while abs(richardson) >= epsilon[i]:
            n *= 2
            i_n2 = i_n
            i_n = method(n=n)

            richardson = get_richardson(i_n, i_n2, k)
            iteration += 1

        print(" Iteration: " + str(iteration))
        print(" Result: " + str(i_n))
        print(" Result with Richardson: " + str(i_n + richardson))


if __name__ == "__main__":
    eps = [10 ** (-i) for i in range(1, 4)]

    print("\nMethod of trapezoid:")
    calc_integral(eps, method_of_trapezoid, 2)

    print("\nMethod of rectangles:")
    calc_integral(eps, method_of_rectangles, 2)

    print("\nMethod of Simpson:")
    calc_integral(eps, method_of_simpson, 4)


