package com.learn.aws.rekognition;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsFeatureName;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

@Slf4j
public class RekognitionApiHandler {

    private RekognitionClient rekClient;
    private String bucket;
    private String image;

    public RekognitionApiHandler(RekognitionClient rekClient, S3EventNotificationRecord s3Record) {
        this.rekClient = rekClient;
        this.bucket = s3Record.getS3().getBucket().getName();
        this.image = s3Record.getS3().getObject().getUrlDecodedKey();
    }


    public List<Label> getLabelsFromImageUsingDetectLabelsApi() {

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
            log.info("Detected labels for the given photo {}", labels);

            return labels;

        } catch (RekognitionException e) {
            log.error("Error while processing using detect lables api", e);
            throw e;
        }
    }

    public List<ModerationLabel> getLabelsFromImageUsingDetectModerationLabelsApi() {

        try {

            DetectModerationLabelsRequest detectModerationLabelsRequest =
                    DetectModerationLabelsRequest.builder()
                            .image(myImage -> myImage
                                    .s3Object(s3Object -> s3Object.bucket(bucket).name(image)))
                            .build();


            DetectModerationLabelsResponse labelsResponse =
                    rekClient.detectModerationLabels(detectModerationLabelsRequest);
            List<ModerationLabel> labels = labelsResponse.moderationLabels();
            log.info("Detected moderation labels for the given photo {}", labels);

            return labels;

        } catch (RekognitionException e) {
            log.error("Error while processing using detect moderation lables api", e);
            throw e;
        }
    }

}
