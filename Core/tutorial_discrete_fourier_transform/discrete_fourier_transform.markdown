Discrete Fourier Transform {#tutorial_discrete_fourier_transform}
==========================

@prev_tutorial{tutorial_random_generator_and_text}
@next_tutorial{tutorial_file_input_output_with_xml_yml}

Goal
----

We'll seek answers for the following questions:

@add_toggle{C++}
-   What is a Fourier transform and why use it?
-   How to do it in OpenCV?
-   Usage of functions such as: @ref cv::copyMakeBorder() , @ref cv::merge() , @ref cv::dft() , @ref
    cv::getOptimalDFTSize() , @ref cv::log() and @ref cv::normalize() .
@end_toggle
@add_toggle{Java}
-   What is a Fourier transform and why use it?
-   How to do it in OpenCV?
-   Usage of functions such as: [Core.copyMakeBorder()] , [Core.merge()] , [Core.dft()] , [Core.getOptimalDFTSize()] ,
    [Core.log()] and [Core.normalize()].
@end_toggle

Source code
-----------

@add_toggle{C++}
You can [download this from here
](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp) or
find it in the
`samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp` of the
OpenCV source code library.
@end_toggle

@add_toggle{Java}
You can find it in the `samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java` of the
OpenCV source code library.
@end_toggle

@add_toggle{C++}
Here's a sample usage of @ref cv::dft() :

@includelineno cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp
@end_toggle

@add_toggle{Java}
Here's a sample usage of:  [Core.dft()]

@includelineno java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java
@end_toggle

Explanation
-----------

The Fourier Transform will decompose an image into its sinus and cosines components. In other words,
it will transform an image from its spatial domain to its frequency domain. The idea is that any
function may be approximated exactly with the sum of infinite sinus and cosines functions. The
Fourier Transform is a way how to do this. Mathematically a two dimensional images Fourier transform
is:

\f[F(k,l) = \displaystyle\sum\limits_{i=0}^{N-1}\sum\limits_{j=0}^{N-1} f(i,j)e^{-i2\pi(\frac{ki}{N}+\frac{lj}{N})}\f]\f[e^{ix} = \cos{x} + i\sin {x}\f]

Here f is the image value in its spatial domain and F in its frequency domain. The result of the
transformation is complex numbers. Displaying this is possible either via a *real* image and a
*complex* image or via a *magnitude* and a *phase* image. However, throughout the image processing
algorithms only the *magnitude* image is interesting as this contains all the information we need
about the images geometric structure. Nevertheless, if you intend to make some modifications of the
image in these forms and then you need to retransform it you'll need to preserve both of these.

In this sample I'll show how to calculate and show the *magnitude* image of a Fourier Transform. In
case of digital images are discrete. This means they may take up a value from a given domain value.
For example in a basic gray scale image values usually are between zero and 255. Therefore the
Fourier Transform too needs to be of a discrete type resulting in a Discrete Fourier Transform
(*DFT*). You'll want to use this whenever you need to determine the structure of an image from a
geometrical point of view. Here are the steps to follow (in case of a gray scale input image *I*):

@add_toggle{C++}
-#  **Expand the image to an optimal size**. The performance of a DFT is dependent of the image
    size. It tends to be the fastest for image sizes that are multiple of the numbers two, three and
    five. Therefore, to achieve maximal performance it is generally a good idea to pad border values
    to the image to get a size with such traits. The @ref cv::getOptimalDFTSize() returns this
    optimal size and we can use the @ref cv::copyMakeBorder() function to expand the borders of an
    image:
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp expand
    The appended pixels are initialized with zero.

-#  **Make place for both the complex and the real values**. The result of a Fourier Transform is
    complex. This implies that for each image value the result is two image values (one per
    component). Moreover, the frequency domains range is much larger than its spatial counterpart.
    Therefore, we store these usually at least in a *float* format. Therefore we'll convert our
    input image to this type and expand it with another channel to hold the complex values:
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp complex_and_real

-#  **Make the Discrete Fourier Transform**. It's possible an in-place calculation (same input as
    output):
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp dft

-#  **Transform the real and complex values to magnitude**. A complex number has a real (*Re*) and a
    complex (imaginary - *Im*) part. The results of a DFT are complex numbers. The magnitude of a
    DFT is:

    \f[M = \sqrt[2]{ {Re(DFT(I))}^2 + {Im(DFT(I))}^2}\f]

    Translated to OpenCV code:
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp magnitude

-#  **Switch to a logarithmic scale**. It turns out that the dynamic range of the Fourier
    coefficients is too large to be displayed on the screen. We have some small and some high
    changing values that we can't observe like this. Therefore the high values will all turn out as
    white points, while the small ones as black. To use the gray scale values to for visualization
    we can transform our linear scale to a logarithmic one:

    \f[M_1 = \log{(1 + M)}\f]

    Translated to OpenCV code:
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp log

-#  **Crop and rearrange**. Remember, that at the first step, we expanded the image? Well, it's time
    to throw away the newly introduced values. For visualization purposes we may also rearrange the
    quadrants of the result, so that the origin (zero, zero) corresponds with the image center.
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp crop_rearrange

-#  **Normalize**. This is done again for visualization purposes. We now have the magnitudes,
    however this are still out of our image display range of zero to one. We normalize our values to
    this range using the @ref cv::normalize() function.
    @snippet samples/cpp/tutorial_code/core/discrete_fourier_transform/discrete_fourier_transform.cpp normalize

@end_toggle

@add_toggle{Java}
-#  **Expand the image to an optimal size**. The performance of a DFT is dependent of the image
    size. It tends to be the fastest for image sizes that are multiple of the numbers two, three and
    five. Therefore, to achieve maximal performance it is generally a good idea to pad border values
    to the image to get a size with such traits. The [Core.getOptimalDFTSize()] returns this
    optimal size and we can use the [Core.copyMakeBorder()] function to expand the borders of an
    image:
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java expand
    The appended pixels are initialized with zero.

-#  **Make place for both the complex and the real values**. The result of a Fourier Transform is
    complex. This implies that for each image value the result is two image values (one per
    component). Moreover, the frequency domains range is much larger than its spatial counterpart.
    Therefore, we store these usually at least in a *float* format. Therefore we'll convert our
    input image to this type and expand it with another channel to hold the complex values:
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java complex_and_real

-#  **Make the Discrete Fourier Transform**. It's possible an in-place calculation (same input as
    output):
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java dft

-#  **Transform the real and complex values to magnitude**. A complex number has a real (*Re*) and a
    complex (imaginary - *Im*) part. The results of a DFT are complex numbers. The magnitude of a
    DFT is:

    \f[M = \sqrt[2]{ {Re(DFT(I))}^2 + {Im(DFT(I))}^2}\f]

    Translated to OpenCV code:
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java magnitude

-#  **Switch to a logarithmic scale**. It turns out that the dynamic range of the Fourier
    coefficients is too large to be displayed on the screen. We have some small and some high
    changing values that we can't observe like this. Therefore the high values will all turn out as
    white points, while the small ones as black. To use the gray scale values to for visualization
    we can transform our linear scale to a logarithmic one:

    \f[M_1 = \log{(1 + M)}\f]

    Translated to OpenCV code:
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java log

-#  **Crop and rearrange**. Remember, that at the first step, we expanded the image? Well, it's time
    to throw away the newly introduced values. For visualization purposes we may also rearrange the
    quadrants of the result, so that the origin (zero, zero) corresponds with the image center.
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java crop_rearrange

-#  **Normalize**. This is done again for visualization purposes. We now have the magnitudes,
    however this are still out of our image display range of zero to 255. We normalize our values to
    this range using the [Core.normalize()] function.
    @snippet samples/java/tutorial_code/core/discrete_fourier_transform/DiscreteFourierTransform.java normalize

@end_toggle

Result
------

An application idea would be to determine the geometrical orientation present in the image. For
example, let us find out if a text is horizontal or not? Looking at some text you'll notice that the
text lines sort of form also horizontal lines and the letters form sort of vertical lines. These two
main components of a text snippet may be also seen in case of the Fourier transform. Let us use
[this horizontal ](https://github.com/Itseez/opencv/tree/master/samples/data/imageTextN.png) and [this rotated](https://github.com/Itseez/opencv/tree/master/samples/data/imageTextR.png)
image about a text.

In case of the horizontal text:

![](images/result_normal.jpg)

In case of a rotated text:

![](images/result_rotated.jpg)

You can see that the most influential components of the frequency domain (brightest dots on the
magnitude image) follow the geometric rotation of objects on the image. From this we may calculate
the offset and perform an image rotation to correct eventual miss alignments.

<!-- invisible references list -->
[Core.copyMakeBorder()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#copyMakeBorder-org.opencv.core.Mat-org.opencv.core.Mat-int-int-int-int-int-
[Core.merge()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#merge-java.util.List-org.opencv.core.Mat-
[Core.dft()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#dft-org.opencv.core.Mat-org.opencv.core.Mat-
[Core.getOptimalDFTSize()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#getOptimalDFTSize-int-
[Core.log()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#log-org.opencv.core.Mat-org.opencv.core.Mat-
[Core.normalize()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#normalize-org.opencv.core.Mat-org.opencv.core.Mat-
