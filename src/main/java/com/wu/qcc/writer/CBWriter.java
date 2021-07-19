package com.wu.qcc.writer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.wu.qcc.interfaces.ExtenedBillerListInterface;
import com.wu.qcc.interfaces.factory.ExtendedBillerListFactory;
import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.repository.MetadataRepository;
import com.wu.qcc.util.FlowSingleton;
import com.wu.qcc.util.StatSingleton;

import lombok.Data;

@Data
public class CBWriter implements ItemWriter<AccountDAO> {
	@Autowired
	BranchRevisionDAO brdao;
	
	@Autowired
	MetadataRepository metarepo;
	
	private boolean customAccountWrite;
	
	private String billerPrefix;

	private static final SimpleDateFormat DefaultFormat =
	         new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@Autowired
	ExtendedBillerListFactory billerFactory;
	
	ExtenedBillerListInterface billerlist;
	
	
	//Exception Handling is Pending.
	//Implement retry Listener
	
	//
	
	@Override
	public void write(List<? extends AccountDAO> items) throws Exception {
		
		 
		try{String accountId = null;
	      String dataBuffer=null;
	      String accountinDoc=null;
	      
	      Bucket bucket = metarepo.getCouchbaseOperations().getCouchbaseBucket();
	   
	     // we will change it to facade pattern.
	   
	   
	    	 billerlist= billerFactory.getList(billerPrefix);
	    		  items=billerlist.newList(items);
	    	  
	   
	      
		for (AccountDAO acc: items)
		{

       	 	accountId = getAccountDocumentName(acc.getAccount(), billerPrefix);
            dataBuffer=acc.getDataBuffer();
            accountinDoc=acc.getAccount();
            acc.setBranchId(Integer.parseInt(brdao.getBranch().toString()));
            acc.setRevisionId(Integer.parseInt(brdao.getRevision().toString()));
           			
            try {
            
            JsonObject content = JsonObject.create().put("branchId", acc.getBranchId())
                    .put("revisionId", acc.getRevisionId())
                    .put("dataBuffer", dataBuffer)
                    .put("addedOn", DefaultFormat.format(acc.getInsertedOn()));
       	 
            bucket.mutateIn(accountId).arrayPrepend("history", content)
                    .upsert("lastUpdateOn", DefaultFormat.format(new Date())).upsert("account", accountinDoc).execute();

            
            }catch (DocumentDoesNotExistException e){

                try {
                	JsonDocument accountDocument = createNewAccountDocument(acc, billerPrefix);
                bucket.insert(accountDocument);}
                catch(DocumentAlreadyExistsException ex)
                {
                	JsonObject content = JsonObject.create().put("branchId", acc.getBranchId())
                            .put("revisionId", acc.getRevisionId())
                            .put("dataBuffer", dataBuffer)
                            .put("addedOn", DefaultFormat.format(acc.getInsertedOn()));
               	 
                    bucket.mutateIn(accountId).arrayPrepend("history", content)
                            .upsert("lastUpdateOn", DefaultFormat.format(new Date())).upsert("account", accountinDoc).execute();

                }
                
            }
            
		StatSingleton.getInstance().incrementWriteCount();
		}
		}catch(Exception e)
		{
			CustomFlowVaribalePojo customObject = FlowSingleton.getInstance().getMap(billerPrefix);
			customObject.setFailedDueException(true);
			customObject.getExceptionList().add(e);
			customObject.setException(e);
			FlowSingleton.getInstance().putMap(billerPrefix, customObject);
			throw e;
			
		}
		
	}
	
	
	private JsonDocument createNewAccountDocument(AccountDAO acc, String biller2) {
		 String dataBuffer=null;
	      String accountId=null;
	      String accountinDoc=null;
	      
	      accountId = getAccountDocumentName(acc.getAccount(), billerPrefix);
          dataBuffer=acc.getDataBuffer();
          accountinDoc=acc.getAccount();
          
          JsonArray history = JsonArray.create();
          JsonObject historyObj = JsonObject.create();
          historyObj.put("branchId", acc.getBranchId());
          historyObj.put("revisionId", acc.getRevisionId());
          historyObj.put("dataBuffer", dataBuffer);
          historyObj.put("addedOn", DefaultFormat.format(acc.getInsertedOn()));
          history.add(historyObj);

          JsonObject props = JsonObject.create();
          props.put("secuenceNumber", 1);
          props.put("isPaid", false);

          JsonObject content = JsonObject.create();
          content.put("account", accountinDoc);
          content.put("type", "account");
          content.put("lastUpdateOn", DefaultFormat.format(acc.getInsertedOn()));
          content.put("isDirty", false);
          content.put("props", props);
          content.put("history", history);
          return JsonDocument.create(accountId, content);
		
	}


	public static String getAccountDocumentName(String account, String biller) {
		return String.format("%s" + "-" + "%s", biller, account);
	}

}
