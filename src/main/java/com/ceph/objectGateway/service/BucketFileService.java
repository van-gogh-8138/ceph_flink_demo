package com.ceph.objectGateway.service;

import com.ceph.commonObject.common;
import org.springframework.web.multipart.MultipartFile;

public interface BucketFileService {
    /**
     * 获取bucket内的所有对象
     */
    common<?> bucketFileList(String bucketName);

    /**
     * 文件上传
     */
    common<?> fileUpload(MultipartFile multipartFile, String bucketName);

    /**
     * 文件下载
     */
    common<?> fileDownLoad(String bucketName, String key);
}
