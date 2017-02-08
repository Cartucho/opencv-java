Adding (blending) two images using OpenCV {#tutorial_adding_images}
=========================================

@prev_tutorial{tutorial_mat_operations}
@next_tutorial{tutorial_basic_linear_transform}

Goal
----

In this tutorial you will learn:

@add_toggle{C++}
-   what is *linear blending* and why it is useful;
-   how to add two images using @ref cv::addWeighted()
@end_toggle

@add_toggle{Java}
-   what is *linear blending* and why it is useful;
-   how to add two images using [Core.addWeighted()]
@end_toggle

@add_toggle{Python}
-   what is *linear blending* and why it is useful;
-   how to add two images using **cv2.addWeighted()**
@end_toggle
Theory
------

@note
   The explanation below belongs to the book [Computer Vision: Algorithms and
    Applications](http://szeliski.org/Book/) by Richard Szeliski

From our previous tutorial, we know already a bit of *Pixel operators*. An interesting dyadic
(two-input) operator is the *linear blend operator*:

\f[g(x) = (1 - \alpha)f_{0}(x) + \alpha f_{1}(x)\f]

By varying \f$\alpha\f$ from \f$0 \rightarrow 1\f$ this operator can be used to perform a temporal
*cross-dissolve* between two images or videos, as seen in slide shows and film productions (cool,
eh?)

Code
----

As usual, after the not-so-lengthy explanation, let's go to the code:

@add_toggle{C++}
@include samples/cpp/tutorial_code/core/tutorial_adding_images/adding_images.cpp
@end_toggle

@add_toggle{Java}
@include samples/java/tutorial_code/core/AddingImages.java
@end_toggle

@add_toggle{Python}
@include samples/python/tutorial_code/core/adding_images.py
@end_toggle

Explanation
-----------

Since we are going to perform:
\f[g(x) = (1 - \alpha)f_{0}(x) + \alpha f_{1}(x)\f]
We need two source images (\f$f_{0}(x)\f$ and \f$f_{1}(x)\f$). So, we load them in the usual way:
@warning Since we are *adding* *src1* and *src2*, they both have to be of the same size (width and height) and type.

@add_toggle{C++}
@snippet samples/cpp/tutorial_code/core/tutorial_adding_images/adding_images.cpp load_images

Now we need to generate the `g(x)` image. For this, the function @ref cv::addWeighted() comes quite handy:
@snippet samples/cpp/tutorial_code/core/tutorial_adding_images/adding_images.cpp blend
since @ref cv::addWeighted()  produces:
\f[dst = \alpha \cdot src1 + \beta \cdot src2 + \gamma\f]
In this case, `gamma` is the argument \f$0.0\f$ in the code above.

Create windows, show the images and wait for the user to end the program.

@end_toggle

@add_toggle{Java}
@snippet samples/java/tutorial_code/core/AddingImages.java load_images

Now we need to generate the `g(x)` image. For this, the function [Core.addWeighted()] comes quite handy:
@snippet samples/java/tutorial_code/core/AddingImages.java blend
since [Core.addWeighted()]  produces:
\f[dst = \alpha \cdot src1 + \beta \cdot src2 + \gamma\f]
In this case, `gamma` is the argument \f$0.0\f$ in the code above.

Create windows, show the images and wait for the user to close the window to end the program.

@end_toggle

@add_toggle{Python}
@code{.py}
src1 = cv2.imread('../../images/LinuxLogo.jpg')
src2 = cv2.imread('../../images/WindowsLogo.jpg')
@endcode

Now we need to generate the `g(x)` image. For this, the function **cv2.addWeighted()** comes quite handy:
@code{.py}
beta = 1.0 - alpha                 # Calculate beta = 1 - alpha
gamma = 0.0                        # parameter gamma = 0
dst = cv2.addWeighted(src1,alpha,src2,beta,gamma)  # Get weighted sum of img1 and img2
@endcode
since **cv2.addWeighted()** produces:
\f[dst = \alpha \cdot src1 + \beta \cdot src2 + \gamma\f]
In this case, `gamma` is the argument \f$0.0\f$ in the code above.

Create windows, show the images and wait for the user to end the program.

@end_toggle

Result
------

![](images/Adding_Images_Tutorial_Result_Big.jpg)

<!-- invisible references list -->
[Core.addWeighted()]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Core.html#addWeighted-org.opencv.core.Mat-double-org.opencv.core.Mat-double-double-org.opencv.core.Mat-
