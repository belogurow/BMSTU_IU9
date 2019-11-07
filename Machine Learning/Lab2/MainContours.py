import math

import cv2
import numpy as np

GREEN_COLOR = (0, 255, 0)


def prepare_approx(approx):
    approx_temp = np.vstack((approx, approx[:2]))
    approx_cont_with_angle = []

    for i in range(len(approx)):
        p1 = approx_temp[i][0]
        p2 = approx_temp[i + 1][0]
        p3 = approx_temp[i + 2][0]

        v0 = p2 - p1
        v1 = p2 - p3
        angle = np.math.atan2(np.linalg.det([v0, v1]), np.dot(v0, v1)) * (180.0 / math.pi)
        if angle < 0:
            angle += 360.0

        approx_cont_with_angle.append([p2, angle])

    approx_cont_with_angle.append(approx_cont_with_angle[0])
    all_contours = []
    cont = []
    for i, item in enumerate(approx_cont_with_angle):
        if item[1] < 180:
            cont.append(item[0])
        else:
            if len(cont) != 0:
                all_contours.append(cont)
            cont = []

    if len(cont) != 0:
        all_contours.append(cont)

    return all_contours


def create_rectangle(cont):
    p1 = cont[0]
    p2 = cont[1]
    p3 = cont[2]
    p4 = np.array([p3[0] + p1[0] - p2[0], p3[1] + p1[1] - p2[1]])
    return [p1, p2, p3, p4]


if __name__ == "__main__":
    img = cv2.imread('input.jpg')
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 1
    _, thresh = cv2.threshold(img_gray, 220, 230, 3)
    cnts, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # 2
    # filter = cv2.bilateralFilter(img_gray, 1, 1, 11)
    # edged = cv2.Canny(filter, 46, 350, apertureSize=3)
    # cnts = cv2.findContours(edged.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # cnts = imutils.grab_contours(cnts)

    # loop over our contours
    for i, c in enumerate(cnts):
        # approximate the contour
        peri = cv2.arcLength(c, True)
        area = cv2.contourArea(c)
        approx = cv2.approxPolyDP(c, 0.006 * peri, True)

        if len(approx) >= 4:
            all_contours = prepare_approx(approx)
            all_contours = filter(lambda cont: len(cont) >= 3, all_contours)
            for cont in all_contours:
                if len(cont) == 3:
                    cont = create_rectangle(cont)
                cv2.drawContours(img, [np.vstack((cont))], 0, GREEN_COLOR, 2)

    cv2.imwrite('output.jpg', img)
