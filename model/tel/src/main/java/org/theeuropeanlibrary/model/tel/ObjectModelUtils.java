/* ObjectModelUtils.java - created on Jun 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.KnowledgeOrganizationSystem;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.common.qualifier.PartyRelation;
import org.theeuropeanlibrary.model.common.qualifier.SpatialIdentifierType;
import org.theeuropeanlibrary.model.common.qualifier.SpatialRelation;
import org.theeuropeanlibrary.model.common.qualifier.TemporalRelation;
import org.theeuropeanlibrary.model.common.qualifier.TextRelation;
import org.theeuropeanlibrary.model.common.qualifier.TitleType;
import org.theeuropeanlibrary.model.common.spatial.BoundingBoxReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.GeoReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.subject.Subject;
import org.theeuropeanlibrary.model.common.subject.TitleSubject;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.common.time.HistoricalPeriod;
import org.theeuropeanlibrary.model.common.time.Instant;
import org.theeuropeanlibrary.model.common.time.Period;
import org.theeuropeanlibrary.model.common.time.Temporal;
import org.theeuropeanlibrary.model.common.time.TemporalTextualExpression;
import org.theeuropeanlibrary.translation.Translations;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;

/**
 * Provides convenient utility function to retrieve places, parties and temporal objects despite the
 * actual derived class.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Ruud Diterwich (ruud@diterwich.com)
 * @since Jun 10, 2011
 */
public final class ObjectModelUtils {
    /**
     * Private constructor as this is a utility function class.
     */
    private ObjectModelUtils() {
        // nothing todo
    }

    /**
     * Converts a collection of qualified values to a list of values.
     * 
     * @param <T>
     * @param qualifiedValues
     * @param unique
     * @return values
     */
    public static <T> List<T> toValues(Collection<QualifiedValue<? extends T>> qualifiedValues,
            boolean unique) {
        List<T> values = new ArrayList<T>(qualifiedValues.size());
        for (QualifiedValue<? extends T> value : qualifiedValues) {
            if (unique) {
                if (!values.contains(value.getValue())) {
                    values.add(value.getValue());
                }
            } else {
                values.add(value.getValue());
            }
        }
        return values;
    }

    /**
     * @param <E>
     * @param value
     * @param qualifierType
     * @return qualifier value, or null
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<?>> E getQualifier(QualifiedValue<?> value, Class<E> qualifierType) {
        for (Enum<?> qualifier : value.getQualifiers()) {
            if (qualifierType.isInstance(qualifier)) { return (E)qualifier; }
        }
        return null;
    }

    /**
     * @param record
     * @param qualifiers
     * @return places
     */
    public static List<NamedPlace> getPlaces(MetaDataRecord<?> record, Enum<?>... qualifiers) {
        return toValues(getQualifiedPlaces(record, qualifiers), true);
    }

    /**
     * @param record
     * @param qualifiers
     * @return all kinds of places onto record
     */
    public static List<QualifiedValue<? extends NamedPlace>> getQualifiedPlaces(
            MetaDataRecord<?> record, Enum<?>... qualifiers) {
        List<QualifiedValue<? extends NamedPlace>> result = new ArrayList<MetaDataRecord.QualifiedValue<? extends NamedPlace>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PLACE, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_PLACE, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_BOX_PLACE, qualifiers));
        Collections.sort(result);
        return result;
    }

    /**
     * @param record
     * @param qualifiers
     * @return Spatial entities
     */
    public static List<QualifiedValue<? extends SpatialEntity>> getQualifiedSpatials(
            MetaDataRecord<?> record, Enum<?>... qualifiers) {
        List<QualifiedValue<? extends SpatialEntity>> result = new ArrayList<MetaDataRecord.QualifiedValue<? extends SpatialEntity>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PLACE, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_PLACE, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_BOX_PLACE, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEOGRAPHIC_ENTITY, qualifiers));
        Collections.sort(result);
        return result;
    }

    /**
     * @param record
     * @param qualifiers
     * @return Spatial entities
     */
    public static List<? extends SpatialEntity> getSpatials(MetaDataRecord<?> record,
            Enum<?>... qualifiers) {
        return toValues(getQualifiedSpatials(record, qualifiers), true);
    }

    /**
     * @param record
     * @param qualifiers
     * @return places
     */
    public static List<Party> getParties(MetaDataRecord<?> record, Enum<?>... qualifiers) {
        return toValues(getQualifiedParties(record, qualifiers), true);
    }

    /**
     * Returns parties that are creators or contributors, ordered
     * 
     * @param record
     * @param qualifiers
     * @return places
     */
    public static List<Party> getPartiesIntelectuallyResponsible(MetaDataRecord<?> record,
            Enum<?>... qualifiers) {
        return toValues(getQualifiedPartiesIntelectuallyResponsible(record, qualifiers), true);
    }

    /**
     * Returns parties, ordered
     * 
     * @param record
     * @param qualifiers
     * @return all kinds of parties onto record
     */
    public static List<QualifiedValue<? extends Party>> getQualifiedParties(
            MetaDataRecord<?> record, Enum<?>... qualifiers) {
        List<QualifiedValue<? extends Party>> result = new ArrayList<MetaDataRecord.QualifiedValue<? extends Party>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PERSON, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.MEETING, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.FAMILY, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.ORGANIZATION, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PARTY, qualifiers));
        Collections.sort(result);
        return result;
    }

    /**
     * Returns parties that are creators or contributors, ordered
     * 
     * @param record
     * @param qualifiers
     * @return all kinds of parties onto record
     */
    public static List<QualifiedValue<? extends Party>> getQualifiedPartiesIntelectuallyResponsible(
            MetaDataRecord<?> record, Enum<?>... qualifiers) {
        List<QualifiedValue<? extends Party>> result = new ArrayList<MetaDataRecord.QualifiedValue<? extends Party>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PERSON, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.MEETING, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.FAMILY, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.ORGANIZATION, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PARTY, qualifiers));
        Collections.sort(result);
        for (Iterator<QualifiedValue<? extends Party>> it = result.iterator(); it.hasNext();) {
            QualifiedValue<? extends Party> p = it.next();
            PartyRelation pRel = p.getQualifier(PartyRelation.class);
            if (!(pRel != null && (pRel == PartyRelation.CREATOR || pRel == PartyRelation.CONTRIBUTOR)))
                it.remove();
        }
        return result;
    }

    /**
     * @param record
     * @param qualifiers
     * @return places
     */
    public static List<Temporal> getTemporals(MetaDataRecord<?> record, Enum<?>... qualifiers) {
        return toValues(getQualifiedTemporals(record, qualifiers), true);
    }

    /**
     * Returns temporals, ordered
     * 
     * @param record
     * @param qualifiers
     * @return all kinds of temporals onto record
     */
    public static List<QualifiedValue<? extends Temporal>> getQualifiedTemporals(
            MetaDataRecord<?> record, Enum<?>... qualifiers) {
        List<QualifiedValue<? extends Temporal>> result = new ArrayList<MetaDataRecord.QualifiedValue<? extends Temporal>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.INSTANT, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PERIOD, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TIME_TEXTUAL, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TEMPORAL, qualifiers));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.HISTORICAL_PERIOD, qualifiers));
        Collections.sort(result);
        return result;
    }

    /**
     * 
     * @param record
     * @return subject objects
     */
    public static List<QualifiedValue<?>> getSubjects(MetaDataRecord<?> record) {
        List<QualifiedValue<?>> result = new ArrayList<MetaDataRecord.QualifiedValue<?>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.INSTANT,
                TemporalRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PERIOD,
                TemporalRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TIME_TEXTUAL,
                TemporalRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.HISTORICAL_PERIOD,
                TemporalRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TITLE, TitleType.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TOPIC));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PARTY, PartyRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PERSON, PartyRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.MEETING, PartyRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.FAMILY, PartyRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.ORGANIZATION,
                PartyRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_PLACE,
                SpatialRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEO_BOX_PLACE,
                SpatialRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.PLACE, SpatialRelation.SUBJECT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.GEOGRAPHIC_ENTITY,
                SpatialRelation.SUBJECT));
        Collections.sort(result);
        return result;
    }

    /**
     * 
     * @param record
     * @return description objects
     */
    public static List<QualifiedValue<?>> getDescriptions(MetaDataRecord<?> record) {
        List<QualifiedValue<?>> result = new ArrayList<MetaDataRecord.QualifiedValue<?>>();
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TEXT, TextRelation.DESCRIPTION));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TEXT, TextRelation.ABSTRACT));
        result.addAll(record.getQualifiedValues(ObjectModelRegistry.TEXT,
                TextRelation.TABLE_OF_CONTENTS));
        Collections.sort(result);
        return result;
    }

    /**
     * @param party
     * @return Id's of records
     */
    public static List<Long> getAuthorityParties(Party party) {
        List<Long> result = new ArrayList<Long>();
        for (Identifier id : party.getIdentifiers()) {
            if (id.getScope().equals(KnowledgeOrganizationSystem.TEL.toString())) {
                result.add(Long.parseLong(id.getIdentifier()));
            }
        }
        return result;
    }
    
    /**
     * @param subjectValue
     * @return subject string
     */
    public static String displaySubject(QualifiedValue<?> subjectValue) {
        return displaySubject(subjectValue, ", ");
    }

    /**
     * @param subjectValue
     * @return subject string
     */
    public static String displaySubjectHeading(QualifiedValue<?> subjectValue) {
        return displaySubject(subjectValue, " -- ");
    }
    
    /**
     * 
     * @param subject
     *            subject, may be null
     * @return display string, or null
     */
    public static String displaySubject(Subject subject) {
        return displaySubject(subject, ", ");
    }

    /**
     * 
     * @param subject
     *            subject, may be null
     * @return display string, or null
     */
    public static String displaySubjectHeading(Subject subject) {
        return displaySubject(subject, " -- ");
    }

    /**
     * @param subjectValue
     * @return subject string
     */
    private static String displaySubject(QualifiedValue<?> subjectValue, String separator) {
        if (subjectValue.getValue() instanceof Temporal) {
            Temporal temporal = (Temporal)subjectValue.getValue();

            String subject = displaySubject(temporal.getSubject(), separator);
            if (subject != null && !subject.isEmpty()) { return temporal.toString() + separator +
                                                                subject; }
            return temporal.toString();

        } else if (subjectValue.getValue() instanceof Title) {
            Title title = (Title)subjectValue.getValue();

            String subject = displaySubject(title.getTitleSubject(), separator);
            if (subject != null && !subject.isEmpty()) { return title.toString() + separator + subject; }
            return title.toString();
        } else if (subjectValue.getValue() instanceof Topic) {
            Topic topic = (Topic)subjectValue.getValue();
            return displaySubject(topic, separator);

        } else if (subjectValue.getValue() instanceof Party) {
            Party party = (Party)subjectValue.getValue();

            String subject = displaySubject(party.getSubject(), separator);
            if (subject != null && !subject.isEmpty()) { return party.toString() + separator + subject; }
            return party.toString();

        } else if (subjectValue.getValue() instanceof SpatialEntity) {
            SpatialEntity spatialEntity = (SpatialEntity)subjectValue.getValue();

            String subject = displaySubject(spatialEntity.getSubject(), separator);
            if (subject != null && !subject.isEmpty()) { return spatialEntity.toString() + separator +
                                                                subject; }
            return spatialEntity.toString();
        }
        return "";
    }

    /**
     * 
     * @param subject
     *            subject, may be null
     * @return display string, or null
     */
    private static String displaySubject(Subject subject, String separator) {
        if (subject == null) { return null; }
        if (subject instanceof TitleSubject) {
            TitleSubject titleSubject = (TitleSubject)subject;
            return StringUtils.join(
                    filterEmpty(subject.getFormSubdivision(), subject.getGeneralSubdivision(),
                            subject.getChronologicalSubdivision(),
                            subject.getGeographicSubdivision(), titleSubject.getTitleDates(),
                            titleSubject.getMiscellaneousInformation()), separator);
        }
        if (subject instanceof Topic) {
            Topic topic = (Topic)subject;
            return StringUtils.join(
                    filterEmpty(topic.getTopicName(), topic.getTopicDescription(),
                            topic.getSecondTopicTerm(), topic.getLocationOfEvent(),
                            topic.getActiveDates(), subject.getFormSubdivision(),
                            subject.getGeneralSubdivision(), subject.getChronologicalSubdivision(),
                            subject.getGeographicSubdivision()), separator);
        }
        return StringUtils.join(
                filterEmpty(subject.getFormSubdivision(), subject.getGeneralSubdivision(),
                        subject.getChronologicalSubdivision(), subject.getGeographicSubdivision()),
                        separator);
    }

    /**
     * @param record
     * @param preferredLanguage
     * @return display string with publish information about the record
     */
    public static String displayPublisher(MetaDataRecord<?> record, Language preferredLanguage) {
        // places
        String publisher = StringUtils.join(getPlaces(record, SpatialRelation.PUBLICATION), ",");

        // publishers and times
        List<Object> partiesAndTemporals = new ArrayList<Object>();
        partiesAndTemporals.addAll(getParties(record, PartyRelation.PUBLISHER));

        List<Temporal> temporals = null;

        if (preferredLanguage != null) {
            // try to get
            temporals = getTemporals(record, TemporalRelation.PUBLICATION, preferredLanguage);

            if (temporals.isEmpty()) {
                temporals = getTemporals(record, TemporalRelation.CREATION, preferredLanguage);
            }

            if (temporals.isEmpty()) {
                temporals = getTemporals(record, TemporalRelation.CREATION_OR_AVAILABILITY,
                        preferredLanguage);
            }
        }

        if (temporals == null || temporals.isEmpty()) {
            temporals = getTemporals(record, TemporalRelation.PUBLICATION);
        }

        if (temporals.isEmpty()) {
            temporals = getTemporals(record, TemporalRelation.CREATION);

        }

        if (temporals.isEmpty()) {
            temporals = getTemporals(record, TemporalRelation.CREATION_OR_AVAILABILITY);
        }

        partiesAndTemporals.addAll(temporals);

        String parties = StringUtils.join(partiesAndTemporals, ", ");
        if (publisher.length() > 0) {
            if (parties.length() > 0) {
                publisher += ": " + parties;
            }
        } else {
            if (parties.length() > 0) publisher = parties;
        }
        return publisher;
    }

    /**
     * @param qualifier
     *            qualifier, or null
     * @param locale
     * @return display name for qualifier, never null
     */
    public static String display(Enum<?> qualifier, Locale locale) {
        return Translations.getTranslation("qualifier." +
                                           (qualifier != null ? qualifier.toString().toLowerCase()
                                                   : "unknown"), locale);
    }

    /**
     * @param <I>
     * @param values
     * @return the filtered list (no empty and null values)
     */
    public static <I> List<I> filterEmpty(I... values) {
        List<I> result = new ArrayList<I>();
        for (I i : values) {
            if (i != null && !i.toString().isEmpty()) {
                result.add(i);
            }
        }

        return result;
    }

    /**
     * Changes all links in the MDR to be served by the data portal
     * 
     * @param mdr
     */
    public static void setupWebResourcesLinks(MetaDataRecord<?> mdr) {
        setupWebResourcesLinks("http://data.theeuropeanlibrary.org", null, mdr);
    }

    /**
     * Changes the urls in Link objects to be served by redirection through the data portal. This is
     * necessary for the RDF representation of data in LOD and EDM.
     * 
     * @param baseUrl
     * @param source
     * @param mdr
     */
    public static void setupWebResourcesLinks(String baseUrl, String source, MetaDataRecord<?> mdr) {
        setupWebResourcesLinksForLod(baseUrl, mdr);
// List<QualifiedValue<Link>> catlinks = mdr.getQualifiedValues(ObjectModelRegistry.LINK);
// mdr.deleteValues(ObjectModelRegistry.LINK,
// LinkTarget.DIGITAL_OBJECT);
// mdr.deleteValues(ObjectModelRegistry.LINK,
// LinkTarget.THUMBNAIL);
// for (int index = 0; index < catlinks.size(); index++) {
// mdr.addValue(ObjectModelRegistry.LINK,
// new Link(baseUrl + "/WebResource/" + mdr.getId() +
// "/" + index + "-" + getLinkHash(catlinks.get(index))+ (source==null? "" : "?source=" +
// source) ),
// catlinks.get(index).getQualifier(LinkTarget.class));
// }
    }

    /**
     * @param baseUrl
     * @param mdr
     */
    public static void setupWebResourcesLinksForLod(String baseUrl, MetaDataRecord<?> mdr) {
        List<QualifiedValue<Link>> catlinks = mdr.getQualifiedValues(ObjectModelRegistry.LINK);
        mdr.deleteValues(ObjectModelRegistry.LINK, LinkTarget.DIGITAL_OBJECT);
        mdr.deleteValues(ObjectModelRegistry.LINK, LinkTarget.THUMBNAIL);
        for (int index = 0; index < catlinks.size(); index++) {
            mdr.addValue(ObjectModelRegistry.LINK,
                    new Link(getWebResourceLinkForLod(baseUrl, catlinks.get(index), index)),
                    catlinks.get(index).getQualifier(LinkTarget.class));
        }
    }

    /**
     * @param baseUrl
     * @param catlinks
     * @param index
     * @return
     */
    private static String getWebResourceLinkForLod(String baseUrl, QualifiedValue<Link> catlinks,
            int index) {
        return baseUrl + "#webresource" + index +
               Integer.toHexString(catlinks.getValue().getUrl().hashCode());
    }

    /**
     * @param mdr
     * @return list of countries of publication
     */
    public static List<Country> getCountriesOfPublication(MetaDataRecord<?> mdr) {
        List<Country> countries = new ArrayList<Country>();

        List<QualifiedValue<SpatialEntity>> qualifiedValues = mdr.getQualifiedValues(
                ObjectModelRegistry.GEOGRAPHIC_ENTITY, SpatialRelation.PUBLICATION);
        for (QualifiedValue<SpatialEntity> spEntity : qualifiedValues) {
            for (Identifier id : spEntity.getValue().getIdentifiers()) {
                if (id.getScope() != null &&
                    id.getScope().equals(SpatialIdentifierType.ISO3166.name())) {
                    String code = null;
                    if (id.getIdentifier().length() <= 2)
                        code = id.getIdentifier();
                    else if (id.getIdentifier().length() >= 5 &&
                             id.getIdentifier().charAt(2) == '-')
                        code = id.getIdentifier().substring(3, 5);
                    if (code != null) {
                        Country iso = Country.getByIso2(code);
                        if (iso != null && !countries.contains(iso)) countries.add(iso);
                        break;
                    }
                }

            }
        }
        return countries;
    }

    /**
     * @param mdr
     * @param agent
     * @param qualifiers
     */
    public static void addValue(MetaDataRecord<?> mdr, Party agent, Enum<?>... qualifiers) {
        if (agent.getClass().equals(Party.class))
            mdr.addValue(ObjectModelRegistry.PARTY, agent, qualifiers);
        else if (agent.getClass().equals(Person.class))
            mdr.addValue(ObjectModelRegistry.PERSON, (Person)agent, qualifiers);
        else if (agent.getClass().equals(Organization.class))
            mdr.addValue(ObjectModelRegistry.ORGANIZATION, (Organization)agent, qualifiers);
        else if (agent.getClass().equals(Meeting.class))
            mdr.addValue(ObjectModelRegistry.MEETING, (Meeting)agent, qualifiers);
        else if (agent.getClass().equals(Family.class))
            mdr.addValue(ObjectModelRegistry.FAMILY, (Family)agent, qualifiers);
        else
            throw new RuntimeException(agent.getClass().getName());
    }

    /**
     * @param mdr
     * @param spatial
     * @param qualifiers
     */
    public static void addValue(MetaDataRecord<?> mdr, SpatialEntity spatial, Enum<?>... qualifiers) {
        if (spatial.getClass().equals(SpatialEntity.class))
            mdr.addValue(ObjectModelRegistry.GEOGRAPHIC_ENTITY, spatial, qualifiers);
        else if (spatial.getClass().equals(NamedPlace.class))
            mdr.addValue(ObjectModelRegistry.PLACE, (NamedPlace)spatial, qualifiers);
        else if (spatial.getClass().equals(GeoReferencedPlace.class))
            mdr.addValue(ObjectModelRegistry.GEO_PLACE, (GeoReferencedPlace)spatial, qualifiers);
        else if (spatial.getClass().equals(BoundingBoxReferencedPlace.class))
            mdr.addValue(ObjectModelRegistry.GEO_BOX_PLACE, (BoundingBoxReferencedPlace)spatial,
                    qualifiers);
        else
            throw new RuntimeException(spatial.getClass().getName());
    }

    /**
     * @param mdr
     * @param tempo
     * @param qualifiers
     */
    public static void addValue(MetaDataRecord<?> mdr, Temporal tempo, Enum<?>... qualifiers) {
        if (tempo.getClass().equals(Period.class))
            mdr.addValue(ObjectModelRegistry.PERIOD, (Period)tempo, qualifiers);
        else if (tempo.getClass().equals(Instant.class))
            mdr.addValue(ObjectModelRegistry.INSTANT, (Instant)tempo, qualifiers);
        else if (tempo.getClass().equals(TemporalTextualExpression.class))
            mdr.addValue(ObjectModelRegistry.TEMPORAL, tempo, qualifiers);
        else if (tempo.getClass().equals(HistoricalPeriod.class))
            mdr.addValue(ObjectModelRegistry.HISTORICAL_PERIOD, (HistoricalPeriod)tempo, qualifiers);
        else
            throw new RuntimeException(tempo.getClass().getName());
    }

}
