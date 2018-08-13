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
public class PositionProbabilityGuess extends RandomGuess{
    private final ArrayList <Point> aliveShipTypes; //информация об оставшихся целими кораблях
    private final int shipTypeCount;
    private final PositionProbabilityGuessArray ppga;
    private final int width;
    private final int height;
    private boolean randomGuessMode;
    private int curDeckCount;
    private ArrayList<ProbabilityForCell> pfca;
    
    public PositionProbabilityGuess(PlayingField playingField){
        super(playingField);
        aliveShipTypes=playingField.getGameConf().getShipTypesClone();
        shipTypeCount=aliveShipTypes.size();
        width=playingField.getWidth();
        height=playingField.getHeight();
//        curDeckCount=aliveShipTypes.get(0).x; //самая длинная палуба
        ppga=new PositionProbabilityGuessArray();
        curDeckCount=aliveShipTypes.get(0).x;
        initialize();
        
    }
    
    public void initialize(){
        ppga.initializeAll();
    }
    
    @Override
    protected Point obtainGuess(){ //получение хода в обычном режиме (нет раненого корабля)
    
        if (randomGuessMode) return super.obtainGuess();
        if (previousGuessResult==GuessResult.DESTROY) {
            updateAliveShipsList();
        }
        previousGuess=ppga.getGuess();
        if (previousGuess==null){ //не осталось ни одной свободной пары клеток
            randomGuessMode=true;
            return super.obtainGuess(); //случайный выбор
        }
        return previousGuess;
    }
    

  
    private void updateAliveShipsList(){
        int deckCount= playingField.makeShotToShips(previousGuess).getDeckCount();
        for (int i=0;i<shipTypeCount;i++){
            if (aliveShipTypes.get(i).x==deckCount) {
                aliveShipTypes.get(i).y--;
                break;
            }
        }
        for (int i=0;i<shipTypeCount;i++){
            if(aliveShipTypes.get(i).y>0) {
                curDeckCount=aliveShipTypes.get(i).x;
                break;
            }
        }
        ppga.detectSerchingShipIndex();
        
        for(int i=0;i<shipTypeCount;i++){
            if(aliveShipTypes.get(i).y>0 && aliveShipTypes.get(i).x>1) return;
        }
        //не осталось ни одного корабля с количеством палуб больше одной
        randomGuessMode=true;
//        //сначала ищем однопалубные корабли в двойных свободных ячейках
//        aliveShipTypes.get(shipTypeCount-1).x=2;
//        aliveShipTypes.get(shipTypeCount-1).y=1;
    }
    
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
    //определяем количество пустых клеток в заданном направлении
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
    
    
    
    private class PositionProbabilityGuessArray{
        private final ProbabilityForCell [][] pfc; //для каждого типа корабля своя матрица
        private final Point p = new Point(); //чтобы не создавать многочисленные объекты Point
        private final ProbabilityForCell [] mask; // матрица маски
        private int serchingShipIndex;
        
        PositionProbabilityGuessArray(){
            pfc=new ProbabilityForCell [shipTypeCount+1][width*height]; //shipTypeCount cуммарная матрица
            mask=new ProbabilityForCell[width*height];
        }
        
        private void initialize(int ii){
            ProbabilityForCell[] pfcvar=pfc[ii];
            for(int i=0;i<height;i++)
                for (int j=0;j<width;j++)
                    pfcvar [getArrayIndex(i,j)]=new ProbabilityForCell(0,j,i);
            
            
            
        }
        
        private void initializeAll(){
            for (int i=0;i<=shipTypeCount;i++)initialize(i);
            for(int i=0;i<height;i++)
                for (int j=0;j<width;j++)
                    mask [getArrayIndex(i,j)]=new ProbabilityForCell(0,j,i);
            
        }
        
        private void setProbabilityToZero(int ii){
            ProbabilityForCell[] pfcvar = pfc[ii];
            for (ProbabilityForCell p:pfcvar) p.setProbability(0);
        }
        
        //восстановление координат отсортированного массива
        private void recoveryCoordinates(int ii){
            ProbabilityForCell[] pfcvar = pfc[ii];
            for(int i=0;i<height;i++)
                for (int j=0;j<width;j++)
                    pfcvar [getArrayIndex(i,j)].setCoordinates(j, i);
            
        }
        
        private void setProbabilityToZeroAll(){
            for (int i=0;i<=shipTypeCount;i++)setProbabilityToZero(i);
    
        }
        
        
        private int getArrayIndex(int i,int j){
            return i*width+j;
        }
        

        private Point getGuess() {

            setProbabilityToZeroAll();
            recoveryCoordinates(shipTypeCount);
            calculatePfcs();
            for (int i=0;i<shipTypeCount-1;i++){
                System.out.format("Палуб: %d, количество: %d",aliveShipTypes.get(i).x, aliveShipTypes.get(i).y);
                printpfc("",pfc[i]);
            }
                
            createMask();
//            calculateSumPfc();
            calculateSumPfcForSearchingShip();
//            increaseProbOnEdge();
            printpfc("Суммарная вероятность до сортировки",pfc[shipTypeCount]);
            Arrays.sort(pfc[shipTypeCount]); //сортировка по убыванию
            int maxProbability=pfc[shipTypeCount][0].getProbability();
            if(maxProbability==0) return null;
            int lastIndexMaxProbability = 0;
            for (int i=0; i<pfc[shipTypeCount].length; i++) {
                if (pfc[shipTypeCount][i].getProbability()<maxProbability){ 
                    lastIndexMaxProbability=i;
                    break;
                }
            }
            
            return pfc[shipTypeCount][(int) (Math.random() * lastIndexMaxProbability)].getCoordinates();
        }
        
        private void calculatePfcs(){
            for (int i=0;i<shipTypeCount;i++) 
                if (doCalculate(i)) calculatePfc(i);
        }
        private boolean doCalculate(int i){
            if (aliveShipTypes.get(i).y==0) return false; //все корабли данного типа подбиты, вычисления не нужны
            if (aliveShipTypes.get(i).x==1) return false; //однопалубные корабли не влияют на максимум и минимум 
            return true;
        }
        
        private void calculatePfc(int ii){
            ProbabilityForCell[] pfcvar = pfc[ii];
            int deckCount=aliveShipTypes.get(ii).x;
            int addValue=aliveShipTypes.get(ii).y; //добавляем количество оставшихся в живых кораблей
            boolean hor=true;
            for(int j=0; j<width;j++){
                for (int i=0;i<=height-deckCount;i++){
                    p.x=j;
                    p.y=i;
                    if (!playingField.isShipOnFreeArea(p,deckCount,!hor)) continue;
                    for (int k=0;k<deckCount;k++){
                        pfcvar[getArrayIndex(i+k,j)].addProbability(addValue); //увеличиваем количество возможных размещений на число целых кораблей
                    }
                }
            }
            for(int i=0; i<height;i++){
                for (int j=0;j<=width-deckCount;j++){
                    p.x=j;
                    p.y=i;
                    if (!playingField.isShipOnFreeArea(p,deckCount,hor)) continue;
                    for (int k=0;k<deckCount;k++){
                        pfcvar[getArrayIndex(i,j+k)].addProbability(addValue); //увеличиваем количество возможных размещений на число целых кораблей
                    }
                }
            }
            
        }
        void detectSerchingShipIndex(){
            
            for(int i=0;i<aliveShipTypes.size();i++){
                if (aliveShipTypes.get(i).y>0){
                    serchingShipIndex=i;
                    break;
                }
            }
        }
        //вычисляет матрицу для корабля с наибольшим количеством палуб, mask не нужен
        private void calculateSumPfcForSearchingShip() {
            calculateSumPfcForShip(serchingShipIndex);
//            for (int i=0;i<pfc[shipTypeCount].length;i++)
//                pfc[shipTypeCount][i].setProbability(pfc[j][i].getProbability());
            
        }
        //вычисляет матрицу для ВСЕХ живых кораблей, применяется вместе с mask
        private void calculateSumPfc() {
//            for (int i=0;i<shipTypeCount;i++){
//                if (!doCalculate(i)) continue;
//                for (int j=0;j<pfc[0].length;j++){
//                    pfc[shipTypeCount][j].add(pfc[i][j]);
//                }
//            }

            System.out.println("Суммарные матрицы");
            for (int k=0;k<shipTypeCount;k++){
                if(!doCalculate(k)) continue;
                calculateSumPfcForShip(k);
                System.out.format("Палуб: %d, количество: %d",aliveShipTypes.get(k).x, aliveShipTypes.get(k).y);
                printpfc("",pfc[shipTypeCount]);
                
            }
            for (int i=0;i<mask.length;i++){
                pfc[shipTypeCount][i].multiply(mask[i].getProbability());
            }
        }
        
        private void calculateSumPfcForShip(int k){
            int deckCount, sum;
                deckCount=aliveShipTypes.get(k).x;
                for(int i=0;i<height;i++)
                    for(int j=0;j<width;j++){
                        sum=pfc[k][getArrayIndex(i,j)].getProbability();
                        if (sum==0) continue;
                        for(int ii=i-(deckCount-1);ii<i+deckCount;ii++){
                            if (ii==i)continue;
                            if (outOfBounds(ii,j)) continue;
                            sum+=pfc[k][getArrayIndex(ii,j)].getProbability();
                        }
                        for(int jj=j-(deckCount-1);jj<j+deckCount;jj++){
                            if (jj==j)continue;
                            if (outOfBounds(i,jj)) continue;
                            sum+=pfc[k][getArrayIndex(i,jj)].getProbability();
                        }
//                        pfc[shipTypeCount][getArrayIndex(i,j)].setProbability(sum);
                        pfc[shipTypeCount][getArrayIndex(i,j)].addProbability(sum);
                        
                    }
            
        }

        private void printpfc(String str,ProbabilityForCell [] pfcvar) {
            System.out.println(str);
            for (int i=0;i<height;i++){
                for(int j=0;j<width;j++)
                    System.out.printf("%3d ", pfcvar[getArrayIndex(i,j)].getProbability());
                System.out.println();
            }
        }

        private boolean outOfBounds(int i, int j) {
            return (i<0 || j<0 || i>=height || j>=width);
        }
        
        private void increaseProbOnEdge(){
                //увеличиваем вероятности по краям
            double multiply;
//            if (aliveShipTypes.get(0).y>1) multiply=1.; //множитель для вероятностей по краю
//            else if (shipTypeCount>1 && aliveShipTypes.get(1).y>0) multiply=0.5;
//            else if (shipTypeCount>2 && aliveShipTypes.get(2).y>1) multiply=0.2;
//            else multiply=0.;
            multiply=2.;
            multiply=1.+Math.random()*multiply;
            for(int i=0; i<height;i++){
                for (int j=0;j<width;j+=width-1){
                    pfc[shipTypeCount][getArrayIndex(i,j)].multiply(multiply);
                }
            }
            for(int i=0; i<height;i+=height-1){
                for (int j=1;j<width-1;j++){ //чтобы дважды не увеличивать значения в угловых точках
                    pfc[shipTypeCount][getArrayIndex(i,j)].multiply(multiply);
                }
            }
        }
        

        private void createMask() {
            int j=0;
            for(int i=0;i<aliveShipTypes.size();i++){
                if (aliveShipTypes.get(i).y>0){
                    j=i;
                    break;
                }
            }
                
            for(int i=0;i<mask.length;i++){
                mask[i].setProbability((int) Math.signum(pfc[j][i].getProbability()));
            }
//            //вместо вычисления суммирующей матрицы
//            for (int i=0;i<pfc[shipTypeCount].length;i++)
//                pfc[shipTypeCount][i].setProbability(pfc[j][i].getProbability());
                
        }
         
    }
        
    
}
