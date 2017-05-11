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

    # Detect edges using canny
    canny_output = cv2.Canny(src_gray, thresh, thresh * 2, 3)
    # Find contours
    im2, contours, hierarchy = cv2.findContours(canny_output, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # Get the moments
    mu = np.empty_like(contours)
    for i in range(0, len(contours)):
        mu[i] = cv2.moments(contours[i])

    # Get the mass centers:
    mc = np.empty_like(contours)
    for i in range(0, len(contours)):
        if mu[i]['m00'] != 0:
            mc[i] = (int(mu[i]['m10'] / mu[i]['m00']), int(mu[i]['m01'] / mu[i]['m00']))

    # Draw contours
    w, h = canny_output.shape
    drawing = np.zeros((w, h, 3), dtype=np.uint8)
    for i in range(0, len(contours)):
        color = (np.random.uniform(0, 255), np.random.uniform(0, 255), np.random.uniform(0, 255))
        cv2.drawContours(drawing, contours, i, color, 2)
        if mc[i] is not None:
            cv2.circle(drawing, mc[i], 4, color, -1)

    # Show in a window
    cv2.namedWindow('Contours', cv2.WINDOW_AUTOSIZE)
    cv2.imshow('Contours', drawing)

    # Calculate the area with the moments 00 and compare with the result of the OpenCV function
    print "\t Info: Area and Contour Length"
    for i in range(0, len(contours)):
        print " * Contour[%d] - Area (M_00) = %.2f - Area OpenCV: %.2f - Length: %.2f" % (
            i, mu[i]['m00'], cv2.contourArea(contours[i]), cv2.arcLength(contours[i], True))

    pass


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nfind_contours.py  < path_to_image >'
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

    cv2.createTrackbar(' Canny thresh:', source_window, thresh, max_thresh, thresh_callback)

    thresh_callback(thresh)

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])

