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
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import eu.europeana.uim.common.TKey;
import gnu.trove.TLongHashSet;

import org.theeuropeanlibrary.repository.convert.Converter;


import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * Converts a List to a byte array and back
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 10, 2011
 */

@SuppressWarnings("rawtypes")
public class MongoDBTHashSetBytesConverter extends Converter<byte[], Set>{


    /**
     * Private Constructor (instantiate via factory method)
     */
	private MongoDBTHashSetBytesConverter(){
		
	}
	
    /**
     * Factory Method
     */
    public static MongoDBTHashSetBytesConverter getInstance() {
      return new MongoDBTHashSetBytesConverter();
    }

    @Override
    public Set decode(byte[] data) {
        InputStream bin = new ByteArrayInputStream(data);
        HashSet<TKey<?, ?>> result = new HashSet<TKey<?, ?>>();
        if(data != null){

        try {
            CodedInputStream input = null;
            try {
                GZIPInputStream gzip = new GZIPInputStream(bin);
                input = CodedInputStream.newInstance(gzip);
                input.setSizeLimit(Integer.MAX_VALUE);
            } catch (Throwable t) {
                input = CodedInputStream.newInstance(bin);
                input.setSizeLimit(2 * data.length);
            }
            
            while (!input.isAtEnd()) {
            	
                TKey<?, ?> val =TKey.fromString(input.readString());
                result.add(val);
            }
            bin.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not read long set from byte array!", e);
        } finally {
        }
        }
        return result;
    }

    @Override
    public byte[] encode(Set set) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        @SuppressWarnings("unchecked")
		HashSet<TKey<?, ?>> castset = (HashSet<TKey<?, ?>>) set;
        
        if(set == null){
        	
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(bout);
            CodedOutputStream output = CodedOutputStream.newInstance(gzip);
            synchronized (set) {
                Object[] array = castset.toArray();
                for (Object o : array) {
                    //output.writeString(o);
                }
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
        }
        byte[] data = bout.toByteArray();
        return data;
    }

    @Override
    public Class<byte[]> getEncodeType() {
        return byte[].class;
    }

    @Override
    public Class<Set> getDecodeType() {
        return  Set.class;
    }

}
