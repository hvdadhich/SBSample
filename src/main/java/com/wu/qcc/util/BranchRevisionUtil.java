package com.wu.qcc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.couchbase.client.core.message.kv.subdoc.multi.Lookup;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonLongDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.subdoc.DocumentFragment;
import com.wu.qcc.model.Branch;
import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.Revision;
import com.wu.qcc.repository.MetadataRepository;

public class BranchRevisionUtil {
	private static final Logger log = LogManager.getLogger(BranchRevisionUtil.class);
	@Autowired
	private BranchRevisionDAO branchRevisionDao;
	
	@Autowired
	private MetadataRepository metaDatarepo;

	private static final SimpleDateFormat DefaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	
	
	public  List<Revision> getAllRevision(int branchIndex,String billerPrefix) {
		try {
	    List<Revision> revisions = new ArrayList<>(10);
		DocumentFragment<Lookup> items =
				 metaDatarepo.getCouchbaseOperations().getCouchbaseBucket().lookupIn(getBillerMetaDataName(billerPrefix))
	                     .get(String.format("branches[%s].revisions", branchIndex)).execute();
		
		JsonArray fetchedList = (JsonArray) items.content(0);
        Iterator iterator = fetchedList.iterator();
        while (iterator.hasNext()) {
           JsonObject obj = (JsonObject) iterator.next();
           Revision rev = new Revision(obj.getInt("branchId"), obj.getInt("revisionId"),
                 obj.getString("status"),
                 obj.getString("lastUpdatedOn"),
                 obj.getString("lastUpdateBy"), obj.getLong("mamfileId"));
           revisions.add(rev);
        }

        return revisions;
     } catch (Exception e) {
    	 log.error("Exception when getting All Revision. Details: " + e.toString());
     }
     return Collections.emptyList();
  }

	
	
	public static String getBillerMetaDataName(String billerPrefix2) {
		return "mam-biller-"+billerPrefix2.toLowerCase();
	}
	
	
	public  void updateMetaDocument(String billerPrefix, int branchIndex, int revisionIndex, Revision revision) throws Exception {
		 Bucket bucket = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket();

	      try {
	    	  revision.setLastUpdatedOn(DefaultFormat.format(new Date()));
	    	  revision.setLastUpdateBy("system");

	    	  
	         String pathBase = String.format("branches[%s].revisions[%s]", branchIndex, revisionIndex);
	         DocumentFragment fragment = bucket.mutateIn(getBillerMetaDataName(billerPrefix))
	               .replace(pathBase + ".status", revision.getStatus())
	               .replace(pathBase + ".lastUpdateBy", revision.getLastUpdateBy())
	               .replace(pathBase + ".lastUpdatedOn",
	                     revision.getLastUpdatedOn())
	               .execute();

	      } catch (Exception e) {
	         log.error("Exception when getting single. Details: " + e.toString());
	        throw (new Exception("Wrong update in revision"));
	      }

	      
		
	}
	
	public  List<Branch> getAllBranches(String key) {
	      Bucket bucket = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket();

	      try {
	         List<Branch> branches = new ArrayList<>(10);

	         DocumentFragment<Lookup> items = bucket
	               .lookupIn(getBillerMetaDataName(key)).get("branches").execute();

	         JsonArray fetchedList = (JsonArray) items.content(0);
	         Iterator iterator = fetchedList.iterator();
	         while (iterator.hasNext()) {
	            JsonObject obj = (JsonObject) iterator.next();
	            Branch branch = new Branch().setBranchId(obj.getInt("branchId"))
	                  .setRevisionSequence(obj.getInt("revisionSequence"))
	                  .setLastUpdateOn((obj.getString("lastUpdateOn")));
	            branches.add(branch);
	         }
	         return branches;
	      } catch (Exception e) {
	         log.error(String.format("Cannot get all branches from %s. \nException details are\n\t: ", key, e));
	      
	      }

	      return Collections.emptyList();
	   }

 
 public static int getIndexOfId(int branchId, List<Branch> list) {
	      int index = -1;
	      for (int i = 0; i < list.size() && index < 0; i++) {
	         Branch single = list.get(i);
	         if (single.getBranchId() == branchId) {
	            index = i;
	         }
	      }
	      return index;
	   }
 
 
 public  long getNextFileId() {
	      Bucket bucket = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket();
	      final String key = "mamfile-counter";
	      JsonLongDocument rv = bucket.counter(key, 1, 1);
	      return rv.content();
	   }

 
 
}
