Hough Circle Transform {#tutorial_hough_circle}
======================

@prev_tutorial{tutorial_hough_lines}
@next_tutorial{tutorial_remap}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the OpenCV function @ref cv::HoughCircles to detect circles in an image.

@end_toggle

@add_toggle{Java}

-   Use the OpenCV function [Imgproc.HoughCircles()] to detect circles in an image.

@end_toggle

Theory
------

### Hough Circle Transform

-   The Hough Circle Transform works in a *roughly* analogous way to the Hough Line Transform
    explained in the previous tutorial.
-   In the line detection case, a line was defined by two parameters \f$(r, \theta)\f$. In the circle
    case, we need three parameters to define a circle:

    \f[C : ( x_{center}, y_{center}, r )\f]

    where \f$(x_{center}, y_{center})\f$ define the center position (green point) and \f$r\f$ is the radius,
    which allows us to completely define a circle, as it can be seen below:

    ![](images/Hough_Circle_Tutorial_Theory_0.jpg)

-   For sake of efficiency, OpenCV implements a detection method slightly trickier than the standard
    Hough Transform: *The Hough gradient method*, which is made up of two main stages. The first
    stage involves edge detection and finding the possible circle centers and the second stage finds
    the best radius for each candidate center. For more details, please check the book *Learning
    OpenCV* or your favorite Computer Vision bibliography

Code
----

-  **What does this program do?**
    -   Loads an image and blur it to reduce the noise
    -   Applies the *Hough Circle Transform* to the blurred image .
    -   Display the detected circle in a window.

@add_toggle{C++}
-  The sample code that we will explain can be downloaded from [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/houghcircles.cpp).
    A slightly fancier version (which shows trackbars for
    changing the threshold values) can be found [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp).
    @include samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp
@end_toggle

@add_toggle{Java}

-  The tutorial code's is shown lines below.
    @include samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java
@end_toggle

Explanation
-----------

@add_toggle{C++}
-#  Load an image
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp load_image
-#  Convert it to grayscale:
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp gray
-#  Apply a Gaussian blur to reduce noise and avoid false circle detection:
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp gaussian_blur
-#  Proceed to apply Hough Circle Transform:
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp hough_circles
    with the arguments:

    -   *src_gray*: Input image (grayscale).
    -   *circles*: A vector that stores sets of 3 values: \f$x_{c}, y_{c}, r\f$ for each detected
        circle.
    -   *HOUGH_GRADIENT*: Define the detection method. Currently this is the only one available in
        OpenCV.
    -   *dp = 1*: The inverse ratio of resolution.
    -   *min_dist = src_gray.rows/8*: Minimum distance between detected centers.
    -   *param_1* = 200: Upper threshold for the internal Canny edge detector.
    -   *param_2* = 100: Threshold for center detection.
    -   *min_radius = 0*: Minimum radio to be detected. If unknown, put zero as default.
    -   *max_radius = 0*: Maximum radius to be detected. If unknown, put zero as default.

-#  Draw the detected circles:
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp loop
    You can see that we will draw the circle(s) on red and the center(s) with a small dot

-#  Display the detected circle(s):
    @snippet samples/cpp/tutorial_code/ImgTrans/HoughCircle_Demo.cpp imshow
@end_toggle

@add_toggle{Java}
-#  Load an image
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java load_image
-#  Convert it to grayscale:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java gray
-#  Apply a Gaussian blur to reduce noise and avoid false circle detection:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java gaussian_blur
-#  Proceed to apply Hough Circle Transform:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java hough_circles
    with the arguments:

    -   *src_gray*: Input image (grayscale).
    -   *circles*: A vector that stores sets of 3 values: \f$x_{c}, y_{c}, r\f$ for each detected
        circle.
    -   *HOUGH_GRADIENT*: Define the detection method. Currently this is the only one available in
        OpenCV.
    -   *dp = 1*: The inverse ratio of resolution.
    -   *min_dist = src_gray.rows/8*: Minimum distance between detected centers.
    -   *param_1* = 100.0: Upper threshold for the internal Canny edge detector.
    -   *param_2* = 30.0: Threshold for center detection.
    -   *min_radius = 0*: Minimum radio to be detected. If unknown, put zero as default.
    -   *max_radius = 0*: Maximum radius to be detected. If unknown, put zero as default.

-#  Draw the detected circles:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java loop
    You can see that we will draw the circle(s) on red and the center(s) with a small dot

-#  Display the detected circle(s):
    @snippet samples/java/tutorial_code/ImgProc/tutorial_hough_circle/HoughCircles.java imshow
@end_toggle

Result
------

The result of running the code above with a test image is shown below:

@add_toggle{C++}

![](images/Hough_Circle_Tutorial_Result.jpg)

@end_toggle

@add_toggle{Java}

 - Test Image:

![](images/smarties.png)

 - Result:

![](images/smarties_result.png)

@end_toggle

<!-- invisible references list -->
[Imgproc.HoughCircles()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#HoughCircles-org.opencv.core.Mat-org.opencv.core.Mat-int-double-double-
