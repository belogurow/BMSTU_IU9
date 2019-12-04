import logging
import os
import sys

import cv2
import numpy as np
from Image import Image

DIR_PATH_TO_NAME = {
    "/keanu": "Keanu Charles Reeves",
    "/presley": "Elvis Presley",
    "/cage": "Nicolas Cage",
    "/dicaprio": "Leonardo DiCaprio",
}

TRAIN_DIR_PATH = "train-data"
TEST_DIR_PATH = "test-data"


def prepare_training_data(data_dir_path):
    images = []
    dirs = os.listdir(data_dir_path)

    for dir_name in dirs:
        dir_name = "/" + dir_name
        subject_dir_path = data_dir_path + dir_name

        logging.debug(f'Process {subject_dir_path}')

        person_name = DIR_PATH_TO_NAME[dir_name]

        # process images by person
        for image_name in os.listdir(subject_dir_path):
            train_image = Image(subject_dir_path + "/" + image_name)

            logging.debug(f'Process \t{image_name}')
            cv2.imshow("Training on image...", cv2.resize(train_image.img, (400, 500)))
            cv2.waitKey(100)

            is_detected = train_image.detect_face(person_name)
            if is_detected:
                images.append(train_image)

    cv2.destroyAllWindows()
    cv2.waitKey(1)
    cv2.destroyAllWindows()
    return images


def draw_rectangle(img, rect):
    (x, y, w, h) = rect
    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)


def draw_text(img, text, x, y):
    cv2.putText(img, text, (x, y), cv2.FONT_HERSHEY_PLAIN, 1.5, (0, 255, 0), 2)


def predict(face_recognizer, names, data_dir_path):
    for image_name in os.listdir(data_dir_path):
        test_image = Image(data_dir_path + "/" + image_name)

        test_image.detect_face(image_name)

        # predict the image using our face recognizer
        name_index, _ = face_recognizer.predict(test_image.face)
        person_name = names[name_index]

        # draw a rectangle around face detected
        draw_rectangle(test_image.img, test_image.rect)
        # draw name of predicted person
        draw_text(test_image.img, str(person_name), test_image.rect[0], test_image.rect[1] - 5)
        cv2.imwrite('processed-' + image_name, test_image.img)
        cv2.imshow(str(person_name), cv2.resize(test_image.img, (400, 500)))

    cv2.waitKey(0)
    cv2.destroyAllWindows()
    cv2.waitKey(1)
    cv2.destroyAllWindows()


def init_logger():
    root = logging.getLogger()
    root.setLevel(logging.DEBUG)

    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    root.addHandler(handler)


if __name__ == "__main__":
    init_logger()

    logging.info("Prepare data")
    images = prepare_training_data(TRAIN_DIR_PATH)
    logging.info("Data prepared")

    logging.info("Start predict data")
    face_recognizer = cv2.face.LBPHFaceRecognizer_create()

    all_faces = list(map(lambda image: image.face, images))

    names = list(DIR_PATH_TO_NAME.values())
    name_indexes = list(map(lambda image: names.index(image.train_name), images))
    face_recognizer.train(all_faces, np.array(name_indexes))

    predict(face_recognizer, names, TEST_DIR_PATH)
    logging.info("Finish predict data")

