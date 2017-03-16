/**
 * @file HoughCircle_Demo.cpp
 * @brief Demo code for Hough Transform
 * @author OpenCV team
 */

#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <iostream>

using namespace cv;
using namespace std;

/// Global variables
//! [global_variables]
Mat src, src_gray;

// initial and max values of the parameters of interests.
int cannyThreshold = 200;
int accumulatorThreshold = 50;
const int maxAccumulatorThreshold = 200;
const int maxCannyThreshold = 255;

// windows and trackbars name
const char* windowName = "Hough Circle Detection Demo";
const char* cannyThresholdTrackbarName = "Canny threshold";
const char* accumulatorThresholdTrackbarName = "Accumulator Threshold";
//! [global_variables]

/**
 * @function HoughDetection
 * @brief Trackbar callback - Circles Detection
 */
void HoughDetection(int, void*)
{
    // those paramaters cannot be =0
    cannyThreshold = std::max(cannyThreshold, 1);
    accumulatorThreshold = std::max(accumulatorThreshold, 1);

    // will hold the results of the detection
    std::vector<Vec3f> circles;
    // runs the actual detection
    HoughCircles( src_gray, circles, HOUGH_GRADIENT, 1, src_gray.rows/8, cannyThreshold, accumulatorThreshold, 0, 0 );

    // clone the colour, input image for displaying purposes
    Mat display = src.clone();
    for( size_t i = 0; i < circles.size(); i++ )
    {
        Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
        int radius = cvRound(circles[i][2]);
        // circle center
        circle( display, center, 3, Scalar(0,255,0), -1, 8, 0 );
        // circle outline
        circle( display, center, radius, Scalar(0,0,255), 3, 8, 0 );
    }

    // shows the results
    imshow( windowName, display);
}

/**
 * @function main
 */
int main(int argc, char** argv)
{
    if (argc < 2)
    {
        std::cerr<<"No input image specified\n";
        std::cout<<"Usage : tutorial_HoughCircle_Demo <path_to_input_image>\n";
        return -1;
    }

    // Read the image
    src = imread( argv[1], IMREAD_COLOR );

    if( src.empty() )
    {
        std::cerr<<"Invalid input image\n";
        return -1;
    }

    // Convert it to gray
    cvtColor( src, src_gray, COLOR_BGR2GRAY );

    // Reduce the noise so we avoid false circle detection
    GaussianBlur( src_gray, src_gray, Size(9, 9), 2, 2 );

    // create the main window, and attach the trackbars
    namedWindow( windowName, WINDOW_AUTOSIZE );

    // Create Trackbars
    createTrackbar(cannyThresholdTrackbarName, windowName,
                   &cannyThreshold, maxCannyThreshold, HoughDetection);
    createTrackbar(accumulatorThresholdTrackbarName, windowName,
                   &accumulatorThreshold,maxAccumulatorThreshold, HoughDetection);

    // Default start
    HoughDetection(0, 0);

    // Wait until user exit program by pressing a key
    waitKey(0);
    return 0;
}
