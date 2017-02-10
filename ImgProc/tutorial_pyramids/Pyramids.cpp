/**
 * @file Pyramids.cpp
 * @brief Sample code of image pyramids (pyrDown and pyrUp)
 * @author OpenCV team
 */

#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <math.h>
#include <stdlib.h>
#include <stdio.h>

using namespace cv;

Mat img;
const char* window_name = "Pyramids Demo";

int main(void)
{
    printf("\n Zoom In-Out demo  \n ");
    printf("------------------ \n");
    printf(" * [u] -> Zoom in  \n");
    printf(" * [d] -> Zoom out \n");
    printf(" * [ESC] -> Close program \n \n");

    //! [load_image]
    img = imread("../data/chicky_512.png");
    if (img.empty()) {
        printf(" No data! -- Exiting the program \n");
        return -1;
    }
    //! [load_image]

    //! [create_window]
    namedWindow(window_name, WINDOW_AUTOSIZE);
    imshow(window_name, img);
    //! [create_window]

    //! [loop]
    for (;;) {
        int c;
        c = waitKey(10);
        if ((char)c == 27) {
            break;
        }
        if ((char)c == 'u') {
            //! [pyrUp]
            pyrUp(img, img, Size(img.cols * 2, img.rows * 2));
            //! [pyrUp]
            printf("** Zoom In: Image x 2 \n");
        }
        else if ((char)c == 'd') {
            //! [pyrDown]
            pyrDown(img, img, Size(img.cols / 2, img.rows / 2));
            //! [pyrDown]
            printf("** Zoom Out: Image / 2 \n");
        }
        imshow(window_name, img);
    }
    //! [loop]
    return 0;
}
