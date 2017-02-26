import sys
import cv2
import numpy as np

src = None
src_gray = None
lowThreshold = 0
max_lowThreshold = 100
ratio = 3
kernel_size = 3
window_name = 'Edge Map'


def CannyThreshold(param):
    global lowThreshold
    lowThreshold = param

    detected_edges = cv2.blur(src_gray, (3, 3))

    detected_edges = cv2.Canny(detected_edges, lowThreshold, lowThreshold*ratio, kernel_size)

    dst = cv2.bitwise_and(src, src, mask=detected_edges)

    cv2.imshow(window_name, dst)

    pass


def main(argv):
    global src
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Usage:\ncanny_detector_demo.py <path_to_image>'
        return -1

    global src_gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)

    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    cv2.createTrackbar('Min Threshold:', window_name, lowThreshold, max_lowThreshold, CannyThreshold)

    CannyThreshold(lowThreshold)

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
