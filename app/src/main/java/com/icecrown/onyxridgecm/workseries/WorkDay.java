//*********************************************************
// Project: OnyxRidge Construction Management
//
// File: WorkDay.java
//
// Written by: Raymond O'Neill
//
// Date written: 11/1/2020
// Date added: 11/6/2020
//
// Details: Holds information regarding a day of work
//          (the day itself and the total hours worked)
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/8/2020
// - R.O.
// - DETAILS:
//      - Changed initial variable value
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
//      - Refactored method names to lower camel case
//*********************************************************
package com.icecrown.onyxridgecm.workseries;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorkDay {
    private GregorianCalendar day;
    private double hours;

    public WorkDay() {
        day = new GregorianCalendar();
        hours = 0;
    }
    public WorkDay(GregorianCalendar day, double hours)
    {
        this.day = new GregorianCalendar(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
        this.hours = hours;
    }
    public WorkDay(WorkDay day) {
        this.equate(day);
    }

    public double getHours() {
        return hours;
    }

    public GregorianCalendar getDay() {
        return day;
    }

    public void setDay(GregorianCalendar day) {
        this.day = day;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
    public WorkDay equate(WorkDay day) {
        if(day != null) {
            this.day = new GregorianCalendar(day.getDay().get(Calendar.YEAR), day.getDay().get(Calendar.MONTH), day.getDay().get(Calendar.DAY_OF_MONTH));
            this.hours = day.hours;
        }
        else {
            day = new WorkDay();
        }
        return this;
    }

    public String generateHyphenDateString() {
        String s = (day.get(Calendar.MONTH) + 1) + "-" + day.get(Calendar.DAY_OF_MONTH) + "-" + day.get(Calendar.YEAR);
        return s;
    }

}
