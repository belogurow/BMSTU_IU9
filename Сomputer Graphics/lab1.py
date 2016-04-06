import random

from pyglet.window import mouse
from pyglet import *
from pyglet.gl import *
import pyglet
import math

class Circle():
    def __init__(self, x=300, y=100):
        self.x = x
        self.y = y
        self.red = random.random()
        self.green = 0.2
        self.blue = random.random()
        self.rad = random.randint(30.0, 100.0)
        self.alpha = 1.0
        #self.draw_circle()

    def draw_circle(self):
        glColor4f(self.red, self.green, self.blue, self.alpha)
        glEnable(GL_BLEND)
        """glBegin(GL_TRIANGLE_FAN)
        glVertex2f(self.x, self.y)
        for i in range(2000):
            t = 2 * math.pi * i / 2000
            glVertex2f(self.x + math.sin(t) * self.rad, self.y + math.cos(t) * self.rad)
        glEnd()"""
        glBegin(GL_POLYGON)
        for i in range(25):
            t = 2 * math.pi * i / 25
            glVertex2f(self.x + math.sin(t) * self.rad, self.y + math.cos(t) * self.rad)
        glEnd()
        glDisable(GL_BLEND)
        glFlush()

circles = []

window = pyglet.window.Window(800, 600, resizable=False, caption='Lab 1')
label = pyglet.text.Label("Count: " + str(len(circles)),
                          font_name="Forte",
                          font_size=36,
                          y=window.height - 50)

pyglet.gl.glClearColor(0.1, 1, 0.5, 1)

@window.event
def on_mouse_press(x, y, button, modifiers):
    if button == mouse.LEFT:
        circles.append(Circle(x, y))
        label.text = "Count: " + str(len(circles))
@window.event
def on_mouse_drag(x, y, dx, dy, buttons, modifiers):
    if buttons == mouse.LEFT:
        circles.append(Circle(x, y))
        label.text = "Count: " + str(len(circles))

@window.event
def on_draw():
    window.clear()
    global circles
    for item in circles:
        item.draw_circle()
    label.draw()

def update(dt):
    for item in circles:
        if item.alpha <= 0.3:
            circles.pop(0)
            label.text = "Count: " + str(len(circles))
        else:
            item.alpha -= 0.07

if __name__ == '__main__':
    pyglet.clock.schedule_interval(update, 1 / 15)
    pyglet.app.run()