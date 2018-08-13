package seabattlegame;

import java.awt.Point;


/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class SeaBattleModel 
{

	
    private final Player man;
    private final Player computer;
    private boolean gameIsOver;
    private GameConf gameConf;
    private final SeaBattleGame controller;
//    private MakeGuess manGuessInterface;
//    private MakeGuess computerGuessInterface;
    private boolean manTurn;
    private Player activePlayer; //игрок чья очередь делать ход
    private boolean firstGuess;



    public SeaBattleModel(SeaBattleGame aThis) {
        controller=aThis;
//        gameConf=controller.getGameConf();  
        //сортируем shipTypes по убыванию количества палуб        
//        Collections.sort(gameConf.getShipTypes(), (p1,p2)->p2.x-p1.x); 
        man=new Player("Игрок");
        computer=new Player("Компьютер");    
    
    }



    //Создание основных объектов
    public void initialize(){
        gameConf=controller.getGameConf();  
//        for(Point p : gameConf.getShipTypes()) System.out.println(p);
        man.initialize(gameConf);
        computer.initialize(gameConf);
        man.setOpponentPlayingField(computer.getPlayingField());
        computer.setOpponentPlayingField(man.getPlayingField());


        man.getPlayingField().setShipsPlacing(new RandomShipsPlacing(gameConf));
        man.getPlayingField().shipsPlacing();
        man.getPlayingField().fitNearArea();
//        man.getPlayingField().displayShips(true); не нужно, т.к. это сделано в конструкторе поля
        computer.getPlayingField().setShipsPlacing(new ComputerShipsPlacing(gameConf));
        computer.getPlayingField().shipsPlacing();
        computer.getPlayingField().fitNearArea();
//        computer.getPlayingField().displayShips(true); //для тестирования

    }
    
    public void startGame(){
        firstGuess=true;
        gameIsOver=false;
        
        computer.setMakeGuessInterface(new ComputerGuess(computer.getOpponentPlayingField()));
        
        
	manTurn = (Math.round(Math.random())==1); //чей ход? true - человек
        //Тест!!!!
//        manTurn=false;
        
        controller.getMainView().updateViews();
//        if (manTurn) return; //если ходит человек, то выходим и ждём щелчка мыши
        controlGame();
        
    }
    public void setMakeManGuessInterface(MakeGuess makeManGuess){
        man.setMakeGuessInterface(makeManGuess);    
    }
    
    public void controlGame(){
        
        Point p;
//        boolean manTurn=true; //для теста
//для устранения быстрого кликанья мышью. Не работают
//        if (!firstGuess && manTurn) return; //
        
        while(!gameIsOver){
            
            activePlayer = manTurn ? man : computer;

            if (activePlayer.isGuessMade()) p=activePlayer.getGuess();
            else return;
            
//            if (p==null) {
//                Thread.yield();
//                return;
//            }
            System.out.println("ход сделан"+p);

/*				System.out.println(p);
            System.out.println(activePlayer);
            System.out.println(activePlayer.getOpponentPlayingField());*/
            System.out.println("Ход: ("+p.x+","+p.y+")");
            GuessResult gr= activePlayer.checkGuess(p);

            /*activePlayer.setGuessResult(gr);*/
            System.out.println(gr);
            
            controller.getMainView().updateViews();


            switch (gr){
                case OUT:
                case OUT2:
                case HIT2:
                        System.out.println("Мимо. Переход хода");
                        manTurn=!manTurn;
                        System.out.println("manTurn="+manTurn);
                        break;
                case HIT:
                        System.out.println("Ранен!");
                        break;
                case GAMEOVER: //включает в себя DESTROY поэтому нет break
                        gameIsOver=true;
                        activePlayer.setWinner(true);
                case DESTROY:
                        System.out.println("Убит!");
                        break;
                default: throw new IllegalArgumentException();
                
            }
//            if (manTurn && !gameIsOver) return; //ждём щелчка мыши - хода игрока

        }
        
      
        String str;
        if (man.isWinner())str="Поздравляю, Вы выиграли!";
        else {
            str="К сожалению Вы проиграли.";
            computer.getPlayingField().displayShips(true);
            
            //достаточно обновить поле компьютера, но почему-то иногда поле игрока при выигрыше компьютера не обновлялось
//            controller.getMainView().updateComputerView();

        }
        controller.getMainView().updateViews();  
        
        str+="\nСчёт: "+man.getVictoryCount()+"-"+computer.getVictoryCount();
        controller.gameOver(str);
//        initialize();
//        startGame();
    }

   
    public PlayingField getManPlayingField(){
        return man.getPlayingField();
    }
            
    public PlayingField getComputerPlayingField(){
        return computer.getPlayingField();
    }

    public boolean isManWin() {
        return man.isWinner();
    }
    public String getManName(){
        return man.getNameScore();
    }
    
    public String getComputerName(){
        return computer.getNameScore();
    }

}







