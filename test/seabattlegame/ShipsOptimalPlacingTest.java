/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author sergey
 */
public class ShipsOptimalPlacingTest {
    public static void main (String[] args){
        GameConf gameConf= new GameConf(10,10, new ArrayList<>(Arrays.asList
            (new Point[] {new Point(4,1), new Point(3,2), new Point(2,3), new Point(1,4)}))); 
        OptimalShipsPlacing sop =new OptimalShipsPlacing(gameConf);
        ArrayList<Ship> ships=sop.shipsPlacing();
        ships.forEach(p->System.out.println(p));
    }
}
