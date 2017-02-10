import cv2

print " \nZoom In-Out demo "
print " ------------------ "
print "  * [u] -> Zoom in  "
print "  * [d] -> Zoom out "
print "  * [ESC] -> Close program \n"

img = cv2.imread('../data/chicky_512.png')

while 1:
    h, w = img.shape[:2]

    cv2.imshow('Pyramids Demo', img)
    k = cv2.waitKey(10) % 256

    if k == 27:
        break

    elif k == ord('u'):  # Zoom in, make image double size
        img = cv2.pyrUp(img, dstsize=(2 * w, 2 * h))

    elif k == ord('d'):  # Zoom down, make image half the size
        img = cv2.pyrDown(img, dstsize=(w / 2, h / 2))

cv2.destroyAllWindows()
