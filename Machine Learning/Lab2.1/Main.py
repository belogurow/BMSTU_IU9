import cv2
import numpy as np

from Piece import Piece

# HEIGHT, WIDTH
PIECE_SHAPE = (40, 60)

GREEN_COLOR = (0, 255, 0)


def proccess_teng(img, pieces, method='teng'):
    teng_vals = [piece.teng() for piece in pieces]

    threshold = np.max(teng_vals) * 0.05
    for piece in pieces:
        if piece.teng_val < threshold:
            cv2.rectangle(img, piece.top_left_coords, piece.bottom_right_coords, GREEN_COLOR)

    cv2.imwrite(f'output-{method}.jpg', img)


def crop_image(image, shape=PIECE_SHAPE):
    height, width = shape
    imgheight, imgwidth, _ = image.shape

    pieces = []
    for j in range(0, imgwidth, width):
        for i in range(0, imgheight, height):
            piece = Piece(image, (j, i), (j + width, i + height))
            pieces.append(piece)

    return pieces


if __name__ == "__main__":
    img = cv2.imread('input.jpg')
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    pieces = crop_image(img)
    proccess_teng(img, pieces)

    [piece.glvn() for piece in pieces]
    # cv2.imwrite('output.jpg', img)
