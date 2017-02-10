package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.opencv.core.Core;

public class Main extends Application {

 @Override
 public void start(Stage primaryStage) throws Exception {
  Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
  primaryStage.setTitle("Show Image");
  primaryStage.setScene(new Scene(root, 700, 500));
  primaryStage.show();
 }


 public static void main(String[] args) {

  // load the native OpenCV library
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

  launch(args);
 }
}
