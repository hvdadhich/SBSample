package com.wu.qcc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AccountDAO {

    @SerializedName("Account")
    private String account;

    @SerializedName("BranchId")
    private int branchId;

    @SerializedName("RevisionId")
    private int revisionId;

    @SerializedName("RecordId")
    private int recordId;

    @SerializedName("InsertedOn")
    private Date insertedOn;

    @SerializedName("DataBuffer")
    private String dataBuffer;
    
    
    private Map<String,String> customData = new HashMap();
    
    

    public AccountDAO(int branchId, int revisionId, String account, String dataBuffer, Date insertedOn) {
        this(branchId, revisionId, account, dataBuffer);
        this.setInsertedOn(insertedOn);
    }

    public AccountDAO(int branchId, int revisionId, String account, String dataBuffer) {
        this(account, dataBuffer);
        this.setBranchId(branchId);
        this.setRevisionId(revisionId);
    }

    public AccountDAO(String account, String dataBuffer) {
        this.account = account;
        this.setDataBuffer(dataBuffer);
        this.setInsertedOn(new Date());
    }

    public String getAccount() {
        return account;
    }

    public Date getInsertedOn() {
        return insertedOn;
    }

    public void setInsertedOn(Date insertedOn) {
        this.insertedOn = insertedOn;
    }

    public String getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(String dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this, AccountDAO.class);
        return json;
    }
    
    
}