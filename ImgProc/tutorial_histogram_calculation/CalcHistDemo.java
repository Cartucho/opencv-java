import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import java.util.List;

class CalcHistDemoRun {

    public void run() {

        //! [create_mats]
        Mat src = new Mat(), dst = new Mat();
        //! [create_mats]

        //! [load_image]
        /// Load image
        src = Imgcodecs.imread( "../data/lena.jpg" , Imgcodecs.IMREAD_COLOR );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [bgr]
        /// Separate the image in 3 places ( B, G and R )
        List<Mat> bgr_planes = new ArrayList<Mat>();
        Core.split(src, bgr_planes);
        //! [bgr]

        //! [bins_number]
        /// Establish the number of bins
        int sizeOfHist = 256;
        MatOfInt histSize = new MatOfInt(sizeOfHist);
        //! [bins_number]

        //! [set_ranges]
        /// Set the ranges ( for B,G,R )
        MatOfFloat range = new MatOfFloat(0.0f, 256.0f);
        //! [set_ranges]

        MatOfInt channels = new MatOfInt(0);

        //! [parameters]
        Boolean accumulate = false;
        //! [parameters]

        //! [create_bgr_mats]
        Mat hist_b = new Mat();
        Mat hist_g = new Mat();
        Mat hist_r = new Mat();
        //! [create_bgr_mats]

        //! [calc_hist]
        /// Compute the histograms:
        Imgproc.calcHist(bgr_planes.subList(0, 1), channels, new Mat(), hist_b, histSize, range, accumulate);
        Imgproc.calcHist(bgr_planes.subList(1, 2), channels, new Mat(), hist_g, histSize, range, accumulate);
        Imgproc.calcHist(bgr_planes.subList(2, 3), channels, new Mat(), hist_r, histSize, range, accumulate);
        //! [calc_hist]

        //! [to_display]
        // Draw the histograms for B, G and R
        int hist_w = 512; int hist_h = 400;
        int bin_w = (int) Math.round( (double) hist_w / histSize.get(0, 0)[0]);

        Mat histImage = new Mat( hist_h, hist_w, CvType.CV_8UC3, new Scalar( 0,0,0) );
        //! [to_display]

        //! [normalize]
        /// Normalize the result to [ 0, histImage.rows ]
        Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        //! [normalize]

        //! [draw]
        /// Draw for each channel
        for( int i = 1; i < sizeOfHist; i++ )
        {
            Imgproc.line( histImage, new Point( bin_w*(i-1), hist_h -  Math.round(hist_b.get(i - 1, 0)[0])),
                        new Point( bin_w*(i), hist_h - Math.round(hist_b.get(i, 0)[0])),
                        new Scalar( 255, 0, 0), 2, 8, 0  );
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_g.get(i, 0)[0])),
                        new Scalar(0, 255, 0), 2, 8, 0);
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_r.get(i, 0)[0])),
                        new Scalar(0, 0, 255), 2, 8, 0);
        }
        //! [draw]

        //! [imshow]
        Image tmpImg = toBufferedImage(histImage);
        displayImage("calcHist Demo", tmpImg);
        //! [imshow]
    }


    public Image toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public void displayImage(String title, Image img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
public class CalcHistDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CalcHistDemoRun().run();
    }
}
