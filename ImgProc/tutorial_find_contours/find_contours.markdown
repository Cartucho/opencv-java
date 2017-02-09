Finding contours in your image {#tutorial_find_contours}
==============================

@prev_tutorial{tutorial_template_matching}
@next_tutorial{tutorial_hull}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the OpenCV function @ref cv::findContours
-   Use the OpenCV function @ref cv::drawContours

@end_toggle

@add_toggle{Java}

-   Use the OpenCV function [Imgproc.findContours()]
-   Use the OpenCV function [Imgproc.drawContours()]

@end_toggle

Code
----

@add_toggle{C++}

This tutorial code's is shown lines below. You can also download it from
[here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/ShapeDescriptors/findContours_demo.cpp)
@include samples/cpp/tutorial_code/ShapeDescriptors/findContours_demo.cpp

@end_toggle

@add_toggle{Java}

The tutorial code's is shown lines below.
@include samples/java/tutorial_code/ImgProc/tutorial_find_contours/FindContoursDemo.java

@end_toggle

Result
------

Here it is:
![](images/Find_Contours_Original_Image.jpg)
![](images/Find_Contours_Result.jpg)

<!-- invisible references list -->
[Imgproc.findContours()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#findContours-org.opencv.core.Mat-java.util.List-org.opencv.core.Mat-int-int-
[Imgproc.drawContours()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#drawContours-org.opencv.core.Mat-java.util.List-int-org.opencv.core.Scalar-
