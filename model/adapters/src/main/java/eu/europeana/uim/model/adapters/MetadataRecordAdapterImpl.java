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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;

/**
 *
 * @param <I>
 * @param <Q>
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 9 May 2012
 */
public class MetadataRecordAdapterImpl<I, Q extends QValueAdapterStrategy<?, ?, ?, ?>> implements
        MetadataRecordAdapter<I, Q> {

    /**
     * registered strategies
     */
    public Map<TKey<?, ?>, Q> strategies;

    private MetaDataRecord<I> adaptedRecord;

    /**
     * @param adaptedRecord
     * @param strategies
     */
    protected MetadataRecordAdapterImpl(MetaDataRecord<I> adaptedRecord,
            Map<TKey<?, ?>, Q> strategies) {
        this.adaptedRecord = adaptedRecord;
        this.strategies = strategies;
    }

    @Override
    public Collection<I> getCollection() {
        return adaptedRecord.getCollection();
    }

    @Override
    public <N, T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers) {
        StrategyExecutor<N, T, T> executor = new StrategyExecutor<N, T, T>(key, null, qualifiers) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public Object query(TKey key, Object value, Enum<?>[] qualifiers) {

                return adaptedRecord.getFirstValue(key, qualifiers);
            }

            @Override
            public T adaptback2normal(QValueAdapterStrategy<N, T, ?, ?> strategy, Object result) {
                return strategy.adaptValue(result);
            }
        };
        executor.execute();
        return executor.getResult();
    }

    @Override
    public <N, T> eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> getFirstQualifiedValue(
            TKey<N, T> key, Enum<?>... qualifiers) {

        StrategyExecutor<N, T, QualifiedValue<T>> executor = new StrategyExecutor<N, T, QualifiedValue<T>>(
                key, null, qualifiers) {

                    @SuppressWarnings({"unchecked", "rawtypes"})
                    @Override
                    public Object query(TKey key, Object value, Enum<?>[] qualifiers) {

                        return adaptedRecord.getFirstQualifiedValue(key, qualifiers);
                    }

                    @Override
                    public eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> adaptback2normal(
                            QValueAdapterStrategy<N, T, ?, ?> strategy, Object result) {
                                return strategy.adaptQvalue((eu.europeana.uim.store.MetaDataRecord.QualifiedValue<?>) result);
                            }
                };

        executor.execute();
        return executor.getResult();
    }

    @Override
    public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> getQualifiedValues(
            TKey<N, T> key, Enum<?>... qualifiers) {

        StrategyExecutor<N, T, List<QualifiedValue<T>>> executor = new StrategyExecutor<N, T, List<QualifiedValue<T>>>(
                key, null, qualifiers) {

                    @SuppressWarnings({"unchecked", "rawtypes"})
                    @Override
                    public Object query(TKey key, Object value, Enum<?>[] qualifiers) {

                        return adaptedRecord.getQualifiedValues(key, qualifiers);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> adaptback2normal(
                            QValueAdapterStrategy<N, T, ?, ?> strategy, Object result) {

                                return (List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>>) strategy.adaptqualifierList((List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<?>>) result);
                            }
                };

        executor.execute();
        return executor.getResult();
    }

    @Override
    public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
        StrategyExecutor<N, T, List<T>> executor = new StrategyExecutor<N, T, List<T>>(key, null,
                qualifiers) {
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    @Override
                    public Object query(TKey key, Object value, Enum<?>[] qualifiers) {

                        return adaptedRecord.getFirstValue(key, qualifiers);
                    }

                    @Override
                    public List<T> adaptback2normal(QValueAdapterStrategy<N, T, ?, ?> strategy,
                            Object result) {
                        return strategy.adaptList((List<?>) result);
                    }
                };
        executor.execute();
        return executor.getResult();
    }

    @Override
    public <N, T> QualifiedValue<T> addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
        @SuppressWarnings("unchecked")
        final QualifiedValue<T>[] ret = new QualifiedValue[1];

        StrategyExecutor<N, T, List<T>> executor = new StrategyExecutor<N, T, List<T>>(key, value,
                qualifiers) {
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    @Override
                    public Object query(TKey key, Object value, Enum<?>[] qualifiers) {

                        ret[0] = adaptedRecord.addValue(key, value, qualifiers);
                        return null;
                    }

                    @Override
                    public List<T> adaptback2normal(QValueAdapterStrategy<N, T, ?, ?> strategy,
                            Object result) {
                        // Not actually used, since method returns no value
                        return null;
                    }
                };

        executor.execute();
        return ret[0];
    }

    @Override
    public <N, T> boolean deleteValue(TKey<N, T> key,
            eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> value) {
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> deleteValues(
            TKey<N, T> key, Enum<?>... qualifiers) {
        StrategyExecutor<N, T, List<QualifiedValue<T>>> executor = new StrategyExecutor<N, T, List<QualifiedValue<T>>>(
                key, null, qualifiers) {
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    @Override
                    public Object query(TKey key, Object value, Enum<?>[] qualifiers) {
                        return adaptedRecord.deleteValues(key, qualifiers);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> adaptback2normal(
                            QValueAdapterStrategy<N, T, ?, ?> strategy, Object result) {
                                return (List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>>) strategy.adaptqualifierList((List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<?>>) result);
                            }
                };

        executor.execute();
        return executor.getResult();
    }

    @Override
    public I getId() {
        return adaptedRecord.getId();
    }

    @Override
    public MetaDataRecord<I> getAdaptedRecord() {
        return adaptedRecord;
    }

     /**
     * @param id engine base ID
     */
//    @Override
    public void setId(I id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUniqueId(String uniqueId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public Set<Object> getExternalIdentifiers() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void addExternalIdentifier(Object externalId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void removeExternalIdentifier(Object externalId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

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
    @SuppressWarnings("unused")
    private abstract class StrategyExecutor<N, T, RESTYPE> {

        TKey<N, T> key;
        Enum<?>[] qualifiers;
        T value;

        private RESTYPE result;

        /**
         * Creates a new instance of this class.
         *
         * @param key
         * @param value
         */
        public StrategyExecutor(TKey<N, T> key, T value) {
            this.key = key;
            this.value = value;
        }

        public StrategyExecutor(TKey<N, T> key, T value, Enum<?>... qualifiers) {
            this.key = key;
            this.value = value;
            this.qualifiers = qualifiers;
        }

        /**
         * @param key
         * @param value
         * @param qualifiers
         * @return object
         */
        @SuppressWarnings("rawtypes")
        public abstract Object query(TKey key, Object value, Enum<?>[] qualifiers);

        public abstract RESTYPE adaptback2normal(QValueAdapterStrategy<N, T, ?, ?> strategy,
                Object result);

        /**
         *
         */
        @SuppressWarnings("unchecked")
        public void execute() {
            // First find if a transformation strategy is available
            QValueAdapterStrategy<N, T, ?, ?> strategy = (QValueAdapterStrategy<N, T, ?, ?>) strategies.get(key);

            if (strategy == null) {
                setResult((RESTYPE) query(key, value, qualifiers));
            } else {

                // First convert the value into the format which is anticipated to be present in
                // the qualifier index
                QValueAdapterStrategy<N, T, ?, ?>.AdaptedInput adapted = strategy.adaptinput(key,
                        qualifiers);

                Enum<?>[] qualifiersarray = null;

                if (adapted.getQualifiers() != null) {
                    Set<Enum<?>> qs = adapted.getQualifiers();

                    qualifiersarray = new Enum<?>[qs.size()];
                    Iterator<Enum<?>> it = qs.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        qualifiersarray[i] = it.next();
                        i++;
                    }

                }
                adapted.getQualifiers();

                // Get the result according to the converted key and qualifiers
                Object adaptedResult = query(adapted.getKey(), value, qualifiersarray);

                // Adapt the object of the retrieved result
                RESTYPE normal = adaptback2normal(strategy, adaptedResult);
                setResult(normal);
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

    @Override
    public <S, T> void addRelation(QualifiedValue<S> source, QualifiedValue<T> target,
            Enum<?>... qualifiers) {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <T> void deleteRelations(QualifiedValue<T> value, Enum<?>... qualifiers) {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <N, S, T> Set<QualifiedValue<T>> getTargetQualifiedValues(QualifiedValue<S> source,
            TKey<N, T> targetKey, Enum<?>... qualifiers) {
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <N, S, T> Set<QualifiedValue<S>> getSourceQualifiedValues(QualifiedValue<T> target,
            TKey<N, S> sourceKey, Enum<?>... qualifiers) {
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <N, S, T> Set<eu.europeana.uim.store.MetaDataRecord.QualifiedRelation<S, T>> getTargetQualifiedRelations(
            eu.europeana.uim.store.MetaDataRecord.QualifiedValue<S> source, TKey<N, T> targetKey,
            Enum<?>... qualifiers) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public <N, S, T> Set<eu.europeana.uim.store.MetaDataRecord.QualifiedRelation<S, T>> getSourceQualifiedRelations(
            eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> target, TKey<N, S> sourceKey,
            Enum<?>... qualifiers) {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }
}
