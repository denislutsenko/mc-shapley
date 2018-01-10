import game.GameController;
import game.exceptions.InvalidPatternException;
import game.exceptions.RuleExistsException;
import game.model.Player;
import game.model.PlayerFactory;
import game.model.Rule;

import static UI.ConsoleUI.print;
import static UI.ConsoleUI.printf;
import static UI.ConsoleUI.readLine;


public class App {

    public static void main(String[] args) {
        initialize();
        while (true){
            try {
                askForRules();
                displayRulesPerPlayers();
            }
            catch (Exception ex){
                print(ex.getMessage());
                GameController.refresh();
            }
        }
    }

    private static void initialize() {
        printf("************************************* Started ************************************%n%n");
        printf(">> All rules should be entered using the following syntax: \"{Ben /\\ !John} -> 5\" (without quotes).%n");
        print("Players' names are case-insensitive (e.g \"Ben\" is the same as \"ben\").");
        print("First literal of the rule can't be negated.");
        print("Symbols allowed to represent conjunction: \"&\", \"/\\\"(slash and backslash).");
        print("Allowed arrows: \"=\", \"=>\". \"->\".");
        print("Allowed negation symbols: \"~\", \"-\", \"!\".");
        print("Only one symbol of a particular type can be used.");
        print("Empty line indicates the end of the input.");
        printf(">> Type \"exit\" to exit the app.");
    }

    private static void displayPlayers() {
        print("Created players: { ");
        for (Player p : PlayerFactory.getPlayers()) {
            printf(p + " ");
        }
        printf("}%n");
    }

    public static void askForRules() throws InvalidPatternException, RuleExistsException {
        printf("%n%nEnter rules:%n");

        String line = null;

        while ((line = readLine()) != null && !line.isEmpty()){
            GameController.initializeRule(line);
        }
    }



    public static void displayRulesPerPlayers(){
        //todo
        for (Player p : PlayerFactory.getPlayers()) {
            System.out.printf("Player %s:%n", p);
            for (Rule rule: p.getRules()){
                System.out.printf("  %s%n", rule);
            }
            System.out.printf("%n");
        }

        GameController.calculateShapley();
    }

}
