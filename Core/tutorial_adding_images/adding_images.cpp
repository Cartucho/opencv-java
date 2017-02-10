#include <opencv2/opencv.hpp>
#include <iostream>

using namespace cv;

int main( void )
{
 double alpha = 0.5; double beta; double input;

 Mat src1, src2, dst;

 std::cout<<" Simple Linear Blender "<<std::endl;
 std::cout<<"-----------------------"<<std::endl;
 std::cout<<"* Enter alpha [0-1]: ";
 std::cin>>input;

 if( input >= 0.0 && input <= 1.0 )
   { alpha = input; }

//! [load_images]
 src1 = imread("../../images/LinuxLogo.jpg");
 src2 = imread("../../images/WindowsLogo.jpg");
//! [load_images]

 if( !src1.data ) { printf("Error loading src1 \n"); return -1; }
 if( !src2.data ) { printf("Error loading src2 \n"); return -1; }

 namedWindow("Linear Blend", 1);

//! [blend]
 beta = ( 1.0 - alpha );
 addWeighted( src1, alpha, src2, beta, 0.0, dst);
//! [blend]

 imshow( "Linear Blend", dst );

 waitKey(0);
 return 0;
}
