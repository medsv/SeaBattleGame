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
//    private final int width;
//    private final int height;
    private boolean randomGuessMode;
    private int curDeckCount;
    private ArrayList<ProbabilityForCell> pfca;
    private boolean edgeMode; //true если выбирается ячейка по краям
    private final int EDGEMODEWEIGHT=15;
    private final int INNERMODEWEIGHT=10;
    
    public PositionProbabilityGuess(PlayingField playingField){
        super(playingField);
        aliveShipTypes=playingField.getShipTypes();
        shipTypeCount=aliveShipTypes.size();
//        curDeckCount=aliveShipTypes.get(0).x; //самая длинная палуба
        ppga=new PositionProbabilityGuessArray();
        curDeckCount=aliveShipTypes.get(0).x;
        initialize();
        
    }
    
    private void initialize(){
        ppga.initializeAll();
    }
    
    @Override
    protected Point obtainGuess(){ //получение хода в обычном режиме (нет раненого корабля)
    
        if (randomGuessMode) return super.obtainGuess();
        if (lastGuessResult==GuessResult.DESTROY) {
            updateAliveShipsList();
        }
        ppga.detectMode();
        lastGuess=ppga.getGuess();
        while (lastGuess==null){
            if (edgeMode) {
                edgeMode=false;
                lastGuess=ppga.getGuess();
            }
            else {
                randomGuessMode=true;
                lastGuess=super.obtainGuess(); 
            }
        }
        return lastGuess;
    }
    

  
    private void updateAliveShipsList(){
        int deckCount= playingField.getDestroyedShipDeckCount();
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
        
    if (edgeMode) {
        for (ProbabilityForCell pfc1:pfca){
            if(pfc1.getCoordinates().x==0 || pfc1.getCoordinates().x==width-1)
                pfc1.addProbability(height);
            else if (pfc1.getCoordinates().y==0 || pfc1.getCoordinates().y==height-1)
                pfc1.addProbability(width);
            }
        }        
        pfca.sort((p1,p2)->p2.getProbability()-p1.getProbability());
    }
    //определяем количество пустых клеток в заданном направлении
    private int detectEmptyCellCount(Point coords) {
        Point p=(Point) coords.clone();
        int dx=p.x-firstHittedDeck.x;
        int dy=p.y-firstHittedDeck.y;
        int emptyCellCount=0;
        while(playingField.isCellInside(p) && 
                (playingField.getPlayingFieldState()[p.y][p.x]==CellState.EMPTY 
                ||playingField.getPlayingFieldState()[p.y][p.x]==CellState.DECK)){
                emptyCellCount++;
                p.translate(dx,dy);
                
        }
        return emptyCellCount;
    }
    
    @Override
    protected Point guessInHittedMode(){ //аЕбаЛаИ баАаНаИаЛаИ аКаОбаАаБаЛб аИ аПаОбаЛаЕ ббаОаГаО аПбаОаМаАаЗаАаЛаИ

         lastGuess=pfca.get(0).getCoordinates();
         pfca.remove(0);
         return lastGuess;
    }
    
    
    
    private class PositionProbabilityGuessArray{
        private final ProbabilityForCell [][] pfc; //для каждого типа корабля своя матрица
        private final Point p = new Point(); //чтобы не создавать многочисленные объекты Point
        private final ProbabilityForCell [] mask; // матрица маски
        private int serchingShipIndex;
        
        PositionProbabilityGuessArray(){
//            pfc=new ProbabilityForCell [shipTypeCount+2][width*height]; //shipTypeCount cуммарная матрица
            pfc=new ProbabilityForCell [shipTypeCount+2][]; //shipTypeCount cуммарная матрица
            for (int i=0;i<shipTypeCount+1;i++){
                pfc[i]=new ProbabilityForCell[width*height];
            }
            pfc [shipTypeCount+1]=new ProbabilityForCell[2*(width +height-2)]; //вероятности по краям поля
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
                for (int i=0;i<shipTypeCount;i++)
                    if (doCalculate(i)) setProbabilityToZero(i);
                setProbabilityToZero(shipTypeCount);
        }
        
        
        private int getArrayIndex(int i,int j){
            return i*width+j;
        }
        
        private void detectMode(){
            double edgeModeWeight=EDGEMODEWEIGHT*playingField.getAvailableEdgeCellRatio();
            double innerModeWeight=INNERMODEWEIGHT*playingField.getAvailableInnerCellRatio();
            edgeMode=Math.random()*(edgeModeWeight+innerModeWeight)<edgeModeWeight;
//            edgeMode=false; //тестирование
        }
        
        private Point getGuess() {


            Point p;
           
            setProbabilityToZeroAll();
            recoveryCoordinates(shipTypeCount);
            calculatePfcs();
//            for (int i=0;i<shipTypeCount-1;i++){
//                System.out.format("Палуб: %d, количество: %d",aliveShipTypes.get(i).x, aliveShipTypes.get(i).y);
//                printpfc("",pfc[i]);
//            }
                
            createMask();
            calculateSumPfc();
//            calculateSumPfcForSearchingShip();
//            increaseProbOnEdge();
            printpfc("Суммарная вероятность до сортировки",pfc[shipTypeCount]);
            if(edgeMode){
                p=getRandMaxProbCell(pfc[shipTypeCount+1]);
                if (p!=null) return p;
            }
            p=getRandMaxProbCell(pfc[shipTypeCount]);
            return p;
//            Arrays.sort(pfc[shipTypeCount]); //сортировка по убыванию
//            int maxProbability=pfc[shipTypeCount][0].getProbability();
//            if(maxProbability==0) return null;
//            int lastIndexMaxProbability = 0;
//            for (int i=0; i<pfc[shipTypeCount].length; i++) {
//                if (pfc[shipTypeCount][i].getProbability()<maxProbability){ 
//                    lastIndexMaxProbability=i;
//                    break;
//                }
//            }
//            
//            return pfc[shipTypeCount][(int) (Math.random() * lastIndexMaxProbability)].getCoordinates();
        }
        
        private Point getRandMaxProbCell(ProbabilityForCell [] pfc){
            Arrays.sort(pfc); //сортировка по убыванию
            int maxProbability=pfc[0].getProbability();
            if(maxProbability==0) return null;
            int lastIndexMaxProbability = 0;
            for (int i=0; i<pfc.length; i++) {
                if (pfc[i].getProbability()<maxProbability){ 
                    lastIndexMaxProbability=i;
                    break;
                }
            }
            return pfc[(int) (Math.random() * lastIndexMaxProbability)].getCoordinates();
            
        }
        
        private void calculatePfcs(){
            for (int i=0;i<shipTypeCount;i++) 
                if (doCalculate(i)) calculatePfc(i);
        }
        private boolean doCalculate(int i){
            if (aliveShipTypes.get(i).x==1) return false; //однопалубные корабли не влияют на максимум и минимум 
            //для режима edge вероятности нужно считать для всех кораблей, чтобы не стрелять по крайним палубам
            if (!edgeMode) if (aliveShipTypes.get(i).x!=curDeckCount) return false; //только искомый корабль
            
            if (aliveShipTypes.get(i).y==0) return false; //все корабли данного типа подбиты, вычисления не нужны
            return true;
        }
        
        private void calculatePfc(int ii){
            ProbabilityForCell[] pfcvar = pfc[ii];
            int deckCount=aliveShipTypes.get(ii).x;
            int addValue=aliveShipTypes.get(ii).y; //добавляем количество оставшихся в живых кораблей
            boolean hor=true;
            for(int j=0; j<width;j++){
                if(edgeMode && (j!=0 && j!=(width-1))) continue;

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
                if(edgeMode && (i!=0 && i!=(height-1))) continue;
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
                    if(i==serchingShipIndex) break;
                    else{
                        serchingShipIndex=i;
                        createMask();
                        break;
                    }
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

//            System.out.println("Суммарные матрицы");
//            for (int k=0;k<shipTypeCount;k++){
//                if(!doCalculate(k)) continue;
//                calculateSumPfcForShip(k);
//                System.out.format("Палуб: %d, количество: %d",aliveShipTypes.get(k).x, aliveShipTypes.get(k).y);
//                printpfc("",pfc[shipTypeCount]);
//                
//            }

            
            
            for (int i=0;i<shipTypeCount;i++){
                if (!doCalculate(i))continue;
                for (int j=0;j<pfc[i].length;j++){
                    pfc[shipTypeCount][j].add(pfc[i][j]);
                }
            }
            printpfc("Суммарная матрица",pfc[shipTypeCount]);
            for (int i=0;i<mask.length;i++){
                pfc[shipTypeCount][i].multiply(mask[i].getProbability());
            }
            if(edgeMode){
                int k=0;
                for(int i=0;i<height;i++)
                    if (i==0 || i== height-1)
                        for (int j=0;j<width;j++) pfc[shipTypeCount+1][k++]=
                                pfc[shipTypeCount][getArrayIndex(i,j)];
                    else{
                        pfc[shipTypeCount+1][k++]=pfc[shipTypeCount][getArrayIndex(i,0)];
                        pfc[shipTypeCount+1][k++]=
                                pfc[shipTypeCount][getArrayIndex(i,width-1)];  
                        
                }   
                
//                System.out.println("Матрица edge");
//                for (ProbabilityForCell p:pfc[shipTypeCount+1])
//                    System.out.println(p.getProbability());
            
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
//            System.out.println(str);
//            for (int i=0;i<height;i++){
//                for(int j=0;j<width;j++)
//                    System.out.printf("%3d ", pfcvar[getArrayIndex(i,j)].getProbability());
//                System.out.println();
//            }
        }

        private boolean outOfBounds(int i, int j) {
            return (i<0 || j<0 || i>=height || j>=width);
        }
        
//        private void increaseProbOnEdge(){
//                //увеличиваем вероятности по краям
//            double multiply;
////            if (aliveShipTypes.get(0).y>1) multiply=1.; //множитель для вероятностей по краю
////            else if (shipTypeCount>1 && aliveShipTypes.get(1).y>0) multiply=0.5;
////            else if (shipTypeCount>2 && aliveShipTypes.get(2).y>1) multiply=0.2;
////            else multiply=0.;
//            multiply=2.;
//            multiply=1.+Math.random()*multiply;
//            for(int i=0; i<height;i++){
//                for (int j=0;j<width;j+=width-1){
//                    pfc[shipTypeCount][getArrayIndex(i,j)].multiply(multiply);
//                }
//            }
//            for(int i=0; i<height;i+=height-1){
//                for (int j=1;j<width-1;j++){ //чтобы дважды не увеличивать значения в угловых точках
//                    pfc[shipTypeCount][getArrayIndex(i,j)].multiply(multiply);
//                }
//            }
//        }
        

        private void createMask() {
//            int j=0;
//            for(int i=0;i<aliveShipTypes.size();i++){
//                if (aliveShipTypes.get(i).y>0){
//                    j=i;
//                    break;
//                }
//            }
                
            for(int i=0;i<mask.length;i++){
                mask[i].setProbability((int) Math.signum(pfc[serchingShipIndex][i].getProbability()));
            }
//            //вместо вычисления суммирующей матрицы
//            for (int i=0;i<pfc[shipTypeCount].length;i++)
//                pfc[shipTypeCount][i].setProbability(pfc[j][i].getProbability());
                
        }
         
    }
        
    
}
