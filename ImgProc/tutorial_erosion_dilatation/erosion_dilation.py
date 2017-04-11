import sys
import cv2

src = None
erosion_dst = None
dilation_dst = None

erosion_elem = 0
erosion_size = 0
dilation_elem = 0
dilation_size = 0
max_elem = 2
max_kernel_size = 21


def main(argv):

    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nerosion_dilation.py < path_to_image >'
        return -1

    global src
    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Can\'t read the image'
        return -1

    cv2.namedWindow('Erosion Demo', cv2.WINDOW_AUTOSIZE)
    cv2.namedWindow('Dilation Demo', cv2.WINDOW_AUTOSIZE)

    cols = src.shape[1]
    cv2.moveWindow('Dilation Demo', cols, 0)

    # Create Erosion Trackbar
    cv2.createTrackbar('Element:\n 0: Rect \n 1: Cross \n 2: Ellipse', 'Erosion Demo',
                       erosion_elem, max_elem, erode_elem)

    cv2.createTrackbar('Kernel size:\n 2n +1', 'Erosion Demo', erosion_size,
                       max_kernel_size, erode_size)

    # Create Dilation Trackbar
    cv2.createTrackbar('Element:\n 0: Rect \n 1: Cross \n 2: Ellipse', 'Dilation Demo',
                       dilation_elem, max_elem, dilate_elem)

    cv2.createTrackbar('Kernel size:\n 2n +1', 'Dilation Demo', dilation_size,
                       max_kernel_size, dilate_size)

    # Default start
    erode_elem(erosion_elem)
    dilate_elem(dilation_size)

    cv2.waitKey(0)
    return 0


def erode_elem(param):
    global erosion_elem
    erosion_elem = param
    erode()


def erode_size(param):
    global erosion_size
    erosion_size = param
    erode()


def erode():
    if erosion_elem == 0:
        erosion_type = cv2.MORPH_RECT
    elif erosion_elem == 1:
        erosion_type = cv2.MORPH_CROSS
    elif erosion_elem == 2:
        erosion_type = cv2.MORPH_ELLIPSE

    element = cv2.getStructuringElement(erosion_type,
                                        (2 * erosion_size + 1,
                                         2 * erosion_size + 1))
    eroded = cv2.erode(src, element)
    cv2.imshow('Erosion Demo', eroded)


def dilate_elem(param):
    global dilation_elem
    dilation_elem = param
    dilate()


def dilate_size(param):
    global dilation_size
    dilation_size = param
    dilate()


def dilate():
    if dilation_elem == 0:
        dilation_type = cv2.MORPH_RECT
    elif dilation_elem == 1:
        dilation_type = cv2.MORPH_CROSS
    elif dilation_elem == 2:
        dilation_type = cv2.MORPH_ELLIPSE

    element = cv2.getStructuringElement(dilation_type,
                                        (2 * dilation_size + 1,
                                         2 * dilation_size + 1))
    dilated = cv2.dilate(src, element)
    cv2.imshow('Dilation Demo', dilated)


if __name__ == "__main__":
    main(sys.argv)
