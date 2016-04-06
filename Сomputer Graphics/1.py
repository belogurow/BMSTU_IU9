import pyglet
from pyglet.gl import *

window = pyglet.window.Window(800, 600, caption = 'Lab №1')

@window.event()
def on_draw():
 #glClearColor(0.0, 0.0, 0.0, 0.0)
    window.clear()
    glBegin (GL_POLYGON)
    glColor3f (1.0, 1.0, 0.0)
    glVertex2f(50,50)
    glVertex2f(50,175)
    glVertex2f(175,175)
    glVertex2f(175,50)
    glEnd()

pyglet.app.run()