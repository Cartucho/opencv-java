Eroding and Dilating {#tutorial_erosion_dilatation}
====================

@prev_tutorial{tutorial_gausian_median_blur_bilateral_filter}
@next_tutorial{tutorial_opening_closing_hats}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}
-   Apply two very common morphology operators: Dilation and Erosion. For this purpose, you will use
    the following OpenCV functions:
    -   @ref cv::erode
    -   @ref cv::dilate
@end_toggle

@add_toggle{Java}
-   Apply two very common morphology operators: Dilation and Erosion. For this purpose, you will use
    the following OpenCV functions:
    -   [Imgproc.erode()]
    -   [Imgproc.dilate()]
@end_toggle

@add_toggle{Python}
-   Apply two very common morphology operators: Dilation and Erosion. For this purpose, you will use
    the following OpenCV functions:
    -   **cv2.erode()**
    -   **cv2.dilate()**
@end_toggle

Cool Theory
-----------

@note The explanation below belongs to the book **Learning OpenCV** by Bradski and Kaehler.

Morphological Operations
------------------------

-   In short: A set of operations that process images based on shapes. Morphological operations
    apply a *structuring element* to an input image and generate an output image.
-   The most basic morphological operations are two: Erosion and Dilation. They have a wide array of
    uses, i.e. :
    -   Removing noise
    -   Isolation of individual elements and joining disparate elements in an image.
    -   Finding of intensity bumps or holes in an image
-   We will explain dilation and erosion briefly, using the following image as an example:

    ![](images/Morphology_1_Tutorial_Theory_Original_Image.png)

### Dilation

-   This operations consists of convoluting an image \f$A\f$ with some kernel (\f$B\f$), which can have any
    shape or size, usually a square or circle.
-   The kernel \f$B\f$ has a defined *anchor point*, usually being the center of the kernel.
-   As the kernel \f$B\f$ is scanned over the image, we compute the maximal pixel value overlapped by
    \f$B\f$ and replace the image pixel in the anchor point position with that maximal value. As you can
    deduce, this maximizing operation causes bright regions within an image to "grow" (therefore the
    name *dilation*). Take as an example the image above. Applying dilation we can get:

    ![](images/Morphology_1_Tutorial_Theory_Dilation.png)

The background (bright) dilates around the black regions of the letter.

### Erosion

-   This operation is the sister of dilation. What this does is to compute a local minimum over the
    area of the kernel.
-   As the kernel \f$B\f$ is scanned over the image, we compute the minimal pixel value overlapped by
    \f$B\f$ and replace the image pixel under the anchor point with that minimal value.
-   Analagously to the example for dilation, we can apply the erosion operator to the original image
    (shown above). You can see in the result below that the bright areas of the image (the
    background, apparently), get thinner, whereas the dark zones (the "writing") gets bigger.

    ![](images/Morphology_1_Tutorial_Theory_Erosion.png)

Code
----

@add_toggle{C++}

This tutorial code's is shown in the lines below. You can also download it from
[here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/Morphology_1.cpp)
@include samples/cpp/tutorial_code/ImgProc/Morphology_1.cpp

@end_toggle

@add_toggle{Java}

This tutorial code's is shown in the three files composing a JavaFX Application.

Load the native OpenCV library and the primary stage, **Main.java** :
@include samples/java/tutorial_code/ImgProc/ErodingAndDilating/Main.java

If you have _downloaded and installed_ [Scene Builder 2.0] you can now right click
on your _FXML file_ and select _Open with SceneBuilder_. Scene Builder can help
construct your gui by interacting with a graphic interface; this allows you to see
a real time preview of your window and modify your components and their position just by
editing the graphic preview. If you save the file you will notice that some FXML
code has been generated automatically in the _FXML file_, in this case **sample.fxml** :

@code{.xml}

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.image.*?>
<?import javafx.scene.layout.*?>

<GridPane alignment="center" hgap="10" vgap="10" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="sample.Controller">
   <children>
      <VBox prefHeight="200.0" prefWidth="100.0">
         <children>
            <ImageView fx:id="image" fitHeight="442.0" fitWidth="637.0"/>
            <HBox prefHeight="44.0" prefWidth="637.0" spacing="17.0">
               <children>
                  <ChoiceBox fx:id="dilationOrErosion" prefHeight="25.0" prefWidth="100.0">
                     <tooltip>
                        <Tooltip text="Choose Dilation or Erosion Demo" />
                     </tooltip></ChoiceBox>
                  <ChoiceBox fx:id="element" prefHeight="25.0" prefWidth="102.0">
                     <tooltip>
                        <Tooltip text="Choose Element Type" />
                     </tooltip></ChoiceBox>
                  <Label prefHeight="25.0" prefWidth="170.0" text="       Kernel Size (2n+1) :" />
                  <TextField fx:id="kernelSize" prefHeight="25.0" prefWidth="51.0" />
                  <Button fx:id="showImage" mnemonicParsing="false" onAction="#Show" text="Update Image" />
               </children>
               <padding>
                  <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
               </padding>
            </HBox>
         </children></VBox>
   </children>
   <columnConstraints>
      <ColumnConstraints />
   </columnConstraints>
   <rowConstraints>
      <RowConstraints />
      <RowConstraints />
   </rowConstraints>
</GridPane>

@endcode

For example we can change text of our button in “Update Image” in the _Text_ field under the _Properties_ menu and the id of the button (e.g. “showImage”) in the _fx:id_ field under the _Code_ menu. We could also do it by applying changes directly in the _line 25_ of the xml code.
On line 25 we can also see _onAction="#Show"_ which defines the method to be called in the **Controller.java** class (_public void Show(ActionEvent actionEvent)_).

**Controller.java**, the class that will mange the GUI:
@include samples/java/tutorial_code/ImgProc/ErodingAndDilating/Controller.java

For our application we need to do basically two things: set and control the choice box and the button push and the refreshment of the image view. To do so we have to create a reference between the gui components and a variable used in our controller class using the _fx:id_
from line 12, 15, 19 and 23 from **sample.fxml**:

@snippet samples/java/tutorial_code/ImgProc/ErodingAndDilating/Controller.java fxml_variables

The \@FXML tag means that we are linking our variable to an element of the fxml file and the value used to declare the variable has to equal to the _fx:id_ set for that specific element.

@end_toggle

@add_toggle{Python}

This tutorial code's is shown in the lines below.
@include samples/python/tutorial_code/ImgProc/Morphology_1.py

@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Most of the stuff shown is known by you (if you have any doubt, please refer to the tutorials in
    previous sections). Let's check the general structure of the program:

    -   Load an image (can be BGR or grayscale)
    -   Create two windows (one for dilation output, the other for erosion)
    -   Create a set of 02 Trackbars for each operation:
        -   The first trackbar "Element" returns either **erosion_elem** or **dilation_elem**
        -   The second trackbar "Kernel size" return **erosion_size** or **dilation_size** for the
            corresponding operation.
    -   Every time we move any slider, the user's function **Erosion** or **Dilation** will be
        called and it will update the output image based on the current trackbar values.

    Let's analyze these two functions:

-#  **erosion:**
    @snippet samples/cpp/tutorial_code/ImgProc/Morphology_1.cpp erosion
    -   The function that performs the *erosion* operation is @ref cv::erode . As we can see, it
        receives three arguments:
        -   *src*: The source image
        -   *erosion_dst*: The output image
        -   *element*: This is the kernel we will use to perform the operation. If we do not
            specify, the default is a simple `3x3` matrix. Otherwise, we can specify its
            shape. For this, we need to use the function cv::getStructuringElement :
            @snippet samples/cpp/tutorial_code/ImgProc/Morphology_1.cpp element
            We can choose any of three shapes for our kernel:

            -   Rectangular box: MORPH_RECT
            -   Cross: MORPH_CROSS
            -   Ellipse: MORPH_ELLIPSE

            Then, we just have to specify the size of our kernel and the *anchor point*. If not
            specified, it is assumed to be in the center.

    -   That is all. We are ready to perform the erosion of our image.
@note Additionally, there is another parameter that allows you to perform multiple erosions
(iterations) at once. We are not using it in this simple tutorial, though. You can check out the
Reference for more details.

-#  **dilation:**

    The code is below. As you can see, it is completely similar to the snippet of code for **erosion**.
    Here we also have the option of defining our kernel, its anchor point and the size of the operator
    to be used.
    @snippet samples/cpp/tutorial_code/ImgProc/Morphology_1.cpp dilation


@end_toggle

@add_toggle{Java}

In this part we will explain in further detail the class **Controller.java** :
-#  The first method to be called is _initialize()_, where we:

    -   Set the list items (_dilationOrErosionList_) to the _ChoiceBox dilationOrErosion_
and initialize it with the value _Erosion_.
    -   Set the list items (_elementList_) to the _ChoiceBox element_
and initialize it with the value _Rect_.
    -   Set the _TextField kernelSize_ to be initialized with the value 0.
    -   Load an image (can be BGR or grayscale) and set it to the _ImageView image_.

    Based on the selected elements, when the button _Update Image_ is pressed, the method _Show()_
    will result in one of the two following scenarios:

-#  **erosion:**
    @snippet samples/java/tutorial_code/ImgProc/ErodingAndDilating/Controller.java erosion
    -   The function that performs the *erosion* operation is [Imgproc.erode()] . As we can see, it
        receives three arguments:
        -   *src*: The source image
        -   *dst*: The output image
        -   *element*: This is the kernel we will use to perform the operation. If we do not
            specify, the default is a simple `3x3` matrix. Otherwise, we can specify its
            shape. For this, we need to use the [Imgproc.getStructuringElement()] function:
            @snippet samples/java/tutorial_code/ImgProc/ErodingAndDilating/Controller.java element
            We can choose any of three shapes for our kernel:

            -   Rectangular box: Imgproc.MORPH_RECT
            -   Cross: Imgproc.MORPH_CROSS
            -   Ellipse: Imgproc.MORPH_ELLIPSE

            Then, we just have to specify the size of our kernel and the *anchor point*. If not
            specified, it is assumed to be in the center.

    -   That is all. We are ready to perform the erosion of our image.
@note Additionally, there is another parameter that allows you to perform multiple erosions
(iterations) at once. We are not using it in this simple tutorial, though. You can check out the
Reference for more details.

-#  **dilation:**

    The code is below. As you can see, it is completely similar to the snippet of code for **erosion**.
    Here we also have the option of defining our kernel, its anchor point and the size of the operator
    to be used.
    @snippet samples/java/tutorial_code/ImgProc/ErodingAndDilating/Controller.java dilation

@end_toggle

@add_toggle{Python}

-#  Most of the stuff shown is known by you (if you have any doubt, please refer to the tutorials in
    previous sections). Let's check the general structure of the program:

    -   Load an image (can be BGR or grayscale)
    -   Create two windows (one for dilation output, the other for erosion)
    -   Create a set of 02 Trackbars for each operation:
        -   The first trackbar "Element" returns either **erosion_elem** or **dilation_elem**
        -   The second trackbar "Kernel size" return **erosion_size** or **dilation_size** for the
            corresponding operation.
    -   Every time we move any slider, the user's function **Erosion** or **Dilation** will be
        called and it will update the output image based on the current trackbar values.

    Let's analyze these two functions:

-#  **erosion:**
    @snippet samples/python/tutorial_code/ImgProc/Morphology_1.py erode
    -   The function that performs the *erosion* operation is **cv2.erode()** . As we can see, it
        receives three arguments:
        -   *img*: The source image
        -   *eroded*: The output image
        -   *kernel*: This is the kernel we will use to perform the operation. We can specify its
            shape. For this, we need to use the function **cv2.getStructuringElement()** :
            @code{.py}
            kernel = cv2.getStructuringElement(erosion_type, (2 * erosion_size + 1, 2 * erosion_size + 1))
            @endcode
            We can choose any of three shapes for our kernel:

            -   Rectangular box: cv2.MORPH_RECT
            -   Cross: cv2.MORPH_CROSS
            -   Ellipse: cv2.MORPH_ELLIPSE

            Then, we just have to specify the size of our kernel and the *anchor point*. If not
            specified, it is assumed to be in the center.

    -   That is all. We are ready to perform the erosion of our image.
@note Additionally, there is another parameter that allows you to perform multiple erosions
(iterations) at once. We are not using it in this simple tutorial, though. You can check out the
Reference for more details.

-#  **dilation:**

    The code is below. As you can see, it is completely similar to the snippet of code for **erosion**.
    Here we also have the option of defining our kernel, its anchor point and the size of the operator
    to be used.
    @snippet samples/python/tutorial_code/ImgProc/Morphology_1.py dilate

@end_toggle

Results
-------

For instance, using this image:

![](images/Morphology_1_Tutorial_Original_Image.jpg)

@add_toggle{C++}

Compiling the code above and executing it with that image as argument we get the results below.
Varying the indices in the Trackbars give different output images, naturally.
Try them out! You can even try to add a third Trackbar to control the number of iterations.

![](images/Morphology_1_Result.jpg)

@end_toggle

@add_toggle{Java}
We get the results below. Varying the selection in the ChoiceBox's give different output images,
naturally. Try them out!

![](images/Morphology_1_Tutorial_Erosion_Cross.png)
@end_toggle

@add_toggle{Python}

Varying the indices in the Trackbars give different output images, naturally.
Try them out! You can even try to add a third Trackbar to control the number of iterations.

![](images/Morphology_1_Result.jpg)

@end_toggle

<!-- invisible references list -->
[Scene Builder 2.0]: http://www.oracle.com/technetwork/java/javafxscenebuilder-1x-archive-2199384.html
[Imgproc.erode()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#erode-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Mat-
[Imgproc.dilate()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#dilate-org.opencv.core.Mat-org.opencv.core.Mat-org.opencv.core.Mat-
[Imgproc.getStructuringElement()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#getStructuringElement-int-org.opencv.core.Size-
