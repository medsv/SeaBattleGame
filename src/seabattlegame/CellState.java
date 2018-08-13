package seabattlegame;

/**
 * <p>Состояние ячейки игрового поля:</p>
 * 
 * <p>EMPTY - пустая</p>
 * 
 * <p>DECK - в клетке размещена палуба корабля</p>
 * 
 * <p>SHOT - по клетке стреляли</p>
 * 
 * <p>HITTEDDECK - в клетке размещена подбитая палуба корабля</p>
 * 
 * <p>&nbsp;</p>
 * 
 * <p>&nbsp;</p>
 * 
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */
public enum CellState
{
    EMPTY, //неоткрытая клетка (может быть либо пустой, либо палубой)
    DECK, //палуба корабля
    SHOT, //выстрел по пустой клетке
    HITTEDDECK,//подбитая палуба 
    NEARAREA;//ближняя зона подбитого корабля
	
    @Override
    public String toString() {

        switch(this) {
            case EMPTY: return "Пустая";
            case DECK: return "Палуба корабля";
            case SHOT: return "Воронк"; //а от снаряда";
            case HITTEDDECK: return "Подбитая палуба корабля";
            case NEARAREA: return "Ближняя зона"; //появляется в свободных ячейках после подбития корабля
            default: throw new IllegalArgumentException();
        }
    }
}


