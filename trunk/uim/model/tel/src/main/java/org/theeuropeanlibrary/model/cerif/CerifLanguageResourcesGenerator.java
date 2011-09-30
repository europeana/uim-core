/* GenerateCerifLanguageResources.java - created on 20 de Set de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.cerif;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.theeuropeanlibrary.qualifier.Language;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 20 de Set de 2011
 */
public class CerifLanguageResourcesGenerator {
    CerifOntology                                         cerif        = new CerifOntology();
    Map<Language, Map<CerifConcept, LabelAndDescription>> translations = new HashMap<Language, Map<CerifConcept, LabelAndDescription>>();

    /**
     * @param mappingCsvAsStream
     * @throws IOException
     */
    public void readTranslationsFromCsv(InputStream mappingCsvAsStream) throws IOException {
        ArrayList<Language> languageColumns = new ArrayList<Language>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(mappingCsvAsStream));
        String[] lineParts = reader.readLine().split("\t");
        for (int i = 1; i < lineParts.length; i += 2) {
            if (lineParts[i].trim().isEmpty()) break;
            int codeStartIdx = lineParts[i].indexOf('(');
            Language l = Language.lookupLanguage(lineParts[i].substring(codeStartIdx + 1,
                    codeStartIdx + 3));
            languageColumns.add(l);
        }
        reader.readLine();// Ignore line two
        int lineNumber = 2;
        for (String inputLine = reader.readLine(); inputLine != null; inputLine = reader.readLine()) {
            System.out.println(inputLine);
            lineNumber++;
            lineParts = inputLine.split("\t");
            if (lineParts.length < 3)
                throw new IllegalArgumentException("CERIF ontology file - invalid line " +
                                                   lineNumber + ": \"" + inputLine + "\"");
            CerifConcept concept = cerif.getConcept(lineParts[0].trim());
            if (concept == null)
                throw new IllegalArgumentException("CERIF ontology file - invalid line " +
                                                   lineNumber + ": \"" + inputLine + "\"");
            for (int i = 0; i < languageColumns.size(); i++) {
                int csvIdx = 1 + i * 2;
                LabelAndDescription ld = new LabelAndDescription();
                ld.label = lineParts[csvIdx].trim();
                if (csvIdx + 1 < lineParts.length)
                    ld.description = lineParts[csvIdx + 1].trim();
                else
                    ld.description = "";

                if (ld.label.isEmpty()) continue;

                Map<CerifConcept, LabelAndDescription> languageTranslations = translations.get(languageColumns.get(i));
                if (languageTranslations == null) {
                    languageTranslations = new HashMap<CerifConcept, CerifLanguageResourcesGenerator.LabelAndDescription>();
                    translations.put(languageColumns.get(i), languageTranslations);
                }
                languageTranslations.put(concept, ld);

            }
        }
        mappingCsvAsStream.close();
    }

    /**
     * @param outFolder
     * @param filenamePrefix
     * @throws IOException
     */
    public void writeLanguageFiles(File outFolder, String filenamePrefix) throws IOException {
        for (Language lang : translations.keySet()) {
            Map<CerifConcept, LabelAndDescription> langTranslations = translations.get(lang);
            File outFile = new File(outFolder, filenamePrefix + "_" + lang.getIso2() +
                                               ".properties");
            BufferedWriter writer = new BufferedWriter(new FileWriterWithEncoding(outFile, "UTF-8"));
            for (CerifConcept topConcept : cerif.getTopConcepts()) {
                String labelProperty = String.format("cerif.ortelius.%s.label=%s\n",
                        topConcept.getCode(), langTranslations.get(topConcept).label);
                String descProperty = String.format("cerif.ortelius.%s.description=%s\n",
                        topConcept.getCode(), langTranslations.get(topConcept).description);
                writer.write(labelProperty);
                writer.write(descProperty);

                for (CerifConcept subConcept : topConcept.getNarrower()) {
                    labelProperty = String.format("cerif.ortelius.%s.label=%s\n",
                            subConcept.getCode(), langTranslations.get(subConcept).label);
                    descProperty = String.format("cerif.ortelius.%s.description=%s\n",
                            subConcept.getCode(), langTranslations.get(subConcept).description);
                    writer.write(labelProperty);
                    writer.write(descProperty);
                }
            }
            writer.close();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CerifLanguageResourcesGenerator generator = new CerifLanguageResourcesGenerator();
        generator.readTranslationsFromCsv(new FileInputStream(
                "src/test/resources/org/theeuropeanlibrary/model/cerif/cerif_translations.txt"));
        File outFolder = new File("target/generated-cerif-language-resources");
        if (!outFolder.exists()) outFolder.mkdir();
        generator.writeLanguageFiles(outFolder, "cerif_ortelius");
    }

    class LabelAndDescription {
        String label;
        String description;
    }

}
