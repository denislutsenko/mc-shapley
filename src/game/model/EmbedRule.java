package game.model;

import java.util.Objects;

public class EmbedRule {

    protected final String pattern;
    protected final Rule ef;
    protected final Rule ms;


    public EmbedRule(String pattern, Rule ef, Rule ms) {
        this.pattern = pattern;
        this.ef = ef;
        this.ms = ms;
    }

    public double getEFSV(Player player){
        return ef.getShapleyVal(player);
    }

    public double getMSSV(Player player){
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
}
