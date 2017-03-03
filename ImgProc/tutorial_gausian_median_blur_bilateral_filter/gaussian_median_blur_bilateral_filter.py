import sys
import cv2
import numpy as np

DELAY_CAPTION = 1500
DELAY_BLUR = 100
MAX_KERNEL_LENGTH = 31

src = None
dst = None
window_name = 'Smoothing Demo'


def main(argv):
    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    # Load the source image
    global src
    src = cv2.imread(sys.argv[1], 1)
    if src is None:
        print 'Usage:\ngaussian_median_blur_bilateral_filter.py <path_to_image>'
        return -1

    if display_caption('Original Image') != 0:
        return 0

    global dst
    dst = np.copy(src)
    if display_dst(DELAY_CAPTION) != 0:
        return 0

    # Applying Homogeneous blur
    if display_caption('Homogeneous Blur') != 0:
        return 0

    ## [blur]
    for i in xrange(1, MAX_KERNEL_LENGTH, 2):
        dst = cv2.blur(src, (i, i))
        if display_dst(DELAY_BLUR) != 0:
            return 0
    ## [blur]

    # Applying Gaussian blur
    if display_caption('Gaussian Blur') != 0:
        return 0

    ## [gaussian_blur]
    for i in xrange(1, MAX_KERNEL_LENGTH, 2):
        dst = cv2.GaussianBlur(src, (i, i), 0)
        if display_dst(DELAY_BLUR) != 0:
            return 0
    ## [gaussian_blur]

    # Applying Median blur
    if display_caption('Median Blur') != 0:
        return 0

    ## [median_blur]
    for i in xrange(1, MAX_KERNEL_LENGTH, 2):
        dst = cv2.medianBlur(src, i)
        if display_dst(DELAY_BLUR) != 0:
            return 0
    ## [median_blur]

    # Applying Bilateral Filter
    if display_caption('Bilateral Blur') != 0:
        return 0

    ## [bilateral_blur]
    # Remember, bilateral is a bit slow, so as value go higher, it takes long time
    for i in xrange(1, MAX_KERNEL_LENGTH, 2):
        dst = cv2.bilateralFilter(src, i, i * 2, i / 2)
        if display_dst(DELAY_BLUR) != 0:
            return 0
    ## [bilateral_blur]

    #  Wait until user press a key
    display_caption('End: Press a key!')
    cv2.waitKey(0)

    return 0


def display_caption(caption):
    global dst
    dst = np.zeros(src.shape, src.dtype)
    rows, cols, ch = src.shape
    cv2.putText(dst, caption,
                (cols / 4, rows / 2),
                cv2.FONT_HERSHEY_COMPLEX, 1, (255, 255, 255))
    
    return display_dst(DELAY_CAPTION)


def display_dst(delay):
    cv2.imshow(window_name, dst)

    c = cv2.waitKey(delay)
    if c >= 0:
        return -1

    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
