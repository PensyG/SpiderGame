//name: Penny Grant/Reece Sharp
//date: 
//project:

public class Web {
    private int[][] web;    //square web array of webLength length
    private int webLength;  //length of web array

    public Web(int x) {
        web = new int[x][x];
        webLength = x;
    }

    /**
     * displays the web, and the strength values of vibration. Higher values means
     * a fly is in that direction
     *
     * NOTE: works with only 1 fly, will adjust later to work with multiple, params will
     * change to ArrayLists so they're dynamic
     *
     * @param flies an ArrayList object, stores all of the Fly objects
     * @param spiderX row of spider, 0 - webLength
     * @param spiderY column of spider, 0 - webLength
     */
    public void vibrate(java.util.ArrayList<Fly> flies, int spiderX, int spiderY) {
        //define variables
        int formatLength;           //largest # of digits in web array (100 = 3, 54 = 2)

        //initialize variables
        int energy = webLength / 2;                     //energy of bug, based on size of web
        int differenceX = 0;        //difference in fly location row with current row
        int differenceY = 0;        //difference in fly location column with current column
        int distance = webLength;           //movement blocks from the fly location
        int flyX = 0;
        int flyY = 0;
        boolean fly;


        //dynamically set the formatting for the grid, rows of 1's
        //don't form accurate columns with 10's, etc
        formatLength = String.valueOf(energy).length();


        for (int i = 0; i < webLength; i++) {
            for (int j = 0; j < webLength; j++) {
                //reset
                fly = false;
                distance = webLength;
                for (int k = 0; k < flies.size(); k++) {

                    flyX = flies.get(k).getX(); //fly row
                    flyY = flies.get(k).getY(); //fly column

                    //is current element a fly?
                    if(flyX == i && flyY == j)
                        fly = true;

                    web[flyX][flyY] = energy;   //current fly location

                    differenceX = Math.abs(flyX - i);   //get difference in iteration row to bug row
                    differenceY = Math.abs(flyY - j);   //get difference in iteration column  and bug column
                    //if (distance < Math.max(differenceX, differenceY))

                    if (distance > Math.max(differenceX, differenceY)) {
                        distance = Math.max(differenceX, differenceY);  //greater = distance
                    }
                }


                //if out of range of vibrations, set so it subtracts to 0
                if (distance > energy)
                    distance = energy;

                web[i][j] = energy - distance;

                if (i == spiderX && j == spiderY)
                    System.out.print(String.format(" %-" + formatLength + "s ", "S"));
                else if(fly)
                    System.out.print(String.format(" %-" + formatLength + "s ", "F"));
                else
                    System.out.print(String.format(" %-" + formatLength + "d ", web[i][j]));
                //column of '-' (optional)
                if (j < webLength-1)
                    System.out.print("-");
            } //end column
            System.out.println();

            //row of '|' (optional)
            if (i >= 0 && i < webLength-1) {
                for (int j = 0; j < webLength; j++) {
                    System.out.print(String.format(" %-" + (formatLength + 2) + "s", "|"));
                }
            }
            System.out.println();
        } //end row

        //diagnostics on Fly location
        for(int i = 0; i < flies.size(); i++) {
            System.out.printf("X: %d, Y: %d\n", flies.get(i).getX(), flies.get(i).getY());
        }
    }
}//end of class