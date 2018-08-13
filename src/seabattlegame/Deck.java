package seabattlegame;
import java.awt.Point;

/**
 * <p>Палуба корабля</p>
 * 
 * <p>&nbsp;</p>
 * 
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class Deck
{
	
    private Point position;
    private boolean hitted;

    public Deck(){
        position=new Point(); 
        hitted=false;
    }

    public Deck(Point position){
        this();
        this.position.setLocation(position);

    }

    public Point getPosition(){
        return position;
    }

    public void setPosition(Point position){
        this.position.setLocation(position);
    }


    @Override
    public String toString(){
        return "Палуба x=" + position.x +", y="+position.y;
    }
    public boolean isHitted(){
        return hitted;
    }

    public void setHitted(){
        hitted=true;
    }
}

