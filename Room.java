import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 * <p>
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * @author Michael Kölling and David J. Barnes and Anton Davidouski
 * @version 2016.02.29
 */

public class Room {
    private String description;
    private String secondStageDescription;
    private boolean roomSecondStage;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private String name;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     *
     * @param name        The room's name.
     * @param description The room's description.
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        roomSecondStage = true;
        exits = new HashMap<>();
    }

    /**
     * Unlock the room once a certain action has been completed. If available for this room, update its description to match its new state.
     */
    public void unlockRoom() {
        roomSecondStage = false;
        if (secondStageDescription != null) {
            description = secondStageDescription;
        }
    }

    /**
     * Return the room's name in string format.
     * @return the name of the room
     */
    public String getName(){
        return name;
    }

    /**
     * Method used to set a description for the room once it has been unlocked/an action has been completed inside it.
     * Used in Game class when all rooms are initialised.
     *
     * @param newDescription The description which should be displayed when room is unlocked.
     */
    public void setSecondStageDescription(String newDescription) {
        secondStageDescription = newDescription;
    }

    /**
     * Define an exit from this room.
     *
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     *
     * @return Details of the room's exits.
     */
    public String getExitString() {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit + ": " + exits.get(exit).getName() +  "  ";
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     *
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }
}

