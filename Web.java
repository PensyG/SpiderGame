//name: Penny Grant/Reece Sharp
//date: 
//project:
/*
 * BUGS:
 *
 */

/*
 * IDEAS: if a fly escapes, say the cardinal direction it left in relative to the spider
 *
 */

public class Web {
    //used in checkElement(), and showVibration()

    private int[][] web;    //square web array of webLength length
    private int webLength;  //length of web array

    public Web(int x) {
        webLength = x;
        web = new int[webLength][webLength];
    }

    /**
     * @return the diameter of the square web 2D array
     */
    public int getWebLength() {
        return webLength;
    }
    public void setIndex(int i, int j, int vibration) {
        web[i][j] = vibration;
    }

    /**
     * @param i - row of 2D array
     * @param j - column of 2D array
     * @return returns the vibration value of the 2D indexes passed
     */
    public int getVibration(int i, int j) {
        return web[i][j];
    }
}//end of class