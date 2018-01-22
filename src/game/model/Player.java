package game.model;

import game.exceptions.RuleExistsException;

import java.math.BigDecimal;
import java.util.*;

public class Player {

    protected final String name;
    protected Set<Rule> rules;
    protected Set<EmbedRule> embedRules;

    public Player(String name) {
        this.name = name;
        this.rules = new LinkedHashSet<>();
        this.embedRules = new LinkedHashSet<>();
    }

    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    public Set<EmbedRule> getEmbedRules() {
        return Collections.unmodifiableSet(embedRules);
    }

    public String getName() {
        return name;
    }

    public void addRule(Rule rule) throws RuleExistsException {
        if (rules.contains(rule)){
            throw new RuleExistsException("Rule with such literals already exists!");
        }
        this.rules.add(rule);
    }

    public void addEmbedRule(EmbedRule embedRule) throws RuleExistsException {
        if (embedRules.contains(embedRule)){
            throw new RuleExistsException("Rule with such literals already exists!");
        }
        this.embedRules.add(embedRule);
    }

    public BigDecimal calculateShapley() {
        BigDecimal total = BigDecimal.ZERO;
        for (Rule rule : rules) {
            total = total.add(BigDecimal.valueOf(rule.getShapleyVal(this)));
        }
        return total;
    }

    public BigDecimal calculateEFSV() {
        BigDecimal total = BigDecimal.ZERO;
        for (EmbedRule embedRule : embedRules) {
            total = total.add(BigDecimal.valueOf(embedRule.calculateEFSV(this)));
        }
        return total;
    }

    public BigDecimal calculateMSSV() {
        BigDecimal total = BigDecimal.ZERO;
        for (EmbedRule embedRule : embedRules) {
            total = total.add(BigDecimal.valueOf(embedRule.calculateMSSV(this)));
        }
        return total;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equalsIgnoreCase(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
