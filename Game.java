import java.util.*;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Sahil Sharma
 * @version October 21, 2017
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom; 
    private Stack<Room> stackRooms; 
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        //Initialize variables 
        
        stackRooms = new Stack<Room>(); 
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, cafe;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        cafe = new Room("in the Cafe");
        office = new Room("in the computing admin office");

        // Initialize items inside rooms 
        outside.addItem(new Item(" Garden Hose", 2000)); 
        theatre.addItem(new Item(" Projector", 5000)); 
        pub.addItem(new Item(" Beer", 500)); 
        lab.addItem(new Item(" lab rats", 500));
        cafe.addItem(new Item("Food", 300)); 
        office.addItem(new Item(" Stapler", 100)); 

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);
        cafe.setExit("east",pub); 

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        stackRooms.push(null); 
        currentRoom = outside; // start game outside
        previousRoom = null; 
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if(commandWord.equals("back")){
            back(command);
        }
        else if(commandWord.equals("stackBack")){
            stackBack(command);
        }

        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room 
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            stackRooms.push(currentRoom);
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /** 
     * "look" was entered. checks if one word command and if true prints the long description
     * of the current room. 
     * 
     * @param command The command to be processed
     */

    private void look(Command command){
        if(command.hasSecondWord()){
            System.out.println("look what?"); 
        }
        else{ 
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "eat" was entered. checks if one word command and if true prints stating that you have
     * eaten and are full 
     * 
     * @param command The command to be processed
     */
    private void eat(Command command){

        if(command.hasSecondWord()){
            System.out.println("eat what?"); 
        }
        else{
            System.out.println("You have eaten and are no longer hungry"); 
        }
    }

    /** 
     * "back" was entered. checks if one word command and if true goes back to the previous room 
     * that you were just in. 
     * 
     * @param command The command to be processed
     */

    private void back(Command command){

        if(command.hasSecondWord()){
            System.out.println("back what?"); 
        }
        else if(previousRoom == null){

            System.out.println("There is no previous room, cant go back"); 

        }
        else{
            Room temp = currentRoom;
            currentRoom = previousRoom;
            previousRoom = temp;

            System.out.println("Going back....");
            System.out.println(currentRoom.getLongDescription());

        }
    }

    /** 
     * "stackBack" was entered. checks if one word command and if true rewinds  you back to the rooms
     *  in the order that you had entered them in. 
     * 
     * @param command The command to be processed
     */

    private void stackBack(Command command){
        if(stackRooms.peek() != null){
            previousRoom = currentRoom; 
            currentRoom = stackRooms.pop(); 
            System.out.println(currentRoom.getLongDescription());
        }
        else{
            System.out.println("You are already at the beginning");  
        }

    }
}

