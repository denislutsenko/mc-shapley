package game;

import UI.ConsoleUI;
import game.exceptions.InvalidPatternException;
import game.exceptions.RuleExistsException;
import game.model.EmbedRule;
import game.model.Player;
import game.model.PlayerFactory;
import game.model.Rule;

import java.math.BigDecimal;
import java.util.*;

public class GameController {

    public static void initializeEmbedRule(String line) throws Exception {
        Rule ef = null;
        Rule ms = null;

        String efLine = null;
        String msLine = null;

        Set<Player> allPlayers = new LinkedHashSet<>();

        try {
            efLine = Parser.convertLineToEf(line);
            Map<Boolean, List<Player>> literals = Parser.parsePlayers(efLine);
            ef = new Rule(efLine, literals.get(true), literals.get(false), Parser.parseValue(efLine));
            allPlayers.addAll(literals.get(true));
            allPlayers.addAll(literals.get(false));
        } catch (Exception ex) {
            ConsoleUI.print("Can't be converted to EF!");
        }


        try {
            msLine = Parser.convertLineToMS(line);
            Map<Boolean, List<Player>> literals = Parser.parsePlayers(msLine);
            ms = new Rule(msLine, literals.get(true), literals.get(false), Parser.parseValue(msLine));
            allPlayers.addAll(literals.get(true));
            allPlayers.addAll(literals.get(false));
        } catch (Exception ex) {
            ConsoleUI.print("Can't be converted to MS!");
        }

        if (ef == null && ms == null) {
            throw new Exception("Entered rule is not valid!");
        }

        EmbedRule embedRule = new EmbedRule(line, ef, ms);

        for (Player player : allPlayers) {
            player.addEmbedRule(embedRule);
        }

    }

    public static void initializeRule(String line) throws InvalidPatternException, RuleExistsException {
        Map<Boolean, List<Player>> literals = Parser.parsePlayers(line);
        Rule rule = new Rule(line, literals.get(true), literals.get(false), Parser.parseValue(line));

        List<Player> allPlayers = new ArrayList<>(literals.get(true));
        allPlayers.addAll(literals.get(false));

        addRuleToPlayers(rule, allPlayers);
    }

    private static void addRuleToPlayers(Rule rule, List<Player> players) throws RuleExistsException {
        for (Player p : players) {
            p.addRule(rule);
        }

    }

    public static void calculateShapley(int mode){
        if (mode < 2) {
            Map<Player, BigDecimal> shapley = new HashMap<>();

            for (Player p : PlayerFactory.getPlayers()) {
                shapley.put(p, p.calculateShapley());
            }

            ConsoleUI.print("Shapley value of the game:");
            ConsoleUI.printf(shapley);
        } else {
            Map<Player, BigDecimal> efsv = new HashMap<>();
            Map<Player, BigDecimal> mssv = new HashMap<>();
            for (Player p : PlayerFactory.getPlayers()) {
                efsv.put(p, p.calculateEFSV());
                mssv.put(p, p.calculateMSSV());

            }

            ConsoleUI.print("EFSV: " + efsv);
            ConsoleUI.print("MSSV: " + mssv);
        }
        ConsoleUI.printf("%n********************************************************************************");
        refresh();
    }

    public static void refresh(){
        PlayerFactory.refresh();
    }
}
