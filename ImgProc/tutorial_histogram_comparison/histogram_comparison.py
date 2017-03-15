import sys
import cv2
import numpy as np


def main(argv):

    # Load three images with different environment settings
    if len(sys.argv) < 4:
        print 'Not enough parameters'
        print 'Usage:\nhistogram_comparison.py  < image_settings0 > < image_settings1 > < image_settings2 >'
        return -1

    src_base = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)
    src_test1 = cv2.imread(sys.argv[2], cv2.IMREAD_COLOR)
    src_test2 = cv2.imread(sys.argv[3], cv2.IMREAD_COLOR)

    if src_base is None or src_test1 is None or src_test2 is None:
        print 'Can\'t read one of the images'
        return -1

    # Convert to HSV
    hsv_base = cv2.cvtColor(src_base, cv2.COLOR_BGR2HSV)
    hsv_test1 = cv2.cvtColor(src_test1, cv2.COLOR_BGR2HSV)
    hsv_test2 = cv2.cvtColor(src_test2, cv2.COLOR_BGR2HSV)

    rows, cols, ch = hsv_base.shape
    hsv_half_down = hsv_base[rows/2:rows-1, 0:cols-1]

    # Using 50 bins for hue and 60 for saturation
    h_bins = 50
    s_bins = 60
    histSize = np.array([h_bins, s_bins])

    # hue varies from 0 to 179, saturation from 0 to 255
    h_ranges = np.array([0, 180])
    s_ranges = np.array([0, 256])

    ranges = np.concatenate([h_ranges, s_ranges])

    # Use the o-th and 1-st channels

    channels = np.array([0, 1])

    # Histograms
    hist_base = None

    # Calculate the histograms for the HSV images
    hist_base = cv2.calcHist([hsv_base], channels, None, histSize, ranges, None, False)
    hist_base = cv2.normalize(hist_base, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX)

    hist_half_down = cv2.calcHist([hsv_half_down], channels, None, histSize, ranges, None, False)
    hist_half_down = cv2.normalize(hist_half_down, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX)

    hist_test1 = cv2.calcHist([hsv_test1], channels, None, histSize, ranges, None, False)
    hist_test1 = cv2.normalize(hist_test1, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX)

    hist_test2 = cv2.calcHist([hsv_test2], channels, None, histSize, ranges, None, False)
    hist_test2 = cv2.normalize(hist_test2, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX)

    # Apply the histogram comparison methods
    for i in range(0, 4):
        compare_method = i
        base_base = cv2.compareHist(hist_base, hist_base, compare_method)
        base_half = cv2.compareHist(hist_base, hist_half_down, compare_method)
        base_test1 = cv2.compareHist(hist_base, hist_test1, compare_method)
        base_test2 = cv2.compareHist(hist_base, hist_test2, compare_method)

        print ' Method [%d] Perfect, Base-Half, Base-Test(1), Base-Test(2)' \
              ' : %f, %f, %f, %f' % (i, base_base, base_half , base_test1, base_test2)

    print 'Done \n'

    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
