//name: Penny Grant/Reece Sharp
//date:
//project:

/*
 * BUGS:
 *
 */

/*
 * IDEAS:
 *
 * Change Spider display to "_S_", would require rewrite of entire method
 *
 */

import java.util.ArrayList;

public class Spider {
    private static int MAX_MOVEMENT;
    private final int MAX_LIFE;     //currentLife can never be above this value
    private int life;               //Spider health, if it reaches 0, game over
    private int locationRow;        //row of spider
    private int locationCol;        //column of spider
    private int hungerConstant;

    public Spider(int webLength) {
        MAX_MOVEMENT = webLength / 10;
        MAX_LIFE = webLength / 2;
        life = MAX_LIFE;
        locationRow = webLength / 2;
        locationCol = webLength / 2;
        hungerConstant = webLength / 50;
    }

    public int getRow() {
        return locationRow;
    }

    public int getCol() {
        return locationCol;
    }


    public static boolean checkDistance(int movement) {
        return (movement <= MAX_MOVEMENT) && (movement > 0);
    }

    private void hunger() {
        life -= hungerConstant;
    }

    /**
     * Checks if a fly is
     *
     * @param flies
     */
    private void eat(ArrayList<Fly> flies) {
        Fly fly;
        for (int i = 0; i < flies.size(); i++) {
            fly = flies.get(i);
            if (fly.getRow() == locationRow && fly.getCol() == locationCol) {
                life += fly.getEnergy();
                if (life > MAX_LIFE)
                    life = MAX_LIFE;
            }

        }

    }

    /**
     * checks the health of the spider, if at, or below 0, the game ends
     *
     * @return
     */
    public boolean alive() {
        if (life <= 0)
            return false;
        return true;
    }

    /**
     * iterates the characteristics of the spider, is called each turn
     *
     * @param web
     * @param flies
     */
    public void update(Web web, ArrayList<Fly> flies) {

        if (alive()) {
            System.out.printf("Life: %3d, Max Movement: %d%n", life, MAX_MOVEMENT);
            hunger();   //subtract life from spider
        }
    }

    /**
     * generates a square of view around the spider, of SPIDER_VIEW radius
     *
     * @param web -
     */
    public void generateSpiderView(Web web) {
        final int viewRadius = 3; //radius of elements out from the spider that it can feel (1-3 work well)
        final int viewDiameter = (viewRadius * 2) + 1; //diameter of the view circle
        int formatLength = String.valueOf(Fly.getMaxEnergy()).length(); //formats the width of the spaces
        int diameterCounter = 0;    //used for displaying the dimensions of the viewDiameter

        for (int i = 0; i < web.getWebLength(); i++) {
            for (int j = 0; j < web.getWebLength(); j++) {
                //is spot is spider
                if (checkSpider(i, j)) {
                    System.out.print(String.format("%-" + formatLength + "s ", "*"));
                    diameterCounter++;
                }
                //get local energies
                else if (web.getDistance(i, j, this.getRow(), this.getCol()) <= viewRadius) {
                    System.out.print(String.format("%-" + formatLength + "d ", web.getVibration(i, j)));
                    diameterCounter++;
                }

                //format the viewing square around the spider
                if (viewDiameter - diameterCounter == 0) {
                    System.out.println();
                    diameterCounter = 0;
                }
            }
        }
    }

    /**
     * checks to see if the spider is in the element (i, j) in the web array
     *
     * @param i - the row of the array
     * @param j - the column of the array
     */
    public boolean checkSpider(int i, int j) {
        return i == locationRow && j == locationCol;
    }

    /**
     * Checks for valid user entry (in bounds of the array), and then adjusts the spider's position.
     * Can move in 8 directions, but only in that direction once per turn. In addition, there is a cap
     * far the spider can move in that turn
     * <p>
     * Input example: wa11, which means up-left, 11 spaces
     * Input possible commands: [(w, a, s, d) (1-MAX_MOVEMENT)]
     * <p>
     * The commands should also be able to be entered backwards, the method should plug into a switch
     * statement to see if the command was valid, then throw that to a in-range checker to make sure it's still
     * within the web indexes
     */
    public void move(int row, int col) {
        locationRow = row;
        locationCol = col;
    }
}
