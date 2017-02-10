/**
 * @file CannyDetector_Demo.cpp
 * @brief Sample code showing how to detect edges using the Canny Detector
 * @author OpenCV team
 */

#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <stdlib.h>
#include <stdio.h>

using namespace cv;

/// Global variables
//! [global_variables]
Mat src, src_gray;
Mat dst, detected_edges;

int edgeThresh = 1;
int lowThreshold;
int const max_lowThreshold = 100;
int ratio = 3;
int kernel_size = 3;
const char* window_name = "Edge Map";
//! [global_variables]
/**
 * @function CannyThreshold
 * @brief Trackbar callback - Canny thresholds input with a ratio 1:3
 */
static void CannyThreshold(int, void*)
{
    //! [blur]
    /// Reduce noise with a kernel 3x3
    blur( src_gray, detected_edges, Size(3,3) );
    //! [blur]

    //! [canny]
    /// Canny detector
    Canny( detected_edges, detected_edges, lowThreshold, lowThreshold*ratio, kernel_size );
    //! [canny]

    //! [output]
    /// Using Canny's output as a mask, we display our result
    dst = Scalar::all(0);
    //! [output]

    //! [copy_to]
    src.copyTo( dst, detected_edges);
    //! [copy_to]
    //! [imshow]
    imshow( window_name, dst );
    //! [imshow]
}


/**
 * @function main
 */
int main( int, char** argv )
{
  //! [load_image]
  /// Load an image
  src = imread( argv[1] );

  if( src.empty() )
    { return -1; }
  //! [load_image]

  //! [create]
  /// Create a matrix of the same type and size as src (for dst)
  dst.create( src.size(), src.type() );
  //! [create]

  //! [grayscale]
  /// Convert the image to grayscale
  cvtColor( src, src_gray, COLOR_BGR2GRAY );
  //! [grayscale]

  //! [window]
  /// Create a window
  namedWindow( window_name, WINDOW_AUTOSIZE );
  //! [window]

  //! [trackbar]
  /// Create a Trackbar for user to enter threshold
  createTrackbar( "Min Threshold:", window_name, &lowThreshold, max_lowThreshold, CannyThreshold );
  //! [trackbar]

  /// Show the image
  CannyThreshold(0, 0);

  /// Wait until user exit program by pressing a key
  waitKey(0);

  return 0;
}
