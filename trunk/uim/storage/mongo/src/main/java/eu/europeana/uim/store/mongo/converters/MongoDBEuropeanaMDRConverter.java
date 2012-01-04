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
import java.util.HashMap;
import java.util.HashSet;
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
 * Converts a List to a byte array and back
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 10, 2011
 */

@SuppressWarnings("rawtypes")
public class MongoDBEuropeanaMDRConverter extends Converter<HashMap<String, List<byte[]>>, MetaDataRecordBean<ObjectId>>{


	private static final int FIELD_ENTRY_ORDER     = 1;
    private static final int FIELD_ENTRY_VALUE     = 2;
    private static final int FIELD_ENTRY_QUALIFIER = 3;
	
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

    @Override
    public MetaDataRecordBean<ObjectId> decode(HashMap<String, List<byte[]>> fields) {


        return null;
    }

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	return bout.toByteArray();
    }
    
    
    private <NS, T> QualifiedValue  decodeQualifiedValue(TKey<NS, T> key,byte[] enc) throws IOException{
    	ByteArrayInputStream bin = new ByteArrayInputStream(enc);
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
        
        
    	return null;
    }
    

    @SuppressWarnings("unchecked")
	@Override
    public Class<HashMap<String, List<byte[]>>> getEncodeType() {
        return  (Class<HashMap<String, List<byte[]>>>) new HashMap<String, List<byte[]>>().getClass();
    }

    @SuppressWarnings("unchecked")
	@Override
    public Class<MetaDataRecordBean<ObjectId>> getDecodeType() {
        return  (Class<MetaDataRecordBean<ObjectId>>) new MetaDataRecordBean<ObjectId>().getClass();
    }

}
