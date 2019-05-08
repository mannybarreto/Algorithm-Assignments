package csp;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * DateConstraint superclass: all date constraints will have
 * an L_VAL variable and some operation that compares it to
 * some other variable or date value.
 */
public abstract class DateConstraint {

    public final int L_VAL;
    public final String OP;
    
    private final Set<String> LEGAL_OPS = new HashSet<>(
        Arrays.asList("==", "!=", "<", "<=", ">", ">=")
    );
    
    DateConstraint (int lVal, String operator) {
        if (!LEGAL_OPS.contains(operator)) {
            throw new IllegalArgumentException("Invalid constraint operator");
        }
        if (lVal < 0) {
            throw new IllegalArgumentException("Invalid variable index");
        }
        
        L_VAL = lVal;
        OP = operator;
    }
    
    /**
     * The arity of a constraint determines the number of variables
     * found within
     * @return 1 for UnaryDateConstraints, 2 for Binary
     */
    public int arity () {
        return (this instanceof UnaryDateConstraint) ? 1 : 2;
    }
    
    @Override
    public String toString () {
        return L_VAL + " " + OP;
    }
    
}
