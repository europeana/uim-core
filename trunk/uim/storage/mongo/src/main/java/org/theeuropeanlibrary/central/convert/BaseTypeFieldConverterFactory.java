/* FieldConverterFactory.java - created on 7 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.util.Date;

import org.theeuropeanlibrary.repository.convert.Converter;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 7 de Abr de 2011
 */
public class BaseTypeFieldConverterFactory {

	/**
	 * Creates a <code>FieldConverterInterface</code> implementation for the
	 * given type
	 * 
	 * @param type
	 * @return a <code>FieldConverterInterface</code> implementation for the
	 *         given type
	 */
	@SuppressWarnings("unchecked")
	public static BaseTypeConverter newInstanceFor(Class<?> type) {
		BaseTypeConverter fldConv = null;
		fldConv = new BaseTypeConverter(newFieldEncoderFor(type));
		return fldConv;
	}

	/**
	 * @param type
	 *            type for the encoder
	 * @return an encoder
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static BaseTypeEncoder newFieldEncoderFor(Class<?> type) {
		BaseTypeEncoder encoder = null;
		if (type.equals(String.class)) {
			encoder = new StringEncoder();
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			encoder = new IntegerEncoder();
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			encoder = new LongEncoder();
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			encoder = new FloatEncoder();
		} else if (type.equals(Date.class)) {
			encoder = new DateEncoder();
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			encoder = new DoubleEncoder();
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			encoder = new BooleanEncoder();
		} else if (type.isEnum() || type.equals(Enum.class)) {
			encoder = new EnumEncoder((Class<? extends Enum>) type);
		}
		if (encoder == null)
			throw new IllegalArgumentException(
					"Class contains fields not supported for serialization (and no converter was provided): "
							+ type.getName());
		return encoder;
	}


	/**
	 * Creates a new Converter for a base type
	 * 
	 * @param type the converted base type
	 * @return converter
	 */
	@SuppressWarnings("rawtypes")
	public static Converter newConverterFor(Class type) {
		return new BaseTypeEncoderBasedConverter(
				newFieldEncoderFor(type));
	}
	
}