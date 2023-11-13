package com.ceph.objectGateway.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.ceph.commonObject.common;
import com.ceph.objectGateway.service.BucketService;
import com.ceph.objectGateway.util.AwsClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BucketServiceImpl implements BucketService {

    /**
     * 获取BucketList
     */
    public common<?> getBucketList(){
        AmazonS3 s3 = AwsClientUtil.s3Client();
        if(s3 !=null){
            List<Bucket> bucketList =  s3.listBuckets();
            return common.createSuccess(bucketList);
        }
        return common.createError("ceph连接异常");
    }
    /**
     * 创建 bucket
     */
    public common<?> createBucket(String bucketName){
        if(StringUtils.isNotBlank(bucketName)){
            AmazonS3 s3 = AwsClientUtil.s3Client();
            if(s3 != null){
                List<Bucket> bucketList =  s3.listBuckets();
                for (Bucket bucket : bucketList) {
                    if(bucket.getName().equals(bucketName)){
                        return common.createError(bucketName+"已存在");
                    }
                }
                Bucket bucket;
                try {
                    bucket = s3.createBucket(bucketName);
                } catch (SdkClientException e){
                    return common.createError("bucket创建异常",e);
                }
                return common.createSuccess(bucket);
            }
            return common.createError("ceph连接异常");
        }
        return common.createError("参数异常");
    }
}
