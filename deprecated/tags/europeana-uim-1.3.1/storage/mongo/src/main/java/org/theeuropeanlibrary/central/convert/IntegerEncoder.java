/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>Integer</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class IntegerEncoder implements BaseTypeEncoder<Integer> {

	@Override
	public Integer decode(CodedInputStream input) throws Exception {
		return input.readFixed32();
	}

	@Override
	public void encode(int fieldId, Integer value, CodedOutputStream output)
			throws Exception {
		output.writeFixed32(fieldId, value);
	}


}