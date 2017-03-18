import sys
import cv2
import math
import numpy as np

src = None
edges = None
min_threshold = 50
max_trackbar = 150

standard_name = 'Standard Hough Lines Demo'
probabilistic_name = 'Probabilistic Hough Lines Demo'

s_trackbar = max_trackbar
p_trackbar = max_trackbar


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nhough_lines.py  < path_to_image >'
        return -1

    global src
    # Read the image
    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Invalid input image'
        return -1

    # Convert it to gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)

    global edges
    # Apply Canny edge detector
    edges = cv2.Canny(src_gray, 50, 200, None, 3)

    # Create Trackbars for Thresholds
    thresh_label = "Thres: %d + input" % (min_threshold)

    cv2.namedWindow(standard_name, cv2.WINDOW_AUTOSIZE)
    cv2.createTrackbar(thresh_label, standard_name,
                       s_trackbar, max_trackbar, Standard_Hough)

    cv2.namedWindow(probabilistic_name, cv2.WINDOW_AUTOSIZE)
    cv2.createTrackbar(thresh_label, probabilistic_name,
                       p_trackbar, max_trackbar, Probabilistic_Hough)

    # Default start
    Standard_Hough(s_trackbar)
    Probabilistic_Hough(p_trackbar)

    cv2.waitKey(0)
    return 0


def Standard_Hough(param):
    global s_trackbar
    s_trackbar = param

    # clone the colour, input image for displaying purposes
    display = np.copy(src)
    # 1. Use Standard Hough Transformm
    s_lines = cv2.HoughLines(edges, 1, np.pi / 180, min_threshold + s_trackbar, None, 0, 0)

    # Show the result
    if s_lines is not None:
        for i in range(0, len(s_lines)):
            r = s_lines[i][0][0]
            t = s_lines[i][0][1]
            cos_t = math.cos(t)
            sin_t = math.sin(t)
            x0 = r * cos_t
            y0 = r * sin_t
            alpha = 1000

            pt1 = (int(x0 + alpha*(-sin_t)), int(y0 + alpha*cos_t))
            pt2 = (int(x0 - alpha*(-sin_t)), int(y0 - alpha*cos_t))

            cv2.line(display, pt1, pt2, (255,0,0), 3, cv2.LINE_AA)

    cv2.imshow(standard_name, display)


def Probabilistic_Hough(param):
    global p_trackbar
    p_trackbar = param

    # clone the colour, input image for displaying purposes
    display = np.copy(src)
    # 1. Use Standard Hough Transformm
    p_lines = cv2.HoughLinesP(edges, 1, np.pi / 180, min_threshold + p_trackbar, None, 30, 10)

    # Show the result
    if p_lines is not None:
        for i in range(0, len(p_lines)):
            l = p_lines[i][0]
            cv2.line(display, (l[0], l[1]), (l[2], l[3]), (255,0,0), 3, cv2.LINE_AA)

    cv2.imshow(probabilistic_name, display)

if __name__ == "__main__":
    main(sys.argv)

