Convex Hull {#tutorial_hull}
===========

@prev_tutorial{tutorial_find_contours}
@next_tutorial{tutorial_bounding_rects_circles}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the OpenCV function @ref cv::convexHull

@end_toggle

@add_toggle{Java}

-   Use the OpenCV function [Imgproc.convexHull()]

@end_toggle

Code
----

@add_toggle{C++}

This tutorial code's is shown lines below. You can also download it from
[here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/ShapeDescriptors/hull_demo.cpp)

@include samples/cpp/tutorial_code/ShapeDescriptors/hull_demo.cpp

@end_toggle

@add_toggle{Java}
The tutorial code's is shown lines below.
@include samples/java/tutorial_code/ImgProc/tutorial_hull/HullDemo.java
@end_toggle

Result
------

Here it is:

![Original](images/Hull_Original_Image.jpg)
![Result](images/Hull_Result.jpg)

<!-- invisible references list -->
[Imgproc.convexHull()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#convexHull-org.opencv.core.MatOfPoint-org.opencv.core.MatOfInt-
