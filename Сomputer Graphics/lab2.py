import pyglet
from pyglet.gl import *
from pyglet.window import key
import math
import random

window = pyglet.window.Window(1300, 600, resizable=True, caption='Lab 2')

vertex_side = []
vertex_bottom = []
k = 1
slices = 50
stacks = 5
angle_y = angle_z = angle_x = 0
tr_z = tr_y = tr_x = 0
button = key.Y
space = False


def calc_cone(radius=50, height=150, slices=50, stacks=5):
    vertex_side.clear()
    vertex_bottom.clear()
    betta = math.atan(radius / height)
    c = radius / math.sin(betta)
    l = [(c / stacks) * i for i in range(stacks + 1)]
    for j in range(len(l) - 1):
        for i in range(slices):
            polygon = []
            phi_1 = 2 * math.pi * i / slices
            phi_2 = 2 * math.pi * (i + 1) / slices
            theta = math.pi - betta
            polygon.append([l[j] / 2 * math.cos(phi_1) * math.sin(theta),
                            height + l[j] * math.cos(theta),
                            l[j] * math.sin(phi_1) * math.sin(theta)])
            polygon.append([l[j + 1] / 2 * math.cos(phi_1) * math.sin(theta),
                            height + l[j + 1] * math.cos(theta),
                            l[j + 1] * math.sin(phi_1) * math.sin(theta)])
            polygon.append([l[j + 1] / 2 * math.cos(phi_2) * math.sin(theta),
                            height + l[j + 1] * math.cos(theta),
                            l[j + 1] * math.sin(phi_2) * math.sin(theta)])
            polygon.append([l[j] / 2 * math.cos(phi_2) * math.sin(theta),
                            height + l[j] * math.cos(theta),
                            l[j] * math.sin(phi_2) * math.sin(theta)])
            vertex_side.append(polygon)

    r = [(radius / stacks) * i for i in range(stacks + 1)]
    for j in range(len(r) - 1):
        for i in range(slices):
            polygon = []
            alpha_1 = 2 * math.pi * i / slices
            alpha_2 = 2 * math.pi * (i + 1) / slices
            polygon.append([math.cos(alpha_1) * r[j] / 2, 0, math.sin(alpha_1) * r[j]])
            polygon.append([math.cos(alpha_1) * r[j + 1] / 2, 0, math.sin(alpha_1) * r[j + 1]])
            polygon.append([math.cos(alpha_2) * r[j + 1] / 2, 0, math.sin(alpha_2) * r[j + 1]])
            polygon.append([math.cos(alpha_2) * r[j] / 2, 0, math.sin(alpha_2) * r[j]])
            vertex_bottom.append(polygon)
    print("calc")


def draw_cone():
    glColor3f(1, 0, 0)
    for polygon in vertex_side:
        glBegin(GL_POLYGON)
        for vertex in polygon:
            glVertex3f(vertex[0], vertex[1], vertex[2])
        glEnd()

    glColor3f(0, 1, 0)
    for polygon in vertex_bottom:
        glBegin(GL_POLYGON)
        for vertex in polygon:
            glVertex3f(vertex[0], vertex[1], vertex[2])
        glEnd()


def draw_cube(side=100):
    dy = dx = dz = side
    x0 = y0 = z0 = - side / 2
    glBegin(GL_QUADS)
    glColor3f(0.0, 0.0, 1.0)  # blue
    glVertex3f(x0, y0 + dy, z0)
    glVertex3f(x0, y0 + dy, z0 + dz)
    glVertex3f(x0 + dx, y0 + dy, z0 + dz)
    glVertex3f(x0 + dx, y0 + dy, z0)

    glColor3f(0.0, 1.0, 1.0)  # cyan
    glVertex3f(x0, y0, z0)
    glVertex3f(x0 + dx, y0, z0)
    glVertex3f(x0 + dx, y0, z0 + dz)
    glVertex3f(x0, y0, z0 + dz)

    glColor3f(0.0, 1.0, 0.0)  # green
    glVertex3f(x0, y0, z0 + dz)
    glVertex3f(x0 + dx, y0, z0 + dz)
    glVertex3f(x0 + dx, y0 + dy, z0 + dz)
    glVertex3f(x0, y0 + dy, z0 + dz)

    glColor3f(1.0, 1.0, 0.0)  # yellow
    glVertex3f(x0, y0, z0)
    glVertex3f(x0, y0, z0 + dz)
    glVertex3f(x0, y0 + dy, z0 + dz)
    glVertex3f(x0, y0 + dy, z0)

    glColor3f(1.0, 1.0, 1.0)  # white
    glVertex3f(x0 + dx, y0, z0)
    glVertex3f(x0 + dx, y0 + dy, z0)
    glVertex3f(x0 + dx, y0 + dy, z0 + dz)
    glVertex3f(x0 + dx, y0, z0 + dz)

    glColor3f(1.0, 0.0, 0.0)  # red
    glVertex3f(x0, y0, z0)
    glVertex3f(x0, y0 + dy, z0)
    glVertex3f(x0 + dx, y0 + dy, z0)
    glVertex3f(x0 + dx, y0, z0)
    glEnd()


@window.event
def on_text_motion(motion):
    global k, angle_y, angle_z, angle_x, tr_z, tr_y, tr_x, button
    if button == key.X:
        if motion == key.MOTION_UP:
            tr_x += 10
        if motion == key.MOTION_DOWN:
            tr_x -= 10
        if motion == key.MOTION_RIGHT:
            angle_x += 3
        if motion == key.MOTION_LEFT:
            angle_x -= 3
    elif button == key.Y:
        if motion == key.MOTION_UP:
            tr_y += 10
        if motion == key.MOTION_DOWN:
            tr_y -= 10
        if motion == key.MOTION_RIGHT:
            angle_y += 3
        if motion == key.MOTION_LEFT:
            angle_y -= 3
    elif button == key.Z:
        if motion == key.MOTION_UP:
            tr_z += 10
        if motion == key.MOTION_DOWN:
            tr_z -= 10
        if motion == key.MOTION_RIGHT:
            angle_z += 3
        if motion == key.MOTION_LEFT:
            angle_z -= 3


@window.event
def on_key_press(symbol, modifiers):
    global space, button, slices, k, stacks
    if symbol == key.X or symbol == key.Z or symbol == key.Y:
        button = symbol
    if symbol == key.NUM_6:
        slices += 5
        calc_cone(slices=slices, stacks=stacks)
    if symbol == key.NUM_4 and slices > 5:
        slices -= 5
        calc_cone(slices=slices, stacks=stacks)
    if symbol == key.NUM_3:
        stacks += 2
        calc_cone(slices=slices, stacks=stacks)
    if symbol == key.NUM_1 and stacks > 2:
        stacks -= 2
        calc_cone(slices=slices, stacks=stacks)
    if symbol == key.NUM_8:
        k *= 1.03
    if symbol == key.NUM_2:
        k *= 0.97
    if symbol == key.SPACE:
        if space:
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            space = False
        else:
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            space = True


projection = [1.5, 0, 0, 0,
              0, 1.5, 0, 0,
              0, 0, 1.73, 0,
              0, 0, 1, 2]
calc_cone()


@window.event
def on_resize(width, height):
    glViewport(0, 0, width, height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glMultTransposeMatrixf((GLfloat * 16)(*projection))
    gluPerspective(65, width / float(height), .1, 1000)
    # glMultTransposeMatrixf((GLfloat*16)(*translate))
    # glFrustum(-20, 20, -20, 20, .1, 100)
    # glMultMatrixf((GLfloat*16)(*translate))
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    return pyglet.event.EVENT_HANDLED


@window.event
def on_draw():
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glLoadIdentity()
    glEnable(GL_DEPTH_TEST)

    glTranslatef(0, 0, -250)

    glBegin(GL_LINE_LOOP)
    glColor3f(1, 0, 0)  # red - x
    glVertex3f(0, 0, 0)
    glVertex3f(500, 0, 0)
    glEnd()
    glBegin(GL_LINE_LOOP)
    glColor3f(0, 1, 0)  # green - y
    glVertex3f(0, 0, 0)
    glVertex3f(0, 500, 0)
    glEnd()
    glBegin(GL_LINE_LOOP)
    glColor3f(0, 0, 1)  # blue - z
    glVertex3f(0, 0, 0)
    glVertex3f(0, 0, 500)
    glEnd()

    glTranslatef(-100, 0, 100)
    glRotatef(67, 0, 1, 0)

    draw_cube()

    glRotatef(-67, 0, 1, 0)
    glTranslatef(180, -70, 0)
    glTranslatef(tr_x, tr_y, tr_z)
    glRotatef(angle_z, 0, 0, 1)
    glRotatef(angle_x, 1, 0, 0)
    glRotatef(angle_y, 0, 1, 0)
    glScalef(k, k, k)

    #calc_cone(vertex_side, vertex_bottom, 50, 200, slices, stacks)
    draw_cone()
    glFlush()


if __name__ == "__main__":
    pyglet.app.run()