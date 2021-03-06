//*********************************************************
// Project: OnyxRidge Construction Management
//
// File: WorkYear.java
//
// Written by: Raymond O'Neill
//
// Date written: 11/1/2020
// Date added: 11/6/2020
//
// Details: Holds information regarding a year of work
//          (the months themselves, which uses composition)
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Removed traces of `WorkMonth.equate(...)`
//      - Reformatted comment header
//      - Removed unused testing code
//*********************************************************
package com.icecrown.onyxridgecm.workseries;

public class WorkYear {
    private WorkMonth[] months = new WorkMonth[12];
    private int year;

    public WorkYear(int year) {
        this.year = year;
    }
    public WorkYear() {
        year = 1900;
    }
    public WorkYear(WorkMonth[] months, int year) {
        this.months = new WorkMonth[months.length];
        for(int i = 0; i < months.length; i++) {
            this.months[i] = new WorkMonth();
            this.months[i] = months[i];
        }
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public WorkMonth[] getMonths() {
        return months;
    }

    public void setMonths(WorkMonth[] months) {
        this.months = months;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonthIndex(int monthOffset, WorkMonth month) {
        if(months[monthOffset] == null) {
            months[monthOffset] = new WorkMonth();
        }
        months[monthOffset] = month;
    }
}
