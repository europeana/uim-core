package org.theeuropeanlibrary.model.tel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.FullText;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Numbering;
import org.theeuropeanlibrary.model.common.Text;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.qualifier.CertaintyLevel;
import org.theeuropeanlibrary.model.common.qualifier.ContributionType;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.IdentifierType;
import org.theeuropeanlibrary.model.common.qualifier.KnowledgeOrganizationSystem;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.common.qualifier.LanguageRelation;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.common.qualifier.NumberingRelation;
import org.theeuropeanlibrary.model.common.qualifier.PartyIdentifierType;
import org.theeuropeanlibrary.model.common.qualifier.PartyRelation;
import org.theeuropeanlibrary.model.common.qualifier.ResourceRelation;
import org.theeuropeanlibrary.model.common.qualifier.ResourceType;
import org.theeuropeanlibrary.model.common.qualifier.SpatialIdentifierType;
import org.theeuropeanlibrary.model.common.qualifier.SpatialRelation;
import org.theeuropeanlibrary.model.common.qualifier.Status;
import org.theeuropeanlibrary.model.common.qualifier.TemporalRelation;
import org.theeuropeanlibrary.model.common.qualifier.TextRelation;
import org.theeuropeanlibrary.model.common.qualifier.TitleType;
import org.theeuropeanlibrary.model.common.spatial.BoundingBoxReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.GeoReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.common.time.HistoricalPeriod;
import org.theeuropeanlibrary.model.common.time.Instant;
import org.theeuropeanlibrary.model.common.time.Period;
import org.theeuropeanlibrary.model.common.time.Temporal;
import org.theeuropeanlibrary.model.common.time.TemporalTextualExpression;
import org.theeuropeanlibrary.model.tel.cluster.Hash;
import org.theeuropeanlibrary.model.tel.cluster.Partition;
import org.theeuropeanlibrary.model.tel.cluster.Redirect;
import org.theeuropeanlibrary.model.tel.qualifier.Audience;
import org.theeuropeanlibrary.model.tel.qualifier.BibliographicLevel;
import org.theeuropeanlibrary.model.tel.qualifier.CatalogingForm;
import org.theeuropeanlibrary.model.tel.qualifier.ContextLevel;
import org.theeuropeanlibrary.model.tel.qualifier.DigitalObjectTarget;
import org.theeuropeanlibrary.model.tel.qualifier.FieldScope;
import org.theeuropeanlibrary.model.tel.qualifier.FieldSource;
import org.theeuropeanlibrary.model.tel.qualifier.FormOfItem;
import org.theeuropeanlibrary.model.tel.qualifier.HashType;
import org.theeuropeanlibrary.model.tel.qualifier.Illustrations;
import org.theeuropeanlibrary.model.tel.qualifier.MaterialType;
import org.theeuropeanlibrary.model.tel.qualifier.Maturity;
import org.theeuropeanlibrary.model.tel.qualifier.NoteType;
import org.theeuropeanlibrary.model.tel.qualifier.PartitionType;
import org.theeuropeanlibrary.model.tel.qualifier.PrintType;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Registry holding all known keys for the detailed internal object model of metadata on a record.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
@SuppressWarnings("all")
public final class ObjectModelRegistry {
    public static final String                                                                 XML_NAMESPACE       = "http://theeuropeanlibrary.org/internal_object_model";

    // FIXME: new way of conversion
    @FieldId(10)
    public static final TKey<ObjectModelRegistry, MetaDataRecordBean>                          METADATARECORD      = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "metadatarecord",
                                                                                                                           MetaDataRecordBean.class);

    /**
     * field keys lookup to an integer value for authority information
     */
    public static Map<TKey<?, ?>, Integer>                                                     mdrTkeyFieldId;
    /**
     * integer value to field keys lookup for authority information
     */
    public static Map<Integer, TKey<?, ?>>                                                     mdrFieldIdTkey;

    static {
        mdrTkeyFieldId = new HashMap<TKey<?, ?>, Integer>();
        mdrFieldIdTkey = new HashMap<Integer, TKey<?, ?>>();
        
        mdrTkeyFieldId.put(METADATARECORD, 10);
        mdrFieldIdTkey.put(10, METADATARECORD);
    }

    @FieldId(2)
    public static final TKey<ObjectModelRegistry, Provenance>                                  PROVENANCE          = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "provenance",
                                                                                                                           Provenance.class);

    @FieldId(3)
    public static final TKey<ObjectModelRegistry, Partition>                                   PARTITION           = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "partition",
                                                                                                                           Partition.class);

    @FieldId(4)
    public static final TKey<ObjectModelRegistry, Hash>                                        HASH                = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "hash",
                                                                                                                           Hash.class);

    @FieldId(5)
    public static final TKey<ObjectModelRegistry, Identifier>                                  IDENTIFIER          = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "identifier",
                                                                                                                           Identifier.class);

    @FieldId(6)
    public static final TKey<ObjectModelRegistry, Title>                                       TITLE               = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "title",
                                                                                                                           Title.class);

    @FieldId(7)
    public static final TKey<ObjectModelRegistry, Topic>                                       TOPIC               = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "topic",
                                                                                                                           Topic.class);

    @FieldId(8)
    public static final TKey<ObjectModelRegistry, Party>                                       PARTY               = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "party",
                                                                                                                           Party.class);

    @FieldId(9)
    public static final TKey<ObjectModelRegistry, Person>                                      PERSON              = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "person",
                                                                                                                           Person.class);

    @FieldId(10)
    public static final TKey<ObjectModelRegistry, Meeting>                                     MEETING             = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "meeting",
                                                                                                                           Meeting.class);

    @FieldId(11)
    public static final TKey<ObjectModelRegistry, Family>                                      FAMILY              = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "family",
                                                                                                                           Family.class);

    @FieldId(12)
    public static final TKey<ObjectModelRegistry, Organization>                                ORGANIZATION        = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "organization",
                                                                                                                           Organization.class);

    @FieldId(13)
    public static final TKey<ObjectModelRegistry, Link>                                        LINK                = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "link",
                                                                                                                           Link.class);

    @FieldId(14)
    public static final TKey<ObjectModelRegistry, Facet>                                       FACET               = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "facet",
                                                                                                                           Facet.class);

    @FieldId(15)
    public static final TKey<ObjectModelRegistry, Temporal>                                    TEMPORAL            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "temporal",
                                                                                                                           Temporal.class);

    @FieldId(16)
    public static final TKey<ObjectModelRegistry, Instant>                                     INSTANT             = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "instant",
                                                                                                                           Instant.class);

    @FieldId(17)
    public static final TKey<ObjectModelRegistry, Period>                                      PERIOD              = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "period",
                                                                                                                           Period.class);
    public static final TKey<ObjectModelRegistry, TemporalTextualExpression>                   TIME_TEXTUAL        = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "time textual",
                                                                                                                           TemporalTextualExpression.class);

    @FieldId(18)
    public static final TKey<ObjectModelRegistry, HistoricalPeriod>                            HISTORICAL_PERIOD   = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "historical period",
                                                                                                                           HistoricalPeriod.class);

    @FieldId(19)
    public static final TKey<ObjectModelRegistry, Numbering>                                   NUMBERING           = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "numbering",
                                                                                                                           Numbering.class);

    @FieldId(20)
    public static final TKey<ObjectModelRegistry, Edition>                                     EDITION             = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "edition",
                                                                                                                           Edition.class);

    @FieldId(21)
    public static final TKey<ObjectModelRegistry, Text>                                        TEXT                = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "text",
                                                                                                                           Text.class);

    @FieldId(22)
    public static final TKey<ObjectModelRegistry, LabeledText>                                 LABELED_TEXT        = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "labeled text",
                                                                                                                           LabeledText.class);

    @FieldId(23)
    public static final TKey<ObjectModelRegistry, FullText>                                    FULL_TEXT           = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "full text",
                                                                                                                           FullText.class);

    @FieldId(24)
    public static final TKey<ObjectModelRegistry, NamedPlace>                                  PLACE               = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "place",
                                                                                                                           NamedPlace.class);

    @FieldId(25)
    public static final TKey<ObjectModelRegistry, GeoReferencedPlace>                          GEO_PLACE           = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "georeference place",
                                                                                                                           GeoReferencedPlace.class);

    @FieldId(26)
    public static final TKey<ObjectModelRegistry, BoundingBoxReferencedPlace>                  GEO_BOX_PLACE       = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "bounding box reference place",
                                                                                                                           BoundingBoxReferencedPlace.class);

    @FieldId(27)
    public static final TKey<ObjectModelRegistry, SpatialEntity>                               GEOGRAPHIC_ENTITY   = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "geographic entity",
                                                                                                                           SpatialEntity.class);

    @FieldId(28)
    public static final TKey<ObjectModelRegistry, Metadata>                                    METADATA            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "metadata",
                                                                                                                           Metadata.class);

    @FieldId(29)
    public static final TKey<ObjectModelRegistry, RelatedResource>                             RELATED_RESOURCE    = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "related resource",
                                                                                                                           RelatedResource.class);

    @FieldId(30)
    public static final TKey<ObjectModelRegistry, AccessPermission>                            ACCESS_PERMISSION   = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "access permission",
                                                                                                                           AccessPermission.class);

    // ----------------------------------------- Qualifiers
    @FieldId(31)
    public static final TKey<ObjectModelRegistry, Status>                                      STATUS              = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "status",
                                                                                                                           Status.class);

    @FieldId(32)
    public static final TKey<ObjectModelRegistry, Redirect>                                    REDIRECT            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "redirect",
                                                                                                                           Redirect.class);

    @FieldId(33)
    public static final TKey<ObjectModelRegistry, Language>                                    LANGUAGE            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "language",
                                                                                                                           Language.class);

    @FieldId(34)
    public static final TKey<ObjectModelRegistry, Country>                                     COUNTRY             = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "country",
                                                                                                                           Country.class);

    @FieldId(35)
    public static final TKey<ObjectModelRegistry, Maturity>                                    MATURITY            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "maturity",
                                                                                                                           Maturity.class);

    @FieldId(36)
    public static final TKey<ObjectModelRegistry, ResourceType>                                RESOURCE_TYPE       = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "resource_type",
                                                                                                                           ResourceType.class);

    @FieldId(37)
    public static final TKey<ObjectModelRegistry, BibliographicLevel>                          BIBLIOGRAPHIC_LEVEL = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "bibliographic_level",
                                                                                                                           BibliographicLevel.class);

    @FieldId(38)
    public static final TKey<ObjectModelRegistry, PrintType>                                   PRINT_TYPE          = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "print_type",
                                                                                                                           PrintType.class);

    @FieldId(39)
    public static final TKey<ObjectModelRegistry, MaterialType>                                MATERIAL_TYPE       = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "material_type",
                                                                                                                           MaterialType.class);

    @FieldId(40)
    public static final TKey<ObjectModelRegistry, Audience>                                    AUDIENCE            = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "audience",
                                                                                                                           Audience.class);

    @FieldId(41)
    public static final TKey<ObjectModelRegistry, FormOfItem>                                  FORM                = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "form",
                                                                                                                           FormOfItem.class);

    @FieldId(42)
    public static final TKey<ObjectModelRegistry, CatalogingForm>                              CATALOGING_FORM     = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "cataloging_form",
                                                                                                                           CatalogingForm.class);

    @FieldId(43)
    public static final TKey<ObjectModelRegistry, Illustrations>                               ILLUSTRATIONS       = TKey.register(
                                                                                                                           ObjectModelRegistry.class,
                                                                                                                           "illustrations",
                                                                                                                           Illustrations.class);

    private static final Map<Class<?>, TKey<?, ?>>                                             tKeyClassMap        = new HashMap<Class<?>, TKey<?, ?>>();

    private static final Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>                  validQualifiers     = new HashMap<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>();

    private static final Map<TKey<?, ?>, Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>> validRelations      = new HashMap<TKey<?, ?>, Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>>();

    static {
        validQualifiers.put(TEMPORAL, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(TemporalRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(INSTANT, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(TemporalRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(PERIOD, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(TemporalRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(TIME_TEXTUAL, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(TemporalRelation.class);
                add(Language.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(HISTORICAL_PERIOD, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(TemporalRelation.class);
                add(Language.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(TITLE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(TitleType.class);
// add(TopicIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(TOPIC, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(KnowledgeOrganizationSystem.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(PARTITION, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(PartitionType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(HASH, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(HashType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(TEXT, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(TextRelation.class);
                add(NoteType.class);
                add(ResourceRelation.class);
                add(FieldSource.class);
                add(ContextLevel.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(LABELED_TEXT, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(TextRelation.class);
                add(NoteType.class);
                add(ResourceRelation.class);
                add(FieldSource.class);
                add(ContextLevel.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(FULL_TEXT, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(EDITION, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(LANGUAGE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(LanguageRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(IDENTIFIER, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(IdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
                add(ContextLevel.class);
                add(KnowledgeOrganizationSystem.class);
                add(SpatialIdentifierType.class);
                add(PartyIdentifierType.class);
                add(CertaintyLevel.class);
            }
        });

        validQualifiers.put(LINK, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(LinkTarget.class);
                add(DigitalObjectTarget.class);
                add(ResourceRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
                add(ContextLevel.class);
            }
        });

        validQualifiers.put(NUMBERING, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(NumberingRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(PARTY, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(PartyRelation.class);
                add(ContributionType.class);
                add(PartyIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(PERSON, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(PartyRelation.class);
                add(ContributionType.class);
                add(PartyIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(MEETING, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(PartyRelation.class);
                add(ContributionType.class);
                add(PartyIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(FAMILY, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(PartyRelation.class);
                add(ContributionType.class);
                add(PartyIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(ORGANIZATION, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(PartyRelation.class);
                add(ContributionType.class);
                add(PartyIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(GEO_PLACE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(SpatialRelation.class);
                add(SpatialIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(GEO_BOX_PLACE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(SpatialRelation.class);
                add(SpatialIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(PLACE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(SpatialRelation.class);
                add(SpatialIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(GEOGRAPHIC_ENTITY, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(SpatialRelation.class);
                add(SpatialIdentifierType.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(FORM, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(ContextLevel.class);
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        validQualifiers.put(METADATA, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validQualifiers.put(RELATED_RESOURCE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(Language.class);
                add(ResourceRelation.class);
                add(FieldSource.class);
                add(FieldScope.class);
                add(ContextLevel.class);
            }
        });

        validQualifiers.put(ACCESS_PERMISSION, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });
        
        validQualifiers.put(RESOURCE_TYPE, new ArrayList<Class<? extends Enum<?>>>() {
            {
                add(FieldSource.class);
                add(FieldScope.class);
            }
        });

        validRelations.put(PERSON, new HashMap<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>() {
            {
                put(IDENTIFIER, new ArrayList<Class<? extends Enum<?>>>() {
                    {
                        add(CertaintyLevel.class);
                    }
                });
            }
        });

        for (Field field : ObjectModelRegistry.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (object instanceof TKey) {
                    TKey k = (TKey)object;
                    if (tKeyClassMap.containsKey(k.getType())) { throw new RuntimeException(
                            "Two TKeys for the same class in the Object Model: " +
                                    k.getType().getName()); }
                    tKeyClassMap.put(k.getType(), k);

                    List<Class<? extends Enum<?>>> validEnums = getValidEnums(k);
                    for (Class<? extends Enum<?>> validEnum : validEnums) {
                        if (!QualifierRegistry.isValidEnum(validEnum)) { throw new RuntimeException(
                                "The enum hasn't been registered in the qualifier registry: " +
                                        validEnum); }
                    }
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
        if (ret == null) {
            ret = new ArrayList<Class<? extends Enum<?>>>(0);
        }
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
    public static <T> TKey<ObjectModelRegistry, T> lookup(Class<T> cls) {
        return (TKey<ObjectModelRegistry, T>)tKeyClassMap.get(cls);
    }

    public static Set<Class<?>> getAllSupportedClasses() {
        return tKeyClassMap.keySet();
    }

    public static Set<Class<? extends Enum>> getAllSupportedQualifiers() {
        Set<Class<? extends Enum>> qualifiers = new HashSet<Class<? extends Enum>>();
        for (Entry<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>> entry : validQualifiers.entrySet()) {
            qualifiers.addAll(entry.getValue());
        }
        return qualifiers;
    }

    /**
     * Returns valid target keys and qualifiers for the given source key that could form a relation.
     * 
     * @param key
     *            typed key for which valid relation keys and qualifiers should be returned
     * @return valid keys and enums for the given source key or null
     */
    public static Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>> getValidTargets(
            TKey<?, ?> key) {
        Map<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>> ret = validRelations.get(key);
        if (ret == null) {
            ret = new HashMap<TKey<?, ?>, ArrayList<Class<? extends Enum<?>>>>(0);
        }
        return ret;
    }

    public static Map<TKey<?, ?>, Integer> tkeyFieldId;
    public static Map<Integer, TKey<?, ?>> fieldIdTkey;

    static {
        tkeyFieldId = new HashMap<TKey<?, ?>, Integer>();
        fieldIdTkey = new HashMap<Integer, TKey<?, ?>>();

        for (Field f : ObjectModelRegistry.class.getDeclaredFields()) {
            if (f.getName().equals("METADATARECORD")) {
                continue;
            }
            FieldId ann = f.getAnnotation(FieldId.class);
            if (ann != null) {
                if (fieldIdTkey.containsKey(ann.value())) { throw new RuntimeException(
                        "Duplicate field id '" + ann.value() + "' is not allowed!"); }

                try {
                    tkeyFieldId.put((TKey<?, ?>)f.get(TKey.class), ann.value());
                    fieldIdTkey.put(ann.value(), (TKey<?, ?>)f.get(TKey.class));
                } catch (Exception e) {
                    throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
                }
            }
        }

        for (Field f : AuthorityObjectModelRegistry.class.getDeclaredFields()) {
            FieldId ann = f.getAnnotation(FieldId.class);
            if (ann != null) {
                if (fieldIdTkey.containsKey(ann.value())) { throw new RuntimeException(
                        "Duplicate field id '" + ann.value() + "' is not allowed!"); }

                try {
                    tkeyFieldId.put((TKey<?, ?>)f.get(TKey.class), ann.value());
                    fieldIdTkey.put(ann.value(), (TKey<?, ?>)f.get(TKey.class));
                } catch (Exception e) {
                    throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
                }
            }
        }
    }
}
