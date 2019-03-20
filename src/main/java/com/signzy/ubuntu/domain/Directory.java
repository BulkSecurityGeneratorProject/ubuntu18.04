package com.signzy.ubuntu.domain;



import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Directory.
 */
@Entity
@Table(name = "directory")
public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "parent")
    private String parent;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "is_directory")
    private Boolean isDirectory;

    @Column(name = "time_stamp")
    private Instant timeStamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Directory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public Directory parent(String parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public Directory type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isIsDirectory() {
        return isDirectory;
    }

    public Directory isDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
        return this;
    }

    public void setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public Directory timeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Directory directory = (Directory) o;
        if (directory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), directory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Directory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", parent='" + getParent() + "'" +
            ", type='" + getType() + "'" +
            ", isDirectory='" + isIsDirectory() + "'" +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
