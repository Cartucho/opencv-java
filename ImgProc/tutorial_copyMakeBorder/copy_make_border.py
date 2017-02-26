import sys
import cv2

src = None
window_name = 'copyMakeBorder Demo'

borderType = 0
max_Trackbar = 4


def BorderMethod(param):
    global borderType
    borderType = param

    top = int(0.05 * src.shape[0])
    bottom = top
    left = int(0.05 * src.shape[1])
    right = left

    BLUE = [255, 0, 0]
    dst = cv2.copyMakeBorder(src, top, bottom, left, right, borderType, value=BLUE)

    cv2.imshow(window_name, dst)


def main(argv):
    global src
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Usage:\ncopy_make_border.py <path_to_image>'
        return -1

    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    trackbar_label = 'Method: \n 0: CONSTANT \n 1: REPLICATE \n 2: REFLECT \n 3: WRAP \n 4: REFLECT_101 \n'
    cv2.createTrackbar(trackbar_label, window_name, borderType, max_Trackbar, BorderMethod)

    BorderMethod(borderType)

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])
