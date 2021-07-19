package com.wu.qcc.model;

import java.util.Date;

public class RevisionBuilder {
    private long branchId;
    private long revisionId;
    private long fileId;
    private String status;
    private String lastUpdatedOn;
    private String lastUpdateBy;

    public RevisionBuilder setBranchId(long branchId) {
        this.branchId = branchId;
        return this;
    }

    public RevisionBuilder setRevisionId(long revisionId) {
        this.revisionId = revisionId;
        return this;
    }

    public RevisionBuilder setFileId(long fileId) {
        this.fileId = fileId;
        return this;
    }

    public RevisionBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public RevisionBuilder setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    public RevisionBuilder setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
        return this;
    }

    public Revision createRevision(){
        return new Revision(branchId, revisionId, status, lastUpdatedOn, lastUpdateBy, fileId);
    }

}
