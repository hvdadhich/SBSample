package com.wu.qcc.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entity that will carry out all the fields for each Revision
 */
public class Revision {

    private long branchId;
    private long revisionId;
    private long fileId;
    private String status;
    private String lastUpdatedOn;
    private String lastUpdateBy;

    public Revision(long branchId, long revisionId, String status, String lastUpdatedOn, String lastUpdateBy, long fileId) {
        this.branchId = branchId;
        this.revisionId = revisionId;
        this.status = status;
        this.lastUpdatedOn = lastUpdatedOn;
        this.lastUpdateBy = lastUpdateBy;
        this.fileId = fileId;
    }

    public Revision(int branchId, int revisionId, int fileId) {
        this.branchId = branchId;
        this.revisionId = revisionId;
        this.fileId = fileId;
    }

    public Revision() {

    }

    public long getBranchId() {
        return branchId;
    }

    public long getRevisionId() {
        return revisionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastUpdatedOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format( new Date());
        this.lastUpdateBy = "System";
    }

    public Revision setRevisionId(long revisionId) {
        this.revisionId = revisionId;
        return this;
    }

    public Revision setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    public Revision setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
        return this;
    }

    public Revision setBranchId(long branchId) {
        this.branchId = branchId;
        return this;
    }

    public Revision setFileId(long fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public long getFileId() {
        return fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Revision)) {
            return false;
        }
        Revision revision = (Revision) o;
        return branchId == revision.branchId &&
               revisionId == revision.revisionId &&
               fileId == revision.fileId &&
               Objects.equal(status, revision.status);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(branchId, revisionId, fileId, status);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("branchId", branchId)
                .add("revisionId", revisionId)
                .add("fileId", fileId)
                .add("status", status)
                .add("lastUpdatedOn", lastUpdatedOn)
                .add("lastUpdateBy", lastUpdateBy)
                .toString();
    }

    public static class Builder {
        private final Revision revision;

        private Builder(int revisionId, int branchId, int fileId) {
            revision = new Revision(branchId, revisionId, fileId);
        }

        public static Builder create(int revisionId, int branchId, int fileId) {
            return new Builder(revisionId, branchId, fileId);
        }

        public Builder setStatus(String status) {
            revision.setStatus(status);
            return this;
        }

        public Revision get() {
            return revision;
        }
    }

}

