/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.uim.model.adapters;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;


/**
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 9 May 2012
 */
public abstract class QValueAdapterStrategy<FROMNS,FROMTYPE,TONS,TOTYPE>{

	
	
	/**
	 * @param <T>
	 * @param <Y>
	 * @param asd
	 * @return
	 */
	final <T, Y> List<QualifiedValue<T>> adaptqualifierList(List<QualifiedValue<Y>> value){
		
		List<QualifiedValue<Y>> retvalue = new ArrayList<QualifiedValue<Y>>();
		
		return null;
		
	}
	
	
	/**
	 * @param asd
	 * @return
	 */
	final <T, Y>List<T> adaptList(List<Y> value){
		
		List<Y> retvalue = new ArrayList<Y>();
		
		return null;
		
	}
	
	/**
	 * @param asd
	 * @return
	 */
	final <T, Y>QualifiedValue<T> adaptQvalue(QualifiedValue<Y> value){
		
		Y obj = adaptValue(value.getValue());
		
		//QualifiedValue<Y> retqvalue = new QualifiedValue<Y>();
		
		return null;
		
	}
	
	/**
	 * @param asd
	 * @return
	 */
	final <T, Y>T adaptValue(Y value){
		
		AdaptedOutput out = adaptoutput((TOTYPE) value);
		
		return null;
		
	}
	
	
    /**
     * @param adaptedResult
     * @return
     */
    public abstract AdaptedOutput adaptoutput(TOTYPE adaptedResult,Enum<?>... qualifiers);
    
    
    /**
     * @param key
     * @param qualifiers
     * @return
     */
	public abstract AdaptedInput adaptinput(TKey<FROMNS, FROMTYPE> key, Enum<?>... qualifiers);
    

    
        
    /**
     *
     * @author Georgios Markakis <gwarkx@hotmail.com>
     * @since 15 May 2012
     */
    public final class AdaptedInput
    {
    	private TKey<TONS,TOTYPE> key;
    	private Enum<?>[] qualifiers;
    	
    	private Class<TOTYPE> adaptedClass; 
    	
    	public AdaptedInput(Class<TOTYPE> adaptedClass){
    		this.setAdaptedClass(adaptedClass);
    	}
    	
		/**
		 * @return the key
		 */
		public TKey<TONS,TOTYPE> getKey() {
			return key;
		}
		
		/**
		 * @param key the key to set
		 */
		public void setKey(TKey<TONS,TOTYPE> key) {
			this.key = key;
		}

		/**
		 * @return the qualifiers
		 */
		public Enum<?>[] getQualifiers() {
			return qualifiers;
		}

		/**
		 * @param qualifiers the qualifiers to set
		 */
		public void setQualifiers(Enum<?>[] qualifiers) {
			this.qualifiers = qualifiers;
		}
		/**
		 * @return the adaptedClass
		 */
		public Class<TOTYPE> getAdaptedClass() {
			return adaptedClass;
		}
		/**
		 * @param adaptedClass the adaptedClass to set
		 */
		public void setAdaptedClass(Class<TOTYPE> adaptedClass) {
			this.adaptedClass = adaptedClass;
		}
    	
    }
    
    /**
    *
    * @author Georgios Markakis <gwarkx@hotmail.com>
    * @since 15 May 2012
    */
   public final class AdaptedOutput
   {
   	private FROMTYPE outputObject;
   	private Enum<?>[] outputQualifiers;
   	
   	private Class<FROMTYPE> adaptedClass; 
   	
   	public AdaptedOutput(Class<FROMTYPE> adaptedClass){
   		this.setAdaptedClass(adaptedClass);
   	}

		/**
		 * @return the adaptedClass
		 */
		public Class<FROMTYPE> getAdaptedClass() {
			return adaptedClass;
		}
		/**
		 * @param adaptedClass the adaptedClass to set
		 */
		public void setAdaptedClass(Class<FROMTYPE> adaptedClass) {
			this.adaptedClass = adaptedClass;
		}

		/**
		 * @return the outputQualifiers
		 */
		public Enum<?>[] getOutputQualifiers() {
			return outputQualifiers;
		}

		/**
		 * @param outputQualifiers the outputQualifiers to set
		 */
		public void setOutputQualifiers(Enum<?>[] outputQualifiers) {
			this.outputQualifiers = outputQualifiers;
		}

		/**
		 * @return the outputObject
		 */
		public FROMTYPE getOutputObject() {
			return outputObject;
		}

		/**
		 * @param outputObject the outputObject to set
		 */
		public void setOutputObject(FROMTYPE outputObject) {
			this.outputObject = outputObject;
		}
   	
   }
    
    
}
