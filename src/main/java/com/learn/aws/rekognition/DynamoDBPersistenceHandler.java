package com.learn.aws.rekognition;

import java.util.List;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import software.amazon.awssdk.services.rekognition.model.Label;

public class DynamoDBPersistenceHandler {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;
    private List<Label> labels;
    private S3EventNotificationRecord s3Record;

    public DynamoDBPersistenceHandler(List<Label> labels, S3EventNotificationRecord s3Record) {
        client = AmazonDynamoDBClientBuilder.standard().withRegion(System.getenv("AWS_REGION")).build();
        mapper = new DynamoDBMapper(client);
        this.labels = labels;
        this.s3Record = s3Record;
    }

    public void persistImageModerationResults() {

        this.labels.stream().filter(label -> label.name().equals("Adult")).forEach(label -> {
            ImageModerationResult imageModerationResult = new ImageModerationResult();
            imageModerationResult.setLabelName(label.name());
            imageModerationResult.setLabelConfidence(label.confidence());
            imageModerationResult
                    .setS3ImageName(this.s3Record.getS3().getObject().getUrlDecodedKey());
            mapper.save(imageModerationResult);
        });
    }

}
