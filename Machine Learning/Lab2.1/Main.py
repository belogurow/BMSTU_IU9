import cv2
import numpy as np

from Piece import Piece

# HEIGHT, WIDTH
PIECE_SHAPE = (40, 60)

GREEN_COLOR = (0, 255, 0)


def proccess(img, pieces, method='teng'):
    if method == 'teng':
        teng_vals = [piece.teng() for piece in pieces]

        threshold = np.max(teng_vals) * 0.05
        for piece in pieces:
            if piece.teng_val < threshold:
                piece.is_blured = True
                cv2.rectangle(img, piece.top_left_coords, piece.bottom_right_coords, GREEN_COLOR)
            else:
                piece.is_blured = False

    elif method == 'glvn':
        glvn_vals = [piece.glvn() for piece in pieces]

        threshold = np.max(glvn_vals) * 0.06
        for piece in pieces:
            if piece.glvn_val < threshold:
                piece.is_blured = True
                cv2.rectangle(img, piece.top_left_coords, piece.bottom_right_coords, GREEN_COLOR)
            else:
                piece.is_blured = False

    else:
        raise ValueError(f'Unknown method - {method}')

    cv2.imwrite(f'output-{method}.jpg', img)

    # Если кол-во размытых частей больше 70%,
    # то считаем изображение размытым
    is_blured_pieces = list(filter(lambda piece: piece.is_blured, pieces))

    return len(is_blured_pieces) / len(pieces) > 0.7




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

    print(proccess(img, pieces, method='teng'))
    print(proccess(img, pieces, method='glvn'))

