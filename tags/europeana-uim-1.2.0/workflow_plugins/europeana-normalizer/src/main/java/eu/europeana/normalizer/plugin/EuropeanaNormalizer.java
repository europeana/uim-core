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

/**
 * todo: add class description
 *
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */
public interface EuropeanaNormalizer {

    /**
     * These are the supported mapping engines. A mapping engine is used during the
     * normalization stage.
     */
    enum MappingEngine {
        GROOVY,
        XSLT,
        JAVA
    }

    /**
     * Normalize the original metadata stream with the provided mapping stream.
     *
     * @param original The original metadata.
     * @param mapping  The mapping.
     * @param output   The normalized output.
     */
    void normalize(InputStream original, InputStream mapping, OutputStream output);
}
