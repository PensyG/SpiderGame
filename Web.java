import java.util.ArrayList;

/**
 * The web contains a 2D array of int values, each element is calculated by the size and distance of each
 * Fly currently in the web. The Spider uses this web to move around, and find Fly objects to eat. It is a
 * Square 2D array based of sizes 50, 100, 200, or 400.
 */
public class Web {
    private int[][] web;                //square web array of webLength length
    private int webLength;              //length of web array

    /**
     * @param x - diameter of the web
     */
    public Web(int x) {
        webLength = x;
        web = new int[webLength][webLength];
    }

    /**
     * Checks if index sent is within the between 0 - (webLength - 1)
     *
     * @param i - row of the web array
     * @param j - column of the web web array
     * @return if (i, j) is inside of the bounds of the web array
     */
    public boolean checkBounds(int i, int j) {
        return (0 <= i && 0 <= j) && (i < webLength && j < webLength);
    }

    /**
     * @return the diameter of the square web 2D array
     */
    public int getWebLength() {
        return webLength;
    }

    /**
     * @param i - row of 2D array
     * @param j - column of 2D array
     * @return returns the vibration value of the 2D indexes passed
     */
    public int getVibration(int i, int j) {
        return web[i][j];
    }

    /**
     * sets i, j to be the vibration value calculated in the calculateElements
     *
     * @param i         - Row of the index
     * @param j         - Col of the index
     * @param vibration - power of the vibration, higher values mean more vibrations
     */
    private void setVibration(int i, int j, int vibration) {
        if (vibration < 0)
            vibration = 0;    //vibration value

        web[i][j] = vibration;
    }

    /**
     * Takes a turn for the web, by updating the web on the new Fly locations
     *
     * @param spider - Spider object, used for debugging to show spider location in web
     * @param flies  - ArrayList of the Fly objects, used to calculate the vibration values in each element
     */
    public void update(Spider spider, ArrayList<Fly> flies) {
        //update the web vibration values
        calculateElements(flies);

        //debug method
        //shows entire web, can be slow to generate in webs larger than 50 (console is inefficient)
        if (Game.DEBUG)
            debug(spider);
    }

    /**
     * calculates the value for each element in the web array by comparing all of the Fly objects
     * in the flies array with one another. Whichever vibration is largest is set to be the element
     */
    private void calculateElements(ArrayList<Fly> flies) {
        //define
        ArrayList<Integer> vibrateArray = new ArrayList<>(); //array of fly vibration at location
        int flyRow;               //current fly row
        int flyCol;              //current fly column
        int flyEnergy;          //energy of each fly, based on size of web, and size of fly
        int distance;           //movement blocks from the fly location (includes diagonal)

        //init
        int vibrateValue = 0;        //current vibration value for element

        for (int i = 0; i < webLength; i++) {
            for (int j = 0; j < webLength; j++) {
                for (Fly fly : flies) {
                    //get location and energy of specific fly
                    flyRow = fly.getRow();
                    flyCol = fly.getCol();
                    flyEnergy = fly.getEnergy();
                    //distance of specific fly to element
                    distance = getMaxDistance(i, j, flyRow, flyCol);
                    vibrateValue = flyEnergy - distance;      //power of vibration from fly to element
                    vibrateArray.add(vibrateValue);         //add vibration value to array
                }
                //find greatest vibration value at this element, clears after running through
                if (vibrateArray.size() > 0) {
                    for (int e : vibrateArray)
                        if (e > vibrateValue)
                            vibrateValue = e;
                    vibrateArray.clear();
                }
                //update vibration
                setVibration(i, j, vibrateValue);
            }
        }
    }

    /**
     * Calculates the amount of elements between (i, j) and (x, y), including diagonals
     *
     * @param i - Row of index
     * @param j - Column of index
     * @param x - row of element
     * @param y - column of element
     * @return the distance between two elements, any int < webLength
     */
    private int getMaxDistance(int i, int j, int x, int y) {
        int differenceX = Math.abs(x - i);        //difference in location row with current row 'i'
        int differenceY = Math.abs(y - j);        //difference in location column with current column 'j'

        return Math.max(differenceX, differenceY); //return which is larger, the row, or the column
    }

    /**
     * displays the web, and the strength values of vibration.
     *
     * @param spider - Spider object, used in showing the location of the spider in web
     */
    private void debug(Spider spider) {
        int formatLength;   //dynamic web display formatter

        System.out.println("DEBUG: Web");

        //find largest # in the array, and format based on that
        formatLength = String.valueOf(Fly.getMaxEnergy()).length();

        //formats column bars
        System.out.print("      ");
        for (int i = 0; i < webLength; i++)
            if (i % 5 == 0)
                System.out.print(String.format("%-" + ((formatLength * 5) + 5) + "d", i));
        System.out.println();

        for (int i = 0; i < webLength; i++) {
            //formats row bars
            if (i % 5 == 0)
                System.out.printf("%4d |", i);
            else
                System.out.printf("%4s |", "");

            //format what char to display in console
            for (int j = 0; j < webLength; j++) {
                if (spider.isSpider(i, j))
                    System.out.print(String.format("%-" + formatLength + "s ", "*"));
                else if(Fly.getFly(i, j) != null)
                    System.out.print(String.format("%-" + formatLength + "s ",  "F"));
                else
                    System.out.print(String.format("%-" + formatLength + "d ", getVibration(i, j)));
            }
            System.out.println();   //separate rows
        }
        System.out.println();
    }
}