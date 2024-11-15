/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.  Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 * <p>
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns.
 *
 * @author Michael Kölling and David J. Barnes and Anton Davidouski
 * @version 2016.02.29
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private int NewNumber;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room home, hospital, sauls, rv, pollos, laundromat, dealership, school, jesses;

        home = new Room("Home", "You are in your house.");
        hospital = new Room("Hospital", "You are in the hospital.");
        sauls = new Room("Saul Goodman's office", "You are in Saul Goodman's Law office. Speak to Saul to get more information.");
        sauls.setSecondStageDescription("You are back in Saul's office. He has offered to help you with your operation - for a price of course. Use the command \"give Saul <amount>\" to give him money.");
        rv = new Room("RV", "There is a strange RV parked here, but it is locked - I wonder where I can get the keys?");
        rv.setSecondStageDescription("You are in the RV. It is old but runs - it is good enough for your first laboratory.");
        pollos = new Room("Los Pollos Hermanos", "You in the Los Pollos Hermanos fast food restaurant chain. The food here is good, but not sure what else to do here - maybe an acquaintance can introduce me to somebody?");
        pollos.setSecondStageDescription("You are in Los Pollos Hermanos. Gus Fring will offer you access to his lab - but he needs a large sample of product first. He wants at least 1kg. Use the command \"give Gus <item>\" to give him his sample.");
        laundromat = new Room("Laundromat", "You are at a large commercial laundry facility. It is very busy and deliveries always seem to be going in and out - would be the perfect place to hide an illegal operation, but you can't be sure.");
        laundromat.setSecondStageDescription("You are in Gus Frings Secret underground lab - hidden in the Laundromat. The equipment here is the best you've ever seen - state of the art.");
        dealership = new Room("Car Dealership", "You are at a large car dealership. Speak to the salesperson to see what vehicles are available.");
        dealership.setSecondStageDescription("You are back at the dealership but they have no good offers on any vehicles.");
        school = new Room("School", "You are at the school you teach at. The chemistry department stock room has plenty of useful materials.");
        school.setSecondStageDescription("You are at the school you teach at. They noticed the missing equipment last time and are suspicious. You should not steal anything else.");
        jesses = new Room("Jesse's House", "The house of one of your old chemistry students. Speak to Jesse.");
        jesses.setSecondStageDescription("You are at Jesse's house. If he is home you can speak to Jesse.");

        home.setExit("north-west", hospital);
        home.setExit("north", sauls);
        home.setExit("north-east", rv);
        home.setExit("east", laundromat);
        home.setExit("south-east", jesses);
        home.setExit("south", school);
        home.setExit("south-west", dealership);
        home.setExit("west", pollos);

        hospital.setExit("east", sauls);
        hospital.setExit("south-east", home);
        hospital.setExit("south", pollos);

        sauls.setExit("east", rv);
        sauls.setExit("south-east", laundromat);
        sauls.setExit("south", home);
        sauls.setExit("south-west", pollos);
        sauls.setExit("west", hospital);

        rv.setExit("south", laundromat);
        rv.setExit("south-west", home);
        rv.setExit("west", sauls);

        laundromat.setExit("north", rv);
        laundromat.setExit("south", jesses);
        laundromat.setExit("south-west", school);
        laundromat.setExit("west", home);
        laundromat.setExit("north-west", sauls);

        jesses.setExit("north", laundromat);
        jesses.setExit("west", school);
        jesses.setExit("north-west", home);

        school.setExit("north", home);
        school.setExit("north-east", laundromat);
        school.setExit("east", jesses);
        school.setExit("west", dealership);
        school.setExit("north-west", pollos);

        dealership.setExit("north", pollos);
        dealership.setExit("north-east", home);
        dealership.setExit("east", school);

        pollos.setExit("north", hospital);
        pollos.setExit("north-east", sauls);
        pollos.setExit("east", home);
        pollos.setExit("south-east", school);
        pollos.setExit("south", dealership);

        currentRoom = home;  // start game at home
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getDescription());
        System.out.println(currentRoom.getExitString());
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     *
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }
}
