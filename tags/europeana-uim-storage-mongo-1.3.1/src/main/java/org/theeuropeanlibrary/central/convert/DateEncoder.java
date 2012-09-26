/* EnumFieldEncoder.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.util.Date;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>BaseTypeEncoder</code> for <code>Date</code>
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class DateEncoder implements BaseTypeEncoder<Date> {

	@Override
	public Date decode(CodedInputStream input) throws Exception {
		return new Date(input.readFixed64());
	}

	@Override
	public void encode(int fieldId, Date value, CodedOutputStream output)
			throws Exception {
		output.writeFixed64(fieldId, value.getTime());
	}


}