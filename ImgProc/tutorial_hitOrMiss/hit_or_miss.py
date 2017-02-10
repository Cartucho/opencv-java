import cv2
import numpy as np

"""
  Hit-or-miss transform function

  Parameters:
    src     The source image
    kernel  The kernel matrix. 1=foreground, -1=background, 0=don't care
"""
def hitmiss( src, kernel ):

   k1 = np.uint8((kernel == 1))
   k2 = np.uint8((kernel == -1))

   if (cv2.countNonZero(k1) <= 0):
      e1 = src
   else:
      e1 = cv2.erode(src, k1, iterations=1)

   if (cv2.countNonZero(k2) <= 0):
      e2 = src
   else:
      src_complement = cv2.bitwise_not(src)
      e2 = cv2.erode(src_complement, k2, iterations=1)

   dst = e1 & e2
   return dst

"""
   Tutorial hit_or_miss.py
"""
input_image = np.array((
	[0, 0, 0, 0, 0, 0, 0, 0],
    [0, 255, 255, 255, 0, 0, 0, 255],
    [0, 255, 255, 255, 0, 0, 0, 0],
    [0, 255, 255, 255, 0, 255, 0, 0],
    [0, 0, 255, 0, 0, 0, 0, 0],
    [0, 0, 255, 0, 0, 255, 255, 0],
    [0,255, 0, 255, 0, 0, 255, 0],
    [0, 255, 255, 255, 0, 0, 0, 0]), dtype="uint8")

kernel = np.array((
	[0, 1, 0],
	[1, -1, 1],
	[0, 1, 0]), dtype="int")

output_image = hitmiss(input_image, kernel)

rate = 50
kernel = (kernel + 1) * 127
kernel = np.uint8(kernel)
kernel = cv2.resize(kernel, None, fx = rate, fy = rate, interpolation = cv2.INTER_NEAREST)
cv2.imshow("kernel", kernel)
cv2.moveWindow("kernel", 0, 0)

input_image = cv2.resize(input_image, None, fx = rate, fy = rate, interpolation = cv2.INTER_NEAREST)
cv2.imshow("Original", input_image)
cv2.moveWindow("Original", 0, 200)

output_image = cv2.resize(output_image, None , fx = rate, fy = rate, interpolation = cv2.INTER_NEAREST)
cv2.imshow("Hit or Miss", output_image)
cv2.moveWindow("Hit or Miss", 500, 200)

cv2.waitKey(0)
cv2.destroyAllWindows()
