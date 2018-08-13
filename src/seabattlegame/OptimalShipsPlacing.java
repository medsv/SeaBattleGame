/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author sergey
 */
public class OptimalShipsPlacing extends RandomShipsPlacing {
    
    
    public OptimalShipsPlacing(GameConf gameConf) {
        super(gameConf);

    }
    @Override
    public ArrayList<Ship> shipsPlacing(){
        
        double shipsOnEdgeRatio;
        int shipOnEdgeCount;
        
        int rnd = (int)(Math.random()*101);
        if (rnd<11) shipsOnEdgeRatio=0.6;
        else if (rnd<31) shipsOnEdgeRatio=0.4;
        else if (rnd<51) shipsOnEdgeRatio=0.2;
        else if (rnd<71) shipsOnEdgeRatio=0.1;
                
        else shipsOnEdgeRatio=0.;
        
        
        if (shipsOnEdgeRatio==0.) return super.shipsPlacing(); //корабли расставляются случайным образом
        shipOnEdgeCount=(int)Math.round(shipsOnEdgeRatio*gameConf.getTotalShipCount());
        ArrayList <Point> shipsOnEdge = new ArrayList<>();
        ArrayList <Point> shipsRandom = new ArrayList<>(shipTypes);
        int shipCount=0;
        for (Point p:shipTypes){
            if (p.y+shipCount>shipOnEdgeCount){
                Point pp = (Point) p.clone();
                pp.y=shipOnEdgeCount-shipCount;
                shipsOnEdge.add(pp);
                break;
            } else{
                shipsOnEdge.add(p);
                shipCount+=p.y;
            }
            
        }
        shipsRandom.removeAll(shipsOnEdge);
        int i=shipsOnEdge.size()-1;
        if (shipsRandom.get(0).x==shipsOnEdge.get(i).x){
            Point pp = (Point) shipsRandom.get(0).clone(); //чтобы не изменить gameConf
            pp.y=shipTypes.get(i).y-shipsOnEdge.get(i).y;
            shipsRandom.set(0, pp);
        }
        placeShipsOnEdge(shipsOnEdge);
        return shipsRandomPlacing(shipsRandom);
    }
    
    public ArrayList<Ship> placeShipsOnEdge (ArrayList<Point> shipsOnEdge){ //x-кол-во палуб, y-кол-во кораблей
        boolean hor; //true для горизонтального направления
        int deckCount;
        int fieldWidth=gameConf.getWidth(); 
        int fieldHeight=gameConf.getHeight();
        Ship ship;
        iterationCount=0; //считает кол-во попыток разместить корабли
        shipsPlaced=false;
        
 
        
        do{
            tempships.clear();            
iteration:                   
            for (Point p: shipsOnEdge){
                int x =0,y=0, length;
                deckCount=p.x;
                for (int i=1; i<(p.y+1);i++){
                    do{
                        //null если корабль не влез в отведённое поле
                        hor= (int)(Math.random()*2)==0;
                        if (hor) {
                            y = (int)(Math.random()*2)==0 ? 0:fieldHeight-1;
                            length=fieldWidth-deckCount;
                        } else {
                            x = (int)(Math.random()*2)==0 ? 0:fieldWidth-1;
                            length=fieldHeight-deckCount;
                        }
                        int pos=(int)((length+1)*Math.random());
                        if (hor) x=pos;
                        else y=pos;
                        ship = new Ship (new Point(x,y), deckCount, hor);
                        iterationCount++;
                        if (iterationCount==ITERATIONCOUNTLIMIT){
                            break iteration;
                        }
                    } while(intersects(ship)); //повторяем до тех пор пока не получим корабль который не пересекается с уже размещёнными кораблями
                    tempships.add(ship);
//                System.out.println (ship); 
            }
            shipsPlaced=true;
        } 
        
    } while (!shipsPlaced); 
    ships.addAll(tempships);
    return ships;
}
}
