package seabattlegame;
import java.awt.Point;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sergey
 */
public class ProbabilityForCell implements Comparable<ProbabilityForCell> {
    private int probability;
    private Point coords;
    public ProbabilityForCell(int propability,int x, int y){
        coords=new Point(x,y);
        this.probability=probability;
    }

    ProbabilityForCell(Point coords) {
        this(0,coords.x,coords.y);
    }
    public void setProbability(int probability){
        this.probability=probability;
    }
    
    public int getProbability(){
        return probability;
    }

    public void incr(){
        probability++;
    }
    
    public void add(ProbabilityForCell o){
        this.probability+=o.getProbability();
    }
    
    public Point getCoordinates(){
        return coords;
    }
    
    public void setCoordinates(int x, int y){
        coords.x=x;
        coords.y=y;
    }

    @Override
    public int compareTo(ProbabilityForCell o){
        return -(probability-o.probability);//сортировка по убыванию
    }

    void multiply(double multiply) {
        probability*=multiply;
    }
    
    public void addProbability(int prob){
        probability+=prob;
    }
    
}    

