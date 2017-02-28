import sys
import cv2
import numpy as np

def main(argv):
    # Load the image
    src = cv2.imread(sys.argv[1], cv2.IMREAD_COLOR)

    # Check if everything was fine
    if src is None:
        print 'Usage:\nimage_segmentation.py <path_to_image>'
        return -1

    # Show source image
    cv2.imshow('Source image', src)

    # Change the background from white to black, since that will help later to extract
    # better results during the use of Distance Transform
    rows, cols, channels = src.shape

    for x in range(0, rows):
        for y in range(0, cols):
            if (src[x][y] == [255, 255, 255]).all():
                src[x][y] = [0, 0, 0]

    # Show output image
    cv2.imshow('Black Background Image', src)

    # Create a kernel that we will use for sharpening our image
    kernel = np.array((
        [1, 1, 1],
        [1, -8, 1],
        [1, 1, 1]), dtype="float32") # an approximation of second derivative, a quite strong kernel

    """
    Do the laplacian filtering as it is
    well, we need to convert everything in something more deeper then CV_8U
    because the kernel has some negative values,
    and we can expect in general to have a Laplacian image with negative values
    BUT a 8bits unsigned int (the one we are working with) can contain values from 0 to 255
    so the possible negative number will be truncated
    """

    sharp = src
    imgLaplacian = cv2.filter2D(sharp, cv2.CV_32F, kernel)
    #imgLaplacian = cv2.filter2D(sharp, -1, kernel)
    sharp = np.float32(src)
    imgResult = sharp - imgLaplacian

    imgResult = cv2.convertScaleAbs(imgResult)
    imgLaplacian = cv2.convertScaleAbs(imgLaplacian)

    cv2.imshow('Laplace Filtered Image', imgLaplacian)
    cv2.imshow('New Sharped Image', imgResult)

    cv2.waitKey(0)
    return 0


if __name__ == "__main__":
    main(sys.argv[1:])

