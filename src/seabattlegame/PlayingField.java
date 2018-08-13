package seabattlegame;
import java.util.ArrayList;
import java.awt.Point;


public class PlayingField {

    private GameConf gameConf;


    private ArrayList<Ship> ships;
    private Ship hittedShip; 


    private CellState[][] playingFieldState;
    private ArrayList <Point> shotAvailableCells;
    private int destroyedShipCount;
    private boolean isShipsDisplayed=false; //true если включён режим вывода кораблей на экран
    private int fieldWidth;
    private int fieldHeight;
    private GuessResult lastGuessResult=GuessResult.OUT;
    private Point lastGuess;
    private RandomShipsPlacing shipsPlacing;
    private PlayingFieldStatistics pfStatistics;


    public PlayingField(GameConf gameConf){
        this.gameConf=gameConf;
        fieldHeight=gameConf.getHeight();
        fieldWidth=gameConf.getWidth();
        playingFieldState = new CellState[fieldHeight][fieldWidth];
        shotAvailableCells=new ArrayList<Point>(fieldHeight*fieldWidth);
//        ships=new ArrayList<Ship> (gameConf.getTotalShipCount()); 
        pfStatistics=new PlayingFieldStatistics(this); //так можно?
        initialize();
    }
    public void initialize(){
        destroyedShipCount=0;
//        Arrays.fill(playingFieldState, this);
//        Arrays.fill(playingFieldState, CellState.EMPTY);
        ships=new ArrayList<Ship> (gameConf.getTotalShipCount());
        for(int i=0;i<fieldHeight;i++){ 
            for(int j=0;j<fieldWidth;j++) {
                setCellState(j,i,CellState.EMPTY);
                shotAvailableCells.add(new Point(j,i));
            }
        }
//        shipsPlacing=new RandomShipsPlacing(gameConf);
    //    shipsPlacing=new ShipsOptimalPlacing(gameConf);

    }
    public int getWidth(){
        return fieldWidth;
    }

    public int getHeight(){
        return fieldHeight;
    }
    
    public ArrayList<Point> getShipTypes(){
        return gameConf.getShipTypes();
    }
    
    public ArrayList<Ship> getShips() {
        return ships;
    }    

    public void shipsPlacing(){
        ships=shipsPlacing.shipsPlacing();
    }

    public boolean isCellInside(int x, int y){
        return (x>-1 && y>-1 && x<fieldWidth && y<fieldHeight);
    }
    
    public boolean isCellInside(Point p){
        return PlayingField.this.isCellInside(p.x, p.y);
    }
//Проверяет попадает ли заданный корабль только на не открытые в ходе игры клетки
    public boolean isShipOnFreeArea(Point curp, int deckCount, boolean hor) {
        int dx=0;
        int dy=0;
        Point p = new Point(curp);
        if (hor) dx=1;
        else dy=1;

        for (int i=0;i<deckCount;i++){
            CellState temp=getCellState(p);            
            if (!isCellInside(p) || (getCellState(p)!=CellState.EMPTY
                    && getCellState(p)!=CellState.DECK)) {
                
                return false;
            } 
                    
            p.translate(dx, dy);
        }
        return true;
    }
    //нужно убрать, нарушаетинкапсуляцию
    public CellState[][] getPlayingFieldState(){
            return playingFieldState;
    }
                
    public CellState getCellState(Point p) {
        return getCellState(p.x,p.y); 
    }
    public CellState getCellState(int j, int i) {
        return playingFieldState[i][j]; 
    }
    
    private void setCellState(Point p,CellState cellStateType){
        setCellState(p.x, p.y,cellStateType);
    }
    
    private void setCellState(int j, int i,CellState cellStateType){
        playingFieldState[i][j]=cellStateType;
    }
    

     
    
    //показывает (отмечает) корабли на поле состояния. Если палуба подбита она уже отображена.
    public void displayShips(boolean display){
        if (isShipsDisplayed==display) return;
        isShipsDisplayed=display;
        for(Ship ship : ships){
            Deck[] decks=ship.getDecks();
            for (Deck deck:decks){
//                System.out.println(deck);
                if(isShipsDisplayed){
                    if (getCellState(deck.getPosition())==CellState.EMPTY)
                        setCellState(deck.getPosition(),CellState.DECK);
                }else{
                    if (getCellState(deck.getPosition())==CellState.DECK)
                        setCellState(deck.getPosition(),CellState.EMPTY);
                } 
            }
//            ArrayList<Point> nearArea=ship.getNearArea();
//            for(Point na:nearArea){
//                int i=na.y;
//                int j=na.x;
//                if(isShipsDisplayed){
//                    if (playingFieldState[i][j]==CellState.EMPTY)
//                        playingFieldState[i][j]=CellState.NEARAREA;
//                    
//                }else{
//                    if (playingFieldState[i][j]==CellState.NEARAREA)
//                        playingFieldState[i][j]=CellState.EMPTY;
//                } 
//                
//            }
            //для теста
/*			for (Point p:ship.getNearArea()){
                    playingFieldState[p.y][p.x]=CellState.NEARAREA;
            } */


        }

    }

    //возвращаем результат выстрела
    public GuessResult checkGuess(Point guess){
        
        shotAvailableCells.remove(guess);
	hittedShip = getShip(guess); //
	if (hittedShip!=null){ //попали в корабль
            if(getCellState(guess)==CellState.HITTEDDECK) 
                lastGuessResult= GuessResult.HIT2;
            else {
                setCellState(guess,CellState.HITTEDDECK);
                if (hittedShip.isDestroyed()) {
                    for (Point na : hittedShip.getNearArea()){
                        if (getCellState(na)==CellState.EMPTY){ 
                                setCellState(na,CellState.NEARAREA);
                                shotAvailableCells.remove(na);
                            }
                    }
                    if (++destroyedShipCount==gameConf.getTotalShipCount()) lastGuessResult=GuessResult.GAMEOVER;
                    else lastGuessResult=GuessResult.DESTROY;
                }
                else lastGuessResult=GuessResult.HIT;
            }

		
	} else{ //промах
            if (getCellState(guess)==CellState.SHOT ||
                getCellState(guess)==CellState.NEARAREA) lastGuessResult=GuessResult.OUT2;
            else {
                lastGuessResult=GuessResult.OUT;
                setCellState(guess,CellState.SHOT);
            }

	}
	lastGuess=guess;
        updateStatistics();
	return lastGuessResult;
        
        
        
        
//        lastGuess=guess;
//        shotAvailableCells.remove(guess);//удаляем сделанный ход из списка доступных
//        switch (playingFieldState[guess.y][guess.x]){
//            case EMPTY:
//            case DECK: //на случай если включён режим видимости кораблей
//                for (Ship ship : ships){
////						System.out.println(ship);
//                    Deck decks[]=ship.getDecks();
//                    //проверяем есть ли попадание в корабль
//                    for (int i=0;i<decks.length; i++){
//                        if (guess.equals(decks[i].getPosition())){
//                            decks[i].setHitted();
//                            playingFieldState[guess.y][guess.x]=CellState.HITTEDDECK;
//                            //если было попадание, но подбиты не все палубы значит корабль ранен
//                            for (Deck d : ship.getDecks()){
//                                if(!d.isHitted()) {
//                                    lastGuessResult= GuessResult.HIT;
//                                    return lastGuessResult;
//                                } 
//                            }
//                            //если подбиты все палубы, корабль уничтожен
//                            //пустые ячейки вокруг подбитого корабля помечаем NEARAREA
//                            for (Point na : ship.getNearArea()){
//                                if (playingFieldState[na.y][na.x]==CellState.EMPTY){ 
//                                        playingFieldState[na.y][na.x]=CellState.NEARAREA;
//                                        shotAvailableCells.remove(na);
//                                    }
//
//                            }
//                            destroyedShipCount++;
//                            //если подбиты все корабли игра закончена
//                            if (destroyedShipCount==gameConf.getTotalShipCount()) {
//                                lastGuessResult= GuessResult.GAMEOVER;
//                                return lastGuessResult;
//                            }
//
//                            lastGuessResult= GuessResult.DESTROY;
//                            return lastGuessResult;
//                        }
//                    }
//                }
//                    //нет попадания в корабль
//                    playingFieldState[guess.y][guess.x]=CellState.SHOT; //помечаем, что по пустой клетке был произведён выстрел
//                    lastGuessResult= GuessResult.OUT; //нет попадания в корабль
//                    return lastGuessResult;
//            case SHOT: //повторный выстрел в клетку
//                    lastGuessResult= GuessResult.OUT2;
//                    return lastGuessResult;
//            case HITTEDDECK: //выстрел в уже подбитую палубу
//                    lastGuessResult= GuessResult.HIT2;
//                    return lastGuessResult;
//            case NEARAREA: //выстрел в соприкасающуюся с подбитым кораблём клетку
//                    lastGuessResult= GuessResult.OUT2;
//                    return lastGuessResult;
//            default: throw new IllegalArgumentException();
//            
//        }
//       

    }
    
    
    //если корабль содержит палубы с заданными координатами возвращает соответствующий объект Ship
    private Ship getShip (Point p){
        
        for (Ship s:ships)if(s.isHitted(p)) return s;
                
        return null;
    }
    
    public int getDestroyedShipDeckCount(){
        if (lastGuessResult!=GuessResult.DESTROY) return 0;
        return hittedShip.getDeckCount();
    }
    
    public GuessResult getLastGuessResult(){
        return lastGuessResult;
    }
    
    public Point getLastGuess(){
        return lastGuess;
    } 
    
    public Point getRandomShotAvailableCell(){
        return shotAvailableCells.get((int)(Math.random()*shotAvailableCells.size()));
    }
    
    public boolean isShotAvailableCell(int i, int j){ //i-y; j-x
        return getCellState(j,i)==CellState.EMPTY ||
                getCellState(j,i)==CellState.DECK;
    }
    
    public boolean isShotAvailableCell (Point p){
        return PlayingField.this.isShotAvailableCell(p.y,p.x);
    }

    public boolean removeShip(Ship currentShip) {
        return ships.remove(currentShip);
    }
    
    //не проверяет на пересечение
    public void addShip(Ship currentShip) {
        ships.add(currentShip);
       
    }
    //можно было это сделать в конструкторе, но image нужен не для всех поле, а только при редактировании поля игрока
    public void createShipsImage(){
//        for(Ship s:ships) s.createImage();
        //это NetBeans предложил вместо закомментированного выше
        ships.forEach((s) -> {
            s.createImage();
                });
    }
    //для экономии памяти. После того как редактирование поля закончено image не нужен
    public void deleteShipsImage(){
//        for(Ship s:ships) s.deleteImage();
        //это NetBeans предложил вместо закомментированного выше
        ships.forEach((s) -> {
            s.deleteImage();
                });
    }

    public void fitNearArea(){
//        ships.forEach((s) -> s.getNearArea().removeIf(p-> (p.x<0 || p.y<0 || p.x>(fieldWidth-1) || p.y>(fieldHeight-1))));
        ships.forEach((s) -> s.getNearArea().removeIf(p-> !isCellInside(p)));
        
    }

    public void setShipsPlacing(RandomShipsPlacing shipsPlacing) {
        this.shipsPlacing=shipsPlacing; //To change body of generated methods, choose Tools | Templates.
    }

    public GameConf getGameConf() {
        return gameConf;
    }

    public int getTotalDeckCount() {
        return pfStatistics.getTotalDeckCount();
    }
    public int getTotalShipCount() {
        return pfStatistics.getTotalShipCount();
    }

    public int getTotalFreeCellCount() {
        return pfStatistics.getTotalFreeCellCount();
    }
    public int getHittedDeckCount() {
        return pfStatistics.getHittedDeckCount();
    }

    public int getShotCount() {
        return pfStatistics.getShotCount();
    }

    public int getNearAreaCount() {
        return pfStatistics.getNearAreaCount();
    }
    public double getAvailableEdgeCellRatio(){
        return pfStatistics.getAvailableEdgeCellRatio();
    }
    
    public double getAvailableInnerCellRatio(){
        return pfStatistics.getAvailableInnerCellRatio();
    }
    
    
    public void updateStatistics(){
        pfStatistics.updateStatistics();
    }
}

