/* GenerateTelInternalDocumentationScript.java - created on 23 de Fev de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.documentation;

/**
 * Script to execute the geeration of the doccumentations
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 23 de Fev de 2012
 */
public class Main {
    /**
     * @param args
     */
    @SuppressWarnings("restriction")
    public static void main(String[] args) {
        String programName = "GenerateTelInternalDocumentationScript";
        String defaultDocletClassName = TelInternalDocumentationDoclet.class.getCanonicalName();
        String[] javaDocParams = new String[] { "-sourcepath",
                "src/main/java;../common/src/main/java", "-private",
                "org.theeuropeanlibrary.model.tel", "org.theeuropeanlibrary.model.tel.qualifier",
                "org.theeuropeanlibrary.model.common", "org.theeuropeanlibrary.model.common.party",
                "org.theeuropeanlibrary.model.common.qualifier",
                "org.theeuropeanlibrary.model.common.spatial",
                "org.theeuropeanlibrary.model.common.subject",
                "org.theeuropeanlibrary.model.common.time" };
        com.sun.tools.javadoc.Main.execute(programName, defaultDocletClassName, javaDocParams);
    }
}
