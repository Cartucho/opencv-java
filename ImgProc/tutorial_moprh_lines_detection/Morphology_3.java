import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class Morphology_3Run {

    public void run() {

    //! [load_image]
        // Load the image
        Mat src = Imgcodecs.imread("../images/music_sheet.jpg");

        // Show source image
        Image img = toBufferedImage( src );
        displayImage("src" , img, 0, 0);
    //! [load_image]

    //! [gray]
        // Transform source image to gray if it is not
        Mat gray = new Mat();
        if (src.channels() == 3)
        {
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        }
        else
        {
            gray = src;
        }

        // Show gray image
        img = toBufferedImage( gray );
        displayImage("gray" , img, 0, 50);
    //! [gray]

    //! [bin]
        // Apply adaptiveThreshold at the bitwise_not of gray
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

        // Show binary image
        img = toBufferedImage( bw );
        displayImage("binary" , img, 0, 100);
    //! [bin]

    //! [init]
        // Create the images that will use to extract the horizontal and vertical lines
        Mat horizontal = bw.clone();
        Mat vertical = bw.clone();
    //! [init]

    //! [horiz]
        // Specify size on horizontal axis
        int horizontalsize = horizontal.cols() / 30;

        // Create structure element for extracting horizontal lines through morphology operations
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize,1));

        // Apply morphology operations
        Imgproc.erode(horizontal, horizontal, horizontalStructure);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure);

        // Show extracted horizontal lines
        img = toBufferedImage( horizontal );
        displayImage("horizontal" , img, 0, 150);
    //! [horiz]

    //! [vert]
        // Specify size on vertical axis
        int verticalsize = vertical.rows() / 30;

        // Create structure element for extracting vertical lines through morphology operations
        Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size( 1,verticalsize));

        // Apply morphology operations
        Imgproc.erode(vertical, vertical, verticalStructure);
        Imgproc.dilate(vertical, vertical, verticalStructure);

        // Show extracted vertical lines
        img = toBufferedImage( vertical );
        displayImage("vertical" , img, 0, 200);
    //! [vert]

    //! [smooth]
        // Inverse vertical image
        Core.bitwise_not(vertical, vertical);
        img = toBufferedImage( vertical );
        displayImage("vertical_bit" , img, 0, 250);

        // Extract edges and smooth image according to the logic
        // 1. extract edges
        // 2. dilate(edges)
        // 3. src.copyTo(smooth)
        // 4. blur smooth img
        // 5. smooth.copyTo(src, edges)

        // Step 1
        Mat edges = new Mat();
        Imgproc.adaptiveThreshold(vertical, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 3, -2);
        img = toBufferedImage( edges );
        displayImage("edges" , img, 0, 300);

        // Step 2
        Mat kernel = Mat.ones(2, 2, CvType.CV_8UC1);
        Imgproc.dilate(edges, edges, kernel);
        img = toBufferedImage( edges );
        displayImage("dilate" , img, 0, 350);

        // Step 3
        Mat smooth = new Mat();
        vertical.copyTo(smooth);

        // Step 4
        Imgproc.blur(smooth, smooth, new Size(2, 2));

        // Step 5
        smooth.copyTo(vertical, edges);

        // Show final result
        img = toBufferedImage( vertical );
        displayImage("smooth" , img, 0, 400);

    //! [smooth]
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

    public void displayImage(String title, Image img, int x, int y)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
}

public class Morphology_3 {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Morphology_3Run().run();

    }
}
