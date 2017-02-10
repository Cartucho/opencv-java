import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class RemapDemoRun {

    JLabel imageView;

    //! [global]
    Mat src = new Mat(), dst = new Mat();
    Mat map_x = new Mat(), map_y = new Mat();
    String remap_window = "Remap demo";
    int ind = 0;
    //! [global]

    public void run() {

        //! [load_image]
        /// Load the image
        src = Imgcodecs.imread( "../data/dog.jpg", Imgcodecs.IMREAD_COLOR );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [create_mats]
        /// Create dst, map_x and map_y with the same size as src:
        dst.create( src.size(), src.type() );
        map_x.create( src.size(), CvType.CV_32FC1 );
        map_y.create( src.size(), CvType.CV_32FC1 );
        //! [create_mats]
 
        Imgproc.putText( dst, "Remap",
                new Point( src.cols()/4, src.rows()/2),
                Core.FONT_HERSHEY_COMPLEX, 1, new Scalar(255, 255, 255) );
        Image tmpImg = toBufferedImage(dst);
        //! [create_window]
        displayImage(remap_window, tmpImg);
        //! [create_window]

        //! [main_loop]
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /// Update map_x & map_y. Then apply remap
            update_map();
            Imgproc.remap( src, dst, map_x, map_y, Imgproc.INTER_LINEAR,
                    Core.BORDER_CONSTANT, new Scalar(0, 0, 0) );

            // Display results
            tmpImg = toBufferedImage(dst);
            imageView.setIcon(new ImageIcon(tmpImg));
        }
        //! [main_loop]
    }

    private void update_map() {
        ind = ind%4;

        //! [map_loop]
        for( int j = 0; j < src.rows(); j++ )
        { for( int i = 0; i < src.cols(); i++ )
        {
            switch( ind )
            {
                case 0:
                    if( i > src.cols()*0.25 && i < src.cols()*0.75 && j > src.rows()*0.25 && j < src.rows()*0.75 )
                    {
                        map_x.put(j, i, 2*( i - src.cols()*0.25f ) + 0.5f);
                        map_y.put(j, i, 2*( j - src.rows()*0.25f ) + 0.5f);
                    }
                    else
                    {
                        map_x.put(j, i, 0);
                        map_y.put(j, i, 0);
                    }
                    break;
                case 1:
                    map_x.put(j, i, (float)i);
                    map_y.put(j, i, (float)(src.rows()) - j);
                    break;
                case 2:
                    map_x.put(j, i, (float)(src.cols() - i));
                    map_y.put(j, i, (float)j);
                    break;
                case 3:
                    map_x.put(j, i, (float)(src.cols() - i));
                    map_y.put(j, i, (float)(src.rows() - j));
                    break;
            } // end of switch
        }
        }
        ind++;
        //! [map_loop]
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
        imageView =new JLabel(icon);
        frame.add(imageView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class RemapDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RemapDemoRun().run();
    }
}
