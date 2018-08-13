/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

/**
 *
 * @author sergey
 */
public class PlayingFieldStatistics {
    private final PlayingField playingField;
    private final CellState[][] playingFieldState;
    private int hittedDeckCount, shotCount, nearAreaCount;
    private final int totalFreeCellCount;
    private final int totalDeckCount;
    private final int totalShipCount;
    private final int totalCellCount;
    private final int totalEdgeCellCount;
    private int availableEdgeCellCount;
    private int availableCellCount;
    private final int width;
    private final int height;

    public PlayingFieldStatistics(PlayingField playingField){
        this.playingField=playingField;
        playingFieldState=playingField.getPlayingFieldState();
        totalFreeCellCount=playingField.getGameConf().getTotalFreeCellCount();
        totalDeckCount=playingField.getGameConf().getTotalDeckCount();
        totalShipCount=playingField.getGameConf().getTotalShipCount();
        width=playingField.getWidth();
        height =playingField.getHeight();
        totalCellCount=width*height;
        totalEdgeCellCount=2*(height+width-2);
        availableCellCount=totalCellCount;        
        availableEdgeCellCount=totalEdgeCellCount;

    }
    
    public int getTotalShipCount() {
        return totalShipCount;
    }
    
    public int getTotalDeckCount() {
        return totalDeckCount;
    }
    public int getTotalFreeCellCount() {
        return totalFreeCellCount;
    }
    
    public int getHittedDeckCount() {
        return hittedDeckCount;
    }

    public int getShotCount() {
        return shotCount;
    }

    public int getNearAreaCount() {
        return nearAreaCount;
    }
    
    public double getAvailableCellRatio(){
        return availableCellCount/(double)width/height;
    }
    
    public double getAvailableEdgeCellRatio(){
        return availableEdgeCellCount/(double)totalEdgeCellCount;
    }
    
    public double getAvailableInnerCellRatio(){
        return (availableCellCount-availableEdgeCellCount)
                /(totalCellCount-(double)totalEdgeCellCount);
    }
    
    
    public void updateStatistics(){
        hittedDeckCount=0;
        shotCount=0;
        nearAreaCount=0;
        for(CellState[] csta: playingFieldState)
            for (CellState cst:csta)
                switch(cst){
                    case HITTEDDECK : {
                        hittedDeckCount++;
                        break;
                    }
                    case SHOT : {
                        shotCount++;
                        break;
                    }
                    case NEARAREA : {
                        nearAreaCount++;
                        break;
                    }
                }
        availableCellCount=width*height-shotCount-nearAreaCount;
        availableEdgeCellCount=0;
        for(int i=0;i<height;i++){
            if (i==0 || i== height-1){
                for (int j=0;j<width;j++)
                    if(playingField.isShotAvailableCell(j,i))availableEdgeCellCount++;
            }
            else{
                if(playingField.isShotAvailableCell(0,i)) availableEdgeCellCount++;
                if(playingField.isShotAvailableCell(width-1,i))availableEdgeCellCount++;                            
            }
        }
    }
}


