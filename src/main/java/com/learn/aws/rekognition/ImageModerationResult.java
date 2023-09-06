package com.learn.aws.rekognition;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName = "ImageModerationResults")
@Getter
@Setter
public class ImageModerationResult {

    @DynamoDBAttribute
    String labelName;

    @DynamoDBAttribute
    Float labelConfidence;

    @DynamoDBHashKey
    String s3ImageName;

}