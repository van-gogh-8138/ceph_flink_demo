package com.ceph.objectGateway.service;

import com.ceph.commonObject.common;

public interface BucketService {
    /**
     * 获取BucketList
     * @return
     */
    common<?> getBucketList();

    /**
     * 创建 bucket
     * @return
     */
    common<?> createBucket(String bucketName);
}
