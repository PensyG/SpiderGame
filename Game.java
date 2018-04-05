import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 */
public class Game {
    private static Scanner keyboard = new Scanner(System.in);
    private static int score;                           //score of the player
    private static int turns;                           //turns player has survived each game

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
     * @param args
     */
    public static void main(String[] args) {
        String userInput;               //
        boolean continueMenu = true;    //


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
                    System.out.print("Error: Invalid Number.\n\n");
                    break;
            }
        } while (continueMenu);
    }

    /**
     * sets up the webSize and creates the objects to pass into the game controller
     */
    public static void setupGame() {
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
     * @param web
     * @param spider
     * @param flies
     */
    public static void gameController(Web web, Spider spider, ArrayList<Fly> flies) {

        boolean continueGame = true;
        int action;
        Game.score = 0;
        Game.turns = 0;
        flies.clear();

        do {
            //add one to score each turn
            adjustScore(1);
            Game.turns++;
            System.out.println("Turn: " + Game.turns);
            //update Web and Fly
            objectUpdate(spider, web, flies);

            //user turn
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
            //quit value encountered (any negative menu constants)
            else {
                displayText(action);
                displayScore();
                continueGame = false;
            }
        } while (continueGame);
    }

    /**
     *
     */
    public static void setOptions() {
        String userInput;
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
     *
     */
    public static void setDifficulty() {
        String userInput;
        boolean cont = true;   //flag for user to continue

        displayText(MENU_DIFFICULTY);
        do {
            System.out.print("Current Difficulty: " + Game.DIFFICULTY +
                    "\nInput: ");
            userInput = keyboard.nextLine();

            switch (userInput.toLowerCase()) {
                case "1":
                case "easy":
                    DIFFICULTY_VIEW = 3;
                    DIFFICULTY_MULTIPLIER = .75;
                    Game.DIFFICULTY = "Easy";
                    break;
                case "2":
                case "medium":
                    DIFFICULTY_VIEW = 2;
                    DIFFICULTY_MULTIPLIER = 1;
                    Game.DIFFICULTY = "Medium";
                    break;
                case "3":
                case "hard":
                    DIFFICULTY_VIEW = 1;
                    DIFFICULTY_MULTIPLIER = 1.5;
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
        } while (cont);
    }

    /**
     * Checks to see what the command is. Can either be 1: quit, 2: help menu, 3: valid movement
     *
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
                case "help":
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
     * @param spider
     * @param web
     * @param flies
     */
    public static void objectUpdate(Spider spider, Web web, ArrayList<Fly> flies) {
        Fly.update(web);
        web.update(spider, flies);
    }

    /**
     * Used to check if the String passed is a digit
     *
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
     * @param adjust
     */
    public static void adjustScore(int adjust) {
        score += adjust;
    }

    /**
     *
     */
    public static void displayScore() {
        System.out.print("SCORE:" +
                "\n**********************" +
                "\n** Survival: " + turns +
                "\n** Flies: " + score +
                "\n**********************" +
                "\n** Difficulty: " + DIFFICULTY +
                "\n** Multiplier: " + DIFFICULTY_MULTIPLIER +
                "\n**********************\n");
        score += turns;
        score *= DIFFICULTY_MULTIPLIER;
        System.out.printf("TOTAL: %d%n%n", score);
    }

    /**
     * @param value
     */
    public static void displayText(int value) {
        switch (value) {
            case QUIT_DEATH:
                System.out.print("The Spider has died...\n\n");
                break;
            case QUIT_PLAYER:
                System.out.print("Exiting Current Game...\n\n");
                break;
            case HELP_IN_GAME:
                System.out.print("A 2 character max combination input of the four cardinal directions mapped\n" +
                        "to 'WASD' allows you to move in 8 directions. In addition, you have a certain\n" +
                        "distance you move in any of those directions each turn, based on the size of\n" +
                        "the web. Separate the two with a space, and you have a valid input: 'wd 5',\n" +
                        "or 'a 7'.\n");
                break;
            case HELP_MAIN:
                System.out.print("Navigate the web as a spider with a limited view distance. Flies will randomly\n" +
                        "get stuck in your web, and vibrate. You can feel the vibration around yourself,\n" +
                        "and your goal is to find and eat flies to stay alive. The flies have a chance to\n" +
                        "escape each turn, and after a long enough time in the web, can die.\n\n");
                break;
            case HELP_DIFFICULTY:
                System.out.print("Easy: View Distance (3), Score Multiplier (75%) \n" +
                        "Medium: View Distance (2), Score Multiplier (100%)\n" +
                        "Hard: View Distance (1), Score Multiplier (150%)\n\n");
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
