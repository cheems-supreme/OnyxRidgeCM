//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: Document.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/8/2020
// Date added: 11/6/2020
//
// Detail: Class to be used with the DocumentsAdapter.java class
//         contains information about a document and a reference
//         to said document.
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
//      - Reformatted `import` list
//*******************************************************************
package com.icecrown.onyxridgecm.utility;

import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

import java.util.Date;


public class Document {
    private final StorageReference ref;
    private final String filename;
    private final Date dateCreated;
    private final Date dateOfContent;
    private final String writtenByFirst;
    private final String writtenByLast;
    private final boolean accident;

    public Document(StorageReference ref, String filename, Date dateCreated, Date dateOfContent, String writtenByFirst, String writtenByLast, boolean accident)
    {
        this.ref = ref;
        this.filename = filename;
        this.dateCreated = dateCreated;
        this.dateOfContent = dateOfContent;
        this.writtenByFirst = writtenByFirst;
        this.writtenByLast = writtenByLast;
        this.accident = accident;
    }

    public StorageReference getRef()
    {
        return ref;
    }
    public String getFilename()
    {
        return filename;
    }
    public Date getDate()
    {
        return dateCreated;
    }
    public boolean hasAccident() {
        return accident;
    }
    public Date getDateOfContent() {
        return dateOfContent;
    }
    public String getDateAsString() { return new SimpleDateFormat("MM/dd/yyyy").format(dateCreated); }
    public String getAuthor()
    {
        return writtenByFirst + " " + writtenByLast;
    }
    public String getAuthorByLast() { return writtenByLast + ", " + writtenByFirst; }
    public String getDateOfContentAsString() { return new SimpleDateFormat("MM/dd/yyyy").format(dateOfContent); }
}
