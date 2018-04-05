import java.util.ArrayList;

/**
 *
 */
public class Spider {
    private final int MAX_MOVEMENT = 5;         //The max amount of elements the spider can move
    private final int MAX_LIFE;                 //currentLife can never be above this value

    private int life;                           //Spider health, if it reaches 0, game over
    private int locationRow;                    //row of spider
    private int locationCol;                    //column of spider

    /**
     * Creates the spider constants for the current game
     * @param webLength - Diameter of the web
     */
    public Spider(int webLength) {
        MAX_LIFE = (webLength / 10) + 10;       //larger webs are a little harder, give more health to compensate
        life = MAX_LIFE;                        //start health at max
        locationRow = webLength / 2;            //Start in middle row
        locationCol = webLength / 2;            //Start in middle column
    }

    /**
     * Subtract health from the spider
     */
    private void hunger() {
        life--;
    }

    /**
     * checks the health of the spider, if at, or below 0, the game ends
     * @return valid if the health is above 0, false otherwise
     */
    public boolean alive() {
        return (life > 0);
    }

    /**
     * Changes the spiders location to the new element passed (row, col), will always be a
     * valid spot in the web
     * @param row - valid row of web object
     * @param col - valid column of web object
     */
    public void move(int row, int col) {
        locationRow = row;
        locationCol = col;
    }

    /**
     * Checks position vs array of Fly object, if one is in the same spot as the spider,
     * the spider eats it and gains health
     *
     * @param flies - current List of Fly objects in web array
     */
    private void eat(ArrayList<Fly> flies) {
        Fly fly;
        for (int i = 0; i < flies.size(); i++) {
            fly = flies.get(i);
            if (fly.getRow() == locationRow && fly.getCol() == locationCol) {
                System.out.printf("%s fly eaten. (+%d)%n" +
                        "***********%n",
                        fly.getSize(), fly.getScoreValue());
                life += fly.getFoodValue();
                Game.adjustScore(fly.getScoreValue());
                flies.remove(fly);

            }
        }
    }

    /**
     * updates the spider health attributes
     * @param flies - current List of Fly objects in web array
     */
    public void update(ArrayList<Fly> flies) {
        eat(flies);
        hunger();
        if (life > MAX_LIFE)
            life = MAX_LIFE;
    }

    /**
     * checks to see if the spider is in the element (i, j) in the web array
     *
     * @param i - the row of the array
     * @param j - the column of the array
     */
    public boolean isSpider(int i, int j) {
        return i == locationRow && j == locationCol;
    }

    public void generateInformation() {
        System.out.printf("Health: %d, Movement: %d%n", life, MAX_MOVEMENT);
    }

    /**
     * generates a square of view around the spider, of SPIDER_VIEW radius
     *
     * @param web -
     */
    public void generateView(Web web) {
        if (Game.DEBUG)
            debug(web);

        int viewRadius = Game.DIFFICULTY_VIEW; //radius of elements out from the spider that it can see
        int viewDiameter = (viewRadius * 2) + 1; //diameter of the view square
        int formatLength = String.valueOf(Fly.getMaxEnergy()).length(); //formats the width of the spaces
        int changeRow;
        int changeCol;
        int currentIterRow;
        int currentIterCol;

        boolean inBounds;

        for (int i = 0; i < viewDiameter; i++) {
            for (int j = 0; j < viewDiameter; j++) {
                //compares and finds the current spot in the web in relation to what the spider sees
                changeRow = -(viewRadius - i);
                changeCol = -(viewRadius - j);
                currentIterRow = locationRow + changeRow;
                currentIterCol = locationCol + changeCol;

                inBounds = web.checkBounds(currentIterRow, currentIterCol);
                if (inBounds) {
                    if (isSpider(currentIterRow, currentIterCol))
                        System.out.print(String.format("%-" + formatLength + "s ", "*"));
                    else
                        System.out.print(String.format("%-" + formatLength + "d ",
                                web.getVibration(currentIterRow, currentIterCol)));
                } else
                    System.out.print(String.format("%-" + formatLength + "s ", "-"));
            }
            System.out.println();
        }
    }

    /**
     * checks if the movement is valid, and inbounds
     *
     * @return the new position of the spider, or null
     */
    public int[] getMovement(String userInput, Web web) {
        boolean validDirection = false;
        boolean validDistance = false;

        int numSpaces = 0;
        for (int i = 0; i < userInput.length(); i++)
            if (Character.isWhitespace(userInput.charAt(i)))
                numSpaces++;

        //if two values separated by a space
        if (numSpaces == 1) {
            //split into direction and distance Strings
            String[] input = userInput.split(" ");

            //FIRST STRING (alphabetical, direction)
            char[] directionChars = input[0].toCharArray();

            if (directionChars.length <= 2) {

                //counts the directions, can only be one of each, and no opposites
                int directionRow = 0;
                int directionCol = 0;

                //can only be a max of 2 (diagonal directions), or 1 (cardinal directions)
                boolean invalidKey = false; //flag for non WASD keys
                for (char c : directionChars) {
                    switch (c) {
                        case 'w':
                            directionRow -= 1;
                            break;
                        case 's':
                            directionRow += 1;
                            break;
                        case 'a':
                            directionCol -= 1;
                            break;
                        case 'd':
                            directionCol += 1;
                            break;
                        default:
                            invalidKey = true;
                            break;
                    }
                }
                //duplicate directions (dd = 2) or conflicting opposite inputs (ws or ad) or non directional key
                validDirection = (!(Math.abs(directionRow) == 2 || Math.abs(directionCol) == 2) &&
                        !(directionRow == 0 && directionCol == 0) && !invalidKey);
            }

            //SECOND STRING (numerical)
            //attempt to convert the String to an int

            int moveDistance = 0;
            if (Game.isDigit(input[1]))
                moveDistance = Integer.parseInt(input[1]);
            validDistance = ((moveDistance > 0) && (moveDistance <= MAX_MOVEMENT));
        }

        //verified both the direction and distance are correctly input, now checking if the
        //new location is in bounds of the array
        if (validDistance && validDirection) {
            int[] newLocation;
            newLocation = calculateMovement(userInput);

            //check if new element is in bounds of the web
            if (web.checkBounds(newLocation[0], newLocation[1])) {
                //return new, valid position in web after
                return newLocation;
            } else
                System.out.println("Out of bounds.");
        } else
            System.out.println("Invalid input");
        //movement command is invalid
        return null;
    }

    /**
     * Calculates the new position of the spider, will only be called if it passes the validMovement method
     *
     * @param userInput - user input, will be a movement String by this point
     * @return a 2 element array with the first being the change in row, and the second element being the
     * change in column
     */
    private int[] calculateMovement(String userInput) {
        String[] input = userInput.split(" ");
        int[] newElement = new int[2];

        int distance = Integer.parseInt(input[1]);
        int changeRow = 0;
        int changeCol = 0;

        char[] chars = input[0].toCharArray();
        for (char c : chars) {
            switch (c) {
                case 'w':
                    changeRow -= distance;
                    break;
                case 's':
                    changeRow += distance;
                    break;
                case 'a':
                    changeCol -= distance;
                    break;
                case 'd':
                    changeCol += distance;
                    break;
                default:
                    break;
            }
        }
        newElement[0] = locationRow + changeRow;
        newElement[1] = locationCol + changeCol;

        return newElement;
    }

    /**
     * @param web - Web object the spider is currently in
     */
    private void debug(Web web) {
        System.out.println("Debug: SPIDER");
        System.out.printf("ROW: %d, COLUMN: %d, HEALTH: %d, MOVEMENT: %d%n",
                locationRow, locationCol, life, MAX_MOVEMENT);
        int viewRadius = Game.DIFFICULTY_VIEW; //radius of elements out from the spider that it can feel (1-3 work well)
        final int viewDiameter = (viewRadius * 2) + 1; //diameter of the view circle
        int changeRow;
        int changeCol;
        int currentRow;
        int currentCol;

        boolean inBounds;
        //if in bounds, shows true, else false
        for (int i = 0; i < viewDiameter; i++) {
            for (int j = 0; j < viewDiameter; j++) {
                //Up-Left = negative, Down-Right = positive (differences in indexes)
                changeRow = -(viewRadius - i);
                changeCol = -(viewRadius - j);
                currentRow = locationRow + changeRow;
                currentCol = locationCol + changeCol;

                inBounds = web.checkBounds(currentRow, currentCol);
                if (i == viewRadius && j == viewRadius)
                    System.out.print("   *   ");
                else
                    System.out.printf(" %-5s ", inBounds);
            }
            System.out.println();
        }
        //shows current indexes of spider view (spider is in the center)
        for (int i = 0; i < viewDiameter; i++) {
            for (int j = 0; j < viewDiameter; j++) {
                changeRow = -(viewRadius - i);
                changeCol = -(viewRadius - j);
                currentRow = locationRow + changeRow;
                currentCol = locationCol + changeCol;

                if (i == viewRadius && j == viewRadius)
                    System.out.print("    *    ");
                else
                    System.out.printf(" %3d,%3d ", currentRow, currentCol);
            }
            System.out.println();
        }
    }
}