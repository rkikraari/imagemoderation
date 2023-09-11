package com.learn.aws.rekognition.api.detectmoderationlabels;

import java.util.List;
import java.util.UUID;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;

public class DynamoDBPersistenceHandler {

    private DynamoDbClient client;
    private DynamoDbEnhancedClient enhancedClient;
    private List<ModerationLabel> labels;
    private S3EventNotificationRecord s3Record;

    public DynamoDBPersistenceHandler(List<ModerationLabel> labels,
            S3EventNotificationRecord s3Record) {

        client = DynamoDbClient.builder().region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();
        enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
        this.labels = labels;
        this.s3Record = s3Record;
    }

    public void persistImageModerationResults() {

        if (!this.labels.isEmpty()) {
            DynamoDbTable<ImageModerationResult> imageModerationResultsTable = enhancedClient.table(
                    "ImageModerationResults", TableSchema.fromBean(ImageModerationResult.class));

            String uuid = UUID.randomUUID().toString();

            ImageModerationResult imageModerationResult = new ImageModerationResult();
            imageModerationResult.setIsHumanVerificationRequired(true);
            imageModerationResult.setId(uuid);
            imageModerationResult
                    .setS3ImageName(this.s3Record.getS3().getObject().getUrlDecodedKey());
            imageModerationResultsTable.putItem(imageModerationResult);
        }
    }
}
