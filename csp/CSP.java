package csp;

import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some unary and binary constraints
 * on the dates of each meeting.
 */
public class CSP {

    /**
     * Public interface for the CSP solver in which the number of meetings,
     * range of allowable dates for each meeting, and constraints on meeting
     * times are specified.
     * @param nMeetings The number of meetings that must be scheduled, indexed from 0 to n-1
     * @param rangeStart The start date (inclusive) of the domains of each of the n meeting-variables
     * @param rangeEnd The end date (inclusive) of the domains of each of the n meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of the n meetings,
     *         indexed by the variable they satisfy, or null if no solution exists.
     */
    public static List<LocalDate> solve (int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
        //List<DateVar> domains = new List<LocalDate>(nMeetings);

        List<LocalDate> assignment = new ArrayList<LocalDate>();
        initializeAssignment(assignment, nMeetings);
        

        DateVar probDomain = new DateVar(rangeStart, rangeEnd);
        return backtrack(assignment, nMeetings, probDomain, constraints);
    }

    // TODO
    private static List<LocalDate> backtrack(List<LocalDate> assignment, int nMeetings, DateVar probDomain, Set<DateConstraint> constraints) {
        if (assignment.size() == nMeetings) {
            return assignment;
        }

        int unassignedVars = getUnassignedVar(assignment, nMeetings);

        for (LocalDate current = probDomain.rangeStart; current.isBefore(probDomain.rangeEnd); current = current.plusDays(1)) {
            assignment.add(unassignedVars, current);
            if (checkAssignmentConsistency(assignment, constraints)) {
                List<LocalDate> result = backtrack(assignment, nMeetings, probDomain, constraints);
                if (checkAssignmentConsistency(result, constraints)) {
                    return result;
                }
            }
            assignment.remove(unassignedVars);
        }

        return null;
    }
    
//    public static nodeConsistency(){
//        //if(domain value conflicts with unary constraint){
//        //  remove it;
//        //}
//        
//    }


    // Helper Methods / Classes
    public static void initializeAssignment(List<LocalDate> assignment, int size) {
        for(int i = 0; i < size; i++) {
            assignment.add(null);
        }
    }

    /**
     * Helper function that returns variable that has yet to be assigned
     * in the assignment list by MRV
     * @param assignment List of assignments currently made
     * @param nMeetings number of meetings
     */
    private static int getUnassignedVar(List<LocalDate> assignment, int nMeetings) {
        for (int i = 0; i < nMeetings; i++) {
            if (assignment.get(i) == null) {
                return i;
            }
        }
        return 0;
    }

    public static class DateVar {
        LocalDate rangeStart;
        LocalDate rangeEnd;

        public DateVar (LocalDate rangeStart, LocalDate rangeEnd) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }
    }

    /**
     * Checks if two dates pass a constrant. leftDate constraint rightDate == true?
     * @param leftDate
     * @param rightDate
     * @param constraint
     * @return boolean of whether or not the two dates pass
     */
    public static boolean checkConsistency (LocalDate leftDate, LocalDate rightDate, DateConstraint constraint) {
        boolean isConsistent = false;
        switch (constraint.OP) {
            case "==": if (leftDate.isEqual(rightDate))  isConsistent = true; break;
            case "!=": if (!leftDate.isEqual(rightDate)) isConsistent = true; break;
            case ">":  if (leftDate.isAfter(rightDate))  isConsistent = true; break;
            case "<":  if (leftDate.isBefore(rightDate)) isConsistent = true; break;
            case ">=": if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate))  isConsistent = true; break;
            case "<=": if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) isConsistent = true; break;
        }
        
        return isConsistent;
    }
    public static boolean checkAssignmentConsistency(List<LocalDate> assignment, Set<DateConstraint> constraints){
        //check if unary or binary
        //use checkConsistency
        //go through all constraints
        boolean isConsistent = true;
        for (DateConstraint constraint : constraints){
            if (constraint.arity() == 2) {
                BinaryDateConstraint castedConstraint = (BinaryDateConstraint) constraint;
                if (assignment.get(castedConstraint.L_VAL) == null || assignment.get(castedConstraint.L_VAL) == null) {
                    break;
                } else {
                    checkConsistency(assignment.get(castedConstraint.L_VAL), assignment.get(castedConstraint.R_VAL), constraint);
                }
            } else {
                UnaryDateConstraint castedConstraint = (UnaryDateConstraint) constraint;
                if (assignment.get(castedConstraint.L_VAL) == null) {
                    break;
                } else {
                    checkConsistency(assignment.get(castedConstraint.L_VAL), castedConstraint.R_VAL, constraint);
                }
            }
            
        }
        return isConsistent;
    }

    
}
