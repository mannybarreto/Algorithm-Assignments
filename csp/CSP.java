package csp;

import java.time.LocalDate;
import java.util.Set;
import java.util.List;

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
        //throw new UnsupportedOperationException();
        List<LocalDate> assignment = new List<LocaleDate>();
        DateVar probDomain = new DateVar(rangeStart, rangeEnd);
        return backtrack(assignment, nMeetings, probDomain, constraints);
    }

    // TODO
    private static List<LocalDate> backtrack(List<LocalDate> assignment, int nMeetings, DateVar probDomain, Set<DateConstraint> constraints) {
        if (assignment.size() == nMeetings) {
            return assignment;
        }

        List<Integer> unassignedVars = getUnassaignedVars(assignment, nMeetings);

        for (LocalDate current = probDomain.rangeStart; current.isBefore(probDomain.rangeEnd); current = current.plusDays(1)) {
            
        }

        return null;
    }


    // Helper Methods / Classes
    /**
     * Helper function that returns a list of variables that have yet to be assigned
     * in the assignment list.
     * @param assignment List of assignments currently made
     * @param nMeetings number of meetings
     */
    private static List<Integer> getUnassignedVars(List<LocalDate> assignment, int nMeetings) {
        List<Integer> result = new List<Integer>();
        for (int i = 0; i < nMeetings; i++) {
            if (assignment.get(i) == null) {
                result.add(i);
            }
        }
    }

    public static class DateVar {
        LocalDate rangeStart;
        LocalDate rangeEnd;

        public DateVar (LocalDate rangeStart, LocalDate rangeEnd) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }
    }

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
}
