#include <opencv2/opencv.hpp>
#include <iostream>

using namespace cv;

int main( int, char** argv )
{
//! [parameters]
    double alpha; /*< Simple contrast control */
    int beta;  /*< Simple brightness control */
//! [parameters]

//! [load_image]
    Mat image = imread( argv[1] );
//! [load_image]
//! [mat_zeros]
    Mat new_image = Mat::zeros( image.size(), image.type() );
//! [mat_zeros]

    std::cout<<" Basic Linear Transforms "<<std::endl;
    std::cout<<"-------------------------"<<std::endl;
    std::cout<<"* Enter the alpha value [1.0-3.0]: ";std::cin>>alpha;
    std::cout<<"* Enter the beta value [0-100]: "; std::cin>>beta;

//! [loop]
    for( int y = 0; y < image.rows; y++ ) {
        for( int x = 0; x < image.cols; x++ ) {
            for( int c = 0; c < 3; c++ ) {
                new_image.at<Vec3b>(y,x)[c] =
                saturate_cast<uchar>( alpha*( image.at<Vec3b>(y,x)[c] ) + beta );
            }
        }
    }
//! [loop]

//! [show_images]
    namedWindow("Original Image", 1);
    namedWindow("New Image", 1);

    imshow("Original Image", image);
    imshow("New Image", new_image);

    waitKey(0);
//! [show_images]

    return 0;
}
