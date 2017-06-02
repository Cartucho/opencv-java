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

    // Constants fo namedWindow
    static int WINDOW_NORMAL = 0; // enables you to resize the window
    static int WINDOW_AUTOSIZE = 1; // adjusts automatically the window size. It is not resizable!

    // Control Variables
    static int n_closed_windows = 0;
    static int pressedKey = -1;
    static CountDownLatch latch = new CountDownLatch(1);

    // Windows Map
    static Map<String,ImageWindow> windows = new HashMap<String,ImageWindow>();

    public static void namedWindow(String winname, int flag) {
        ImageWindow newWin = new ImageWindow(winname, flag);
        windows.put(winname, newWin);
    }

    public static void imshow(String winname, Mat img) {

        if (img.empty()){
            System.err.println("Error: Empty image in imshow");
            System.exit(-1);
        }else{
            ImageWindow tmpWindow = windows.get(winname);
            if(tmpWindow == null){
                ImageWindow newWin = new ImageWindow(winname, img);
                windows.put(winname, newWin);
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

    public static JFrame createJFrame(String title, int flag)
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
                latch.countDown();
            }
        });

        if(flag == WINDOW_AUTOSIZE)
            frame.setResizable(false);

        return frame;
    }

    public static int waitKey(int delay) {

        // Reset control values
        latch = new CountDownLatch(1);
        n_closed_windows = 0;
        pressedKey = -1;

        // If there are no windows to be shown return
        if(windows.isEmpty()) {
            System.err.println("Error: waitKey must be used after an imshow");
            return pressedKey;
        }

        // Remove the unused windows
        for (ImageWindow win : windows.values())
            if(win.alreadyUsed)
                windows.remove(win.name);

        // (if) Create (else) Update frame
        for (ImageWindow win : windows.values()) {

            if(win.img != null) {

                ImageIcon icon = new ImageIcon(toBufferedImage(win.img));

                if (win.lbl == null) {
                    JFrame frame = createJFrame(win.name, win.flag);
                    JLabel lbl = new JLabel(icon);
                    win.setFrameLabelVisible(frame, lbl);
                } else {
                    win.lbl.setIcon(icon);
                }
            }else{
                System.err.println("Error: no imshow associated with" +
                        " namedWindow: \"" + win.name + "\"");
                return pressedKey;
            }
        }

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

    private static void waitKeyError() {
    }

    public static void destroyWindow(String winname) {
        ImageWindow tmpWin = windows.get(winname);
        if(tmpWin != null)
            windows.remove(winname);
    }

    public static void destroyAllWindows() {
        windows.clear();
    }

    public static void resizeWindow(String winname, int width, int height) {
        ImageWindow tmpWin = windows.get(winname);
        if(tmpWin != null)
            tmpWin.setNewDimension(width, height);
    }
}
