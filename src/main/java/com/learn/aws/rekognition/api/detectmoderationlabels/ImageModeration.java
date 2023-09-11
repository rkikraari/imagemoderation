package com.learn.aws.rekognition.api.detectmoderationlabels;

import java.util.List;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;

@Slf4j
public class ImageModeration implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        try (RekognitionClient client = RekognitionClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build()) {

            S3EventNotificationRecord s3Record = s3Event.getRecords().get(0);
            RekognitionApiHandler rekognitionApiHandler =
                    new RekognitionApiHandler(client, s3Record);
            List<ModerationLabel> labels = rekognitionApiHandler.getLabelsFromImageUsingDetectModerationLabelsApi();

            DynamoDBPersistenceHandler dbPersistenceHandler = new DynamoDBPersistenceHandler(labels, s3Record);
            dbPersistenceHandler.persistImageModerationResults();

        } catch (Exception e) {
            log.error("Error while moderating the image using rekognition", e);
        }
        return "Success";
    }
}
