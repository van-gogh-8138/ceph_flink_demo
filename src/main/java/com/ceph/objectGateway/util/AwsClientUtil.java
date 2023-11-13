package com.ceph.objectGateway.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ceph.objectGateway.configure.S3Configure;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Aws工具类
 */

@Component
public class AwsClientUtil {
    @Resource
    private S3Configure s3Configure;

    // 维护一个本类的静态变量
    public static AwsClientUtil awsClientUtil;

    @PostConstruct
    public void init() {
        awsClientUtil = this;
        awsClientUtil.s3Configure = this.s3Configure;
    }
    public static AmazonS3 s3Client() {
        try {
            AWSCredentials credentials =
                    new BasicAWSCredentials(awsClientUtil.s3Configure.getAccessKey(),
                    awsClientUtil.s3Configure.getSecretKey()); // 根据配置文件与华为云ceph建立连接

            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setProtocol(Protocol.HTTP);

            clientConfiguration.setRequestTimeout(10000);
            clientConfiguration.setConnectionTimeout(10000);
            System.out.println(awsClientUtil.s3Configure.getHosts());
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(awsClientUtil.s3Configure.getHosts(), Regions.US_EAST_1.name());//Regions.US_EAST_1.name()
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(endpointConfiguration)
                    .withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfiguration)
                    .build();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
