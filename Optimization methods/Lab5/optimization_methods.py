from sys import stderr
import numpy as _np
from scipy import optimize as _opt


_optimization_methods = {
    'gs': 'gaussian_seidel',
    'fr': 'fletcher_reeves'
}


def _gaussian_seidel(f, x, h, eps, disp):
    """
    Minimization of scalar function of one or more variables using the
    Gaussian-Seidel algorithm.
    Arguments
    ---------
    f : function
    x : numpy array
        Function arguments
    h : numpy array
        Search step
    eps : float
        Tolerance. Must be > 0
    """
    n = _np.size(x)
    Z = 0.5
    k = 0
    while k < 1000:
        k += 1
        if disp:
            print('Iteration: %d' % k, end='\n\n')

        x = _np.append(x, [x[k - 1]], 0)

        for i in range(n):
            if disp:
                print('Step: %d' % (i + 1), end='\n\n')

            def _f(_x):
                x[k][i] = _x
                return f(x[k])

            res = _opt.minimize_scalar(_f, tol=eps)
            minimum = None
            if res.success:
                minimum = res.x
            else:
                raise Exception(
                    'Failed to find minimum on ' +
                    i + ' step of iteration ' + k)
            if disp:
                print('Minimum: %f' % minimum)

            x[k][i] = minimum  # * h[i]
            if disp:
                print('x_%d = ' % k, x[k])
                print('f(x_%d) = %f' % (k, f([x for x in x[k]])), end='\n\n')

        if _np.allclose(x[k], x[k - 1], eps, eps):
            if _np.linalg.norm(x[k] - x[k - 1]) < eps:
                return (x[-1], f([x for x in x[-1]]))
            else:
                for i in range(n):
                    h[i] *= Z
                if disp:
                    print('h = ', h, end='\n\n')


def _fletcher_reeves(f, x, h, eps, disp):
    results = _opt.minimize(f, x[0], method='cg',
                            tol=eps, options={'disp': disp})
    x_min = None
    if results.success:
        x_min = results.x
    else:
        raise Exception('Failed to optimize this function')

    return (x_min, f(x_min))


def optimize_multidimential_function(f, method, x0, h0, eps, disp=False):
    """
    Minimization of scalar function of one or more variables
    using one of avalible methods: 1) Gaussian-Seidel
                                   2) Fletcher-Reeves
    ---------
    Arguments
    ---------
    f : function
        Function to optimize
    method : string
        One of these strings: 1) 'gs' for Gaussian-Seidel
                              2) 'fr' for Fletcher-Reeves
    x0 : list
        Function arguments
    h0 : list
        Search steps
    eps : float
        Tolerance. Must be > 0
    disp : bool
        Display values while computing
    """
    n = len(x0)
    if n != len(h0):
        raise Exception('Number of points of x0 is not equal h')

    if not (eps > 0):
        raise Exception('eps <= 0')

    x = _np.array([x0], dtype=_np.float)
    h = _np.array(h0, dtype=_np.float)

    method = method.lower()
    if method in _optimization_methods:
        try:
            if _optimization_methods[method] == 'gaussian_seidel':
                return _gaussian_seidel(f, x, h, eps, disp)
            elif _optimization_methods[method] == 'fletcher_reeves':
                return _fletcher_reeves(f, x, h, eps, disp)
        except Exception as error:
            print(error, file=stderr)
            raise Exception('Failed to optimize this function')
    else:
        raise Exception('There is no such method ' + str(method))