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

import java.util.Scanner;

public class Spider {
    //private final int MAX_MOVEMENT = 5;
    private final int MAX_LIFE = Calculate.SPIDER_LIFE;   //currentLife can never be above this value
    private final int MAX_MOVEMENT = Calculate.SPIDER_MOVEMENT_DISTANCE;
    private int currentLife;                        //Spider health, if it reaches 0, game over
    private int locationX;                          //row of spider
    private int locationY;                          //column of spider

    private static Scanner keyboard = new Scanner(System.in);      //user input


    public Spider(int webLength) {
        currentLife = MAX_LIFE;
        locationX = webLength / 2;
        locationY = webLength / 2;
    }

    public int getX() {
        return locationX;
    }

    public int getY() {
        return locationY;
    }

    public int getHealth() {
        return currentLife;
    }

    /**
     * called if in an element with a fly, consumes fly and adds life
     *
     * @param fly
     */
    public void eatFly(Fly fly) {
        //might need to call the web.checkElement()
        //and the flies array to remove it
        currentLife += fly.getEnergy();
        if (currentLife > MAX_LIFE)
            currentLife = MAX_LIFE;
    }

    /**
     * checks the health of the spider
     * @return
     */
    public boolean alive() {
        if (currentLife <= 0)
            return false;
        return true;
    }

    /**
     * Checks for valid user entry (in bounds of the array), and then adjusts the spider's position.
     * Can move in 8 directions, but only in that direction once per turn. In addition, there is a cap
     * far the spider can move in that turn
     *
     * Input example: wa11, which means up-left, 11 spaces
     * Input possible commands: [(w, a, s, d) (1-MAX_MOVEMENT)]
     *
     * The commands should also be able to be entered backwards, the method should plug into a switch
     * statement to see if the command was valid, then throw that to a in-range checker to make sure it's still
     * within the web indexes
     */
    public void move(Web web, String userInput) {


        final char UP = 'w';
        final char DOWN = 's';
        final char LEFT = 'a';
        final char RIGHT = 'd';

        int index;

        int changeX = 0;    //change in row index
        int changeY = 0;    //change in column index

        String stringDirection;    //gets the first occurrence of a direction (letters)
        String stringDistance;     //gets firs occurrence of a distance (number)
        boolean hasDirection;  //checks that user input has a direction
        boolean hasDistance;   //checks that user input has a distance
        char value;
        int distance;   //i


        boolean moved = false;  //flag for successful input
        while (!moved) {

            System.out.print("Spider Movement: ");
            userInput = keyboard.nextLine().toLowerCase();

            //reset flags
            hasDirection = false;
            hasDistance = false;
            stringDirection = "";
            stringDistance = "";
            index = 0;

            /*
            Hardcoded, we know the first input should be a direction.
            The second character is either another direction (diagonal), also != to first
            The third should either be a whiteSpace, or a distance (int)
            After that, continue to check if there are more distance values,
            as the move distance can be more than a single digit can provide (13)
             */
            if (userInput.length() >= 2) {
                if (Character.isAlphabetic(userInput.charAt(index)))
                {
                    stringDirection += userInput.charAt(index);
                    index++;
                    hasDirection = true;
                    if (Character.isAlphabetic(userInput.charAt(index))
                            && userInput.charAt(index) != userInput.charAt(index - 1)) {
                        stringDirection += userInput.charAt(index);
                        index++;
                    }
                    if (Character.isWhitespace(userInput.charAt(index))) {
                        index++;
                    }
                    if (Character.isDigit(userInput.charAt(index))) {
                        hasDistance = true;
                        while (index < userInput.length() && Character.isDigit(userInput.charAt(index))) {
                            stringDistance += Integer.toString(userInput.charAt(index) - '0');
                            index++;
                        }
                    }
                }
            }

            //Successfully split up the values,
            if (hasDirection && hasDistance) {
                distance = Integer.parseInt(stringDistance);
                if (distance > 0 && distance <= MAX_MOVEMENT) {
                    for (int i = 0; i < stringDirection.length(); i++) {
                        value = stringDirection.charAt(i);
                        switch (value) {
                            case UP:
                                changeX -= distance;
                                break;
                            case DOWN:
                                changeX += distance;
                                break;
                            case RIGHT:
                                changeY += distance;
                                break;
                            case LEFT:
                                changeY -= distance;
                                break;
                        }
                    }
                    //check that changes are in range
                    if (Calculate.checkBounds(web, locationX+changeX, locationY+changeY)) {
                        //NOTE: needs to changed to check bounds
                        locationX += changeX;
                        locationY += changeY;
                        moved = true;
                    }
                    else
                        System.out.println("Error: Out of Web");
                }
                else
                    System.out.println("Invalid distance");
            }
            else
                System.out.println("Invalid entry");
        }//end while
    }//end method
}//end of class