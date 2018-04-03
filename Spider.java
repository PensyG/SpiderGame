//name: Penny Grant/Reece Sharp
//date:
//project:

public class Spider
{
    private int energy;
    private int locationX;      //row of spider
    private int locationY;      //column of spider

    public Spider(int webLength) {
        locationX = webLength / 2;
        locationY = webLength / 2;
    }

    public int getX() {
        return locationX;
    }

    public int getY() {
        return locationY;
    }
}//end of class