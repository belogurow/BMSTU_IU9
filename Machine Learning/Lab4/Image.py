import cv2


class Image:
    def __init__(self, path):
        self.img = cv2.imread(path)

        self.face = None
        self.rect = None
        self.train_name = None

    def detect_face(self, name):
        gray = cv2.cvtColor(self.img, cv2.COLOR_BGR2GRAY)

        # load OpenCV face detector
        face_cascade = cv2.CascadeClassifier('lbpcascade_frontalface.xml')
        faces = face_cascade.detectMultiScale(gray, scaleFactor=1.2, minNeighbors=5)

        # if no faces are detected then return original img
        if len(faces) == 0:
            return False

        (x, y, w, h) = faces[0]

        self.face = gray[y:y + w, x:x + h]
        self.rect = faces[0]
        self.train_name = name

        return True
