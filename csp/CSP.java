package csp;

import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some unary and binary constraints
 * on the dates of each meeting.
 * @author Manny Barreto
 * @author Bennett Shingledecker
 * @author Andrew Forney
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
        List<DateVar> domains = new ArrayList<DateVar>(nMeetings);
        initializeDomains(domains, nMeetings, rangeStart, rangeEnd);
        
        for (DateConstraint constraint : constraints) {
            if (constraint.arity() == 1) {
                UnaryDateConstraint castedConstraint = (UnaryDateConstraint) constraint;
                nodeConsistency(castedConstraint, domains);
            } else {
                BinaryDateConstraint castedConstraint = (BinaryDateConstraint) constraint;
                constrainWithArcConsistency(castedConstraint, domains);
            }
        }

        List<LocalDate> assignment = new ArrayList<LocalDate>();
        initializeAssignment(assignment, nMeetings);
        
        return backtrack(assignment, nMeetings, domains, constraints);
    }

    /**
     * Recursively backtracks using constrained domains until solution is found.
     * @param assignment Current set of assigned vars
     * @param nMeetings Numbers of meetings
     * @param domains List containing the DataVars with each vars domains
     * @param constraints Set of contraints given in the problem
     * @return Completed assignment
     */
    private static List<LocalDate> backtrack(List<LocalDate> assignment, int nMeetings, List<DateVar> domains, Set<DateConstraint> constraints) {
        if (!assignment.contains(null)) {
            return assignment;
        }

        int unassignedVarIndex = getUnassignedVar(assignment, nMeetings);

        for (LocalDate date : domains.get(unassignedVarIndex).domain) {
            assignment.set(unassignedVarIndex, date);
            
            if (checkAssignmentConsistency(assignment, constraints)) {
                List<LocalDate> result = backtrack(assignment, nMeetings, domains, constraints);
                if (result != null) {
                    return result;
                }
            }
            
            assignment.set(unassignedVarIndex, null);
        }

        return null;
    }
    
    public static void nodeConsistency(UnaryDateConstraint constraint, List<DateVar> domains) {
        HashSet<LocalDate> domain = copyDomain(domains.get(constraint.L_VAL).domain);
        for (LocalDate date : domains.get(constraint.L_VAL).domain) {
            switch (constraint.OP) {
                case "==": if (!date.isEqual(constraint.R_VAL))   domain.remove(date); break;
                case "!=": if (date.isEqual(constraint.R_VAL))  domain.remove(date); break;
                case ">":  if (date.isBefore(constraint.R_VAL) || date.isEqual(constraint.R_VAL))  domain.remove(date); break;
                case "<":  if (date.isAfter(constraint.R_VAL) || date.isEqual(constraint.R_VAL))  domain.remove(date); break;
                case ">=": if (date.isBefore(constraint.R_VAL)) domain.remove(date); break;
                case "<=": if (date.isAfter(constraint.R_VAL)) domain.remove(date); break;
            }
        }
        domains.set(constraint.L_VAL, new DateVar(domain));
    }
    
    public static void constrainWithArcConsistency(BinaryDateConstraint constraint, List<DateVar> domains) {
        HashSet<LocalDate> leftDomain = copyDomain(domains.get(constraint.L_VAL).domain);
        HashSet<LocalDate> rightDomain = domains.get(constraint.R_VAL).domain;
        
        if (rightDomain.isEmpty()) {
            return;
        }
        
        for (LocalDate leftDate : domains.get(constraint.L_VAL).domain) {
            Boolean found = false;
            switch (constraint.OP) {
                case "==": 
                    leftDomain.retainAll(rightDomain);
                    return;
                case "!=":
                    found = false;
                    for (LocalDate rightDate : rightDomain) {
                        if (!leftDate.isEqual(rightDate)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { leftDomain.remove(leftDate); }
                    break;
                case ">":
                    found = false;
                    for (LocalDate rightDate : rightDomain) {
                        if (leftDate.isAfter(rightDate)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { leftDomain.remove(leftDate); }
                    break;
                case "<":
                    found = false;
                    for (LocalDate rightDate : rightDomain) {
                        if (leftDate.isBefore(rightDate)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { leftDomain.remove(leftDate); }
                    break;
                case ">=":
                    found = false;
                    for (LocalDate rightDate : rightDomain) {
                        if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { leftDomain.remove(leftDate); }
                    break;
                case "<=":
                    found = false;
                    for (LocalDate rightDate : rightDomain) {
                        if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { leftDomain.remove(leftDate); }
                    break;
            }
        }
        domains.get(constraint.L_VAL).domain = leftDomain;
    }


    // Helper Methods / Classes
    /**
     * Fills assignment List variable with nulls.
     * @param assignment List to be filled
     * @param size Number of assignments needing to be made
     */
    public static void initializeAssignment(List<LocalDate> assignment, int size) {
        for (int i = 0; i < size; i++) {
            assignment.add(null);
        }
    }

    /**
     * Initializes the domains variable with all dates by days from rangeStart to rangeEnd
     * @param domains List to be filled with domain
     * @param size  Size of the list
     * @param rangeStart Where the initial domains begin
     * @param rangeEnd Where the initial domains end
     */
    public static void initializeDomains(List<DateVar> domains, int size, LocalDate rangeStart, LocalDate rangeEnd) {
        for (int i = 0; i < size; i++) {
            HashSet<LocalDate> newDomain = new HashSet<LocalDate>();
            for (LocalDate currentDate = rangeStart; currentDate.isBefore(rangeEnd) || currentDate.isEqual(rangeEnd); currentDate = currentDate.plusDays(1)) {
                newDomain.add(currentDate);
            }   
            domains.add(new DateVar(newDomain));
        }
    }
    
    /**
     * Copies HashSet into a new one to prevent concurrency errors
     * @param domain to be copied
     * @return New HashSet copy of the domain
     */
    public static HashSet<LocalDate> copyDomain(HashSet<LocalDate> domain) {
        HashSet<LocalDate> result = new HashSet<LocalDate>();
        
        for (LocalDate date : domain) {
            result.add(date);
        }
        
        return result;
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
    
    /**
     * Iterates through all the constraints checking if given assignment is valid.
     * @param assignment Current assignments to be checked for consistency
     * @param constraints List of constraints used for checking
     * @return True or False whether or not assignments are consistent
     */
    public static boolean checkAssignmentConsistency(List<LocalDate> assignment, Set<DateConstraint> constraints){
        for (DateConstraint constraint : constraints){
            if (constraint.arity() == 2) {
                BinaryDateConstraint castedConstraint = (BinaryDateConstraint) constraint;
                if (assignment.get(castedConstraint.L_VAL) != null && assignment.get(castedConstraint.R_VAL) != null) {
                    if (!checkConsistency(assignment.get(castedConstraint.L_VAL), assignment.get(castedConstraint.R_VAL), constraint)) {
                        return false;
                    }
                } 
            } else {
                UnaryDateConstraint castedConstraint = (UnaryDateConstraint) constraint;
                if (assignment.get(castedConstraint.L_VAL) != null) {
                    if (!checkConsistency(assignment.get(castedConstraint.L_VAL), castedConstraint.R_VAL, constraint)) {
                        return false;
                    }
                }
            }
            
        }
        return true;
    }

    public static class DateVar {
        public HashSet<LocalDate> domain;
        
        public DateVar (HashSet<LocalDate> domain) {
            this.domain = domain;
        }
    }
    
}
