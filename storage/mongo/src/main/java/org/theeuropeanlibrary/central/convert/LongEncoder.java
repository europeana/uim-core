/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>Field</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class LongEncoder implements BaseTypeEncoder<Long> {

	@Override
	public Long decode(CodedInputStream input) throws Exception {
		return input.readFixed64();
	}

	@Override
	public void encode(int fieldId, Long value, CodedOutputStream output)
			throws Exception {
		output.writeFixed64(fieldId, value);
	}


}