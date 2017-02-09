Thresholding Operations using inRange {#tutorial_threshold_inRange}
=============================

@prev_tutorial{tutorial_threshold}
@next_tutorial{tutorial_filter_2d}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}
-   Perform basic thresholding operations using OpenCV function @ref cv::inRange
@end_toggle

@add_toggle{Java}
-   Perform basic thresholding operations using OpenCV function [Core.inRange()]
@end_toggle
-   Detect an object based on the range of pixel values it has

Theory
-----------
@add_toggle{C++}
-   In the previous tutorial, we learnt how perform thresholding using @ref cv::threshold function.

-   In this tutorial, we will learn how to do it using @ref cv::inRange function.
@end_toggle

@add_toggle{Java}
-   In the previous tutorial, we learnt how perform thresholding using [Imgproc.threshold()] function.

-   In this tutorial, we will learn how to do it using [Core.inRange()] function.
@end_toggle

The concept remains same, but now we add a range of pixel values we need.

Code
----

@add_toggle{C++}

The tutorial code's is shown lines below. You can also download it from
[here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp)
@include samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp

@end_toggle

@add_toggle{Java}

The tutorial code's is shown lines below.
@include samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java

@end_toggle

Explanation
-----------

@add_toggle{C++}
-#  Let's check the general structure of the program:
    -   Create two Matrix elements to store the frames
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp mat
    -   Capture the video stream from default capturing device.
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp cap
    -   Create a window to display the default frame and the threshold frame.
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp window
    -   Create trackbars to set the range of RGB values
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp trackbar
    -   Until the user want the program to exit do the following
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp while
    -   Show the images
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp show
    -   For a trackbar which controls the lower range, say for example Red value:
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp low
    -   For a trackbar which controls the upper range, say for example Red value:
        @snippet samples/cpp/tutorial_code/ImgProc/Threshold_inRange.cpp high
    -   It is necessary to find the maximum and minimum value to avoid discrepancies such as
        the high value of threshold becoming less the low value.
@end_toggle

@add_toggle{Java}
-#  Let's check the general structure of the program:
    -   Create two Matrix elements to store the frames
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java mat
    -   Capture the video stream from default capturing device.
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java cap
    -   Create a window to display the default frame and the threshold frame.
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java window
    -   Create trackbars to set the range of RGB values
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java trackbar
    -   Until the user want the program to exit do the following
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java while
    -   Show the images
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java show
    -   For a trackbar which controls the lower range, say for example Red value:
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java low
    -   For a trackbar which controls the upper range, say for example Red value:
        @snippet samples/java/tutorial_code/ImgProc/tutorial_threshold_inRange/ThresholdInRange.java high
    -   It is necessary to find the maximum and minimum value to avoid discrepancies such as
        the high value of threshold becoming less the low value.
@end_toggle

Results
-------

-#  After compiling this program, run it. The program will open the windows.

-#  As you set the RGB range values from the trackbar, the resulting frame will be visible in the other window.

    ![](images/Threshold_inRange_Tutorial_Result_input.jpeg)
    ![](images/Threshold_inRange_Tutorial_Result_output.jpeg)

<!-- invisible references list -->
[Core.inRange()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#inRange-org.opencv.core.Mat-org.opencv.core.Scalar-org.opencv.core.Scalar-org.opencv.core.Mat-
[Imgproc.threshold()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#threshold-org.opencv.core.Mat-org.opencv.core.Mat-double-double-int-
