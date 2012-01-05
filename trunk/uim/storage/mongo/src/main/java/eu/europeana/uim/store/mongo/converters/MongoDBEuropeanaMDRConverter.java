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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import gnu.trove.TLongHashSet;

import org.bson.types.ObjectId;
import org.theeuropeanlibrary.repository.convert.Converter;


import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;

/**
 * Converts between metadata record bean and byte array.
 * 
 * @author Andreas Juffinger <andreas.juffinger@kb.nl>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * 
 * @since Jan 5, 2012
 */

@SuppressWarnings("rawtypes")
public class MongoDBEuropeanaMDRConverter extends Converter<HashMap<String, List<byte[]>>, MetaDataRecordBean<ObjectId>>{


	private static final int FIELD_ENTRY_ORDER     = 10;
    private static final int FIELD_ENTRY_VALUE     = 20;
    private static final int FIELD_ENTRY_QUALIFIER = 30;
	
    /**
     * Private Constructor (instantiate via factory method)
     */
	private MongoDBEuropeanaMDRConverter(){
		
	}
	
    /**
     * Factory Method
     */
    public static MongoDBEuropeanaMDRConverter getInstance() {
      return new MongoDBEuropeanaMDRConverter();
    }

    
    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#decode(java.lang.Object)
     */
    @Override
    public MetaDataRecordBean<ObjectId> decode(HashMap<String, List<byte[]>> fields) {

    	MetaDataRecordBean<ObjectId> mdr = new MetaDataRecordBean<ObjectId>();
    	
    	Collection c = fields.keySet();
    	   
        Iterator itr = c.iterator();
    	
        while (itr.hasNext()){
        	
        	TKey key = TKey.fromString((String)itr.next());
        
        	
        	List<byte[]> bqvalues =  fields.get(key.toString());
 
        	List<QualifiedValue<String>> qvalues = new ArrayList<QualifiedValue<String>>();
        	
        	
        	for(byte[] qvalue: bqvalues){
        		
        		QualifiedValue qval;
				try {
					qval = decodeQualifiedValue( key, qvalue);
					qvalues.add(qval);
				} catch (IOException e) {
					e.printStackTrace();
				}

        	}
			mdr.setValue(key, qvalues);  
        }

        return mdr;
    }

    
    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#encode(java.lang.Object)
     */
    @Override
    public HashMap<String, List<byte[]>> encode(MetaDataRecordBean<ObjectId> rec) {
    	
    	HashMap<String, List<byte[]>> sd = new HashMap<String, List<byte[]>>();
    	
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        	
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(bout);
            CodedOutputStream output = CodedOutputStream.newInstance(gzip);
            
             Set<TKey<?, ?>> keys = rec.getAvailableKeys();
             
             for(TKey<?, ?> key: keys){
            	 ArrayList<byte[]> arlist = new ArrayList<byte[]>();
            	 
            	 List<?> values = rec.getQualifiedValues(key);
            	 
            	 for(Object qval: values){
            		 byte[] enc = encodeQualifiedValue((QualifiedValue)qval);
            		 arlist.add(enc);
            	 }
            	 
            	 sd.put(key.toString(), arlist);
            	 
             }
            
            output.flush();
            
            gzip.flush();
            gzip.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not write long set to byte array!", e);
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close output stream!", e);
            }
        }
        

        return sd;
    }
    
    
    
    /**
     * @param qval
     * @return
     */
    private byte[] encodeQualifiedValue(QualifiedValue qval){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(bout);
	        CodedOutputStream output = CodedOutputStream.newInstance(gzip);
	        
	        output.writeInt32(FIELD_ENTRY_ORDER, qval.getOrderIndex());
	        
	        output.writeString(FIELD_ENTRY_VALUE, (String)qval.getValue());
	        

	        Set<Enum<?>> qualifiers = qval.getQualifiers();
	        	            for (Enum<?> qualifier : qualifiers) {
	        	                String qualifierEncoded = qualifier.getClass().getName() + "@" + qualifier.name();
	        	                output.writeString(FIELD_ENTRY_QUALIFIER, qualifierEncoded);
	       }

	       output.flush();
	       
           gzip.flush();
           gzip.close();
	       
		} catch (IOException e) {
			e.printStackTrace();
		}
        finally {
        try {
            bout.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close output stream!", e);
        }
    }

    	
    	return bout.toByteArray();
    }
    
    
    /**
     * @param <NS>
     * @param <T>
     * @param key
     * @param enc
     * @return
     * @throws IOException
     */
    private <NS, T> QualifiedValue  decodeQualifiedValue(TKey<NS, T> key,byte[] enc) throws IOException{

    	GZIPInputStream bin = new GZIPInputStream(new ByteArrayInputStream(enc));
        CodedInputStream input = CodedInputStream.newInstance(bin);

        
        int order = -1;
        String value = null;
        Set<Enum<?>> qualifiers = new HashSet<Enum<?>>();
        int tag;
        while ((tag = input.readTag()) != 0) {
            int field = WireFormat.getTagFieldNumber(tag);
            switch (field) {
            case FIELD_ENTRY_ORDER:
                order = input.readInt32();
                break;
            case FIELD_ENTRY_VALUE:
            	value = input.readString();
                break;
            case FIELD_ENTRY_QUALIFIER:
                String encoded = input.readString();
                String[] split = encoded.split("@");
                Class<? extends Enum> type;
                try {
                    type = (Class<? extends Enum>)Class.forName(split[0]);
                } catch (ClassNotFoundException e) { 
                    throw new RuntimeException("Could not convert encoded enum '" + encoded +
                                               "'!", e);
                }
                Enum<?> enumValue = Enum.valueOf(type, split[1]);
                qualifiers.add(enumValue);
                break;
            default:
                break;
            }
        }
        
        QualifiedValue qval = new QualifiedValue(value,qualifiers,order);
        
    	return qval;
    }
    

    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#getEncodeType()
     */
    @SuppressWarnings("unchecked")
	@Override
    public Class<HashMap<String, List<byte[]>>> getEncodeType() {
        return  (Class<HashMap<String, List<byte[]>>>) new HashMap<String, List<byte[]>>().getClass();
    }

    /* (non-Javadoc)
     * @see org.theeuropeanlibrary.repository.convert.Converter#getDecodeType()
     */
    @SuppressWarnings("unchecked")
	@Override
    public Class<MetaDataRecordBean<ObjectId>> getDecodeType() {
        return  (Class<MetaDataRecordBean<ObjectId>>) new MetaDataRecordBean<ObjectId>().getClass();
    }

}
