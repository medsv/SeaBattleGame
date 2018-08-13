package seabattlegame;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

/**
 * <p>Корабль</p>
 * 
 */

public class Ship
{
	private int deckCount;
	private Deck[] decks;
	private boolean destroyed;
	private ArrayList <Point> nearArea;
	private boolean horArrangement;
        private Rectangle image;


	public Ship(Point leftUpperDeck, int deckCount, boolean horArrangement)	{
		this.deckCount=deckCount;
		this.horArrangement=horArrangement;
		decks=new Deck[deckCount];
                for(int i=0; i<deckCount;i++){
    			decks[i]=new Deck ();
                }
                setPosition(leftUpperDeck,horArrangement);
                
              
	}
        public void setPosition(Point leftUpperDeck, boolean horArrangement) {
		int dx, dy; //направление в котором будут прикрепляться палубы относительно левой верхней
                if (horArrangement) { 
			dx=1; dy=0;
		}
		else {
			dx=0; dy=1;
		}
		Point p = leftUpperDeck;
//		System.out.println(deckCount);
		for(int i=0; i<deckCount;i++){
                        decks[i].setPosition(p);
			p.x=p.x+dx;
			p.y=p.y+dy;
		}
		setNearArea();
                
        
        }
	
	//формирование ближней зоны корабля
	private void setNearArea(){
            
            HashSet <Point> tempNearArea = new HashSet<>() ; //чтобы не добавлялись дубликаты
            Point p;
            int i, j;
            for (Deck d:decks)
                    for (i=-1;i<2;i++)
                            for(j=-1;j<2;j++){
                                    p=new Point(d.getPosition());
                                    p.translate(i, j);
                                    tempNearArea.add(p);
                            }

            for (Deck d:decks) tempNearArea.remove(d.getPosition()); //удаляем палубы из ближней зоны
            

            nearArea = new ArrayList<>(tempNearArea);            
            
//            nearArea= new ArrayList <Point>(deckCount*2+6);
//            int dx, dy; 
//            if (horArrangement) { 
//                    dx=0; dy=1;
//            }
//            else {
//                    dx=1; dy=0;
//            }
//            Point p;
//            boolean b;
//            for(int i=0;i<deckCount;i++){
//                    p=new Point(decks[i].getPosition());
//                    p.translate(dx,dy);
//                    b=nearArea.add(p);
//                    p=new Point(decks[i].getPosition());
//                    p.translate(-dx,-dy);
//                    b=nearArea.add(p);
//
////			System.out.println(p);
////			b=nearArea.add(new Point(decks[i].getPosition()));
//            }
//            p=new Point(decks[0].getPosition());
//            p.translate(-dy,-dx);
//            b=nearArea.add(p);
//            p=new Point(decks[0].getPosition());
//            p.translate(dx,dy);
//            p.translate(-dy,-dx);
//            b=nearArea.add(p);
//            p=new Point(decks[0].getPosition());
//            p.translate(-dx,-dy);
//            p.translate(-dy,-dx);
//            b=nearArea.add(p);		
//
//            p=new Point(decks[deckCount-1].getPosition());
//            p.translate(dy,dx);
//            b=nearArea.add(p);
//            p=new Point(decks[deckCount-1].getPosition());
//            p.translate(dx,dy);
//            p.translate(dy,dx);
//            b=nearArea.add(p);
//            p=new Point(decks[deckCount-1].getPosition());
//            p.translate(-dx,-dy);
//            p.translate(dy,dx);
//            b=nearArea.add(p);		
		
//		System.out.println(nearArea);
	}


	public String toString() {
		String str;
		str= "Корабль, палуб " + deckCount + ":";
		for(int i=0;i<deckCount;i++){
			str = str+"\n" + (i+1) + " "+ decks[i]; //+decks[i].toString();
		}
		str=str + "\n" + "Ближняя зона" + "\n";
		for (int j=0;j<nearArea.size();j++){
			str=str+"(" +nearArea.get(j).x+","+nearArea.get(j).y+")"+" ";
		}

		return str;
		
	}

	/**
	 * <p>Если подбиты все палубы - возвращается true, в противном случае - false</p>

	 */
	
	public boolean isDestroyed() {
		// TODO implement me
		return destroyed;
	}

	/**
	 * <p>Зона вокруг корабля в которой не должен находиться другой корабль</p>

	 */
	
	public Deck[] getDecks() {

		return decks;
	}
	
	
	public ArrayList<Point> getNearArea() {
		return nearArea;
	}
	/**
	 * <p>Входные параметры -количество палуб

	 */
	
	public static Ship createRandomShip(int deckCount, int fieldWidth, int fieldHeight) {
		
		boolean hor;
		int i= (int)Math.round(Math.random());
		if (i==0) hor=true;
		else hor=false;
		
		
		int width, height; //прямоугольник в котором размещается верхний левый угол корабля
		if (hor) {
			width=fieldWidth-deckCount+1;
			height=fieldHeight;
		}
		else{
			width=fieldWidth;
			height=fieldHeight-deckCount+1;
		}
		
		if (width<1 || height<1) return null; //корабль не вмещается в отведённое пространство
		
		Ship ship = new Ship (new Point((int)(width*Math.random()),(int)(height*Math.random())), deckCount, hor);
//		ship.fitNearArea(fieldWidth,fieldHeight);	
		return ship;
	}
	
	//удаляем клетки ближней зоны корабля которые выходят за рамки игрового поля
//	public void fitNearArea(int fieldWidth, int fieldHeight){
//		if (decks[0].getPosition().x<1 || decks[0].getPosition().y<1 
//			|| decks[deckCount-1].getPosition().x>(fieldWidth-2) || decks[deckCount-1].getPosition().y>(fieldHeight-2)) {
//			
//			Iterator <Point> iter = nearArea.iterator();
//			while (iter.hasNext()){
//				Point p = iter.next();
//				if (p.x<0 || p.y<0 || p.x>(fieldWidth-1) || p.y>(fieldHeight-1)) iter.remove();
//			}
//			System.out.println("Корабль прижат к границе ");
//		} 
//	}
	
	//проверяет пересекает ли один корабль другой корабль или ближнюю зону другого корабля
	public boolean intersects(Ship ship){
		Deck[] otherdecks=ship.getDecks();
		for(Deck od:otherdecks){
			for(Deck d : decks){
				if (d.getPosition().equals(od.getPosition())) return true;

			}
		}
		
		ArrayList <Point> otherNearArea=ship.getNearArea();
		
		Deck[]  testdecks;
		ArrayList<Point> testneararea;
		
		//выбираем оптимальную комбинацию для проверки пересечения ближней зоны и палуб кораблей
		
		if((otherNearArea.size()*decks.length)<(nearArea.size()*otherdecks.length)){
			testdecks=decks;
			testneararea=otherNearArea;
			}
		{
			testdecks=otherdecks;
			testneararea=nearArea;
		}
		
		for(Point tna:testneararea){
			for(Deck d : testdecks){
				if (tna.equals(d.getPosition())) return true;
			}
		}
			
		return false;
	}

//    public boolean isDeck(Point p) {
//        for(Deck d:decks) 
//            if (p.equals(d.getPosition())) return true;
//        return false;
//    }

    public int getDeckCount() {
        return deckCount;
    }
    
    public void createImage(){
        image=new Rectangle();        
    }
    
    public Rectangle getImage(int xStep, int yStep){
        int kx,ky;
        if (horArrangement){
            kx=deckCount;
            ky=1;
        } else{
            kx=1;
            ky=deckCount;
        }
        image.setBounds(decks[0].getPosition().x*xStep, decks[0].getPosition().y*yStep,
            kx*xStep,ky*yStep);
        return image;
    }

    public void deleteImage() {
        image=null; 
    }

    public Rectangle getImage(double xStep, double yStep) {
         return getImage((int)xStep, (int)yStep);
     }
    boolean isHitted (Point shot){
	boolean hitted=false;
	for (Deck d:decks) 
            if (shot.equals(d.getPosition())){ 
                d.setHitted();
                hitted=true;
                break;
            }
	if (hitted) {
            destroyed=true;
            for (Deck d:decks){ 
                if (!d.isHitted()) {
                    destroyed=false;
                    break;
                }
            }
        }
	
	return hitted;

    }

}