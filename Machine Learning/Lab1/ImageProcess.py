import cv2
import numpy as np
from scipy import ndimage


class ImageProcess:
    def __init__(self, cv_image):
        self.img = cv_image

    def save(self, path="result.jpg"):
        cv2.imwrite(path, self.img)

    def rotate(self, degrees=45):
        # always center
        self.img = ndimage.rotate(self.img, degrees)
        return self

    def translate(self, x=0, y=0):
        rows, cols = self.img.shape[:2]

        M = np.float32([[1, 0, x], [0, 1, -y]])
        self.img = cv2.warpAffine(self.img, M, (cols, rows))
        return self

    def flip(self, over_x=False, over_y=False):
        if over_x:
            self.img = cv2.flip(self.img, 0)
        if over_y:
            self.img = cv2.flip(self.img, 1)

        return self

    def shrink(self, factor=1.0):
        self.img = cv2.resize(self.img, (0, 0), fx=factor, fy=factor)
        return self
