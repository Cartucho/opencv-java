More Morphology Transformations {#tutorial_opening_closing_hats}
===============================

@prev_tutorial{tutorial_erosion_dilatation}
@next_tutorial{tutorial_morph_lines_detection}

Goal
----

In this tutorial you will learn how to:

@add_toggle{C++}
-   Use the OpenCV function @ref cv::morphologyEx to apply Morphological Transformation such as:
    -   Opening
    -   Closing
    -   Morphological Gradient
    -   Top Hat
    -   Black Hat
@end_toggle

@add_toggle{Java}
-   Use the OpenCV function [Imgproc.morphologyEx()] to apply Morphological Transformation such as:
    -   Opening
    -   Closing
    -   Morphological Gradient
    -   Top Hat
    -   Black Hat

@end_toggle

@add_toggle{Python}
-   Use the OpenCV function **cv2.morphologyEx()** to apply Morphological Transformation such as:
    -   Opening
    -   Closing
    -   Morphological Gradient
    -   Top Hat
    -   Black Hat
@end_toggle

Theory
------

@note The explanation below belongs to the book **Learning OpenCV** by Bradski and Kaehler.

In the previous tutorial we covered two basic Morphology operations:

-   Erosion
-   Dilation.

Based on these two we can effectuate more sophisticated transformations to our images. Here we
discuss briefly 05 operations offered by OpenCV:

### Opening

-   It is obtained by the erosion of an image followed by a dilation.

    \f[dst = open( src, element) = dilate( erode( src, element ) )\f]

-   Useful for removing small objects (it is assumed that the objects are bright on a dark
    foreground)
-   For instance, check out the example below. The image at the left is the original and the image
    at the right is the result after applying the opening transformation. We can observe that the
    small spaces in the corners of the letter tend to dissapear.

    ![](images/Morphology_2_Tutorial_Theory_Opening.png)

### Closing

-   It is obtained by the dilation of an image followed by an erosion.

    \f[dst = close( src, element ) = erode( dilate( src, element ) )\f]

-   Useful to remove small holes (dark regions).

    ![](images/Morphology_2_Tutorial_Theory_Closing.png)

### Morphological Gradient

-   It is the difference between the dilation and the erosion of an image.

    \f[dst = morph_{grad}( src, element ) = dilate( src, element ) - erode( src, element )\f]

-   It is useful for finding the outline of an object as can be seen below:

    ![](images/Morphology_2_Tutorial_Theory_Gradient.png)

### Top Hat

-   It is the difference between an input image and its opening.

    \f[dst = tophat( src, element ) = src - open( src, element )\f]

    ![](images/Morphology_2_Tutorial_Theory_TopHat.png)

### Black Hat

-   It is the difference between the closing and its input image

    \f[dst = blackhat( src, element ) = close( src, element ) - src\f]

    ![](images/Morphology_2_Tutorial_Theory_BlackHat.png)

Code
----

@add_toggle{C++}

This tutorial code's is shown lines below. You can also download it from
[here](https://github.com/Itseez/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/Morphology_2.cpp)
@include samples/cpp/tutorial_code/ImgProc/Morphology_2.cpp

@end_toggle

@add_toggle{Java}

This tutorial code's is shown in the three files composing a JavaFX Application.

**Main.java**
@include samples/java/tutorial_code/ImgProc/OpeningClosingHats/Main.java

**sample.fxml**
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
                  <ChoiceBox fx:id="morphOperator" prefHeight="25.0" prefWidth="100.0">
                     <tooltip>
                        <Tooltip text="Choose Morphological Operator" />
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
**Controller.java**
@include samples/java/tutorial_code/ImgProc/OpeningClosingHats/Controller.java

@end_toggle

@add_toggle{Python}

This tutorial code's is shown in the lines below.
@include samples/python/tutorial_code/ImgProc/Morphology_2.py

@end_toggle

Explanation
-----------

@add_toggle{C++}

-#  Let's check the general structure of the program:
    -   Load an image
    -   Create a window to display results of the Morphological operations
    -   Create 03 Trackbars for the user to enter parameters:
        -   The first trackbar **Operator** returns the kind of morphology operation to use
            (**morph_operator**).
            @code{.cpp}
            createTrackbar("Operator:\n 0: Opening - 1: Closing \n 2: Gradient - 3: Top Hat \n 4: Black Hat",
                           window_name, &morph_operator, max_operator,
                           Morphology_Operations );
            @endcode
        -   The second trackbar **Element** returns **morph_elem**, which indicates what kind of
            structure our kernel is:
            @code{.cpp}
            createTrackbar( "Element:\n 0: Rect - 1: Cross - 2: Ellipse", window_name,
                    &morph_elem, max_elem,
                    Morphology_Operations );
            @endcode
        -   The final trackbar **Kernel Size** returns the size of the kernel to be used
            (**morph_size**)
            @code{.cpp}
            createTrackbar( "Kernel size:\n 2n +1", window_name,
                    &morph_size, max_kernel_size,
                    Morphology_Operations );
            @endcode
    -   Every time we move any slider, the user's function **Morphology_Operations** will be called
        to effectuate a new morphology operation and it will update the output image based on the
        current trackbar values.
        @code{.cpp}
        /*
         * @function Morphology_Operations
         */
        void Morphology_Operations( int, void* )
        {
            // Since MORPH_X : 2,3,4,5 and 6
            int operation = morph_operator + 2;

            Mat element = getStructuringElement( morph_elem, Size( 2*morph_size + 1, 2*morph_size+1 ), Point( morph_size, morph_size ) );

            /// Apply the specified morphology operation
            morphologyEx( src, dst, operation, element );
            imshow( window_name, dst );
        }
        @endcode

        We can observe that the key function to perform the morphology transformations is @ref
        cv::morphologyEx . In this example we use four arguments (leaving the rest as defaults):

        -   **src** : Source (input) image
        -   **dst**: Output image
        -   **operation**: The kind of morphology transformation to be performed. Note that we have
            5 alternatives:

            -   *Opening*: MORPH_OPEN : 2
            -   *Closing*: MORPH_CLOSE: 3
            -   *Gradient*: MORPH_GRADIENT: 4
            -   *Top Hat*: MORPH_TOPHAT: 5
            -   *Black Hat*: MORPH_BLACKHAT: 6

            As you can see the values range from \<2-6\>, that is why we add (+2) to the values
            entered by the Trackbar:
            @code{.cpp}
            int operation = morph_operator + 2;
            @endcode
        -   **element**: The kernel to be used. We use the function @ref cv::getStructuringElement
            to define our own structure.

@end_toggle

@add_toggle{Java}

Since this JavaFX Application has the same structure and logic as the previous tutorial,
we advise you to complete that tutorial first.
In this part we will explain in further detail the class **Controller.java** :

-#  The first method to be called is _initialize()_, where we:

    -   Set the list items (_morphOperatorList_) to the _ChoiceBox morphOperator_
and initialize it with the value _Opening_.
    -   Set the list items (_elementList_) to the _ChoiceBox element_
and initialize it with the value _Rect_.
    -   Set the _TextField kernelSize_ to be initialized with the value 0.
    -   Load an image (can be BGR or grayscale) and set it to the _ImageView image_.

-#  Based on the selected elements, when the button _Update Image_ is pressed, the method _Show()_ is called:
        @code{.java}
        int morph_operator = morphOperator.getSelectionModel().getSelectedIndex();
        int morph_elem = element.getSelectionModel().getSelectedIndex();
        int morph_size = Integer.parseInt(kernelSize.getText());

        // Since MORPH_X : 2,3,4,5 and 6
        int operation = morph_operator + 2;

        Mat element = Imgproc.getStructuringElement( morph_elem,
                new Size( 2*morph_size + 1, 2*morph_size+1 ), new Point( morph_size, morph_size ) );

        Imgproc.morphologyEx( src, dst, operation, element );
        image.setImage(mat2Image(dst));
        @endcode

        After getting the selected values from the Choice Box we can observe that the key function
        to perform the morphology transformations is [Imgproc.morphologyEx()] . In this example we
        use four arguments (leaving the rest as defaults):

        -   **src** : Source (input) image
        -   **dst**: Output image
        -   **operation**: The kind of morphology transformation to be performed. Note that we have
            5 alternatives:

            -   *Opening*: Imgproc.MORPH_OPEN : 2
            -   *Closing*: Imgproc.MORPH_CLOSE: 3
            -   *Gradient*: Imgproc.MORPH_GRADIENT: 4
            -   *Top Hat*: Imgproc.MORPH_TOPHAT: 5
            -   *Black Hat*: Imgproc.MORPH_BLACKHAT: 6

            As you can see the values range from \<2-6\>, that is why we add (+2) to the values:
            @code{.cpp}
            int operation = morph_operator + 2;
            @endcode
        -   **element**: The kernel to be used. We use the function [Imgproc.getStructuringElement()]
            to define our own structure.

@end_toggle

@add_toggle{Python}

-#  Let's check the general structure of the program:
    -   Load an image
    -   Create a window to display results of the Morphological operations
    -   Create 03 Trackbars for the user to enter parameters:
        -   The first trackbar **Operator** returns the kind of morphology operation to use
            (**morph_operator**).
            @code{.py}
            cv2.createTrackbar('Operator:\n 0: Opening - 1: Closing \n 2: Gradient - 3: Top Hat \n 4: Black Hat',
                   window_name, morph_elem, max_operator, update_operator)
            @endcode
        -   The second trackbar **Element** returns **morph_elem**, which indicates what kind of
            structure our kernel is:
            @code{.py}
            cv2.createTrackbar('Element: \n 0: Rect \n 1: Cross \n 2: Ellipse', window_name,
                   morph_elem, max_elem, update_elem)
            @endcode
        -   The final trackbar **Kernel Size** returns the size of the kernel to be used
            (**morph_size**)
            @code{.py}
            cv2.createTrackbar('Size: 2n+1', window_name, morph_size, max_kernel_size, update_size)
            @endcode
    -   Every time we move any slider, the user's function **Morphology_Operations** will be called
        to effectuate a new morphology operation and it will update the output image based on the
        current trackbar values.
        @code{.py}
        def morphology_operations():
            # Since MORPH_X : 2,3,4,5 and 6
            operation = morph_operator + 2

            kernel = cv2.getStructuringElement(morph_elem, (2 * morph_size + 1, 2 * morph_size + 1))
            dst = cv2.morphologyEx(img, operation, kernel)
            cv2.imshow(window_name, dst)
        @endcode

        We can observe that the key function to perform the morphology transformations is
        **cv2.morphologyEx()** . In this example we use four arguments (leaving the rest as defaults):

        -   **img** : Source (input) image
        -   **dst**: Output image
        -   **operation**: The kind of morphology transformation to be performed. Note that we have
            5 alternatives:

            -   *Opening*: cv2.MORPH_OPEN : 2
            -   *Closing*: cv2.MORPH_CLOSE: 3
            -   *Gradient*: cv2.MORPH_GRADIENT: 4
            -   *Top Hat*: cv2.MORPH_TOPHAT: 5
            -   *Black Hat*: cv2.MORPH_BLACKHAT: 6

            As you can see the values range from \<2-6\>, that is why we add (+2) to the values
            entered by the Trackbar:
            @code{.py}
            operation = morph_operator + 2;
            @endcode
        -   **kernel**: The kernel to be used. We use the function **cv2.getStructuringElement()**
            to define our own structure.

@end_toggle

Results
-------

@add_toggle{C++}

-   After compiling the code above we can execute it giving an image path as an argument. For this
    tutorial we use as input the image: **baboon.png**:

    ![](images/Morphology_2_Tutorial_Original_Image.jpg)

-   And here are two snapshots of the display window. The first picture shows the output after using
    the operator **Opening** with a cross kernel. The second picture (right side, shows the result
    of using a **Blackhat** operator with an ellipse kernel.

    ![](images/Morphology_2_Tutorial_Result.jpg)

@end_toggle

@add_toggle{Java}

For this tutorial we use as input the image: **baboon.png**:

![](images/Morphology_2_Tutorial_Original_Image.jpg)

We get the results below. Varying the selection in the ChoiceBox's give different output images,
naturally. Try them out!

![](images/Morphology_2_Tutorial_Black_Hat.png)

@end_toggle

@add_toggle{Python}

-   For this tutorial we use as input the image: **baboon.png**:

    ![](images/Morphology_2_Tutorial_Original_Image.jpg)

-   And here are two snapshots of the display window. The first picture shows the output after using
    the operator **Opening** with a cross kernel. The second picture (right side, shows the result
    of using a **Blackhat** operator with an ellipse kernel.

    ![](images/Morphology_2_Tutorial_Result.jpg)

@end_toggle

<!-- invisible references list -->
[Imgproc.morphologyEx()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#morphologyEx-org.opencv.core.Mat-org.opencv.core.Mat-int-org.opencv.core.Mat-
[Imgproc.getStructuringElement()]: http://docs.opencv.org/java/3.1.0/org/opencv/imgproc/Imgproc.html#getStructuringElement-int-org.opencv.core.Size-
