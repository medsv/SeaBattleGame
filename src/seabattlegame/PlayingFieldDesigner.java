/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author sergey
 */
public class PlayingFieldDesigner extends PlayingFieldTemplate {
    private PlayingFieldDesignerEventHandler playingFieldDesignerEH;
//    private final SeaBattleGame controller;
    private Rectangle currentShipImage; //изображение перемещаемого корабля
    private EnableButtonListener enableButtonListener;
    private PlayingFieldDesignerView pfdv;

    public PlayingFieldDesigner(PlayingFieldDesignerView pfdv) {
        this.pfdv=pfdv; //нужно для того, чтобы управлять доступностью кнопки "Играть"
    }

        
    @Override
    public void setPlayingField(PlayingField playingField){
        super.setPlayingField(playingField);
        playingFieldDesignerEH=new PlayingFieldDesignerEventHandler();
//            controller=seaBattleGame;
        view.addEventHandler(MouseEvent.MOUSE_DRAGGED,playingFieldDesignerEH);
        view.addEventHandler(MouseEvent.MOUSE_PRESSED,playingFieldDesignerEH);
        view.addEventHandler(MouseEvent.MOUSE_RELEASED,playingFieldDesignerEH);
        view.addEventHandler(MouseEvent.MOUSE_CLICKED,playingFieldDesignerEH);
    
        
    }
    
        public void addEnableButtonListener(EnableButtonListener enableButtonListener){
            this.enableButtonListener= enableButtonListener;
            
        }

        public void placeShipsOnPlayingField(){
            playingFieldDesignerEH.getShipImageArray().placeShipsOnPlayingField();
            playingField.displayShips(true);
         }    
    
        @Override
        public void updateView(){
            playingFieldDesignerEH.updateView();

        }

        @Override
        public void repaintView(){
            super.repaintView();
            updateView();
    //        super.repaintView();
        }        


        private class PlayingFieldDesignerEventHandler 
                        implements EventHandler <MouseEvent>{
            private MouseEvent event;
            private boolean shipSelected;
//            private boolean movingMode; //процесс перемещения корабля
//            private Point startXY; 
            private ShipImage currentShipImage; //перемещаемый корабль
            private final ArrayList<Ship> ships;
//            private boolean repaint; //необходимость перепорисовки
            private boolean intersects; //true если активный (редактируемый) корабль пересекает другой корабль либо ближнюю зону
            private final ShipImageArray shipImageArray;
            private boolean movingMode;
//            private final ShipImage[] shipsImage;

            PlayingFieldDesignerEventHandler(){
//                startXY = new Point();
                ships=playingField.getShips();
                playingField.createShipsImage();
                shipImageArray=new ShipImageArray(ships);
            }

            @Override
            public void handle(MouseEvent event) {

                this.event=event;


                if (event.getButton()!=MouseButton.PRIMARY) return;
                EventType<? extends MouseEvent> eventType = event.getEventType();

//                EventType<MouseEvent> eventType = (EventType<MouseEvent>) event.getEventType();
//                Note: /home/sergey/NetBeansProjects/SeaBattleGame/src/seabattlegame/PlayingFieldDesigner.java uses unchecked or unsafe operations.
//                Note: Recompile with -Xlint:unchecked for details.

                if (eventType==MouseEvent.MOUSE_PRESSED) mousePressed();
                if (eventType==MouseEvent.MOUSE_DRAGGED) mouseDragged();
                if (eventType==MouseEvent.MOUSE_RELEASED) mouseReleased();
                if (eventType==MouseEvent.MOUSE_CLICKED) mouseClicked();

                repaintView();

            }
            //движение мышки с нажатой кнопкой
            private void mouseDragged() {
                movingMode=true;
                System.out.println("mouseDragged");
                shipImageArray.move((int)event.getX(),(int) event.getY());
                repaintView();
                
            }

            private void mousePressed() {
//                startXY.setLocation(event.getX(), event.getY());
                shipImageArray.checkSelection(event.getX(),  event.getY());
                System.out.println("mousePressed ");


            }

            private void mouseReleased() {
                if(movingMode){
                    shipImageArray.fitImage();
                    shipImageArray.resetShipImageSelection();
                    movingMode=false;
                }
                pfdv.enableButton(!shipImageArray.getIntersection());
                repaintView();
            }

            private void mouseClicked() {
                if(event.getClickCount()!=2){
                    shipImageArray.resetShipImageSelection();
                    return;
                }
//                if(shipSelected) currentShipImage.rotate();
                shipImageArray.rotate();
                shipImageArray.resetShipImageSelection();
                System.out.println("mouseDoubleClicked");
                repaintView();
            }


            private void updateView() {
                shipImageArray.draw();
            }
            
            ShipImageArray getShipImageArray(){
                return shipImageArray;
            }
            
            private class ShipImage {

                private final Rectangle ship; //изображение корабля (может располагаться только в клетках)
                private final Rectangle nearArea; //ближняя зона корабля
                private final Rectangle image; //перемещаемое изображение корабля
                private final Ship shipObject; // объект Корабль
                private Point playingFieldPos; //расположение в координатах игрового поля
                private int maxX, maxY;
                private boolean intersected;

                ShipImage(Ship s){
                    shipObject=s;
                    ship=s.getImage(xStep, yStep);
                    image=new Rectangle();
                    nearArea=new Rectangle();
                    playingFieldPos=new Point();
                    
                    initialize();
                }
                
                private void initialize(){
                    fitImage();
                    createNearArea();
                    calculateMaxXY();
                    playingFieldPos.x= (int) Math.round(ship.getX()/xStep);
                    playingFieldPos.y = (int) Math.round(ship.getY()/yStep);

                }

                public Rectangle getImage() {
                    return image;
                }

                public Ship getShipObject(){return shipObject;}
                public Point getPlayingFieldPos(){return playingFieldPos;}
                

                void move (int dx,int dy){
                    int newX=(int)(image.getX()+dx);
                    int newY=(int)(image.getY()+dy);
                    moveTo(newX, newY);
                }

                void moveTo (int x, int y){
                    if (x<0) x=0;
                    if (x>maxX) x=maxX;
                    if (y<0) y=0;
                    if (y>maxY) y=maxY;
                    image.setLocation(x,y);
                    playingFieldPos.x= (int) Math.round(x/xStep);
                    playingFieldPos.y = (int) Math.round(y/yStep);
                    ship.setLocation((int)(playingFieldPos.x *xStep), (int)(playingFieldPos.y *yStep)); //корабль должен находиться в клетках
                    createNearArea();
                }

                
                
                private void calculateMaxXY(){
                    maxX=width-(int)ship.getWidth()-1;
                    maxY=height-(int)ship.getHeight()-1;
                }



                private boolean isInside(Point startXY) {
                    return image.contains(startXY);
                }

                
                private boolean intersects(ShipImage shipImage){
                    return ship.intersects(shipImage.getNearArea());
                }

                private void rotate(){
                    int centerX=(int) ship.getCenterX();
                    int centerY=(int) ship.getCenterY();
                    ship.setSize((int)ship.getHeight(),(int)ship.getWidth());
                    calculateMaxXY(); //пересчитываем границы
                    if(ship.getWidth()>ship.getHeight()) 
                            moveTo ((int)(centerX- ship.getWidth()/2), (int)(centerY-yStep/2));
                    else moveTo ((int)(centerX- xStep/2), (int)(centerY- ship.getHeight()/2));
                    fitImage();
//                    calculateMaxXY();
                }

 

                void fitImage(){
                    image.setBounds(ship);
                }

                void createNearArea(){
                    nearArea.setBounds(ship);
                    nearArea.grow((int)xStep, (int)yStep);
                }

                private Rectangle getNearArea() {
                    return nearArea;
                }

                private void setIntersected(boolean intersects) {
                    intersected=intersects;
                }

                private boolean getIntersected() {
                    return intersected;
                }

                private Rectangle getShip() {
                    return ship;
                }
            }   
            
            class ShipImageArray {
                private ShipImage[] shipsImage;
                private ArrayList<Ship> ships;
                private ShipImage selectedShipImage;
                private Point startXY;
                private boolean inersection;
                private boolean shipImageSelected;//true если щелчок мыши пришелся на корабль
                ShipImageArray(ArrayList<Ship> ships){
                    startXY=new Point();
                    this.ships=ships;
                    shipsImage=new ShipImage[ships.size()];
                    for (int i=0;i<ships.size();i++){
                            shipsImage[i]=new ShipImage(ships.get(i));
                    }
                }
                
                private void chekIntersections(){
                    for(ShipImage si:shipsImage)si.setIntersected(false);
                    
                    for (int i=0; i<shipsImage.length-1; i++){
                        for(int j=i+1;j<shipsImage.length;j++){
                            if(shipsImage[i].intersects(shipsImage[j])) {
                                shipsImage[i].setIntersected(true);
                                shipsImage[j].setIntersected(true);
                            }
                        }
                    }
                }
                void checkSelection(double x, double y){
                    this.startXY.setLocation(x, y);
                    shipImageSelected=false;
                    for(ShipImage si:shipsImage){
                        if(si.isInside(startXY)) {
                            selectedShipImage=si;
//                            this.startXY=startXY;
                            shipImageSelected=true;
                            break;
                        }
                    }
                }

                void draw(){
//                    
                    for (ShipImage sh:shipsImage){
//                        if (shipImageSelected && sh==selectedShipImage) continue;
                        if (sh.getIntersected()) gc.setFill(Color.RED);
                        else gc.setFill(Color.BLUE);
                        drawRectangle(sh.getShip());
                    }
                    if(!shipImageSelected) return;
                    gc.setFill(Color.GRAY);
                    drawRectangle(selectedShipImage.getShip());
                    gc.setFill(Color.BLUE);
                    drawRectangle(selectedShipImage.getImage());
                }
                private void drawRectangle (Rectangle r){
                    gc.fillRect(r.getX(),r.getY(),r.getWidth(),r.getHeight());

                }
                boolean getIntersection(){
                    for (ShipImage sh:shipsImage)
                        if (sh.getIntersected()) return true;
                    return false;
                }

                void move (int x, int y){
                    if(!shipImageSelected) return;
                    selectedShipImage.move(x-startXY.x, y-startXY.y);
                    startXY.x=x;
                    startXY.y=y;	
                    chekIntersections();

                }
                void rotate(){
                    if(!shipImageSelected) return;
                    selectedShipImage.rotate();
                    chekIntersections();
                }
                
                void fitImage(){
                    if(shipImageSelected)selectedShipImage.fitImage();
                }
                
                void resetShipImageSelection(){
                    shipImageSelected=false;
                }
                void placeShipsOnPlayingField(){
                    for (ShipImage sh:shipsImage){
                        sh.getShipObject().setPosition(sh.getPlayingFieldPos(), 
                                    (sh.getImage().getWidth()>sh.getImage().getHeight()));
//                        sh.getShipObject().fitNearArea(playingField.getWidth(), playingField.getHeight());
                    }
                    playingField.fitNearArea();
                }    

            }            
            
        }
   
}
