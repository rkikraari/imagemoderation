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
import software.amazon.awssdk.services.rekognition.model.DetectLabelsSettings;
import software.amazon.awssdk.services.rekognition.model.GeneralLabelsSettings;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.S3Object;

public class ImageModeration implements RequestHandler<S3Event, String> {

    private static Logger logger = LoggerFactory.getLogger(ImageModeration.class);

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        
        RekognitionClient client = null;
        try {
            S3EventNotificationRecord record = s3Event.getRecords().get(0);
            String bucketName = record.getS3().getBucket().getName();
            String imageName = record.getS3().getObject().getUrlDecodedKey();

            client = RekognitionClient.builder().region(Region.of(System.getenv("AWS_REGION")))
                    .build();
            return getLabelsfromImage(client, bucketName, imageName);
        } catch (Exception e) {
            logger.error("Error while processing using rekognition", e);
        } finally {
            client.close();
        }
        return "Empty String";
    }

    public static String getLabelsfromImage(RekognitionClient rekClient, String bucket, String image) {

        try {
            S3Object s3Object = S3Object.builder()
                    .bucket(bucket)
                    .name(image)
                    .build();

            Image myImage = Image.builder()
                    .s3Object(s3Object)
                    .build();

            List<DetectLabelsFeatureName> reck_features = new ArrayList<>();
            reck_features.add(DetectLabelsFeatureName.GENERAL_LABELS);

            List<String> labelInclusionFilters = new ArrayList<>();
            labelInclusionFilters.add("Adult");

            GeneralLabelsSettings generalLabelsSettings = GeneralLabelsSettings.builder().labelInclusionFilters(labelInclusionFilters).build();

            DetectLabelsSettings labelSettings = DetectLabelsSettings.builder().generalLabels(generalLabelsSettings).build();

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(myImage)
                    .features(reck_features)
                    .settings(labelSettings)
                    .maxLabels(10)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            List<Label> labels = labelsResponse.labels();
            logger.info("Detected labels for the given photo");
            for (Label label : labels) {
                logger.info("Label name is {} : Label confidence score is {}", label.name(), label.confidence().toString());
            }
            return labels.toString();

        } catch (RekognitionException e) {
            logger.error("Error while processing using detect lables api", e);
        }
        return "No Label found";
    }
}