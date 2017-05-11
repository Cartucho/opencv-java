import sys
import cv2

src = None
dst = None

morph_operator = 0
morph_elem = 0
morph_size = 0
max_operator = 4
max_elem = 2
max_kernel_size = 21

window_name = "Morphology Transformations Demo"


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nopening_closing_hats.py < path_to_image >'
        return -1

    global src
    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Can\'t read the image'
        return -1

    # Create window
    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    # Create Trackbar to select Morphology operation
    cv2.createTrackbar('Operator:\n 0: Opening - 1: Closing  \n 2: Gradient - 3: Top Hat \n 4: Black Hat',
                       window_name, morph_operator, max_operator, morph_operator_update)

    # Create Trackbar to select kernel type
    cv2.createTrackbar('Element:\n 0: Rect - 1: Cross - 2: Ellipse', window_name, morph_elem,
                       max_elem, morph_elem_update)

    # Create Trackbar to choose kernel size
    cv2.createTrackbar('Kernel size:\n 2n +1', window_name, morph_size,
                       max_kernel_size, morph_size_update)

    # Default start
    morphology_operations()

    cv2.waitKey(0)
    return 0


def morph_operator_update(param):
    global morph_operator
    morph_operator = param
    morphology_operations()


def morph_elem_update(param):
    global morph_elem
    morph_elem = param
    morphology_operations()


def morph_size_update(param):
    global morph_size
    morph_size = param
    morphology_operations()


def morphology_operations():
    # Since MORPH_X : 2,3,4,5 and 6
    operation = morph_operator + 2

    element = cv2.getStructuringElement(morph_elem, (2 * morph_size + 1, 2 * morph_size + 1))

    # Apply the specified morphology operation
    dst = cv2.morphologyEx(src, operation, element)
    cv2.imshow(window_name, dst)


if __name__ == "__main__":
    main(sys.argv)

