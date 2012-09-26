/* IomJavaDocs.java - created on 30 de Mar de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.documentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 30 de Mar de 2012
 */
public class ObjectModelJavaDocs {
    private HashMap<String, ArrayList<ClassDoc>> classesByGroup = new HashMap<String, ArrayList<ClassDoc>>();
    private ArrayList<ClassDoc> allClasses = new ArrayList<ClassDoc>();
    private ArrayList<ClassDoc> qualifiers = new ArrayList<ClassDoc>();
    private HashSet<ClassDoc> superClasses = new HashSet<ClassDoc>();
    private HashSet<String> allClasseNames = new HashSet<String>();
    
    
    
    /**
     * Creates a new instance of this class.
     * @param root
     */
    @SuppressWarnings("rawtypes")
    public ObjectModelJavaDocs(RootDoc root) {
        Set<Class<? extends Enum>> allSupportedQualifiers = ObjectModelRegistry.getAllSupportedQualifiers();
        
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            String className = classDoc.qualifiedName();
            allClasseNames.add(classDoc.name());
            if (className.contains(".qualifier.")) {
                try {
                    if(!isSupportedClass(className))
                        qualifiers.add(classDoc);
                    else {
                        ArrayList<ClassDoc> clsDocs = classesByGroup.get("");
                        if (clsDocs == null) {
                            clsDocs = new ArrayList<ClassDoc>();
                            classesByGroup.put("", clsDocs);
                        }
                        clsDocs.add(classDoc);
                        allClasses.add(classDoc);
                        if(allSupportedQualifiers.contains(Class.forName(className)))
                            qualifiers.add(classDoc);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            } else {
                String group = className.substring(0, className.lastIndexOf('.'));
                group = group.substring(group.lastIndexOf('.') + 1);
                if (group.equals("common") || group.equals("tel")) group = "";
                ArrayList<ClassDoc> clsDocs = classesByGroup.get(group);
                if (clsDocs == null) {
                    clsDocs = new ArrayList<ClassDoc>();
                    classesByGroup.put(group, clsDocs);
                }
                clsDocs.add(classDoc);
                allClasses.add(classDoc);

                superClasses.add(classDoc.superclass());
            }
        }

        for (String group : new String[] { "", "party", "time", "spatial", "subject" }) {
            Collections.sort(classesByGroup.get(group), new Comparator<ClassDoc>() {
                @Override
                public int compare(ClassDoc o1, ClassDoc o2) {
                    return o1.name().compareTo(o2.name());
                }
            });
        }
        Collections.sort(allClasses, new Comparator<ClassDoc>() {
            @Override
            public int compare(ClassDoc o1, ClassDoc o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        Collections.sort(qualifiers, new Comparator<ClassDoc>() {
            @Override
            public int compare(ClassDoc o1, ClassDoc o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        
     // System.out.println(qualifiers);
     // System.out.println(classesByGroup);

    }

    /**
     * Returns the classesByGroup.
     * @return the classesByGroup
     */
    public HashMap<String, ArrayList<ClassDoc>> getClassesByGroup() {
        return classesByGroup;
    }

    /**
     * Returns the qualifiers.
     * @return the qualifiers
     */
    public ArrayList<ClassDoc> getQualifiers() {
        return qualifiers;
    }

    /**
     * Returns the superClasses.
     * @return the superClasses
     */
    public HashSet<ClassDoc> getSuperClasses() {
        return superClasses;
    }

    /**
     * Returns the allClasseNames.
     * @return the allClasseNames
     */
    public HashSet<String> getAllClasseNames() {
        return allClasseNames;
    }
    
    /**
     * @param qualifiedClassName
     * @return true if a class is in the Object model registry
     */
    public boolean isSupportedClass(String qualifiedClassName) {
        try {
            return ObjectModelRegistry.lookup(Class.forName(qualifiedClassName)) != null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @return all classes' javadoc 
     */
    public ArrayList<ClassDoc> getAllClasses() {
        return allClasses;
    }
    
    
}
