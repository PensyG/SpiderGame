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

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Organizes the spider game, the player controls the spider, and moves around
 * their web trying to eat the flies before they die, until the spider dies
 * Goal for the program is to be completely dynamic, based on the user input size
 * of the web (above 10 or so, below that and methods don't function as they should)
 */
public class Game {
    private static final int FLY_TURN_GEN = Calculate.FLY_TURN_GEN; //after 'X' amount of turns, generate a fly
    private static Scanner keyboard = new Scanner(System.in);      //user input
    private static Random random = new Random();                   //random gen for web, and fly gen

    public static void main(String[] args) {

        int webLength;      //size of the web

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
        Fly.flies = new ArrayList<>();

        //start game
        gameController(spider, web);
    }//end main

    private static void gameController(Spider spider, Web web) {
        int time = 1;       //amount of turns of game
        String userInput = "";

        while (userInput != "quit") {

            //NOTE:

            System.out.println("Time: " + time);

            //fly iteration checks
            Fly.struggleFree();
            Calculate.debugFlies();

            //*********
            //web iteration checks
            Calculate.calculateElements(web);
            //Calculate.debugWeb(web, spider);    //shows entire web, can be slow in webs wider than 50

            //*********
            //spider iteration checks/
            System.out.println("Health: " + spider.getHealth());
            Calculate.generateSpiderView(web, spider);

            //if (userInput != "quit")
            spider.move(web, userInput);

            //if ()                         //test if in fly location to consume
            //spider.eatFly()

            //add fly to web
            if (time % FLY_TURN_GEN == 0)
                generateFly(web, spider);
            time++;
            System.out.println("End Turn.\n" +
                    "--------------------\n");
        }
    }

    /**
     * Generates a fly in a currently unoccupied space
     *
     * @param web    - Web object, is used for it's array and checking capability
     * @param spider - object, passed into the web checking
     */
    private static void generateFly(Web web, Spider spider) {
        int x;  //row
        int y;  //column

        boolean generated = false;

        //gen random locations until empty spot is found, then add fly there
        while (!generated) {
            x = random.nextInt(web.getWebLength());
            y = random.nextInt(web.getWebLength());
            if (Calculate.checkElement(x, y, spider) == 0) {
                Fly.flies.add(new Fly(web.getWebLength(), x, y));
                generated = true;
            }
        }
    }//end class
}
