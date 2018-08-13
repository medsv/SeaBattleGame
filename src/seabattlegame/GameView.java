package seabattlegame;



public  interface GameView
{
    void updateManView(); 
    void updateComputerView(); 
    void updateViews(); //если можно обновить только сразу два, например, консоль
}

