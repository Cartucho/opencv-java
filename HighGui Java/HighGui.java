import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

public class HighGui {

    public static int count = 0;
    static ArrayList<JFrame> framesList = new ArrayList<JFrame>();

    /*
       The function namedWindow just makes sure that if you wish to do something
       with that same window afterwards (eg move, resize, close that window),
       you can do it by referencing it with the same name.
     */
    public static void namedWindow() {
    }

    public static void imshow(String winname, Mat _img) {
        Image tmpImg = toBufferedImage(_img);
            displayImage(winname, tmpImg);
        count++;
    }

    public static Image toBufferedImage(Mat m) {
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

    public static void displayImage(String title, Image img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        //frame.addWindowListener(exitListener);

        // num is a reference to each image shown
        int index = count;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println(index +" is closing");

                count--;
                if(count == 0)
                    System.exit(0);
            }
        });

        framesList.add(index, frame);
    }

    public static void waitKey(int delay) {

        for (JFrame frame : framesList) {
            frame.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {}

                @Override
                public void keyPressed(KeyEvent e) {
                    //System.out.println("Pressed " + e.getKeyChar());
                    System.exit(0);
                }
            });
            frame.pack();
            frame.setVisible(true);
        }
    }
}
