package com.wu.qcc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.cluster.ClusterInfo;

@Configuration
@EnableCouchbaseRepositories(basePackages = { "com.wu.qcc.repository" })
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	@Value("#{'${spring.couchbase.bootstrap-hosts}'.split(',')}")
	private List<String> couchbaseBootstrapHosts;

	@Value("${spring.couchbase.bucket.name}")
	private String bucketName = "MAMLoaderSB";

	@Value("${spring.couchbase.bucket.password}")
	private String bucketPassword;

	@Value("${spring.couchbase.password}")
	private String couchbasePassword;

	@Value("${spring.couchbase.username}")
	private String couchbaseUsername;
	
	

	@Override
	protected List<String> getBootstrapHosts() {
		return new ArrayList<>(couchbaseBootstrapHosts);
	}

	@Override
	protected String getBucketName() {
		return bucketName;
	}

	@Override
	protected String getBucketPassword() {
		return bucketPassword;
	}
	
	@Override

	@Bean(name = BeanNames.COUCHBASE_CLUSTER_INFO)
	public ClusterInfo couchbaseClusterInfo() throws Exception {
		return couchbaseCluster().authenticate(couchbaseUsername, couchbasePassword).clusterManager().info();
	}

	@Override

	@Bean(destroyMethod = "close", name = BeanNames.COUCHBASE_BUCKET)
	public Bucket couchbaseClient() throws Exception {
		return couchbaseCluster().openBucket(getBucketName());
	}
	
	
	
	}
