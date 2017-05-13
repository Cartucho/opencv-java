import sys
import cv2

src_gray = None

threshold_value = 0
threshold_type = 3
max_value = 255
max_type = 4
max_BINARY_value = 255

window_name = 'Threshold Demo'

trackbar_type = 'Type: \n 0: Binary \n 1: Binary Inverted \n' \
                ' 2: Truncate \n 3: To Zero \n 4: To Zero Inverted'
trackbar_value = 'Value'


def main(argv):
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nthreshold.py  < path_to_image >'
        return -1

    # [load_image]
    # Read the image
    src = cv2.imread(argv[1], cv2.IMREAD_COLOR)

    if src is None:
        print 'Invalid input image'
        return -1

    global src_gray
    # Convert it to gray
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
    # [load_image]

    # [create_window]
    # Create a window to display results
    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)
    # [create_window]

    # [create_trackbars]
    # Create Erosion Trackbar
    cv2.createTrackbar(trackbar_type, window_name,
                       threshold_type, max_type, type_update)

    cv2.createTrackbar(trackbar_value, window_name,
                       threshold_value, max_value,
                       value_update)
    # [create_trackbars]

    # Call the function to initialize
    threshold_demo()

    cv2.waitKey(0)
    return 0


def type_update(param):
    global threshold_type
    threshold_type = param
    threshold_demo()


def value_update(param):
    global threshold_value
    threshold_value = param
    threshold_demo()


def threshold_demo():
    """
       0: Binary
       1: Binary Inverted
       2: Threshold Truncated
       3: Threshold to Zero
       4: Threshold to Zero Inverted
    """
    
    ret, dst = cv2.threshold(src_gray, threshold_value,
                             max_BINARY_value, threshold_type)
    cv2.imshow(window_name, dst)


if __name__ == "__main__":
    main(sys.argv)
