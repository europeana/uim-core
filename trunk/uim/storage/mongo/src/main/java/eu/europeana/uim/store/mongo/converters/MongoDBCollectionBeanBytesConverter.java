/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.store.mongo.converters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.theeuropeanlibrary.repository.convert.Converter;


import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.store.bean.CollectionBean;

/**
 * Converts between collection bean and byte array.
 *
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * 
 * @since Jan 5, 2012
 */
@SuppressWarnings("rawtypes")
public class MongoDBCollectionBeanBytesConverter extends Converter<byte[], CollectionBean>{

    private static final int                         ID       = 1;
    private static final int                         MNEMONIC = 2;
    private static final int                         NAME     = 3;
    private static final int                         LANGUAGE = 4;
    private static final int                         OAIBASE  = 5;
    private static final int                         OAIMETA  = 6;
    private static final int                         OAISET   = 7;
    private static final int                         VALUES   = 8;

    
    /**
     * Private Constructor (instantiate via factory method)
     */
    private MongoDBCollectionBeanBytesConverter(){
    	
    }
    
    
    /**
     * Factory Method
     */
    public static MongoDBCollectionBeanBytesConverter getInstance() {
         return new MongoDBCollectionBeanBytesConverter();
    }
    
    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<CollectionBean> getDecodeType() {
        return CollectionBean.class;
    }

    @Override
    public CollectionBean<String> decode(byte[] data) {
        CollectionBean<String> bean = new CollectionBean<String>();
        CodedInputStream input = CodedInputStream.newInstance(data);
        int tag;
        
        
        try {
            while ((tag = input.readTag()) != 0) {
                int field = WireFormat.getTagFieldNumber(tag);
                switch (field) {
                case ID:
                    bean.setId(input.readString());
                    break;
                case MNEMONIC:
                    bean.setMnemonic(input.readString());
                    break;
                case NAME:
                    bean.setName(input.readString());
                    break;
                case LANGUAGE:
                    bean.setLanguage(input.readString());
                    break;
                case OAIBASE:
                    bean.setOaiBaseUrl(input.readString());
                    break;
                case OAIMETA:
                    bean.setOaiMetadataPrefix(input.readString());
                    break;
                case OAISET:
                    bean.setOaiSet(input.readString());
                    break;
                case VALUES:
                    Map<String, String> map = decodeMap(input.readString());
                    for (Entry<String, String> entry : map.entrySet()) {
                        bean.putValue(entry.getKey(), entry.getValue());
                    }
                    break;
                default:
                    break;
                }
            }

            while (!input.isAtEnd()) {
                String key = input.readString();
                String val = input.readString();
                bean.putValue(key, val);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read ProviderBean from byte array!", e);
        } finally {
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(CollectionBean data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CodedOutputStream output = CodedOutputStream.newInstance(bout);
        try {
            if (data.getId() != null) {
                output.writeString(ID,data.getId().toString());
            }
            if (data.getMnemonic() != null) {
                output.writeString(MNEMONIC, data.getMnemonic());
            }
            if (data.getName() != null) {
                output.writeString(NAME, data.getName());
            }
            if (data.getLanguage() != null) {
                output.writeString(LANGUAGE, data.getLanguage());
            }
            if (data.getOaiBaseUrl(false) != null) {
                output.writeString(OAIBASE, data.getOaiBaseUrl(false));
            }
            if (data.getOaiMetadataPrefix(false) != null) {
                output.writeString(OAIMETA, data.getOaiMetadataPrefix(false));
            }
            if (data.getOaiSet() != null) {
                output.writeString(OAISET, data.getOaiSet());
            }
            if (data.values() != null && !data.values().isEmpty()) {
                output.writeString(VALUES, encodeMap(data.values()));
            }

            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not write ProviderBean '" + data.getId() +
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

    static String encodeMap(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("$--$");
            }
            if (entry.getKey() != null)
                builder.append("\"" + entry.getKey() + "\"");
            else
                builder.append("null");
            builder.append("$==$");
            if (entry.getValue() != null)
                builder.append("\"" + entry.getValue() + "\"");
            else
                builder.append("null");

        }
        return builder.toString();
    }

    static Map<String, String> decodeMap(String data) {
        Map<String, String> result = new HashMap<String, String>();
        if (data != null && data.length() > 0) {
            String[] split = data.split("\\$--\\$");
            for (String string : split) {
                String[] split2 = string.split("\\$==\\$");
                if (split2.length != 2) {
                    throw new IllegalArgumentException("Can not parse map from string: <" + string +
                                                       ">");
                } else {
                    String stripKey = StringUtils.strip(split2[0], "\"");
                    if (stripKey.equals(split2[0])) stripKey = null;

                    String stripVal = StringUtils.strip(split2[1], "\"");
                    if (stripVal.equals(split2[1])) stripVal = null;
                    result.put(stripKey, stripVal);
                }

            }
        }
        return result;
    }
}
