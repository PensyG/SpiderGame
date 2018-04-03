
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private static Scanner keyboard = new Scanner(System.in);
    private static int score;                           //holds the score of the player

    //Constants passed to displayText()
    private final static int HELP_MENU = 10;            //
    private final static int HELP_IN_GAME = 11;         //

    private final static int QUIT_DEATH = -1;           //
    private final static int QUIT_PLAYER = -2;          //

    //will display additional information during game
    public static final boolean DEBUG = false;          //

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String userInput;               //
        boolean continueMenu = true;    //
        do {
            System.out.print("Spider Game\n" +
                    "1. Start\n" +
                    "2. Help\n" +
                    "3. Quit\n" +
                    "Input: ");
            userInput = keyboard.nextLine();

            if (isDigit(userInput)) {
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        setupGame();
                        break;
                    case 2:
                        displayText(HELP_MENU);
                        break;
                    case 3:
                        continueMenu = false;
                        break;
                    default:
                        System.out.print("Error: Invalid Number.\n\n");
                        break;
                }
            }
            else
                System.out.print("Error: Non number input.\n\n");
        } while (continueMenu);
    }

    /**
     * sets up the webSize and creates the objects to pass into the game controller
     */
    public static void setupGame() {

        boolean validWeb;
        int webSize;
        String userInput;
        do {
            System.out.print("\nWeb Sizes (Larger is Harder)\n" +
                    "1. 50x50   - Small\n" +
                    "2. 100x100 - Medium\n" +
                    "3. 200x200 - Large\n" +
                    "4. 400x400 - Huge\n" +
                    "Input: ");
            userInput = keyboard.nextLine();

            //check if input is a digit
            if (isDigit(userInput))
                webSize = Integer.parseInt(userInput);
            else
                webSize = 0;

            //check if valid digit
            validWeb = (webSize > 0 && webSize <= 4);

            //change webSize to correct size
            switch(webSize) {
                case 1:
                    webSize = 50;
                    break;
                case 2:
                    webSize = 100;
                    break;
                case 3:
                    webSize = 200;
                    break;
                case 4:
                    webSize = 400;
                    break;
                default:
                    System.out.print("Invalid Input. \n");
                    break;
            }
        } while(!validWeb);

        System.out.println("Starting game... (Press enter for help)\n");

        //create objects
        Web web = new Web(webSize);
        Spider spider = new Spider(webSize);
        //static Fly array

        //start game
        gameController(web, spider, Fly.getFlies());
        //go back to main menu
    }

    /**
     *
     * @param web
     * @param spider
     * @param flies
     */
    public static void gameController(Web web, Spider spider, ArrayList<Fly> flies) {


        boolean continueGame = true;
        int action;
        Game.score = 0;
        Fly.generateFly(web);

        do {
            //add one to score each turn
            adjustScore(1);
            System.out.println("Score: " + score);
            //update Web and Fly
            objectUpdate(spider, web, flies);

            //user turn
            spider.generateInformation();
            spider.generateView(web);
            action = getInput(spider, web);

            if (!spider.alive())
                action = QUIT_DEATH;

            //continue game (positive values are continues, allows for custom information)
            if (action >= 0) {
                spider.update(flies);
                System.out.println("-------------");    //separate each turn
            }
            //quit value encountered
            else {
                displayText(action);
                displayScore();
                continueGame = false;
            }
        } while(continueGame);
    }

    /**
     * Checks to see what the command is. Can either be 1: quit, 2: help menu, 3: valid movement
     * @param spider
     * @param web
     * @return
     */
    public static int getInput(Spider spider, Web web) {

        String userInput;
        int action = 0;
        boolean valid = false;
        do {
            System.out.print("\nCommand: ");
            userInput = keyboard.nextLine();

            userInput = (userInput.toLowerCase()).trim();  //standardize input

            switch (userInput) {
                case "quit":
                case "exit":
                    action = QUIT_PLAYER;
                    valid = true;
                    break;
                case "":
                    displayText(HELP_IN_GAME);
                    break;
                default:
                    int[] newLocation = spider.getMovement(userInput, web);
                    if (newLocation != null) {
                        spider.move(newLocation[0], newLocation[1]);
                        valid = true;
                    }
                    break;
            }
        } while (!valid);
        return action;
    }

    /**
     *
     * @param spider
     * @param web
     * @param flies
     */
    public static void objectUpdate(Spider spider, Web web, ArrayList<Fly> flies) {
        //Fly update
        Fly.update(web);
        //web update
        web.update(spider, flies);
    }

    /**
     * Used to check if the String passed is a digit
     * @param value
     * @return
     */
    public static boolean isDigit(String value) {
        boolean isDigit = true;
        for (char c : value.toCharArray())
            if (!Character.isDigit(c))
                isDigit = false;
        return isDigit;
    }

    /**
     *
     * @param adjust
     */
    public static void adjustScore(int adjust) {
        score += adjust;
    }
    public static void displayScore() {
        System.out.println("Score: " + score);
    }
    public static void displayText(int value) {

        switch (value) {
            case QUIT_DEATH:    //-1
                System.out.print("The Spider has died...\n\n");
                break;
            case QUIT_PLAYER:   //-2
                System.out.print("Exiting Current Game...\n\n");
                break;
            case HELP_IN_GAME:
                System.out.print("In Game Command details:\n\n");
                break;
            case HELP_MENU:
                System.out.print("Game Overview Details: \n\n");
                break;
        }
    }
}
