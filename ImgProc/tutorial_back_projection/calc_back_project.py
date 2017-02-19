import sys
import cv2
import numpy as np

## [global_variables]
src = cv2.imread('', 0)
hsv = cv2.imread('', 0)
hue = cv2.imread('', 0)

bins = 25
## [global_variables]

def main(argv):
    global src
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Usage:\ncalc_back_project.py <path_to_image>'
        return -1

    global hsv
    hsv = cv2.cvtColor(src, cv2.COLOR_BGR2HSV)

    global hue
    hue = np.zeros(hsv.shape, dtype=np.uint8)
    ch = [0] * 2
    cv2.mixChannels([hsv], [hue], ch)

    window_image = 'Source image'
    cv2.namedWindow( window_image, cv2.WINDOW_AUTOSIZE )
    cv2.createTrackbar('* Hue  bins: ', window_image, bins, 180, Hist_and_Backproj)

    Hist_and_Backproj(bins)

    cv2.imshow( window_image, src )

    cv2.waitKey(0)
    return 0


def Hist_and_Backproj(param):

    global bins
    bins = param

    histSize =  max(bins, 2)
    hue_range = [0, 180]

    hist = cv2.calcHist([hue], [0], None, [histSize], hue_range)
    cv2.normalize(hist, hist, 0, 255, cv2.NORM_MINMAX)

    backproj = cv2.calcBackProject([hue], [0], hist, hue_range, 1)

    cv2.imshow( 'BackProj', backproj )

    w = 400
    h = 400
    bin_w = int(round( w / histSize))
    histImg = np.zeros([w, h, 3], dtype=np.uint8)
    for i in range(0, bins):
        cv2.rectangle( histImg, ( i*bin_w, h ), ( (i+1)*bin_w, h - int(round( hist[i]*h/255.0 ) )), ( 0, 0, 255 ), -1)

    cv2.imshow( 'Histogram', histImg )

    pass

if __name__ == "__main__":
    main(sys.argv[1:])

