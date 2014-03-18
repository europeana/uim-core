/* EditionBeanBytesConverter.java - created on 5 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A converter implementation based on a <code>BaseTypeEncoder</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 5 de Abr de 2011
 */
public class BaseTypeEncoderBasedConverter extends Converter<byte[], Object> {
    @SuppressWarnings("rawtypes")
    BaseTypeEncoder encoder;

    /**
     * Creates a new instance of this class.
     * 
     * @param encoder
     */
    public BaseTypeEncoderBasedConverter(@SuppressWarnings("rawtypes") BaseTypeEncoder encoder) {
        super();
        this.encoder = encoder;
    }

    @Override
    public Object decode(byte[] data) {
        Object bean = null;
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        CodedInputStream input = CodedInputStream.newInstance(bin);
        try {
            input.readTag();
            bean = encoder.decode(input);
        } catch (Exception e) {
            throw new RuntimeException("Could not read object from byte array! ("+encoder.getClass().getSimpleName()+", "+data.length+" bytes)", e);
        } finally {
            try {
                bin.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream!", e);
            }
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(Object data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            encoder.encode(1, data, output);
            output.flush();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not write ProviderBean '" + data + "' to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }
        return bout.toByteArray();
    }

    @Override
    public Class<Object> getDecodeType() {
        return Object.class;
    }

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }
}