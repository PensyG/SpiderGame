//name: Penny Grant/Reece Sharp
//date: 
//project:

import java.util.ArrayList;

/*
 * BUGS:
 *
 */

/*
 * IDEAS:
 *
 */

public class Web {
    private final int EMPTY = 0;
    private final int FLY = 1;
    private final int SPIDER = 2;

    private int[][] web;    //square web array of webLength length
    private int webLength;  //length of web array

    public Web(int x) {
        webLength = x;
        web = new int[webLength][webLength];
    }

    public int getWebLength() {
        return webLength;
    }

    /**
     * displays the web, and the strength values of vibration. Higher values means
     * a fly is in that direction
     *
     * @param flies  - an ArrayList object, stores all of the Fly objects
     * @param spider - Spider object
     */
    public void vibrate(java.util.ArrayList<Fly> flies, Spider spider) {
        //define
        int formatLength;   //dynamic web display formatter
        Fly currentFly;

        //init
        int maxEnergy = 0;  //formatting for the web array
        int flySize = 0;    //current fly size

        //update web
        calcElements(flies);

        for (int i = 0; i < webLength; i++) {
            for (int j = 0; j < webLength; j++) {
                //reset flag/algorithm variables

                for (int k = 0; k < flies.size(); k++) {
                    if (maxEnergy < flies.get(k).getEnergy())  //find max energy to format the web display
                        maxEnergy = flies.get(k).getEnergy();

                    //if current location is fly, get fly
                    if (flies.get(k).getX() == i && flies.get(k).getY() == j) {
                        currentFly = getFlyAtLocation(flies, i, j);
                        flySize = currentFly.getSize();
                    }
                }

                formatLength = String.valueOf(maxEnergy).length();
                //format what char to display in console
                if (checkElement(i, j, flies, spider) == SPIDER)
                    System.out.print(String.format(" %-" + formatLength + "s  ", "S"));
                else if (checkElement(i, j, flies, spider) == FLY)
                    System.out.print(String.format(" %d%-" + formatLength + "s ", flySize, "F"));
                else
                    System.out.print(String.format(" %-" + formatLength + "d  ", web[i][j]));

            } //end column
            System.out.println();
        } //end row

        //diagnostics on Fly location
        for (int i = 0; i < flies.size(); i++) {
            System.out.printf("ROW: %3d, COLUMN: %3d, SIZE: %d, ENERGY: %2d\n",
                    flies.get(i).getX(), flies.get(i).getY(),
                    flies.get(i).getSize(), flies.get(i).getEnergy());
        }
    }


    /**
     * calculates the value for each element in the web array
     *
     * @param flies ArrayList of Fly objects
     */
    private void calcElements(java.util.ArrayList<Fly> flies) {
        //define
        ArrayList<Integer> energyArray = new ArrayList<>(); //array of fly vibration at location
        int differenceX;        //difference in fly location row with current row 'i'
        int differenceY;        //difference in fly location column with current column 'j'
        int distance;           //movement blocks from the fly location (includes diagonal)
        int flyX;               //current fly row
        int flyY;               //current fly column
        boolean isFly;            //is current location a fly
        int vibration;        //current vibration value for element

        //init
        int energy = 0;         //energy of each bug, based on size of web, and size of bug

        for (int i = 0; i < webLength; i++) {
            for (int j = 0; j < webLength; j++) {
                //reset variables
                vibration = 0;
                isFly = false;
                //distance = webLength;

                //each fly in array
                for (int k = 0; k < flies.size(); k++) {

                    //get location and energy of fly
                    energy = flies.get(k).getEnergy();
                    flyX = flies.get(k).getX();
                    flyY = flies.get(k).getY();



                    //is current element a fly?, used setting elements and displaying the web
                    differenceX = Math.abs(flyX - i);   //get difference in iteration row and bug row
                    differenceY = Math.abs(flyY - j);   //get difference in iteration column and bug column

                    distance = Math.max(differenceX, differenceY); //distance of specific fly to current index

                    vibration = energy - distance;      //power of vibration from fly to index
                    energyArray.add(vibration);         //add to array
                }

                //find greatest vibration value at index
                if (energyArray.size() > 0) {
                    for (int p : energyArray)
                        if (p > vibration)
                            vibration = p;
                    energyArray.clear();    //clear array of values for next index check after finding max


                }

                //needs to know what fly is at that location
                if (isFly)                    //location is a fly
                    web[i][j] = energy;     //energy of fly
                else if (vibration > 0)
                    web[i][j] = vibration;    //vibration value (energy - distance)
                else
                    web[i][j] = 0;          //outside of vibration zone
            }
        }
    }

    /**
     * checks to see if anything is in the element passed (i, j) in the web array
     *
     * @param i      - the row of the array
     * @param j      - the column of the array
     * @param flies  - ArrayList of Fly objects
     * @param spider - Spider object
     * @return an int value (0 = empty, 1 = fly, 2 = spider)
     */
    public int checkElement(int i, int j, java.util.ArrayList<Fly> flies, Spider spider) {
        for (int k = 0; k < flies.size(); k++)
            if (i == flies.get(k).getX() && j == flies.get(k).getY())
                return FLY;
        if (i == spider.getX() && j == spider.getY())
            return SPIDER;
        return EMPTY;
    }

    /**
     * Passes the index in the web Array, and finds the fly in the flies ArrayList and returns it
     * This function is used in setting the vibrations in the web Array, and for displaying the size
     * of the flies at their index, respectively. Should ALWAYS return a valid Fly.
     *
     * Note: the current structure of the program necessitates this, could refactor this method later
     *
     * @param flies - Arraylist of Fly objects
     * @param i - Row of the web
     * @param j - Column of the web
     * @return - the fly located at that element
     */
    public Fly getFlyAtLocation(java.util.ArrayList<Fly> flies, int i, int j) {
        //define
        int x, y;

        //init
        Fly fly = null;

        //for each fly in array, compare their locations to indexes passed
        for (int k = 0; k < flies.size(); k++) {
            x = flies.get(k).getX();
            y = flies.get(k).getY();

            if (i == x && j == y)
                fly = flies.get(k);
        }
        //will always be in range (checks are done before method call)
        return fly;
    }
}//end of class