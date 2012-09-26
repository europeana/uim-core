package eu.europeana.model.edm;

import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.common.time.Instant;
import org.theeuropeanlibrary.model.common.time.Period;
import org.theeuropeanlibrary.model.common.time.TemporalTextualExpression;

import eu.europeana.uim.common.TKey;

/**
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 */
@SuppressWarnings("all")
public final class ProvidedChoModelRegistry {

    
    public static final TKey<ProvidedChoModelRegistry, Identifier>                 IDENTIFIER          = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "identifier",
                                                                                                          Identifier.class);

    public static final TKey<ProvidedChoModelRegistry, Title>                      TITLE               = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "title",
                                                                                                          Title.class);

    public static final TKey<ProvidedChoModelRegistry, Topic>                      TOPIC               = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "topic",
                                                                                                          Topic.class);

    public static final TKey<ProvidedChoModelRegistry, Party>                      PARTY               = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "party",
                                                                                                          Party.class);
    public static final TKey<ProvidedChoModelRegistry, Person>                     PERSON              = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "person",
                                                                                                          Person.class);
    public static final TKey<ProvidedChoModelRegistry, Meeting>                    MEETING             = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "meeting",
                                                                                                          Meeting.class);
    public static final TKey<ProvidedChoModelRegistry, Family>                     FAMILY              = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "family",
                                                                                                          Family.class);
    public static final TKey<ProvidedChoModelRegistry, Organization>               ORGANIZATION        = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "organization",
                                                                                                          Organization.class);

    public static final TKey<ProvidedChoModelRegistry, Link>                       LINK                = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "link",
                                                                                                          Link.class);

    public static final TKey<ProvidedChoModelRegistry, Instant>                    INSTANT             = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "instant",
                                                                                                          Instant.class);
    public static final TKey<ProvidedChoModelRegistry, Period>                     PERIOD              = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "period",
                                                                                                          Period.class);
    public static final TKey<ProvidedChoModelRegistry, TemporalTextualExpression>  TIME_TEXTUAL        = TKey.register(
                                                                                                          ProvidedChoModelRegistry.class,
                                                                                                          "time textual",
                                                                                                          TemporalTextualExpression.class);

}
