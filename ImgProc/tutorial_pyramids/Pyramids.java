import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Scanner;

class PyramidsRun {

    Mat img = new Mat();
    String window_name = "Pyramids Demo";

    public void run() {

        System.out.println( "\n Zoom In-Out demo " );
        System.out.println( "------------------  " );
        System.out.println( " * [1] -> Zoom in   " );
        System.out.println( " * [2] -> Zoom out  " );
        System.out.println( " * [0] -> Close program \n" );

    //! [load_image]
        img = Imgcodecs.imread("../data/chicky_512.png");
    //! [load_image]


    //! [show_image]
        Image imgShow = toBufferedImage( img );
        JFrame frame = displayImage(window_name , imgShow, 0, 0);
    //! [show_image]

        Scanner reader = new Scanner(System.in);

    //! [loop]
        while (true){

            System.out.print("Enter a number: ");
            int n = reader.nextInt();

            if(n == 0){
                frame.dispose();
                break;
            }else if(n == 1){
                Imgproc.pyrUp( img, img, new Size( img.cols()*2, img.rows()*2 ) );
                System.out.println( "** Zoom In: Image x 2" );
            }else if( n == 2){
                Imgproc.pyrDown( img, img, new Size( img.cols()/2, img.rows()/2 ) );
                System.out.println( "** Zoom Out: Image / 2" );
            }else{
                System.out.print(" \n Invalid option! \n");
            }

            frame.dispose();
            imgShow = toBufferedImage( img );
            frame = displayImage(window_name , imgShow, 0, 0);

        }
    //! [loop]
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

    public JFrame displayImage(String title, Image img, int x, int y)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);

        return frame;
    }

}

public class Pyramids {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new PyramidsRun().run();

    }
}
