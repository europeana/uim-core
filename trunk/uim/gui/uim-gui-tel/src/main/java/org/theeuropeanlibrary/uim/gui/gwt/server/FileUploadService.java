/* FileUploadService.java - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.theeuropeanlibrary.uim.gui.gwt.server.engine.Engine;

import eu.europeana.uim.api.ResourceEngine;

/**
 * Servlet used for file upload.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public class FileUploadService extends HttpServlet {
    private final Engine engine;

    /**
     * Creates a new instance of this class.
     */
    public FileUploadService() {
        this.engine = Engine.getInstance();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResourceEngine<Long> resourceEngine = (ResourceEngine<Long>)engine.getRegistry().getResourceEngine();
        ServletFileUpload upload = new ServletFileUpload();

        try {
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();

                FileOutputStream ofile = new FileOutputStream(new File(resourceEngine.getRootDirectory() + File.separator + item.getName()));
                
                // Process the input stream
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    ofile.write(buffer, 0, len);
                }
                
                ofile.close();
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
