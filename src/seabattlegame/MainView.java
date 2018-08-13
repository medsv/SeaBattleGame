/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author sergey
 */
public class MainView implements MakeGuess, GameView {
    final int INSERT=10;
    final int GAP=5;
    Stage primaryStage;
    private final Canvas manField;
    private final Canvas computerField;
    private final PlayingFieldTemplate manPlayingFieldView;
    private final PlayingFieldTemplate computerPlayingFieldView;
    Label manName; 
    Label computerName; 
    Scene scene;
    private final ScoreBoard manScoreBoard; 
    private final ScoreBoard computerScoreBoard;
    private final Canvas manBoard;
    private final Canvas computerBoard;
    private boolean guessMade;
    private Point guess;
    private Controller controller;
//    private SeaBattleModel seaBattleModel;
    
//    GetManGuessByMouseClick getManGuess;
    
    public MainView(Controller controller){
        this.controller=controller;
        primaryStage=(Stage) controller.getPrimaryStage();
//        seaBattleModel=controller.getSeaBattleModel();
        computerPlayingFieldView = new PlayingFieldView();
//        computerPlayingFieldView.setGetClickedCellCoords(this);        
        manPlayingFieldView = new PlayingFieldView();
        manScoreBoard=new ScoreBoard();
        computerScoreBoard=new ScoreBoard();
        
        manField=manPlayingFieldView.getView();
        computerField=computerPlayingFieldView.getView();
        manBoard=manScoreBoard.getView();
        computerBoard=computerScoreBoard.getView();
//        guess=new Point();
        computerPlayingFieldView.setMouseEventHandler(new MouseEventHandler());

        computerScoreBoard.setDeckColor(computerPlayingFieldView.getDeckColor());
        computerScoreBoard.setHittedDeckColor(computerPlayingFieldView.getHittedDeckColor());
        computerScoreBoard.setShotColor(computerPlayingFieldView.getShotColor());
        
        manScoreBoard.setDeckColor(manPlayingFieldView.getDeckColor());
        manScoreBoard.setHittedDeckColor(manPlayingFieldView.getHittedDeckColor());
        manScoreBoard.setShotColor(manPlayingFieldView.getShotColor());
        
        
        Initialaize();
    }

  

    private void Initialaize() {
//        getManGuess = new GetManGuessByMouseClick(); 

 

//        Canvas manField=new Canvas(xCount*CELLSIZE,yCount*CELLSIZE); 
//        Canvas computerField=new Canvas(xCount*CELLSIZE,yCount*CELLSIZE);
//        computerField.addEventHandler(MouseEvent.MOUSE_CLICKED,getManGuess);

//        drawGrid(manField,xCount,yCount);
//        drawGrid(computerField,xCount,yCount);  

        manName = new Label(""); 
        manName.setFont(Font.font(20));
        
        computerName = new Label("");
        computerName.setFont(Font.font(20));

//        GridPane root = new GridPane(); 
//        root.setAlignment(Pos.CENTER);
//        root.setHgap(4*GAP); 
//        root.setVgap(GAP); 
//        root.setPadding(new Insets(INSERT)); 
//    //    root.setGridLinesVisible(true); 
//        root.add(manName, 0, 0); 
//        root.add(computerName , 1, 0); 
//        root.add(manField,0,1); 
//        root.add(computerField,1,1); 
////        root.add(manField,0,2); 
////        root.add(computerField,1,2); 
//        
//                root.setStyle("-fx-padding: 5;" +
//                        "-fx-border-style: solid inside;" +
//                        "-fx-border-width: 2;" +
//                        "-fx-border-insets: 5;" +
//                        "-fx-border-radius: 5;" +
//                        "-fx-border-color: GREY;");
//
//        
//        root.add(manBoard,0,2); 
//        root.add(computerBoard,1,2);
        
        
        HBox root=new HBox();
        VBox vboxMan =new VBox();
        VBox vboxComputer =new VBox();
        vboxMan.getChildren().addAll(manName,manField,manBoard);
        vboxComputer.getChildren().addAll(computerName,computerField,computerBoard);
        vboxMan.setSpacing(10);
        vboxComputer.setSpacing(10); 
        vboxMan.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: GREY;");
        vboxComputer.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: GREY;");
        
        
        
        root.setSpacing(10);
        root.setPadding(new Insets(5));
        root.getChildren().addAll(vboxMan,vboxComputer);

        
//root.setGridLinesVisible(true); 
        scene =new Scene(root);

    }

    public void setManName(String str) {
        manName.setText(str);
    }
    public void setComputerName(String str) {
        computerName.setText(str);    
    }
    
    public void show(){
        primaryStage.close();  
        primaryStage.setTitle("Морской бой");        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void repaintManView() {
        manPlayingFieldView.repaintView();
        manScoreBoard.repaintView();
    }

    void repaintComputerView() {
        computerPlayingFieldView.repaintView();
        computerScoreBoard.repaintView();
    }

    @Override
    public void updateManView() {
        manPlayingFieldView.updateView();
        manScoreBoard.updateView();
    }

    @Override
    public void updateComputerView() {
        computerPlayingFieldView.updateView();
        computerScoreBoard.updateView();

    }
    
    @Override
    public void updateViews() {
        updateManView();
        updateComputerView();
    }

    @Override
    public Point getGuess() {
        if (!guessMade) return null;
        guessMade=false;
        return guess;

    }


    @Override
    public boolean isGuessMade() {
        return guessMade;
    }

    void startNewGame() {
        computerPlayingFieldView.setPlayingField(controller.getComputerPlayingField());
        manPlayingFieldView.setPlayingField(controller.getManPlayingField());
        computerScoreBoard.setPlayingField(controller.getComputerPlayingField());
        manScoreBoard.setPlayingField(controller.getManPlayingField());
        computerPlayingFieldView.repaintView();
        manPlayingFieldView.repaintView();
    }

    
    class MouseEventHandler implements EventHandler <MouseEvent>{
        @Override
        public void handle(MouseEvent event) {
            if (guessMade) { //ход компьютера пока не обработан 
                System.out.println("Пустой клик");
                return;
            }
            guess=computerPlayingFieldView.getCellCoords(event.getX(), event.getY());
            System.out.println("Новый клик" + guess);
            guessMade=true;
            controller.controlGame();
        }    
    }
}
