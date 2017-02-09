Remapping {#tutorial_remap}
=========

@prev_tutorial{tutorial_hough_circle}
@next_tutorial{tutorial_warp_affine}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

a.  Use the OpenCV function @ref cv::remap to implement simple remapping routines.

@end_toggle

@add_toggle{Java}

a.  Use the OpenCV function [Imgproc.remap()] to implement simple remapping routines.

@end_toggle


Theory
------

### What is remapping?

-   It is the process of taking pixels from one place in the image and locating them in another
    position in a new image.
-   To accomplish the mapping process, it might be necessary to do some interpolation for
    non-integer pixel locations, since there will not always be a one-to-one-pixel correspondence
    between source and destination images.
-   We can express the remap for every pixel location \f$(x,y)\f$ as:

    \f[g(x,y) = f ( h(x,y) )\f]

    where \f$g()\f$ is the remapped image, \f$f()\f$ the source image and \f$h(x,y)\f$ is the mapping function
    that operates on \f$(x,y)\f$.

-   Let's think in a quick example. Imagine that we have an image \f$I\f$ and, say, we want to do a
    remap such that:

    \f[h(x,y) = (I.cols - x, y )\f]

    What would happen? It is easily seen that the image would flip in the \f$x\f$ direction. For
    instance, consider the input image:

    ![](images/Remap_Tutorial_Theory_0.jpg)

    observe how the red circle changes positions with respect to x (considering \f$x\f$ the horizontal
    direction):

    ![](images/Remap_Tutorial_Theory_1.jpg)

@add_toggle{C++}

-   In OpenCV, the function @ref cv::remap offers a simple remapping implementation.

@end_toggle

@add_toggle{Java}

-   In OpenCV, the function [Imgproc.remap()] offers a simple remapping implementation.

@end_toggle

Code
----

-  **What does this program do?**
    -   Loads an image
    -   Each second, apply 1 of 4 different remapping processes to the image and display them
        indefinitely in a window.
    -   Wait for the user to exit the program

@add_toggle{C++}

-  The tutorial code's is shown lines below. You can also download it from
    [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp)
    @include samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp

@end_toggle

@add_toggle{Java}

-  The tutorial code's is shown lines below.
    @include samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java

@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Create some variables we will use:
    @snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp global
-#  Load an image:
    @snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp load_image
-#  Create the destination image and the two mapping matrices (for x and y )
    @snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp create_mats
-#  Create a window to display results
    @snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp create_window
-#  Establish a loop. Each 1000 ms we update our mapping matrices (*mat_x* and *mat_y*) and apply
    them to our source image:
    @snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp main_loop
    The function that applies the remapping is @ref cv::remap . We give the following arguments:

    -   **src**: Source image
    -   **dst**: Destination image of same size as *src*
    -   **map_x**: The mapping function in the x direction. It is equivalent to the first component
        of \f$h(i,j)\f$
    -   **map_y**: Same as above, but in y direction. Note that *map_y* and *map_x* are both of
        the same size as *src*
    -   **INTER_LINEAR**: The type of interpolation to use for non-integer pixels. This is by
        default.
    -   **BORDER_CONSTANT**: Default

    How do we update our mapping matrices *mat_x* and *mat_y*? Go on reading:

-#  **Updating the mapping matrices:** We are going to perform 4 different mappings:
    -#  Reduce the picture to half its size and will display it in the middle:
        \f[h(i,j) = ( 2*i - src.cols/2  + 0.5, 2*j - src.rows/2  + 0.5)\f]
        for all pairs \f$(i,j)\f$ such that: \f$\dfrac{src.cols}{4}<i<\dfrac{3 \cdot src.cols}{4}\f$ and
        \f$\dfrac{src.rows}{4}<j<\dfrac{3 \cdot src.rows}{4}\f$
    -#  Turn the image upside down: \f$h( i, j ) = (i, src.rows - j)\f$
    -#  Reflect the image from left to right: \f$h(i,j) = ( src.cols - i, j )\f$
    -#  Combination of b and c: \f$h(i,j) = ( src.cols - i, src.rows - j )\f$

This is expressed in the following snippet. Here, *map_x* represents the first coordinate of
*h(i,j)* and *map_y* the second coordinate.
@snippet samples/cpp/tutorial_code/ImgTrans/Remap_Demo.cpp map_loop

@end_toggle

@add_toggle{Java}

-#  Create some variables we will use:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java global
-#  Load an image:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java load_image
-#  Create the destination image and the two mapping matrices (for x and y )
    @snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java create_mats
-#  Create a window to display results
    @snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java create_window
-#  Establish a loop. Each 1000 ms we update our mapping matrices (*mat_x* and *mat_y*) and apply
    them to our source image:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java main_loop
    The function that applies the remapping is @ref cv::remap . We give the following arguments:

    -   **src**: Source image
    -   **dst**: Destination image of same size as *src*
    -   **map_x**: The mapping function in the x direction. It is equivalent to the first component
        of \f$h(i,j)\f$
    -   **map_y**: Same as above, but in y direction. Note that *map_y* and *map_x* are both of
        the same size as *src*
    -   **INTER_LINEAR**: The type of interpolation to use for non-integer pixels. This is by
        default.
    -   **BORDER_CONSTANT**: Default

    How do we update our mapping matrices *mat_x* and *mat_y*? Go on reading:

-#  **Updating the mapping matrices:** We are going to perform 4 different mappings:
    -#  Reduce the picture to half its size and will display it in the middle:
        \f[h(i,j) = ( 2*i - src.cols/2  + 0.5, 2*j - src.rows/2  + 0.5)\f]
        for all pairs \f$(i,j)\f$ such that: \f$\dfrac{src.cols}{4}<i<\dfrac{3 \cdot src.cols}{4}\f$ and
        \f$\dfrac{src.rows}{4}<j<\dfrac{3 \cdot src.rows}{4}\f$
    -#  Turn the image upside down: \f$h( i, j ) = (i, src.rows - j)\f$
    -#  Reflect the image from left to right: \f$h(i,j) = ( src.cols - i, j )\f$
    -#  Combination of b and c: \f$h(i,j) = ( src.cols - i, src.rows - j )\f$

This is expressed in the following snippet. Here, *map_x* represents the first coordinate of
*h(i,j)* and *map_y* the second coordinate.
@snippet samples/java/tutorial_code/ImgProc/tutorial_remap/RemapDemo.java map_loop

@end_toggle


Result
------

-#  After compiling the code above, you can execute it giving as argument an image path. For
    instance, by using the following image:

    ![](images/Remap_Tutorial_Original_Image.jpg)

-#  This is the result of reducing it to half the size and centering it:

    ![](images/Remap_Tutorial_Result_0.jpg)

-#  Turning it upside down:

    ![](images/Remap_Tutorial_Result_1.jpg)

-#  Reflecting it in the x direction:

    ![](images/Remap_Tutorial_Result_2.jpg)

-#  Reflecting it in both directions:

    ![](images/Remap_Tutorial_Result_3.jpg)

<!-- invisible references list -->
[Imgproc.remap()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#remap-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Mat-int-
