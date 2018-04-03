//name Penny Grant/Reece Sharp
//date
//project

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//NOTE: to test, I'd recommend inputting 10, or 20, higher numbers are harder to understand

/**
 * Organizes the spider game, the player controls the spider, and moves around
 * their web trying to eat the flies before they die, until the spider dies
 *
 */
public class Game
{
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);	//user input
        Random random = new Random();	//random gen for web, and fly gen

        int webSize; //size of the web

        int time = 0;	//amount of turns of game
        boolean quit = false;	//flag for game exit
        int userInput = 0; //NOTE: needs to be a String, for spider input

        //user gen for web
        System.out.println("Enter an integer for a web size, or -1 for a random size");
        webSize = keyboard.nextInt();

        keyboard.nextLine();

        //if negative entry, random square size between 50-300 in multiples of 10
        if (webSize < 0)
            webSize = random.nextInt((25)*10) + 50;

        //Generate objects in game
        Web web = new Web(webSize);
        Spider spider = new Spider(webSize);
        ArrayList<Fly> flies = new ArrayList<>();

        /*
         * as time goes on
         * 	flies escape quicker
         * 	health decreases more quickly
         * 	less fly gen
         */
        while (!quit) {
            web.vibrate(flies, spider.getX(), spider.getY());
            System.out.println("Enter a generate a Fly to iterate, 'quit' to leave");
            keyboard.nextLine();

            //test flies each turn
            for (int i = 0; i < flies.size(); i++) {
                if (flies.get(i).struggleFree(random.nextDouble()));
                    //flies.remove(i);  //Note: isn't working correctly
            }

            flies.add(new Fly(webSize, random.nextInt(webSize), random.nextInt(webSize)));
            time++;

            if (userInput == -1) {
                quit = true;
            }

        }//end while
    }//end main
}//end class
