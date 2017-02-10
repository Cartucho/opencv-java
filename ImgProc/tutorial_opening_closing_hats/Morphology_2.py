import cv2

global img
global morph_elem
global morph_size
global morph_operator
window_name = 'Morphology Transformations Demo'

def morphology_operations():
    # Since MORPH_X : 2,3,4,5 and 6
    operation = morph_operator + 2

    kernel = cv2.getStructuringElement(morph_elem, (2 * morph_size + 1, 2 * morph_size + 1))
    dst = cv2.morphologyEx(img, operation, kernel)
    cv2.imshow(window_name, dst)


def update_operator(opertator):
    global morph_operator
    morph_operator = opertator
    morphology_operations()


def update_elem(element):
    global morph_elem
    morph_elem = element
    morphology_operations()

def update_size(size):
    global morph_size
    morph_size = size
    morphology_operations()


morph_elem = 0
morph_size = 0  # initial kernel size  = 2 * 0 + 1 = 1
morph_operator = 0
max_operator = 4
max_elem = 2
max_kernel_size = 21  # maximum kernel size = 43

img = cv2.imread('../images/baboon.jpg')

cv2.namedWindow(window_name)

# Creating Trackbar for operator type
cv2.createTrackbar('Operator:\n 0: Opening - 1: Closing \n 2: Gradient - 3: Top Hat \n 4: Black Hat',
                   window_name, morph_elem, max_operator, update_operator)
# Creating Trackbar for element type
cv2.createTrackbar('Element: \n 0: Rect \n 1: Cross \n 2: Ellipse', window_name,
                   morph_elem, max_elem, update_elem)
# Creating Trackbar for kernel size
cv2.createTrackbar('Size: 2n+1', window_name, morph_size, max_kernel_size, update_size)

morphology_operations()

cv2.waitKey(0)
cv2.destroyAllWindows()
