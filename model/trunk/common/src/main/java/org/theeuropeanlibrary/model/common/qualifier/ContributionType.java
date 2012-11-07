package org.theeuropeanlibrary.model.common.qualifier;

import java.util.HashMap;

/**
 * Disambiguates between purposes of a specific party like creator or publisher.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire (nfreire@gmail.com)
 * @since Mar 18, 2011
 */
public enum ContributionType {
    /**
     * party contributed to the content of the work with supplementary text Kept only for backward
     * compatibility
     */
    @Deprecated
    AUTHOR_OF_SUPPLEMENTARY_TEXT("Author of supplementary text"),
    /**
     * party contributed to the content of the work as a translator
     */
    TRANSLATOR("Translator"),
    /**
     * party contributed to the content of the work as an illustrator
     */
    ILLUSTRATOR("Illustrator"),
    /**
     * party contributed to the content of the work as an editor
     */
    EDITOR("Editor"),
    /**
     * party contributed to the content of the work as an illustrator
     */
    FORMER_OWNER("Former owner"),
    /**
     * Actor
     */
    ACTOR("Actor"),
    /**
     * Adapter
     */
    ADAPTER("Adapter"),
    /**
     * Analyst
     */
    ANALYST("Analyst"),
    /**
     * Animator
     */
    ANIMATOR("Animator"),
    /**
     * Annotator
     */
    ANNOTATOR("Annotator"),
    /**
     * Applicant
     */
    APPLICANT("Applicant"),
    /**
     * Architect
     */
    ARCHITECT("Architect"),
    /**
     * Arranger
     */
    ARRANGER("Arranger"),
    /**
     * Art copyist
     */
    ART_COPYIST("Art copyist"),
    /**
     * Artist
     */
    ARTIST("Artist"),
    /**
     * Artistic director
     */
    ARTISTIC_DIRECTOR("Artistic director"),
    /**
     * Assignee
     */
    ASSIGNEE("Assignee"),
    /**
     * Associated name
     */
    ASSOCIATED_NAME("Associated name"),
    /**
     * Attributed name
     */
    ATTRIBUTED_NAME("Attributed name"),
    /**
     * Auctioneer
     */
    AUCTIONEER("Auctioneer"),
    /**
     * Author
     */
    AUTHOR("Author"),
    /**
     * Author in quotations or text abstracts
     */
    AUTHOR_IN_QUOTATIONS_OR_TEXT_ABSTRACTS("Author in quotations or text abstracts"),
    /**
     * Author of afterword, colophon, etc.
     */
    AUTHOR_OF_AFTERWORD_COLOPHON_ETC_("Author of afterword, colophon, etc."),
    /**
     * Author of dialog
     */
    AUTHOR_OF_DIALOG("Author of dialog"),
    /**
     * Author of introduction
     */
    AUTHOR_OF_INTRODUCTION("Author of introduction"),
    /**
     * Author of screenplay
     */
    AUTHOR_OF_SCREENPLAY("Author of screenplay"),
    /**
     * Bibliographic antecedent
     */
    BIBLIOGRAPHIC_ANTECEDENT("Bibliographic antecedent"),
    /**
     * Binder
     */
    BINDER("Binder"),
    /**
     * Binding designer
     */
    BINDING_DESIGNER("Binding designer"),
    /**
     * Blurb writer
     */
    BLURB_WRITER("Blurb writer"),
    /**
     * Book designer
     */
    BOOK_DESIGNER("Book designer"),
    /**
     * Book producer
     */
    BOOK_PRODUCER("Book producer"),
    /**
     * Bookjacket designer
     */
    BOOKJACKET_DESIGNER("Bookjacket designer"),
    /**
     * Bookplate designer
     */
    BOOKPLATE_DESIGNER("Bookplate designer"),
    /**
     * Bookseller
     */
    BOOKSELLER("Bookseller"),
    /**
     * Calligrapher
     */
    CALLIGRAPHER("Calligrapher"),
    /**
     * Cartographer
     */
    CARTOGRAPHER("Cartographer"),
    /**
     * Censor
     */
    CENSOR("Censor"),
    /**
     * Choreographer
     */
    CHOREOGRAPHER("Choreographer"),
    /**
     * Cinematographer
     */
    CINEMATOGRAPHER("Cinematographer"),
    /**
     * Client
     */
    CLIENT("Client"),
    /**
     * Collaborator
     */
    COLLABORATOR("Collaborator"),
    /**
     * Collector
     */
    COLLECTOR("Collector"),
    /**
     * Collotyper
     */
    COLLOTYPER("Collotyper"),
    /**
     * Colorist
     */
    COLORIST("Colorist"),
    /**
     * Commentator
     */
    COMMENTATOR("Commentator"),
    /**
     * Commentator for written text
     */
    COMMENTATOR_FOR_WRITTEN_TEXT("Commentator for written text"),
    /**
     * Compiler
     */
    COMPILER("Compiler"),
    /**
     * Complainant
     */
    COMPLAINANT("Complainant"),
    /**
     * Complainant-appellant
     */
    COMPLAINANT_APPELLANT("Complainant-appellant"),
    /**
     * Complainant-appellee
     */
    COMPLAINANT_APPELLEE("Complainant-appellee"),
    /**
     * Composer
     */
    COMPOSER("Composer"),
    /**
     * Compositor
     */
    COMPOSITOR("Compositor"),
    /**
     * Conceptor
     */
    CONCEPTOR("Conceptor"),
    /**
     * Conductor
     */
    CONDUCTOR("Conductor"),
    /**
     * Conservator
     */
    CONSERVATOR("Conservator"),
    /**
     * Consultant
     */
    CONSULTANT("Consultant"),
    /**
     * Consultant to a project
     */
    CONSULTANT_TO_A_PROJECT("Consultant to a project"),
    /**
     * Contestant
     */
    CONTESTANT("Contestant"),
    /**
     * Contestant-appellant
     */
    CONTESTANT_APPELLANT("Contestant-appellant"),
    /**
     * Contestant-appellee
     */
    CONTESTANT_APPELLEE("Contestant-appellee"),
    /**
     * Contestee
     */
    CONTESTEE("Contestee"),
    /**
     * Contestee-appellant
     */
    CONTESTEE_APPELLANT("Contestee-appellant"),
    /**
     * Contestee-appellee
     */
    CONTESTEE_APPELLEE("Contestee-appellee"),
    /**
     * Contractor
     */
    CONTRACTOR("Contractor"),
    /**
     * Contributor
     */
    CONTRIBUTOR("Contributor"),
    /**
     * Copyright claimant
     */
    COPYRIGHT_CLAIMANT("Copyright claimant"),
    /**
     * Copyright holder
     */
    COPYRIGHT_HOLDER("Copyright holder"),
    /**
     * Corrector
     */
    CORRECTOR("Corrector"),
    /**
     * Correspondent
     */
    CORRESPONDENT("Correspondent"),
    /**
     * Costume designer
     */
    COSTUME_DESIGNER("Costume designer"),
    /**
     * Cover designer
     */
    COVER_DESIGNER("Cover designer"),
    /**
     * Creator
     */
    CREATOR("Creator"),
    /**
     * Curator
     */
    CURATOR("Curator"),
    /**
     * Dancer
     */
    DANCER("Dancer"),
    /**
     * Data contributor
     */
    DATA_CONTRIBUTOR("Data contributor"),
    /**
     * Data manager
     */
    DATA_MANAGER("Data manager"),
    /**
     * Dedicatee
     */
    DEDICATEE("Dedicatee"),
    /**
     * Dedicator
     */
    DEDICATOR("Dedicator"),
    /**
     * Defendant
     */
    DEFENDANT("Defendant"),
    /**
     * Defendant-appellant
     */
    DEFENDANT_APPELLANT("Defendant-appellant"),
    /**
     * Defendant-appellee
     */
    DEFENDANT_APPELLEE("Defendant-appellee"),
    /**
     * Degree grantor
     */
    DEGREE_GRANTOR("Degree grantor"),
    /**
     * Delineator
     */
    DELINEATOR("Delineator"),
    /**
     * Depicted
     */
    DEPICTED("Depicted"),
    /**
     * Depositor
     */
    DEPOSITOR("Depositor"),
    /**
     * Designer
     */
    DESIGNER("Designer"),
    /**
     * Director
     */
    DIRECTOR("Director"),
    /**
     * Dissertant
     */
    DISSERTANT("Dissertant"),
    /**
     * Distribution place
     */
    DISTRIBUTION_PLACE("Distribution place"),
    /**
     * Distributor
     */
    DISTRIBUTOR("Distributor"),
    /**
     * Donor
     */
    DONOR("Donor"),
    /**
     * Draftsman
     */
    DRAFTSMAN("Draftsman"),
    /**
     * Dubious author
     */
    DUBIOUS_AUTHOR("Dubious author"),
    /**
     * Electrician
     */
    ELECTRICIAN("Electrician"),
    /**
     * Electrotyper
     */
    ELECTROTYPER("Electrotyper"),
    /**
     * Engineer
     */
    ENGINEER("Engineer"),
    /**
     * Engraver
     */
    ENGRAVER("Engraver"),
    /**
     * Etcher
     */
    ETCHER("Etcher"),
    /**
     * Event place
     */
    EVENT_PLACE("Event place"),
    /**
     * Expert
     */
    EXPERT("Expert"),
    /**
     * Facsimilist
     */
    FACSIMILIST("Facsimilist"),
    /**
     * Field director
     */
    FIELD_DIRECTOR("Field director"),
    /**
     * Film editor
     */
    FILM_EDITOR("Film editor"),
    /**
     * First party
     */
    FIRST_PARTY("First party"),
    /**
     * Forger
     */
    FORGER("Forger"),
    /**
     * Funder
     */
    FUNDER("Funder"),
    /**
     * Geographic information specialist
     */
    GEOGRAPHIC_INFORMATION_SPECIALIST("Geographic information specialist"),
    /**
     * Graphic technician
     */
    GRAPHIC_TECHNICIAN("Graphic technician"),
    /**
     * Honoree
     */
    HONOREE("Honoree"),
    /**
     * Host
     */
    HOST("Host"),
    /**
     * Illuminator
     */
    ILLUMINATOR("Illuminator"),
    /**
     * Inscriber
     */
    INSCRIBER("Inscriber"),
    /**
     * Instrumentalist
     */
    INSTRUMENTALIST("Instrumentalist"),
    /**
     * Interviewee
     */
    INTERVIEWEE("Interviewee"),
    /**
     * Interviewer
     */
    INTERVIEWER("Interviewer"),
    /**
     * Inventor
     */
    INVENTOR("Inventor"),
    /**
     * Laboratory
     */
    LABORATORY("Laboratory"),
    /**
     * Laboratory director
     */
    LABORATORY_DIRECTOR("Laboratory director"),
    /**
     * Landscape architect
     */
    LANDSCAPE_ARCHITECT("Landscape architect"),
    /**
     * Lead
     */
    LEAD("Lead"),
    /**
     * Lender
     */
    LENDER("Lender"),
    /**
     * Libelant
     */
    LIBELANT("Libelant"),
    /**
     * Libelant-appellant
     */
    LIBELANT_APPELLANT("Libelant-appellant"),
    /**
     * Libelant-appellee
     */
    LIBELANT_APPELLEE("Libelant-appellee"),
    /**
     * Libelee
     */
    LIBELEE("Libelee"),
    /**
     * Libelee-appellant
     */
    LIBELEE_APPELLANT("Libelee-appellant"),
    /**
     * Libelee-appellee
     */
    LIBELEE_APPELLEE("Libelee-appellee"),
    /**
     * Librettist
     */
    LIBRETTIST("Librettist"),
    /**
     * Licensee
     */
    LICENSEE("Licensee"),
    /**
     * Licensor
     */
    LICENSOR("Licensor"),
    /**
     * Lighting designer
     */
    LIGHTING_DESIGNER("Lighting designer"),
    /**
     * Lithographer
     */
    LITHOGRAPHER("Lithographer"),
    /**
     * Lyricist
     */
    LYRICIST("Lyricist"),
    /**
     * Manufacture place
     */
    MANUFACTURE_PLACE("Manufacture place"),
    /**
     * Manufacturer
     */
    MANUFACTURER("Manufacturer"),
    /**
     * Marbler
     */
    MARBLER("Marbler"),
    /**
     * Markup editor
     */
    MARKUP_EDITOR("Markup editor"),
    /**
     * Metadata contact
     */
    METADATA_CONTACT("Metadata contact"),
    /**
     * Metal-engraver
     */
    METAL_ENGRAVER("Metal-engraver"),
    /**
     * Moderator
     */
    MODERATOR("Moderator"),
    /**
     * Monitor
     */
    MONITOR("Monitor"),
    /**
     * Music copyist
     */
    MUSIC_COPYIST("Music copyist"),
    /**
     * Musical director
     */
    MUSICAL_DIRECTOR("Musical director"),
    /**
     * Musician
     */
    MUSICIAN("Musician"),
    /**
     * Narrator
     */
    NARRATOR("Narrator"),
    /**
     * Opponent
     */
    OPPONENT("Opponent"),
    /**
     * Organizer of meeting
     */
    ORGANIZER_OF_MEETING("Organizer of meeting"),
    /**
     * Originator
     */
    ORIGINATOR("Originator"),
    /**
     * Other
     */
    OTHER("Other"),
    /**
     * Owner
     */
    OWNER("Owner"),
    /**
     * Papermaker
     */
    PAPERMAKER("Papermaker"),
    /**
     * Patent applicant
     */
    PATENT_APPLICANT("Patent applicant"),
    /**
     * Patent holder
     */
    PATENT_HOLDER("Patent holder"),
    /**
     * Patron
     */
    PATRON("Patron"),
    /**
     * Performer
     */
    PERFORMER("Performer"),
    /**
     * Permitting agency
     */
    PERMITTING_AGENCY("Permitting agency"),
    /**
     * Photographer
     */
    PHOTOGRAPHER("Photographer"),
    /**
     * Plaintiff
     */
    PLAINTIFF("Plaintiff"),
    /**
     * Plaintiff-appellee
     */
    PLAINTIFF_APPELLEE("Plaintiff-appellee"),
    /**
     * Plaintiff-appellant
     */
    PLAINTIFF_APPELLANT("Plaintiff-appellant"),
    /**
     * Platemaker
     */
    PLATEMAKER("Platemaker"),
    /**
     * Printer
     */
    PRINTER("Printer"),
    /**
     * Printer of plates
     */
    PRINTER_OF_PLATES("Printer of plates"),
    /**
     * Printmaker
     */
    PRINTMAKER("Printmaker"),
    /**
     * Process contact
     */
    PROCESS_CONTACT("Process contact"),
    /**
     * Producer
     */
    PRODUCER("Producer"),
    /**
     * Production manager
     */
    PRODUCTION_MANAGER("Production manager"),
    /**
     * Production personnel
     */
    PRODUCTION_PERSONNEL("Production personnel"),
    /**
     * Production place
     */
    PRODUCTION_PLACE("Production place"),
    /**
     * Programmer
     */
    PROGRAMMER("Programmer"),
    /**
     * Project director
     */
    PROJECT_DIRECTOR("Project director"),
    /**
     * Proofreader
     */
    PROOFREADER("Proofreader"),
    /**
     * Publication place
     */
    PUBLICATION_PLACE("Publication place"),
    /**
     * Publisher
     */
    PUBLISHER("Publisher"),
    /**
     * Publishing director
     */
    PUBLISHING_DIRECTOR("Publishing director"),
    /**
     * Puppeteer
     */
    PUPPETEER("Puppeteer"),
    /**
     * Recipient
     */
    RECIPIENT("Recipient"),
    /**
     * Recording engineer
     */
    RECORDING_ENGINEER("Recording engineer"),
    /**
     * Redactor
     */
    REDACTOR("Redactor"),
    /**
     * Renderer
     */
    RENDERER("Renderer"),
    /**
     * Reporter
     */
    REPORTER("Reporter"),
    /**
     * Repository
     */
    REPOSITORY("Repository"),
    /**
     * Research team head
     */
    RESEARCH_TEAM_HEAD("Research team head"),
    /**
     * Research team member
     */
    RESEARCH_TEAM_MEMBER("Research team member"),
    /**
     * Researcher
     */
    RESEARCHER("Researcher"),
    /**
     * Respondent
     */
    RESPONDENT("Respondent"),
    /**
     * Respondent-appellant
     */
    RESPONDENT_APPELLANT("Respondent-appellant"),
    /**
     * Respondent-appellee
     */
    RESPONDENT_APPELLEE("Respondent-appellee"),
    /**
     * Responsible party
     */
    RESPONSIBLE_PARTY("Responsible party"),
    /**
     * Restager
     */
    RESTAGER("Restager"),
    /**
     * Reviewer
     */
    REVIEWER("Reviewer"),
    /**
     * Rubricator
     */
    RUBRICATOR("Rubricator"),
    /**
     * Scenarist
     */
    SCENARIST("Scenarist"),
    /**
     * Scientific advisor
     */
    SCIENTIFIC_ADVISOR("Scientific advisor"),
    /**
     * Scribe
     */
    SCRIBE("Scribe"),
    /**
     * Sculptor
     */
    SCULPTOR("Sculptor"),
    /**
     * Second party
     */
    SECOND_PARTY("Second party"),
    /**
     * Secretary
     */
    SECRETARY("Secretary"),
    /**
     * Set designer
     */
    SET_DESIGNER("Set designer"),
    /**
     * Signer
     */
    SIGNER("Signer"),
    /**
     * Singer
     */
    SINGER("Singer"),
    /**
     * Sound designer
     */
    SOUND_DESIGNER("Sound designer"),
    /**
     * Speaker
     */
    SPEAKER("Speaker"),
    /**
     * Sponsor
     */
    SPONSOR("Sponsor"),
    /**
     * Stage manager
     */
    STAGE_MANAGER("Stage manager"),
    /**
     * Standards body
     */
    STANDARDS_BODY("Standards body"),
    /**
     * Stereotyper
     */
    STEREOTYPER("Stereotyper"),
    /**
     * Storyteller
     */
    STORYTELLER("Storyteller"),
    /**
     * Supporting host
     */
    SUPPORTING_HOST("Supporting host"),
    /**
     * Surveyor
     */
    SURVEYOR("Surveyor"),
    /**
     * Teacher
     */
    TEACHER("Teacher"),
    /**
     * Technical director
     */
    TECHNICAL_DIRECTOR("Technical director"),
    /**
     * Thesis advisor
     */
    THESIS_ADVISOR("Thesis advisor"),
    /**
     * Transcriber
     */
    TRANSCRIBER("Transcriber"),
    /**
     * Type designer
     */
    TYPE_DESIGNER("Type designer"),
    /**
     * Typographer
     */
    TYPOGRAPHER("Typographer"),
    /**
     * University place
     */
    UNIVERSITY_PLACE("University place"),
    /**
     * Videographer
     */
    VIDEOGRAPHER("Videographer"),
    /**
     * Vocalist
     */
    VOCALIST("Vocalist"),
    /**
     * Witness
     */
    WITNESS("Witness"),
    /**
     * Wood-engraver
     */
    WOOD_ENGRAVER("Wood-engraver"),
    /**
     * Woodcutter
     */
    WOODCUTTER("Woodcutter"),
    /**
     * Writer of accompanying material
     */
    WRITER_OF_ACCOMPANYING_MATERIAL("Writer of accompanying material"), ;

    private String label;

    private ContributionType(String label) {
        this.label = label;
    }

    /**
     * @return Human readable label in English
     */
    public String getLabel() {
        return label;
    }

    private static final HashMap<String, ContributionType> FROM_UNIMARC = new HashMap<String, ContributionType>() {
                                                                            {
                                                                                put("005", ACTOR);
                                                                                put("010", ADAPTER);
                                                                                put("020",
                                                                                        ANNOTATOR);
                                                                                put("030", ARRANGER);
                                                                                put("040", ARTIST);
                                                                                put("050", ASSIGNEE);
                                                                                put("060",
                                                                                        ASSOCIATED_NAME);
                                                                                put("065",
                                                                                        AUCTIONEER);
                                                                                put("070", AUTHOR);
                                                                                put("072",
                                                                                        AUTHOR_IN_QUOTATIONS_OR_TEXT_ABSTRACTS);
                                                                                put("075",
                                                                                        AUTHOR_OF_AFTERWORD_COLOPHON_ETC_);
                                                                                put("090",
                                                                                        AUTHOR_OF_DIALOG);
                                                                                put("080",
                                                                                        AUTHOR_OF_INTRODUCTION);
                                                                                put("100",
                                                                                        BIBLIOGRAPHIC_ANTECEDENT);
                                                                                put("110", BINDER);
                                                                                put("120",
                                                                                        BINDING_DESIGNER);
                                                                                put("130",
                                                                                        BOOK_DESIGNER);
                                                                                put("140",
                                                                                        BOOKJACKET_DESIGNER);
                                                                                put("150",
                                                                                        BOOKPLATE_DESIGNER);
                                                                                put("160",
                                                                                        BOOKSELLER);
                                                                                put("170",
                                                                                        CALLIGRAPHER);
                                                                                put("180",
                                                                                        CARTOGRAPHER);
                                                                                put("190", CENSOR);
                                                                                put("200",
                                                                                        CHOREOGRAPHER);
                                                                                put("205",
                                                                                        COLLABORATOR);
                                                                                put("210",
                                                                                        COMMENTATOR);
                                                                                put("212",
                                                                                        COMMENTATOR_FOR_WRITTEN_TEXT);
                                                                                put("220", COMPILER);
                                                                                put("230", COMPOSER);
                                                                                put("240",
                                                                                        COMPOSITOR);
                                                                                put("245",
                                                                                        CONCEPTOR);
                                                                                put("250",
                                                                                        CONDUCTOR);
                                                                                put("255",
                                                                                        CONSULTANT_TO_A_PROJECT);
                                                                                put("260",
                                                                                        COPYRIGHT_HOLDER);
                                                                                put("270",
                                                                                        CORRECTOR);
                                                                                put("273", CURATOR);
                                                                                put("275", DANCER);
                                                                                put("280",
                                                                                        DEDICATEE);
                                                                                put("290",
                                                                                        DEDICATOR);
                                                                                put("295",
                                                                                        DEGREE_GRANTOR);
                                                                                put("300", DIRECTOR);
                                                                                put("305",
                                                                                        DISSERTANT);
                                                                                put("310",
                                                                                        DISTRIBUTOR);
                                                                                put("320", DONOR);
                                                                                put("330",
                                                                                        DUBIOUS_AUTHOR);
                                                                                put("340", EDITOR);
                                                                                put("350", ENGRAVER);
                                                                                put("360", ETCHER);
                                                                                put("365", EXPERT);
                                                                                put("370",
                                                                                        FILM_EDITOR);
                                                                                put("380", FORGER);
                                                                                put("390",
                                                                                        FORMER_OWNER);
                                                                                put("400", FUNDER);
                                                                                put("410",
                                                                                        GRAPHIC_TECHNICIAN);
                                                                                put("420", HONOREE);
                                                                                put("430",
                                                                                        ILLUMINATOR);
                                                                                put("440",
                                                                                        ILLUSTRATOR);
                                                                                put("450",
                                                                                        INSCRIBER);
                                                                                put("460",
                                                                                        INTERVIEWEE);
                                                                                put("470",
                                                                                        INTERVIEWER);
                                                                                put("584", INVENTOR);
                                                                                put("480",
                                                                                        LIBRETTIST);
                                                                                put("490", LICENSEE);
                                                                                put("500", LICENSOR);
                                                                                put("510",
                                                                                        LITHOGRAPHER);
                                                                                put("520", LYRICIST);
                                                                                put("530",
                                                                                        METAL_ENGRAVER);
                                                                                put("540", MONITOR);
                                                                                put("545", MUSICIAN);
                                                                                put("550", NARRATOR);
                                                                                put("555", OPPONENT);
                                                                                put("557",
                                                                                        ORGANIZER_OF_MEETING);
                                                                                put("560",
                                                                                        ORIGINATOR);
                                                                                put("570", OTHER);
                                                                                put("580",
                                                                                        PAPERMAKER);
                                                                                put("582",
                                                                                        PATENT_APPLICANT);
                                                                                put("587",
                                                                                        PATENT_HOLDER);
                                                                                put("590",
                                                                                        PERFORMER);
                                                                                put("600",
                                                                                        PHOTOGRAPHER);
                                                                                put("610", PRINTER);
                                                                                put("620",
                                                                                        PRINTER_OF_PLATES);
                                                                                put("630", PRODUCER);
                                                                                put("635",
                                                                                        PROGRAMMER);
                                                                                put("640",
                                                                                        PROOFREADER);
                                                                                put("650",
                                                                                        PUBLISHER);
                                                                                put("651",
                                                                                        PUBLISHING_DIRECTOR);
                                                                                put("660",
                                                                                        RECIPIENT);
                                                                                put("670",
                                                                                        RECORDING_ENGINEER);
                                                                                put("673",
                                                                                        RESEARCH_TEAM_HEAD);
                                                                                put("677",
                                                                                        RESEARCH_TEAM_MEMBER);
                                                                                put("595",
                                                                                        RESEARCHER);
                                                                                put("675", REVIEWER);
                                                                                put("680",
                                                                                        RUBRICATOR);
                                                                                put("690",
                                                                                        SCENARIST);
                                                                                put("695",
                                                                                        SCIENTIFIC_ADVISOR);
                                                                                put("700", SCRIBE);
                                                                                put("705", SCULPTOR);
                                                                                put("710",
                                                                                        SECRETARY);
                                                                                put("720", SIGNER);
                                                                                put("721", SINGER);
                                                                                put("723", SPONSOR);
                                                                                put("725",
                                                                                        STANDARDS_BODY);
                                                                                put("727",
                                                                                        THESIS_ADVISOR);
                                                                                put("730",
                                                                                        TRANSLATOR);
                                                                                put("740",
                                                                                        TYPE_DESIGNER);
                                                                                put("750",
                                                                                        TYPOGRAPHER);
                                                                                put("755", VOCALIST);
                                                                                put("760",
                                                                                        WOOD_ENGRAVER);
                                                                                put("770",
                                                                                        WRITER_OF_ACCOMPANYING_MATERIAL);
                                                                            }
                                                                        };

    private static final HashMap<String, ContributionType> FROM_MARC21  = new HashMap<String, ContributionType>() {
                                                                            {
                                                                                put("act", ACTOR);
                                                                                put("adp", ADAPTER);
                                                                                put("anl", ANALYST);
                                                                                put("anm", ANIMATOR);
                                                                                put("ann",
                                                                                        ANNOTATOR);
                                                                                put("app",
                                                                                        APPLICANT);
                                                                                put("arc",
                                                                                        ARCHITECT);
                                                                                put("arr", ARRANGER);
                                                                                put("acp",
                                                                                        ART_COPYIST);
                                                                                put("art", ARTIST);
                                                                                put("ard",
                                                                                        ARTISTIC_DIRECTOR);
                                                                                put("asg", ASSIGNEE);
                                                                                put("asn",
                                                                                        ASSOCIATED_NAME);
                                                                                put("att",
                                                                                        ATTRIBUTED_NAME);
                                                                                put("auc",
                                                                                        AUCTIONEER);
                                                                                put("aut", AUTHOR);
                                                                                put("aqt",
                                                                                        AUTHOR_IN_QUOTATIONS_OR_TEXT_ABSTRACTS);
                                                                                put("aft",
                                                                                        AUTHOR_OF_AFTERWORD_COLOPHON_ETC_);
                                                                                put("aud",
                                                                                        AUTHOR_OF_DIALOG);
                                                                                put("aui",
                                                                                        AUTHOR_OF_INTRODUCTION);
                                                                                put("aus",
                                                                                        AUTHOR_OF_SCREENPLAY);
                                                                                put("ant",
                                                                                        BIBLIOGRAPHIC_ANTECEDENT);
                                                                                put("bnd", BINDER);
                                                                                put("bdd",
                                                                                        BINDING_DESIGNER);
                                                                                put("blw",
                                                                                        BLURB_WRITER);
                                                                                put("bkd",
                                                                                        BOOK_DESIGNER);
                                                                                put("bkp",
                                                                                        BOOK_PRODUCER);
                                                                                put("bjd",
                                                                                        BOOKJACKET_DESIGNER);
                                                                                put("bpd",
                                                                                        BOOKPLATE_DESIGNER);
                                                                                put("bsl",
                                                                                        BOOKSELLER);
                                                                                put("cll",
                                                                                        CALLIGRAPHER);
                                                                                put("ctg",
                                                                                        CARTOGRAPHER);
                                                                                put("cns", CENSOR);
                                                                                put("chr",
                                                                                        CHOREOGRAPHER);
                                                                                put("cng",
                                                                                        CINEMATOGRAPHER);
                                                                                put("cli", CLIENT);
                                                                                put("clb",
                                                                                        COLLABORATOR);
                                                                                put("col",
                                                                                        COLLECTOR);
                                                                                put("clt",
                                                                                        COLLOTYPER);
                                                                                put("clr", COLORIST);
                                                                                put("cmm",
                                                                                        COMMENTATOR);
                                                                                put("cwt",
                                                                                        COMMENTATOR_FOR_WRITTEN_TEXT);
                                                                                put("com", COMPILER);
                                                                                put("cpl",
                                                                                        COMPLAINANT);
                                                                                put("cpt",
                                                                                        COMPLAINANT_APPELLANT);
                                                                                put("cpe",
                                                                                        COMPLAINANT_APPELLEE);
                                                                                put("cmp", COMPOSER);
                                                                                put("cmt",
                                                                                        COMPOSITOR);
                                                                                put("ccp",
                                                                                        CONCEPTOR);
                                                                                put("cnd",
                                                                                        CONDUCTOR);
                                                                                put("con",
                                                                                        CONSERVATOR);
                                                                                put("csl",
                                                                                        CONSULTANT);
                                                                                put("csp",
                                                                                        CONSULTANT_TO_A_PROJECT);
                                                                                put("cos",
                                                                                        CONTESTANT);
                                                                                put("cot",
                                                                                        CONTESTANT_APPELLANT);
                                                                                put("coe",
                                                                                        CONTESTANT_APPELLEE);
                                                                                put("cts",
                                                                                        CONTESTEE);
                                                                                put("ctt",
                                                                                        CONTESTEE_APPELLANT);
                                                                                put("cte",
                                                                                        CONTESTEE_APPELLEE);
                                                                                put("ctr",
                                                                                        CONTRACTOR);
                                                                                put("ctb",
                                                                                        CONTRIBUTOR);
                                                                                put("cpc",
                                                                                        COPYRIGHT_CLAIMANT);
                                                                                put("cph",
                                                                                        COPYRIGHT_HOLDER);
                                                                                put("crr",
                                                                                        CORRECTOR);
                                                                                put("crp",
                                                                                        CORRESPONDENT);
                                                                                put("cst",
                                                                                        COSTUME_DESIGNER);
                                                                                put("cov",
                                                                                        COVER_DESIGNER);
                                                                                put("cre", CREATOR);
                                                                                put("cur", CURATOR);
                                                                                put("dnc", DANCER);
                                                                                put("dtc",
                                                                                        DATA_CONTRIBUTOR);
                                                                                put("dtm",
                                                                                        DATA_MANAGER);
                                                                                put("dte",
                                                                                        DEDICATEE);
                                                                                put("dto",
                                                                                        DEDICATOR);
                                                                                put("dfd",
                                                                                        DEFENDANT);
                                                                                put("dft",
                                                                                        DEFENDANT_APPELLANT);
                                                                                put("dfe",
                                                                                        DEFENDANT_APPELLEE);
                                                                                put("dgg",
                                                                                        DEGREE_GRANTOR);
                                                                                put("dln",
                                                                                        DELINEATOR);
                                                                                put("dpc", DEPICTED);
                                                                                put("dpt",
                                                                                        DEPOSITOR);
                                                                                put("dsr", DESIGNER);
                                                                                put("drt", DIRECTOR);
                                                                                put("dis",
                                                                                        DISSERTANT);
                                                                                put("dpb",
                                                                                        DISTRIBUTION_PLACE);
                                                                                put("dst",
                                                                                        DISTRIBUTOR);
                                                                                put("dnr", DONOR);
                                                                                put("drm",
                                                                                        DRAFTSMAN);
                                                                                put("dub",
                                                                                        DUBIOUS_AUTHOR);
                                                                                put("edt", EDITOR);
                                                                                put("elg",
                                                                                        ELECTRICIAN);
                                                                                put("elt",
                                                                                        ELECTROTYPER);
                                                                                put("eng", ENGINEER);
                                                                                put("egr", ENGRAVER);
                                                                                put("etr", ETCHER);
                                                                                put("evp",
                                                                                        EVENT_PLACE);
                                                                                put("exp", EXPERT);
                                                                                put("fac",
                                                                                        FACSIMILIST);
                                                                                put("fld",
                                                                                        FIELD_DIRECTOR);
                                                                                put("flm",
                                                                                        FILM_EDITOR);
                                                                                put("fpy",
                                                                                        FIRST_PARTY);
                                                                                put("frg", FORGER);
                                                                                put("fmo",
                                                                                        FORMER_OWNER);
                                                                                put("fnd", FUNDER);
                                                                                put("gis",
                                                                                        GEOGRAPHIC_INFORMATION_SPECIALIST);
                                                                                put("-grt",
                                                                                        GRAPHIC_TECHNICIAN);
                                                                                put("hnr", HONOREE);
                                                                                put("hst", HOST);
                                                                                put("ilu",
                                                                                        ILLUMINATOR);
                                                                                put("ill",
                                                                                        ILLUSTRATOR);
                                                                                put("ins",
                                                                                        INSCRIBER);
                                                                                put("itr",
                                                                                        INSTRUMENTALIST);
                                                                                put("ive",
                                                                                        INTERVIEWEE);
                                                                                put("ivr",
                                                                                        INTERVIEWER);
                                                                                put("inv", INVENTOR);
                                                                                put("lbr",
                                                                                        LABORATORY);
                                                                                put("ldr",
                                                                                        LABORATORY_DIRECTOR);
                                                                                put("lsa",
                                                                                        LANDSCAPE_ARCHITECT);
                                                                                put("led", LEAD);
                                                                                put("len", LENDER);
                                                                                put("lil", LIBELANT);
                                                                                put("lit",
                                                                                        LIBELANT_APPELLANT);
                                                                                put("lie",
                                                                                        LIBELANT_APPELLEE);
                                                                                put("lel", LIBELEE);
                                                                                put("let",
                                                                                        LIBELEE_APPELLANT);
                                                                                put("lee",
                                                                                        LIBELEE_APPELLEE);
                                                                                put("lbt",
                                                                                        LIBRETTIST);
                                                                                put("lse", LICENSEE);
                                                                                put("lso", LICENSOR);
                                                                                put("lgd",
                                                                                        LIGHTING_DESIGNER);
                                                                                put("ltg",
                                                                                        LITHOGRAPHER);
                                                                                put("lyr", LYRICIST);
                                                                                put("mfp",
                                                                                        MANUFACTURE_PLACE);
                                                                                put("mfr",
                                                                                        MANUFACTURER);
                                                                                put("mrb", MARBLER);
                                                                                put("mrk",
                                                                                        MARKUP_EDITOR);
                                                                                put("mdc",
                                                                                        METADATA_CONTACT);
                                                                                put("mte",
                                                                                        METAL_ENGRAVER);
                                                                                put("mod",
                                                                                        MODERATOR);
                                                                                put("mon", MONITOR);
                                                                                put("mcp",
                                                                                        MUSIC_COPYIST);
                                                                                put("msd",
                                                                                        MUSICAL_DIRECTOR);
                                                                                put("mus", MUSICIAN);
                                                                                put("nrt", NARRATOR);
                                                                                put("opn", OPPONENT);
                                                                                put("orm",
                                                                                        ORGANIZER_OF_MEETING);
                                                                                put("org",
                                                                                        ORIGINATOR);
                                                                                put("oth", OTHER);
                                                                                put("own", OWNER);
                                                                                put("ppm",
                                                                                        PAPERMAKER);
                                                                                put("pta",
                                                                                        PATENT_APPLICANT);
                                                                                put("pth",
                                                                                        PATENT_HOLDER);
                                                                                put("pat", PATRON);
                                                                                put("prf",
                                                                                        PERFORMER);
                                                                                put("pma",
                                                                                        PERMITTING_AGENCY);
                                                                                put("pht",
                                                                                        PHOTOGRAPHER);
                                                                                put("ptf",
                                                                                        PLAINTIFF);
                                                                                put("pte",
                                                                                        PLAINTIFF_APPELLEE);
                                                                                put("ptt",
                                                                                        PLAINTIFF_APPELLANT);
                                                                                put("plt",
                                                                                        PLATEMAKER);
                                                                                put("prt", PRINTER);
                                                                                put("pop",
                                                                                        PRINTER_OF_PLATES);
                                                                                put("prm",
                                                                                        PRINTMAKER);
                                                                                put("prc",
                                                                                        PROCESS_CONTACT);
                                                                                put("pro", PRODUCER);
                                                                                put("pmn",
                                                                                        PRODUCTION_MANAGER);
                                                                                put("prd",
                                                                                        PRODUCTION_PERSONNEL);
                                                                                put("prp",
                                                                                        PRODUCTION_PLACE);
                                                                                put("prg",
                                                                                        PROGRAMMER);
                                                                                put("pdr",
                                                                                        PROJECT_DIRECTOR);
                                                                                put("pfr",
                                                                                        PROOFREADER);
                                                                                put("pup",
                                                                                        PUBLICATION_PLACE);
                                                                                put("pbl",
                                                                                        PUBLISHER);
                                                                                put("pbd",
                                                                                        PUBLISHING_DIRECTOR);
                                                                                put("ppt",
                                                                                        PUPPETEER);
                                                                                put("rcp",
                                                                                        RECIPIENT);
                                                                                put("rce",
                                                                                        RECORDING_ENGINEER);
                                                                                put("red", REDACTOR);
                                                                                put("ren", RENDERER);
                                                                                put("rpt", REPORTER);
                                                                                put("rps",
                                                                                        REPOSITORY);
                                                                                put("rth",
                                                                                        RESEARCH_TEAM_HEAD);
                                                                                put("rtm",
                                                                                        RESEARCH_TEAM_MEMBER);
                                                                                put("res",
                                                                                        RESEARCHER);
                                                                                put("rsp",
                                                                                        RESPONDENT);
                                                                                put("rst",
                                                                                        RESPONDENT_APPELLANT);
                                                                                put("rse",
                                                                                        RESPONDENT_APPELLEE);
                                                                                put("rpy",
                                                                                        RESPONSIBLE_PARTY);
                                                                                put("rsg", RESTAGER);
                                                                                put("rev", REVIEWER);
                                                                                put("rbr",
                                                                                        RUBRICATOR);
                                                                                put("sce",
                                                                                        SCENARIST);
                                                                                put("sad",
                                                                                        SCIENTIFIC_ADVISOR);
                                                                                put("scr", SCRIBE);
                                                                                put("scl", SCULPTOR);
                                                                                put("spy",
                                                                                        SECOND_PARTY);
                                                                                put("sec",
                                                                                        SECRETARY);
                                                                                put("std",
                                                                                        SET_DESIGNER);
                                                                                put("sgn", SIGNER);
                                                                                put("sng", SINGER);
                                                                                put("sds",
                                                                                        SOUND_DESIGNER);
                                                                                put("spk", SPEAKER);
                                                                                put("spn", SPONSOR);
                                                                                put("stm",
                                                                                        STAGE_MANAGER);
                                                                                put("stn",
                                                                                        STANDARDS_BODY);
                                                                                put("str",
                                                                                        STEREOTYPER);
                                                                                put("stl",
                                                                                        STORYTELLER);
                                                                                put("sht",
                                                                                        SUPPORTING_HOST);
                                                                                put("srv", SURVEYOR);
                                                                                put("tch", TEACHER);
                                                                                put("tcd",
                                                                                        TECHNICAL_DIRECTOR);
                                                                                put("ths",
                                                                                        THESIS_ADVISOR);
                                                                                put("trc",
                                                                                        TRANSCRIBER);
                                                                                put("trl",
                                                                                        TRANSLATOR);
                                                                                put("tyd",
                                                                                        TYPE_DESIGNER);
                                                                                put("tyg",
                                                                                        TYPOGRAPHER);
                                                                                put("uvp",
                                                                                        UNIVERSITY_PLACE);
                                                                                put("vdg",
                                                                                        VIDEOGRAPHER);
                                                                                put("voc", VOCALIST);
                                                                                put("wit", WITNESS);
                                                                                put("wde",
                                                                                        WOOD_ENGRAVER);
                                                                                put("wdc",
                                                                                        WOODCUTTER);
                                                                                put("wam",
                                                                                        WRITER_OF_ACCOMPANYING_MATERIAL);
                                                                            }
                                                                        };

    /**
     * @param relatorCode
     * @return corresponding ContributionType
     */
    public static ContributionType fromUnimarc(String relatorCode) {
        return FROM_UNIMARC.get(relatorCode);
    }

    /**
     * @param relatorCode
     * @return corresponding ContributionType
     */
    public static ContributionType fromMarc21(String relatorCode) {
        return FROM_MARC21.get(relatorCode);
    }

}
