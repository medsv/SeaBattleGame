package seabattlegame;
import java.util.ArrayList;
import java.awt.Point;
import java.util.function.Consumer;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class GameConf
{
		
    private final int fieldWidth;
    private final int fieldHeight;

    /**
     * <p>Типы кораблей. Point.x - количество палуб, Point.y - количество кораблей данного типа.</p>

     */

    private final ArrayList<Point> shipTypes;

    private int totalShipCount; //максимальное количество кораблей
    private int totalDeckCount;
    private int totalFreeCellCount;


    public GameConf(int fieldWidth, int fieldHeight, ArrayList<Point> shipTypes){
            this.fieldWidth=fieldWidth;
            this.fieldHeight=fieldHeight;
            this.shipTypes=shipTypes;
            //Анализирует массив shipTypes и расчитываем totalShipCount
            calculateFieldParameters();
            totalFreeCellCount=fieldWidth*fieldHeight-totalDeckCount;
    }

    public GameConf(int maxDeckCount) {
        int shipCount=1;
        shipTypes=new ArrayList<>(maxDeckCount);
        for (int i=maxDeckCount;i>0;i--){
            shipTypes.add(new Point(i,shipCount));
            shipCount++;
        }
        calculateFieldParameters();
        fieldWidth=(int)Math.round(Math.sqrt(totalDeckCount*5.));
        fieldHeight=fieldWidth;
        totalFreeCellCount=fieldWidth*fieldHeight-totalDeckCount;
    }

    private void calculateFieldParameters() {
        totalShipCount=0; 
        for(Point p:shipTypes){
            totalShipCount+= p.y;
            totalDeckCount+=p.y*p.x;
        }
//            totalFreeCellCount=fieldWidth*fieldHeight-totalDeckCount;
    }

    public int getTotalShipCount() {
            return totalShipCount;
    }

    public int getWidth() {
            return fieldWidth;
    }	
    public int getHeight() {
            return fieldHeight;
    }	
    public ArrayList<Point> getShipTypes() {
        ArrayList<Point> pa = new ArrayList<>(shipTypes.size());
        shipTypes.forEach((p) -> pa.add((Point) p.clone()));
        return pa;
    }	

    public int getTotalDeckCount() {
        return totalDeckCount;
    }
    public int getTotalFreeCellCount() {
        return totalFreeCellCount;
    }

        
//        public ArrayList<Point> getShipTypesClone(){
//            ArrayList<Point> pa = new ArrayList<>(shipTypes.size());
//            shipTypes.forEach((p) -> pa.add((Point) p.clone()));
//            return pa;
//        }

    public int getLongestShipDeckCount() {
        return shipTypes.get(0).x;
    }

        
}

