package com.ceph.objectGateway.controller.bucket;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.ceph.commonObject.common;
import com.ceph.objectGateway.service.BucketFileService;
import com.ceph.objectGateway.service.BucketService;
import com.ceph.objectGateway.util.AwsClientUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 *  bucket 相关接口
 */
@RestController
@RequestMapping("/bucket")
public class BucketController {

    @Resource
    private BucketService bucketService;

    @Resource
    private BucketFileService bucketFileService;

    /**
     * 测试连接ceph 接口
     */
    @RequestMapping("/testCeph")
    public String test(){
        AmazonS3 s3 = AwsClientUtil.s3Client();
        StringBuilder sb = new StringBuilder("bucketList:");
        if(s3 !=null){
            List<Bucket> bucketList =  s3.listBuckets();
            for (Bucket bucket : bucketList) {
                sb.append(bucket.getName());
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * bucket的list查询接口
     */
    @RequestMapping(value = "/bucketList",method = {RequestMethod.GET,RequestMethod.POST})
    public common<?> bucketList(){
        return bucketService.getBucketList();
    }

    /**
     * 根据 bucketName 查询所有的文件（不分页，直接查询ceph服务）
     */
    @RequestMapping(value = "/bucketFileList",method = {RequestMethod.GET,RequestMethod.POST})
    public common<?> bucketFileList(@RequestParam(value = "bucketName")String bucketName){
        return bucketFileService.bucketFileList(bucketName);
    }

    /**
     * 创建 bucket
     */
    @RequestMapping(value = "/createBucket",method = {RequestMethod.GET,RequestMethod.POST})
    public common<?> createBucket(@RequestParam(value = "bucketName")String bucketName){
        return bucketService.createBucket(bucketName);
    }

    /**
     * 文件上传
     */
    @PostMapping(value = "/fileUpload")
    public common<?> fileUpload(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "bucketName")String bucketName){
        return bucketFileService.fileUpload(multipartFile,bucketName);
    }

    /**
     * 文件下载
     */
    @GetMapping(value = "/fileDownLoad")
    public common<?> fileDownLoad(@RequestParam("bucketName") String bucketName, @RequestParam(value = "key")String key){
        return bucketFileService.fileDownLoad(bucketName,key);
    }
}
