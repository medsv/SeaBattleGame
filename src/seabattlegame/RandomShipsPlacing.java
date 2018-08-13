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
public class RandomShipsPlacing {
    protected GameConf gameConf;
    protected ArrayList<Ship> ships, tempships;
    protected ArrayList<Point> shipTypes;
    protected final int  ITERATIONCOUNTLIMIT=500;
    protected boolean shipsPlaced;
    protected int iterationCount;
//    private boolean placeonEdge; //
    
    public RandomShipsPlacing(GameConf gameConf){
        this.gameConf=gameConf;
        ships=new ArrayList<> (gameConf.getTotalShipCount());
        tempships=new ArrayList<>() ;
        shipTypes=gameConf.getShipTypes();
    }
    
    public ArrayList<Ship> shipsPlacing(){
        return shipsRandomPlacing(shipTypes);
    }
    protected ArrayList<Ship> shipsRandomPlacing(ArrayList<Point> shipTypes){
        Ship ship;
        iterationCount=0; //считает кол-во попыток разместить корабли
        shipsPlaced=false;
    //        ships=null; //уничтожаем имеющийся массив кораблей
        System.out.println("Процесс shipsRandomPlacing начался");
        // Point.x - количество палуб, Point.y - количество кораблей данного типа.

        do{
            tempships.clear();
            tempships.addAll(ships);
iteration:                   
            for(Point p: shipTypes){
                for (int i=1; i<(p.y+1);i++){
                    do{
                        //null если корабль не влез в отведённое поле
                        ship = Ship.createRandomShip(p.x, gameConf.getWidth(), gameConf.getHeight());
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
//        System.out.println (counter);
        ships.addAll(tempships);
        return ships;
    }
    
    protected boolean intersects(Ship ship){

        for (int j=0; j<tempships.size();j++){
//            System.out.println(ship.intersects(ships.get(j)));
            if (ship.intersects(tempships.get(j))) return true;
        }
        return false;
        
    }


}
