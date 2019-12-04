import cv2


class Piece:
    def __init__(self, image, top_left_coords, bottom_right_coords):
        self.top_left_coords = top_left_coords
        self.bottom_right_coords = bottom_right_coords
        self.piece = image[top_left_coords[1]:bottom_right_coords[1], top_left_coords[0]:bottom_right_coords[0]]

        self.teng_val = None
        self.glvn_val = None
        self.is_blured = None

    def teng(self):
        ksize = 5
        Gx = cv2.Sobel(self.piece, ddepth=cv2.CV_64F, dx=1, dy=0, ksize=ksize)
        Gy = cv2.Sobel(self.piece, ddepth=cv2.CV_64F, dx=0, dy=1, ksize=ksize)
        FM = Gx * Gx + Gy * Gy
        self.teng_val = cv2.mean(FM)[0]

        return self.teng_val

    def glvn(self):
        # FM = np.square(cv2.meanStdDev(self.piece)) / cv2.mean(self.piece)
        # self.glvn_val = cv2.mean(FM)[0]

        mean, stdev = cv2.meanStdDev(self.piece)
        self.glvn_val = (stdev[0] ** 2 / mean[0])[0]

        return self.glvn_val
