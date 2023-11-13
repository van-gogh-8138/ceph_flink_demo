package com.ceph.objectGateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ceph.commonObject.common;
import com.ceph.objectGateway.service.BucketFileService;
import com.ceph.objectGateway.util.AwsClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BucketFileServiceImpl implements BucketFileService {

    /**
     * 获取bucket内的所有对象
     */
    public common<?> bucketFileList(String bucketName){
        if(StringUtils.isNotBlank(bucketName)){
            AmazonS3 s3 = AwsClientUtil.s3Client();
            if(s3 !=null) {
                ObjectListing objects = s3.listObjects(bucketName);
                return common.createSuccess(objects);
            }
            return common.createError("ceph连接异常");
        }
        return common.createError("bucket不能为空");
    }

    /**
     * 文件上传
     */
    public common<?> fileUpload(MultipartFile multipartFile, String bucketName){
        if(!multipartFile.isEmpty() && StringUtils.isNotBlank(bucketName)){
            String name = multipartFile.getOriginalFilename();
//            System.out.println(name);
            long size = multipartFile.getSize();
            AmazonS3 s3 = AwsClientUtil.s3Client();
            if(s3 != null){
                String fileName = null;
                if (name != null) {
                    fileName = name.substring(0,name.lastIndexOf("."));
                }
                // 获取文件后缀
                String suffix = null;
                if (name != null) {
                    suffix = name.substring(name.lastIndexOf(".") + 1);
                }
                //key 使用uuid随机生成
                String key = fileName +" "+new Date().getTime()+"."+suffix;
                ObjectMetadata metadata = new ObjectMetadata();
                // 必须设置ContentLength
                metadata.setContentLength(size);
                try {
                    PutObjectRequest request = new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), metadata);
                    //公有读
                    s3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));

                    s3.shutdown();
                    GeneratePresignedUrlRequest guRequest = new GeneratePresignedUrlRequest(bucketName, key);
                    URL url = s3.generatePresignedUrl(guRequest);
                    String  imgUrl = url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+url.getPath();
                    String  path = url.getPath();
                    JSONObject result = new JSONObject();
                    result.put("fileName",key);
                    result.put("bucketName",bucketName);
                    result.put("size",size);
                    result.put("url",imgUrl);
                    result.put("path",path);
                    return common.createSuccess("上传成功",result);
                } catch (IOException e) {
                    return common.createError("文件:"+name+"上传异常");
                }
            }
            return common.createError("ceph连接异常");
        }
        return common.createError("参数异常");
    }

    /**
     * 文件下载
     */
    public common<?> fileDownLoad(String bucketName, String filename){
        if(StringUtils.isNotBlank(bucketName) && StringUtils.isNotBlank(filename)){
            AmazonS3 s3 = AwsClientUtil.s3Client();
            if(s3 != null){
                GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, filename);
                URL url = s3.generatePresignedUrl(request);
                Map<String,Object> result = new HashMap<>();
                result.put("url",url);
                return common.createSuccess(result);
            }
            return common.createError("ceph连接异常");
        }
        return common.createError();
    }

}
