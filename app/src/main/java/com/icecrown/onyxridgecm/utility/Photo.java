//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: Photo.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/26/2020
// Date added: 11/10/2020
//
// Detail: Class to be used with the PhotosAdapter to hold photos
//         taken from Firebase Storage
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Changed name of get...(...) method for TakenBy
//      - Added two `String` variables for first name and last name
//      - Removed `takenBy` `String` variable that held the full name
//      - Made all variables but the `Bitmap` `final`.
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
//      - Refactored method names to lower camel case
//      - Implemented `LocalDate` instead of `Date` instance
//      - Refactored `getDateAsString(...)` to use `LocalDate` and
//        `DateTimeFormatter` classes and methods
//      - Removed unused imports
//*******************************************************************

package com.icecrown.onyxridgecm.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Photo {
    private final String filename;
    private Bitmap thumbnail;
    private final Uri imageUri;
    private final StorageReference ref;
    private final LocalDate dateTaken;
    private final String takenByFirst;
    private final String takenByLast;

    public Photo(String filename, Uri uri, StorageReference ref, LocalDate dateTaken, String takenByLast, String takenByFirst) {
        this.filename = filename;
        imageUri = uri;
        this.ref = ref;
        this.dateTaken = dateTaken;
        this.takenByFirst = takenByFirst;
        this.takenByLast = takenByLast;
    }


    public Uri getImageUri() { return imageUri; }
    public StorageReference getRef()
    {
        return ref;
    }
    public LocalDate getDate()
    {
        return dateTaken;
    }
    public String getDateAsString()
    {
        return dateTaken.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
    public String getTakenBy()
    {
        return takenByFirst + " " + takenByLast;
    }
    public Bitmap getThumbnail() {
        return thumbnail;
    }
    public String getFilename() { return filename; }
    public String getTakenByLastNameFirst() { return takenByLast + ", " + takenByFirst; }

    public String generateBitmapForUri(Context c, ContentResolver res) {
        if(res != null && c != null) {
            try {
                ImageDecoder.Source s = ImageDecoder.createSource(res, imageUri);
                thumbnail = ImageDecoder.decodeBitmap(s);
                return "COMPLETE";
            } catch (IOException e) {
                Log.d("EPOCH-2", "Bitmap thumbnail generation failed. default set");
                thumbnail = BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_broken_image);
                return "FAILED";

            }
        }
        else {
            Log.d("EPOCH-2", "ContentResolver and/or Context null");
            return "NULL";
        }
    }
}
