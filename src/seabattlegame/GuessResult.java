package seabattlegame;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */
public enum GuessResult
{
    OUT, //выстрел мимо корабля
    OUT2,//выстрел по клетке, по которой уже производился выстрел
    HIT, //попадание в палубу корабля, корабль ранен
    HIT2,//попадание в ранее подбитую палубу 
    DESTROY, //попадание в палубу, корабль уничтожен 
    GAMEOVER;//попадание в палубу, уничтожен последний корабль
}
