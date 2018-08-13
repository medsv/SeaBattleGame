/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author sergey
 */
public class PlayingFieldTemplate {
    protected int CELLSIZE=30;
    protected int xCount; 
    protected int yCount;
    protected PlayingField playingField;
    protected int width;
    protected int height;
    protected Canvas view;
    protected double xStep; 
    protected double yStep;
    protected GraphicsContext gc;
    private Color gridColor;
    protected Color deckColor;
    protected Color hittedDeckColor;
    protected Color shotColor;
    protected Color nearAreaColor;
    protected Color intersectedShipsColor;
    protected Color lastShotColor;

    private int gridLineWidth;
    private Point cellCoords;

    PlayingFieldTemplate (){
        view=new Canvas();
        gc = view.getGraphicsContext2D();
//        xStep=CELLSIZE;
//        yStep=CELLSIZE;
        
        gridColor=Color.GRAY;//по умолчанию
        deckColor=Color.BLUE;
        hittedDeckColor=Color.BLACK;
        shotColor=Color.GREY;
        nearAreaColor=Color.GREY;
        intersectedShipsColor=Color.RED;
        lastShotColor=Color.RED;
        
        
        gridLineWidth=1;//по умолчанию
        cellCoords=new Point();
        
    }

    public void setPlayingField(PlayingField playingField){
        this.playingField=playingField;
//        xCount=playingField.getWidth();
//        yCount=playingField.getHeight();
//        CELLSIZE=30;
        xCount=playingField.getWidth();        
        yCount=playingField.getHeight(); 
        CELLSIZE=30;
        if (xCount>15) CELLSIZE=25;
        if (xCount>25) CELLSIZE=20;
        xStep=CELLSIZE;
        yStep=CELLSIZE;
        setSize();
    }
    
    protected void setSize(){
        setWidth();
        setHeight();
    }
    
    protected void setWidth(){
//        xCount=playingField.getWidth();
        view.setWidth(xCount*CELLSIZE);
        width=(int)view.getWidth();
    }
    
    protected void setHeight(){
//        yCount=playingField.getHeight();
        view.setHeight(yCount*CELLSIZE);
        height=(int)view.getHeight();
    }    
    
    public PlayingField getPlayingField(){
        return playingField;
    }
    

    protected void drawGrid() {

        gc.setLineWidth(gridLineWidth);
         gc.setStroke(gridColor);
         for (int i=0;i<=xCount;i++){
             gc.strokeLine(i*xStep, 0, i*xStep, height);
         }
         for (int i=0;i<=yCount;i++){
             gc.strokeLine(0, i*yStep, width,i*yStep);
         }
    }


    public void repaintView(){
        repaintView(0,0, width, height);
    }

    public void repaintView(int x, int y, int width, int height){
        gc.clearRect(x, y, width, height);
        drawGrid();

    }

    public void updateView(){
        //будет реализована в наследниках
    }

    public Canvas getView(){
        return view;
    }


    public int getWidth(){
        return width;
    }


    public int getHeigh(){
        return height;
    }	

    protected Point getCellCoords(double x, double y){
        cellCoords.setLocation((int)(x/xStep), (int)(y/yStep));
        return cellCoords;
    }
    protected Point getCellCoords(Point p){
        return getCellCoords(p.x,p.y);
    }
    
    
    //!!!!!Это нужно будет удалить
    void setMouseEventHandler (EventHandler <MouseEvent> mouseEventHandler){
        view.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEventHandler);
    }
    
    public void setGridColor(Color gridColor){
        this.gridColor=gridColor;	
    }

    public void setGridLineWidth(int gridLineWidth){
        this.gridLineWidth=gridLineWidth;

    }	

    public Color getGridColor() {
        return gridColor;
    }

    public int getGridLineWidth() {
        return gridLineWidth;
    }

    public Color getDeckColor() {
        return deckColor;
    }

    public Color getHittedDeckColor() {
        return hittedDeckColor;
    }

    public Color getShotColor() {
        return shotColor;
    }

    public Color getNearAreaColor() {
        return nearAreaColor;
    }
    
    public void setDeckColor(Color deckColor) {
        this.deckColor = deckColor;
        if (deckColor==Color.RED) intersectedShipsColor=Color.ORANGE;
        else intersectedShipsColor=Color.RED;
    }

    public void setHittedDeckColor(Color hittedDeckColor) {
        this.hittedDeckColor = hittedDeckColor;
    }

    public void setShotColor(Color shotColor) {
        this.shotColor = shotColor;
    }

    public void setNearAreaColor(Color nearAreaColor) {
        this.nearAreaColor = nearAreaColor;
    }
    
    public Color getLastShotColor() {
        return lastShotColor;
    }

    public void setLastShotColor(Color lastShotColor) {
        this.lastShotColor = lastShotColor;
    }

}
