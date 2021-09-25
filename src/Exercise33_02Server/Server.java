package Exercise33_02Server;

import javafx.application.Application;
import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Text area fro displaying contents
        TextArea textArea = new TextArea();

        //Create a scene and stage
        Scene scene = new Scene(new ScrollPane(textArea), 450, 200);
        primaryStage.setTitle("Exercise33_02Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread( () -> {
            try {
                //Create a server socket
                ServerSocket serverSocket = new ServerSocket(5000);
                Platform.runLater( () -> textArea.appendText("Exercise33_02Server started at "
                + new Date()+ "\n"));

                //Listen for a connection request
                Socket socket = serverSocket.accept();
                Platform.runLater( () -> textArea.appendText("Connected to a client at "+ new Date()+"\n"));

                //Create data input and output streams
                DataInputStream inputStreamWeight = new DataInputStream(socket.getInputStream());
                DataInputStream inputStreamHeight = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                while (true){
                    //Receive height and weight from client
                    double weight = inputStreamWeight.readDouble();
                    double height = inputStreamHeight.readDouble();

                    //Compute bmi by creating an instance of the BMI class
                    BMI bmi = new BMI(weight,height);

                    //Send BMI to client
                    outputStream.writeDouble(bmi.getBMI());
                    outputStream.writeUTF(bmi.getStatus());

                    Platform.runLater( () -> textArea.appendText("Weight:"+weight+"\n"+
                            "Height: "+ height+"\n"+
                            "BMI is "+ bmi.getBMI()+". "+bmi.getStatus()));
                }

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }).start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
