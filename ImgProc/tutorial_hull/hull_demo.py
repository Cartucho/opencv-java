import sys
import cv2
import numpy as np

src = None
src_gray = None
thresh = 100
max_thresh = 255


def thresh_callback(param):
    global thresh
    thresh = param

    # Detect edges using Threshold
    ret, threshold_output = cv2.threshold(src_gray, thresh, 255, cv2.THRESH_BINARY)

    # Find contours
    im2, contours, hierarchy = cv2.findContours(threshold_output, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # Find the convex hull object for each contour
    hull = np.empty_like(contours)
    for i in range(0, len(contours)):
        hull[i] = cv2.convexHull(contours[i])

    # Draw contours + hull results
    w, h = threshold_output.shape
    drawing = np.zeros((w, h, 3), dtype=np.uint8)
    for i in range(0, len(contours)):
        color = (np.random.uniform(0, 255), np.random.uniform(0, 255), np.random.uniform(0, 255))
        cv2.drawContours(drawing, contours, i, color, 1)
        cv2.drawContours(drawing, hull, i, color, 1)

    # Show in a window
    cv2.namedWindow('Hull demo', cv2.WINDOW_AUTOSIZE)
    cv2.imshow('Hull demo', drawing)

    pass


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nhull_demo.py  < path_to_image >'
        return -1

    # Load source image
    global src
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)
    if src is None:
        print 'Invalid input image'
        return -1

    # Convert image to gray and blur it
    global src_gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
    src_gray = cv2.blur(src_gray, (3, 3))

    # Create Window
    source_window = 'Source'
    cv2.namedWindow(source_window, cv2.WINDOW_AUTOSIZE)
    cv2.imshow(source_window, src)

    cv2.createTrackbar(' Threshold:', source_window, thresh, max_thresh, thresh_callback)

    thresh_callback(thresh)

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
