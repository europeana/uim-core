/* ListBytesConverter.java - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.repository.convert;

import gnu.trove.TLongHashSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * Converts a List to a byte array and back
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 10, 2011
 */
public class TLongHashSetBytesConverter extends Converter<byte[], TLongHashSet> {
    /**
     * Single convenience instance of a string type converter
     */
    public static final Converter<byte[], TLongHashSet> INSTANCE = new TLongHashSetBytesConverter();

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
