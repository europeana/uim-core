/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>Float</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class FloatEncoder implements BaseTypeEncoder<Float> {

	@Override
	public Float decode(CodedInputStream input) throws Exception {
		return input.readFloat();
	}

	@Override
	public void encode(int fieldId, Float value, CodedOutputStream output)
			throws Exception {
		output.writeFloat(fieldId, value);
	}


}