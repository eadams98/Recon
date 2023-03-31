package com.idea.recon.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue(
        strategy = GenerationType.AUTO
    )
    private Integer id;
    private String schoolName;

    public School() {
    }

    public School(final Integer id, final String schoolName) {
        this.id = id;
        this.schoolName = schoolName;
    }

    public Integer getId() {
        return this.id;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setSchoolName(final String schoolName) {
        this.schoolName = schoolName;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof School)) {
            return false;
        } else {
            School other = (School)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                Object this$schoolName = this.getSchoolName();
                Object other$schoolName = other.getSchoolName();
                if (this$schoolName == null) {
                    if (other$schoolName != null) {
                        return false;
                    }
                } else if (!this$schoolName.equals(other$schoolName)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof School;
    }

    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        return result;
    }

    public String toString() {
        Integer var10000 = this.getId();
        return "School(id=" + var10000 + ", schoolName=" + this.getSchoolName() + ")";
    }
}
