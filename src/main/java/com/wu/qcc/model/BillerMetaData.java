package com.wu.qcc.model;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Biller entity
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillerMetaData {

	@Id
	private String id;
	@Field
   private  String prefix;
	@Field
   private  String name;
	@Field
   private int currentBranch;
	@Field
   private String createdOn;
	@Field
   private int branchSequence;
//	@Field
//   private byte[] salt;
//	@Field
//   private boolean isEncryped;
	@Field
	private String type;
	
	@Field
	private List<Branch> branches;

   
//   public BillerMetaData(String prefix, String name) {
//      this.prefix = prefix;
//      this.name = name;
//   }

//   public BillerMetaData(String prefix, String name, byte[] salt, boolean isEncryped) {
//	      this.prefix = prefix;
//	      this.name = name;
////	      this.salt=salt;
////	      this.isEncryped=isEncryped;
//	   }
//   public String getPrefix() {
//      return prefix;
//   }
//
//   public String getName() {
//      return name;
//   }
//
//   public BillerMetaData setName(String name) {
//      this.name = name;
//      return this;
//   }
//
//   public int getCurrentBranch() {
//      return currentBranch;
//   }
//
//   public BillerMetaData setCurrentBranch(int currentBranch) {
//      this.currentBranch = currentBranch;
//      return this;
//   }
//
//   public Date getCreatedOn() {
//      return createdOn;
//   }
//
//   public BillerMetaData setCreatedOn(Date createdOn) {
//      this.createdOn = createdOn;
//      return this;
//   }
//
//   public int getBranchSequence() {
//      return branchSequence;
//   }
//
//   public BillerMetaData setBranchSequence(int branchSequence) {
//      this.branchSequence = branchSequence;
//      return this;
//   }   

  
//@Override
//   public String toString() {
//      return MoreObjects.toStringHelper(this).add("prefix", prefix).add("name", name)
//            .add("currentBranch", currentBranch).add("createdOn", createdOn)
//            .add("branchSequence", branchSequence).toString();
//   }
}
