/* TelInternalDocumentationDoclet.java - created on 23 de Fev de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.documentation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.theeuropeanlibrary.model.common.qualifier.NumberingRelation;
import org.theeuropeanlibrary.model.common.qualifier.TextRelation;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import eu.europeana.uim.common.TKey;

/**
 * Generates the documentation for the Internal Object Model from the java doc comments
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 23 de Fev de 2012
 */
@SuppressWarnings("restriction")
public class RdfMappingClassesTableDoclet {
    /**
     * @param root
     * @return success
     */
    public static boolean start(RootDoc root) {
        ObjectModelJavaDocs iomDocs = new ObjectModelJavaDocs(root);
        StringBuffer csv = new StringBuffer();
// csv.append("Class;EDM-property;Non-EDM property\n");
        csv.append("Class;edm:ProvidedCHO;edm:WebResource;ore:Aggregation;EDM properties;edm:TimeSpan;edm:Place;edm:Agent;skos:Concept;EDM properties;Notes on mapping\n");
        for (ClassDoc cd : iomDocs.getAllClasses()) {
            if (isClassToDocument(cd, iomDocs.getSuperClasses())) {
                if (cd.qualifiedName().contains(".qualifier."))
                    qualifierToCsv(csv, cd);
                else
                    classToCsv(csv, cd, iomDocs.getAllClasseNames());
            }
        }

        try {
            FileUtils.write(new File("target/ObjModelClassesToRdf.csv"), csv.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return true;
    }

    private static boolean isClassToDocument(ClassDoc cls, HashSet<ClassDoc> superClasses) {
        TKey<ObjectModelRegistry, ?> tkey;
        try {
            tkey = ObjectModelRegistry.lookup(Class.forName(cls.qualifiedName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (tkey == null && !superClasses.contains(cls)) return false;
        return true;
    }

    private static void classToCsv(StringBuffer csv, ClassDoc cls, HashSet<String> allClassNames) {
        TKey<ObjectModelRegistry, ?> tkey;
        try {
            tkey = ObjectModelRegistry.lookup(Class.forName(cls.qualifiedName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (cls.isAbstract()) { return; }

        if (cls.name().equals("Numbering")) {
            ArrayList<String> quals = new ArrayList<String>();
            for (NumberingRelation rel : NumberingRelation.values())
                quals.add(rel.toString());
            Collections.sort(quals);
            for (String q : quals)
                classToCsv(csv, cls, allClassNames, tkey, cls.name() + " (" + q + ")");
        } else if (cls.name().equals("Text")) {
            ArrayList<String> quals = new ArrayList<String>();
            for (TextRelation rel : TextRelation.values())
                quals.add(rel.toString());
            Collections.sort(quals);
            for (String q : quals)
                classToCsv(csv, cls, allClassNames, tkey, cls.name() + " (" + q + ")");
        } else
            classToCsv(csv, cls, allClassNames, tkey, cls.name());
    }

    private static void classToCsv(StringBuffer csv, ClassDoc cls, HashSet<String> allClasseNames,
            TKey<ObjectModelRegistry, ?> tkey, String overideClassName) {
        csv.append(String.format("%s\n", overideClassName));

// boolean hasSuperClass = cls.superclass() != null &&
// !cls.superclass().name().equals("Object") &&
// !cls.superclass().name().equals("Enum");

// ArrayList<FieldDoc> allFields = TelInternalDocumentationDoclet.getAllFieldDocs(cls,
// hasSuperClass);
// for (FieldDoc fd : allFields) {
// csv.append(String.format("%s;property;%s\n",overideClassName, fd.name()));
// }
//
// // allowed qualifiers
// List<Class<? extends Enum<?>>> validEnums = ObjectModelRegistry.getValidEnums(tkey);
// for (Class<? extends Enum<?>> q : validEnums) {
// csv.append(String.format("%s;qualifier;%s\n",overideClassName, q.getSimpleName()));
// }
    }

    private static void qualifierToCsv(StringBuffer csv, ClassDoc cls) {
        csv.append(String.format("%s;\n", cls.name()));
    }

}