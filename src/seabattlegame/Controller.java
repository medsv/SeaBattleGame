/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegame;



/**
 *
 * @author sergey
 */
interface Controller {
    public void setCurrentGameConf(int longestShipDeckCount);
    public GameConf getCurrentGameConf();
    public void startNewGame();
    public Object getPrimaryStage();
    public PlayingField getManPlayingField();
    public PlayingField getComputerPlayingField();

    public void controlGame();
}
