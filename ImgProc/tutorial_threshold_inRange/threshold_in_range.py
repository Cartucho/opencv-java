import cv2

low_r = 30
low_g = 30
low_b = 30
high_r = 100
high_g = 100
high_b = 100

window_name = 'Object Detection'
frame_name = 'Video Capture'


def on_low_r_thresh_trackbar(param):
    param = min(high_r - 1, param)
    global low_r
    low_r = param
    cv2.setTrackbarPos('Low R', window_name, low_r)


def on_high_r_thresh_trackbar(param):
    param = max(param, low_r + 1)
    global high_r
    high_r = param
    cv2.setTrackbarPos('High R', window_name, high_r)


def on_low_g_thresh_trackbar(param):
    param = min(high_g - 1, param)
    global low_g
    low_g = param
    cv2.setTrackbarPos('Low G', window_name, low_g)


def on_high_g_thresh_trackbar(param):
    param = max(param, low_g + 1)
    global high_g
    high_g = param
    cv2.setTrackbarPos('High G', window_name, high_g)


def on_low_b_thresh_trackbar(param):
    param = min(high_b - 1, param)
    global low_b
    low_b = param
    cv2.setTrackbarPos('Low B', window_name, low_b)


def on_high_b_thresh_trackbar(param):
    param = max(param, low_b + 1)
    global high_b
    high_b = param
    cv2.setTrackbarPos('High B', window_name, high_b)


# [cap]
cap = cv2.VideoCapture(0)
# [cap]

# [window]
cv2.namedWindow(frame_name, cv2.WINDOW_AUTOSIZE)
cv2.namedWindow(window_name, cv2.WINDOW_AUTOSIZE)
# [window]

# [trackbar]
# -- Trackbars to set thresholds for RGB values
cv2.createTrackbar('Low R', window_name,
                   low_r, 255, on_low_r_thresh_trackbar)
cv2.createTrackbar('High R', window_name,
                   high_r, 255, on_high_r_thresh_trackbar)
cv2.createTrackbar('Low G', window_name,
                   low_g, 255, on_low_g_thresh_trackbar)
cv2.createTrackbar('High G', window_name,
                   high_g, 255, on_high_g_thresh_trackbar)
cv2.createTrackbar('Low B', window_name,
                   low_b, 255, on_low_b_thresh_trackbar)
cv2.createTrackbar('High B', window_name,
                   high_b, 255, on_high_b_thresh_trackbar)
# [trackbar]

while (True):
    # [while]
    ret, frame = cap.read()
    if frame is None:
        break

    # -- Detect the object based on RGB Range Values
    frame_threshold = cv2.inRange(frame, (low_b, low_g, low_r), (high_b, high_g, high_r))
    # [while]
    # [show]
    # -- Show the frames
    cv2.imshow(frame_name, frame)
    cv2.imshow(window_name, frame_threshold)
    # [show]

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()
