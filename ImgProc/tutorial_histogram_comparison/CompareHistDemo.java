import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

class CompareHistDemoRun {

    public void run() {

        //! [declare_mats]
        Mat src_base = new Mat(), hsv_base = new Mat();
        Mat src_test1 = new Mat(), hsv_test1 = new Mat();
        Mat src_test2 = new Mat(), hsv_test2 = new Mat();
        Mat hsv_half_down = new Mat();
        //! [declare_mats]

        //! [load_images]
        src_base = Imgcodecs.imread( "../data/Base_0.jpg", Imgcodecs.IMREAD_COLOR );
        src_test1 = Imgcodecs.imread( "../data/Test_1.jpg", Imgcodecs.IMREAD_COLOR );
        src_test2 = Imgcodecs.imread( "../data/Test_2.jpg", Imgcodecs.IMREAD_COLOR );

        if(src_base.empty() || src_test1.empty() || src_test2.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_images]

        //! [convert_to_hsv]
        /// Convert to HSV
        Imgproc.cvtColor( src_base, hsv_base, Imgproc.COLOR_BGR2HSV );
        Imgproc.cvtColor( src_test1, hsv_test1, Imgproc.COLOR_BGR2HSV );
        Imgproc.cvtColor( src_test2, hsv_test2, Imgproc.COLOR_BGR2HSV );
        //! [convert_to_hsv]

        //! [half_base]
        hsv_half_down = hsv_base.submat(new Range( hsv_base.rows()/2, hsv_base.rows() - 1 ),
                new Range( 0, hsv_base.cols() - 1 ));
        //! [half_base]

        //! [intialize_arguments]
        /// Using 50 bins for hue and 60 for saturation
        int h_bins = 50; int s_bins = 60;
        MatOfInt histSize = new MatOfInt(h_bins, s_bins);

        // hue varies from 0 to 179, saturation from 0 to 255
        MatOfFloat range = new MatOfFloat(0.0f, 180.0f, 0.0f, 256.0f);

        // Use the o-th and 1-st channels
        MatOfInt channels = new MatOfInt(0, 1);
        //! [intialize_arguments]

        //! [mat_histograms]
        /// Histograms
        Mat hist_base = new Mat();
        Mat hist_half_down = new Mat();
        Mat hist_test1 = new Mat();
        Mat hist_test2 = new Mat();
        //! [mat_histograms]

        //! [calculate_histograms]
        /// Calculate the histograms for the HSV images
        Imgproc.calcHist(new ArrayList<Mat>(Arrays.asList(hsv_base)), channels,
                new Mat(), hist_base, histSize, range, false);
        Core.normalize(hist_base, hist_base, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        Imgproc.calcHist(new ArrayList<Mat>(Arrays.asList(hsv_half_down)), channels,
                new Mat(), hist_half_down, histSize, range, false);
        Core.normalize(hist_half_down, hist_half_down, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        Imgproc.calcHist(new ArrayList<Mat>(Arrays.asList(hsv_test1)), channels,
                new Mat(), hist_test1, histSize, range, false);
        Core.normalize(hist_test1, hist_test1, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        Imgproc.calcHist(new ArrayList<Mat>(Arrays.asList(hsv_test2)), channels,
                new Mat(),  hist_test2, histSize, range, false);
        Core.normalize( hist_test2,  hist_test2, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        //! [calculate_histograms]

        //! [loop]
        /// Apply the histogram comparison methods
        for( int i = 0; i < 4; i++ )
        {
            int compare_method = i;
            double base_base = Imgproc.compareHist( hist_base, hist_base, compare_method );
            double base_half = Imgproc.compareHist( hist_base, hist_half_down, compare_method );
            double base_test1 = Imgproc.compareHist( hist_base, hist_test1, compare_method );
            double base_test2 = Imgproc.compareHist( hist_base, hist_test2, compare_method );

            System.out.printf(" Method [%d] Perfect, Base-Half, Base-Test(1), Base-Test(2) :" +
                    " %f, %f, %f, %f\n", i, base_base, base_half , base_test1, base_test2 );
        }
        //! [loop]

        System.out.println( "Done" );
    }
}

public class CompareHistDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CompareHistDemoRun().run();
    }
}
