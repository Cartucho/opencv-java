/**
 * @file copyMakeBorder_demo.cpp
 * @brief Sample code that shows the functionality of copyMakeBorder
 * @author OpenCV team
 */

#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <stdlib.h>
#include <stdio.h>

using namespace cv;

//! [declare_variables]
/// Global Variables
Mat src, dst;
int top, bottom, left, right;
int borderType;
const char* window_name = "copyMakeBorder Demo";
RNG rng(12345);
//! [declare_variables]

/**
 * @function main
 */
int main( int, char** argv )
{

  int c;

  //! [load_image]
  /// Load an image
  src = imread( argv[1] );

  if( src.empty() )
    {
      printf(" No data entered, please enter the path to an image file \n");
      return -1;
    }
  //! [load_image]

  /// Brief how-to for this program
  printf( "\n \t copyMakeBorder Demo: \n" );
  printf( "\t -------------------- \n" );
  printf( " ** Press 'c' to set the border to a random constant value \n");
  printf( " ** Press 'r' to set the border to be replicated \n");
  printf( " ** Press 'ESC' to exit the program \n");

  //! [create_window]
  /// Create window
  namedWindow( window_name, WINDOW_AUTOSIZE );
  //! [create_window]

  //! [initialize_arguments]
  /// Initialize arguments for the filter
  top = (int) (0.05*src.rows); bottom = top;
  left = (int) (0.05*src.cols); right = left;
  //! [initialize_arguments]
  dst = src;

  imshow( window_name, dst );

  //! [loop]
  for(;;)
       {
         c = waitKey(500);

         if( (char)c == 27 )
           { break; }
         else if( (char)c == 'c' )
           { borderType = BORDER_CONSTANT; }
         else if( (char)c == 'r' )
           { borderType = BORDER_REPLICATE; }

         //! [value]
         Scalar value( rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255) );
         //! [value]
         //! [copy_make_border]
         copyMakeBorder( src, dst, top, bottom, left, right, borderType, value );
         //! [copy_make_border]

         //! [imshow]
         imshow( window_name, dst );
         //! [imshow]
       }
  //! [loop]

  return 0;
}
