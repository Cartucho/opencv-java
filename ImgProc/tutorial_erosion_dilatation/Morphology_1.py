import cv2

global img
global erosion_elem
global erosion_size
global dilation_elem
global dilation_size


## [erode]
def erode():
    if erosion_elem == 0:
        erosion_type = cv2.MORPH_RECT
    elif erosion_elem == 1:
        erosion_type = cv2.MORPH_CROSS
    else:
        erosion_type = cv2.MORPH_ELLIPSE

    kernel = cv2.getStructuringElement(erosion_type, (2 * erosion_size + 1, 2 * erosion_size + 1))
    eroded = cv2.erode(img, kernel)
    cv2.imshow('erosion demo', eroded)
## [erode]


def erode_elem(type_ele):
    global erosion_elem
    erosion_elem = type_ele
    erode()


def erode_size(kernel_size):
    global erosion_size
    erosion_size = kernel_size
    erode()


## [dilate]
def dilate():
    if dilation_elem == 0:
        dilation_type = cv2.MORPH_RECT
    elif dilation_elem == 1:
        dilation_type = cv2.MORPH_CROSS
    else:
        dilation_type = cv2.MORPH_ELLIPSE

    kernel = cv2.getStructuringElement(dilation_type, (2 * dilation_size + 1, 2 * dilation_size + 1))
    dilated = cv2.dilate(img, kernel)
    cv2.imshow('dilation demo', dilated)
## [dilate]


def dilate_elem(type_ele):
    global dilation_elem
    dilation_elem = type_ele
    dilate()


def dilate_size(kernel_size):
    global dilation_size
    dilation_size = kernel_size
    dilate()


erosion_elem = 0
erosion_size = 0  # initial kernel size  = 2 * 0 + 1 = 1
dilation_elem = 0
dilation_size = 0
max_elem = 2
max_kernel_size = 21  # maximum kernel size = 43

img = cv2.imread('../images/cat.jpg')

cv2.namedWindow('erosion demo')
cv2.namedWindow('dilation demo')

# Creating Trackbar for element type
cv2.createTrackbar('Element: \n 0: Rect \n 1: Cross \n 2: Ellipse', 'erosion demo',
                   erosion_elem, max_elem, erode_elem)
cv2.createTrackbar('Element: \n 0: Rect \n 1: Cross \n 2: Ellipse', 'dilation demo',
                   dilation_elem, max_elem, dilate_elem)

# Creating Trackbar for kernel size
cv2.createTrackbar('Size: 2n+1', 'erosion demo', erosion_size, max_kernel_size, erode_size)
cv2.createTrackbar('Size: 2n+1', 'dilation demo', dilation_size, max_kernel_size, dilate_size)

erode()
dilate()

cv2.waitKey(0)
cv2.destroyAllWindows()
