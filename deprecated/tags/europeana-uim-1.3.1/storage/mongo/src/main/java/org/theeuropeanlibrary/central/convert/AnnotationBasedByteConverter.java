/* AnnotationBasedByteConverter.java - created on 5 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.repository.convert.Converter;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

/**
 * A <code>Converter</code> that converts objects according to annotations added to the serializable
 * fields on the converted classes. The field ids are specified by the <code>FieldId</code>
 * annotations. For inner Objects (besides base types), converters are specified on the constructor.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @param <T>
 *            The Class that this Converter converts
 * @date 5 de Abr de 2011
 */
public class AnnotationBasedByteConverter<T> extends Converter<byte[], T> {
    private Class<T>                           theClass;
    private ArrayList<FieldConverterInterface> idIndexedFieldArray;

    /**
     * Creates a new instance of this class.
     * 
     * @param theClass
     *            The converted class
     * @param customEncoders
     *            a map specifying converters to be used for specific fields containing complex
     *            Objects
     */
    public AnnotationBasedByteConverter(Class<T> theClass,
                                        Map<Integer, FieldConverterInterface> customEncoders) {
        this.theClass = theClass;
        ArrayList<FieldConverterInterface> fieldsWithAnnotatoins = new ArrayList<FieldConverterInterface>();
        HashMap<Integer, FieldConverterInterface> idToFieldMap = new HashMap<Integer, FieldConverterInterface>();
        int maxFieldId = initFieldsFromClass(theClass, customEncoders, fieldsWithAnnotatoins,
                idToFieldMap);

        idIndexedFieldArray = new ArrayList<FieldConverterInterface>(maxFieldId + 1);
        for (int i = 0; i <= maxFieldId; i++) {
            idIndexedFieldArray.add(idToFieldMap.get(i));
        }
    }

    private int initFieldsFromClass(Class<?> theClass,
            Map<Integer, FieldConverterInterface> customEncoders,
            ArrayList<FieldConverterInterface> fieldsWithAnnotatoins,
            HashMap<Integer, FieldConverterInterface> idToFieldMap) {
        int maxFieldId = 0;
        if (!theClass.getSuperclass().equals(Object.class))
            maxFieldId = initFieldsFromClass(theClass.getSuperclass(), customEncoders,
                    fieldsWithAnnotatoins, idToFieldMap);

        for (Field f : theClass.getDeclaredFields()) {
        	f.setAccessible(true);
            FieldId ann = f.getAnnotation(FieldId.class);
            if (ann != null) {
                FieldConverterInterface fldConv = null;
                maxFieldId = Math.max(maxFieldId, ann.value());
                if (customEncoders != null) fldConv = customEncoders.get(ann.value());
                if (fldConv == null)
                    fldConv = BaseTypeFieldConverterFactory.newInstanceFor(f.getType());
                fldConv.configure(f, ann.value());
                // fieldToIdMap.put(f, ann.id());
                idToFieldMap.put(ann.value(), fldConv);
                fieldsWithAnnotatoins.add(fldConv);
            }
        }
        return maxFieldId;
    }

    @Override
    public T decode(byte[] data) {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        try {
            Constructor<T> defaultConstructor = theClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            T obj = defaultConstructor.newInstance();
            CodedInputStream input = CodedInputStream.newInstance(bin);
            int tag;
            while ((tag = input.readTag()) != 0) {
                tag = WireFormat.getTagFieldNumber(tag);
                FieldConverterInterface fieldBufferSetter = idIndexedFieldArray.get(tag);
                fieldBufferSetter.decode(obj, input);
            }
            return obj;
        } catch (InstantiationException e) {
            throw new RuntimeException("Class default contructor thrown exception " + theClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Class does not provide an accesible default contructor " + theClass.getName(), e);
        } catch (IOException e) {
            throw new RuntimeException("Could not read from byte array! " + theClass.getName(), e);
        } catch (SecurityException e) {
			throw new RuntimeException("Could not access constructor " + theClass.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Default constructor does not exist in class " + theClass.getName(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Constructor thrown exception " + theClass.getName(), e);
		} finally {
            try {
                bin.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream!", e);
            }
        }
    }

    @Override
    public byte[] encode(T data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            for (FieldConverterInterface conv : idIndexedFieldArray) {
                if (conv != null) {
                    conv.encode(data, output);
                }
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not write ProviderBean '" + data.toString() +
                                       "' to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }
        return bout.toByteArray();
    }

    @Override
    public Class<T> getDecodeType() {
        return theClass;
    }

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }
}