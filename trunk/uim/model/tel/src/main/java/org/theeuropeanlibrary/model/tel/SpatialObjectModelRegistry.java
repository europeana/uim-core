package org.theeuropeanlibrary.model.tel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.tel.spatial.CoordinateQualifier;
import org.theeuropeanlibrary.model.tel.spatial.FeatureClass;
import org.theeuropeanlibrary.model.tel.spatial.IdentifierRelation;
import org.theeuropeanlibrary.model.tel.spatial.NameQualifier;
import org.theeuropeanlibrary.model.tel.spatial.UpdateFromDataSource;

import eu.europeana.uim.common.TKey;

/**
 * Registry holding all known keys for the detailed internal object model of a geographic record.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 31 de Out de 2011
 */
@SuppressWarnings("rawtypes")
public final class SpatialObjectModelRegistry {
    /**
     * named form of a toponym
     */
    public static final TKey<SpatialObjectModelRegistry, String>       NAME         = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "Name",
                                                                                                             String.class);

    /**
     * Country where the toponym is located
     */
    public static final TKey<SpatialObjectModelRegistry, Country>              COUNTRY         = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "country",
                                                                                                             Country.class);

    /**
     * identifiers in the source authority files, or other data sets (for example wikipedia, geonames)
     */
    public static final TKey<SpatialObjectModelRegistry, Identifier>           IDENTIFIER          = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "identifier",
                                                                                                             Identifier.class);

    /**
     * Last update date at the data sources
     */
    public static final TKey<SpatialObjectModelRegistry, UpdateFromDataSource> LAST_UPDATE         = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "last update",
                                                                                                             UpdateFromDataSource.class);
    
    /**
     * geographic feature class
     */
    public static final TKey<SpatialObjectModelRegistry, FeatureClass>                 FEATURE_CLASS                = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "feature class",
                                                                                                             FeatureClass.class);
    
    /**
     * Population 
     */
    public static final TKey<SpatialObjectModelRegistry, Long>       POPULATION         = TKey.register(
    		SpatialObjectModelRegistry.class,
    		"population",
    		Long.class);

    /**
     * A coordinate - longitude or latitude 
     */
    public static final TKey<SpatialObjectModelRegistry, Double>       COORDINATE         = TKey.register(
                                                                                                             SpatialObjectModelRegistry.class,
                                                                                                             "coordinate",
                                                                                                             Double.class);
    
    private static final Map<Class<?>, TKey<?, ?>>                               tKeyClassMap        = new HashMap<Class<?>, TKey<?, ?>>();

    private static final Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>    validQualifiers     = new HashMap<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>();

    static {
    	
        validQualifiers.put(NAME, new ArrayList<Class<? extends Enum<?>>>() {
            {
            	add(NameQualifier.class);
                add(Language.class);
            }
        });
        
        validQualifiers.put(IDENTIFIER, new ArrayList<Class<? extends Enum<?>>>() {
        	{
        		add(IdentifierRelation.class);
        	}
        });
        
        validQualifiers.put(COORDINATE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(CoordinateQualifier.class);
            }
        });
        
        for (Field field : SpatialObjectModelRegistry.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (object instanceof TKey) {
                    TKey k = (TKey)object;
                    if (tKeyClassMap.containsKey(k.getType()))
                        throw new RuntimeException(
                                "Two TKeys for the same class in the Object Model: " +
                                        k.getType().getName());
                    tKeyClassMap.put(k.getType(), k);
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns valid enums for the given key or null. Note null is supposed to happen only for
     * dynamic keys and not for the ones defined in this map, as dynamic keys do not enforce only
     * specific qualifiers.
     * 
     * @param key
     *            typed key for which known qualifiers should be retrieved
     * @return valid enums for the given key or null
     */
    public static List<Class<? extends Enum<?>>> getValidEnums(TKey<?, ?> key) {
        ArrayList<Class<? extends Enum<?>>> ret = validQualifiers.get(key);
        if (ret == null) return new ArrayList<Class<? extends Enum<?>>>(0);
        return ret;
    }

    /**
     * Returns valid {@link TKey} for the given class or null.
     * 
     * @param <T>
     *            value type of tkey and class
     * @param cls
     *            for which class a {@link TKey} should be retrieved
     * @return {@link TKey} or null
     */
    @SuppressWarnings("unchecked")
    public static <T> TKey<SpatialObjectModelRegistry, T> lookup(Class<T> cls) {
        return (TKey<SpatialObjectModelRegistry, T>)tKeyClassMap.get(cls);
    }
}
