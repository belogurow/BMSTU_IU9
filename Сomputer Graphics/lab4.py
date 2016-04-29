import pyglet
import numpy
from pyglet.gl import *
from pyglet.window import mouse
from pyglet.window import key


window = pyglet.window.Window(1300, 700, resizable=True, caption='Lab 4')

stack = []
vertex = []
data = (GLfloat * (3*window.width*window.height))()
# data = numpy.empty(window.height * window.width * 3, dtype=GLfloat)
#data = array.array('B', [0] * 3*window.width*window.height)
#print(data[0])

sign = lambda x: 1 if (x > 1) else -1 if (x < 0) else 0
color = lambda x, y: 1 if data[(y * window.width + x) * 3 + 0] == 1 else 0


def clear_data():
    global data
    del data
    data = (GLfloat * (3*window.width*window.height))(0)


def put_pixel(x, y, intens=1):
    # glColor3f(0.2, 1, 1)
    # glBegin(GL_POINTS)
    # glVertex2f(x, y)
    # glEnd()
    # glFlush()
    #print("(%d, %d)" % (x, y))
    for i in range(3):
        data[(y * window.width + x) * 3 + i] = intens
    #data[(y * window.width + x) * 3 + 3] = 1


def bresenham(x1, y1, x2, y2):
    dy = (y2 - y1)
    dx = (x2 - x1)
    sign_x = sign(dx)
    sign_y = sign(dy)
    dx = abs(dx)
    dy = abs(dy)
    if dx > dy:
        pdx = sign_x
        pdy = 0
        es = dy
        el = dx
    else:
        pdx = 0
        pdy = sign_y
        es = dx
        el = dy
    x = x1
    y = y1
    err = el
    put_pixel(x, y)
    for i in range(el):
        err -= 2 * es
        if err <= 0:
            err += 2 * el
            x += sign_x
            put_pixel(x, y)
            y += sign_y
        else:
            x += pdx
            put_pixel(x, y)
            y += pdy
        # print(str(x) + " " + str(y))
        put_pixel(x, y)


def test_border(x, y, x_right):
    while x <= x_right:
        flag = False
        while data[(y * window.width + x) * 3 + 0] != 1.0 and x < x_right:
            if not flag:
                flag = True
            x += 1
        if flag:
            if x == x_right and data[(y * window.width + x) * 3 + 0] != 1.0:
                stack.append([x, y, 1])
            else:
                stack.append([x - 1, y, 1])
        else:
            flag = False
        x_entry = x
        while data[(y * window.width + x) * 3 + 0] == 1.0 and x < x_right:
            x += 1
        if x == x_entry:
            x += 1


def test_border_up(x, y):
    """while data[((y+1)*window.width + x - 1)*3+0] != 1:
        x -= 1
        #print(x)
    if stack.count([x, y+1, 1]) == 0:
        stack.append([x, y+1, 1])
    while data[((y-1)*window.width + x0 - 1)*3+0] != 1:
        x -= 1
    if stack.count([x0, y-1, 1]) == 0:
        stack.append([x, y-1, 1])
    #print("test border : " + str(stack[:len(stack)]))"""
    #put_pixel(x, y)
    y += 1
    #if data[(y*window.width + x) * 3 + 0] != 1:
    #    test_border_up(x, y)
    while data[(y*window.width + x-1) * 3 + 0] != 1:
        x -= 1
    if stack.count([x, y, 1]) == 0:
        stack.append([x, y, 1])
    """if data[((y+1)*window.width + (x-1)) * 3 + 0] != 1:
        test_border_up(x, y)
    if data[((y+1)*window.width + (x+1)) * 3 + 0] != 1:
        test_border_up(x, y)
    if data[((y-1)*window.width + (x-1)) * 3 + 0] != 1:
        test_border_up(x, y)
    if data[((y-1)*window.width + (x+1)) * 3 + 0] != 1:
        test_border_up(x, y)"""


def my_test_border(x, y):
    #if data[(y*window.width + x) * 3 + 0] != 1:
    #    my_test_border(x, y)
    while data[(y*window.width + (x-1)) * 3 + 0] != 1:
        x -= 1
    if stack.count([x, y, 1]) == 0:
        stack.append([x, y, 1])


def my_test_border2(x_left, x_right, y):
    if data[(y * window.width + x_right) * 3 + 0] == 1:
        while data[(y * window.width + x_right) * 3 + 0] == 1:
            x_right -= 1
    else:
        while data[(y * window.width + x_right) * 3 + 0] != 1:
            x_right += 1
        x_right -= 1

    if data[(y * window.width + x_left) * 3 + 0] == 1:
        while data[(y * window.width + x_left) * 3 + 0] == 1:
            x_left += 1
    else:
        while data[(y * window.width + x_left) * 3 + 0] != 1:
            x_left -= 1
        x_left += 1

    if x_left <= x_right:
        #stack.append([x_left, y])
        x = x_left
        fill = False
        while x <= x_right:
            stack.append([x, y])
            while data[(y * window.width + x) * 3 + 0] != 1:
                x += 1
            if x-1 == x_right:
                break
            while data[(y * window.width + x) * 3 + 0] == 1:
                x += 1


def postfiltr(x, y):
    left_up = color(x-1, y+1)
    up = color(x, y+1)
    right_up = color(x+1, y+1)
    left = color(x-1, y)
    this = color(x, y)
    right = color(x+1, y)
    left_down = color(x-1, y-1)
    down = color(x, y-1)
    right_down = color(x+1, y-1)
    intens = this/2 + (left_up + up + right_up + left + right + left_down + down + right_down)/16
    put_pixel(x-1, y+1, intens=intens/16)
    put_pixel(x, y+1, intens=intens/16)
    put_pixel(x+1, y+1, intens=intens/16)
    put_pixel(x-1, y, intens=intens/16)
    put_pixel(x, y, intens=intens/2)
    put_pixel(x, y+1, intens=intens/16)
    put_pixel(x-1, y-1, intens=intens/16)
    put_pixel(x, y-1, intens=intens/16)
    put_pixel(x+1, y-1, intens=intens/16)


@window.event
def on_key_press(symbol, modifiers):
    if symbol == key.SPACE:
        clear_data()
        vertex.clear()


@window.event
def on_mouse_press(x, y, button, modifiers):
    if button == mouse.LEFT:
        # data = (GLfloat * (3*window.width*window.height))(0)
        vertex.append([x, y])
    if button == mouse.RIGHT:
        # global x0, y0
        # x0, y0 = x, y
        stack.append([x, y, 1])


@window.event
def on_resize(width, height):
    if height == 0:
        height = 1
    glViewport(0, 0, width, height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluOrtho2D(0, width, 0, height)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    # print("New: %d %d" % (width,height))
    # print("New_window %d %d" % (window.width,window.height))
    # data.fill(0)
    # if x0!=-1 & y0!=-1:
    #    stack.append([x0,y0])
    # test = lambda stack: [x for x in stack] if len(stack)>0 else "Null"
    # print(test(stack))


@window.event
def on_draw():
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    # glLoadIdentity()
    # glEnable(GL_DEPTH_TEST)
    clear_data()
    #bresenham(0, 0, 7, 5)
    if len(vertex) >= 2:
        for i in range(len(vertex) - 1):
            bresenham(vertex[i][0], vertex[i][1], vertex[i + 1][0], vertex[i + 1][1])
        bresenham(vertex[len(vertex) - 1][0], vertex[len(vertex) - 1][1], vertex[0][0], vertex[0][1])
    bresenham(150, 100, 200, 90)
    bresenham(200, 90, 168, 133)
    bresenham(168, 133, 160, 70)
    bresenham(160, 70, 150, 125)
    bresenham(150, 125, 150, 100)

    bresenham(200, 380, 202, 380)
    bresenham(202, 380, 202, 400)
    bresenham(202, 400, 200, 400)
    bresenham(200, 400, 200, 380)
    # glReadBuffer(GL_FRONT)
    # data = (GLubyte * (3*window.width*window.height))(0)
    # data = (GLfloat * (3*window.width*window.height))(0)
    """while len(stack) > 0:
        x, y = stack[len(stack) - 1][0], stack[len(stack) - 1][1]
        stack.pop()
        if data[(y * window.width + x) * 3 + 0] != 1.0:
            my_test_border(x, y-1)
            my_test_border(x, y+1)
        put_pixel(x, y)
        x0 = x
        while data[(y * window.width + (x - 1)) * 3 + 0] != 1.0:
            x -= 1
            put_pixel(x, y)
        x = x0
        while data[(y * window.width + (x + 1)) * 3 + 0] != 1.0:
            x += 1
            put_pixel(x, y)
            #test_border_up(x, y)
            my_test_border(x, y-1)
            my_test_border(x, y+1)
    """
    # TODO change feel polygon
    while len(stack) > 0:
        x, y = stack[len(stack) - 1][0], stack[len(stack) - 1][1]
        stack.pop()
        put_pixel(x, y)
        x0 = x
        while data[(y * window.width + (x - 1)) * 3 + 0] != 1.0:
            x -= 1
            put_pixel(x, y)
        x_left = x
        x = x0
        while data[(y * window.width + (x + 1)) * 3 + 0] != 1.0:
            x += 1
            put_pixel(x, y)
        x_right = x
        my_test_border2(x_left, x_right, y+1)
        my_test_border2(x_left, x_right, y-1)


    # glDrawPixels(window.width, window.height, GL_RGB, GL_FLOAT, (GLfloat * len(data))(*data))
    #glDrawPixels(window.width, window.height, GL_RGB, GL_INT, data)
    #for x in range(1, 500, 2):
    #    for y in range(1, 500, 2):
    #        postfiltr(x ,y)
    glDrawPixels(window.width, window.height, GL_RGB, GL_FLOAT, data)
    glFlush()


if __name__ == "__main__":
    pyglet.app.run()
