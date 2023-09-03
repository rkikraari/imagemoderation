package com.learn.aws.rekognition;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsFeatureName;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

public class ImageModeration implements RequestHandler<S3Event, String> {

    private static final Logger logger = LoggerFactory.getLogger(ImageModeration.class);

    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        try (RekognitionClient client = RekognitionClient.builder()
                .region(Region.of(System.getenv("AWS_REGION"))).build()) {

            S3EventNotificationRecord s3Record = s3Event.getRecords().get(0);
            String bucketName = s3Record.getS3().getBucket().getName();
            String imageName = s3Record.getS3().getObject().getUrlDecodedKey();

            return getLabelsfromImage(client, bucketName, imageName);
        } catch (Exception e) {
            logger.error("Error while processing using rekognition", e);
        }
        return "Empty String";
    }

    public static String getLabelsfromImage(RekognitionClient rekClient, String bucket,
            String image) {

        try {

            List<DetectLabelsFeatureName> rekognitionFeatures = new ArrayList<>();
            rekognitionFeatures.add(DetectLabelsFeatureName.GENERAL_LABELS);

            List<String> labelInclusionFilters = new ArrayList<>();
            labelInclusionFilters.add("Adult");

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(myImage -> myImage
                            .s3Object(s3Object -> s3Object.bucket(bucket).name(image)))
                    .features(rekognitionFeatures)
                    .settings(labelSettings -> labelSettings
                            .generalLabels(generalLabelsSettings -> generalLabelsSettings
                                    .labelInclusionFilters(labelInclusionFilters)))
                    .maxLabels(10).build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            List<Label> labels = labelsResponse.labels();
            logger.info("Detected labels for the given photo");
            for (Label label : labels) {
                logger.info("Label name is {} : Label confidence score is {}", label.name(),
                        label.confidence());
            }
            return labels.toString();

        } catch (RekognitionException e) {
            logger.error("Error while processing using detect lables api", e);
        }
        return "No Label found";
    }
}
