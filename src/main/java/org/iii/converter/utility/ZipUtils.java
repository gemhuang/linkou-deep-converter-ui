package org.iii.converter.utility;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class ZipUtils {

    public static void extractAll(File zip, String target) throws ZipException {
        ZipFile zipFile = new ZipFile(zip);
        zipFile.extractAll(target);
    }
}
