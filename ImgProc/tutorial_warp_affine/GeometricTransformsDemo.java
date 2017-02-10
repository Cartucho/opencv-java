import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class GeometricTransformsDemoRun {

    public void run() {

        MatOfPoint2f srcTri;
        MatOfPoint2f dstTri;

        String source_window = "Source image";
        String warp_window = "Warp";
        String warp_rotate_window = "Warp + Rotate";

        //! [variables]
        Mat rot_mat = new Mat( 2, 3, CvType.CV_32FC1 );
        Mat warp_mat = new Mat( 2, 3, CvType.CV_32FC1 );
        Mat src = new Mat(), warp_dst = new Mat(), warp_rotate_dst = new Mat();
        //! [variables]

        //! [load_image]
        /// Load the image
        src = Imgcodecs.imread( "../data/landscape.jpg", Imgcodecs.IMREAD_COLOR );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [mat_zero]
        /// Set the dst image the same type and size as src
        warp_dst = Mat.zeros( src.rows(), src.cols(), src.type() );
        //! [mat_zero]

        //! [set_points]
        /// Set your 3 points to calculate the Affine Transform
        Point[] srcPointArray = {new Point(0,0),
                new Point(src.cols() - 1.f, 0 ),
                new Point( 0, src.rows() - 1.f )};
        srcTri = new MatOfPoint2f(srcPointArray);

        Point[] dstPointArray = {new Point(src.cols()*0.0f, src.rows()*0.33f),
                new Point(src.cols()*0.85f, src.rows()*0.25f),
                new Point( src.cols()*0.15f, src.rows()*0.7f )};
        dstTri = new MatOfPoint2f(dstPointArray);
        //! [set_points]

        //! [get_affine_transform]
        /// Get the Affine Transform
        warp_mat = Imgproc.getAffineTransform( srcTri, dstTri );
        //! [get_affine_transform]

        //! [apply_affine_transform]
        /// Apply the Affine Transform just found to the src image
        Imgproc.warpAffine( src, warp_dst, warp_mat, warp_dst.size() );
        //! [apply_affine_transform]

        /** Rotating the image after Warp */

        //! [rotate_variables]
        /// Compute a rotation matrix with respect to the center of the image
        Point center = new Point( warp_dst.cols()/2, warp_dst.rows()/2 );
        double angle = -50.0;
        double scale = 0.6;
        //! [rotate_variables]

        //! [rotation_matrix]
        /// Get the rotation matrix with the specifications above
        rot_mat = Imgproc.getRotationMatrix2D( center, angle, scale );
        //! [rotation_matrix]

        //! [rotate_image]
        /// Rotate the warped image
        Imgproc.warpAffine( warp_dst, warp_rotate_dst, rot_mat, warp_dst.size() );
        //! [rotate_image]

        //! [imshow]
        /// Show what you got
        Image show_image;

        show_image = toBufferedImage(src);
        displayImage(source_window, show_image);

        show_image = toBufferedImage(warp_dst);
        displayImage(warp_window, show_image);

        show_image = toBufferedImage(warp_rotate_dst);
        displayImage(warp_rotate_window, show_image);
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

public class GeometricTransformsDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new GeometricTransformsDemoRun().run();
    }
}
