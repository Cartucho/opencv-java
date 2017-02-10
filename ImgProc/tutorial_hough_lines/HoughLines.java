import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.util.Scanner;

class HoughLinesRun {

    public void run() {

        System.out.println(" Hough Lines Demo ");
        System.out.println(" ---------------- ");
        System.out.println(" Press [0] to draw lines" +
                " using the Standard Hough Line Transform ");
        System.out.println(" Press [1] to draw lines" +
                " using the Probabilistic Hough Line Transform ");

        //! [load_image]
        Mat src = Imgcodecs.imread( "../data/building.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        Mat dst = new Mat(), cdst = new Mat();
        //! [canny]
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        //! [canny]
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);

        Scanner in = new Scanner(System.in);
        System.out.print("\n Choice : ");

        if(in.hasNextInt()){
            int num = in.nextInt();
            if(num == 0)
                standardHoughLineTransform(dst, cdst);
            else if(num == 1)
                probabilisticHoughLineTransform(dst, cdst);
        }else{
            System.out.println(" Invalid choice! ");
        }


    }

    private void standardHoughLineTransform(Mat dst, Mat cdst) {

        double rho = 1.0, theta = Math.PI/(double)180;
        int threshold = 100;

        Mat lines = new Mat();
        //! [hough_lines]
        Imgproc.HoughLines(dst, lines, rho, theta, threshold);
        //! [hough_lines]

        //! [loop]
        for (int x = 0; x < lines.rows(); x++) {
            double[] vec = lines.get(x, 0);
            double tmpRho = vec[0],
                    tmpTheta = vec[1];

            double a = Math.cos(tmpTheta), b = Math.sin(tmpTheta);
            double x0 = a*tmpRho, y0 = b*tmpRho;

            Point start = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point end = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));

            Scalar color = new Scalar(0, 0, 255);
            int thickness = 3, lineType = Imgproc.LINE_AA, shift = 0;

            Imgproc.line(cdst, start, end, color, thickness, lineType, shift);
        }
        //! [loop]

        Image tmpImg = toBufferedImage(cdst);
        displayImage("Detected lines using the Standard approach", tmpImg);
    }

    private void probabilisticHoughLineTransform(Mat dst, Mat cdst) {

        double rho = 1.0, theta = Math.PI/(double)180;
        int threshold = 50;
        double minLineLength = 20.0, maxLineGap = 20.0;

        Mat lines = new Mat();
        //! [hough_lines_p]
        Imgproc.HoughLinesP(dst, lines, rho, theta, threshold, minLineLength, maxLineGap);
        //! [hough_lines_p]

        //! [loop_p]
        for (int x = 0; x < lines.rows(); x++) {
            double[] vec = lines.get(x, 0);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);

            Scalar color = new Scalar(0, 0, 255);
            int thickness = 3, lineType = Imgproc.LINE_AA, shift = 0;

            Imgproc.line(cdst, start, end, color, thickness, lineType, shift);
        }
        //! [loop_p]

        Image tmpImg = toBufferedImage(cdst);
        displayImage("Detected lines using the Probabilistic approach", tmpImg);
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

public class HoughLines {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughLinesRun().run();

    }
}
