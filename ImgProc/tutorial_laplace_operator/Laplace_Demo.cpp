/**
 * @file Laplace_Demo.cpp
 * @brief Sample code showing how to detect edges using the Laplace operator
 * @author OpenCV team
 */

#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <stdlib.h>
#include <stdio.h>

using namespace cv;

/**
 * @function main
 */
int main( int, char** argv )
{
  //! [declare_variables]
  Mat src, src_gray, dst;
  int kernel_size = 3;
  int scale = 1;
  int delta = 0;
  int ddepth = CV_16S;
  const char* window_name = "Laplace Demo";
  //! [declare_variables]

  //! [load_image]
  /// Load an image
  src = imread( argv[1] );

  if( src.empty() )
    { return -1; }
  //! [load_image]

  //! [gaussian_blur]
  /// Remove noise by blurring with a Gaussian filter
  GaussianBlur( src, src, Size(3,3), 0, 0, BORDER_DEFAULT );
  //! [gaussian_blur]

  //! [convert_to_gray]
  /// Convert the image to grayscale
  cvtColor( src, src_gray, COLOR_RGB2GRAY );
  //! [convert_to_gray]

  /// Create window
  namedWindow( window_name, WINDOW_AUTOSIZE );

  /// Apply Laplace function
  Mat abs_dst;

  //! [laplacian]
  Laplacian( src_gray, dst, ddepth, kernel_size, scale, delta, BORDER_DEFAULT );
  //! [laplacian]
  //! [convert_scale]
  convertScaleAbs( dst, abs_dst );
  //! [convert_scale]

  //! [imshow]
  /// Show what you got
  imshow( window_name, abs_dst );
  //! [imshow]

  waitKey(0);

  return 0;
}
