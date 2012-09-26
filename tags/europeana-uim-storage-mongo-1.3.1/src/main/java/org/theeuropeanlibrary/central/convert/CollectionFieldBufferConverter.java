/* CollectionFieldBufferConverter.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class CollectionFieldBufferConverter implements FieldConverterInterface {
    Field                   field;
	private int id;
	@SuppressWarnings("rawtypes")
	private Converter itemConverter;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param itemConverter
	 *            the converter for the items in the collection
	 */
	@SuppressWarnings("rawtypes")
	public CollectionFieldBufferConverter(Converter itemConverter) {
		this.itemConverter = itemConverter;
	}

	@Override
	public void configure(Field field, int fieldId) {
		this.field = field;
		this.id = fieldId;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void decode(Object bean, CodedInputStream input) {
		try {
			Collection collection = (Collection) field.get(bean);
			CodedInputStream inputCollection = CodedInputStream
					.newInstance(input.readBytes().toByteArray());
			while ((inputCollection.readTag()) != 0) {
				byte[] itemBytes = inputCollection.readBytes().toByteArray();
				collection.add(itemConverter.decode(itemBytes));
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception during serialization", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void encode(Object bean, CodedOutputStream output) {
		try {
			Collection collection = (Collection) field.get(bean);
			for (Object obj : collection) {
				if (obj != null) {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					CodedOutputStream innerOutput = CodedOutputStream
							.newInstance(bout);
					try {
						byte[] bytes = (byte[]) itemConverter.encode(obj);
						innerOutput.writeBytes(1, ByteString.copyFrom(bytes));
						innerOutput.flush();
						output.writeBytes(id,
								ByteString.copyFrom(bout.toByteArray()));
					} catch (Exception e) {
						throw new RuntimeException(
								"Could not write collection '"
										+ field.getName()
										+ "' to byte array!", e);
					} finally {
						try {
							bout.close();
						} catch (IOException e) {
							throw new RuntimeException(
									"Could not close output stream!", e);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception during serialization", e);
		}
	}
}