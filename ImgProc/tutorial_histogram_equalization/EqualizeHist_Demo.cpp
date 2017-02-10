/**
 * @function EqualizeHist_Demo.cpp
 * @brief Demo code for equalizeHist function
 * @author OpenCV team
 */

#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>

using namespace cv;
using namespace std;

/**
 * @function main
 */
int main( int, char** argv )
{
  //! [initial_variables]
  Mat src, dst;

  const char* source_window = "Source image";
  const char* equalized_window = "Equalized Image";
  //! [initial_variables]

  //! [load_image]
  /// Load image
  src = imread( argv[1], 1 );

  if( src.empty() )
    { cout<<"Usage: ./Histogram_Demo <path_to_image>"<<endl;
      return -1;
    }
  //! [load_image]

  //! [grayscale]
  /// Convert to grayscale
  cvtColor( src, src, COLOR_BGR2GRAY );
  //! [grayscale]

  //! [equalization]
  /// Apply Histogram Equalization
  equalizeHist( src, dst );
  //! [equalization]

  //! [imshow]
  /// Display results
  namedWindow( source_window, WINDOW_AUTOSIZE );
  namedWindow( equalized_window, WINDOW_AUTOSIZE );

  imshow( source_window, src );
  imshow( equalized_window, dst );
  //! [imshow]

  //! [wait_key]
  /// Wait until user exits the program
  waitKey(0);
  //! [wait_key]

  return 0;

}
