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

package eu.europeana.normalizer.plugin;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Threaded normalizer.
 *
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */
public class EuropeanaNormalizerImpl implements EuropeanaNormalizer {

    private static final Logger LOG = Logger.getLogger(EuropeanaNormalizerImpl.class.getName());
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void normalize(InputStream original, InputStream mapping, OutputStream output) {
        executorService.execute(new NormalizerProcessor(original, mapping, output));
    }

    private class NormalizerProcessor implements Runnable {

        private InputStream original;
        private InputStream mapping;
        private OutputStream output;
        private MappingEngine mappingEngine;

        private NormalizerProcessor(InputStream original, InputStream mapping, OutputStream output) {
            this(original, mapping, output, EuropeanaNormalizer.MappingEngine.GROOVY);
        }

        private NormalizerProcessor(InputStream original, InputStream mapping, OutputStream output, MappingEngine mappingEngine) {
            this.original = original;
            this.mapping = mapping;
            this.output = output;
            this.mappingEngine = mappingEngine;
        }

        @Override
        public void run() {
            // todo: read from original, do mapping, write to output.
        }
    }
}
