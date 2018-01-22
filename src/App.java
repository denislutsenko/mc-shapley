import game.GameController;
import game.exceptions.InvalidPatternException;
import game.exceptions.RuleExistsException;
import game.model.EmbedRule;
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
                int mode = askGameMode();
                askForRules(mode);
                displayRulesPerPlayers(mode);
            }
            catch (NumberFormatException ex){
                print("Invalid option!. Choose number from the list.");
            }
            catch (Exception ex){
                print(ex.getMessage());
                GameController.refresh();
            }
        }
    }

    public static int askGameMode() throws Exception {
        printf("%n%nChoose MC-nets:%n");
        print("1 - simple MC-nets");
        print("2 - embedded MC-nets");

        String line = null;

        line = readLine();

        int mode = Integer.parseInt(line);

        if (mode < 1 || mode > 2){
            throw new Exception("Error!. Choose net from the list above!");
        }
        return mode;
    }

    public static void initialize() {
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

    public static void askForRules(int mode) throws Exception {
        printf("%n%nEnter rules:%n");

        String line = null;

        while ((line = readLine()) != null && !line.isEmpty()){
            if (mode > 1) {
                GameController.initializeEmbedRule(line);
            } else {
                GameController.initializeRule(line);
            }
        }
    }


    public static void displayRulesPerPlayers(int mode) {
        for (Player p : PlayerFactory.getPlayers()) {
            printf(String.format("Player %s:%n", p));
            if (mode > 1) {
                for (EmbedRule embedRule : p.getEmbedRules()) {
                    printf(String.format("  %s%n", embedRule));
                }
                printf("%n");
            } else {
                for (Rule rule : p.getRules()) {
                    printf(String.format("  %s%n", rule));
                }
                printf("%n");
            }
        }

        GameController.calculateShapley(mode);
    }

}
