import cv2
import numpy as np


# Create an image
r = 100
src = np.zeros((4 * r, 4 * r), dtype=np.uint8)

# Create a sequence of points to make a contour:
vert = [(3 * r / 2, int(1.34 * r)), (1 * r, 2 * r),
        (3 * r / 2, int(2.866 * r)), (5 * r / 2, int(2.866 * r)),
        (3 * r, 2 * r), (5 * r / 2, int(1.34 * r))]

# Draw it in src
for j in range(0, 6):
    cv2.line(src, vert[j], vert[(j + 1) % 6], (255, 255, 255), 3, 8)

# Get the contours
src_copy = np.copy(src)

im2, contours, hierarchy = cv2.findContours(src_copy, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

# Calculate the distances to the contour
res = np.zeros(src.shape, np.float32)  # array to store distances
rows, cols = src.shape

for j in range(0, rows):
    for i in range(0, cols):
        res.itemset((i, j), cv2.pointPolygonTest(contours[0], (j, i), True))

# Find minimum and maximum to adjust colors
mini, maxi = np.abs(cv2.minMaxLoc(res)[:2])
mini = 255.0 / mini
maxi = 255.0 / maxi

# Depicting the  distances graphically
drawing = np.zeros((rows, cols, 3), np.uint8)

for i in xrange(rows):
    for j in xrange(cols):
        if res.item((i, j)) < 0:
            # If outside, blue color
            drawing.itemset((i, j, 0), 255 - int(abs(res.item(i, j)) * mini))
        elif res.item((i, j)) > 0:
            # If inside, red color
            drawing.itemset((i, j, 2), 255 - int(res.item(i, j) * maxi))
        else:
            # If on the contour, white color.
            drawing[i, j] = [255, 255, 255]

# Create Window and show your results
source_window = "Source"
cv2.namedWindow(source_window, cv2.WINDOW_AUTOSIZE)
cv2.imshow(source_window, src)

result_window = "Distance"
cv2.namedWindow(result_window, cv2.WINDOW_AUTOSIZE)
cv2.imshow(result_window, drawing)

cv2.waitKey(0)
