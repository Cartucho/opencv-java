Making your own linear filters! {#tutorial_filter_2d}
===============================

@prev_tutorial{tutorial_threshold_inRange}
@next_tutorial{tutorial_copyMakeBorder}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the OpenCV function @ref cv::filter2D to create your own linear filters.

@end_toggle

@add_toggle{Java}

-   Use the OpenCV function [Imgproc.filter2D()] to create your own linear filters.

@end_toggle

Theory
------

@note The explanation below belongs to the book **Learning OpenCV** by Bradski and Kaehler.

### Convolution

In a very general sense, convolution is an operation between every part of an image and an operator
(kernel).

### What is a kernel?

A kernel is essentially a fixed size array of numerical coefficeints along with an *anchor point* in
that array, which is tipically located at the center.

![](images/filter_2d_tutorial_kernel_theory.png)

### How does convolution with a kernel work?

Assume you want to know the resulting value of a particular location in the image. The value of the
convolution is calculated in the following way:

-#  Place the kernel anchor on top of a determined pixel, with the rest of the kernel overlaying the
    corresponding local pixels in the image.
-#  Multiply the kernel coefficients by the corresponding image pixel values and sum the result.
-#  Place the result to the location of the *anchor* in the input image.
-#  Repeat the process for all pixels by scanning the kernel over the entire image.

Expressing the procedure above in the form of an equation we would have:

\f[H(x,y) = \sum_{i=0}^{M_{i} - 1} \sum_{j=0}^{M_{j}-1} I(x+i - a_{i}, y + j - a_{j})K(i,j)\f]

@add_toggle{C++}

Fortunately, OpenCV provides you with the function @ref cv::filter2D so you do not have to code all
these operations.

@end_toggle

@add_toggle{Java}

Fortunately, OpenCV provides you with the function [Imgproc.filter2D()] so you do not have to code all
these operations.

@end_toggle

Code
----

-#  **What does this program do?**
    -   Loads an image
    -   Performs a *normalized box filter*. For instance, for a kernel of size \f$size = 3\f$, the
        kernel would be:

        \f[K = \dfrac{1}{3 \cdot 3} \begin{bmatrix}
        1 & 1 & 1  \\
        1 & 1 & 1  \\
        1 & 1 & 1
        \end{bmatrix}\f]

        The program will perform the filter operation with kernels of sizes 3, 5, 7, 9 and 11.

    -   The filter output (with each kernel) will be shown during 500 milliseconds

@add_toggle{C++}
-#  The tutorial code's is shown lines below. You can also download it from
    [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp)
    @include samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp
@end_toggle
@add_toggle{Java}
-#  The tutorial code's is shown lines below. You can also download it from
    [here](https://github.com/Itseez/opencv/tree/master/samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java)
    @include samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java
@end_toggle

Explanation
-----------

@add_toggle{C++}
-#  Load an image
    @snippet samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp load_image
-#  Create a window to display the result
    @snippet samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp create_window
-#  Initialize the arguments for the linear filter
    @snippet samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp initialize
-#  Perform an infinite loop updating the kernel size and applying our linear filter to the input
    image. Let's analyze that more in detail:
-#  First we define the kernel our filter is going to use. Here it is:
    @snippet samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp kernel
    The first line is to update the *kernel_size* to odd values in the range: \f$[3,11]\f$. The second
    line actually builds the kernel by setting its value to a matrix filled with \f$1's\f$ and
    normalizing it by dividing it between the number of elements.
-#  After setting the kernel, we can generate the filter by using the function @ref cv::filter2D :
    @snippet samples/cpp/tutorial_code/ImgTrans/filter2D_demo.cpp apply_filter
    The arguments denote:

    -#  *src*: Source image
    -#  *dst*: Destination image
    -#  *ddepth*: The depth of *dst*. A negative value (such as \f$-1\f$) indicates that the depth is
        the same as the source.
    -#  *kernel*: The kernel to be scanned through the image
    -#  *anchor*: The position of the anchor relative to its kernel. The location *Point(-1, -1)*
        indicates the center by default.
    -#  *delta*: A value to be added to each pixel during the convolution. By default it is \f$0\f$
    -#  *BORDER_DEFAULT*: We let this value by default (more details in the following tutorial)

-#  Our program will effectuate a *while* loop, each 500 ms the kernel size of our filter will be
    updated in the range indicated.
@end_toggle

@add_toggle{Java}
-#  Load an image
    @snippet samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java load_image
-#  Create a window to display the result
    @snippet samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java create_window
-#  Initialize the arguments for the linear filter
    @snippet samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java initialize
-#  Perform an infinite loop updating the kernel size and applying our linear filter to the input
    image. Let's analyze that more in detail:
-#  First we define the kernel our filter is going to use. Here it is:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java kernel
    The first line is to update the *kernel_size* to odd values in the range: \f$[3,11]\f$. The second
    line actually builds the kernel by setting its value to a matrix filled with \f$1's\f$ and
    normalizing it by dividing it between the number of elements.
-#  After setting the kernel, we can generate the filter by using the [Imgproc.filter2D()] function:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_filter_2d/Filter2D_Demo.java apply_filter
    The arguments denote:

    -#  *src*: Source image
    -#  *dst*: Destination image
    -#  *ddepth*: The depth of *dst*. A negative value (such as \f$-1\f$) indicates that the depth is
        the same as the source.
    -#  *kernel*: The kernel to be scanned through the image
    -#  *anchor*: The position of the anchor relative to its kernel. The location *Point(-1, -1)*
        indicates the center by default.
    -#  *delta*: A value to be added to each pixel during the convolution. By default it is \f$0\f$
    -#  *BORDER_DEFAULT*: We let this value by default (more details in the following tutorial)

-#  Our program will effectuate a *while* loop, each 500 ms the kernel size of our filter will be
    updated in the range indicated.
@end_toggle

Results
-------

-#  After compiling the code above, you can execute it giving as argument the path of an image. The
    result should be a window that shows an image blurred by a normalized filter. Each 0.5 seconds
    the kernel size should change, as can be seen in the series of snapshots below:

    ![](images/filter_2d_tutorial_result.jpg)

<!-- invisible references list -->
[Imgproc.filter2D()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#filter2D-org.opencv.core.Mat-org.opencv.core.Mat-int-org.opencv.core.Mat-
