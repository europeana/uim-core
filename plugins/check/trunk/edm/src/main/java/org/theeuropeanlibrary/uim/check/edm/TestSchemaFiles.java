/* TestSchemaFiles.java - created on 28 de Jun de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.edm;

import java.io.File;

import javax.xml.validation.SchemaFactory;

/**
 * A script just for testing the EDM xsd files for compatibility problems with java
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 28 de Jun de 2013
 */
public class TestSchemaFiles {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            try {
                File edmXsdFile = new File(
                        "C:\\Documents\\EclipseWorkspaces\\TEL\\tel-uim-plugin-check-edm\\src\\test\\resources\\xsd\\EDM_tel.xsd");
                factory.newSchema(edmXsdFile);
                System.err.println("XSD OK");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

// for (File edmXsdFile: new
// File("C:\\Documents\\EclipseWorkspaces\\TEL\\tel-uim-plugin-check-edm\\src\\test\\resources\\xsd").listFiles())
// {
// System.err.println(edmXsdFile.getName());
// try {
// SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
// factory.newSchema(edmXsdFile);
// } catch (Exception e) {
// // e.printStackTrace();
// System.err.println(e.getMessage());
// }
// }

    }
}
