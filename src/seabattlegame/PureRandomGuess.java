/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;

/**
 *
 * @author sergey
 */
public class PureRandomGuess extends AbstractComputerGuess{
//    protected PlayingField playingField;

//    protected Point lastGuess;

    public PureRandomGuess(PlayingField playingField){
        super(playingField);
    }

//        @Override
//	public void setGuessResult(GuessResult guessResult){ 
//            lastGuessResult=guessResult;
//	}    

    @Override
    public Point getGuess() {
        return getPureRandomGuess();
    }

    protected Point getPureRandomGuess() {
        lastGuess=playingField.getRandomShotAvailableCell();
        return lastGuess; 	
    }

    @Override
    public boolean isGuessMade() {
        return true;
    }
       
}
