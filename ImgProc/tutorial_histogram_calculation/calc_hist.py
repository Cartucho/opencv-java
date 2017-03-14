import sys
import cv2
import numpy as np


def main(argv):
    # Load the source image
    src = cv2.imread(sys.argv[1], 1)

    if src is None:
        print 'Usage:\ncalc_hist.py <path_to_image>'
        return -1

    # Separate the image in 3 places ( B, G and R )
    b, g, r = cv2.split(src)

    # Establish the number of bins
    histSize = 256

    # Set the ranges ( for B,G,R) )
    ranges = np.array([0, 256])

    accumulate = False

    # Compute the histograms:
    b_hist = cv2.calcHist([b], [0], None, [histSize], ranges, None, accumulate)
    g_hist = cv2.calcHist([g], [0], None, [histSize], ranges, None, accumulate)
    r_hist = cv2.calcHist([r], [0], None, [histSize], ranges, None, accumulate)

    # Draw the histograms for B, G and R
    hist_w = 512
    hist_h = 400
    bin_w = round(hist_w / histSize)

    histImage = np.zeros((hist_h, hist_w, 3), np.uint8)

    # Normalize the result to [ 0, histImage.rows ]
    b_hist = cv2.normalize(b_hist, None, alpha=0, beta=hist_h, norm_type=cv2.NORM_MINMAX)
    g_hist = cv2.normalize(g_hist, None, alpha=0, beta=hist_h, norm_type=cv2.NORM_MINMAX)
    r_hist = cv2.normalize(r_hist, None, alpha=0, beta=hist_h, norm_type=cv2.NORM_MINMAX)

    # Draw for each channel
    for i in range(1, 256):
        cv2.line(histImage, (int(bin_w * (i - 1)), int(hist_h - round(b_hist[i - 1]))),
                 (int(bin_w * (i)), int(hist_h - round(b_hist[i]))),
                 (255, 0, 0), 2, 8, 0)
        cv2.line(histImage, (int(bin_w * (i - 1)), int(hist_h - round(g_hist[i - 1]))),
                 (int(bin_w * (i)), int(hist_h - round(g_hist[i]))),
                 (0, 255, 0), 2, 8, 0)
        cv2.line(histImage, (int(bin_w * (i - 1)), int(hist_h - round(r_hist[i - 1]))),
                 (int(bin_w * (i)), int(hist_h - round(r_hist[i]))),
                 (0, 0, 255), 2, 8, 0)

    # Display
    cv2.namedWindow("calcHist Demo", cv2.WINDOW_AUTOSIZE)
    cv2.imshow("calcHist Demo", histImage)

    cv2.waitKey(0)

    return 0


if __name__ == "__main__":
    main(sys.argv[1:])

