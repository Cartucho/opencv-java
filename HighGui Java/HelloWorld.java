import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

class MatchTemplateDemoRun{

    public void run(String[] args) {

        //empty_image_test(); // Throws error of empty image

        //dont_show_without_creating_windows_test(); // no imshow, throw waitkey error

        //dont_show_without_waitKey_test(args); // no waitkey => no waiting and displaying

        //close_window_by_pressing_key_test(args); // press ANYKEY to close the window

        //show_10_images_test(args);

        //show_image_in_two_windows_with_different_titles_test(args); // creates two windows with different names with the same pic

        //multiple_imshow_for_single_waitKey_test(args); // close windows one by one pressing the x or close all with ANYKEY

        //show_image_for_1000_milisec_test(args); // show image for 1 sec

        //show_another_image_after_keyPressed_test(args); // close and open a new window

        //show_another_image_after_keyPressed_in_same_window_test(args); // same name => same window

        //show_camera_test(); // shows user camera frames

        //destroy_window_test(args); // destroys color image showing only the gray one

        //destroy_all_windows_test(args); // We destroy all windows so nothing should be shown and waitKey throws an error

        //create_empty_window_using_namedWindow_test(); // throws error, we need a imshow to show an image

        //create_window_using_namedWindow_test(args); // creating a window using namedWindow

        //resize_window_test(args); // resize the window and the image if needed (keeping aspect ratio)
        
        //move_window_test(args); // move window to the right after keyPressed

        //show_two_images_in_different_positions_test(args);

        //mouse_call_back_test(args);

        System.out.println("Done!");
        System.exit(0);
    }

    /*
    private void mouse_call_back_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );

        HighGui.namedWindow("Window 1", HighGui.WINDOW_NORMAL);

        //set the callback function for any mouse event
        HighGui.setMouseCallback("ImageDisplay", CallBackFunc, null);
        //http://opencvexamples.blogspot.com/2014/01/detect-mouse-clicks-and-moves-on-image.html
        //http://docs.opencv.org/2.4/modules/highgui/doc/user_interface.html

        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }*/

    private void show_10_images_test(String[] args) {

        // load and display one image after the other has been closed: repeat 10x
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i);
            Mat img = Imgcodecs.imread(args[0], Imgcodecs.IMREAD_COLOR);
            String winName = "Window #";
            winName += i;
            HighGui.imshow(winName, img);
            HighGui.waitKey(0);
        }

        System.out.println("Done!");
        System.exit(0);
    }

    private void show_two_images_in_different_positions_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );

        HighGui.namedWindow("Window 1", HighGui.WINDOW_NORMAL);
        HighGui.moveWindow("Window 1", 0, 0);
        HighGui.imshow("Window 1", img);

        img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_GRAYSCALE );
        HighGui.namedWindow("Window 2", HighGui.WINDOW_NORMAL);
        HighGui.moveWindow("Window 2", 400, 400);
        HighGui.imshow("Window 2", img);

        HighGui.waitKey(0);
    }

    private void move_window_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.namedWindow("Window 1");
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
        HighGui.moveWindow("Window 1", 200, 20);
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }

    private void resize_window_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.namedWindow("Window 1", HighGui.WINDOW_NORMAL);
        HighGui.resizeWindow("Window 1", 500, 600);
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }

    private void create_window_using_namedWindow_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.namedWindow("Window 1", HighGui.WINDOW_NORMAL);
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }

    private void create_empty_window_using_namedWindow_test() {
        HighGui.namedWindow("Window 1", HighGui.WINDOW_NORMAL);
        HighGui.waitKey(0);
    }

    private void destroy_all_windows_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Color", img);
        img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_GRAYSCALE );
        HighGui.imshow("Gray", img);
        HighGui.destroyAllWindows();
        HighGui.waitKey(0);
    }

    private void destroy_window_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Color", img);
        img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_GRAYSCALE );
        HighGui.imshow("Gray", img);
        HighGui.destroyWindow("Color");
        HighGui.waitKey(0);
    }

    private void show_camera_test() {
        /// Video!
        VideoCapture camera = new VideoCapture(0); // open the default camera

        Mat frame = new Mat();
        camera.read(frame);

        if(!camera.isOpened()) {  // check if we succeeded
            System.err.println("Error opening camera");
        }
        else {
            while(true){
                camera.read(frame); // get a new frame from camera
                HighGui.imshow("Press Key to close", frame);
                if(HighGui.waitKey(40) >= 0) break;
            }
        }
        camera.release();
    }

    private void show_another_image_after_keyPressed_in_same_window_test(String[] args) {
        // Show image in RGB
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);

        // Show image in GrayScale
        img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_GRAYSCALE );
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }

    private void show_another_image_after_keyPressed_test(String[] args) {
        // Show image in RGB
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Colour", img);
        HighGui.waitKey(0);

        // Show image in GrayScale
        img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_GRAYSCALE );
        HighGui.imshow("Gray", img);
        HighGui.waitKey(0);
    }

    private void show_image_for_1000_milisec_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(1000);
    }

    private void show_image_in_two_windows_with_different_titles_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
        HighGui.imshow("Window 2", img);
        HighGui.waitKey(0);
    }

    private void multiple_imshow_for_single_waitKey_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
        HighGui.imshow("Window 2", img);
        HighGui.waitKey(0);
    }

    private void close_window_by_pressing_key_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
        HighGui.waitKey(0);
    }

    private void dont_show_without_waitKey_test(String[] args) {
        Mat img = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );
        HighGui.imshow("Window 1", img);
    }

    private void dont_show_without_creating_windows_test() {
        HighGui.waitKey(0);
    }

    private void empty_image_test() {
        Mat emptyImg = new Mat();
        HighGui.imshow("Window 1", emptyImg);
    }
}

public class HelloWorld
{
    public static void main(String[] args) {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // run code
        new MatchTemplateDemoRun().run(args);
    }
}
