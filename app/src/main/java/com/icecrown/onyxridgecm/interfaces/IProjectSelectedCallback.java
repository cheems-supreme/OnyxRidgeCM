//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: IProjectSelectedCallback.java
//
// Written by: Raymond O'Neill
//
// Written on: ~11/6/2020
//
// Purpose: Used to handle what happens when a project name
//          is selected in any of the classes its used in
//**************************************************************
package com.icecrown.onyxridgecm.interfaces;

public interface IProjectSelectedCallback {
    void onProjectSelected(String projectName);
}
