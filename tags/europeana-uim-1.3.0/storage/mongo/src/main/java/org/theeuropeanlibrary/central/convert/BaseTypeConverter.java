/* FieldBufferSetter.java - created on 5 de Abr de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.central.convert;

import java.lang.reflect.Field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * /** A <code>FieldConverterInterface</code> implementation for base types
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 5 de Abr de 2011
 */
public class BaseTypeConverter implements FieldConverterInterface {
    Field                   field;
    int                     id;
    String                  xmlElementName;
    BaseTypeEncoder<Object> encoder;

    /**
     * Creates a new instance of this class.
     * 
     * @param encoder
     */
    public BaseTypeConverter(BaseTypeEncoder<Object> encoder) {
        this.encoder = encoder;
    }

    @Override
    public void configure(Field field, int fieldId) {
        this.field = field;
        this.id = fieldId;
        xmlElementName = Character.toUpperCase(field.getName().charAt(0)) +
        	field.getName().substring(1);
    }

    @Override
    public void decode(Object bean, CodedInputStream input) {
        try {
            field.set(bean, encoder.decode(input));
        } catch (Exception e) {
            throw new RuntimeException("Exception during serialization", e);
        }
    }

    @Override
    public void encode(Object bean, CodedOutputStream output) {
        try {
            Object value = field.get(bean);
            if (value != null) encoder.encode(id, value, output);
        } catch (Exception e) {
            throw new RuntimeException("Exception during serialization", e);
        }
    }


}