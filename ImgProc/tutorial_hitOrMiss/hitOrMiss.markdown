Hit-or-Miss {#tutorial_hitOrMiss}
=================================

@prev_tutorial{tutorial_opening_closing_hats}
@next_tutorial{tutorial_moprh_lines_detection}

Goal
----

In this tutorial you will learn how to find a given configuration or pattern in a binary image by using the Hit-or-Miss transform (also known as Hit-and-Miss transform).
This transform is also the basis of more advanced morphological operations such as thinning or pruning.

@add_toggle_cpp

We will use the OpenCV function @ref cv::morphologyEx.

@end_toggle

@add_toggle_java

We will use the OpenCV function **Imgproc.morphologyEx**.

@end_toggle

@add_toggle_python

We will use the OpenCV function **cv2.morphologyEx()**.

@end_toggle


Hit-or-Miss theory
-------------------

Morphological operators process images based on their shape. These operators apply one or more *structuring elements* to an input image to obtain the output image.
The two basic morphological operations are the *erosion* and the *dilation*. The combination of these two operations generate advanced morphological transformations such as *opening*, *closing*, or *top-hat* transform.
To know more about these and other basic morphological operations refer to the tutorials @ref tutorial_erosion_dilatation "here" and @ref tutorial_opening_closing_hats "here".

The Hit-or-Miss transformation is useful to find patterns in binary images. In particular, it finds those pixels whose neighbourhood matches the shape of a first structuring element \f$B_1\f$
while not matching the shape of a second structuring element \f$B_2\f$ at the same time. Mathematically, the operation applied to an image \f$A\f$ can be expressed as follows:
\f[
    A\circledast B = (A\ominus B_1) \cap (A^c\ominus B_2)
\f]

Therefore, the hit-or-miss operation comprises three steps:
    1. Erode image \f$A\f$ with structuring element \f$B_1\f$.
    2. Erode the complement of image \f$A\f$ (\f$A^c\f$) with structuring element \f$B_2\f$.
    3. AND results from step 1 and step 2.

The structuring elements \f$B_1\f$ and \f$B_2\f$ can be combined into a single element \f$B\f$, which can contain 1, 0, or -1. The 1-valued elements make up the domain of B1 and the -1-valued elements make up the domain of B2, and the 0-valued elements are ignored.

 Let's see an example:
![Structuring elements (kernels). Left: kernel to 'hit'. Middle: kernel to 'miss'. Right: final combined kernel, where 1=foreground, -1=background, 0=don't care.](images/hitmiss_kernels.png)

In this case, we are looking for a pattern in which the central pixel belongs to the background while the north, south, east, and west pixels belong to the foreground. The rest of pixels in the neighbourhood can be of any kind, we don't care about them. Now, let's apply this kernel to an input image:

![Input binary image](images/hitmiss_input.png)
![Output binary image](images/hitmiss_output.png)

You can see that the pattern is found in just one location within the image.


Code
----

The code corresponding to the previous example is shown below. You can also download it from
[here](https://github.com/opencv/opencv/tree/master/samples/cpp/tutorial_code/ImgProc/HitMiss.cpp)
@include samples/cpp/tutorial_code/ImgProc/HitMiss.cpp

As you can see, it is as simple as using the function @ref cv::morphologyEx with the operation type @ref cv::MORPH_HITMISS and the chosen kernel.

Other examples
--------------

Here you can find the output results of applying different kernels to the same input image used before:

![Kernel and output result for finding top-right corners](images/hitmiss_example2.png)
![Kernel and output result for finding left end points](images/hitmiss_example3.png)

Now try your own patterns!
