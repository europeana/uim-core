/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>Integer</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class ShortEncoder implements BaseTypeEncoder<Short> {

	@Override
	public Short decode(CodedInputStream input) throws Exception {
		byte[] bytes = input.readBytes().toByteArray();
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(bytes[0]);
		bb.put(bytes[1]);
		return bb.getShort(0);
	}

	@Override
	public void encode(int fieldId, Short value, CodedOutputStream output)
			throws Exception {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort(value.shortValue());
		output.writeBytes(fieldId, ByteString.copyFrom(bb.array()));
	}


}