/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author sergey
 */
public class GetGuessBlockTest1 extends Application {
    private Point point = new Point();
    private boolean guessDone;
    private Canvas view;

    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.close();
        view = new Canvas();
        view.setWidth(500);
        view.setHeight(500);
        
        HBox root = new HBox();
        root.getChildren().add(view);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("getGuess invocation");
//        while (point.x<95 || point.y<95){
            getGuess();
//        }
    }

    void getGuess () {
        System.out.println("getGuess start");
        Thread t = new Thread(new BlockGetGuess ());
        t.start();
        System.out.println("getGuess before join");
        try {
            t.join();
        } catch (InterruptedException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        System.out.println("getGuess exit");
    }


    class BlockGetGuess implements Runnable{
        @Override
        public void run() {
            System.out.println("Thread has started");
            view.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyMouseEventHandler ());
            while(!guessDone){
                Thread.yield();
            }
            guessDone=false;
        }
        
        class MyMouseEventHandler implements EventHandler <MouseEvent>{
            @Override
            public void handle(MouseEvent event) {
                point.x=(int) event.getX();
                point.y=(int) event.getY();
                guessDone=true;
                System.out.println(point);
            }
        }
    }
}