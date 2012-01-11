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
import java.util.Map;
import java.util.Map.Entry;
import org.theeuropeanlibrary.repository.convert.Converter;


import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

import eu.europeana.uim.store.bean.ProviderBean;

/**
 * Converts between provider bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since Feb 18, 2011
 */

@SuppressWarnings("rawtypes")
public class MongoDBProviderBeanBytesConverter  extends Converter<byte[], ProviderBean>{


    private static final int                       ID         = 1;
    private static final int                       MNEMONIC   = 2;
    private static final int                       NAME       = 3;
    private static final int                       AGGREGATOR = 4;
    private static final int                       OAIBASE    = 5;
    private static final int                       OAIMETA    = 6;
    private static final int                       VALUES     = 7;

    
    
    
    /**
     * Private Constructor (instantiate via factory method)
     */
    private MongoDBProviderBeanBytesConverter(){
    	
    }
        
    /**
     * Factory Method
     */
    public static MongoDBProviderBeanBytesConverter getInstance() {
    	return new MongoDBProviderBeanBytesConverter();
    }
    
    
    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#getEncodeType()
     */
    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#getDecodeType()
     */
    @Override
    public Class<ProviderBean> getDecodeType() {
        return ProviderBean.class;
    }
    
    
    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#decode(java.lang.Object)
     */
    @Override
    public ProviderBean<String> decode(byte[] data) {
        ProviderBean<String> bean = new ProviderBean<String>();
        
        
        if(data != null){

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
                case AGGREGATOR:
                    bean.setAggregator(input.readBool());
                    break;
                case OAIBASE:
                    bean.setOaiBaseUrl(input.readString());
                    break;
                case OAIMETA:
                    bean.setOaiMetadataPrefix(input.readString());
                    break;
                case VALUES:
                    Map<String, String> map = MongoDBCollectionBeanBytesConverter.decodeMap(input.readString());
                    for (Entry<String, String> entry : map.entrySet()) {
                        bean.putValue(entry.getKey(), entry.getValue());
                    }
                    break;
                default:
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read ProviderBean from byte array!", e);
        } finally {
        }
        
        }
        
        return bean;
    }

    
    
    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#encode(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(ProviderBean data) {
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
            output.writeBool(AGGREGATOR, data.isAggregator());
            if (data.getOaiBaseUrl() != null) {
                output.writeString(OAIBASE, data.getOaiBaseUrl());
            }
            if (data.getOaiMetadataPrefix() != null) {
                output.writeString(OAIMETA, data.getOaiMetadataPrefix());
            }
            if (data.values() != null && !data.values().isEmpty()) {
                output.writeString(VALUES, MongoDBCollectionBeanBytesConverter.encodeMap(data.values()));
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
}
