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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * todo: add class description
 *
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */
public class EuropeanaNormalizerImplTest {

    private EuropeanaNormalizerImpl europeanaNormalizer;
    private InputStream original;
    private InputStream mapping;
    private OutputStream output;

    @Before
    public void setUp() throws Exception {
        europeanaNormalizer = new EuropeanaNormalizerImpl();
    }

    @Test
    public void testNormalize() throws Exception {
        europeanaNormalizer.normalize(original, mapping, output);
    }

    @After
    public void tearDown() throws Exception {

    }
}
