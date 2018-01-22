package game.model;

import java.util.Objects;

public class EmbedRule {

    protected final String pattern;
    protected final Rule ef;
    protected final Rule ms;
    protected final Rule simple;


    public EmbedRule(String pattern, Rule ef, Rule ms, Rule simple) {
        this.pattern = pattern;
        this.ef = ef;
        this.ms = ms;
        this.simple = simple;
    }

    public double calculateEFSV(Player player){
        if (ef == null){
            return 0.0;
        }

        return ef.getShapleyVal(player);
    }

    public double calculateMSSV(Player player){
        if (ms == null){
            return 0.0;
        }

       return ms.getShapleyVal(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbedRule embedRule = (EmbedRule) o;
        return pattern.equals(embedRule.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }

    @Override
    public String toString() {
        String initial = String.format("Embedded rule: %-20s", pattern);
        if (ef != null){
            initial += String.format("%n\tEFSV %s", ef);
        }
        if (ms != null){
            initial += String.format("%n\tMSSV %s", ms);
        }
        if (simple != null){
            initial += String.format("%n\tSimple %s", simple);
        }
        return initial;
    }
}
