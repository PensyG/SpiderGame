//name Penny Grant/Reece Sharp
//date
//project
/*
 * BUGS:
 *
 */

/*
 * IDEAS:
 *
 * Score based system
 * Difficulty system
 *      (view size, array size,
 *
 * as time goes on
 * 	flies escape quicker
 * 	health decreases more quickly
 * 	less fly gen
 *
 * 	Possible Refactor Class Ideas
 * 	    View
 * 	    Diagnostics
 * 	    Animal (polymorphism for the fly + Spider)
 *
 */

import java.util.Scanner;

/**
 * Organizes the spider game, the player controls the spider, and moves around
 * their web trying to eat the flies before they die, until the spider dies
 * Goal for the program is to be completely dynamic, based on web size (50 min)
 */
public class Game {
    public static final boolean DEBUG = false;   //calls debug methods in each object update method if true

    public static Scanner keyboard = new Scanner(System.in);      //user input

    //Game constants
    private static final int CHOICE_INVALID = 0;   //choice is invalid, reiterate
    private static final int CHOICE_EXIT = 2;       //user wants to exit
    private static final int CHOICE_MOVE = 3;       //user wants to, and has a valid move

    private static final int EXIT_DEATH = 1;        //spider died

    public static void main(String[] args) {

        int webLength = 0;      //size of the web

        //web generation
        System.out.print("Web sizes:\n" +
                "1: 50x50\n" +
                "2: 100x100\n" +
                "3: 200x200\n");

        boolean valid = false;
        while (!valid) {

            System.out.println("Enter 1, 2, or 3");
            webLength = keyboard.nextInt();
            keyboard.nextLine();

            if (webLength >= 1 && webLength <= 3)
                valid = true;
            else
                System.out.println("Invalid Entry.");
        }
        switch (webLength) {
            case 1:
                webLength = 50;
                break;
            case 2:
                webLength = 100;
                break;
            case 3:
                webLength = 200;
                break;
        }

        //Generate objects in game
        Web web = new Web(webLength);
        Spider spider = new Spider(webLength);

        //start game
        gameController(spider, web);

    }//end main

    /**
     * Controls the flow of the game
     * @param spider - Spider object, needed to call turn updates
     * @param web - Web object, needed to call turn updates
     */
    private static void gameController(Spider spider, Web web) {
        int time = 0;       //amount of turns of game
        int quitValue;      //flag for custom exit message
        int userAction;         //flag for userInput (move, exit)
        String userInput;

        System.out.println("Enter 'quit', or 'exit' at anytime to leave the game");

        //start with a fly in the web
        Fly.generateFly(web);

        quitValue = CHOICE_INVALID;
        while (quitValue == CHOICE_INVALID) {
            time++;
            System.out.println("Turn: " + time);

            //increment objects
            Fly.update(web);
            web.update(spider, Fly.getFlies());

            //player turn
            spider.generateSpiderView(web);
            do {
                System.out.print("Spider Action: ");
                userInput = Game.keyboard.nextLine();
                userAction = Game.checkInput(userInput, spider, web);
            } while (userAction == CHOICE_INVALID);

            spider.update(web, Fly.getFlies());

            if (userAction == CHOICE_EXIT)
                quitValue = CHOICE_EXIT;
            else if (!spider.alive())
                quitValue = EXIT_DEATH;

            System.out.println("End Turn.\n" +
                    "--------------------\n");
        }
        displayEnd(quitValue);
    }

    /**
     * displays the method of game quit (death, user exit, etc)
     * @param quitValue
     */
    private static void displayEnd(int quitValue) {
        switch (quitValue) {
            case EXIT_DEATH:
                System.out.println("Spider has died");
                break;
            case CHOICE_EXIT:
                System.out.println("Goodbye!");
                break;
        }
    }

    /**
     * Checks the input to see if it's movement, or if it's a user quit.
     * Will refactor to break it up, but just wanted to get the thing working
     * @param userInput
     * @param spider
     * @param web
     * @return
     */
    public static int checkInput(String userInput, Spider spider, Web web) {
        int choice = CHOICE_INVALID;
        userInput = (userInput.toLowerCase()).trim();  //standardize input

        if (userInput.equals("quit") || userInput.equals("exit")) {
            choice = CHOICE_EXIT;
        } else {
            int numSpaces = 0;

            //get # of spaces (should only be one between direction and distance)
            for (int i = 0; i < userInput.length(); i++)
                if (Character.isWhitespace(userInput.charAt(i)))
                    numSpaces++;

            if (numSpaces == 1) //Valid: 'ds 3', Invalid: 'w  4'
            {
                //split the String up into direction, and distance
                String[] input;     //array of direction and distance;
                input = userInput.split(" ");

                int changeRow = 0;
                int changeCol = 0;

                boolean hasDistance;
                boolean hasDirection = true;

                //FIRST STRING (alphabetical)
                char[] chars = input[0].toCharArray();
                //can only be a max of 2 (diagonal directions), or 1 (cardinal directions)
                if (chars.length <= 2) {
                    for (char c : chars) {
                        switch (c) {
                            case 'w':
                                changeRow -= 1;
                                break;
                            case 's':
                                changeRow += 1;
                                break;
                            case 'a':
                                changeCol -= 1;
                                break;
                            case 'd':
                                changeCol += 1;
                                break;
                            default:
                                hasDirection = false;   //not a valid direction (WASD)
                                break;
                        }
                    }
                    //duplicate directions (dd = 2)
                    if (Math.abs(changeRow) == 2 || Math.abs(changeCol) == 2)
                        hasDirection = false;
                        //conflicting inputs (ws or ad)
                    else if (changeRow == 0 && changeCol == 0)
                        hasDirection = false;
                }
                //not 1 or 2 directional inputs (0, or 3+)
                else
                    hasDirection = false;

                //SECOND STRING (numerical)

                //attempt to convert the String to an int, not valid if fails
                int distance = 0;
                try {
                    distance = Integer.parseInt(input[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Distance");
                }
                //compare to MAX_MOVEMENT if try was successful
                if (distance != 0 && Spider.checkDistance(distance))
                    hasDistance = true;
                else
                    hasDistance = false;

                //if both valid direction, and distance
                if (hasDistance && hasDirection) {
                    changeRow = 0;
                    changeCol = 0;
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
                        }
                    }
                    int newRow = spider.getRow() + changeRow;
                    int newCol = spider.getCol() + changeCol;
                    //check that changes are in range
                    if (web.checkBounds(newRow, newCol)) {
                        spider.move(newRow, newCol);
                        choice = CHOICE_MOVE;   //successful move
                    }
                    else
                        System.out.println("Out of bounds");
                }
                else
                    System.out.println("Invalid entry");
            }
            else
                System.out.println("Too many spaces in entry.");
        }
        return choice;
    }

    /**
     * Checks if the spider movement is valid (contains a direction, and a distance)
     * The direction can be in WASD, and a 2 directional
     * The distance needs to be less than, or equal to the spider MAX_MOVEMENT
     * @param input - da
     * @return
     */
}
