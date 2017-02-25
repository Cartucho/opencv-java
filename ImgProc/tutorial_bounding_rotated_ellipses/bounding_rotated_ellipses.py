import sys
import cv2
import numpy as np

src = None
src_gray = None
thresh = 100
max_thresh = 255


def main(argv):
    global src
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Usage:\nbounding_rotated_ellipses.py <path_to_image>'
        return -1

    global src_gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
    src_gray = cv2.blur(src_gray, (3, 3))

    source_window = 'Source'
    cv2.namedWindow(source_window, cv2.WINDOW_AUTOSIZE)
    cv2.imshow(source_window, src)

    cv2.createTrackbar(' Threshold:', source_window, thresh, max_thresh, thresh_callback)

    thresh_callback(thresh)

    cv2.waitKey(0)
    return 0


def thresh_callback(param):
    global thresh
    thresh = param

    retVal, threshold_output = cv2.threshold(src_gray, thresh, 255, cv2.THRESH_BINARY)
    im2, contours, hierarchy = cv2.findContours(threshold_output, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    cont_size = len(contours)

    minRect = [None] * cont_size
    minEllipse = [None] * cont_size

    for i in range(0, cont_size):
        minRect[i] = cv2.minAreaRect(contours[i])
        if len(contours[i]) > 5:
            minEllipse[i] = cv2.fitEllipse(contours[i])

    w, h = threshold_output.shape
    drawing = np.zeros((w, h, 3), dtype=np.uint8)

    for i in range(0, cont_size):
        color = (np.random.uniform(0, 255), np.random.uniform(0, 255), np.random.uniform(0, 255))
        # contour
        cv2.drawContours(drawing, contours, i, color, 1)
        # ellipse
        if minEllipse[i] is not None:
            cv2.ellipse(drawing, minEllipse[i], color, 2, 8)
        # rotated rectangle
        rect_points = cv2.boxPoints(minRect[i])
        rect_points = np.int0(rect_points)
        cv2.drawContours(drawing, [rect_points], 0, color, 1)

    cv2.namedWindow('Contours', cv2.WINDOW_AUTOSIZE)
    cv2.imshow('Contours', drawing)

    pass


if __name__ == "__main__":
    main(sys.argv[1:])

