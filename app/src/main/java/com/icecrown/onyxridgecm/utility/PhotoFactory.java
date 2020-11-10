package com.icecrown.onyxridgecm.utility;

import java.io.File;
import java.io.IOException;

public class PhotoFactory {

    public static File GenerateImageFile() {
        File localFile = null;
        try {
            localFile = File.createTempFile("images", ".jpg");
            return localFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
