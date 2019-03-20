package com.signzy.ubuntu.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Directory entity.
 */
public class DirectoryDTO implements Serializable {

    private Long id;

    private String name;

    private String parent;

    private String type;

    private Boolean isDirectory;

    private Instant timeStamp;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DirectoryDTO directoryDTO = (DirectoryDTO) o;
        if (directoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), directoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DirectoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", parent='" + getParent() + "'" +
            ", type='" + getType() + "'" +
            ", isDirectory='" + isIsDirectory() + "'" +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
