package com.github.verils.transdoc.core.util;

import java.io.*;

public abstract class FileUtils {

    public static void write(File file, byte[] data) {
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file));
            output.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // Do nothing.
                }
            }
        }
    }
}
