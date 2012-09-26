/* CollectionFieldBufferConverter.java - created on 6 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * A <code>FieldConverterInterface</code> implementation for complex objects
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 6 de Abr de 2011
 */
public class ConverterBasedFieldConverter implements FieldConverterInterface {
	private Field field;
	private int id;
	@SuppressWarnings("rawtypes")
	private Converter itemConverter;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param itemConverter
	 *            the converter to be applied to the inner Object
	 */
	@SuppressWarnings("rawtypes")
	public ConverterBasedFieldConverter(Converter itemConverter) {
		this.itemConverter = itemConverter;
	}

	@Override
	public void configure(Field field, int fieldId) {
		this.field = field;
		this.id = fieldId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void decode(Object bean, CodedInputStream input) {
		try {
			CodedInputStream inputItem = CodedInputStream.newInstance(input
					.readBytes().toByteArray());
			inputItem.readTag();
			byte[] itemBytes = inputItem.readBytes().toByteArray();

			field.set(bean, itemConverter.decode(itemBytes));
		} catch (Exception e) {
			throw new RuntimeException("Exception during serialization", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void encode(Object bean, CodedOutputStream output) {
		try {
			Object obj = field.get(bean);
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
					throw new RuntimeException("Could not write collection '"
							+ field.getName() + "' to byte array!", e);
				} finally {
					try {
						bout.close();
					} catch (IOException e) {
						throw new RuntimeException(
								"Could not close output stream!", e);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception during serialization", e);
		}
	}

	// @Override
	// public void decodeFromXml(Object bean, Element xmlElement) {
	//
	//
	// //
	// throw new UnsupportedOperationException("Sorry, not implemented.");
	// }
	//
	// @Override
	// public void encodeToXml(Object bean, Element xmlParentElement) {
	// try {
	// Object obj = fieldGet.invoke(bean);
	// if (obj != null) {
	// itemConverter.encode(obj)
	//
	//
	// ByteArrayOutputStream bout = new ByteArrayOutputStream();
	// CodedOutputStream innerOutput = CodedOutputStream
	// .newInstance(bout);
	// try {
	// byte[] bytes = (byte[]) itemConverter.encode(obj);
	// innerOutput.writeBytes(1, ByteString.copyFrom(bytes));
	// innerOutput.flush();
	// output.writeBytes(id,
	// ByteString.copyFrom(bout.toByteArray()));
	// } catch (Exception e) {
	// throw new RuntimeException("Could not write collection '"
	// + fieldGet.getName() + "' to byte array!", e);
	// } finally {
	// try {
	// bout.close();
	// } catch (IOException e) {
	// throw new RuntimeException(
	// "Could not close output stream!", e);
	// }
	// }
	// }
	// } catch (Exception e) {
	// throw new RuntimeException("Exception during serialization", e);
	// }
	//
	// //
	// throw new UnsupportedOperationException("Sorry, not implemented.");
	// }
}