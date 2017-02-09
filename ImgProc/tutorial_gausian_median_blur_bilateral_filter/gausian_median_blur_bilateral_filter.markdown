Smoothing Images {#tutorial_gausian_median_blur_bilateral_filter}
================

@next_tutorial{tutorial_erosion_dilatation}

Goal
----

In this tutorial you will learn how to apply diverse linear filters to smooth images using OpenCV
functions such as:

@add_toggle{C++}
-   @ref cv::blur
-   @ref cv::GaussianBlur
-   @ref cv::medianBlur
-   @ref cv::bilateralFilter
@end_toggle

@add_toggle{Java}
-   [Imgproc.blur()]
-   [Imgproc.GaussianBlur()] 
-   [Imgproc.medianBlur()]
-   [Imgproc.bilateralFilter()]
@end_toggle

@add_toggle{Python}
-   **cv2.blur()**
-   **cv2.GaussianBlur()**
-   **cv2.medianBlur()**
-   **cv2.bilateralFilter()**
@end_toggle

Theory
------

@note The explanation below belongs to the book [Computer Vision: Algorithms and
Applications](http://szeliski.org/Book/) by Richard Szeliski and to *LearningOpenCV* .. container::
enumeratevisibleitemswithsquare

-   *Smoothing*, also called *blurring*, is a simple and frequently used image processing
    operation.
-   There are many reasons for smoothing. In this tutorial we will focus on smoothing in order to
    reduce noise (other uses will be seen in the following tutorials).
-   To perform a smoothing operation we will apply a *filter* to our image. The most common type
    of filters are *linear*, in which an output pixel's value (i.e. \f$g(i,j)\f$) is determined as a
    weighted sum of input pixel values (i.e. \f$f(i+k,j+l)\f$) :

    \f[g(i,j) = \sum_{k,l} f(i+k, j+l) h(k,l)\f]

    \f$h(k,l)\f$ is called the *kernel*, which is nothing more than the coefficients of the filter.

    It helps to visualize a *filter* as a window of coefficients sliding across the image.

-   There are many kind of filters, here we will mention the most used:

### Normalized Box Filter

-   This filter is the simplest of all! Each output pixel is the *mean* of its kernel neighbors (
    all of them contribute with equal weights)
-   The kernel is below:

    \f[K = \dfrac{1}{K_{width} \cdot K_{height}} \begin{bmatrix}
        1 & 1 & 1 & ... & 1 \\
        1 & 1 & 1 & ... & 1 \\
        . & . & . & ... & 1 \\
        . & . & . & ... & 1 \\
        1 & 1 & 1 & ... & 1
       \end{bmatrix}\f]

### Gaussian Filter

-   Probably the most useful filter (although not the fastest). Gaussian filtering is done by
    convolving each point in the input array with a *Gaussian kernel* and then summing them all to
    produce the output array.
-   Just to make the picture clearer, remember how a 1D Gaussian kernel look like?

    ![](images/Smoothing_Tutorial_theory_gaussian_0.jpg)

    Assuming that an image is 1D, you can notice that the pixel located in the middle would have the
    biggest weight. The weight of its neighbors decreases as the spatial distance between them and
    the center pixel increases.

    @note
    Remember that a 2D Gaussian can be represented as :
    \f[G_{0}(x, y) = A  e^{ \dfrac{ -(x - \mu_{x})^{2} }{ 2\sigma^{2}_{x} } +  \dfrac{ -(y - \mu_{y})^{2} }{ 2\sigma^{2}_{y} } }\f]
    where \f$\mu\f$ is the mean (the peak) and \f$\sigma\f$ represents the variance (per each of the
    variables \f$x\f$ and \f$y\f$)

### Median Filter

The median filter run through each element of the signal (in this case the image) and replace each
pixel with the **median** of its neighboring pixels (located in a square neighborhood around the
evaluated pixel).

### Bilateral Filter

-   So far, we have explained some filters which main goal is to *smooth* an input image. However,
    sometimes the filters do not only dissolve the noise, but also smooth away the *edges*. To avoid
    this (at certain extent at least), we can use a bilateral filter.
-   In an analogous way as the Gaussian filter, the bilateral filter also considers the neighboring
    pixels with weights assigned to each of them. These weights have two components, the first of
    which is the same weighting used by the Gaussian filter. The second component takes into account
    the difference in intensity between the neighboring pixels and the evaluated one.
-   For a more detailed explanation you can check [this
    link](http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/MANDUCHI1/Bilateral_Filtering.html)

Code
----

@add_toggle{C++}
-   **What does this program do?**
    -   Loads an image
    -   Applies 4 different kinds of filters (explained in Theory) and show the filtered images
        sequentially
-   **Downloadable code**: Click
    [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/Smoothing.cpp)
-   **Code at glance:**
    @include samples/cpp/tutorial_code/ImgProc/Smoothing.cpp
@end_toggle

@add_toggle{Java}
-   **What does this program do?**
    -   Loads an image
    -   Applies 4 different kinds of filters (explained in Theory) and show the filtered images
        sequentially
-   **Code at glance:**
    @include samples/java/tutorial_code/ImgProc/GaussianMedianBlurBilateralFilter/GaussianMedianBlurBilateralFilter.java
@end_toggle

@add_toggle{Python}
-   **What does this program do?**
    -   Loads an image
    -   Applies 4 different kinds of filters (explained in Theory) and show the filtered images
        sequentially
-   **Code at glance:**
    @include samples/python/tutorial_code/ImgProc/gaussian_median_blur_bilateral_filter.py
@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Let's check the OpenCV functions that involve only the smoothing procedure, since the rest is
    already known by now.
-#  **Normalized Block Filter:**

    OpenCV offers the function @ref cv::blur to perform smoothing with this filter.
    @snippet samples/cpp/tutorial_code/ImgProc/Smoothing.cpp blur
    We specify 4 arguments (more details, check the Reference):

    -   *src*: Source image
    -   *dst*: Destination image
    -   *Size( w,h )*: Defines the size of the kernel to be used ( of width *w* pixels and height
        *h* pixels)
    -   *Point(-1, -1)*: Indicates where the anchor point (the pixel evaluated) is located with
        respect to the neighborhood. If there is a negative value, then the center of the kernel is
        considered the anchor point.

-#  **Gaussian Filter:**

    It is performed by the function @ref cv::GaussianBlur :
    @snippet samples/cpp/tutorial_code/ImgProc/Smoothing.cpp gaussian_blur
    Here we use 4 arguments (more details, check the OpenCV reference):

    -   *src*: Source image
    -   *dst*: Destination image
    -   *Size(w, h)*: The size of the kernel to be used (the neighbors to be considered). \f$w\f$ and
        \f$h\f$ have to be odd and positive numbers otherwise this size will be calculated using the
        \f$\sigma_{x}\f$ and \f$\sigma_{y}\f$ arguments.
    -   \f$\sigma_{x}\f$: The standard deviation in x. Writing \f$0\f$ implies that \f$\sigma_{x}\f$ is
        calculated using kernel size.
    -   \f$\sigma_{y}\f$: The standard deviation in y. Writing \f$0\f$ implies that \f$\sigma_{y}\f$ is
        calculated using kernel size.

-#  **Median Filter:**

    This filter is provided by the @ref cv::medianBlur function:
    @snippet samples/cpp/tutorial_code/ImgProc/Smoothing.cpp median_blur
    We use three arguments:

    -   *src*: Source image
    -   *dst*: Destination image, must be the same type as *src*
    -   *i*: Size of the kernel (only one because we use a square window). Must be odd.

-#  **Bilateral Filter**

    Provided by OpenCV function @ref cv::bilateralFilter
    @snippet samples/cpp/tutorial_code/ImgProc/Smoothing.cpp bilateral_blur
    We use 5 arguments:

    -   *src*: Source image
    -   *dst*: Destination image
    -   *d*: The diameter of each pixel neighborhood.
    -   \f$\sigma_{Color}\f$: Standard deviation in the color space.
    -   \f$\sigma_{Space}\f$: Standard deviation in the coordinate space (in pixel terms)

@end_toggle

@add_toggle{Java}

-#  Let's check the OpenCV functions that involve only the smoothing procedure, since the rest is
    already known by now.
-#  **Normalized Block Filter:**

    OpenCV offers the function [Imgproc.blur()] to perform smoothing with this filter.
    @snippet samples/java/tutorial_code/ImgProc/GaussianMedianBlurBilateralFilter/GaussianMedianBlurBilateralFilter.java blur
    We specify 4 arguments (more details, check the Reference):

    -   *src*: Source image
    -   *dst*: Destination image
    -   *Size( w,h )*: Defines the size of the kernel to be used ( of width *w* pixels and height
        *h* pixels)
    -   *Point(-1, -1)*: Indicates where the anchor point (the pixel evaluated) is located with
        respect to the neighborhood. If there is a negative value, then the center of the kernel is
        considered the anchor point.

-#  **Gaussian Filter:**

    It is performed by the [Imgproc.GaussianBlur()] function:
    @snippet samples/java/tutorial_code/ImgProc/GaussianMedianBlurBilateralFilter/GaussianMedianBlurBilateralFilter.java gaussian_blur
    Here we use 4 arguments (more details, check the OpenCV reference):

    -   *src*: Source image
    -   *dst*: Destination image
    -   *Size(w, h)*: The size of the kernel to be used (the neighbors to be considered). \f$w\f$ and
        \f$h\f$ have to be odd and positive numbers otherwise this size will be calculated using the
        \f$\sigma_{x}\f$ and \f$\sigma_{y}\f$ arguments.
    -   \f$\sigma_{x}\f$: The standard deviation in x. Writing \f$0\f$ implies that \f$\sigma_{x}\f$ is
        calculated using kernel size.
    -   \f$\sigma_{y}\f$: The standard deviation in y. Writing \f$0\f$ implies that \f$\sigma_{y}\f$ is
        calculated using kernel size.

-#  **Median Filter:**

    This filter is provided by the [Imgproc.medianBlur()] function:
    @snippet samples/java/tutorial_code/ImgProc/GaussianMedianBlurBilateralFilter/GaussianMedianBlurBilateralFilter.java median_blur
    We use three arguments:

    -   *src*: Source image
    -   *dst*: Destination image, must be the same type as *src*
    -   *i*: Size of the kernel (only one because we use a square window). Must be odd.

-#  **Bilateral Filter**

    Provided by OpenCV [Imgproc.bilateralFilter()] function:
    @snippet samples/java/tutorial_code/ImgProc/GaussianMedianBlurBilateralFilter/GaussianMedianBlurBilateralFilter.java bilateral_blur
    We use 5 arguments:

    -   *src*: Source image
    -   *dst*: Destination image
    -   *d*: The diameter of each pixel neighborhood.
    -   \f$\sigma_{Color}\f$: Standard deviation in the color space.
    -   \f$\sigma_{Space}\f$: Standard deviation in the coordinate space (in pixel terms)

@end_toggle

@add_toggle{Python}

-#  Let's check the OpenCV functions that involve only the smoothing procedure, since the rest is
    already known by now.
-#  **Normalized Block Filter:**

    OpenCV offers the function **cv2.blur()** to perform smoothing with this filter.
    @snippet samples/python/tutorial_code/ImgProc/gaussian_median_blur_bilateral_filter.py blur
    Here we use the arguments ( *dst = cv2.blur(src, (w,h))* ):
    -   *dst*: Destination image
    -   *src*: Source image
    -   (w,h): Defines the size of the kernel to be used ( of width *w* pixels and height
        *h* pixels)

-#  **Gaussian Filter:**

    It is performed by the function **cv2.GaussianBlur()** :
    @snippet samples/python/tutorial_code/ImgProc/gaussian_median_blur_bilateral_filter.py gaussian_blur
    Here we use the arguments ( *dst = cv2.GaussianBlur(src, (w,h))* ):

    -   *dst*: Destination image
    -   *src*: Source image
    -   (w, h): Defines the size of the kernel to be used (the neighbors to be considered). \f$w\f$ and
        \f$h\f$ have to be odd and positive numbers otherwise this will be calculated using the
        \f$\sigma_{x}\f$ and \f$\sigma_{y}\f$ arguments.
    -   \f$\sigma_{x}\f$: The standard deviation in x. Writing \f$0\f$ implies that \f$\sigma_{x}\f$ is
        calculated using kernel size. Since only \f$\sigma_{x}\f$ was specified, \f$\sigma_{y}\f$ takes
        the same value as \f$\sigma_{x}\f$.

-#  **Median Filter:**

    This filter is provided by the **cv2.medianBlur()** function:
    @snippet samples/python/tutorial_code/ImgProc/gaussian_median_blur_bilateral_filter.py median_blur
    Here we use the arguments ( *dst = cv2.medianBlur(src, i)* ):

    -   *src*: Source image
    -   *dst*: Destination image, must be the same type as *src*
    -   *i*: Size of the kernel (only one because we use a square window). Must be odd.

-#  **Bilateral Filter**

    Provided by OpenCV function **cv2.bilateralFilter()**
    @snippet samples/python/tutorial_code/ImgProc/gaussian_median_blur_bilateral_filter.py bilateral_blur
    Here we use the arguments ( dst = cv2.bilateralFilter(src, d, \f$\sigma_{Color}\f$, \f$\sigma_{Space}\f$) ):

    -   *src*: Source image
    -   *dst*: Destination image
    -   *d*: The diameter of each pixel neighborhood.
    -   \f$\sigma_{Color}\f$: Standard deviation in the color space.
    -   \f$\sigma_{Space}\f$: Standard deviation in the coordinate space (in pixel terms)

@end_toggle

Results
-------

-   The code opens an image (in this case *lena.jpg*) and display it under the effects of the 4
    filters explained.
-   Here is a snapshot of the image smoothed using *medianBlur*:

    ![](images/Smoothing_Tutorial_Result_Median_Filter.jpg)

<!-- invisible references list -->
[Imgproc.blur()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#blur-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Size-
[Imgproc.GaussianBlur()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#GaussianBlur-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Size-double-
[Imgproc.medianBlur()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#medianBlur-org.opencv.core.Mat-org.opencv.core.Mat-int-
[Imgproc.bilateralFilter()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#bilateralFilter-org.opencv.core.Mat-org.opencv.core.Mat-int-double-double-
