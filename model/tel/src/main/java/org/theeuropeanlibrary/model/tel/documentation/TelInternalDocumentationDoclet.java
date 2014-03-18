/* TelInternalDocumentationDoclet.java - created on 23 de Fev de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.documentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

import eu.europeana.uim.common.TKey;

/**
 * Generates the documentation for the Internal Object Model from the java doc comments
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 23 de Fev de 2012
 */
@SuppressWarnings("restriction")
public class TelInternalDocumentationDoclet {
    private static final HashMap<String, String> TEXT_SECTIONS        = new HashMap<String, String>();

    private static final HashMap<String, String> PACKAGE_DESCRIPTIONS = new HashMap<String, String>() {
                                                                          {
                                                                              put("",
                                                                                      "General classes");
                                                                              put("party",
                                                                                      "Party classes (Organizations, Persons, etc.)");
                                                                              put("time",
                                                                                      "Temporal classes");
                                                                              put("spatial",
                                                                                      "Spatial classes");
                                                                              put("subject",
                                                                                      "Subject classes");
                                                                          }
                                                                      };

    static {
        try {
            InputStream textStream = TelInternalDocumentationDoclet.class.getClassLoader().getResourceAsStream(
                    "org/theeuropeanlibrary/model/tel/documentation/text_content.txt");
            BufferedReader textReader = new BufferedReader(new InputStreamReader(textStream));
            String currentTitle = "";
            String currentText = "";
            for (String line = textReader.readLine(); line != null; line = textReader.readLine()) {
                if (line.startsWith("=")) {// Section title
                    if (!currentTitle.isEmpty()) {
                        TEXT_SECTIONS.put(currentTitle, currentText);
                    }
                    currentTitle = line.substring(1);
                    currentText = "";
                } else {// text
                    currentText += line;
                }
            }
            TEXT_SECTIONS.put(currentTitle, currentText);
            textReader.close();
        } catch (IOException e) {
            throw new RuntimeException("could not read text_content.txt", e);
        }
    }

    /**
     * @param root
     * @return success
     */
    public static boolean start(RootDoc root) {
        ObjectModelJavaDocs iomDocs = new ObjectModelJavaDocs(root);

        DomBuilder html = new DomBuilder("html");
        html.addElement("head");
        html.addElementBellow("title", "TEL Internal Object Model");
        html.goToParent();
        html.addElement("body");
        html.addElement("center");
        html.addElementBellow("h1", "TEL Internal Object Model");
        Date now = new Date();
        html.addTextNode("created " + new SimpleDateFormat("yyyy-MM-dd").format(now));
        html.goToParent();

        createIntroductoryText(html);

        html.addEmptyElementBellow("hr");
        createIndex(html, iomDocs);
        html.addEmptyElementBellow("hr");

        for (String group : new String[] { "", "party", "time", "spatial", "subject" }) {
            html.addElementBellow("h2", PACKAGE_DESCRIPTIONS.get(group));
            for (ClassDoc cd : iomDocs.getClassesByGroup().get(group)) {
                if (isClassToDocument(cd, iomDocs.getSuperClasses())) {
                    if (cd.qualifiedName().contains(".qualifier."))
                        qualifierToDom(html, cd, true);
                    else
                        classToDom(html, cd, iomDocs.getAllClasseNames());
                    html.addEmptyElementBellow("br");
                }
            }
            html.addEmptyElementBellow("br");
        }

        html.addElementBellow("h2", "Qualifiers");
        for (ClassDoc cd : iomDocs.getQualifiers()) {
            if (!iomDocs.isSupportedClass(cd.qualifiedName())) {
                qualifierToDom(html, cd, false);
                html.addEmptyElementBellow("br");
            }
        }

        try {
            // print html
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
// trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(html.getDom());
            trans.transform(source, result);
            String xmlString = sw.toString();

            System.out.println(xmlString);

            FileUtils.write(new File("target/ObjModelDoc.html"), xmlString);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * @param html
     */
    private static void createIntroductoryText(DomBuilder html) {
        for (String section : new String[] { "Introduction", "Why Internal?",
                "Function and Purpose", "Supported Data Formats", "Known Limitations",
                "About the Model's Structure" }) {
            html.addElementBellow("h2", section);
            for (String paragraph : TEXT_SECTIONS.get(section).split("\n")) {
                html.addElementBellow("p", paragraph);
            }
        }
    }

    private static void createIndex(DomBuilder html, ObjectModelJavaDocs iomDocs) {
        html.addElementBellow("h2", "Data Elements' Index");
        for (String group : new String[] { "", "party", "time", "spatial", "subject" }) {
            html.addElementBellow("h3", PACKAGE_DESCRIPTIONS.get(group));
            html.addElement("ul");
            for (ClassDoc cd : iomDocs.getClassesByGroup().get(group)) {
                if (isClassToDocument(cd, iomDocs.getSuperClasses())) {
                    html.addElement("li");
                    addLinkToClass(html, cd.name());
                    html.goToParent();
                }
            }
            html.goToParent();
        }

        html.addElementBellow("h3", "Qualifiers");
        html.addElement("ul");
        for (ClassDoc cd : iomDocs.getQualifiers()) {
            html.addElement("li");
            addLinkToClass(html, cd.name());
            html.goToParent();
        }
        html.goToParent();
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

    private static void classToDom(DomBuilder dom, ClassDoc cls, HashSet<String> allClasseNames) {
        TKey<ObjectModelRegistry, ?> tkey;
        try {
            tkey = ObjectModelRegistry.lookup(Class.forName(cls.qualifiedName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (cls.isAbstract()) {
            dom.addElement("table");
            dom.setAttribute("border", 1);
            dom.addElement("tr");
            dom.addElement("td");
            dom.setAttribute("colspan", 2);
            dom.addElement("a");
            dom.setAttribute("name", cls.name());
            dom.addElementBellow("b", cls.name() + "(abstract class)");
            dom.goToParent();
            dom.goToParent();
            dom.goToParent();

            dom.addElement("tr");
            dom.addElementBellow("td", "Description");
            dom.addElementBellow("td", cls.commentText());
            dom.goToParent();

            dom.goToParent();
            return;
        }

        dom.addElement("table");
        dom.setAttribute("border", 1);
        dom.addElement("tr");
        dom.addElement("td");
        dom.setAttribute("colspan", 2);
        dom.addElement("a");
        dom.setAttribute("name", cls.name());
        dom.addElementBellow("b", cls.name());
        dom.goToParent();
        dom.goToParent();
        dom.goToParent();

        boolean hasSuperClass = cls.superclass() != null &&
                                !cls.superclass().name().equals("Object") &&
                                !cls.superclass().name().equals("Enum");
        if (hasSuperClass) {
            dom.addElement("tr");
            dom.addElementBellow("td", "Superclass");
            dom.addElement("td");
            addLinkToClass(dom, cls.superclass().name());
            dom.goToParent();
            dom.goToParent();
        }

        dom.addElement("tr");
        dom.addElementBellow("td", "Description");
        dom.addElementBellow("td", cls.commentText());
        dom.goToParent();

        // fields
        dom.addElement("tr");
        dom.addElement("td");
        dom.setAttribute("valign", "top");
        dom.setText("Fields");
        dom.goToParent();
        dom.addElement("td");

        addClassFields(dom, cls, hasSuperClass, allClasseNames);

        dom.goToParent();
        dom.goToParent();

        // allowed qualifiers
        List<Class<? extends Enum<?>>> validEnums = ObjectModelRegistry.getValidEnums(tkey);
        dom.addElement("tr");
        dom.addElement("td");
        dom.setAttribute("valign", "top");
        dom.setText("Qualifiers");
        dom.goToParent();
        dom.addElement("td");
        if (validEnums.size() == 0) {
            dom.setText("None");
        } else {
            for (Class<? extends Enum<?>> q : validEnums) {
                addLinkToClass(dom, q.getSimpleName());
                dom.addEmptyElementBellow("br");
            }
        }
        dom.goToParent();
        dom.goToParent();

        dom.goToParent();// table
    }

    /**
     * @param dom
     * @param cls
     * @param hasSuperClass
     * @param allClasseNames
     */
    private static void addClassFields(DomBuilder dom, ClassDoc cls, boolean hasSuperClass,
            HashSet<String> allClasseNames) {
        dom.addElement("table");
        dom.setAttribute("border", 1);
        dom.addElement("tr");
        dom.addElement("td");
        dom.addElementBellow("b", "Name");
        dom.goToParent();
        dom.addElement("td");
        dom.addElementBellow("b", "Type");
        dom.goToParent();
        dom.addElement("td");
        dom.addElementBellow("b", "Description");
        dom.goToParent();
        dom.goToParent();

        ArrayList<FieldDoc> allFields = getAllFieldDocs(cls, hasSuperClass);

        for (FieldDoc fd : allFields) {
            dom.addElement("tr");
            dom.addElementBellow("td", fd.name());
            if (allClasseNames.contains(fd.type().simpleTypeName())) {
                dom.addElement("td");
                addLinkToClass(dom, fd.type().simpleTypeName());
                dom.goToParent();
            } else
                dom.addElementBellow("td", fd.type().simpleTypeName());

// if(fd.type().simpleTypeName().equals("List")) {
// System.out.println(
// fd.type().asParameterizedType().typeArguments().length
// );
// // System.out.println(fd.type().asParameterizedType().dimension());
// // System.out.println(fd.type().asParameterizedType().qualifiedTypeName());
// // System.out.println(fd.type().asAnnotationTypeDoc());
// // System.out.println(fd.type().asParameterizedType().containingType());
// System.out.println(fd.type().asParameterizedType().interfaceTypes());
// System.out.println(fd.type().asParameterizedType().interfaceTypes()[0]);
// System.out.println(fd.type().asParameterizedType().interfaceTypes()[0].asParameterizedType().typeArguments().length);
// System.out.println(fd.serialFieldTags());
//
// // System.out.println(fd.type().asParameterizedType().superclassType());
// // System.out.println(fd.type().asParameterizedType().typeArguments());
// // System.out.println(fd.type().asTypeVariable());
// // System.out.println(fd.type().asWildcardType());
// System.out.println();
//
// }

            dom.addElementBellow("td", fd.commentText());
            dom.goToParent();
        }
        dom.goToParent();
    }

    /**
     * @param cls
     * @param hasSuperClass
     * @return all fields of a class
     */
    protected static ArrayList<FieldDoc> getAllFieldDocs(ClassDoc cls, boolean hasSuperClass) {
        ArrayList<FieldDoc> allFields = new ArrayList<FieldDoc>();
        if (hasSuperClass) {
            for (FieldDoc fd : cls.superclass().fields()) {
                if (fd.isStatic()) continue;
                allFields.add(fd);
            }
        }
        for (FieldDoc fd : cls.fields()) {
            if (fd.isStatic()) continue;
            allFields.add(fd);
        }
        Collections.sort(allFields, new Comparator<FieldDoc>() {
            @Override
            public int compare(FieldDoc o1, FieldDoc o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        return allFields;
    }

    /**
     * @param dom
     * @param simpleTypeName
     */
    private static void addLinkToClass(DomBuilder dom, String simpleTypeName) {
        dom.addElement("a");
        dom.setAttribute("href", "#" + simpleTypeName);
        dom.setText(simpleTypeName);
        dom.goToParent();
    }

    private static void qualifierToDom(DomBuilder dom, ClassDoc cls, boolean asClass) {
        dom.addElement("table");
        dom.setAttribute("border", 1);
        dom.addElement("tr");
        dom.addElement("td");
        dom.setAttribute("colspan", 2);

        dom.addElement("a");
        dom.setAttribute("name", cls.name());
        dom.addElementBellow("b", cls.name());
// if(asClass)
        dom.addTextNode(" (controled values)");
        dom.goToParent();

        dom.goToParent();
        dom.goToParent();

        dom.addElement("tr");
        dom.addElementBellow("td", "Description");
        dom.addElementBellow("td", cls.commentText());
        dom.goToParent();
        dom.addElement("tr");
        dom.addElement("td");
        dom.setAttribute("colspan", 2);
        dom.addElementBellow("b", "Values");
        dom.goToParent();
        dom.goToParent();

        if (cls.fields().length > 25) {
            dom.addElement("tr");
            dom.addElement("td");
            dom.setAttribute("colspan", 2);
            int col = 0;
            String text = "";
            for (FieldDoc fd : cls.fields()) {
                if (!fd.isStatic() || !fd.type().simpleTypeName().equals(cls.simpleTypeName()))
                    continue;
                if (col > 0) text += "; ";
                text += fd.name();
                if (!fd.commentText().isEmpty()) text += " (" + fd.commentText() + ")";
                col++;
            }
            dom.setText(text);
            dom.goToParent();
            dom.goToParent();
        } else {
            for (FieldDoc fd : cls.fields()) {
                if (!fd.isStatic() || !fd.type().simpleTypeName().equals(cls.simpleTypeName()))
                    continue;
                dom.addElement("tr");
                dom.addElementBellow("td", fd.name());
                dom.addElementBellow("td", fd.commentText());
                dom.goToParent();
            }
        }

        if (asClass) {
            // allowed qualifiers
            TKey<ObjectModelRegistry, ?> tkey;
            try {
                tkey = ObjectModelRegistry.lookup(Class.forName(cls.qualifiedName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            List<Class<? extends Enum<?>>> validEnums = ObjectModelRegistry.getValidEnums(tkey);
            dom.addElement("tr");
            dom.addElement("td");
            dom.setAttribute("colspan", 2);
            dom.addElementBellow("b", "Qualifiers");
            dom.goToParent();
            dom.goToParent();

            dom.addElement("tr");
            dom.addElement("td");
            dom.setAttribute("colspan", 2);
            if (validEnums.size() == 0) {
                dom.setText("None");
            } else {
                for (Class<? extends Enum<?>> q : validEnums) {
                    addLinkToClass(dom, q.getSimpleName());
                    dom.addEmptyElementBellow("br");
                }
            }
            dom.goToParent();
            dom.goToParent();
        }
        dom.goToParent();// table
    }
}