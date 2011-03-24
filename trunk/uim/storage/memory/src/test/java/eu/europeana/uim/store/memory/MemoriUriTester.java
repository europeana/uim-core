package eu.europeana.uim.store.memory;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.Uri;

import java.util.Date;

/**
 * Copyright 2007 EDL FOUNDATION
 * <p/>
 * Licensed under the EUPL, Version 1.0 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * <p/>
 * http://ec.europa.eu/idabc/eupl
 * <p/>
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 * <p/>
 * User: jaclu
 * Date: 23-03-11
 * Time: 13:27
 */
public class MemoriUriTester {
    public static void main(String[] args) throws StorageEngineException {
        MemoryStorageEngine mse = new MemoryStorageEngine();
        mse.initialize();

        Provider p = mse.createProvider();
        Collection collection = mse.createCollection(p);
        Request r = mse.createRequest(collection, new Date(0));

        MetaDataRecord mdr = mse.createMetaDataRecord(r, "sune");

        Uri u = mse.createUri("http://www.sunet.se/", Uri.ItemType.ISSHOWNAT, r, mdr);

        System.out.println("uri is:" + u.getStatus());
     }
}
