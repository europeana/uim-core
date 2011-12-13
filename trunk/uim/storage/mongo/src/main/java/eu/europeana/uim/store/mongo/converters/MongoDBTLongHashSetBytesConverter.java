/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
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
package eu.europeana.uim.store.mongo.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import gnu.trove.TLongHashSet;

import org.theeuropeanlibrary.repository.convert.Converter;


import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * Converts a List to a byte array and back
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 10, 2011
 */

@SuppressWarnings("rawtypes")
public class MongoDBTLongHashSetBytesConverter extends Converter<byte[], TLongHashSet>{

    /**
     * Single convenience instance of a string type converter
     */
    public static final Converter<byte[], TLongHashSet> INSTANCE = new MongoDBTLongHashSetBytesConverter();

    @Override
    public TLongHashSet decode(byte[] data) {
        InputStream bin = new ByteArrayInputStream(data);
        TLongHashSet result = new TLongHashSet();
        try {
            CodedInputStream input = null;
            try {
                GZIPInputStream gzip = new GZIPInputStream(bin);
                input = CodedInputStream.newInstance(gzip);
                input.setSizeLimit(Integer.MAX_VALUE);
            } catch (Throwable t) {
                input = CodedInputStream.newInstance(bin);
                input.setSizeLimit(2 * data.length);
            }
            
            while (!input.isAtEnd()) {
                long val = input.readFixed64();
                result.add(val);
            }
            bin.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not read long set from byte array!", e);
        } finally {
        }
        return result;
    }

    @Override
    public byte[] encode(TLongHashSet set) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(bout);
            CodedOutputStream output = CodedOutputStream.newInstance(gzip);
            synchronized (set) {
                long[] array = set.toArray();
                for (long o : array) {
                    output.writeFixed64NoTag(o);
                }
            }
            output.flush();
            
            gzip.flush();
            gzip.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not write long set to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }

        byte[] data = bout.toByteArray();
        return data;
    }

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<TLongHashSet> getDecodeType() {
        return TLongHashSet.class;
    }

}
