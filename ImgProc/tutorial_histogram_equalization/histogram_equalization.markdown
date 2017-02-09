Histogram Equalization {#tutorial_histogram_equalization}
======================

@prev_tutorial{tutorial_warp_affine}
@next_tutorial{tutorial_histogram_calculation}

Goal
----

In this tutorial you will learn:

@add_toggle{C++}

-   What an image histogram is and why it is useful
-   To equalize histograms of images by using the OpenCV function @ref cv::equalizeHist

@end_toggle

@add_toggle{Java}

-   What an image histogram is and why it is useful
-   To equalize histograms of images by using the OpenCV function [Imgproc.equalizeHist()]

@end_toggle

Theory
------

### What is an Image Histogram?

-   It is a graphical representation of the intensity distribution of an image.
-   It quantifies the number of pixels for each intensity value considered.

![](images/Histogram_Equalization_Theory_0.jpg)

### What is Histogram Equalization?

-   It is a method that improves the contrast in an image, in order to stretch out the intensity
    range.
-   To make it clearer, from the image above, you can see that the pixels seem clustered around the
    middle of the available range of intensities. What Histogram Equalization does is to *stretch
    out* this range. Take a look at the figure below: The green circles indicate the
    *underpopulated* intensities. After applying the equalization, we get an histogram like the
    figure in the center. The resulting image is shown in the picture at right.

![](images/Histogram_Equalization_Theory_1.jpg)

### How does it work?

-   Equalization implies *mapping* one distribution (the given histogram) to another distribution (a
    wider and more uniform distribution of intensity values) so the intensity values are spreaded
    over the whole range.
-   To accomplish the equalization effect, the remapping should be the *cumulative distribution
    function (cdf)* (more details, refer to *Learning OpenCV*). For the histogram \f$H(i)\f$, its
    *cumulative distribution* \f$H^{'}(i)\f$ is:

    \f[H^{'}(i) = \sum_{0 \le j < i} H(j)\f]

    To use this as a remapping function, we have to normalize \f$H^{'}(i)\f$ such that the maximum value
    is 255 ( or the maximum value for the intensity of the image ). From the example above, the
    cumulative function is:

    ![](images/Histogram_Equalization_Theory_2.jpg)

-   Finally, we use a simple remapping procedure to obtain the intensity values of the equalized
    image:

    \f[equalized( x, y ) = H^{'}( src(x,y) )\f]

Code
----

@add_toggle{C++}

-   **What does this program do?**
    -   Loads an image
    -   Convert the original image to grayscale
    -   Equalize the Histogram by using the OpenCV function @ref cv::equalizeHist
    -   Display the source and equalized images in a window.
-   **Downloadable code**: Click
    [here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp)
-   **Code at glance:**
    @include samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp

@end_toggle

@add_toggle{Java}

-   **What does this program do?**
    -   Loads an image
    -   Convert the original image to grayscale
    -   Equalize the Histogram by using the OpenCV function [Imgproc.equalizeHist()]
    -   Display the source and equalized images in a window.
-   **Code at glance:**
    @include samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java

@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Declare the source and destination images as well as the windows names:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp initial_variables
-#  Load the source image:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp load_image
-#  Convert it to grayscale:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp grayscale
-#  Apply histogram equalization with the function @ref cv::equalizeHist :
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp equalization
    As it can be easily seen, the only arguments are the original image and the output (equalized)
    image.
-#  Display both images (original and equalized) :
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp imshow
-#  Wait until user exists the program
    @snippet samples/cpp/tutorial_code/Histograms_Matching/EqualizeHist_Demo.cpp wait_key

@end_toggle

@add_toggle{Java}

-#  Declare the source and destination images as well as the windows names:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java initial_variables
-#  Load the source image:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java load_image
-#  Convert it to grayscale:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java grayscale
-#  Apply histogram equalization with the [Imgproc.equalizeHist()] function :
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java equalization
    As it can be easily seen, the only arguments are the original image and the output (equalized)
    image.
-#  Display both images (original and equalized) :
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_equalization/EqualizeHistDemo.java imshow

@end_toggle

Results
-------

-#  To appreciate better the results of equalization, let's introduce an image with not much
    contrast, such as:

    ![](images/Histogram_Equalization_Original_Image.jpg)

    which, by the way, has this histogram:

    ![](images/Histogram_Equalization_Original_Histogram.jpg)

    notice that the pixels are clustered around the center of the histogram.

-#  After applying the equalization with our program, we get this result:

    ![](images/Histogram_Equalization_Equalized_Image.jpg)

    this image has certainly more contrast. Check out its new histogram like this:

    ![](images/Histogram_Equalization_Equalized_Histogram.jpg)

    Notice how the number of pixels is more distributed through the intensity range.

@note
Are you wondering how did we draw the Histogram figures shown above? Check out the following
tutorial!

<!-- invisible references list -->
[Imgproc.equalizeHist()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#equalizeHist-org.opencv.core.Mat-org.opencv.core.Mat-
