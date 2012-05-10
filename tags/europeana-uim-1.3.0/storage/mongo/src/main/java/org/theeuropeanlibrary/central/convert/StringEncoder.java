/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>String</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class StringEncoder implements BaseTypeEncoder<String> {

	@Override
	public String decode(CodedInputStream input) throws Exception {
		return input.readString();
	}

	@Override
	public void encode(int fieldId, String value, CodedOutputStream output)
			throws Exception {
		output.writeString(fieldId, value);
	}


}