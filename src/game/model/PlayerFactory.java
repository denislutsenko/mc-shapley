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

    public static Map<Boolean, List<Player>> parsePlayers(String line) throws InvalidPatternException {
        line = line.replace(" ", "");

        if (!line.matches("\\{\\w*((&|/\\\\)(~|-|!)*\\w+)*\\}(=>|->|=)(-)*\\d+")){
            throw new InvalidPatternException("Input is not valid! Syntax: \"{Ben /\\ !John} -> 5\" (without quotes).");
        }

        if (line.contains("{") && line.contains("}")){
            line = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
        }

        Map<Boolean, List<Player>> players = new HashMap<>();
        players.put(true, new ArrayList<>());
        players.put(false, new ArrayList<>());

        String[] names = line.split("&|/\\\\");
        for (String name : names) {
            boolean isPositive = !isNegated(name);
            name = isPositive ? name : name.substring(1);
            players.get(isPositive).add(getPlayer(name));
        }

        return Collections.unmodifiableMap(players);
    }

    private static boolean isNegated(String playerName){
        return playerName.matches("^(~|-|!)[a-zA-Z0-9_]*");
    }

    public static void refresh(){
        players = new ArrayList<>();
    }
}
