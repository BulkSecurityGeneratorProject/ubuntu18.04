package com.signzy.ubuntu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Directory entity. This class is used in DirectoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /directories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DirectoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter parent;

    private StringFilter type;

    private BooleanFilter isDirectory;

    private InstantFilter timeStamp;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getParent() {
        return parent;
    }

    public void setParent(StringFilter parent) {
        this.parent = parent;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public BooleanFilter getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(BooleanFilter isDirectory) {
        this.isDirectory = isDirectory;
    }

    public InstantFilter getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(InstantFilter timeStamp) {
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
        final DirectoryCriteria that = (DirectoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(parent, that.parent) &&
            Objects.equals(type, that.type) &&
            Objects.equals(isDirectory, that.isDirectory) &&
            Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        parent,
        type,
        isDirectory,
        timeStamp
        );
    }

    @Override
    public String toString() {
        return "DirectoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (parent != null ? "parent=" + parent + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (isDirectory != null ? "isDirectory=" + isDirectory + ", " : "") +
                (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "") +
            "}";
    }

}
