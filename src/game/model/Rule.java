package game.model;

import game.exceptions.InvalidPatternException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Rule {

    protected final String pattern;
    protected final List<Player> posLiterals;
    protected final List<Player> negLiterals;
    protected final BigDecimal value;
    protected final double posShapV;
    protected final double negShapV;


    private static long factorial(long number){

        if (number < 0)
            throw new RuntimeException("Can't calculate factorial of negative value.");

        long total = 1;
        for (long i = 2; i <= number; i++){
            total *= i;
        }

        return total;
    }

    public Rule(String pattern, List<Player> posLiterals, List<Player> negLiterals, BigDecimal value) {
        this.pattern = pattern;
        this.posLiterals = posLiterals;
        this.negLiterals = negLiterals;
        this.value = value;
        this.posShapV = calculateShapV(true);
        this.negShapV = calculateShapV(false);
    }

    private double calculateShapV(boolean positive){
        long p = posLiterals.size();
        long n = negLiterals.size();

        if (positive && p == 0 || !positive && n == 0)
            return 0.0;

        long facN = factorial(n);
        long facP = factorial(p);

        BigDecimal numerator = positive ? new BigDecimal(factorial(p - 1) * facN).multiply(value)
                                        : new BigDecimal(facP * factorial(n - 1)).multiply(value.negate());

        BigDecimal denominator = new BigDecimal(factorial(p + n));

        return round(numerator.doubleValue()/denominator.doubleValue(), 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getShapleyVal(Player player) {
        double total = 0.0;

        if (posLiterals.contains(player)){
            total += posShapV;
        }

        if (negLiterals.contains(player)){
            total += negShapV;
        }

        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return posLiterals.equals(rule.posLiterals) && negLiterals.equals(rule.negLiterals);
    }

    @Override
    public int hashCode() {

        return Objects.hash(posLiterals, negLiterals);
    }

    @Override
    public String toString() {
        return String.format("Rule: %-20s p = %-2d n = %-2d value = %-10s positive literal ShapVal  = %-10s negative literal ShapVal = %-10s",
                            pattern, posLiterals.size(), negLiterals.size(), value, posShapV, negShapV);
    }
}
