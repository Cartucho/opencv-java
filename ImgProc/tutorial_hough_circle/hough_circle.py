import sys
import cv2
import numpy as np

src = None
src_gray = None

cannyThreshold = 200
accumulatorThreshold = 50
maxCannyThreshold = 255
maxAccumulatorThreshold = 200

windowName = 'Hough Circle Detection Demo'
cannyThresholdTrackbarName = 'Canny threshold'
accumulatorThresholdTrackbarName = 'Accumulator Threshold'


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nhough_circle.py  < path_to_image >'
        return -1

    global src
    # Read the image
    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Invalid input image'
        return -1

    global src_gray
    # Convert it to gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)

    # Reduce the noise so we avoid false circle detection
    src_gray = cv2.GaussianBlur(src_gray, (9, 9), 2, None, 2)

    #  Create the main window
    cv2.namedWindow(windowName, cv2.WINDOW_AUTOSIZE)

    # Create Erosion Trackbar
    cv2.createTrackbar(cannyThresholdTrackbarName, windowName,
                       cannyThreshold, maxCannyThreshold, cannyThresh_update)

    cv2.createTrackbar(accumulatorThresholdTrackbarName, windowName,
                       accumulatorThreshold, maxAccumulatorThreshold,
                       accumulatorThresh_update)

    # Default start
    cannyThresh_update(cannyThreshold)

    cv2.waitKey(0)
    return 0


def cannyThresh_update(param):
    global cannyThreshold
    cannyThreshold = max(param, 1)
    hough_detection()


def accumulatorThresh_update(param):
    global accumulatorThreshold
    accumulatorThreshold = max(param, 1)
    hough_detection()


def hough_detection():
    # runs the actual detection
    circles = cv2.HoughCircles(src_gray, cv2.HOUGH_GRADIENT, 1, src_gray.shape[0] / 8,
                               param1=cannyThreshold, param2=accumulatorThreshold,
                               minRadius=0, maxRadius=0)

    # clone the colour, input image for displaying purposes
    display = np.copy(src)

    if circles is not None:
        circles = np.uint16(np.around(circles))
        for i in circles[0, :]:
            center = (i[0], i[1])
            radius = i[2]
            # circle center
            cv2.circle(display, center, 3, (0, 255, 0), -1)
            # circle outline
            cv2.circle(display, center, radius, (0, 0, 255), 3)

    cv2.imshow(windowName, display)


if __name__ == "__main__":
    main(sys.argv)

