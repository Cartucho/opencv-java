Basic Drawing {#tutorial_basic_geometric_drawing}
=============

@prev_tutorial{tutorial_basic_linear_transform}
@next_tutorial{tutorial_random_generator_and_text}

Goals
-----

In this tutorial you will learn how to:

@add_toggle{C++}
-   Use @ref cv::Point to define 2D points in an image.
-   Use @ref cv::Scalar and why it is useful
-   Draw a **line** by using the OpenCV function @ref cv::line
-   Draw an **ellipse** by using the OpenCV function @ref cv::ellipse
-   Draw a **rectangle** by using the OpenCV function @ref cv::rectangle
-   Draw a **circle** by using the OpenCV function @ref cv::circle
-   Draw a **filled polygon** by using the OpenCV function @ref cv::fillPoly
@end_toggle

@add_toggle{Java}
-   Use [Point] to define 2D points in an image.
-   Use [Scalar] and why it is useful
-   Draw a **line** by using the OpenCV function [Imgproc.line]
-   Draw an **ellipse** by using the OpenCV function [Imgproc.ellipse]
-   Draw a **rectangle** by using the OpenCV function [Imgproc.rectangle]
-   Draw a **circle** by using the OpenCV function [Imgproc.circle]
-   Draw a **filled polygon** by using the OpenCV function [Imgproc.fillPoly]
@end_toggle

@add_toggle{Python}
-   Draw a **line** by using the OpenCV function **cv2.line()**
-   Draw an **ellipse** by using the OpenCV function **cv2.ellipse()**
-   Draw a **rectangle** by using the OpenCV function **cv2.rectangle()**
-   Draw a **circle** by using the OpenCV function **cv2.circle()**
-   Draw a **filled polygon** by using the OpenCV function **cv2.fillPoly()**
@end_toggle

Theory
------

@add_toggle{C++}

For this tutorial, we will heavily use two structures:
@ref cv::Point and @ref cv::Scalar
### Point

It represents a 2D point, specified by its image coordinates \f$x\f$ and \f$y\f$. We can define it as:
@code{.cpp}
Point pt;
pt.x = 10;
pt.y = 8;
@endcode
or
@code{.cpp}
Point pt = Point(10, 8);
@endcode

### Scalar

-   Represents a 4-element vector. The type Scalar is widely used in OpenCV for passing pixel
    values.
-   In this tutorial, we will use it extensively to represent BGR color values (3 parameters). It is
    not necessary to define the last argument if it is not going to be used.
-   Let's see an example, if we are asked for a color argument and we give:
    @code{.cpp}
    Scalar( a, b, c )
    @endcode
    We would be defining a BGR color such as: *Blue = a*, *Green = b* and *Red = c*
@end_toggle

@add_toggle{Java}

For this tutorial, we will heavily use two structures:
[Point] and [Scalar]
### Point

It represents a 2D point, specified by its image coordinates \f$x\f$ and \f$y\f$. We can define it as:
@code{.java}
Point pt = new Point();
pt.x = 10;
pt.y = 8;
@endcode
or
@code{.java}
Point pt = new Point(10, 8);
@endcode

### Scalar

-   Represents a 4-element vector. The type Scalar is widely used in OpenCV for passing pixel
    values.
-   In this tutorial, we will use it extensively to represent BGR color values (3 parameters). It is
    not necessary to define the last argument if it is not going to be used.
-   Let's see an example, if we are asked for a color argument and we give:
    @code{.java}
    Scalar( a, b, c )
    @endcode
    We would be defining a BGR color such as: *Blue = a*, *Green = b* and *Red = c*

@end_toggle

Code
----

@add_toggle{C++}
-   This code is in your OpenCV sample folder. Otherwise you can grab it from
    [here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp)

@end_toggle

@add_toggle{Java}
-   This code is in the
`samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java` of the OpenCV source code
library.
@end_toggle

@add_toggle{Python}
-   This code is in the
`samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py` of the OpenCV source code
library.
@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Since we plan to draw two examples (an atom and a rook), we have to create two images and two
    windows to display them.
    @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp create_windows_images

-#  We created functions to draw different geometric shapes. For instance, to draw the atom we used
    *MyEllipse* and *MyFilledCircle*:
    @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp draw_atom

-#  And to draw the rook we employed *MyLine*, *rectangle* and a *MyPolygon*:
    @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp draw_rook

-#  Let's check what is inside each of these functions:
    -   *MyLine*
        @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp my_line
        As we can see, *MyLine* just call the function @ref cv::line , which does the following:

        -   Draw a line from Point **start** to Point **end**
        -   The line is displayed in the image **img**
        -   The line color is defined by **Scalar( 0, 0, 0)** which is the RGB value correspondent
            to **Black**
        -   The line thickness is set to **thickness** (in this case 2)
        -   The line is a 8-connected one (**lineType** = 8)
    -   *MyEllipse*
        @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp my_ellipse
        From the code above, we can observe that the function @ref cv::ellipse draws an ellipse such
        that:

        -   The ellipse is displayed in the image **img**
        -   The ellipse center is located in the point (w/2.0, w/2.0) and is enclosed in a box
            of size (w/4.0, w/16.0)
        -   The ellipse is rotated **angle** degrees
        -   The ellipse extends an arc between **0** and **360** degrees
        -   The color of the figure will be **Scalar( 255, 0, 0)** which means blue in RGB value.
        -   The ellipse's **thickness** is 2.
    -   *MyFilledCircle*
        @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp my_filled_circle
        Similar to the ellipse function, we can observe that *circle* receives as arguments:

        -   The image where the circle will be displayed (**img**)
        -   The center of the circle denoted as the Point **center**
        -   The radius of the circle: **w/32.0**
        -   The color of the circle: **Scalar(0, 0, 255)** which means *Red* in BGR
        -   Since **thickness** = -1, the circle will be drawn filled.
    -   *MyPolygon*
        @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp my_polygon

        To draw a filled polygon we use the function @ref cv::fillPoly . We note that:

        -   The polygon will be drawn on **img**
        -   The vertices of the polygon are the set of points in **ppt**
        -   The total number of vertices to be drawn are **npt**
        -   The number of polygons to be drawn is only **1**
        -   The color of the polygon is defined by **Scalar( 255, 255, 255)**, which is the BGR
            value for *white*
    -   *rectangle*
        @snippet samples/cpp/tutorial_code/core/Matrix/Drawing_1.cpp rectangle
        Finally we have the @ref cv::rectangle function (we did not create a special function for
        this guy). We note that:

        -   The rectangle will be drawn on **rook_image**
        -   Two opposite vertices of the rectangle are defined by Point( 0, 7*w/8.0 )
            and Point( w, w)
        -   The color of the rectangle is given by **Scalar(0, 255, 255)** which is the BGR value
            for *yellow*
        -   Since the thickness value is given by -1, the rectangle will be filled.

@end_toggle

@add_toggle{Java}

-#  Since we plan to draw two examples (an atom and a rook), we have to create two images and two
    windows to display them.
    @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java create_windows_images

-#  We created functions to draw different geometric shapes. For instance, to draw the atom we used
    *MyEllipse* and *MyFilledCircle*:
    @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java draw_atom

-#  And to draw the rook we employed *MyLine*, *rectangle* and a *MyPolygon*:
    @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java draw_rook

-#  Let's check what is inside each of these functions:
    -   *MyLine*
        @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java my_line
        As we can see, *MyLine* just call the function [Imgproc.line] , which does the following:

        -   Draw a line from Point **start** to Point **end**
        -   The line is displayed in the image **img**
        -   The line color is defined by **Scalar( 0, 0, 0)** which is the RGB value correspondent
            to **Black**
        -   The line thickness is set to **thickness** (in this case 2)
        -   The line is a 8-connected one (**lineType** = 8)
        -   The number of fractional bits in the coordinates of the center and in the radius value is 0 (**shift** = 0)

    -   *MyEllipse*
        @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java my_ellipse
        From the code above, we can observe that the function [Imgproc.ellipse] draws an ellipse such
        that:

        -   The ellipse is displayed in the image **img**
        -   The ellipse center is located in the point (W/2.0, W/2.0) and is enclosed in a box
            of size (W/4.0, W/16.0)
        -   The ellipse is rotated **angle** degrees
        -   The ellipse extends an arc between **0** and **360** degrees
        -   The color of the figure will be **Scalar( 255, 0, 0)** which means blue in RGB value.
        -   The ellipse's **thickness** is 2.
        -   The number of fractional bits in the coordinates of the center and in the radius value is 0 (**shift** = 0)
    -   *MyFilledCircle*
        @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java my_filled_circle
        Similar to the ellipse function, we can observe that [Imgproc.circle] receives as arguments:

        -   The image where the circle will be displayed (**img**)
        -   The center of the circle denoted as the Point **center**
        -   The radius of the circle: **W/32.0**
        -   The color of the circle: **Scalar(0, 0, 255)** which means *Red* in BGR
        -   Since **thickness** = -1, the circle will be drawn filled.
    -   *MyPolygon*
        @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java my_polygon
        To draw a filled polygon we use the function [Imgproc.fillPoly] . We note that:

        -   The polygon will be drawn on **img**
        -   The vertices of the polygon are the set of points in **rook_points**
        -   The number of polygons to be drawn is only **1**
        -   The color of the polygon is defined by **Scalar( 255, 255, 255)**, which is the BGR
            value for *white*
    -   *rectangle*
        @snippet samples/java/tutorial_code/core/Matrix/BasicGeometricDrawing.java rectangle
        Finally we have the @ref cv::rectangle function (we did not create a special function for
        this guy). We note that:

        -   The rectangle will be drawn on **rook_image**
        -   Two opposite vertices of the rectangle are defined by Point( 0, 7*W/8.0 )
            and Point( W, W)
        -   The color of the rectangle is given by **Scalar(0, 255, 255)** which is the BGR value
            for *yellow*
        -   Since the thickness value is given by -1, the rectangle will be filled.
@end_toggle

@add_toggle{Python}

-#  Since we plan to draw two examples (an atom and a rook), we have to create two images and two
    windows to display them.
    @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py create_windows_images

-#  We created functions to draw different geometric shapes. For instance, to draw the atom we used
    *my_ellipse* and *my_filled_circle*:
    @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py draw_atom

-#  And to draw the rook we employed *my_line*, *rectangle* and a *my_polygon*:
    @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py draw_rook

-#  Let's check what is inside each of these functions:
    -   *my_line*
        @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py my_line
        As we can see, *my_line* just call the function **cv2.line()** , which does the following:

        -   Draw a line from **start** to **end**
        -   The line is displayed in the image **img**
        -   The line color is defined by ( 0, 0, 0) which is the RGB value correspondent
            to **Black**
        -   The line thickness is set to **thickness** (in this case 2)
        -   The line is a 8-connected one (**line_type** = 8)
    -   *my_ellipse*
        @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py my_ellipse
        From the code above, we can observe that the function **cv2.ellipse()** draws an ellipse such
        that:

        -   The ellipse is displayed in the image **img**
        -   The ellipse center is located in the (W/2, W/2) and is enclosed in a box
            of size (W/4, W/16)
        -   The ellipse is rotated **angle** degrees
        -   The ellipse extends an arc between **0** and **360** degrees
        -   The color of the figure will be (255, 0, 0) which means **Blue** in RGB value.
        -   The ellipse's **thickness** is 2.
    -   *my_filled_circle*
        @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py my_filled_circle
        Similar to the ellipse function, we can observe that **cv2.circle()** receives as arguments:

        -   The image where the circle will be displayed (**img**)
        -   The center of the circle denoted as the **center**
        -   The radius of the circle: **W/32**
        -   The color of the circle: (0, 0, 255) which means **Red** in BGR
        -   Since **thickness** = -1, the circle will be drawn filled.
    -   *my_polygon*
        @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py my_polygon
        To draw a filled polygon we use the function **cv2.fillPoly()** . We note that:

        -   The polygon will be drawn on **img**
        -   The vertices of the polygon are the set of points in **pts**
        -   The color of the polygon is defined by **Scalar( 255, 255, 255)**, which is the BGR
            value for *White*
    -   *rectangle*
        @snippet samples/python/tutorial_code/core/Matrix/basic_geometric_drawing.py rectangle
        Finally we have the **cv.rectangle()** function (we did not create a special function for
        this guy). We note that:

        -   The rectangle will be drawn on **rook_image**
        -   Two opposite vertices of the rectangle are defined by (0, 7*W/8.0)
            and (W, W)
        -   The color of the rectangle is given by (0, 255, 255) which is the BGR value
            for *Yellow*
        -   Since the thickness value is given by -1, the rectangle will be filled.

@end_toggle

Result
------

Compiling and running your program should give you a result like this:

![](images/Drawing_1_Tutorial_Result_0.png)


<!-- invisible references list -->
[Point]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Point.html
[Scalar]: http://docs.opencv.org/java/3.1.0/org/opencv/core/Scalar.html
[Imgproc.line]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#line-org.opencv.core.Mat-org.opencv.core.Point-org.opencv.core.Point-org.opencv.core.Scalar-
[Imgproc.ellipse]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#ellipse-org.opencv.core.Mat-org.opencv.core.Point-org.opencv.core.Size-double-double-double-org.opencv.core.Scalar-
[Imgproc.rectangle]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#rectangle-org.opencv.core.Mat-org.opencv.core.Point-org.opencv.core.Point-org.opencv.core.Scalar-
[Imgproc.circle]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#circle-org.opencv.core.Mat-org.opencv.core.Point-int-org.opencv.core.Scalar-
[Imgproc.fillPoly]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#fillPoly-org.opencv.core.Mat-java.util.List-org.opencv.core.Scalar-
