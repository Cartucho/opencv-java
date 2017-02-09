Histogram Calculation {#tutorial_histogram_calculation}
=====================

@prev_tutorial{tutorial_histogram_equalization}
@next_tutorial{tutorial_histogram_comparison}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the OpenCV function @ref cv::split to divide an image into its correspondent planes.
-   To calculate histograms of arrays of images by using the OpenCV function @ref cv::calcHist
-   To normalize an array by using the function @ref cv::normalize

@end_toggle

@add_toggle{Java}

-   Use the OpenCV function [Core.split()] to divide an image into its correspondent planes.
-   To calculate histograms of arrays of images by using the OpenCV function [Imgproc.calcHist()]
-   To normalize an array by using the function [Imgproc.normalize()]

@end_toggle

@note In the last tutorial (@ref tutorial_histogram_equalization) we talked about a particular kind of
histogram called *Image histogram*. Now we will considerate it in its more general concept. Read on!

Theory
------

### What are histograms?

-   Histograms are collected *counts* of data organized into a set of predefined *bins*
-   When we say *data* we are not restricting it to be intensity values (as we saw in the previous
    Tutorial). The data collected can be whatever feature you find useful to describe your image.
-   Let's see an example. Imagine that a Matrix contains information of an image (i.e. intensity in
    the range \f$0-255\f$):

    ![](images/Histogram_Calculation_Theory_Hist0.jpg)

-   What happens if we want to *count* this data in an organized way? Since we know that the *range*
    of information value for this case is 256 values, we can segment our range in subparts (called
    **bins**) like:

    \f[\begin{array}{l}
    [0, 255] = { [0, 15] \cup [16, 31] \cup ....\cup [240,255] } \\
    range = { bin_{1} \cup bin_{2} \cup ....\cup bin_{n = 15} }
    \end{array}\f]

    and we can keep count of the number of pixels that fall in the range of each \f$bin_{i}\f$. Applying
    this to the example above we get the image below ( axis x represents the bins and axis y the
    number of pixels in each of them).

    ![](images/Histogram_Calculation_Theory_Hist1.jpg)

-   This was just a simple example of how an histogram works and why it is useful. An histogram can
    keep count not only of color intensities, but of whatever image features that we want to measure
    (i.e. gradients, directions, etc).
-   Let's identify some parts of the histogram:
    -#  **dims**: The number of parameters you want to collect data of. In our example, **dims = 1**
        because we are only counting the intensity values of each pixel (in a greyscale image).
    -#  **bins**: It is the number of **subdivisions** in each dim. In our example, **bins = 16**
    -#  **range**: The limits for the values to be measured. In this case: **range = [0,255]**
-   What if you want to count two features? In this case your resulting histogram would be a 3D plot
    (in which x and y would be \f$bin_{x}\f$ and \f$bin_{y}\f$ for each feature and z would be the number of
    counts for each combination of \f$(bin_{x}, bin_{y})\f$. The same would apply for more features (of
    course it gets trickier).

### What OpenCV offers you

@add_toggle{C++}

For simple purposes, OpenCV implements the function @ref cv::calcHist , which calculates the
histogram of a set of arrays (usually images or image planes). It can operate with up to 32
dimensions. We will see it in the code below!

@end_toggle

@add_toggle{Java}

For simple purposes, OpenCV implements the function [Imgproc.calcHist()] , which calculates the
histogram of a set of arrays (usually images or image planes). It can operate with up to 32
dimensions. We will see it in the code below!

@end_toggle

Code
----

@add_toggle{C++}

-   **What does this program do?**
    -   Loads an image
    -   Splits the image into its R, G and B planes using the function @ref cv::split
    -   Calculate the Histogram of each 1-channel plane by calling the function @ref cv::calcHist
    -   Plot the three histograms in a window
-   **Downloadable code**: Click
    [here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp)
-   **Code at glance:**
    @include samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp

@end_toggle

@add_toggle{Java}

-   **What does this program do?**
    -   Loads an image
    -   Splits the image into its R, G and B planes using the function [Core.split()]
    -   Calculate the Histogram of each 1-channel plane by calling the function [Imgproc.calcHist()]
    -   Plot the three histograms in a window
-   **Code at glance:**
    @include samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java


@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Create the necessary matrices:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp create_mats
-#  Load the source image
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp load_image
-#  Separate the source image in its three R,G and B planes. For this we use the OpenCV function
    @ref cv::split :
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp bgr
    our input is the image to be divided (this case with three channels) and the output is a vector
    of Mat )
-#  Now we are ready to start configuring the **histograms** for each plane. Since we are working
    with the B, G and R planes, we know that our values will range in the interval \f$[0,255]\f$
    -#  Establish number of bins (5, 10...):
        @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp bins_number
    -#  Set the range of values (as we said, between 0 and 255 )
        @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp set_ranges
    -#  We want our bins to have the same size (uniform) and to clear the histograms in the
        beginning, so:
        @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp parameters
    -#  Finally, we create the Mat objects to save our histograms. Creating 3 (one for each plane):
        @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp create_bgr_mats
    -#  We proceed to calculate the histograms by using the OpenCV function @ref cv::calcHist :
        @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp calc_hist
        where the arguments are:

        -   **bgr_planes**: The source array(s)
        -   **1**: The number of source arrays (in this case we are using 1. We can enter here also
            a list of arrays )
        -   **0**: The channel (*dim*) to be measured. In this case it is just the intensity (each
            array is single-channel) so we just write 0.
        -   **Mat()**: A mask to be used on the source array ( zeros indicating pixels to be ignored
            ). If not defined it is not used
        -   **hist_b**: The Mat object where the histogram will be stored
        -   **1**: The histogram dimensionality.
        -   **histSize:** The number of bins per each used dimension
        -   **histRange:** The range of values to be measured per each dimension
        -   **uniform** and **accumulate**: The bin sizes are the same and the histogram is cleared
            at the beginning.

-#  Create an image to display the histograms:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp to_display
-#  Notice that before drawing, we first @ref cv::normalize the histogram so its values fall in the
    range indicated by the parameters entered:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp normalize
    this function receives these arguments:

    -   **hist_b:** Input array
    -   **hist_b:** Output normalized array (can be the same)
    -   **0** and\**histImage.rows: For this example, they are the lower and upper limits to
        normalize the values ofr_hist*\*
    -   **NORM_MINMAX:** Argument that indicates the type of normalization (as described above, it
        adjusts the values between the two limits set before)
    -   **-1:** Implies that the output normalized array will be the same type as the input
    -   **Mat():** Optional mask

-#  Finally, observe that to access the bin (in this case in this 1D-Histogram):
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp draw
    we use the expression:
    @code{.cpp}
    b_hist.at<float>(i)
    @endcode
    where \f$i\f$ indicates the dimension. If it were a 2D-histogram we would use something like:
    @code{.cpp}
    b_hist.at<float>( i, j )
    @endcode
-#  Finally we display our histograms and wait for the user to exit:
    @snippet samples/cpp/tutorial_code/Histograms_Matching/calcHist_Demo.cpp imshow

@end_toggle

@add_toggle{Java}

-#  Create the necessary matrices:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java create_mats
-#  Load the source image
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java load_image
-#  Separate the source image in its three R,G and B planes. For this we use the OpenCV [Core.split()] function :
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java bgr
    our input is the image to be divided (this case with three channels) and the output is a vector
    of Mat )
-#  Now we are ready to start configuring the **histograms** for each plane. Since we are working
    with the B, G and R planes, we know that our values will range in the interval \f$[0,255]\f$
    -#  Establish number of bins (5, 10...):
        @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java bins_number
    -#  Set the range of values (as we said, between 0 and 255 )
        @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java set_ranges
    -#  We want to clear the histograms in the
        beginning, so:
        @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java parameters
    -#  Finally, we create the Mat objects to save our histograms. Creating 3 (one for each plane):
        @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java create_bgr_mats
    -#  We proceed to calculate the histograms by using the OpenCV [Imgproc.calcHist()] function :
        @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java calc_hist
        where the arguments are:

        -   **bgr_planes.subList(0, 1)**: The source
        -   **channels = new MatOfInt(0)**: The channel (*dim*) to be measured. In this case it is just the intensity (each
            array is single-channel) so we just write 0.
        -   **new Mat()**: A mask to be used on the source array ( zeros indicating pixels to be ignored
            ). If not defined it is not used
        -   **hist_b**: The Mat object where the histogram will be stored
        -   **histSize:** The number of bins per each used dimension
        -   **range:** The range of values to be measured per each dimension
        -   **accumulate = false**: The histogram is cleared at the beginning.

-#  Create an image to display the histograms:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java to_display
-#  Notice that before drawing, we first [Imgproc.normalize()] the histogram so its values fall in the
    range indicated by the parameters entered:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java normalize
    this function receives these arguments:

    -   **hist_b:** Input array
    -   **hist_b:** Output normalized array (can be the same)
    -   **0** and\**histImage.rows: For this example, they are the lower and upper limits to
        normalize the values ofr_hist*\*
    -   **NORM_MINMAX:** Argument that indicates the type of normalization (as described above, it
        adjusts the values between the two limits set before)
    -   **-1:** Implies that the output normalized array will be the same type as the input
    -   **new Mat():** Optional mask

-#  Finally, observe that to access the bin (in this case in this 1D-Histogram):
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java draw
-#  Finally we display our histograms and wait for the user to exit:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_calculation/CalcHistDemo.java imshow

@end_toggle

Result
------

-#  Using as input argument an image like the shown below:

    ![](images/Histogram_Calculation_Original_Image.jpg)

-#  Produces the following histogram:

    ![](images/Histogram_Calculation_Result.jpg)

<!-- invisible references list -->
[Core.split()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#split-org.opencv.core.Mat-java.util.List-
[Imgproc.calcHist()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#calcHist-java.util.List-org.opencv.core.MatOfInt-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.MatOfInt-org.opencv.core.MatOfFloat-
[Imgproc.normalize()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#normalize-org.opencv.core.Mat-org.opencv.core.Mat-
