/* ResourceRelation.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * Types os relations between resources
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 15 de Nov de 2012
 */
public enum ResourceRelation {
    /**
     * see dc:relation 787 - Other Relationship Entry 488 Other Related Work
     */
    RELATION,
    /**
     * see dcterms
     */
    IS_VERSION_OF,
    /**
     * see dcterms
     */
    HAS_VERSION,
    /**
     * see dcterms
     */
    IS_REPLACED_BY,
    /**
     * see dcterms
     */
    REPLACES,
    /**
     * see dcterms
     */
    IS_REQUIRED_BY,
    /**
     * see dcterms
     */
    REQUIRES,
    /**
     * see dcterms 773 - Host Item Entry 461 Set 463 Piece 462 Subset - with LEADER position 8 != 1
     */
    IS_PART_OF,
    /**
     * see dcterms ? 774 - Constituent Unit Entry 464 Piece-Analytic 462 Subset - with LEADER
     * position 8 = 1
     */
    HAS_PART,
    /**
     * see dcterms
     */
    IS_REFERENCED_BY,
    /**
     * see dcterms
     */
    REFERENCES,
    /**
     * see dcterms
     */
    IS_FORMAT_OF,
    /**
     * see dcterms
     */
    HAS_FORMAT,
    /**
     * see dcterms
     */
    CONFORMS_TO,

    /**
     * 760 - Main Series Entry 410 Series
     */
    IS_SUBSERIES_OF,

    /**
     * 762 - Subseries Entry 411 SubSeries
     */
    HAS_SUBSERIES,

    /**
     * 765 - Original Language Entry 454 Translation Of
     */
    IS_TRANSLATION_OF,

    /**
     * 767 - Translation Entry 453 Translated As
     */
    HAS_TRANSLATION,

    /**
     * 770 - Supplement/Special Issue Entry 421 Supplement
     */
    HAS_SUPPLEMENT,

    /**
     * 772 - Supplement Parent Entry 422 Parent of Supplement
     */
    IS_SUPPLEMENT_OF,

    /**
     * 775 - Other Edition Entry 451 Other Edition in the Same Medium 776 - Additional Physical Form
     * Entry 452 Other Edition in Another Medium
     */
    HAS_OTHER_EDITION,

    /**
     * 777 - Issued With Entry 423 Issued With
     */
    IS_ISSUE_WITH,

    /**
     * 780 - Preceding Entry 0 - Continues 430 CONTINUES
     */
    IS_CONTINUATION_OF,

    /**
     * 780 - Preceding Entry 1 - Continues in part 431 CONTINUES IN PART
     */
    IS_PARTIAL_CONTINUATION_OF,

    /**
     * 780 - Preceding Entry 2 - Supersedes 432 SUPERSEDES
     */
    HAS_SUPERSEDED,

    /**
     * 780 - Preceding Entry 3 - Supersedes in part 433 SUPERSEDES IN PART
     */
    HAS_SUPERSEDED_IN_PART,

    /**
     * 780 - Preceding Entry 5 - Absorbed 434 ABSORBED
     */
    HAS_ABSORVED,

    /**
     * 780 - Preceding Entry 6 - Absorbed in part 435 ABSORBED IN PART
     */
    HAS_ABSORVED_IN_PART,

    /**
     * 780 - Preceding Entry 7 - Separated from 437 SEPARATED FROM
     */
    WAS_SEPARATED_FROM,

    /**
     * 785 - Succeeding Entry 0 - Continued by 440 CONTINUED BY
     */
    WAS_CONTINUED_BY,

    /**
     * 785 - Succeeding Entry 1 - Continued in part by 441 CONTINUED IN PART BY
     */
    WAS_CONTINUED_IN_PART_BY,

    /**
     * 785 - Succeeding Entry 2 - Superseded by 442 SUPERSEDED BY
     */
    WAS_SUPERSEDED_BY,

    /**
     * 785 - Succeeding Entry 3 - Superseded in part by 443 SUPERSEDED IN PART BY
     */
    WAS_SUPERSEDED_IN_PART_BY,

    /**
     * 785 - Succeeding Entry 4 - Absorbed by 444 ABSORBED BY
     */
    WAS_ABSORBED_BY,

    /**
     * 785 - Succeeding Entry 5 - Absorbed in part by 445 ABSORBED IN PART BY
     */
    WAS_ABSORBED_IN_PART_BY,

    /**
     * 780 - Preceding Entry 4 - Formed by the union of ... and ... 436 Formed by Merger of ...,
     * ..., AND ...
     */
    WAS_FORMED_BY_MERGER_OF,

    /**
     * 785 - Succeeding Entry 6 - Split into ... and ... 446 Split into ..., ..., And ...
     */
    HAS_SPLIT_INTO,

    /**
     * 785 - Succeeding Entry 7 - Merged with ... to form ... 447 Merged With ... And ... To Form
     * ... HAS_MERGED_WITH (first occurrences) HAS_FORMED (last occurrence)
     */
    HAS_MERGED_WITH,

    /**
     * 785 - Succeeding Entry 7 - Merged with ... to form ... 447 Merged With ... And ... To Form
     * ... HAS_MERGED_WITH (first occurrences) HAS_FORMED (last occurrence)
     */
    HAS_FORMED,

    /**
     * 785 - Succeeding Entry 8 - Changed back to 448 Changed Back To
     */
    HAS_CHANGED_BACK_TO,

    /**
     * 786 - Data Source Entry
     */
    HAS_DATA_SOURCE,

    /**
     * 455 Reproduction Of
     */
    IS_REPRODUCTION_OF,

    /**
     * 456 Reproduced As
     */
    IS_REPRODUCED_BY,

    /**
     * 470 Item Reviewed
     */
    IS_REVIEW_OF,

    /**
     * 481 Also Bound in this Volume 482 Bound With
     */
    IS_BOUND_WITH,

    /**
     * edm:isSimilarTo http://www.cidoc-crm.org/rdfs/cidoc-crm#P130F.shows_features_of - The most
     * generic derivation property, covering also the case of questionable derivation. Is Similar To
     * asserts that parts of the contents of one resource exhibit common features with respect to
     * ideas, shapes, structures, colors, words, plots, topics with the contents of the related
     * resource. Those common features may be attributed to a common origin or influence (in
     * particular for derivation), but also to more generic cultural or psychological factors.
     */
    IS_SIMILAR_TO,
}
