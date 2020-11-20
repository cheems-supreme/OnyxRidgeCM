//*********************************************************
// Project: OnyxRidge Construction Management
//
// File: WorkMonth.java
//
// Written by: Raymond O'Neill
//
// Date written: 11/1/2020
// Date added: 11/6/2020
//
// Details: Holds information regarding a month of work
//          (the amount of days in said month
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/18/2020
// - R.O.
// - DETAILS:
//      - Added method to determine month string based on
//        month offset number
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Removed `equate(...)` method.
//      - Removed unused testing code
//      - Reformatted comment header
//      - Refactored method names to lower camel case
//*********************************************************
package com.icecrown.onyxridgecm.workseries;

import java.util.GregorianCalendar;

public class WorkMonth {
    private WorkDay[] days;
    // Calendar.get(MONTH)
    private int monthOffset;
    // Calendar.get(MONTH) + 1
    private int monthActual;
    private int daysInMonth;
    private String monthName;
    private int belongingToYear;

    public WorkMonth() {
        days = new WorkDay[0];
        monthOffset = -1;
        monthActual = 0;
        belongingToYear = 1900;
        monthName = "";
        daysInMonth = 0;
    }
    public WorkMonth(WorkDay[] days, int monthOffset, int monthActual, int year, String monthName) {
        this.days = days;
        this.monthActual = monthActual;
        this.monthOffset = monthOffset;
        belongingToYear = year;
        this.monthName = monthName;
        daysInMonth = determineDaysInMonth(monthOffset % 2, year);
    }

    public WorkMonth(int year, int monthOffset) {
        belongingToYear = year;
        this.monthOffset = monthOffset;
        monthActual = monthOffset + 1;
        daysInMonth = determineDaysInMonth(monthOffset, year);
        days = new WorkDay[daysInMonth];
        monthName = determineMonthName(monthOffset);
    }


    public int getBelongingToYear() {
        return belongingToYear;
    }

    public String getMonthName() {
        return monthName;
    }

    public WorkDay[] getDays() {
        return days;
    }

    public int getDaysInMonth() {
        return daysInMonth;
    }

    public int getMonthActual() {
        return monthActual;
    }

    public int getMonthOffset() {
        return monthOffset;
    }

    public void setDays(WorkDay[] days) {
        this.days = days;
    }

    public void setDaysInMonth(int daysInMonth) {
        this.daysInMonth = daysInMonth;
    }

    public void setBelongingToYear(int belongingToYear) {
        this.belongingToYear = belongingToYear;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setMonthActual(int monthActual) {
        this.monthActual = monthActual;
    }

    public void setMonthOffset(int monthOffset) {
        this.monthOffset = monthOffset;
    }

    private int determineDaysInMonth(int monthOffset, int year) {
        switch(monthOffset) {
            case 1:
                GregorianCalendar cal = new GregorianCalendar(2020, 1, 1);
                if (cal.isLeapYear(year)) {
                    cal = null;
                    return 29;
                } else {
                    cal = null;
                    return 28;
                }
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                return 0;
        }
    }
    public static String determineMonthName(int monthOffset) {
        switch(monthOffset) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Not a month";
        }
    }

    public static int determineMonthOffset(String monthName) {
        switch(monthName) {
            case "january":
                return 0;
            case "february":
                return 1;
            case "march":
                return 2;
            case "april":
                return 3;
            case "may":
                return 4;
            case "june":
                return 5;
            case "july":
                return 6;
            case "august":
                return 7;
            case "september":
                return 8;
            case "october":
                return 9;
            case "november":
                return 10;
            case "december":
                return 11;
            default:
                return -1;
        }
    }
    public double generateTotalMonthlyHours() {
        double totalHours = 0.00;
        for(WorkDay day : days) {
            if(day != null) {
                totalHours += day.getHours();
            }
        }
        return totalHours;
    }

    public void setDayAtInstance(WorkDay day, int dayOffset) {
        if(days[dayOffset] == null) {
            days[dayOffset] = new WorkDay();
        }
        days[dayOffset] = day;
    }
}
