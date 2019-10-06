import cv2

from Lab1.ImageProcess import ImageProcess

if __name__ == "__main__":
    img = cv2.imread('silniy_kot.jpg')

    new_image = ImageProcess(img) \
        .translate(x=10, y=30) \
        .rotate(degrees=45) \
        .flip(over_y=True) \
        .shrink(factor=0.8) \
        .save()
