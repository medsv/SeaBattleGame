
package seabattlegame;
import java.awt.Point;
public abstract class AbstractComputerGuess implements MakeGuess{
    protected final PlayingField playingField; //класс Игрового поля
    protected final int width;//ширина игрового поля (например 10)
    protected final int height;//высота игрового поля (например 10)
    protected Point lastGuess; //искомый ход
    protected GuessResult lastGuessResult;//результат последнего хода

    public AbstractComputerGuess (PlayingField playingField){
        this.playingField=playingField;
        width=playingField.getWidth();
        height=playingField.getHeight();
        lastGuessResult=GuessResult.OUT;
    }

    @Override
    abstract public Point getGuess();

    @Override
    //данный метод переопределять не нужно,
    //он используется при получении хода от GUI
    public boolean isGuessMade() {
        return true;
    }
}

