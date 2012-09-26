/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A field converter for Enum types
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class EnumEncoder implements BaseTypeEncoder<Enum<?>> {
	/**
	 * the type of enum
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends Enum> type;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param type
	 *            the type of enum
	 */
	public EnumEncoder(@SuppressWarnings("rawtypes") Class<? extends Enum> type) {
		super();
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enum<?> decode(CodedInputStream input) throws Exception {
		return Enum.valueOf(type, input.readString());
	}

	@Override
	public void encode(int fieldId, Enum<?> value, CodedOutputStream output)
			throws Exception {
		output.writeString(fieldId, value.toString());
	}


}