package seabattlegame;
import java.awt.Point;


/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class Player
{

    private GameConf gameConf;
    private PlayingField ownPlayingField;
    private PlayingField opponentPlayingField;
    private String name;
    private String nameScore;
//	private ArrayList <Point> shotAvailableCells;
    private MakeGuess guess;
    private boolean winner=false;
    private int victoryCount;

    
	
//    public PlayingField playingField;



    //Конструктор
    public Player(String name) {
            this.name=name;
            nameScoreCreation();
            System.out.println(name);

    }
    
    public void initialize(GameConf gameConf){
        this.gameConf=gameConf;
        ownPlayingField=new PlayingField(gameConf);
        initialize(ownPlayingField);
    }
    
    public void initialize(PlayingField playingField){
        ownPlayingField=playingField;
        winner=false;
    }
        
    public void setMakeGuessInterface(MakeGuess guess)
    {
            this.guess=guess;
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     * @generated
     * @ordered
     */

    public void setName(String parameter) {
            // TODO implement me
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     * @generated
     * @ordered
     */

    public String getName() {
            // TODO implement me
            return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     * @generated
     * @ordered
     */

    public PlayingField getPlayingField() {
            // TODO implement me
            return ownPlayingField;
    }



    public void setOpponentPlayingField(PlayingField opponentPlayingField) {
            this.opponentPlayingField=opponentPlayingField;
    }

    public PlayingField getOpponentPlayingField(){
            return opponentPlayingField;
    }

    public Point getGuess()
    {
            return guess.getGuess();
    }

    public GuessResult checkGuess(Point p)
    {
            return opponentPlayingField.checkGuess(p);
    }

    /*	public void setGuessResult(GuessResult guessResult){
    guess.setGuessResult(guessResult);
    }*/

    public void setWinner(boolean b) {
        winner=true;
        victoryCount++;
        nameScoreCreation();
    }
    public boolean isWinner(){
        return winner;
    }
    public int getVictoryCount(){
        return victoryCount;
    }

    private void nameScoreCreation() {
        nameScore=name +" (" + victoryCount +")";
        
    }
    public String getNameScore(){
        return nameScore;
    }

    boolean isGuessMade() {
        return guess.isGuessMade();
    }

}

