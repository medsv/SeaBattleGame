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
public class ForEachTest {
    public static void main(String[] args){
        Point [] ps = {new Point(0,0), new Point(1,1)};
        for (Point p:ps){
//            p=new Point(3,3); //массив не меняется
//              p.x=3; //массив меняется
//              p.y=3; //массив меняется

           p.x++; //массив меняется
           p.y++; //массив меняется

        }
    
        for (Point p:ps){
            System.out.print(p+" ");
        }
    }
    
}
