/* FileUtils.java - created on Apr 30, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility functions for file manipulations.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Apr 30, 2012
 */
public class FileUtils {
    /**
     * @param file
     * @param lines
     * @return the tailing n lines (or all if file has less lines)
     */
    public static List<String> tail(File file, int lines) {
        LinkedList<String> result = new LinkedList<String>();
        java.io.RandomAccessFile fileHandler = null;

        try {
            fileHandler = new java.io.RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;

            StringBuilder sb = new StringBuilder();
            int line = 0;

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA || readByte == 0xD) {
                    // line feed || carriege return
                    String l = sb.reverse().toString();
                    result.addFirst(l);

                    // reset buffer
                    sb.setLength(0);

                    line = line + 1;

                    if (line == lines) {
                        if (filePointer == fileLength) {
                            continue;
                        } else {
                            break;
                        }
                    }

                    readByte = fileHandler.readByte();
                    if (readByte == 0xA || readByte == 0xD) {
                        // skip \r\n \r\r \n\r \n\n
                        continue;
                    }
                } else {
                    sb.append((char)readByte);
                }
            }

            if (result.size() < lines) {
                // handle first line - we do not have
                // a newline here.
                String l = sb.reverse().toString();
                result.addFirst(l);
            }

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (fileHandler != null) {
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
