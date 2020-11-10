//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: Photo.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/26/20
// Date added: 11/10/20
//
// Detail: Class to be used with the PhotosAdapter to hold photos
//         taken from Firebase Storage
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {
    private String filename;
    private Bitmap thumbnail;
    private Uri imageUri;
    private StorageReference ref;
    private Date dateTaken;
    private String takenBy;

    public Photo(String filename, Uri uri, StorageReference ref, Date dateTaken, String takenBy) {
        this.filename = filename;
        imageUri = uri;
        this.ref = ref;
        this.dateTaken = dateTaken;
        this.takenBy = takenBy;
    }


    public Uri getImageUri() { return imageUri; }
    public StorageReference getRef()
    {
        return ref;
    }
    public Date getDate()
    {
        return dateTaken;
    }
    public String getDateAsString()
    {
        return new SimpleDateFormat("MM/dd/yyyy").format(dateTaken);
    }
    public String getAuthor()
    {
        return takenBy;
    }
    public Bitmap getThumbnail() {
        return thumbnail;
    }
    public String getFilename() { return filename; }

    public String GenerateBitmapForUri(Context c, ContentResolver res) {
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
