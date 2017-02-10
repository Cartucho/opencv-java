/**
 * @function Geometric_Transforms_Demo.cpp
 * @brief Demo code for Geometric Transforms
 * @author OpenCV team
 */

#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>

using namespace cv;
using namespace std;

/// Global variables
const char* source_window = "Source image";
const char* warp_window = "Warp";
const char* warp_rotate_window = "Warp + Rotate";

/**
 * @function main
 */
int main( int, char** argv )
{
  //! [variables]
  Point2f srcTri[3];
  Point2f dstTri[3];

  Mat rot_mat( 2, 3, CV_32FC1 );
  Mat warp_mat( 2, 3, CV_32FC1 );
  Mat src, warp_dst, warp_rotate_dst;
  //! [variables]

  //! [load_image]
  /// Load the image
  src = imread( argv[1], 1 );
  //! [load_image]

  //! [mat_zero]
  /// Set the dst image the same type and size as src
  warp_dst = Mat::zeros( src.rows, src.cols, src.type() );
  //! [mat_zero]

  //! [set_points]
  /// Set your 3 points to calculate the  Affine Transform
  srcTri[0] = Point2f( 0,0 );
  srcTri[1] = Point2f( src.cols - 1.f, 0 );
  srcTri[2] = Point2f( 0, src.rows - 1.f );

  dstTri[0] = Point2f( src.cols*0.0f, src.rows*0.33f );
  dstTri[1] = Point2f( src.cols*0.85f, src.rows*0.25f );
  dstTri[2] = Point2f( src.cols*0.15f, src.rows*0.7f );
  //! [set_points]

  //! [get_affine_transform]
  /// Get the Affine Transform
  warp_mat = getAffineTransform( srcTri, dstTri );
  //! [get_affine_transform]

  //! [apply_affine_transform]
  /// Apply the Affine Transform just found to the src image
  warpAffine( src, warp_dst, warp_mat, warp_dst.size() );
  //! [apply_affine_transform]

  /** Rotating the image after Warp */

  //! [rotate_variables]
  /// Compute a rotation matrix with respect to the center of the image
  Point center = Point( warp_dst.cols/2, warp_dst.rows/2 );
  double angle = -50.0;
  double scale = 0.6;
  //! [rotate_variables]

  //! [rotation_matrix]
  /// Get the rotation matrix with the specifications above
  rot_mat = getRotationMatrix2D( center, angle, scale );
  //! [rotation_matrix]

  //! [rotate_image]
  /// Rotate the warped image
  warpAffine( warp_dst, warp_rotate_dst, rot_mat, warp_dst.size() );
  //! [rotate_image]

  //! [imshow]
  /// Show what you got
  namedWindow( source_window, WINDOW_AUTOSIZE );
  imshow( source_window, src );

  namedWindow( warp_window, WINDOW_AUTOSIZE );
  imshow( warp_window, warp_dst );

  namedWindow( warp_rotate_window, WINDOW_AUTOSIZE );
  imshow( warp_rotate_window, warp_rotate_dst );
  //! [imshow]

  //! [wait_key]
  /// Wait until user exits the program
  waitKey(0);
  //! [wait_key]

  return 0;
}
