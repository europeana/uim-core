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

package eu.europeana.uim.sugarcrmclient.plugin.objects.queries;

import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.SugarCrmField;

/**
 * 
 * 
 * @author Georgios Markakis
 */
public class ComplexSugarCrmQuery extends SimpleSugarCrmQuery {

	private StringBuffer queryBuffer;
	
	
	public ComplexSugarCrmQuery(SugarCrmField field ,EqOp op, String value ){
		queryBuffer = new StringBuffer();
		
		queryBuffer.append("opportunities.");
		queryBuffer.append(field.getFieldId());
		queryBuffer.append(" ");
		queryBuffer.append(op.getValue());
		queryBuffer.append(" '");
		queryBuffer.append(value);
		queryBuffer.append("' ");
	}
	
	private ComplexSugarCrmQuery(ComplexSugarCrmQuery query,String logicalOperator 
			,SugarCrmField field ,EqOp op, String value ){	
		queryBuffer = new StringBuffer();

		queryBuffer.append(logicalOperator);
		queryBuffer.append(" ");
		queryBuffer.append("opportunities.");
		queryBuffer.append(field.getFieldId());
		queryBuffer.append(" ");
		queryBuffer.append(op.getValue());
		queryBuffer.append(" '");
		queryBuffer.append(value);
		queryBuffer.append("' ");
	}
	
	public ComplexSugarCrmQuery and(SugarCrmField field ,EqOp op, String value ){
		return new ComplexSugarCrmQuery(this,"AND",field,op,value );
	}
	
	public ComplexSugarCrmQuery or(SugarCrmField field ,EqOp op, String value ){
		return new ComplexSugarCrmQuery(this,"OR",field,op,value );
	}

	/**
	 * @param queryBuffer the queryBuffer to set
	 */
	public void setQueryBuffer(StringBuffer queryBuffer) {
		this.queryBuffer = queryBuffer;
	}

	/**
	 * @return the queryBuffer
	 */
	public StringBuffer getQueryBuffer() {
		return queryBuffer;
	}


	@Override
	public String toString(){
		String requestQuery = "(" + queryBuffer.toString() + ")";
		
		return requestQuery;
	}
	
}
