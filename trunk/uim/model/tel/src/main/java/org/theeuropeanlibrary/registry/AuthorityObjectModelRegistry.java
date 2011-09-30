package org.theeuropeanlibrary.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.model.Identifier;
import org.theeuropeanlibrary.model.Link;
import org.theeuropeanlibrary.model.authority.Occurrences;
import org.theeuropeanlibrary.model.authority.OrganizationNameForm;
import org.theeuropeanlibrary.model.authority.PersonNameForm;
import org.theeuropeanlibrary.model.authority.UpdateFromDataSource;
import org.theeuropeanlibrary.qualifier.AuthorityIdentifierType;
import org.theeuropeanlibrary.qualifier.AuthorityLinkTarget;
import org.theeuropeanlibrary.qualifier.Country;
import org.theeuropeanlibrary.qualifier.DisambiguationDataType;
import org.theeuropeanlibrary.qualifier.NameFormRelation;

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
                add(AuthorityIdentifierType.class);
            }
        });
        validQualifiers.put(LINK, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(AuthorityLinkTarget.class);
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
