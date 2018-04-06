import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Menus and organization of the Spider Game
 */
public class Game {
    private static Scanner keyboard = new Scanner(System.in);
    private static int score;                           //score of the player
    private static int turns;                           //turns player has survived each game
    private static double survivalMultiplier;           //exponential score multiplier and difficulty adjuster

    //Constants passed to displayText()
    //can be any numbers, but are grouped
    //and separated to allow for more constants
    private final static int HELP_MAIN = 10;            //displays help for main menu
    private final static int HELP_IN_GAME = 11;         //displays help for in-game
    private final static int HELP_DIFFICULTY = 12;      //displays help for difficulty menu

    private final static int MENU_MAIN = 20;            //displays menu for spider game
    private final static int MENU_DIFFICULTY = 21;      //displays menu for difficulty input
    private final static int MENU_WEB = 22;             //displays menu for webSize input
    private final static int MENU_OPTIONS = 23;         //displays menu for options input

    private final static int QUIT_DEATH = -1;           //spider died
    private final static int QUIT_PLAYER = -2;          //user quit the game

    //game constants (should be changed in-game)
    public static boolean DEBUG = false;                //debug flag, will call extra methods during game if true

    private static String DIFFICULTY = "Medium";        //Used to display current difficulty
    public static int DIFFICULTY_VIEW = 2;              //Spider view, used in Spider class
    private static double DIFFICULTY_MULTIPLIER = 1;    //score multiplier


    /**
     * Starts the program, contains the main menu for the game
     * @param args - empty
     */
    public static void main(String[] args) {
        String userInput;               //user input for the menu
        boolean continueMenu = true;    //flag for quitting the game

        do {
            displayText(MENU_MAIN);
            System.out.print("Input: ");
            userInput = keyboard.nextLine();

            switch (userInput.toLowerCase()) {
                case "1":
                case "start":
                    setupGame();
                    break;
                case "2":
                case "help":
                    displayText(HELP_MAIN);
                    break;
                case "3":
                case "options":
                    setOptions();
                    break;
                case "4":
                case "quit":
                    continueMenu = false;
                    break;
                default:
                    System.out.print("Error: Invalid Input.\n\n");
                    break;
            }
        } while (continueMenu);
    }

    /**
     * sets up the webSize and creates the objects to pass into the game controller
     */
    private static void setupGame() {
        String userInput;
        int webSize = 0;
        boolean validWeb;

        //get webSize for game

        do {
            displayText(MENU_WEB);
            System.out.print("#: ");
            userInput = keyboard.nextLine();

            validWeb = true;    //reset flag
            //set webSize
            switch (userInput.toLowerCase()) {
                case "1":
                case "small":
                    webSize = 50;
                    break;
                case "2":
                case "medium":
                    webSize = 100;
                    break;
                case "3":
                case "large":
                    webSize = 200;
                    break;
                case "4":
                case "huge":
                    webSize = 400;
                    break;
                default:
                    System.out.print("Invalid Input. \n");
                    validWeb = false;
                    break;
            }
        } while (!validWeb);

        setDifficulty();

        System.out.println("\nStarting game...\n" +
                "(Enter 'help' for move info)\n");

        //create objects
        Web web = new Web(webSize);
        Spider spider = new Spider(webSize);
        Fly.setConstants(webSize);
        //static Fly array

        //start game
        gameController(web, spider, Fly.getFlies());
        //go back to main menu
    }

    /**
     * Organizes the flow of the game (turn based), called by setupGame
     * @param web - Web object, updated each turn
     * @param spider - Spider object, player uses this
     * @param flies - Fly ArrayList, contains all of the info on the current Fly objects
     */
    private static void gameController(Web web, Spider spider, ArrayList<Fly> flies) {

        boolean continueGame = true;    //flag for
        int action;         //passed into displayText, uses the Game constants near the declaration

        //reset variables
        Game.score = 0;
        Game.turns = 0;
        Game.survivalMultiplier = 1;
        flies.clear();


        do {
            Game.turns++;
            System.out.println("Turn: " + Game.turns);

            //update multiplier every 5th turn by 5%
            if (turns % 10 == 0) {
                adjustConstants();
            }

            //update Web and Fly
            objectUpdate(spider, web, flies);

            //if alive, user can take turn
            if (!spider.alive())
                action = QUIT_DEATH;
            else {
                spider.generateInformation();
                spider.generateView(web);
                action = getInput(spider, web);
            }
            //continue game (positive values are continues, allows for custom information)
            if (action >= 0) {
                System.out.println("***********");
                spider.update(flies);
            }
            //quit value encountered (any negative menu constants) (action < 0)
            else {
                displayText(action);
                displayScore();
                continueGame = false;
            }
        } while (continueGame);
    }

    /**
     * Options menu, currently only contains the switch for debug mode
     */
    private static void setOptions() {
        String userInput;           //user keyboard input

        displayText(MENU_OPTIONS);
        System.out.print("#: ");
        userInput = keyboard.nextLine();

        switch (userInput.toLowerCase()) {
            case "1":
            case "debug":
                DEBUG = !DEBUG;
                System.out.printf("Debug changed to: %b%n%n", DEBUG);
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }
    }

    /**
     * Menu for changing difficulty, currently only Easy, Medium, and Hard are available,
     * which influence the view distance of the spider (player). Harder difficulties have
     * higher final score multipliers
     */
    private static void setDifficulty() {
        String userInput;
        boolean cont = true;   //flag for user to continue

        displayText(MENU_DIFFICULTY);
        do {
            System.out.print("Current Difficulty: " + Game.DIFFICULTY +
                    "\nInput: ");
            userInput = keyboard.nextLine();
            switch (userInput.toLowerCase()) {
                //easy
                case "1":
                case "easy":
                    DIFFICULTY_VIEW = 3;
                    DIFFICULTY_MULTIPLIER = .5;
                    Game.DIFFICULTY = "Easy";
                    break;
                //medium
                case "2":
                case "medium":
                    DIFFICULTY_VIEW = 2;
                    DIFFICULTY_MULTIPLIER = 1.0;
                    Game.DIFFICULTY = "Medium";
                    break;
                //hard
                case "3":
                case "hard":
                    DIFFICULTY_VIEW = 1;
                    DIFFICULTY_MULTIPLIER = 2.0;
                    Game.DIFFICULTY = "Hard";
                    break;
                case "help":
                    displayText(HELP_DIFFICULTY);
                    break;
                case "":
                    cont = false;
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        } while (cont); //user still wants to change difficulty
    }

    /**
     * Checks to see what the command is. Can either be 1: quit, 2: help menu, or checks if valid movement
     * will make sure the user inputs a valid choice, and won't return until they have
     *
     * @param spider - Spider object to call movement updates
     * @param web - web update to check if movement updates are in bounds
     * @return - Game.constant, will always be a
     */
    private static int getInput(Spider spider, Web web) {

        String userInput;
        int action = 0;
        boolean valid = false;
        do {
            System.out.print("\nCommand: ");
            userInput = keyboard.nextLine();

            userInput = (userInput.toLowerCase()).trim();  //standardize input

            switch (userInput) {
                //quit
                case "quit":
                case "exit":
                    action = QUIT_PLAYER;
                    valid = true;
                    break;
                //help info
                case "help":
                    displayText(HELP_IN_GAME);
                    break;
                //check if valid movement
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
     * Adjusts the game values as the game continues, increasing multipliers, and difficulties
     * called every X turns
     */
    private static void adjustConstants() {
        survivalMultiplier *= 1.025;
        Fly.adjustConstants();
    }

    /**
     * updates the current objects excluding the player (Spider), can be adapted for more
     * also calls debug methods if debug mode is on
     * @param spider - Spider object, passed into web update
     * @param web - Web object, used to update
     * @param flies - Fly ArrayList, passed to update web array elements
     */
    private static void objectUpdate(Spider spider, Web web, ArrayList<Fly> flies) {
        Fly.update(web);
        web.update(spider, flies);
    }

    /**
     * Used to check if the String passed is a digit
     *
     * @param value - String of random characters
     * @return true if the value is all 0-9, or false if not (includes non alpha characters like ',' or ' ')
     */
    public static boolean isDigit(String value) {
        boolean isDigit = true;
        for (char c : value.toCharArray())
            if (!Character.isDigit(c))
                isDigit = false;
        return isDigit;
    }

    /**
     * changes the score by the parameter passed
     * @param adjust - amount of score change, can be positive or negative
     */
    public static void adjustScore(int adjust) {
        score += adjust;
    }

    /**
     * calculates and displays the score of the player after factoring in multipliers
     */
    private static void displayScore() {
        int survivalScore = (int) (turns * survivalMultiplier);
        score += survivalScore;
        score *= DIFFICULTY_MULTIPLIER;
        int finalScore = (int) ((score + survivalScore) * DIFFICULTY_MULTIPLIER);

        System.out.print("**********************" +
                "\n** Survival: " + survivalScore +
                "\n** Flies: " + score +
                "\n**********************" +
                "\n** Difficulty: " + DIFFICULTY +
                "\n** Multiplier: " + DIFFICULTY_MULTIPLIER +
                "\n**********************" +
                "\nTOTAL SCORE: " + finalScore + "\n\n");
    }

    /**
     * uses the value passed to display the relevant text
     * should only called with valid Game constants
     * @param value - A HELP, MENU, or QUIT constant from the top of Game
     */
    private static void displayText(int value) {
        switch (value) {
            case QUIT_DEATH:
                System.out.print("The Spider has died...\n\n");
                break;
            case QUIT_PLAYER:
                System.out.print("Exiting Current Game...\n\n");
                break;
            case HELP_IN_GAME:
                System.out.print(
                        "A 2 character max combination input of the four cardinal directions mapped to 'WASD'" +
                        "UP = 'w', DOWN = 's', RIGHT = 'd', LEFT = 'A'. This allows you to move in 8 directions. " +
                        "In addition, you can only move 5 elements each turn. You can't input opposite\n" +
                        "directions (RIGHT-LEFT, UP-DOWN) or the same direction twice (UP-UP). It must also " +
                        "be in 'Direction - Distance' format, 'Distance - Direction' won't work.\n" +
                        "Valid: 'wd 5', 'a 1', 's 5'\n" +
                        "Invalid: 'ss 3', 'ad 2', 'a 6', '3 sd'\n\n");
                break;
            case HELP_MAIN:
                System.out.print(
                        "You are a spider '*' in a web with a limited view distance (dependant on difficulty).\n" +
                        "Flies have gotten stuck in your web. It's your job to navigate the web, and eat them\n" +
                        "to survive. In order to find them, you must look at the current values in your view.\n" +
                        "Each value represents a vibration amount given off by a fly. The closer the value is\n" +
                        "to a fly, the higher the value. Larger flies have larger amounts of vibration and\n" +
                        "can be felt from farther away. As you survive, fewer flies will get stuck in your\n" +
                        "web. Eat as many flies as you can to get the highest score.\n\n");
                break;
            case HELP_DIFFICULTY:
                System.out.print("Easy: View Distance (3), Score Multiplier (50%) \n" +
                        "Medium: View Distance (2), Score Multiplier (100%)\n" +
                        "Hard: View Distance (1), Score Multiplier (200%)\n\n");
                break;
            case MENU_MAIN:
                System.out.print("Spider Game\n" +
                        "***********\n" +
                        "1: Start\n" +
                        "2: Help\n" +
                        "3: Options\n" +
                        "4: Quit\n");
                break;
            case MENU_WEB:
                System.out.print("\nWeb Sizes: \n" +
                        "1: 50x50   - Small\n" +
                        "2: 100x100 - Medium\n" +
                        "3: 200x200 - Large\n" +
                        "4: 400x400 - Huge\n");
                break;
            case MENU_DIFFICULTY:
                System.out.print("\nDifficulty:\n" +
                        "1: Easy\n" +
                        "2: Medium\n" +
                        "3: Hard\n" +
                        "Enter 'help' for more info,\n" +
                        "or just press enter to continue.\n");
                break;
            case MENU_OPTIONS:
                System.out.printf("\nOptions\n" +
                        "1. Debug: %b%n", DEBUG);
                break;
            default:
                System.out.print("ERROR: displayText() incorrectly called\n");
                break;
        }
    }
}