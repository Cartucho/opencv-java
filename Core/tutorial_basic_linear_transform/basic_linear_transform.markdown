Changing the contrast and brightness of an image! {#tutorial_basic_linear_transform}
=================================================

@prev_tutorial{tutorial_adding_images}
@next_tutorial{tutorial_basic_geometric_drawing}

Goal
----

@add_toggle{C++}
In this tutorial you will learn how to:

-   Access pixel values
-   Initialize a matrix with zeros
-   Learn what @ref cv::saturate_cast does and why it is useful
-   Get some cool info about pixel transformations
@end_toggle

@add_toggle{Java}
In this tutorial you will learn how to:

-   Access pixel values
-   Initialize a matrix with zeros
-   Get some cool info about pixel transformations
@end_toggle

@add_toggle{Python}
In this tutorial you will learn how to:

-   Access pixel values
-   Initialize a matrix with zeros
-   Get some cool info about pixel transformations
@end_toggle

Theory
------

@note
   The explanation below belongs to the book [Computer Vision: Algorithms and
    Applications](http://szeliski.org/Book/) by Richard Szeliski

### Image Processing

-   A general image processing operator is a function that takes one or more input images and
    produces an output image.
-   Image transforms can be seen as:
    -   Point operators (pixel transforms)
    -   Neighborhood (area-based) operators

### Pixel Transforms

-   In this kind of image processing transform, each output pixel's value depends on only the
    corresponding input pixel value (plus, potentially, some globally collected information or
    parameters).
-   Examples of such operators include *brightness and contrast adjustments* as well as color
    correction and transformations.

### Brightness and contrast adjustments

-   Two commonly used point processes are *multiplication* and *addition* with a constant:

    \f[g(x) = \alpha f(x) + \beta\f]

-   The parameters \f$\alpha > 0\f$ and \f$\beta\f$ are often called the *gain* and *bias* parameters;
    sometimes these parameters are said to control *contrast* and *brightness* respectively.
-   You can think of \f$f(x)\f$ as the source image pixels and \f$g(x)\f$ as the output image pixels. Then,
    more conveniently we can write the expression as:

    \f[g(i,j) = \alpha \cdot f(i,j) + \beta\f]

    where \f$i\f$ and \f$j\f$ indicates that the pixel is located in the *i-th* row and *j-th* column.

Code
----

-   The following code performs the operation \f$g(i,j) = \alpha \cdot f(i,j) + \beta\f$ :

@add_toggle{C++}
@include samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp
@end_toggle

@add_toggle{Java}
@include samples/java/tutorial_code/core/BasicLinearTransform.java
@end_toggle

@add_toggle{Python}
@include samples/python/tutorial_code/core/basic_linear_transform.py
@end_toggle

Explanation
-----------

@add_toggle{C++}
-#  We begin by creating parameters to save \f$\alpha\f$ and \f$\beta\f$ to be entered by the user:
    @snippet samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp parameters

-#  We load an image using @ref cv::imread and save it in a Mat object:
    @snippet samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp load_image

-#  Now, since we will make some transformations to this image, we need a new Mat object to store
    it. Also, we want this to have the following features:

    -   Initial pixel values equal to zero
    -   Same size and type as the original image
    @snippet samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp mat_zeros
    We observe that @ref cv::Mat::zeros returns a Matlab-style zero initializer based on
    *image.size()* and *image.type()*

-#  Now, to perform the operation \f$g(i,j) = \alpha \cdot f(i,j) + \beta\f$ we will access to each
    pixel in image. Since we are operating with BGR images, we will have three values per pixel (B,
    G and R), so we will also access them separately. Here is the piece of code:
    @snippet samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp loop
    Notice the following:
    -   To access each pixel in the images we are using this syntax: *image.at\<Vec3b\>(y,x)[c]*
        where *y* is the row, *x* is the column and *c* is R, G or B (0, 1 or 2).
    -   Since the operation \f$\alpha \cdot p(i,j) + \beta\f$ can give values out of range or not
        integers (if \f$\alpha\f$ is float), we use cv::saturate_cast to make sure the
        values are valid.

-#  Finally, we create windows and show the images, the usual way.
    @snippet samples/cpp/tutorial_code/core/tutorial_basic_linear_transform/basic_linear_transform.cpp show_images

@note
    Instead of using the **for** loops to access each pixel, we could have simply used this command:
    @code{.cpp}
    image.convertTo(new_image, -1, alpha, beta);
    @endcode
    where @ref cv::Mat::convertTo would effectively perform *new_image = a*image + beta\*. However, we
    wanted to show you how to access each pixel. In any case, both methods give the same result but
    convertTo is more optimized and works a lot faster.

@end_toggle

@add_toggle{Java}

-#  We begin by creating parameters to save \f$\alpha\f$ and \f$\beta\f$ to be entered by the user:
    @snippet samples/java/tutorial_code/core/BasicLinearTransform.java parameters

-#  We load an image using [Imgcodecs.imread()] and save it in a Mat object:
    @snippet samples/java/tutorial_code/core/BasicLinearTransform.java load_image


-#  Now, since we will make some transformations to this image, we need a new Mat object to store
    it. Also, we want this to have the following features:

    -   Initial pixel values equal to zero
    -   Same size and type as the original image
    @snippet samples/java/tutorial_code/core/BasicLinearTransform.java mat_zeros

    We observe that [Mat.zeros] returns a Matlab-style zero initializer based on
    *image.size()* and *image.type()*

-#  Now, to perform the operation \f$g(i,j) = \alpha \cdot f(i,j) + \beta\f$ we will access to each
    pixel in image. Since we are operating with BGR images, we will have three values per pixel (B,
    G and R), so we will also access them separately. Here is the piece of code:
    @snippet samples/java/tutorial_code/core/BasicLinearTransform.java loop

    Notice the following:
    -   To access each pixel in the images we are using this syntax: *image.get(y, x)[c]*
        where *y* is the row, *x* is the column and *c* is R, G or B (0, 1 or 2).

-#  Finally, we create windows and show the images.
    @snippet samples/java/tutorial_code/core/BasicLinearTransform.java show_images

@note
    Instead of using the **for** loops to access each pixel, we could have simply used this command:
    @code{.java}
    image.convertTo(new_image, -1, alpha, beta);
    @endcode
    where [Mat.convertTo] would effectively perform *new_image = a*image + beta\*. However, we
    wanted to show you how to access each pixel. In any case, both methods give the same result but
    convertTo is more optimized and works a lot faster.

@end_toggle

@add_toggle{Python}
-#  We begin by creating parameters to save \f$\alpha\f$ and \f$\beta\f$ to be entered by the user:
    @code{.py}
    alpha = float(input('* Enter the alpha value [1.0-3.0]: '))     # Simple contrast control
    beta = int(input('Enter the beta value [0-100]: '))             # Simple brightness control
    @endcode

-#  We load an image using @ref cv::imread and save it in a Mat object:
    @code{.py}
    img = cv2.imread('lena.jpg')
    @endcode

-#  Now, to perform the operation \f$g(i,j) = \alpha \cdot f(i,j) + \beta\f$ we will access to each
    pixel in image.
    @code{.py}
        mul_img = cv2.multiply(img, np.array([alpha]))                    # mul_img = img*alpha
        new_img = cv2.add(mul_img, beta)                                  # new_img = img*alpha + beta
    @endcode

-#  Finally, we create windows and show the images, the usual way.
    @code{.py}
        cv2.imshow('original_image', img)
        cv2.imshow('new_image', new_img)

        cv2.waitKey(0)
        cv2.destroyAllWindows()
    @endcode

@end_toggle

Result
------

-   Running our code and using \f$\alpha = 2.2\f$ and \f$\beta = 50\f$
    @code{.bash}
    $ ./BasicLinearTransforms lena.jpg
    Basic Linear Transforms
    -------------------------
    * Enter the alpha value [1.0-3.0]: 2.2
    * Enter the beta value [0-100]: 50
    @endcode

-   We get this:

    ![](images/Basic_Linear_Transform_Tutorial_Result_big.jpg)

<!-- invisible references list -->
[Imgcodecs.imread()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgcodecs/Imgcodecs.html#imread-java.lang.String-
[Mat.zeros]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Mat.html#zeros-org.opencv.core.Size-int-
[Mat.convertTo]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Mat.html#convertTo-org.opencv.core.Mat-int-double-double-
