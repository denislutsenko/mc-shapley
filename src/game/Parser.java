package game;

import game.exceptions.InvalidPatternException;
import game.model.Player;

import java.math.BigDecimal;
import java.util.*;

import static game.model.PlayerFactory.getPlayer;

public class Parser {

    private final static String CONJUNCTION = "(&|/\\\\)";

    private final static String NEGATION = "(~|-|!)";

    private final static String RULE_REGEX = "\\{\\w+((&|/\\\\)(~|-|!)*\\w+)*\\}(=>|->|=)(-)*\\d+";


    public static Map<Boolean, List<Player>> parsePlayers(String line) throws InvalidPatternException {
        line = line.replace(" ", "");

        if (!line.matches(RULE_REGEX)){
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
        return playerName.matches("(~|-|!)\\w+");
    }

    public static BigDecimal parseValue(String line) throws InvalidPatternException {
        line = line.replace(" ", "");

        if (!line.matches(RULE_REGEX)){
            throw new InvalidPatternException("Input does not match the rule's syntax! Syntax: \"{Ben /\\ !John} -> 5\" (without quotes).");
        }

        line = line.replaceAll("(=>|->|=)", "=");
        String value = line.substring(line.indexOf("=") + 1);
        return new BigDecimal(value);
    }
}
