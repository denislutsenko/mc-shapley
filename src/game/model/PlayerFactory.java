package game.model;

import game.exceptions.InvalidPatternException;
import java.util.*;

public class PlayerFactory {

    private static List<Player> players = new ArrayList<>();


    public static Player getPlayer(String name) throws InvalidPatternException {
        
        if (name == null || name.isEmpty()) {
            throw new InvalidPatternException("Player's name cannot be empty!");
        }

        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }

        Player newPlayer = new Player(name);
        players.add(newPlayer);

        return newPlayer;
    }

    public static List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }

    public static void refresh(){
        players = new ArrayList<>();
    }
}
