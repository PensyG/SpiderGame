//name Penny Grant/Reece Sharp
//date
//project

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 * BUGS:
 *
 */

/*
 * IDEAS:
 *
 */

/**
 * Organizes the spider game, the player controls the spider, and moves around
 * their web trying to eat the flies before they die, until the spider dies
 *
 */
public class Game
{
    private static final int QUIT = 0;
    private static final int SPIDER_DEATH = 1;
    public static Scanner keyboard = new Scanner(System.in);      //user input
    public static Random random = new Random();                   //random gen for web, and fly gen

    public static void main(String[] args) {

        int webLength; //size of the web

        int quit_value = -1;    //flag for game exit

        //user gen for web
        System.out.println("Enter an integer for a web size, or -1 for a random size (20 is a good number)");
        webLength = keyboard.nextInt();
        keyboard.nextLine();    //clear input stream

        //random square size between 50-300 in multiples of 10
        if (webLength < 0)
            webLength = random.nextInt((25) * 10) + 50;

        //Generate objects in game
        Web web = new Web(webLength);
        Spider spider = new Spider(webLength);
        ArrayList<Fly> flies = new ArrayList<>();

        /*
         * as time goes on
         * 	flies escape quicker
         * 	health decreases more quickly
         * 	less fly gen
         */
        while (quit_value == -1) {
            quit_value = gameController(flies, spider, web);
        }

        //NOTE needs a score based system
        if (quit_value == SPIDER_DEATH)
            System.out.println("Spider Died from lack of food");
        else if (quit_value == QUIT) {
            //NOTE: print out a score
            System.out.println("Thanks for playing!");
        }


    }//end main

    private static int gameController (java.util.ArrayList <Fly> flies, Spider spider, Web web) {
        int quit_value = -1;    //value to quit, adjusts for different kinds of game endings
        String userInput = ""; //NOTE: needs to be a String, for spider input
        //int time = 0;    //amount of turns of game


        //spider iteration checks


        //fly iteration checks
        for (int i = 0; i < flies.size(); i++) {
            if (flies.get(i).struggleFree())
                flies.remove(i);
        }

        //web iteration checks
        web.vibrate(flies, spider);


        System.out.println("Press Enter to iterate and generate a fly, 'quit' to quit");
        userInput = keyboard.nextLine();

        if (userInput.toLowerCase().equals("quit"))
            quit_value = QUIT;
        else if (!spider.alive())
            quit_value = SPIDER_DEATH;

        //add fly to web
        generateFly(flies, web, spider);
        //time++;

        return quit_value;

    }

    /**
     * Generates a fly in a currently unoccupied space
     * @param flies - Fly object ArrayList
     * @param web - Web object, is used for it's array and checking capability
     * @param spider - object, passed into the web checking
     */
    public static void generateFly(java.util.ArrayList < Fly > flies, Web web, Spider spider) {
        int x;  //row
        int y;  //column

        boolean generated = false;

        //gen random locations until empty, then add fly there
        while (!generated) {
            x = random.nextInt(web.getWebLength());
            y = random.nextInt(web.getWebLength());
            if (web.checkElement(x, y ,flies, spider) == 0){
                flies.add(new Fly(web.getWebLength(), x, y,
                        random.nextInt(Fly.getMAXSIZE()) ));
                generated = true;
            }
        }
    }
}//end class
