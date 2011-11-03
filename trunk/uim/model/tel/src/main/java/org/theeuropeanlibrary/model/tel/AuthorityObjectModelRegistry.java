package org.theeuropeanlibrary.model.tel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.KnowledgeOrganizationSystem;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.tel.authority.Coordinates;
import org.theeuropeanlibrary.model.tel.authority.FeatureClass;
import org.theeuropeanlibrary.model.tel.authority.Occurrences;
import org.theeuropeanlibrary.model.tel.authority.OrganizationNameForm;
import org.theeuropeanlibrary.model.tel.authority.PersonNameForm;
import org.theeuropeanlibrary.model.tel.authority.SpatialIdentifierRelation;
import org.theeuropeanlibrary.model.tel.authority.SpatialNameQualifier;
import org.theeuropeanlibrary.model.tel.authority.UpdateFromDataSource;
import org.theeuropeanlibrary.model.tel.qualifier.AuthorityLinkTarget;
import org.theeuropeanlibrary.model.tel.qualifier.DisambiguationDataType;
import org.theeuropeanlibrary.model.tel.qualifier.NameFormRelation;

import eu.europeana.uim.common.TKey;

/**
 * Registry holding all known keys for the detailed internal object model of metadata on a record.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
// @SuppressWarnings("rawtypes")
@SuppressWarnings("rawtypes")
public final class AuthorityObjectModelRegistry {
    /**
     * named form of a person
     */
    public static final TKey<AuthorityObjectModelRegistry, PersonNameForm>       PERSON_FORM         = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "person name form",
                                                                                                             PersonNameForm.class);
    /**
     * named form of an organization
     */
    public static final TKey<AuthorityObjectModelRegistry, OrganizationNameForm> ORGANIZATION_FORM   = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "organization name form",
                                                                                                             OrganizationNameForm.class);

    /**
     * Nationalities of the entity
     */
    public static final TKey<AuthorityObjectModelRegistry, Country>              NATIONALITY         = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "nationality",
                                                                                                             Country.class);

    /**
     * identifiers in the source authority files, or other data sets (for example wikipedia)
     */
    public static final TKey<AuthorityObjectModelRegistry, Identifier>           IDENTIFIER          = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "identifier",
                                                                                                             Identifier.class);

    /**
     * Data used for disambiguation of parties (such as titles, publishers, isbns, coauthors, etc)
     */
    public static final TKey<AuthorityObjectModelRegistry, Occurrences>          DISAMBIGUATION_DATA = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "disambiguation data",
                                                                                                             Occurrences.class);

    /**
     * Last update date at the data sources
     */
    public static final TKey<AuthorityObjectModelRegistry, UpdateFromDataSource> LAST_UPDATE         = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "last update",
                                                                                                             UpdateFromDataSource.class);

    /**
     * links to external resources like Wikipedia
     */
    public static final TKey<AuthorityObjectModelRegistry, Link>                 LINK                = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "link",
                                                                                                             Link.class);

    /**
     * authority topics
     */
    public static final TKey<AuthorityObjectModelRegistry, Topic>                TOPIC               = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "topic",
                                                                                                             Topic.class);

    /**
     * named form of a geographic entity
     */
    public static final TKey<AuthorityObjectModelRegistry, String>               STRING              = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "string",
                                                                                                             String.class);
    /**
     * geographic feature class
     */
    public static final TKey<AuthorityObjectModelRegistry, FeatureClass>         FEATURE_CLASS       = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "feature class",
                                                                                                             FeatureClass.class);

    /**
     * For spatial records - Population
     */
    public static final TKey<AuthorityObjectModelRegistry, Long>                 POPULATION          = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "population",
                                                                                                             Long.class);

    /**
     * For spatial records - A coordinate - longitude or latitude
     */
    public static final TKey<AuthorityObjectModelRegistry, Coordinates>          COORDINATES         = TKey.register(
                                                                                                             AuthorityObjectModelRegistry.class,
                                                                                                             "coordinates",
                                                                                                             Coordinates.class);

    private static final Map<Class<?>, TKey<?, ?>>                               tKeyClassMap        = new HashMap<Class<?>, TKey<?, ?>>();

    private static final Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>    validQualifiers     = new HashMap<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>();

    static {
        validQualifiers.put(PERSON_FORM, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(NameFormRelation.class);
            }
        });
        validQualifiers.put(ORGANIZATION_FORM, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(NameFormRelation.class);
            }
        });
        validQualifiers.put(DISAMBIGUATION_DATA, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(DisambiguationDataType.class);
            }
        });
        validQualifiers.put(IDENTIFIER, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(SpatialIdentifierRelation.class);
                add(KnowledgeOrganizationSystem.class);
            }
        });
        validQualifiers.put(LINK, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(AuthorityLinkTarget.class);
            }
        });
        validQualifiers.put(TOPIC, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(KnowledgeOrganizationSystem.class);
            }
        });

        validQualifiers.put(STRING, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(SpatialNameQualifier.class);
                add(Language.class);
            }
        });

        for (Field field : AuthorityObjectModelRegistry.class.getDeclaredFields()) {
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
    public static <T> TKey<AuthorityObjectModelRegistry, T> lookup(Class<T> cls) {
        return (TKey<AuthorityObjectModelRegistry, T>)tKeyClassMap.get(cls);
    }
}
