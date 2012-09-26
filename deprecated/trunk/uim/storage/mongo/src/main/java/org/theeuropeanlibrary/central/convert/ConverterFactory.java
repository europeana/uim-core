/* ConverterFactory.java - created on 4 de Nov de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.util.Set;

import org.theeuropeanlibrary.repository.convert.Converter;

import eu.europeana.uim.common.TKey;

/**
 * A factory that manages the implementations of converters for a set of classes
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 4 de Nov de 2011
 */
public interface ConverterFactory {
	
	/**
	 * Get all classes for which the ConverterFactory has a converter 
	 * 
	 * @return classes supported
	 */
	public Set<Class<?>> getSupportedClasses();
	
	/**
	 * Get all classes for which the ConverterFactory has a base type encoder 
	 * 
	 * @return classes supported
	 */
	public Set<Class<?>> getSupportedBaseTypeEncoders();

    /**
     * Gets a Converter for a given Class
     * 
     * @param <T>
     * @param convertedClass
     * @return Converter
     */
    public <T> Converter<byte[], T> getConverter(Class<T> convertedClass) ;

    /**
     * Gets a Converter for a given TKey
     * 
     * @param <NS>
     * @param <T>
     * @param tKey
     * @return Converter
     */
    public <NS, T> Converter<byte[], T> getConverter(TKey<NS, T> tKey) ;
    

    /**
     * Gets a BaseTypeEncoder for a given Class
     * 
     * @param <T>
     * @param encodedClass
     * @return encoder
     */
    public <T> BaseTypeEncoder<T> getBaseTypeEncoder(Class<T> encodedClass) ;
}