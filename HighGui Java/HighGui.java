import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HighGui {

    public static int n_closed_windows = 0;
    public static int pressedKey = -1;
    
    static CountDownLatch latch = new CountDownLatch(1);

    static Map<String,ImageWindow> windows = new HashMap<String,ImageWindow>();

    /*
       The function namedWindow just makes sure that if you wish to do something
       with that same window afterwards (eg move, resize, close that window),
       you can do it by referencing it with the same name.
     */
    public static void namedWindow() {
    }

    public static void imshow(String winname, Mat img) {

        if (img.empty()){
            System.err.println("Error: Empty image in imshow");
            System.exit(-1);
        }else{
            ImageWindow tmpWindow = windows.get(winname);
            if(tmpWindow == null){
                windows.put(winname, new ImageWindow(winname, img));
            }else{
                tmpWindow.setMat(img);
            }
        }
        
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

    public static JFrame createJFrame(String title)
    {
        JFrame frame = new JFrame(title);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                n_closed_windows++;
                System.out.println(n_closed_windows);
                if(n_closed_windows == windows.size())
                    latch.countDown();

            }
        });

        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKey = e.getKeyCode();
                //System.out.println(pressedKey[0]);
                latch.countDown();
            }
        });

        return frame;
    }

    public static int waitKey(int delay) {

        // Remove the unused windows
        for (ImageWindow win : windows.values())
            if(win.alreadyUsed)
                windows.remove(win.name);

        // (if) Create (else) Update frame
        for (ImageWindow win : windows.values()) {

            ImageIcon icon = new ImageIcon(toBufferedImage(win.img));

            if (win.lbl == null) {
                // Create JFrame
                JFrame frame = createJFrame(win.name);
                // Create JLabel
                JLabel lbl = new JLabel(icon);
                win.lbl = lbl;
                // Add JLabel to JFrame, Pack and Show
                frame.add(lbl);
                frame.pack();
                frame.setVisible(true);
            } else {
                win.lbl.setIcon(icon);
            }
        }

        // Reset control values
        latch = new CountDownLatch(1);
        n_closed_windows = 0;
        pressedKey = -1;

        try {
            if(delay == 0){
                latch.await();
            }else {
                latch.await(delay, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set all windows as already used
        for (ImageWindow win : windows.values())
            win.alreadyUsed = true;

        return pressedKey;

    }

}
