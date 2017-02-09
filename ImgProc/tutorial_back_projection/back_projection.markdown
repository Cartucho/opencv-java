Back Projection {#tutorial_back_projection}
===============

@prev_tutorial{tutorial_histogram_comparison}
@next_tutorial{tutorial_template_matching}

Goal
----

In this tutorial you will learn:

@add_toggle{C++}

-   What is Back Projection and why it is useful
-   How to use the OpenCV function @ref cv::calcBackProject to calculate Back Projection
-   How to mix different channels of an image by using the OpenCV function @ref cv::mixChannels

@end_toggle

@add_toggle{Java}

-   What is Back Projection and why it is useful
-   How to use the OpenCV function [Imgproc.calcBackProject()] to calculate Back Projection
-   How to mix different channels of an image by using the OpenCV function [Imgproc.mixChannels()]

@end_toggle

Theory
------

### What is Back Projection?

-   Back Projection is a way of recording how well the pixels of a given image fit the distribution
    of pixels in a histogram model.
-   To make it simpler: For Back Projection, you calculate the histogram model of a feature and then
    use it to find this feature in an image.
-   Application example: If you have a histogram of flesh color (say, a Hue-Saturation histogram ),
    then you can use it to find flesh color areas in an image:

### How does it work?

-   We explain this by using the skin example:
-   Let's say you have gotten a skin histogram (Hue-Saturation) based on the image below. The
    histogram besides is going to be our *model histogram* (which we know represents a sample of
    skin tonality). You applied some mask to capture only the histogram of the skin area:
    ![T0](images/Back_Projection_Theory0.jpg)
    ![T1](images/Back_Projection_Theory1.jpg)

-   Now, let's imagine that you get another hand image (Test Image) like the one below: (with its
    respective histogram):
    ![T2](images/Back_Projection_Theory2.jpg)
    ![T3](images/Back_Projection_Theory3.jpg)


-   What we want to do is to use our *model histogram* (that we know represents a skin tonality) to
    detect skin areas in our Test Image. Here are the steps
    -#  In each pixel of our Test Image (i.e. \f$p(i,j)\f$ ), collect the data and find the
        correspondent bin location for that pixel (i.e. \f$( h_{i,j}, s_{i,j} )\f$ ).
    -#  Lookup the *model histogram* in the correspondent bin - \f$( h_{i,j}, s_{i,j} )\f$ - and read
        the bin value.
    -#  Store this bin value in a new image (*BackProjection*). Also, you may consider to normalize
        the *model histogram* first, so the output for the Test Image can be visible for you.
    -#  Applying the steps above, we get the following BackProjection image for our Test Image:

        ![](images/Back_Projection_Theory4.jpg)

    -#  In terms of statistics, the values stored in *BackProjection* represent the *probability*
        that a pixel in *Test Image* belongs to a skin area, based on the *model histogram* that we
        use. For instance in our Test image, the brighter areas are more probable to be skin area
        (as they actually are), whereas the darker areas have less probability (notice that these
        "dark" areas belong to surfaces that have some shadow on it, which in turns affects the
        detection).

Code
----

@add_toggle{C++}

-   **What does this program do?**
    -   Loads an image
    -   Convert the original to HSV format and separate only *Hue* channel to be used for the
        Histogram (using the OpenCV function @ref cv::mixChannels )
    -   Let the user to enter the number of bins to be used in the calculation of the histogram.
    -   Calculate the histogram (and update it if the bins change) and the backprojection of the
        same image.
    -   Display the backprojection and the histogram in windows.
-   **Downloadable code**:

    -#  Click
        [here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp)
        for the basic version (explained in this tutorial).
    -#  For stuff slightly fancier (using H-S histograms and floodFill to define a mask for the
        skin area) you can check the [improved
        demo](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo2.cpp)
    -#  ...or you can always check out the classical
        [camshiftdemo](https://github.com/opencv/opencv/tree/master/samples/cpp/camshiftdemo.cpp)
        in samples.

-   **Code at glance:**
    @include samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp

@end_toggle

@add_toggle{Java}

-   **What does this program do?**
    -   Loads an image
    -   Convert the original to HSV format and separate only *Hue* channel to be used for the
        Histogram (using the OpenCV function [Imgproc.mixChannels()] )
    -   Let the user to enter the number of bins to be used in the calculation of the histogram.
    -   Calculate the histogram (and update it if the bins change) and the backprojection of the
        same image.
    -   Display the backprojection and the histogram in windows.

-   **Code at glance:**
    @include samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java

@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Declare the matrices to store our images and initialize the number of bins to be used by our
    histogram:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp declare
-#  Read the input image and transform it to HSV format:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp load_image
-#  For this tutorial, we will use only the Hue value for our 1-D histogram (check out the fancier
    code in the links above if you want to use the more standard H-S histogram, which yields better
    results):
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp hue
    as you see, we use the function @ref cv::mixChannels to get only the channel 0 (Hue) from
    the hsv image. It gets the following parameters:

    -   **hsv:** The source array from which the channels will be copied
    -   **1:** The number of source arrays
    -   **hue:** The destination array of the copied channels
    -   **1:** The number of destination arrays
    -   **ch[] = {0,0}:** The array of index pairs indicating how the channels are copied. In this
        case, the Hue(0) channel of &hsv is being copied to the 0 channel of &hue (1-channel)
    -   **1:** Number of index pairs

-#  Create a Trackbar for the user to enter the bin values. Any change on the Trackbar means a call
    to the **Hist_and_Backproj** callback function.
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp trackbar
-#  Show the image and wait for the user to exit the program:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp imshow
-#  **Hist_and_Backproj function:** Initialize the arguments needed for @ref cv::calcHist . The
    number of bins comes from the Trackbar:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp hist_and_backproj
-#  Calculate the Histogram and normalize it to the range \f$[0,255]\f$
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp hist_normalize
-#  Get the Backprojection of the same image by calling the function @ref cv::calcBackProject
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp backproj
    all the arguments are known (the same as used to calculate the histogram), only we add the
    backproj matrix, which will store the backprojection of the source image (&hue)
-#  Display backproj:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp imshow_backproj
-#  Draw the 1-D Hue histogram of the image:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcBackProject_Demo1.cpp draw

@end_toggle

@add_toggle{Java}

-#  Declare the matrices to store our images and initialize the number of bins to be used by our
    histogram:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java declare
-#  Read the input image and transform it to HSV format:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java load_image
-#  For this tutorial, we will use only the Hue value for our 1-D histogram (check out the fancier
    code in the links above if you want to use the more standard H-S histogram, which yields better
    results):
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java hue
    as you see, we use the function [Imgproc.mixChannels()] to get only the channel 0 (Hue) from
    the hsv image. It gets the following parameters:

    -   **hsv:** The source array from which the channels will be copied
    -   **hue:** The destination array of the copied channels
    -   **ch = new MatOfInt(0, 0):** The array of index pairs indicating how the channels are copied. In this
        case, the Hue(0) channel of hsv is being copied to the 0 channel of hue (1-channel)

-#  Create a Trackbar (JSlider) for the user to enter the bin values. Any change on the Trackbar means a call
    to the **stateChanged** followed by **histAndBackProj** function.
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java trackbar
-#  **histAndBackProj function:** Initialize the arguments needed for [Imgproc.calcHist()] . The
    number of bins comes from the Trackbar:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java hist_and_backproj
-#  Calculate the Histogram and normalize it to the range \f$[0,255]\f$
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java hist_normalize
-#  Get the Backprojection of the same image by calling the function [Imgproc.calcBackProject()]
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java backproj
    all the arguments are known (the same as used to calculate the histogram), only we add the
    backproj matrix, which will store the backprojection of the source image (hue)
-#  Draw the 1-D Hue histogram of the image:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_back_projection/CalcBackProjectDemo1.java draw

@end_toggle

Results
-------

Here are the output by using a sample image ( guess what? Another hand ). You can play with the
bin values and you will observe how it affects the results:
![R0](images/Back_Projection1_Source_Image.jpg)
![R1](images/Back_Projection1_Histogram.jpg)
![R2](images/Back_Projection1_BackProj.jpg)

<!-- invisible references list -->
[Imgproc.calcBackProject()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#calcBackProject-java.util.List-org.opencv.core.MatOfInt-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.MatOfFloat-double-
[Imgproc.mixChannels()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#mixChannels-java.util.List-java.util.List-org.opencv.core.MatOfInt-
[Imgproc.calcHist()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#calcHist-java.util.List-org.opencv.core.MatOfInt-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.MatOfInt-org.opencv.core.MatOfFloat-
