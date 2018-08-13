/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author sergey
 */
public class PositionProbabilityGuessOld extends RandomGuess {
    
    private Point [] shipTypes ;
    private int curIndexShipType; //индекс искомого типа корабля в массиве shipTypes
    private ProbabilityForCell[] probabilityValues;//двумерный массив организуем как одномерный
    private ProbabilityForCell[] probabilityMap;//
//    private ProbabilityForCell[] prevProbabilityMap; //предыдущая неотсортированная матрица
    private int curDeckCount;
    private final int width;
    private final int height;
    private ArrayList<ProbabilityForCell> pfca;
    private boolean randomGuessMode; 
    
    public PositionProbabilityGuess(PlayingField playingField) {
        super(playingField);
        shipTypes = new Point[playingField.getShipTypes().size()];
        for (int i=0;i<playingField.getShipTypes().size();i++)
            shipTypes [i]=new Point(playingField.getShipTypes().get(i));
        width=playingField.getWidth();
        height=playingField.getHeight();
        probabilityValues=new ProbabilityForCell[width*height];
        probabilityMap=new ProbabilityForCell[width*height];
    //    prevProbabilityMap=new ProbabilityForCell[width*height];
        curIndexShipType=0;
        curDeckCount=shipTypes[curIndexShipType].x;
        //инициализация массивов
        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                probabilityValues[getArrayIndex(i,j)]=new ProbabilityForCell(0,j,i);
                probabilityMap[getArrayIndex(i,j)]=new ProbabilityForCell(0,j,i);
            }
        }
    }
    
    @Override
    protected Point obtainGuess(){ //получение хода в обычном режиме (нет раненого корабля)
        
        if (randomGuessMode) return super.obtainGuess();
        
        if (previousGuessResult==GuessResult.DESTROY) {
//            if (shipTypes[curIndexShipType].y==0)
            setCurIndexShipType();
        }
        
        calculateProbabilityMap();
    //    prevProbabilityMap=Arrays.copyOf(probabilityMap,probabilityMap.length);
//        int temp =getLasIndexMaxProb();

        int lastIndexMaxProb=getLastIndexMaxProb(probabilityMap);
        if (randomGuessMode) return super.obtainGuess();
        int i= (int)(Math.random()*(lastIndexMaxProb+1));
        previousGuess=probabilityMap[i].getCoordinates();
        return previousGuess;    
    }
    private void calculateProbabilityMap(){
        boolean hor=true; //горизонтальное положение корабля
        Point p=new Point();
        //обнуление вероятностей
        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                probabilityValues[getArrayIndex(i,j)].setProbability(0);
                probabilityMap[getArrayIndex(i,j)].setProbability(0);
                probabilityMap[getArrayIndex(i,j)].setCoordinates(j, i); //т.к. массив сортируется нужно каждый раз восстанавливать координаты
            }

        }

        for(int j=0; j<width;j++){
            for (int i=0;i<=height-curDeckCount;i++){
                p.x=j;
                p.y=i;
                if (!playingField.isShipOnFreeArea(p,curDeckCount,!hor)) continue;
                for (int k=0;k<curDeckCount;k++){
                    probabilityValues[getArrayIndex(i+k,j)].incr(); //увеличиваем количество возможных размещений на 1
//                    System.out.println("i="+i+" k=" + k +" j="+j+ 
//                            " индекс=" +getArrayIndex(i+k,j )+ " вероятность=" + probabilityValues[getArrayIndex(i+k,j)].getProbability());
                }
            }
        }
        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                System.out.print(probabilityValues[getArrayIndex(i,j)].getProbability()+"  ");
            }
            System.out.println();
        }
        System.out.println();
        
        for(int i=0; i<height;i++){
            for (int j=0;j<=width-curDeckCount;j++){
                p.x=j;
                p.y=i;
                if (!playingField.isShipOnFreeArea(p,curDeckCount,hor)) continue;
                for (int k=0;k<curDeckCount;k++){
                    probabilityValues[getArrayIndex(i,j+k)].incr(); //увеличиваем количество возможных размещений на 1
                }
            }
        }

        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                System.out.print(probabilityValues[getArrayIndex(i,j)].getProbability()+"  ");
            }
            System.out.println();
        }
        System.out.println();
        for(int i=0; i<height;i++){
            for (int j=0;j<width;j++){
                if (probabilityValues[getArrayIndex(i,j)].getProbability()==0){//в этой ячейке не может быть палубы корабля
                    probabilityMap[getArrayIndex(i,j)].setProbability(0);
                    continue;
                    
                }
                probabilityMap[getArrayIndex(i,j)].add(probabilityValues[getArrayIndex(i,j)]);
                int dx=1;
                int dy=0;
                calculateCellProbability(i,j,dx,dy);
                dx=-1;
                dy=0;
                calculateCellProbability(i,j,dx,dy);
                dx=0;
                dy=1;
                calculateCellProbability(i,j,dx,dy);
                dx=0;
                dy=-1;
                calculateCellProbability(i,j,dx,dy);
            }
        }
        //увеличиваем вероятности по краям
        if (curDeckCount>1){
            double multiply=1.+Math.random()*1.; //множитель для вероятностей по краю
            for(int i=0; i<height;i++){
                for (int j=0;j<width;j+=width-1){
                    probabilityMap[getArrayIndex(i,j)].multiply(multiply);
                }
            }
            for(int i=0; i<height;i+=height-1){
                for (int j=0;j<width;j++){
                    probabilityMap[getArrayIndex(i,j)].multiply(multiply);
                }
            }
        }
        
        
        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                System.out.print(probabilityMap[getArrayIndex(i,j)].getProbability()+"  ");
            }
            System.out.println();
        }
}

    private void calculateCellProbability(int i, int j, int dx, int dy){
        int x, y, i5;
        x=j; y=i;
        for (int k=1;k<curDeckCount;k++){
            x+=dx;
            y+=dy;
//            boolean b=playingField.isPointInside(x,y);
            if(!playingField.isPointInside(x,y))break;
//            int i1=getArrayIndex(i,j);
//            int i2=getArrayIndex(y,x);
//            int i3=probabilityMap[getArrayIndex(i,j)].getProbability();
//            int i4=probabilityValues[getArrayIndex(y,x)].getProbability();
            probabilityMap[getArrayIndex(i,j)].add(probabilityValues[getArrayIndex(y,x)]);
//            i5=probabilityMap[getArrayIndex(i,j)].getProbability();
            
        }

    }

    private int getArrayIndex(int i,int j){
        return i*width+j;
    }

    
    private int getLastIndexMaxProb(ProbabilityForCell[] probabilityArray){
        Arrays.sort(probabilityMap); //сортировка по убыванию
        int maxProbability=probabilityArray[0].getProbability(); //т.к. массив отсортирован по убыванию
        if (maxProbability==0) {
            randomGuessMode=true;
            return -1;
        }
        
        for (int i=1; i<probabilityArray.length;i++){
            if (probabilityArray[i].getProbability()<maxProbability)return i-1;
            
        }
        return probabilityArray.length-1; //все элементы массива имеют одинаковые значения
      

    }

    private void setCurIndexShipType(){ //вызывается после подбития корабля
        System.out.println("Кол-во палуб до подбития"+shipTypes[curIndexShipType].x);
        //определяем какой корабль (с каким количеством палуб) потопили
        int deckCount= playingField.makeShotToShips(previousGuess).getDeckCount();
        updateAliveShipsList(deckCount);
        
//        System.out.println("Живые корабли");
//        for (Point p:shipTypes){
//            System.out.print(p+ " ");
//        }
//        System.out.println();
        
        while (shipTypes[curIndexShipType].y==0) {//корабли текущего типа потоплены, переходим кдругому типу (с меньшим количеством палуб)
            curIndexShipType++;
            curDeckCount=shipTypes[curIndexShipType].x;
        } 
 
        if (curDeckCount==1) curDeckCount=2;
        
 //       System.out.println("Кол-во палуб после подбития"+shipTypes[curIndexShipType].x);
        
    //    if (curIndexShipType==shipTypes.length) { //если все корабли потоплены
    //        curIndexShipType=-2; //корабли закончились, однопалубных кораблей нет в конфигурации. Игра закончена и такой ситуации не должно возникать.
    //    } else if (shipTypes[curIndexShipType].x==1) curIndexShipType=-1; //остались только однопалубные корабли
    
    }
    
    //уменьшаем кол-во кораблей подбитого типа
    private void updateAliveShipsList(int deckCount){
        for(Point p:shipTypes){
            System.out.println("До вычитания "+p.y);
            if (p.x==deckCount) {
                p.y--;
            } 
            System.out.println("После вычитания "+p.y);
         
        }
        
        
    }
    
//сомнительное решение    
    @Override
    protected  void pgihmCreation(){ 
        ProbabilityForCell pfc;
        super.pgihmCreation();
        pfca=new ArrayList<> (pgihm.size());
        for (int i=0; i<pgihm.size();i++){
                pfc=new ProbabilityForCell(pgihm.get(i));
                pfc.setProbability(detectEmptyCellCount(pgihm.get(i)));
                pfca.add(pfc);
        }
        pfca.sort((p1,p2)->p2.getProbability()-p1.getProbability());

        
    }

    private int detectEmptyCellCount(Point coords) {
        Point p=(Point) coords.clone();
        int dx=p.x-firstHittedDeck.x;
        int dy=p.y-firstHittedDeck.y;
        int emptyCellCount=0;
        while(playingField.isPointInside(p) && 
                (playingField.getPlayingFieldState()[p.y][p.x]==CellStateType.EMPTY 
                ||playingField.getPlayingFieldState()[p.y][p.x]==CellStateType.DECK)){
                emptyCellCount++;
                p.translate(dx,dy);
                
        }
        return emptyCellCount;
    }
    
    @Override
    protected Point guessInHittedMode(){ //аЕбаЛаИ баАаНаИаЛаИ аКаОбаАаБаЛб аИ аПаОбаЛаЕ ббаОаГаО аПбаОаМаАаЗаАаЛаИ

         previousGuess=pfca.get(0).getCoordinates();
         pfca.remove(0);
         return previousGuess;
    }
}
    

