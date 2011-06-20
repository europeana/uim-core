/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.0 or? as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.normalizer.command;

import eu.europeana.normalizer.plugin.EuropeanaNormalizer;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.osgi.service.command.CommandSession;
import org.osgi.service.command.Function;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * The Karaf cli for the Europeana Normalizer.
 *
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */
@Command(name = "normalizer", scope = "test", description = "Initial test")
public class NormalizerConsole implements Function, Action {

    private EuropeanaNormalizer europeanaNormalizer;

    public NormalizerConsole(EuropeanaNormalizer europeanaNormalizer) {
        this.europeanaNormalizer = europeanaNormalizer;
    }

    enum Operation {
        help,
        stop,
        normalize,
        setEngine,
        status
    }

    @Option(name = "-o", aliases = {"--operation"}, required = true)
    private Operation operation;

    @Option(name = "-m", aliases = {"--mappingEngine"}, required = false)
    private String mappingEngine;

    @Option(name = "-original", aliases = {"--original"}, description = "The original metadata file")
    private String original;

    @Option(name = "-mapping", aliases = {"--mapping"}, description = "The mapping file")
    private String mapping;

    @Option(name = "-output", aliases = {"--output"}, description = "The normalized output file")
    private String output;

    @Override
    public Object execute(CommandSession commandSession, List<Object> objects) throws Exception {
        // todo: implement
        return null;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        PrintStream out = commandSession.getConsole();
        if (null == operation) {
            out.printf("Please specify an operation%n");
            return null;
        }
        switch (operation) {
            case setEngine:
                break;
            case help:
                displayHelp(out);
                break;
            case stop:
                throw new UnsupportedOperationException("Not implemented yet");
            case normalize:
                europeanaNormalizer.normalize(
                        new FileInputStream(original),
                        new FileInputStream(mapping),
                        new FileOutputStream(output)
                );
                break;
            case status:
                throw new UnsupportedOperationException("Not implemented yet");
        }
        return null;  // todo: implement
    }

    private void displayHelp(PrintStream out) {
        out.printf("mappingEngine   \t\tThe mappingEngine (Default = GROOVY)%n");
        out.printf("original        \t\tThe original metadata file%n");
        out.printf("mapping         \t\tThe mapping file%n");
        out.printf("output          \t\tThe normalized output file%n");
    }
}
