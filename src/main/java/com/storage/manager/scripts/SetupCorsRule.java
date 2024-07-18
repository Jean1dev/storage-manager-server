package com.storage.manager.scripts;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.GetBucketCrossOriginConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketCrossOriginConfigurationRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SetupCorsRule {

    public static void main(String[] args) {
        String accessKey= "";
        String secretKey = "/DcVZFtRSHSTOjfdSBj";
        String region = "us-east-1";
        String bucket = "";

        BasicAWSCredentials awsCred = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                .build();

        listConfig(s3Client, bucket);

        // Criar a configuração CORS com as regras fornecidas
        CORSRule corsRule = new CORSRule()
                .withAllowedHeaders(List.of("*"))
                .withAllowedMethods(Arrays.asList(CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.GET))
                .withAllowedOrigins(List.of("*"))
                .withExposedHeaders(Collections.singletonList("ETag"));

        List<CORSRule> corsRules = Collections.singletonList(corsRule);

        BucketCrossOriginConfiguration crossOriginConfig = new BucketCrossOriginConfiguration(corsRules);

        // Definir a configuração CORS para o bucket desejado
        SetBucketCrossOriginConfigurationRequest request = new SetBucketCrossOriginConfigurationRequest(bucket, crossOriginConfig);
        s3Client.setBucketCrossOriginConfiguration(request);

        listConfig(s3Client, bucket);
    }

    public static void listConfig(AmazonS3 s3Client, String bucket) {
        // Chamar o método para obter as configurações de CORS de um bucket
        BucketCrossOriginConfiguration corsConfig = getBucketCorsConfiguration(s3Client, bucket);

        // Exibir as regras de CORS retornadas
        List<CORSRule> RetrievedCorsRules = corsConfig.getRules();
        for (CORSRule rule : RetrievedCorsRules) {
            System.out.println("AllowedOrigins: " + rule.getAllowedOrigins());
            System.out.println("AllowedMethods: " + rule.getAllowedMethods());
            System.out.println("AllowedHeaders: " + rule.getAllowedHeaders());
            System.out.println("ExposeHeaders: " + rule.getExposedHeaders());
        }
    }
    public static BucketCrossOriginConfiguration getBucketCorsConfiguration(AmazonS3 s3Client, String bucketName) {
        GetBucketCrossOriginConfigurationRequest request = new GetBucketCrossOriginConfigurationRequest(bucketName);
        return s3Client.getBucketCrossOriginConfiguration(request);
    }

}
