/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author sergey
 */
public class SeaBattleGame extends Application {

    private MainView mainView;
    private PlayingFieldDesignerView playingFieldDesignerView;
    private PlayingFieldDesigner playingFieldDesigner;
    private PlayingFieldView computerPlayingFieldView;
    private PlayingFieldView manPlayingFieldView;
    private SeaBattleModel seaBattleModel;
    private GameConf gameConf;
    private MakeGuessInterface getManGuessByMouseClick;
    private boolean oneMoreGame;
    private boolean gameOver=true;
    private Stage primaryStage;

   
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        //Специально располагаем первыми однопалубные корабли, затем по возрастанию чтобы проверить работу Comparator в SeaBattleModel
	gameConf= new GameConf(10,10, new ArrayList<>(Arrays.asList
	(new Point[] {new Point(1,4), new Point(2,3), new Point(3,2), new Point(4,1)}))); 
        seaBattleModel = new SeaBattleModel (this);
        startNewGame();
        
      
        
    }
    
    
//    private void startDesignPlayingField(){
//        playingFieldDesigner=new PlayingFieldDesigner
//            (this,seaBattleModel.getComputerPlayingField());
//        playingFieldDesignerView=
//    }
            
    
    private void startNewGame(){
        seaBattleModel.initialize();
        System.out.println("seaBattleModel создана");
        
        
        
        computerPlayingFieldView = new PlayingFieldViewGetGuess(seaBattleModel);
        manPlayingFieldView = new PlayingFieldView(seaBattleModel.getManPlayingField());
        //передаём в главное окно ссылки на Canvas
        mainView=new MainView(primaryStage,manPlayingFieldView.getView(),
                computerPlayingFieldView.getView());
        mainView.setManName(seaBattleModel.getManName());
        mainView.setComputerName(seaBattleModel.getComputerName());
        getManGuessByMouseClick=(MakeGuessInterface) computerPlayingFieldView;
        primaryStage.show();
        seaBattleModel.startGame();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    GameConf getGameConf() {
        return gameConf;
    }


    void updateManView() {
        manPlayingFieldView.updateView();
    }
    void updateComputerView() {
        computerPlayingFieldView.updateView();
    }

    MakeGuessInterface getManGuess() {
        return getManGuessByMouseClick;
    }


    public void gameOver(String str) {
//        gameOver=true;
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Результат игры");
        alert.setHeaderText(str);
        alert.showAndWait();
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK)oneMoreGame=true;
//        else oneMoreGame=false; 
//        
//        if (oneMoreGame) {
//            newGame();
//        }
          startNewGame();
    }


    
   
}
