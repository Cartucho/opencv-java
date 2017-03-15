import sys
import cv2


def main(argv):

    source_window = 'Source Image'
    equalized_window = 'Equalized Image'

    # Load image
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nequalize_hist.py  < path_to_image >'
        return -1

    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Can\'t read the image'
        return -1

    # Convert to grayscale
    src = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)

    # Apply Histogram Equalization
    dst = cv2.equalizeHist(src)

    #  Display results
    cv2.namedWindow(source_window, cv2.WINDOW_AUTOSIZE)
    cv2.namedWindow(equalized_window, cv2.WINDOW_AUTOSIZE)

    cv2.imshow(source_window, src)
    cv2.imshow(equalized_window, dst)

    # Wait until user exits the program
    cv2.waitKey(0)

    return 0


if __name__ == "__main__":
    main(sys.argv)
