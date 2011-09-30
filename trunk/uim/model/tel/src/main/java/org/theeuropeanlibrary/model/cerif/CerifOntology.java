/* CerifOntology.java - created on 14 de Set de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.cerif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * In memory representation of the CERIF subject scheme TODO: in future work, this class concept
 * should support: related concepts and preferred concepts
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 14 de Set de 2011
 */
public class CerifOntology {
    /**
     * The top CERIF concepts
     */
    private LinkedHashMap<String, CerifConcept> topConcepts;

    /**
     * An index to efficiently retrieve a concept by its code
     */
    private HashMap<String, CerifConcept>       codeIndex;

    /**
     * Loads CERIF from a stream (in csv format)
     * 
     * @param cerifAsCsvStream
     */
    public CerifOntology(InputStream cerifAsCsvStream) {
        try {
            readMappingFromCsv(cerifAsCsvStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading stream", e);
        }
    }

    /**
     * Loads CERIF from the csv read from the classpth
     */
    public CerifOntology() {
        try {
            InputStream cerifAsCsvStream = CerifOntology.class.getResourceAsStream("/org/theeuropeanlibrary/model/cerif/cerif.ontology.csv");
            readMappingFromCsv(cerifAsCsvStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading stream", e);
        }
    }

    /**
     * Loads CERIF from a stream (in csv format)
     * 
     * @param mappingCsvAsStream
     * @throws IOException
     */
    private void readMappingFromCsv(InputStream mappingCsvAsStream) throws IOException {
        Pattern cleanValuePattern = Pattern.compile("\"?\\s*(\\S.*[^\\s\"]|\\S)\\s*\"?");
        topConcepts = new LinkedHashMap<String, CerifConcept>();
        codeIndex = new HashMap<String, CerifConcept>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(mappingCsvAsStream));
        String[] lineParts = reader.readLine().split(";");
        if (!cleanValuePattern.matcher(lineParts[0]).replaceFirst("$1").equals("CERIF"))
            throw new IllegalArgumentException(
                    "CERIF ontology file does not start with \"CERIF\" and start with \"" +
                            lineParts[0] + "\"");
        Stack<CerifConcept> parents = new Stack<CerifConcept>();
        int currentLevel = 0;
        for (String mappingLine = reader.readLine(); mappingLine != null; mappingLine = reader.readLine()) {
            lineParts = mappingLine.split(";");
            if (lineParts.length < 3)
                throw new IllegalArgumentException("CERIF ontology file - invalid line: \"" +
                                                   mappingLine + "\"");
            String cerifConceptId = cleanValuePattern.matcher(lineParts[0]).replaceFirst("$1").trim();
            String level = cleanValuePattern.matcher(lineParts[1]).replaceFirst("$1").trim();
            String label = cleanValuePattern.matcher(lineParts[2]).replaceFirst("$1").trim();
            if (cerifConceptId.isEmpty() || level.isEmpty() || label.isEmpty())
                throw new IllegalArgumentException("CERIF ontology file - invalid line: \"" +
                                                   mappingLine + "\"");
            try {
                currentLevel = Integer.parseInt(level);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("CERIF ontology file - invalid line: \"" +
                                                   mappingLine + "\"", e);
            }

            CerifConcept lineConcepth = new CerifConcept(cerifConceptId, label, null);
            CerifConcept parentConcept = null;
            if (currentLevel > 1) {
                int parentLevel = parents.size();
                if (currentLevel == parentLevel) {
                    parents.pop();
                    parentConcept = parents.peek();
                } else if (currentLevel > parentLevel) {
                    parentConcept = parents.peek();
                } else if (currentLevel < parentLevel) {
                    while (currentLevel < parentLevel) {
                        parents.pop();
                        parentLevel = parents.size();
                    }
                    parents.pop();
                    parentConcept = parents.peek();
                }
            } else {
                topConcepts.put(cerifConceptId, lineConcepth);
                parents.clear();
            }
            parents.add(lineConcepth);
            if (parentConcept != null) {
                lineConcepth.setBroader(parentConcept);
                parentConcept.addNarrower(lineConcepth);
            }
            codeIndex.put(lineConcepth.getCode(), lineConcepth);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(400 * 30);
        for (CerifConcept c : topConcepts.values()) {
            toString(c, sb);
        }
        return sb.toString();
    }

    private void toString(CerifConcept c, StringBuffer sb) {
        int hierarchyLevel = c.getHierarchyLevel();
        for (int i = 1; i < hierarchyLevel; i++)
            sb.append("-");
        sb.append(c.toString());
        sb.append("\n");
        for (CerifConcept n : c.getNarrower()) {
            toString(n, sb);
        }
    }

    /**
     * Get a concept by its code
     * 
     * @param conceptCode
     * @return CerifConcept
     */
    public CerifConcept getConcept(String conceptCode) {
        return codeIndex.get(conceptCode);
    }

    /**
     * Get the CERIF top concepts
     * 
     * @return the CERIF top concepts
     */
    public Collection<CerifConcept> getTopConcepts() {
        return topConcepts.values();
    }
}
