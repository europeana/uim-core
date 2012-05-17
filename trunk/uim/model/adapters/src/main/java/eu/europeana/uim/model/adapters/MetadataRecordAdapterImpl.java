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

import java.util.List;
import java.util.Map;

import eu.europeana.uim.common.TKey;


import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;

/**
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @param <FROMNS>
 * @since 9 May 2012
 */
public class MetadataRecordAdapterImpl<I,Q extends QValueAdapterStrategy<?,?,?,?>> implements MetadataRecordAdapter<I>{

	public  Map<TKey<?, ?>,Q> strategies;
	
	private MetaDataRecord<I> adaptedRecord;
	
	
	/**
	 * @param adaptedRecord
	 * @param strategies
	 */
	private MetadataRecordAdapterImpl(MetaDataRecord<I>adaptedRecord,Map<TKey<?, ?>,Q> strategies){
		this.adaptedRecord = adaptedRecord;
		this.strategies = strategies;
		
		
	}
	
	
	/**
	 * @param adaptedRecord
	 * @param strategies
	 * @return
	 */
	public static <T,S extends QValueAdapterStrategy<?,?,?,?>> MetadataRecordAdapterImpl<T,S> init(MetaDataRecord<T> adaptedRecord,Map<TKey<?, ?>,S> strategies){
		return new MetadataRecordAdapterImpl<T,S>(adaptedRecord,strategies);
	}
	
	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#getCollection()
	 */
	@Override
	public Collection<I> getCollection() {
		return adaptedRecord.getCollection();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#getFirstValue(eu.europeana.uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N,T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers) {
	
		StrategyExecutor<N, T, T> executor = new StrategyExecutor<N, T, T>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				return adaptedRecord.getFirstValue(key, qualifiers);
			}

			@Override
			public T adaptback2normal(QValueAdapterStrategy<N, T, ?, ?> strategy, T result) {
				
				return strategy.adaptValue(result);
			}
			
		};
		
		executor.execute();
		return executor.getResult();
	}

	
	
	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#getFirstQualifiedValue(eu.europeana.uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> getFirstQualifiedValue(
			TKey<N, T> key, Enum<?>... qualifiers) {
		
		StrategyExecutor<N, T, QualifiedValue<T>> executor = new StrategyExecutor<N, T, QualifiedValue<T>>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				return adaptedRecord.getFirstQualifiedValue(key, qualifiers);
			}

			@Override
			public eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> adaptback2normal(
					QValueAdapterStrategy<N, T, ?, ?> strategy,
					eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> result) {
				return strategy.adaptQvalue(result);
			}
			
		};
		
		executor.execute();
		return executor.getResult();
	}

	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#getQualifiedValues(eu.europeana.uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> getQualifiedValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		
		StrategyExecutor<N, T, List<QualifiedValue<T>>> executor = new StrategyExecutor<N, T, List<QualifiedValue<T>>>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				return adaptedRecord.getQualifiedValues(key, qualifiers);
			}

			@Override
			public List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> adaptback2normal(
					QValueAdapterStrategy<N, T, ?, ?> strategy,
					List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> result) {
				return strategy.adaptqualifierList(result);
			}
		};
		
		executor.execute();
		return executor.getResult();
	}

	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#getValues(eu.europeana.uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
		
		StrategyExecutor<N, T, List<T>> executor = new StrategyExecutor<N, T, List<T>>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				return adaptedRecord.getFirstValue(key, qualifiers);
			}

			@Override
			public List<T> adaptback2normal(
					QValueAdapterStrategy<N, T, ?, ?> strategy, List<T> result) {
				return strategy.adaptList(result);
			}


			
		};
		executor.execute();
		return  executor.getResult();
	}

	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#addValue(eu.europeana.uim.common.TKey, java.lang.Object, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> void addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
		StrategyExecutor<N, T, List<T>> executor = new StrategyExecutor<N, T, List<T>>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				adaptedRecord.addValue(key,null, qualifiers);
				return null;
			}

			@Override
			public List<T> adaptback2normal(
					QValueAdapterStrategy<N, T, ?, ?> strategy, List<T> result) {
				// Not actually used, since method returns no value
				return null;
			}			
		};
		
		executor.execute();
	}
	

	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.MetaDataRecord#deleteValues(eu.europeana.uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> deleteValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		StrategyExecutor<N, T, List<QualifiedValue<T>>> executor = new StrategyExecutor<N, T, List<QualifiedValue<T>>>(key, qualifiers){

			@Override
			public Object query(TKey<?,?> key, Enum<?>... qualifiers) {

				return adaptedRecord.deleteValues(key, qualifiers);
			}

			@Override
			public List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> adaptback2normal(
					QValueAdapterStrategy<N, T, ?, ?> strategy,
					List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> result) {
				return strategy.adaptqualifierList(result);
			}
		};
		
		executor.execute();
		return executor.getResult();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.UimEntity#getId()
	 */
	@Override
	public I getId() {
		return adaptedRecord.getId();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.model.adapters.MetadataRecordAdapter#getAdaptedRecord()
	 */
	@Override
	public MetaDataRecord<I> getAdaptedRecord() {
		return adaptedRecord;
	}

	
	
	/**
	 *
	 * @author Georgios Markakis <gwarkx@hotmail.com>
	 * @since 15 May 2012
	 * @param <N>
	 * @param <T>
	 * @param <Y>
	 * @param <X>
	 * @param <RESTYPE>
	 */
	private abstract class StrategyExecutor<N,T,RESTYPE>{

		TKey<N, T> key;
		Enum<?>[] qualifiers;
		private RESTYPE result;
		
		public StrategyExecutor(TKey<N, T> key, Enum<?>... qualifiers){
			this.key = key;
			this.qualifiers = qualifiers;
		}
		
		
		public abstract Object query(TKey<?, ?> key, Enum<?>... qualifiers);
		
		
		public abstract RESTYPE adaptback2normal(QValueAdapterStrategy<N,T,?,?> strategy, RESTYPE result);
		
		
		/**
		 * 
		 */
		public void execute(){
			//First find if a transformation strategy is available 
			QValueAdapterStrategy<N,T,?,?> strategy = (QValueAdapterStrategy<N,T,?,?>) strategies.get(key);;
			if (strategy == null){
				setResult((RESTYPE) query(key, qualifiers));
			}
			else{
				
				//First convert the value into the format which is anticipated to be present in 
				//the qualifier index
				QValueAdapterStrategy<N,T,?,?>.AdaptedInput adapted = strategy.adaptinput(key,qualifiers);
				
				//Get the result according to the converted key and qualifiers
				RESTYPE adaptedResult = (RESTYPE) query(adapted.getKey(), adapted.getQualifiers()); 
				
				//Adapt the object of the retrieved result
				RESTYPE result = (RESTYPE) adaptback2normal(strategy,adaptedResult);
				
				setResult(result);
			}
			
		}


		/**
		 * @return the result
		 */
		public RESTYPE getResult() {
			return result;
		}


		/**
		 * @param result the result to set
		 */
		public void setResult(RESTYPE result) {
			this.result = result;
		}
	}
	


}
