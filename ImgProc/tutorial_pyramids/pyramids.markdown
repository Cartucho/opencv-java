Image Pyramids {#tutorial_pyramids}
==============

@prev_tutorial{tutorial_morph_lines_detection}
@next_tutorial{tutorial_threshold}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}
-   Use the OpenCV functions @ref cv::pyrUp and @ref cv::pyrDown to downsample or upsample a given
    image.
@end_toggle
@add_toggle{Java}
-   Use the OpenCV functions [Imgproc.pyrUp()] and [Imgproc.pyrDown()] to downsample or upsample a given image.
@end_toggle
@add_toggle{Python}
-   Use the OpenCV functions **cv2.pyrUp()** and **cv2.pyrDown()** to downsample or upsample a given
    image.
@end_toggle

Theory
------

@note The explanation below belongs to the book **Learning OpenCV** by Bradski and Kaehler.

-   Usually we need to convert an image to a size different than its original. For this, there are
    two possible options:
    -#  *Upsize* the image (zoom in) or
    -#  *Downsize* it (zoom out).

@add_toggle{C++}

-   Although there is a *geometric transformation* function in OpenCV that -literally- resize an
    image (@ref cv::resize , which we will show in a future tutorial), in this section we analyze
    first the use of **Image Pyramids**, which are widely applied in a huge range of vision
    applications.

@end_toggle
@add_toggle{Java}

-   Although there is a *geometric transformation* function in OpenCV that -literally- resize an
    image ([Imgproc.resize()] , which we will show in a future tutorial), in this section we analyze
    first the use of **Image Pyramids**, which are widely applied in a huge range of vision
    applications.

@end_toggle
@add_toggle{Python}

-   Although there is a *geometric transformation* function in OpenCV that -literally- resize an
    image (**cv2.resize()** , which we will show in a future tutorial), in this section we analyze
    first the use of **Image Pyramids**, which are widely applied in a huge range of vision
    applications.

@end_toggle


### Image Pyramid

-   An image pyramid is a collection of images - all arising from a single original image - that are
    successively downsampled until some desired stopping point is reached.
-   There are two common kinds of image pyramids:
    -   **Gaussian pyramid:** Used to downsample images
    -   **Laplacian pyramid:** Used to reconstruct an upsampled image from an image lower in the
        pyramid (with less resolution)
-   In this tutorial we'll use the *Gaussian pyramid*.

#### Gaussian Pyramid

-   Imagine the pyramid as a set of layers in which the higher the layer, the smaller the size.

    ![](images/Pyramids_Tutorial_Pyramid_Theory.png)

-   Every layer is numbered from bottom to top, so layer \f$(i+1)\f$ (denoted as \f$G_{i+1}\f$ is smaller
    than layer \f$i\f$ (\f$G_{i}\f$).
-   To produce layer \f$(i+1)\f$ in the Gaussian pyramid, we do the following:
    -   Convolve \f$G_{i}\f$ with a Gaussian kernel:

        \f[\frac{1}{16} \begin{bmatrix} 1 & 4 & 6 & 4 & 1  \\ 4 & 16 & 24 & 16 & 4  \\ 6 & 24 & 36 & 24 & 6  \\ 4 & 16 & 24 & 16 & 4  \\ 1 & 4 & 6 & 4 & 1 \end{bmatrix}\f]

    -   Remove every even-numbered row and column.

-   You can easily notice that the resulting image will be exactly one-quarter the area of its
    predecessor. Iterating this process on the input image \f$G_{0}\f$ (original image) produces the
    entire pyramid.
-   The procedure above was useful to downsample an image. What if we want to make it bigger?:
    columns filled with zeros (\f$0\f$)
    -   First, upsize the image to twice the original in each dimension, wit the new even rows and
    -   Perform a convolution with the same kernel shown above (multiplied by 4) to approximate the
        values of the "missing pixels"

@add_toggle{C++}

-   These two procedures (downsampling and upsampling as explained above) are implemented by the
    OpenCV functions @ref cv::pyrUp and @ref cv::pyrDown , as we will see in an example with the
    code below:

@end_toggle
@add_toggle{Java}

-   These two procedures (downsampling and upsampling as explained above) are implemented by the
    OpenCV functions [Imgproc.pyrUp()] and [Imgproc.pyrDown()] , as we will see in an example with the
    code below:

@end_toggle
@add_toggle{Python}

-   These two procedures (downsampling and upsampling as explained above) are implemented by the
    OpenCV functions **cv2.pyrUp()** and **cv2.pyrDown()** , as we will see in an example with the
    code below:

@end_toggle

@note When we reduce the size of an image, we are actually *losing* information of the image.

Code
----

@add_toggle{C++}

This tutorial code's is shown in the lines below. You can also download it from
[here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/Pyramids.cpp)

@include samples/cpp/tutorial_code/ImgProc/Pyramids.cpp

@end_toggle
@add_toggle{Java}

This tutorial code's is shown in the lines below.

@include samples/java/tutorial_code/ImgProc/Pyramids/Pyramids.java

@end_toggle
@add_toggle{Python}

This tutorial code's is shown in the lines below.

@include samples/python/tutorial_code/ImgProc/Pyramids.py

@end_toggle

Explanation
-----------

Let's check the general structure of the program:
@add_toggle{C++}

-   Load an image (in this case it is defined in the program, the user does not have to enter it
    as an argument)
    @snippet samples/cpp/tutorial_code/ImgProc/Pyramids.cpp load_image

-   Create a window to display the result
    @snippet samples/cpp/tutorial_code/ImgProc/Pyramids.cpp create_window

-   Perform an infinite loop waiting for user input.
    @snippet samples/cpp/tutorial_code/ImgProc/Pyramids.cpp loop
    Our program exits if the user presses *ESC*. Besides, it has two options:

    -   **Perform upsampling (after pressing 'u')**
        @snippet samples/cpp/tutorial_code/ImgProc/Pyramids.cpp pyrUp
        We use the function @ref cv::pyrUp with 03 arguments, since we want to keep updating _img_
        it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly the double of the
            input image)
        -   *Size( img.cols*2, img.rows\*2 )\* : The destination size. Since we are upsampling,
            @ref cv::pyrUp expects a size double than the input image (in this case *tmp*).
    -   **Perform downsampling (after pressing 'd')**
        @snippet samples/cpp/tutorial_code/ImgProc/Pyramids.cpp pyrDown
        Similarly as with @ref cv::pyrUp , we use the function @ref cv::pyrDown with 03
        arguments, since we want to keep updating _img_ it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly half the input
            image)
        -   *Size( img.cols/2, img.rows/2 )* : The destination size. Since we are upsampling,
            @ref cv::pyrDown expects half the size the input image (in this case *tmp*).
    -   Notice that it is important that the input image can be divided by a factor of two (in
        both dimensions). Otherwise, an error will be shown.

@end_toggle
@add_toggle{Java}

-   Load an image (in this case it is defined in the program, the user does not have to enter it
    as an argument)
    @snippet samples/java/tutorial_code/ImgProc/Pyramids/Pyramids.java load_image

-   Create a window to display the result
    @snippet samples/java/tutorial_code/ImgProc/Pyramids/Pyramids.java show_image

-   Perform an infinite loop waiting for user input.
    @snippet samples/java/tutorial_code/ImgProc/Pyramids/Pyramids.java loop

    Our program exits if the user enters _0_. Besides, it has two options:

    -   **Perform upsampling (after pressing '1')**
        @code{.java}
        Imgproc.pyrUp( img, img, new Size( img.cols()*2, img.rows()*2 ) );
        @endcode
        We use the function [Imgproc.pyrUp()] with 03 arguments, since we want to keep updating _img_
        it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly the double of the
            input image)
        -   *Size( img.cols*2, img.rows\*2 )* : The destination size. Since we are upsampling,
            [Imgproc.pyrUp()] expects a size double than the input image (in this case *tmp*).
    -   **Perform downsampling (after pressing '2')**
        @code{.java}
        Imgproc.pyrDown( img, img, new Size( img.cols()/2, img.rows()/2 ) );
        @endcode
        Similarly as with [Imgproc.pyrUp()] , we use the function [Imgproc.pyrDown()] with 03
        arguments, since we want to keep updating _img_ it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly half the input
            image)
        -   *Size( img.cols/2, img.rows/2 )* : The destination size. Since we are upsampling,
            [Imgproc.pyrDown()] expects half the size the input image (in this case *tmp*).
    -   Notice that it is important that the input image can be divided by a factor of two (in
        both dimensions). Otherwise, an error will be shown.

@end_toggle
@add_toggle{Python}

-   Load an image (in this case it is defined in the program, the user does not have to enter it
    as an argument)
    @code{.py}
    # Test image - Make sure it is divisible by 2^{n}
    img = cv2.imread('../data/chicky_512.png')
    @endcode

-   Perform an infinite loop waiting for user input.
    @code{.py}
    while 1:
        h, w = img.shape[:2]

        cv2.imshow('Pyramids Demo', img)
        k = cv2.waitKey(10) % 256

        if k == 27:
            break

        elif k == ord('u'):  # Zoom in, make image double size
            img = cv2.pyrUp(img, dstsize=(2 * w, 2 * h))

        elif k == ord('d'):  # Zoom down, make image half the size
            img = cv2.pyrDown(img, dstsize=(w / 2, h / 2))
    @endcode
    Our program exits if the user presses *ESC*. Besides, it has two options:

    -   **Perform upsampling (after pressing 'u')**
        @code{.py}
        img = cv2.pyrUp(img, dstsize=(2 * w, 2 * h))
        @endcode
        We use the function **cv2.pyrUp()** with 03 arguments, since we want to keep updating _img_
        it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly the double of the
            input image)
        -   *dstsize=( img.cols*2, img.rows\*2 )\* : The destination size. Since we are upsampling,
            **cv2.pyrUp()** expects a size double than the input image (in this case *tmp*).
    -   **Perform downsampling (after pressing 'd')**
        @code{.py}
         img = cv2.pyrDown(img, dstsize=(w / 2, h / 2))
        @endcode
        Similarly as with **cv2.pyrUp()** , we use the function **cv2.pyrDown()** with 03
        arguments, since we want to keep updating _img_ it is both our *src* and *dst*:

        -   *src*: The current image, it is initialized with the *src* original image.
        -   *dst*: The destination image (to be shown on screen, supposedly half the input
            image)
        -   *dstsize=( img.cols/2, img.rows/2 )* : The destination size. Since we are upsampling,
            **cv2.pyrDown()** expects half the size the input image (in this case *tmp*).
    -   Notice that it is important that the input image can be divided by a factor of two (in
        both dimensions). Otherwise, an error will be shown.

@end_toggle

Results
-------

-   After compiling the code above we can test it. The program calls an image **chicky_512.jpg**
    that comes in the *tutorial_code/image* folder. Notice that this image is \f$512 \times 512\f$,
    hence a downsample won't generate any error (\f$512 = 2^{9}\f$). The original image is shown below:

    ![](images/Pyramids_Tutorial_Original_Image.jpg)

-   First we apply two successive pyrDown operations. Our output is:

    ![](images/Pyramids_Tutorial_PyrDown_Result.jpg)

-   Note that we should have lost some resolution due to the fact that we are diminishing the size
    of the image. This is evident after we apply pyrUp twice. Our output
    is now:

    ![](images/Pyramids_Tutorial_PyrUp_Result.jpg)

<!-- invisible references list -->
[Imgproc.pyrUp()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#pyrUp-org.opencv.core.Mat-org.opencv.core.Mat-
[Imgproc.pyrDown()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#pyrDown-org.opencv.core.Mat-org.opencv.core.Mat-
[Imgproc.resize()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#resize-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Size-
