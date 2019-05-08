package csp;

import java.time.LocalDate;

/**
 * UnaryDateConstraints are those in which one variable
 * is being compared by some operator, specified by an
 * int L_VAL (for the corresponding variable 
 * / meeting index) and LocalDate R_VAL, such as:
 * 0 == 2019-1-3
 *   OR
 * 3 <= 2019-11-9
 */
public class UnaryDateConstraint extends DateConstraint {

    public final LocalDate R_VAL;
    
    /**
     * Constructs a new BinaryDateConstraint that constrains the meeting
     * times of two meeting variables.
     * @param lVal Meeting variable index that is the left-operand of the
     *        given operator
     * @param operator The logical comparator constraining both variables
     * @param rVal LocalDate literal that constrains the given variable in
     *        the lVal
     */
    UnaryDateConstraint (int lVal, String operator, LocalDate rVal) {
        super(lVal, operator);
        R_VAL = rVal;
    }
    
    @Override
    public String toString () {
        return super.toString() + " " + R_VAL;
    }
    
}
