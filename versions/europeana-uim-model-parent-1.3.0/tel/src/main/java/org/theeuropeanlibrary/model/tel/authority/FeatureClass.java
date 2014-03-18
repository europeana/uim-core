package org.theeuropeanlibrary.model.tel.authority;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for the GeoNames feature classes A,H,L,P,R,S,T,U,V
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 31 de Out de 2011
 */
public enum FeatureClass {
	/**
	 * Administrative Boundary Features
	 */
	ADMINISTRATIVE_BOUNDARY,
	/**
	 * Hydrographic Features
	 */
	HYDROGRAPHIC,
	/**
	 * Area Features
	 */
	AREA,
	/**
	 * Populated Place Features
	 */
	POPULATED_PLACE,
	/**
	 * Road / Railroad Features
	 */
	ROAD_RAILROAD,
	/**
	 * Spot Features
	 */
	SPOT,
	/**
	 * Hypsographic Features
	 */
	HYPSOGRAPHIC,
	/**
	 * Undersea Features
	 */
	UNDERSEA,
	/**
	 * Vegetation Features
	 */
	V;

	
	private static Map<String, FeatureClass> GEONAMES_CODE_MAP=new HashMap<String, FeatureClass>(){{
		put("A", ADMINISTRATIVE_BOUNDARY);
		put("H", HYDROGRAPHIC);
		put("L", AREA);
		put("P", POPULATED_PLACE);
		put("R", ROAD_RAILROAD);
		put("S", SPOT);
		put("T", HYPSOGRAPHIC);
		put("U", UNDERSEA);
		put("V", V);
	}};
	
	
	/**
	 * Creates an instance from a Geonames Feature Class Code
	 * 
	 * @param value
	 * @return FeatureClass
	 */
	public static FeatureClass fromGeonamesValue(String value) {
		return GEONAMES_CODE_MAP.get(value);
	}
	

	/**
	 * Creates an instance from a Geonames Feature Class Code in a URI
	 * 
	 * @param uri
	 * @return FeatureClass
	 */
	public static FeatureClass fromGeonamesUriValue(String uri) {
		String featCode = uri.substring(uri.lastIndexOf("#")+1);
		if(featCode.equals("null"))
			return null;
		return fromGeonamesValue(featCode);
	}

}
