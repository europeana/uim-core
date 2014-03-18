/* ProxyFieldConverter.java - created on 6 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import org.theeuropeanlibrary.repository.convert.Converter;

/**
 * A proxy for a converter. This is needed in order to specify converters which are recursive during
 * initialization
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @param <T>
 *            converted type
 * @date 6 de Jul de 2011
 */
public class ProxyConverter<T> extends Converter<byte[], T> {
    Converter<byte[], T> actualConverter;

    @Override
    public Class<byte[]> getEncodeType() {
        return actualConverter.getEncodeType();
    }

    @Override
    public Class<T> getDecodeType() {
        return actualConverter.getDecodeType();
    }

    @Override
    public byte[] encode(T data) {
        return actualConverter.encode(data);
    }

    @Override
    public T decode(byte[] data) {
        return actualConverter.decode(data);
    }

    /**
     * @param actualConverter
     *            the proxied converter
     */
    public void setSerializer(Converter<byte[], T> actualConverter) {
        this.actualConverter = actualConverter;
    }
}