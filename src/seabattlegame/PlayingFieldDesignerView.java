/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;


import java.util.Arrays;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author sergey
 */
class PlayingFieldDesignerView {
    private final Controller controller;
    private PlayingFieldDesigner playingFieldDesigner;
    private final int INSET=0; //отступы от краёв окна до GridPane
    private final int GAP=5; //расстояние между соседними ячейками
    private final int MINGAMECONFINDEX=3; //самое маленькое поле - 3-х палубный линкор
    private final int MAXGAMECONFINDEX=9; //самое маленькое поле - 9-ти палубный линкор
    private final Stage primaryStage;
    private final Canvas field;
    private Button buttonPlay,buttonHelp;
    private VBox root;
    private HBox hbox;
    private Scene scene;
    private MenuBar menuBar;
    private Menu menuGameConfig;
    private MenuItem gameConfigArray[];
    private MenuItem exit;
//    private Label prompt;

    PlayingFieldDesignerView(Controller controller) {
        this.controller=controller;
        this.primaryStage=(Stage)controller.getPrimaryStage();
        playingFieldDesigner=new PlayingFieldDesigner(this);
        this.field=playingFieldDesigner.getView();
        Initialaize();
    }

    private void Initialaize() {
//        primaryStage.close(); 
        root = new VBox(); //для того, чтобы меню хорошо смотрелось
        menuBar = new MenuBar();
        menuGameConfig = new Menu("Настройки");
        gameConfigArray=new MenuItem[MAXGAMECONFINDEX-MINGAMECONFINDEX+1];
 
        gameConfigArray[0]=new MenuItem("7х7, 6 кораблей, 3 палубы");
        gameConfigArray[1]= new MenuItem("10х10, 10 кораблей, 4 палубы");
        gameConfigArray[2]= new MenuItem("13х13, 15 кораблей, 5 палуб");
        gameConfigArray[3]= new MenuItem("17х17, 21 корабль, 6 палуб");
        gameConfigArray[4]= new MenuItem("20х20, 28 кораблей, 7 палуб");
        gameConfigArray[5]= new MenuItem("24х24, 36 кораблей, 8 палуб");
        gameConfigArray[6]= new MenuItem("29х29, 45 кораблей, 9 палуб");
        
        gameConfigArray[0].setOnAction(e->setNewGameConf(MINGAMECONFINDEX));
        gameConfigArray[1].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+1));
        gameConfigArray[2].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+2));
        gameConfigArray[3].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+3));
        gameConfigArray[4].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+4));
        gameConfigArray[5].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+5));
        gameConfigArray[6].setOnAction(e->setNewGameConf(MINGAMECONFINDEX+6));
        SeparatorMenuItem sep1=new SeparatorMenuItem();
        MenuItem aboutAuthor =new MenuItem("Об авторе...");
        aboutAuthor.setOnAction(e->aboutAuthor());
        SeparatorMenuItem sep2=new SeparatorMenuItem();
        exit=new MenuItem("Выход");
        exit.setOnAction(e->Platform.exit());
        menuBar.getMenus().addAll(menuGameConfig);
        menuGameConfig.getItems().addAll(Arrays.asList(gameConfigArray));
        menuGameConfig.getItems().addAll(sep1,aboutAuthor,sep2,exit);
        buttonPlay=new Button("Играть");
        buttonHelp=new Button("Помощь");
        buttonHelp.setOnAction(e->showPrompt());
        buttonPlay.setOnAction(e->startNewGame());
        //Fire Button's onAction with Enter in JavaFX
        //https://stackoverflow.com/questions/25758782/fire-buttons-onaction-with-enter-in-javafx
        buttonPlay.defaultButtonProperty().bind(buttonPlay.focusedProperty());
        buttonHelp.defaultButtonProperty().bind(buttonHelp.focusedProperty());
        hbox=new HBox();
        hbox.getChildren().addAll(buttonPlay,buttonHelp);
        hbox.setSpacing(30);
        hbox.setAlignment(Pos.CENTER);
        
  //      root.setVgap(GAP); 
//        root.setPadding(new Insets(INSET)); 

    //    root.setGridLinesVisible(true); 
//        root.add(prompt, 0, 0);
//        GridPane.setHalignment(hbox,HPos.CENTER);
//        root.add(field, 0, 1);
//        root.add(hbox, 0, 2);

        VBox vbox=new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(GAP);
        vbox.setStyle("-fx-padding: 5;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: GREY;");
        
        vbox.getChildren().addAll(field,hbox);
        root.getChildren().addAll(menuBar,vbox);
        
      
//root.setGridLinesVisible(true);        
        scene=new Scene(root);
        primaryStage.setMinWidth(250.); //Чтобы влезал заголовок
    }
    private void setNewGameConf(int longestShipDeckCount){
        controller.setCurrentGameConf(longestShipDeckCount);
    }
    
    public void show(){
        manageMenu();
        primaryStage.close();//если это не сделать stage не уменьшится до размеров scene
        primaryStage.setTitle("Расстановка кораблей");        
        primaryStage.setScene(scene);         
        primaryStage.show();
       
    }
    
    private void manageMenu(){
        for(MenuItem gameConfig:gameConfigArray) 
        Arrays.asList(gameConfigArray).forEach(g->g.setDisable(false));
        gameConfigArray[controller.getCurrentGameConf().getLongestShipDeckCount()-MINGAMECONFINDEX].setDisable(true);        
        
    }
    
    private void showPrompt(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Расстановка кораблей");

        alert.setHeaderText("Перемещайте корабли с помощью мышки.\nДвойной щелчок - поворот корабля.");
        alert.initOwner(primaryStage);
        alert.showAndWait();
        
        
    }
    
//    public EnableButtonListener getEnableButtonListener(){
//        return new EnableButtonListener() {
//            @Override
//            public void enableButton(boolean enableButton) {
//                buttonPlay.setDisable(!enableButton);
//            }
//        };
//    }

//    void addStartNewGameEventHandler(EventHandler<ActionEvent> startNewGameEventHandler) {
//        buttonPlay.setOnAction(startNewGameEventHandler);
//    }

    private void aboutAuthor() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Об авторе");
        alert.setHeaderText("Сергей Медведев\njavaseabattlegame@yandex.ru");
        alert.initOwner(primaryStage); 
        alert.showAndWait();

    }

    private void startNewGame() {
        playingFieldDesigner.placeShipsOnPlayingField();
        controller.startNewGame();
    }

    void setPlayingField(PlayingField manPlayingField) {
        playingFieldDesigner.setPlayingField(manPlayingField);
    }

    void repaintView() {
        playingFieldDesigner.repaintView();
    }

    void enableButton(boolean enableButton) {
        buttonPlay.setDisable(!enableButton);
    }


    
}
