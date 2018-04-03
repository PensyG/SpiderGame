//name: Penny Grant/Reece Sharp
//date:
//project:

public class Spider {
    private int life;         //if it reaches 0, game over, based on web size
    private int locationX;      //row of spider
    private int locationY;      //column of spider

    public Spider(int webLength) {
        life = webLength * 2;
        locationX = webLength / 2;
        locationY = webLength / 2;
    }

    public int getX() {
        return locationX;
    }

    public int getY() {
        return locationY;
    }

    public void eatFly(Fly fly) {
        life += fly.getEnergy();
    }
    public boolean alive() {
        if (life <= 0)
            return false;
        return true;
    }

    /**
     * view distance around the spider, player shouldn't be able to see the entire web
     * could be based on the size of the web, or a static square around the spider
     *
     * @param web
     */
    public void generateView(Web web) {

    }

}//end of class