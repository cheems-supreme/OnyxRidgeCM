//*********************************************************
// Project: BCS430W - Senior Project
//
// Project Name: OnyxRidge
//
// File: WorkMonth.java
//
// Written by: Raymond O'Neill
//
// Date written: 11/1/20
//
// Details: Holds information regarding a month of work
//          (the amount of days in said month
// ------------------------------------------------
// - 11/1/20
// - R.O.
// - DETAILS:
//      - Added a method to set a day to a given day, without
//        needing the whole month's worth of days.
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
        daysInMonth = DetermineDaysInMonth(monthOffset % 2, year);
    }

    public WorkMonth(int year, int monthOffset) {
        belongingToYear = year;
        this.monthOffset = monthOffset;
        monthActual = monthOffset + 1;
        daysInMonth = DetermineDaysInMonth(monthOffset, year);
        days = new WorkDay[daysInMonth];
        monthName = DetermineMonthName(monthOffset);
    }

    public void GenerateMockWorkMonthData(int year, int monthActual) {
        belongingToYear = year;
        this.monthActual = monthActual;
        monthOffset = monthActual - 1;
        monthName = DetermineMonthName(monthOffset);
        daysInMonth = DetermineDaysInMonth(monthActual, year);

        days = new WorkDay[daysInMonth];
        for(int i = 0; i < daysInMonth; i++) {
            days[i] = new WorkDay(new GregorianCalendar(year, monthOffset, (i + 1)), 15 * (10 + (i * 100.00)));
        }

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

    public WorkMonth equate(WorkMonth month) {
        for(int i = 0; i < daysInMonth; i++) {
            this.days[i] = new WorkDay(month.getDays()[i]);
        }
        monthOffset = month.monthOffset;
        monthActual = month.monthActual;
        daysInMonth = month.daysInMonth;
        monthName = month.monthName;
        belongingToYear = month.belongingToYear;
        return this;
    }

    private int DetermineDaysInMonth(int monthOffset, int year) {
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
    public static String DetermineMonthName(int monthOffset) {
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

    public double GenerateTotalMonthlyHours() {
        double totalHours = 0.00;
        for(WorkDay day : days) {
            if(day != null) {
                totalHours += day.getHours();
            }
        }
        return totalHours;
    }

    public void SetDayAtInstance(WorkDay day, int dayOffset) {
        if(days[dayOffset] == null) {
            days[dayOffset] = new WorkDay();
        }
        days[dayOffset].equate(day);
    }
}