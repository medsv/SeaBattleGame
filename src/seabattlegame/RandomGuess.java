package seabattlegame;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class RandomGuess extends PureRandomGuess
{
	
//	private PlayingField playingField;
//	private ArrayList <Point> shotAvailableCells;
//	private Point lastGuess;
        Point firstHittedDeck;
//        protected GuessResult lastGuessResult;
        protected boolean hittedMode=false; //true если предыдущий выстрел первый раз ранил корабль
        private boolean doubleHitted=false;//корабль был ранен два раза
        protected ArrayList<Point> pgihm; //possible gueses in hitted mode - четыре клетки вокруг подбитой палубы в первый раз раненного корабля
        private int dx, dy; //направление движения при добивании

        public RandomGuess(PlayingField playingField){
            super(playingField);
            lastGuessResult=GuessResult.OUT;
        }


        @Override
        public Point getGuess() {
            lastGuessResult=playingField.getLastGuessResult();
            switch (lastGuessResult){
                case HIT:{ //корабль ранен
//                    if (hittedMode) { //если корабль до этого был ранен
//                        if(!doubleHitted){
//                            dx=lastGuess.x-firstHittedDeck.x; //направление движения для добивания корабля
//                            dy=lastGuess.y-firstHittedDeck.y;
//                        }
//                        if((lastGuess.x-dx)<0 || (lastGuess.y-dy)<0 || (lastGuess.x+dx)==playingField.getWidth() ||
//                            (lastGuess.y+dy)==playingField.getHeight()) changeShootingDirection();
//                        doubleHitted=true;
//                        return guessInDoubleHittedMode();
//
//
//
//                    }else { //первое ранение корабля
//                        firstHittedDeck=lastGuess;
//                        System.out.println("hitted mode "+lastGuess);
//                        hittedMode=true; 
//                        pgihmCreation();
//                        return guessInHittedMode();	
//                        }
                    
                    if (hittedMode) { //если корабль до этого был ранен
			doubleHitted=true;
                        hittedMode=false;
                        dx=lastGuess.x-firstHittedDeck.x; //направление движения для добивания корабля
                        dy=lastGuess.y-firstHittedDeck.y;
                        return guessInDoubleHittedMode();

                    }else if (doubleHitted) return guessInDoubleHittedMode();

                    else { //первое ранение корабля
                            firstHittedDeck=lastGuess;
                            System.out.println("hitted mode "+lastGuess);
                            hittedMode=true; 
                            pgihmCreation();
                            return guessInHittedMode();	
                        }
                    }
                case OUT : { //нужно изменить направление стрельбы
                    if (doubleHitted){
                        changeShootingDirection();
                        return guessInDoubleHittedMode();
                    }else if (hittedMode) return guessInHittedMode(); //если ранили корабль и после этого промазали
                    else return obtainGuess();
                    }	
                case DESTROY: {
                    hittedMode=false;
                    doubleHitted=false;
                    return obtainGuess();
                    }
                }	
            return null;		
        }

        protected void pgihmCreation(){
            pgihm= new ArrayList <Point> (4); 
            pgihm.add (new Point(lastGuess.x,lastGuess.y+1)); 
            pgihm.add (new Point(lastGuess.x,lastGuess.y-1)); 
            pgihm.add (new Point(lastGuess.x+1,lastGuess.y)); 
            pgihm.add (new Point(lastGuess.x-1,lastGuess.y)); 
            System.out.println("Формируем массив из четырёх точек");
            System.out.println(pgihm);
            Iterator <Point> iter = pgihm.iterator();
            while (iter.hasNext()){ //оставляем только клетки с EMPTY в пределах игрового поля
                Point p = iter.next();

                if (!playingField.isCellInside(p) || !playingField.isShotAvailableCell(p)) 
                    
                    iter.remove() ; //|| гарантирует, что у getPlayingFieldState()[p.y][p.x] y и x будут в пределах игрового поля
            }
            System.out.println("Откорректированный массив");
            System.out.println(pgihm);
        
        }
        
        
        protected Point guessInHittedMode(){ //если ранили корабль и после этого промазали
            int i= (int) (Math.random() * pgihm.size());
            lastGuess=pgihm.get(i); 
            pgihm.remove(i);
            return lastGuess;

        }

        protected Point obtainGuess(){ //получение хода в обычном режиме (нет раненого корабля)
//            int i=(int)(Math.random()*shotAvailableCells.size());
//            lastGuess=shotAvailableCells.get(i);
            return getPureRandomGuess(); 	

        }

        private Point guessInDoubleHittedMode(){
//            lastGuess.x+=dx;
//            lastGuess.y+=dy;
//            if (!shotAvailableCells.contains(lastGuess)){ 
//                changeShootingDirection();
//                lastGuess.x+=dx;
//                lastGuess.y+=dy;	
//            }
//            return lastGuess;
            lastGuess.x+=dx;
            lastGuess.y+=dy;
            if(!playingField.isCellInside(lastGuess) || !playingField.isShotAvailableCell(lastGuess)){	
             
                changeShootingDirection(); //там lastGuess=firstHittedDeck
                lastGuess.x+=dx;
                lastGuess.y+=dy;	
            }
            return lastGuess;

        }
        private void changeShootingDirection(){
            System.out.println("Предыдущий ход "+lastGuess);
            System.out.println("Смена направления");	
            dx=-dx;
            dy=-dy;
            lastGuess=firstHittedDeck;
            System.out.println("Новый предыдущий ход "+lastGuess);
            System.out.println("dx="+dx+" dy="+dy);
        }

//	public void setGuessResult(GuessResult guessResult){
//        //    lastGuessResult=guessResult;
//	}

}

