//name: Penny Grant/Reece Sharp
//date:
//project:

import java.util.ArrayList;

public class Calculate {
    //game constants (can be adjusted)

    //fly
    public static final int FLY_SIZE = 3;           // max size a fly can be, between 0 and FLY_SIZE

    //spider
    public static final int SPIDER_MOVEMENT_DISTANCE = 5;
    public static final int SPIDER_LIFE = 200;    //health can never be higher than this
    private static final int SPIDER_VIEW = 4; //radius of elements out from the spider that it can feel (1-3 work well)

    //game
    public static final int FLY_TURN_GEN = 1; //after 'X' amount of turns, generate a fly

    //web

    //calculate constants
    //used in checkElement(), and showVibration()
    private static final int EMPTY = 0;
    private static final int FLY = 1;
    private static final int SPIDER = 2;


    //game equations and constants NOTE: don't adjust


    //calculate methods

    /**
     * view distance around the spider, player shouldn't be able to see the entire web
     * could be based on the size of the web, or a static square around the spider
     *
     * @param web -
     * @param spider -
     */
    public static void generateSpiderView(Web web, Spider spider) {
        int viewRadius = SPIDER_VIEW; //radius of elements out from the spider that it can feel (1-3 work well)
        int viewDiameter = (viewRadius * 2) + 1; //diameter of the view circle
        int formatLength = String.valueOf(Fly.getMaxEnergy()).length(); //formats the width of the spaces
        int diameterCounter = 0;    //used for displaying the dimensions of the viewDiameter

        System.out.println("Spider View:");
        for (int i = 0; i < web.getWebLength(); i++) {
            for (int j = 0; j < web.getWebLength(); j++) {
                //is spot is spider
                if (Calculate.checkElement(i, j, spider) == SPIDER) {
                    System.out.print(String.format("%-" + formatLength + "s ", "*"));
                    diameterCounter++;
                }
                //get local energies
                else if (getDistance(i, j, spider.getX(), spider.getY()) <= viewRadius) {
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
     * checks to see if anything is in the element (i, j) in the web array
     *
     * @param i      - the row of the array
     * @param j      - the column of the array
     * @param spider - Spider object
     * @return an int value (0 = empty, 1 = fly, 2 = spider)
     */
    public static int checkElement(int i, int j, Spider spider) {
        //location matches a Fly in the flies array
        for (int k = 0; k < Fly.flies.size(); k++)
            if (i == Fly.flies.get(k).getX() && j == Fly.flies.get(k).getY())
                return FLY;
        //location matches the spider's
        if (i == spider.getX() && j == spider.getY())
            return SPIDER;
        //not the other, empty
        return EMPTY;

    }

    /**
     * Calculates the amount of elements between (i, j) and (x, y), including diagonals
     *
     * @param i - Row of index
     * @param j - Column of index
     * @param x - row of element
     * @param y - row of element
     * @return the distance between two elements, any int < webLength
     */
    public static int getDistance(int i, int j, int x, int y) {
        int differenceX = Math.abs(x - i);        //difference in location row with current row 'i'
        int differenceY = Math.abs(y - j);        //difference in location column with current column 'j'

        return Math.max(differenceX, differenceY); //return which is larger, the row, or the column
    }

    /**
     * calculates the value for each element in the web array by comparing all of the Fly objects
     * in the flies array with one another. Whichever vibration is largest is set to be the element
     */
    public static void calculateElements(Web web) {
        //define
        ArrayList<Integer> energyArray = new ArrayList<>(); //array of fly vibration at location
        int flyX;               //current fly row
        int flyY;               //current fly column
        int flyEnergy;          //energy of each fly, based on size of web, and size of fly
        int distance;           //movement blocks from the fly location (includes diagonal)

        //init
        int vibration = 0;        //current vibration value for element

        //reset variables
        for (int i = 0; i < web.getWebLength(); i++) {
            for (int j = 0; j < web.getWebLength(); j++) {

                //each fly in array
                for (int k = 0; k < Fly.flies.size(); k++) {
                    //get location and energy of specific fly
                    flyX = Fly.flies.get(k).getX();
                    flyY = Fly.flies.get(k).getY();
                    flyEnergy = Fly.flies.get(k).getEnergy();

                    //distance of specific fly to element
                    distance = getDistance(i, j, flyX, flyY);

                    vibration = flyEnergy - distance;      //power of vibration from fly to element
                    energyArray.add(vibration);         //add vibration value to array
                }

                //find greatest vibration value at this element, clears after running through
                if (energyArray.size() > 0) {
                    for (int p : energyArray)
                        if (p > vibration)
                            vibration = p;
                    energyArray.clear();
                }

                //update vibrations in each element
                if (vibration > 0)
                    web.setIndex(i, j, vibration);    //vibration value
                else
                    web.setIndex(i, j, vibration = 0);          //outside of vibration zone
            }
        }
    }

    //DEBUG METHODS

    /**
     * Checks if index sent is within the web based on the
     * @param web -  The web object, gets the length
     * @param i - row of the web array
     * @param j - column of the web web array
     * @return if (i, j) is inside of the bounds of the web array
     */
    public static boolean checkBounds(Web web, int i, int j) {
        if (i < web.getWebLength() && j < web.getWebLength())
            if (0 < i && 0 < j)
                return true;
        return false;
    }

    /**
     * displays the web, and the strength values of vibration. Higher values means
     * a fly is in that direction
     *
     * @param web - Web object, used for it's webLength
     */
    public static void debugWeb(Web web, Spider spider) {
        //define
        int formatLength;   //dynamic web display formatter

        //init
        int maxEnergy = 0;  //formatting for the web array

        System.out.println("DEBUG: Web Array");

        for (int k = 0; k < Fly.flies.size(); k++) {
            //find max energy to format the web display
            if (maxEnergy < Fly.flies.get(k).getEnergy())
                maxEnergy = Fly.flies.get(k).getEnergy();
        }
        //find largest # in the array, and format based on that
        formatLength = String.valueOf(Fly.getMaxEnergy()).length();

        //formats column bars
        System.out.print("      ");
        for (int i = 0; i < web.getWebLength(); i++)
            if (i % 5 == 0)
                System.out.print(String.format("%-" + ((formatLength * 5) + 5) + "d", i));
        System.out.println();

        for (int i = 0; i < web.getWebLength(); i++) {
            //formats row bars
            if (i % 5 == 0)
                System.out.printf("%4d |", i);
            else
                System.out.printf("%4s |", "");

            for (int j = 0; j < web.getWebLength(); j++) {
                //format what char to display in console
                if (Calculate.checkElement(i, j, spider) == SPIDER)
                    System.out.print(String.format("%-" + formatLength + "s ", "*"));
                else
                    System.out.print(String.format("%-" + formatLength + "d ", web.getVibration(i, j)));

            } //end column
            System.out.println();
        } //end row
        System.out.println();
    } //end method

    /**
     *  diagnostics on current Fly information
     */
    public static void debugFlies() {
        System.out.println("DEBUG: flies");
        for (int i = 0; i < Fly.flies.size(); i++) {    //struggle
            System.out.printf("#: %3d, ROW: %3d, COLUMN: %3d, SIZE: %d, ENERGY: %3d\n",
                    i,
                    Fly.flies.get(i).getX(),
                    Fly.flies.get(i).getY(),
                    Fly.flies.get(i).getSize(),
                    Fly.flies.get(i).getEnergy());
        }
        System.out.println("End Flies ------\n");
    }

}//end of class

