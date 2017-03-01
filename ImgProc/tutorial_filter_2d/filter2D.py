import sys
import cv2
import numpy as np


def main(argv):
    window_name = 'filter2D Demo'

    # Load an image
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    # Check if everything was fine
    if src is None:
        print 'Usage:\nfilter2D.py <path_to_image>'
        return -1

    # Create window
    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    ind = 0
    while True:
        c = cv2.waitKey(500)
        if c == 27:
            break

        # Update kernel size for a normalized box filter
        kernel_size = 3 + 2 * (ind % 5)
        kernel = np.ones((kernel_size, kernel_size), dtype=np.float32)
        kernel /= (kernel_size * kernel_size)

        # Apply filter
        dst = cv2.filter2D(src, -1, kernel)

        cv2.imshow(window_name, dst)
        ind += 1

    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
