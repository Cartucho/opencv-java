Histogram Comparison {#tutorial_histogram_comparison}
====================

@prev_tutorial{tutorial_histogram_equalization}
@next_tutorial{tutorial_back_projection}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}

-   Use the function @ref cv::compareHist to get a numerical parameter that express how well two
    histograms match with each other.
-   Use different metrics to compare histograms

@end_toggle

@add_toggle{Java}

-   Use the function [Imgproc.compareHist()] to get a numerical parameter that express how well two
    histograms match with each other.
-   Use different metrics to compare histograms

@end_toggle

Theory
------

-   To compare two histograms ( \f$H_{1}\f$ and \f$H_{2}\f$ ), first we have to choose a *metric*
    (\f$d(H_{1}, H_{2})\f$) to express how well both histograms match.
-   OpenCV implements the function @ref cv::compareHist to perform a comparison. It also offers 4
    different metrics to compute the matching:
    -#  **Correlation ( CV_COMP_CORREL )**
        \f[d(H_1,H_2) =  \frac{\sum_I (H_1(I) - \bar{H_1}) (H_2(I) - \bar{H_2})}{\sqrt{\sum_I(H_1(I) - \bar{H_1})^2 \sum_I(H_2(I) - \bar{H_2})^2}}\f]
        where
        \f[\bar{H_k} =  \frac{1}{N} \sum _J H_k(J)\f]
        and \f$N\f$ is the total number of histogram bins.

    -#  **Chi-Square ( CV_COMP_CHISQR )**
        \f[d(H_1,H_2) =  \sum _I  \frac{\left(H_1(I)-H_2(I)\right)^2}{H_1(I)}\f]

    -#  **Intersection ( method=CV_COMP_INTERSECT )**
        \f[d(H_1,H_2) =  \sum _I  \min (H_1(I), H_2(I))\f]

    -#  **Bhattacharyya distance ( CV_COMP_BHATTACHARYYA )**
        \f[d(H_1,H_2) =  \sqrt{1 - \frac{1}{\sqrt{\bar{H_1} \bar{H_2} N^2}} \sum_I \sqrt{H_1(I) \cdot H_2(I)}}\f]

Code
----

-   **What does this program do?**
    -   Loads a *base image* and 2 *test images* to be compared with it.
    -   Generate 1 image that is the lower half of the *base image*
    -   Convert the images to HSV format
    -   Calculate the H-S histogram for all the images and normalize them in order to compare them.
    -   Compare the histogram of the *base image* with respect to the 2 test histograms, the
        histogram of the lower half base image and with the same base image histogram.
    -   Display the numerical matching parameters obtained.

@add_toggle{C++}

-   **Downloadable code**: Click
    [here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp)
-   **Code at glance:**
    @include cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp

@end_toggle

@add_toggle{Java}

-   **Code at glance:**
    @include samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java

@end_toggle



Explanation
-----------

@add_toggle{C++}

-#  Declare variables such as the matrices to store the base image and the two other images to
    compare ( BGR and HSV )
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp declare_mats
-#  Load the base image (src_base) and the other two test images:
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp load_images
-#  Convert them to HSV format:
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp convert_to_hsv
-#  Also, create an image of half the base image (in HSV format):
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp half_base
-#  Initialize the arguments to calculate the histograms (bins, ranges and channels H and S ).
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp intialize_arguments
-#  Create the MatND objects to store the histograms:
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp mat_histograms
-#  Calculate the Histograms for the base image, the 2 test images and the half-down base image:
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp calculate_histograms
-#  Apply sequentially the 4 comparison methods between the histogram of the base image (hist_base)
    and the other histograms:
    @snippet cpp/tutorial_code/Histograms_Matching/compareHist_Demo.cpp loop

@end_toggle

@add_toggle{Java}

-#  Declare variables such as the matrices to store the base image and the two other images to
    compare ( BGR and HSV )
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java declare_mats
-#  Load the base image (src_base) and the other two test images:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java load_images
-#  Convert them to HSV format:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java convert_to_hsv
-#  Also, create an image of half the base image (in HSV format):
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java half_base
-#  Initialize the arguments to calculate the histograms (bins, ranges and channels H and S ).
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java intialize_arguments
-#  Create the Mat objects to store the histograms:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java mat_histograms
-#  Calculate the Histograms for the base image, the 2 test images and the half-down base image:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java calculate_histograms
-#  Apply sequentially the 4 comparison methods between the histogram of the base image (hist_base)
    and the other histograms:
    @snippet samples/java/tutorial_code/ImgProc/tutorial_histogram_comparison/CompareHistDemo.java loop

@end_toggle

Results
-------

-#  We use as input the following images:
    ![Base_0](images/Histogram_Comparison_Source_0.jpg)
    ![Test_1](images/Histogram_Comparison_Source_1.jpg)
    ![Test_2](images/Histogram_Comparison_Source_2.jpg)
    where the first one is the base (to be compared to the others), the other 2 are the test images.
    We will also compare the first image with respect to itself and with respect of half the base
    image.

-#  We should expect a perfect match when we compare the base image histogram with itself. Also,
    compared with the histogram of half the base image, it should present a high match since both
    are from the same source. For the other two test images, we can observe that they have very
    different lighting conditions, so the matching should not be very good:

-#  Here the numeric results:
      *Method*        |  Base - Base |  Base - Half |  Base - Test 1 |  Base - Test 2
    ----------------- | ------------ | ------------ | -------------- | ---------------
      *Correlation*   |  1.000000    |  0.930766    |  0.182073      |  0.120447
      *Chi-square*    |  0.000000    |  4.940466    |  21.184536     |  49.273437
      *Intersection*  |  24.391548   |  14.959809   |  3.889029      |  5.775088
      *Bhattacharyya* |  0.000000    |  0.222609    |  0.646576      |  0.801869
    For the *Correlation* and *Intersection* methods, the higher the metric, the more accurate the
    match. As we can see, the match *base-base* is the highest of all as expected. Also we can observe
    that the match *base-half* is the second best match (as we predicted). For the other two metrics,
    the less the result, the better the match. We can observe that the matches between the test 1 and
    test 2 with respect to the base are worse, which again, was expected.

<!-- invisible references list -->
[Imgproc.compareHist()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#compareHist-org.opencv.core.Mat-org.opencv.core.Mat-int-
