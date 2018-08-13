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
public class PlayingFieldView extends PlayingFieldTemplate{
    
    final double SHOTSIZE=0.6; //относительный размер круга обозначающего выстрел
//    final double SHOTSIZE=1.0; //относительный размер круга обозначающего выстрел
    protected CellState playingFieldState[][];
    

    
    @Override
    public void setPlayingField(PlayingField playingField){
        super.setPlayingField(playingField);
        playingFieldState=playingField.getPlayingFieldState();

    }

   
    protected void drawCellsState(){
//        System.out.println("xCount="+xCount + "yCount= "+ yCount);
        for (int i=0;i<xCount;i++){ 
            for (int j=0;j<yCount;j++){ 
//                System.out.println("i="+i + "j= "+ j);
                drawCell(i, j);   
               } 
            } 

    }
    
    public void drawCell(int i, int j){ 
    
        double curX, curY; 
        curX=i*xStep;
        curY=j*yStep;
//    System.out.println(curX + " "+ curY);

        switch (playingFieldState[j][i]){ 

            case DECK:  
                gc.setFill(deckColor); 
                gc.fillRect(curX,curY,xStep,yStep); 
                break; 
            case HITTEDDECK: 
                gc.setFill(hittedDeckColor); 
                gc.fillRect(curX,curY,xStep,yStep); 
                break; 
            case SHOT: 
                gc.setFill(shotColor); 
                gc.fillOval(curX+xStep/2*(1-SHOTSIZE),curY+yStep/2*(1-SHOTSIZE),
                        xStep*SHOTSIZE,yStep*SHOTSIZE); 
                break; 
                
            case NEARAREA: 
                gc.setFill(nearAreaColor); 
                gc.fillRect(curX+xStep/2*(1-SHOTSIZE),curY+yStep/2*(1-SHOTSIZE),
                        xStep*SHOTSIZE,yStep*SHOTSIZE); 
//                gc.fillRect(curX,curY,xStep,yStep); 
//                break; 
        } 
    }
    
    @Override
    public void repaintView(){
        super.repaintView();
        drawCellsState();
        drawLastGuess();
        
    }
    
    @Override
    public void updateView(){
        super.updateView();
        drawCellsState();
        drawLastGuess();
        
    }
    private void drawLastGuess(){
        Point p=playingField.getLastGuess();
        double curX, curY; 
 
        
        if (p!=null){
            curX=p.x*xStep;
            curY=p.y*yStep;
            gc.setFill(lastShotColor);
            switch (playingFieldState[p.y][p.x]){
                case HITTEDDECK:
                    gc.fillRect(curX,curY,xStep,yStep);
                    break;
                default:
                    gc.fillOval(curX+xStep/2*(1-SHOTSIZE),curY+yStep/2*(1-SHOTSIZE),
                            xStep*SHOTSIZE,yStep*SHOTSIZE); 
                    
            }
        }
    }
}
