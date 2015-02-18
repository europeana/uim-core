package org.theeuropeanlibrary.model.tel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.qualifier.CertaintyLevel;
import org.theeuropeanlibrary.model.common.qualifier.ContributionType;
import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.Downloadable;
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
import org.theeuropeanlibrary.model.tel.qualifier.AddressType;
import org.theeuropeanlibrary.model.tel.qualifier.Audience;
import org.theeuropeanlibrary.model.tel.qualifier.BibliographicLevel;
import org.theeuropeanlibrary.model.tel.qualifier.CatalogingForm;
import org.theeuropeanlibrary.model.tel.qualifier.CollectionType;
import org.theeuropeanlibrary.model.tel.qualifier.ContextLevel;
import org.theeuropeanlibrary.model.tel.qualifier.DigitalObjectTarget;
import org.theeuropeanlibrary.model.tel.qualifier.DisambiguationDataType;
import org.theeuropeanlibrary.model.tel.qualifier.FieldScope;
import org.theeuropeanlibrary.model.tel.qualifier.FieldSource;
import org.theeuropeanlibrary.model.tel.qualifier.FormOfItem;
import org.theeuropeanlibrary.model.tel.qualifier.HashType;
import org.theeuropeanlibrary.model.tel.qualifier.Illustrations;
import org.theeuropeanlibrary.model.tel.qualifier.MaterialType;
import org.theeuropeanlibrary.model.tel.qualifier.Maturity;
import org.theeuropeanlibrary.model.tel.qualifier.MediaType;
import org.theeuropeanlibrary.model.tel.qualifier.NameFormRelation;
import org.theeuropeanlibrary.model.tel.qualifier.NoteType;
import org.theeuropeanlibrary.model.tel.qualifier.OrganizationType;
import org.theeuropeanlibrary.model.tel.qualifier.PartitionType;
import org.theeuropeanlibrary.model.tel.qualifier.PhoneType;
import org.theeuropeanlibrary.model.tel.qualifier.PrintType;
import org.theeuropeanlibrary.model.tel.qualifier.SpatialIdentifierRelation;
import org.theeuropeanlibrary.model.tel.qualifier.SpatialNameQualifier;
import org.theeuropeanlibrary.model.tel.qualifier.StructuralRelationType;

import eu.europeana.uim.store.MetaDataRecord;

/**
 * Registry holding all known enums used as qualifiers for entries on {@link MetaDataRecord}.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 11.02.2013
 */
@SuppressWarnings("all")
public final class QualifierRegistry {
    @FieldId(1)
    public static final Class<IdentifierType>              IDENTIFIER_TYPE               = IdentifierType.class;

    @FieldId(2)
    public static final Class<CertaintyLevel>              CERTAINTY_LEVEL               = CertaintyLevel.class;

    @FieldId(3)
    public static final Class<ContributionType>            CONTRIBUTION_TYPE             = ContributionType.class;

    @FieldId(4)
    public static final Class<Country>                     COUNTRY                       = Country.class;

    @FieldId(5)
    public static final Class<KnowledgeOrganizationSystem> KNOWLEDGE_ORGANIZATION_SYSTEM = KnowledgeOrganizationSystem.class;

    @FieldId(6)
    public static final Class<Language>                    LANGUAGE                      = Language.class;

    @FieldId(7)
    public static final Class<LanguageRelation>            LANGUAGE_RELATION             = LanguageRelation.class;

    @FieldId(8)
    public static final Class<LinkTarget>                  LINK_TARGET                   = LinkTarget.class;

    @FieldId(9)
    public static final Class<NumberingRelation>           NUMBERING_RELATION            = NumberingRelation.class;

    @FieldId(10)
    public static final Class<PartyIdentifierType>         PARTY_IDENTIFIER_TYPE         = PartyIdentifierType.class;

    @FieldId(11)
    public static final Class<PartyRelation>               PARTY_RELATION                = PartyRelation.class;

    @FieldId(12)
    public static final Class<ResourceRelation>            RESOURCE_RELATION             = ResourceRelation.class;

    @FieldId(13)
    public static final Class<ResourceType>                RESOURCE_TYPE                 = ResourceType.class;

    @FieldId(14)
    public static final Class<SpatialIdentifierType>       SPATIAL_IDENTIFIER_TYPE       = SpatialIdentifierType.class;

    @FieldId(15)
    public static final Class<SpatialRelation>             SPATIAL_RELATION              = SpatialRelation.class;

    @FieldId(16)
    public static final Class<Status>                      STATUS                        = Status.class;

    @FieldId(17)
    public static final Class<TemporalRelation>            TEMPORAL_RELATION             = TemporalRelation.class;

    @FieldId(18)
    public static final Class<TextRelation>                TEXT_RELATION                 = TextRelation.class;

    @FieldId(19)
    public static final Class<TitleType>                   TITLE_TYPE                    = TitleType.class;

    @FieldId(20)
    public static final Class<PartitionType>               PARTITION_TYPE                = PartitionType.class;

    @FieldId(21)
    public static final Class<FieldSource>                 FIELD_SOURCE                  = FieldSource.class;

    @FieldId(22)
    public static final Class<HashType>                    HASH_TYPE                     = HashType.class;

    @FieldId(23)
    public static final Class<ContextLevel>                CONTEXT_LEVEL                 = ContextLevel.class;

    @FieldId(24)
    public static final Class<DigitalObjectTarget>         DIGITAL_OBJECT_TARGET         = DigitalObjectTarget.class;

    @FieldId(25)
    public static final Class<NoteType>                    NOTE_TYPE                     = NoteType.class;

    @FieldId(26)
    public static final Class<NameFormRelation>            NAME_FORM_RELATION            = NameFormRelation.class;

    @FieldId(27)
    public static final Class<SpatialNameQualifier>        SPATIAL_NAME_QUALIFIER        = SpatialNameQualifier.class;

    @FieldId(28)
    public static final Class<SpatialIdentifierRelation>   SPATIAL_IDENTIFIER_RELATION   = SpatialIdentifierRelation.class;

    @FieldId(29)
    public static final Class<DisambiguationDataType>      DISAMBIGUATION_DATA_TYPE      = DisambiguationDataType.class;

    @FieldId(30)
    public static final Class<AddressType>                 ADDRESS_TYPE                  = AddressType.class;

    @FieldId(31)
    public static final Class<Audience>                    AUDIENCE                      = Audience.class;

    @FieldId(32)
    public static final Class<BibliographicLevel>          BIBLIOGRAPHIC_LEVEL           = BibliographicLevel.class;

    @FieldId(33)
    public static final Class<CatalogingForm>              CATALOGING_FORM               = CatalogingForm.class;

    @FieldId(34)
    public static final Class<CollectionType>              COLLECTION_TYPE               = CollectionType.class;

    @FieldId(35)
    public static final Class<FormOfItem>                  FORM_OF_ITEM                  = FormOfItem.class;

    @FieldId(36)
    public static final Class<Illustrations>               ILLUSTRATIONS                 = Illustrations.class;

    @FieldId(37)
    public static final Class<MaterialType>                MATERIAL_TYPE                 = MaterialType.class;

    @FieldId(38)
    public static final Class<Maturity>                    MATURITY                      = Maturity.class;

    @FieldId(39)
    public static final Class<MediaType>                   MEDIA_TYPE                    = MediaType.class;

    @FieldId(40)
    public static final Class<OrganizationType>            ORGANIZATION_TYPE             = OrganizationType.class;

    @FieldId(41)
    public static final Class<PhoneType>                   PHONE_TYPE                    = PhoneType.class;

    @FieldId(42)
    public static final Class<PrintType>                   PRINT_TYPE                    = PrintType.class;

    @FieldId(43)
    public static final Class<StructuralRelationType>      STRUCTURAL_RELATION_TYPE      = StructuralRelationType.class;

    @FieldId(44)
    public static final Class<FieldScope>                  FIELD_SCOPE                   = FieldScope.class;

    @FieldId(45)
    public static final Class<Downloadable>                DOWNLOADABLE                  = Downloadable.class;

    public static final Set<String>                        enumSet                       = new HashSet<String>();

    public static Map<String, String>                      enumFieldId;
    public static Map<String, String>                      fieldIdEnum;

    static {
        for (Field field : QualifierRegistry.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (object instanceof Class) {
                    String enumName = ((Class)object).getName();
                    if (enumSet.contains(enumName)) { throw new RuntimeException(
                            "Two enums for the same class in the Object Model: " + enumName); }
                    enumSet.add(enumName);
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isValidEnum(Class<? extends Enum<?>> enumeration) {
        return enumSet.contains(enumeration.getName());
    }

    static {
        enumFieldId = new HashMap<String, String>();
        fieldIdEnum = new HashMap<String, String>();

        for (Field f : QualifierRegistry.class.getDeclaredFields()) {
            FieldId fann = f.getAnnotation(FieldId.class);
            if (fann == null) {
                continue;
            }

            Class<?> qualifierType;
            try {
                qualifierType = (Class<?>)f.get(Enum.class);
            } catch (Exception e) {
                throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
// continue;
            }

            for (Field g : qualifierType.getDeclaredFields()) {
                FieldId eann = g.getAnnotation(FieldId.class);

                if (g.getName().contains("ENUM$VALUES")) {
                    continue;
                }

                String none = qualifierType.getName() + "@" + g.getName();

                if (eann != null) {
                    String second = f.getDeclaringClass().getName() + "@" + eann.value();

                    if (fieldIdEnum.containsKey(second)) { throw new RuntimeException(
                            "Duplicate field id '" + second + "' is not allowed!"); }

                    try {
                        fieldIdEnum.put(second, none);
                        enumFieldId.put(none, second);
                    } catch (Exception e) {
                        throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
                    }
                }

                if (fann != null) {
                    String first = fann.value() + "@" + g.getName();

                    if (fieldIdEnum.containsKey(first)) { throw new RuntimeException(
                            "Duplicate field id '" + first + "' is not allowed!"); }

                    try {
                        fieldIdEnum.put(first, none);
                        enumFieldId.put(none, first);
                    } catch (Exception e) {
                        throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
                    }
                }

                if (fann != null && eann != null) {
                    String full = fann.value() + "@" + eann.value();

                    if (fieldIdEnum.containsKey(full)) { throw new RuntimeException(
                            "Duplicate field id '" + full + "' is not allowed!"); }

                    try {
                        fieldIdEnum.put(full, none);
                        enumFieldId.put(none, full);
                    } catch (Exception e) {
                        throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        for (Field f : QualifierRegistry.class.getDeclaredFields()) {
            FieldId fann = f.getAnnotation(FieldId.class);
            if (fann == null) {
                continue;
            }

            Class<?> qualifierType;
            try {
                qualifierType = (Class<?>)f.get(Enum.class);
            } catch (Exception e) {
                throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
// continue;
            }

            String filePath = "/home/mmuhr/git/uim-core/model/trunk/tel/src/main/java/org/theeuropeanlibrary/model/tel/qualifier/" +
                              qualifierType.getSimpleName() + ".java";
            File file = new File(filePath);
            if (!file.exists()) {
                filePath = "/home/mmuhr/git/uim-core/model/trunk/common/src/main/java/org/theeuropeanlibrary/model/common/qualifier/" +
                           qualifierType.getSimpleName() + ".java";
                file = new File(filePath);
                if (!file.exists()) {
                    System.out.println(qualifierType.getName());
                }
            }
            String java = FileUtils.readFileToString(file);
            if (java == null || java.length() == 0) {
                System.out.println(qualifierType.getName());
                continue;
            }

            int counter = 1;
            for (Field g : qualifierType.getDeclaredFields()) {
                FieldId eann = g.getAnnotation(FieldId.class);

                if (g.getName().contains("ENUM$VALUES")) {
                    continue;
                }

                if (eann == null) {
                    java = java.replace(g.getName() + ",",
                            " @FieldId(" + counter + ")\n" + g.getName() + ",");
                }
                counter++;
            }

// FileUtils.writeStringToFile(filePath, data);
        }
        System.out.println();
        System.out.println(QualifierRegistry.enumSet);
        System.out.println(QualifierRegistry.enumFieldId);
        System.out.println(QualifierRegistry.fieldIdEnum);
    }
}
