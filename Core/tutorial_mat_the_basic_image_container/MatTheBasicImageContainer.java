import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

class MatTheBasicImageContainerRun {

    public void run() {

    //! [assignment]
        // creates just the header parts
        Mat A, B;
        // here we'll know the method used (allocate matrix)
        A = Imgcodecs.imread("../data/lena.jpg", Imgcodecs.IMREAD_COLOR);
        // Assignment operator
        B = A;
    //! [assignment]

        if(A.empty())
            System.out.println("Error opening image");

    //! [clone_copy]
        Mat C = A.submat(new Rect(10, 10, 100, 100)); // copy a ROI - region of interest
        Mat D = A.clone();
        Mat E = new Mat();
        A.copyTo(E);
    //! [clone_copy]

    //! [constructor]
        Mat M = new Mat(2, 2, CvType.CV_8UC3, new Scalar(0,0,255));
        System.out.println("M = \n" + M.dump());
    //! [constructor]

    //! [java_array]
        int rows = 3, cols = 3;
        int data[] = {  0, -1,  0,
                -1,  5, -1,
                0, -1,  0 };
        //allocate Mat before calling put
        C = new Mat( rows, cols, CvType.CV_32S );
        C.put( 0, 0, data );
        System.out.println("C = \n" + C.dump());
    //! [java_array]

    //! [mat_create]
        M = new Mat();
        M.create(4,4, CvType.CV_8UC(2));
        System.out.println("M = \n" + M.dump());
    //! [mat_create]

    //! [row_clone]
        Mat RowClone = C.row(1).clone();
        System.out.println("RowClone = \n" + RowClone.dump());
    //! [row_clone]

    //! [fill_random]
        Mat R = new Mat(3, 2, CvType.CV_8UC3);
        double inclusive_lower_bound = 0.0;
        double exclusive_upper_bound = 255.0;
        Core.randu(R, inclusive_lower_bound, exclusive_upper_bound);
        System.out.println("R = \n" + R.dump());
    //! [fill_random]

    //! [matlab_style]
        Mat E_ = Mat.eye(4, 4, CvType.CV_64F);
        System.out.println("E = \n" + E_.dump());
        Mat O = Mat.ones(2, 2, CvType.CV_32F);
        System.out.println("O = \n" + O.dump());
        Mat Z = Mat.zeros(3,3, CvType.CV_8UC1);
        System.out.println("Z = \n" + Z.dump());
    //! [matlab_style]
    }
}

public class MatTheBasicImageContainer {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new MatTheBasicImageContainerRun().run();

    }
}
