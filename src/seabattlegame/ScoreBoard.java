/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author sergey
 */
public class ScoreBoard extends PlayingFieldView{
    private String hittedDesckCountText, shotCountText, nearAreaCountText, shotNearAreaCountText;
    private int shotNearAreaCount;
    private Font font;
    private double textX;
    private double textY;
    private double fontSize;
    private double startXForBrace;
    private double [] bracePolylineY;
    private double [] bracePolylineX;
    private int totalDeckCount;
    private int totalFreeCellCount;
//    private CellState[][]playingFieldStateData;
    
        
    public ScoreBoard() {
        
//        fontSize=CELLSIZE/5*4.; //24
//        textX=CELLSIZE/5*6.; //36
////        textY=CELLSIZE-(CELLSIZE-fontSize)/2.; //27
//        textY=CELLSIZE/2.; //27
//        font=new Font("Arial",fontSize);
//
//        gc.setTextBaseline(VPos.CENTER); 
//        gc.setFont(font);
//        
//        bracePolylineY=new double[] {CELLSIZE*1.5,CELLSIZE*1.5,CELLSIZE*2.5,CELLSIZE*2.5};
//        bracePolylineX = new double [4];
//        
    }
    
    public void clear(){
        hittedDesckCountText= " - 0"+" из "+totalDeckCount;
        shotCountText= " - 0";
        nearAreaCountText= " - 0";
        
    }
    @Override
    protected void setHeight(){
        yCount=3;
        view.setHeight(yCount*CELLSIZE);
        height=(int)view.getHeight();
    }    

    @Override
    public void repaintView() {
        gc.clearRect(0, 0, width, height);
        drawCell(0, 0);
        drawCell(0, 1);
        drawCell(0, 2);
        updateView();
    }
    
    
    @Override
    public void updateView() {
        updateData();
        gc.clearRect(CELLSIZE, 0, width-CELLSIZE, height);
        gc.setFill(Color.BLACK);     
        gc.fillText(hittedDesckCountText, textX, textY);
        gc.fillText(shotCountText, textX, textY+CELLSIZE);
        gc.fillText(nearAreaCountText, textX, textY+2*CELLSIZE);
        setBracePolylineX();
        gc.strokePolyline(bracePolylineX,bracePolylineY,4);
        gc.fillText(shotNearAreaCountText, startXForBrace+CELLSIZE*1.2, textY+1.5*CELLSIZE);

        
//        gc.strokeText("Drawing Text", 100, 10, 40); 

    }


    @Override
    public void setPlayingField(PlayingField playingField){
        super.setPlayingField(playingField);
//        playingFieldStateData=playingFieldState; //будет использоваться для подсчёта результата
        //изображаться на табло будет playingFieldStateData
        playingFieldState = new CellState[][] {{CellState.HITTEDDECK},{CellState.SHOT},{CellState.NEARAREA}};
//        playingFieldState[0]=new CellState[] {CellState.HITTEDDECK,CellState.SHOT,CellState.NEARAREA};
        totalDeckCount=playingField.getTotalDeckCount();
        totalFreeCellCount=playingField.getTotalFreeCellCount()-totalDeckCount;
        
        fontSize=CELLSIZE/5*4.; //24
        textX=CELLSIZE/5*6.; //36
//        textY=CELLSIZE-(CELLSIZE-fontSize)/2.; //27
        textY=CELLSIZE/2.; //27
        font=new Font("Arial",fontSize);

        gc.setTextBaseline(VPos.CENTER); 
        gc.setFont(font);
        
        bracePolylineY=new double[] {CELLSIZE*1.5,CELLSIZE*1.5,CELLSIZE*2.5,CELLSIZE*2.5};
        bracePolylineX = new double [4];        
        
        clear();
    }

    private void updateData() {
//        playingField.updateStatistics(); обновление происходит в playingField.checkGuess
        shotNearAreaCount=playingField.getShotCount()+playingField.getNearAreaCount();
        shotNearAreaCountText=shotNearAreaCount+ " из "+playingField.getTotalFreeCellCount();
        hittedDesckCountText=" - "+ playingField.getHittedDeckCount() +" из "+totalDeckCount;
        shotCountText=" - "+playingField.getShotCount();
        nearAreaCountText=" - "+playingField.getNearAreaCount();
        
    }
//    private void updateData() {
//        hittedDesckCount=0;
//        shotCount=0;
//        neaAreaCount=0;
//        shotNeaAreaCount=0;
//        for (int i=0;i<playingFieldStateData.length;i++)
//            for (int j=0; j<playingFieldStateData[0].length;j++){
//
//                switch(playingFieldStateData[i][j]){
//                    case HITTEDDECK : {
//                        hittedDesckCount++;
//                        hittedDesckCountText=" - "+ hittedDesckCount +" из "+totalDeckCount;
//                        break;
//                    }
//                    case SHOT : {
//                        shotCount++;
//                        shotCountText=" - "+shotCount;
//                        break;
//                    }
//                
//                    case NEARAREA : {
//                        neaAreaCount++;
//                        nearAreaCountText=" - "+neaAreaCount;
//                        break;
//                    }
//                }
//            }
//        shotNeaAreaCount=shotCount+neaAreaCount;
//        shotNeaAreaCountText=shotNeaAreaCount+ " из "+totalFreeCellCount;
//    }
    private void setBracePolylineX(){
        
        if (playingField.getShotCount()>99 || playingField.getNearAreaCount()>99)startXForBrace=3.5*CELLSIZE;
        else startXForBrace=3.*CELLSIZE;
        bracePolylineX[0]=startXForBrace;
        bracePolylineX[1]=startXForBrace+CELLSIZE;
        bracePolylineX[2]=bracePolylineX[1];
        bracePolylineX[3]=bracePolylineX[0];
        
    }
}

