/*
* Western Union, moving money for better.
* Copyright (C) 2019 Western Union Holdings, Inc. All Rights Reserved
*/
package com.wu.qcc.repository;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.wu.qcc.model.BillerMetaData;



@Repository
@N1qlPrimaryIndexed
public interface MetadataRepository extends CouchbaseRepository<BillerMetaData, String> {
}
