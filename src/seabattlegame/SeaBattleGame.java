/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;


import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author sergey
 */
public class SeaBattleGame extends Application implements Controller {

    private MainView mainView;

    private PlayingFieldDesignerView playingFieldDesignerView;
//    private PlayingFieldView computerPlayingFieldView;
//    private PlayingFieldView manPlayingFieldView;
    private SeaBattleModel seaBattleModel;

    private GameConf gameConf;
//    private MakeGuessInterface getManGuessByMouseClick;
    private boolean oneMoreGame;
    private boolean gameOver=true;
    private Stage primaryStage;
    private Point guess;
//    private ScoreBoard manScoreBoard; //табло результатов
//    private ScoreBoard computerScoreBoard; //табло результатов
//    private int curGameConfIndex;

   
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        //Специально располагаем первыми однопалубные корабли, затем по возрастанию чтобы проверить работу Comparator в SeaBattleModel
//	gameConf= new GameConf(10,10, new ArrayList<>(Arrays.asList
//	(new Point[] {new Point(1,4), new Point(2,3), new Point(3,2), new Point(4,1)}))); 
//        curGameConfEnum=GameConfEnum.NORMAL;
//        setCurrentGameConf(GameConfEnum.NORMAL);        
        seaBattleModel = new SeaBattleModel (this);
        playingFieldDesignerView=new PlayingFieldDesignerView(this);
        mainView=new MainView(this);
        seaBattleModel.setMakeManGuessInterface(mainView);
//        playingFieldDesigner.addEnableButtonListener(playingFieldDesignerView.getEnableButtonListener());
//        playingFieldDesignerView.addStartNewGameEventHandler (new StartNewGameEventHandler());       

        setCurrentGameConf(4);        
        startDesignPlayingField();
//       startNewGame();
    }
    
    
    private void startDesignPlayingField(){
        seaBattleModel.initialize();
        playingFieldDesignerView.setPlayingField(getManPlayingField());
        playingFieldDesignerView.repaintView();
        playingFieldDesignerView.show();
    }
            
    
    @Override
    public void startNewGame(){
        mainView.startNewGame();
        
    
        mainView.setManName(seaBattleModel.getManName());
        mainView.setComputerName(seaBattleModel.getComputerName());
        mainView.repaintManView();
        mainView.repaintComputerView();
        mainView.show();
        seaBattleModel.startGame();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public GameConf getGameConf() {
        return gameConf;
    }



//    MakeGuessInterface getManGuess() {
//        return getManGuessByMouseClick;
//    }


    public void gameOver(String str) {
        try {
            //        gameOver=true;
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(SeaBattleGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Результат игры");
        alert.setHeaderText(str);
        alert.initOwner(primaryStage); 
        alert.showAndWait();
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK)oneMoreGame=true;
//        else oneMoreGame=false; 
//        
//        if (oneMoreGame) {
//            newGame();
//        }
          startDesignPlayingField();

//      startNewGame();
    }

//    @Override
//    public void setClickedCellCoords(Point clickedCellCoords) {
//        System.out.println(clickedCellCoords);
//        guess=clickedCellCoords;
//        seaBattleModel.controlGame();
//
//    }


    @Override
    public Object getPrimaryStage() { //Object для универсальности
        return primaryStage;
    }

//    public Canvas getPlayingFieldDesignerCanvas() {
//        return playingFieldDesigner.getView();
//    }
    
    public SeaBattleModel getSeaBattleModel() {
        return seaBattleModel;
    }

    @Override
    public void setCurrentGameConf(int longestShipDeckCount){
        gameConf=new GameConf(longestShipDeckCount);
        startDesignPlayingField();
    }
    
    
//    public void setCurrentGameConf(GameConfEnum newGameConfEnum){
//        ArrayList<Point> ships=null;
//        int height=0, width=0;
//        if (curGameConfEnum==newGameConfEnum) return;
//        curGameConfEnum=newGameConfEnum;
//        switch  (curGameConfEnum){
//            case  SMALL :
//            ships=new ArrayList<>(Arrays.asList (new Point[] { 
//                new Point(3,1), new Point(2,2), new Point(1,3)})); 
//                width=7;
//                height=7;
//                break;
//            
//            case  NORMAL :
//            ships=new ArrayList<>(Arrays.asList (new Point[] {new Point(4,1), 
//                new Point(3,2), new Point(2,3), new Point(1,4)})); 
//                width=10;
//                height=10;
//                break;
//            case  LARGE :
//            ships=new ArrayList<>(Arrays.asList (new Point[] {new Point(5,1),new Point(4,2), 
//                new Point(3,3), new Point(2,4), new Point(1,5)})); 
//                width=13;
//                height=13;
//                break;
//                
//        }
//        gameConf= new GameConf(width,height,ships);
//        startDesignPlayingField();
//        
//    }
    
    @Override
    public GameConf getCurrentGameConf(){
        return gameConf;
    }
    
//    public class StartNewGameEventHandler implements EventHandler<ActionEvent>{
//
//        @Override
//        public void handle(ActionEvent event) {
//            playingFieldDesigner.placeShipsOnPlayingField();
//            startNewGame();
//        }
//    
//    }
    
    public GameView getMainView() {
        return (GameView) mainView;
    }

    @Override
    public PlayingField getManPlayingField() {
        return seaBattleModel.getManPlayingField();
    }

    @Override
    public PlayingField getComputerPlayingField() {
        return seaBattleModel.getComputerPlayingField();
    }

    @Override
    public void controlGame() {
        seaBattleModel.controlGame();
    }

   
}
