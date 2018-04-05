import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 */
public class Game
{
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
    public static void main(String[] args)
    {
        String userInput;               //holds user input
        boolean continueMenu = true;    //displays main menu after options are selected until user quit

        //displays game start menu and accepts user input for game actions
        do
        {
            displayText(MENU_MAIN);
            System.out.print("Input: ");
            userInput = keyboard.nextLine();
            
            //switch for userInput
            switch (userInput.toLowerCase())
            {
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
            }//end of userInput switch
        }//end of do-while loop for startup menu userInput
        while (continueMenu);
    }//end of main

    /**
     * The setupGame method sets up the webSize and creates the objects to pass into the game controller
     */
    public static void setupGame()
    {
        String userInput;
        int webSize = 0;
        boolean validWeb;

        //display webSize options and get webSize for game from user
        do
        {
            //display web options
            displayText(MENU_WEB);
            System.out.print("#: ");
            userInput = keyboard.nextLine();

            validWeb = true;    //reset flag
            
            //switch to set webSize
            switch (userInput.toLowerCase())
            {
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
            }//end of webSize switch
        }//end of do-while loop for webSize
        while (!validWeb);

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
    }//end of setupGame method

    /**
     * The gameController method runs the SpiderGame
     * @param web
     * @param spider
     * @param flies
     */
    public static void gameController(Web web, Spider spider, ArrayList<Fly> flies)
    {
        //method variables
        boolean continueGame = true;    //boolean to determine end of game
        int action;                     //determines if game continues or not
        Game.score = 0;                 //player score
        Game.turns = 0;                 //number of turns played
        flies.clear();                  //removes all flies in array
        
        //loop for each turn of play
        do
        {
            //add one to score each turn
            adjustScore(1);
            Game.turns++;
            System.out.println("Turn: " + Game.turns);
            //update Web and Fly
            objectUpdate(spider, web, flies);

            //user turn
            //ends game if spider is dead
            if (!spider.alive())
                action = QUIT_DEATH;
            //turn if spider is alive
            else
            {
                spider.generateInformation();
                spider.generateView(web);
                action = getInput(spider, web);
            }//end of user turn

            //continue game (positive values are continues, allows for custom information)
            if (action >= 0)
            {
                System.out.println("***********");
                spider.update(flies);
            }//end of if action >= 0
            
            //quit value encountered (any negative menu constants)
            else
            {
                displayText(action);
                displayScore();
                continueGame = false;
            }//end of game of action <0
        }//end of do loop for each turn
        while (continueGame);
    }//end of gameController method

    /**
     * The setOptions method switches debug method on or off
     */
    public static void setOptions()
    {
        String userInput;
        displayText(MENU_OPTIONS);
        System.out.print("#: ");
        userInput = keyboard.nextLine();
        
        //switch to compare user input
        switch (userInput.toLowerCase())
        {
            case "1":
            case "debug":
                DEBUG = !DEBUG;
                System.out.printf("Debug changed to: %b%n%n", DEBUG);
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }//end of switch to compare debug
    }//end of setOptions method

    /**
     * The setDifficulty method sets game options based on user decided difficulty
     */
    public static void setDifficulty()
    {
        String userInput;
        boolean cont = true;   //flag for user to continue

        displayText(MENU_DIFFICULTY);
        do
        {
            System.out.print("Current Difficulty: " + Game.DIFFICULTY +
                    "\nInput: ");
            userInput = keyboard.nextLine();
            
            //switch to set game difficulty
            switch (userInput.toLowerCase())
            {
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
            }//end of switch to set difficulty
        }//end of loop to ask for difficulty based on cont flag
        while (cont);
    }//end of setDifficulty method

    /**
     * Checks to see what the command is. Can either be 1: quit, 2: help menu, 3: valid movement
     *
     * @param spider
     * @param web
     * @return
     */
    public static int getInput(Spider spider, Web web)
    {
        String userInput;
        int action = 0;
        boolean valid = false;
        
        //loop to get userInput for spider turn
        do
        {
            System.out.print("\nCommand: ");
            userInput = keyboard.nextLine();

            userInput = (userInput.toLowerCase()).trim();  //standardize input

            //switch for userInput: game exit, in-game help, or spider movement
            switch (userInput)
            {
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
                    if (newLocation != null)
                    {
                        spider.move(newLocation[0], newLocation[1]);
                        valid = true;
                    }//end of spider movement statement
                    break;
            }//end of userInput switch
        } while (!valid);
        return action;
    }//end of getInput method

    /**
     * The objectUpdate method updates the fly and spider elements in the web
     * @param spider
     * @param web
     * @param flies
     */
    public static void objectUpdate(Spider spider, Web web, ArrayList<Fly> flies)
    {
        Fly.update(web);
        web.update(spider, flies);
    }//end of objectUpdate method

    /**
     * Used to check if the String passed is a digit
     *
     * @param value
     * @return
     */
    public static boolean isDigit(String value)
    {
        boolean isDigit = true;
        for (char c : value.toCharArray())
            if (!Character.isDigit(c))
                isDigit = false;
        return isDigit;
    }//end of isDigit method

    /**
     * The adjustScore method updates user's score
     * @param adjust The number to add to game score
     */
    public static void adjustScore(int adjust)
    {
        score += adjust;
    }//end of adjustScore method

    /**
     * The displayScore method prints game totals
     */
    public static void displayScore()
    {
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
    }//end of displayScore method

    /**
     * The displayText method outputs game menus & help text
     * @param value The specific game menu to display
     */
    public static void displayText(int value)
    {
        switch (value)
        {
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
        }//end of switch for displayText
    }//end of displayText method
}//end of game class
