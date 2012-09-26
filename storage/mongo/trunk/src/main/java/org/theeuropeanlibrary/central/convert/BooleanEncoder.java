/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for Boolean
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class BooleanEncoder implements BaseTypeEncoder<Boolean> {

	@Override
	public Boolean decode(CodedInputStream input) throws Exception {
		return input.readBool();
	}

	@Override
	public void encode(int fieldId, Boolean value, CodedOutputStream output)
			throws Exception {
		output.writeBool(fieldId, value);
	}


}