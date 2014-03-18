/* CombinedConverterFactory.java - created on 4 de Nov de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.theeuropeanlibrary.repository.convert.Converter;

import eu.europeana.uim.common.TKey;

/**
 * Combines several ConverterFactories into one factory
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 4 de Nov de 2011
 */
public class CombinedConverterFactory implements ConverterFactory {
	@SuppressWarnings("rawtypes")
	private final Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();

	@SuppressWarnings("rawtypes")
	private final Map<Class<?>, BaseTypeEncoder> baseTypeEncoders = new HashMap<Class<?>, BaseTypeEncoder>() ;
	
	
	/**
	 * Creates a new instance of this class. 
	 * @param converterFactories the converter factories to be combined
	 */
	public CombinedConverterFactory(ConverterFactory... converterFactories) {
		for(ConverterFactory fact: converterFactories) {
			for(Class<?> cls : fact.getSupportedClasses()) {
				if(!converters.containsKey(cls))
					converters.put(cls, fact.getConverter(cls));
			}
			for(Class<?> cls : fact.getSupportedBaseTypeEncoders()) {
				if(!baseTypeEncoders.containsKey(cls))
					baseTypeEncoders.put(cls, fact.getBaseTypeEncoder(cls));
			}
		}
	}
	
	
	
    /**
     * Gets a Converter for a given Class
     * 
     * @param <T>
     * @param convertedClass
     * @return Converter
     */
    @Override
	@SuppressWarnings("unchecked")
    public <T> Converter<byte[], T> getConverter(Class<T> convertedClass) {
        return converters.get(convertedClass);
    }

    /**
     * Gets a Converter for a given TKey
     * 
     * @param <NS>
     * @param <T>
     * @param tKey
     * @return Converter
     */
    @Override
	@SuppressWarnings("unchecked")
    public <NS, T> Converter<byte[], T> getConverter(TKey<NS, T> tKey) {
        return converters.get(tKey.getType());
    }

	@Override
	public Set<Class<?>> getSupportedClasses() {
		return converters.keySet();
	}

	@Override
	public Set<Class<?>> getSupportedBaseTypeEncoders() {
		return baseTypeEncoders.keySet();
	}


	@Override
    @SuppressWarnings("unchecked")
	public <T> BaseTypeEncoder<T> getBaseTypeEncoder(Class<T> encodedClass) {
		return baseTypeEncoders.get(encodedClass);
	}
}