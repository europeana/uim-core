/* FileUploadService.java - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public class FileUploadService extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        ServletFileUpload upload = new ServletFileUpload();
//
//        try {
//            FileItemIterator iter = upload.getItemIterator(request);
//
//            while (iter.hasNext()) {
//                FileItemStream item = iter.next();
//
//                String name = item.getFieldName();
//                InputStream stream = item.openStream();
//
//                // Process the input stream
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                int len;
//                byte[] buffer = new byte[8192];
//                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
//                    out.write(buffer, 0, len);
//                }
//
//                int maxFileSize = 10 * (1024 * 2); // 10 megs max
//                if (out.size() > maxFileSize) {
//                    System.out.println("File is > than " + maxFileSize);
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
