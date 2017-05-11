import sys
import cv2

def main(argv):

    # [declare_variables]
    ddepth = cv2.CV_16S
    kernel_size = 3
    window_name = "Laplace Demo"
    # [declare_variables]

    # [load_image]
    if len(argv) < 1:
        print 'Not enough parameters'
        print 'Usage:\nlaplace_demo.py  < path_to_image >'
        return -1

    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)
    if src is None:
        print 'Invalid input image'
        return -1
    # [load_image]

    # [gaussian_blur]
    # Remove noise by blurring with a Gaussian filter
    src = cv2.GaussianBlur(src, (3, 3), 0)
    # [gaussian_blur]

    # [convert_to_gray]
    # Convert the image to grayscale
    src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
    # [convert_to_gray]

    # Create Window
    cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)

    # [laplacian]
    # Apply Laplace function
    dst = cv2.Laplacian(src_gray, ddepth, kernel_size)
    # [laplacian]

    # [convert_scale]
    abs_dst = cv2.convertScaleAbs(dst)
    # [convert_scale]

    # [imshow]
    cv2.imshow(window_name, abs_dst)
    # [imshow]

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])

