package eu.europeana.uim.store;

import java.util.Date;
/*
    collection  // ref to Collection obj
    created_time    // time stamp creation

    status      // enum     REQS_INIT     just created, under construction
                //          REQS_IMPORTED all related mdrs are referenced & created
                //          REQS_ABORTED  processing of this request aborted due to error
                //          REQS_PENDING_SIGNOFF   waiting for aproval
                //          REQS_ACCEPTED          aproved for production  only one req/collection can be in this state
                //          REQS_PRODUCTION        sent to production      only one req/collection can be in this state
    aproved_user    // User that aproved this request
    aproved_time    // time stamp aproval

    submitted_time  // time stamp, sent to production


    err_msg         // If in REQS_ABORTED state, explanation of what whent wrong

 */
public interface Request  extends DataSet {

	public Collection getCollection();
	public Date getDate();
	
}
