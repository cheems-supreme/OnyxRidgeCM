//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: PhotoFactory.java
//
// Written by: Raymond O'Neill
//
// Date added: 11/10/2020
//
// Detail: Used to handle generation of photo-related content
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Added comment header
//      - Changed File.createTempFile(...) to include the context's
//        getFilesDir directory to work with FileProvider
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Refactored method names to lower camel case
//      - Reformatted comment header
//*******************************************************************
package com.icecrown.onyxridgecm.utility;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class PhotoFactory {

    public static File generateImageFile(Context context) {
        File localFile = null;
        try {
            localFile = File.createTempFile("images", ".jpg", context.getFilesDir());
            return localFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
