import sys
import cv2
import numpy as np

low_r = 30
low_g = 30
low_b = 30
high_r = 100
high_g = 100
high_b = 100

def on_low_r_thresh_trackbar(param):
    global low_r
    low_r = param
    #threshold_demo()

def on_high_r_thresh_trackbar(param):
    global high_r
    high_r = param
    #threshold_demo()

def on_low_g_thresh_trackbar(param):
    global low_g
    low_g = param
    #threshold_demo()

def on_high_g_thresh_trackbar(param):
    global high_g
    high_g = param
    #threshold_demo()

def on_low_b_thresh_trackbar(param):
    global low_b
    low_b = param
    #threshold_demo()

def on_high_b_thresh_trackbar(param):
    global high_b
    high_b = param
    #threshold_demo()

# [cap]
cap = cv2.VideoCapture(0)
# [cap]

# [window]
cv2.namedWindow("Video Capture", cv2.WINDOW_AUTOSIZE)
cv2.namedWindow("Object Detection", cv2.WINDOW_AUTOSIZE)
# [window]

# [trackbar]
#-- Trackbars to set thresholds for RGB values
cv2.createTrackbar("Low R", "Object Detection",
                   low_r, 255, on_low_r_thresh_trackbar)
cv2.createTrackbar("High R", "Object Detection",
                   high_r, 255, on_high_r_thresh_trackbar)
cv2.createTrackbar("Low G", "Object Detection",
                   low_g, 255, on_low_g_thresh_trackbar)
cv2.createTrackbar("High G", "Object Detection",
                   high_g, 255, on_high_g_thresh_trackbar)
cv2.createTrackbar("Low B", "Object Detection",
                   low_b, 255, on_low_b_thresh_trackbar)
cv2.createTrackbar("High B", "Object Detection",
                   high_b, 255, on_high_b_thresh_trackbar)
# [trackbar]

while(True):
    # [while]
    ret, frame = cap.read()
    if frame is None:
        break

    #-- Detect the object based on RGB Range Values
    # [while]
    # [show]
    # [show]

    # Our operations on the frame come here
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Display the resulting frame
    cv2.imshow('frame',gray)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()
