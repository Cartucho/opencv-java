import org.opencv.core.*;

import java.util.Scanner;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.imgcodecs.Imgcodecs;

class AddingImagesRun{
    public void run() {
        double alpha = 0.5; double beta; double input;
        Scanner scan = new Scanner( System.in );

        Mat src1, src2, dst = new Mat();

        System.out.println(" Simple Linear Blender ");
        System.out.println("-----------------------");
        System.out.println("* Enter alpha [0-1]: ");
        input = scan.nextDouble();

        if( input >= 0.0 && input <= 1.0 )
            alpha = input;

    //! [load_images]
        src1 = Imgcodecs.imread("../../images/LinuxLogo.jpg");
        src2 = Imgcodecs.imread("../../images/WindowsLogo.jpg");
    //! [load_images]

        if( src1.empty() == true ){ System.out.println("Error loading src1"); return;}
        if( src2.empty() == true ){ System.out.println("Error loading src2"); return;}

    //! [blend]
        beta = ( 1.0 - alpha );
        Core.addWeighted( src1, alpha, src2, beta, 0.0, dst);
    //! [blend]

        Image image = toBufferedImage(dst);
        displayImage(image);


    }

    public Image toBufferedImage(Mat m){
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

    public void displayImage(Image img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class AddingImages {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new AddingImagesRun().run();
    }
}
