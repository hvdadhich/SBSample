package com.wu.qcc.model;


import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Branch {
    private int branchId;
    private int revisionSequence;
    private String lastUpdateOn;
    private List<Revision> revisions = new ArrayList<>(5);

    public int getBranchId() {
        return branchId;
    }

    public Branch setBranchId(int branchId) {
        this.branchId = branchId;
        return this;
    }

    public int getRevisionSequence() {
        return revisionSequence;
    }

    public Branch setRevisionSequence(int revisionSequence) {
        this.revisionSequence = revisionSequence;
        return this;
    }

    public String getLastUpdateOn() {
        return lastUpdateOn;
    }

    public Branch setLastUpdateOn(String lastUpdateOn) {
        this.lastUpdateOn = lastUpdateOn;
        return this;
    }

    public List<Revision> getRevisions() {
        // TODO: immutable
        return revisions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("branchId", branchId)
                .add("revisionSequence", revisionSequence)
                .add("lastUpdateOn", lastUpdateOn)
                .add("revisions", revisions)
                .toString();
    }
}
