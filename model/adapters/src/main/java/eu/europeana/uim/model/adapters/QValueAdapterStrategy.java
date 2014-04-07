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
import java.util.Set;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;

/**
 * @param <FROMNS>
 * @param <FROMTYPE>
 * @param <TONS>
 * @param <TOTYPE>
 * 
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 9 May 2012
 */
@SuppressWarnings("unchecked")
public abstract class QValueAdapterStrategy<FROMNS, FROMTYPE, TONS, TOTYPE> {
    /**
     * @param value
     * @return
     */
    final List<?> adaptqualifierList(
            List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<?>> value) {
        List<QualifiedValue<FROMTYPE>> retvalue = new ArrayList<QualifiedValue<FROMTYPE>>();
        for (QualifiedValue<?> item : value) {
            retvalue.add(adaptQvalue(item));
        }
        return retvalue;
    }

    /**
     * @param asd
     * @return
     */
    final List<FROMTYPE> adaptList(List<?> value) {
        List<FROMTYPE> retlist = new ArrayList<FROMTYPE>();
        for (Object i : value) {
            retlist.add(adaptValue(i));
        }
        return null;
    }

    /**
     * @param asd
     * @return
     */
    final QualifiedValue<FROMTYPE> adaptQvalue(QualifiedValue<?> value) {
        AdaptedOutput out = adaptoutput((TOTYPE)value.getValue(), value.getQualifiers());
        FROMTYPE obj = out.getOutputObject();
        QualifiedValue<FROMTYPE> retqvalue = new QualifiedValue<FROMTYPE>(obj,
                out.getOutputQualifiers(), value.getOrderIndex());
        return retqvalue;
    }

    /**
     * @param asd
     * @return
     */
    final FROMTYPE adaptValue(Object value) {
        AdaptedOutput out = adaptoutput((TOTYPE)value, null);
        return out.getOutputObject();

    }

    /**
     * @param adaptedResult
     * @param set
     * @return adapted output
     */
    public abstract AdaptedOutput adaptoutput(TOTYPE adaptedResult, Set<Enum<?>> set);

    /**
     * @param key
     * @param qualifiers
     * @return adapted input
     */
    public abstract AdaptedInput adaptinput(TKey<FROMNS, FROMTYPE> key, Enum<?>... qualifiers);

    /**
     * 
     * @author Georgios Markakis <gwarkx@hotmail.com>
     * @since 15 May 2012
     */
    public final class AdaptedInput {
        private TKey<TONS, TOTYPE> key;
        private Set<Enum<?>>       qualifiers;

        /**
         * @return the key
         */
        public TKey<TONS, TOTYPE> getKey() {
            return key;
        }

        /**
         * @param key
         *            the key to set
         */
        public void setKey(TKey<TONS, TOTYPE> key) {
            this.key = key;
        }

        /**
         * @return the qualifiers
         */
        public Set<Enum<?>> getQualifiers() {
            return qualifiers;
        }

        /**
         * @param qualifiers
         *            the qualifiers to set
         */
        public void setQualifiers(Set<Enum<?>> qualifiers) {
            this.qualifiers = qualifiers;
        }

    }

    /**
     * 
     * @author Georgios Markakis <gwarkx@hotmail.com>
     * @since 15 May 2012
     */
    public final class AdaptedOutput {
        private FROMTYPE     outputObject;
        private Set<Enum<?>> outputQualifiers;

        /**
         * @return the outputObject
         */
        public FROMTYPE getOutputObject() {
            return outputObject;
        }

        /**
         * @param outputObject
         *            the outputObject to set
         */
        public void setOutputObject(FROMTYPE outputObject) {
            this.outputObject = outputObject;
        }

        /**
         * @return the outputQualifiers
         */
        public Set<Enum<?>> getOutputQualifiers() {
            return outputQualifiers;
        }

        /**
         * @param outputQualifiers
         *            the outputQualifiers to set
         */
        public void setOutputQualifiers(Set<Enum<?>> outputQualifiers) {
            this.outputQualifiers = outputQualifiers;
        }
    }
}
