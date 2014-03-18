/* PartySubject.java - created on 4 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.subject;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.time.Temporal;

/**
 * Holding information typical to subjects like {@link Topic} for example.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 4 de Jul de 2011
 */
public class Subject {
    /**
     * Form subdivision that designates a specific kind or genre of material as defined by the
     * thesaurus being used.
     */
    @FieldId(1)
    protected String        formSubdivision;
    /**
     * Subject subdivision that is not more appropriately contained in other subdivisions
     */
    @FieldId(2)
    protected String        generalSubdivision;
    /**
     * Subject subdivision that represents a period of time
     */
    @FieldId(3)
    protected Temporal      chronologicalSubdivision;
    /**
     * Geographic subject subdivision
     */
    @FieldId(4)
    protected SpatialEntity geographicSubdivision;

    /**
     * Creates a new instance of this class.
     */
    public Subject() {
        super();
    }

    /**
     * @return Form subdivision that designates a specific kind or genre of material as defined by
     *         the thesaurus being used.
     */
    public String getFormSubdivision() {
        return formSubdivision;
    }

    /**
     * @param formSubdivision
     *            Form subdivision that designates a specific kind or genre of material as defined
     *            by the thesaurus being used.
     */
    public void setFormSubdivision(String formSubdivision) {
        this.formSubdivision = formSubdivision;
    }

    /**
     * @return Subject subdivision that is not more appropriately contained in other subdivisions
     */
    public String getGeneralSubdivision() {
        return generalSubdivision;
    }

    /**
     * @param generalSubdivision
     *            Subject subdivision that is not more appropriately contained in other subdivisions
     */
    public void setGeneralSubdivision(String generalSubdivision) {
        this.generalSubdivision = generalSubdivision;
    }

    /**
     * @return Subject subdivision that represents a period of time
     */
    public Temporal getChronologicalSubdivision() {
        return chronologicalSubdivision;
    }

    /**
     * @param chronologicalSubdivision
     *            Subject subdivision that represents a period of time
     */
    public void setChronologicalSubdivision(Temporal chronologicalSubdivision) {
        this.chronologicalSubdivision = chronologicalSubdivision;
    }

    /**
     * @return Geographic subject subdivision
     */
    public SpatialEntity getGeographicSubdivision() {
        return geographicSubdivision;
    }

    /**
     * @param geographicSubdivision
     *            Geographic subject subdivision
     */
    public void setGeographicSubdivision(SpatialEntity geographicSubdivision) {
        this.geographicSubdivision = geographicSubdivision;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((formSubdivision == null) ? 0 : formSubdivision.hashCode());
        result = prime * result +
                 ((generalSubdivision == null) ? 0 : generalSubdivision.hashCode());
        result = prime * result +
                 ((chronologicalSubdivision == null) ? 0 : chronologicalSubdivision.hashCode());
        result = prime * result +
                 ((geographicSubdivision == null) ? 0 : geographicSubdivision.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Subject other = (Subject)obj;
        if (formSubdivision == null) {
            if (other.formSubdivision != null) return false;
        } else if (!formSubdivision.equals(other.formSubdivision)) return false;
        if (formSubdivision == null) {
            if (other.formSubdivision != null) return false;
        } else if (!formSubdivision.equals(other.formSubdivision)) return false;
        if (generalSubdivision == null) {
            if (other.generalSubdivision != null) return false;
        } else if (!generalSubdivision.equals(other.generalSubdivision)) return false;
        if (chronologicalSubdivision == null) {
            if (other.chronologicalSubdivision != null) return false;
        } else if (!chronologicalSubdivision.equals(other.chronologicalSubdivision)) return false;
        if (geographicSubdivision == null) {
            if (other.geographicSubdivision != null) return false;
        } else if (!geographicSubdivision.equals(other.geographicSubdivision)) return false;
        return true;
    }

    /**
     * @return subject heading label
     */
    public String getSubjectHeadingDisplay() {
        StringBuilder builder = new StringBuilder();
        if (formSubdivision != null) {
            builder.append("--" + formSubdivision);
        }
        if (generalSubdivision != null) {
            builder.append("--" + generalSubdivision);
        }
        if (chronologicalSubdivision != null) {
            builder.append("--" + chronologicalSubdivision);
        }
        if (geographicSubdivision != null) {
            builder.append("--" + geographicSubdivision);
        }
        return builder.toString();
    }

    /**
     * @return human readable string
     */
    public String getDisplay() {
        StringBuilder builder = new StringBuilder();
        if (formSubdivision != null) {
            builder.append(", " + formSubdivision);
        }
        if (generalSubdivision != null) {
            builder.append(", " + generalSubdivision);
        }
        if (chronologicalSubdivision != null) {
            builder.append(", " + chronologicalSubdivision);
        }
        if (geographicSubdivision != null) {
            builder.append(", " + geographicSubdivision);
        }
        return builder.toString();
    }
}
