/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they
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

package eu.europeana.uim.store;

/**
 * @author Jacob Lundqvist <jacob.lundqvist@kb.nl>
 */
public interface Uri {
    enum ItemType { OBJECT, ISSHOWNBY, ISSHOWNAT }
    enum Status {
        CREATED,

        //
        // Intermediate states during processing
        //
        VERIFIED,  // link is ok

        // thumbnail related states
        ORG_SAVED, FULL_GENERATED, BRIEF_GENERATED, TINY_GENERATED,

        //
        // Final states
        //
        COMPLETED, // item check is complete
        FAILED     // item had an error, examine errCode for details
    }
    enum ErrCode {
        NO_ERROR,
        URL_ERROR, HTTP_ERROR, HTML_ERROR, NO_RESPONSE, TIMEOUT,
        WAS_HTML_PAGE_ERROR, OTHER_ERROR,

        // thumbnail related states
        MIMETYPE_ERROR,
        UNSUPORTED_MIMETYPE_ERROR,
        DOWNLOAD_FAILED,
        WRONG_FILESIZE,
        FILE_STORAGE_FAILED,
        OBJ_CONVERT_ERROR,
        UNRECOGNIZED_FORMAT,
    }



	public long getId();
}
