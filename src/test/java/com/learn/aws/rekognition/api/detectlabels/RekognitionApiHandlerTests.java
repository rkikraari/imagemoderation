package com.learn.aws.rekognition.api.detectlabels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Label;

public class RekognitionApiHandlerTests {

    private RekognitionClient client = Mockito.mock(RekognitionClient.class);

    @ParameterizedTest
    @Event(type = S3Event.class, value = "s3event.json")
    void testGetLabelsfromImage(S3Event s3Event) {

        when(client.detectLabels(any(DetectLabelsRequest.class))).thenReturn(getDetectLabelsResponse());

        RekognitionApiHandler rekognitionApiHandler = new RekognitionApiHandler(client, s3Event.getRecords().get(0));
        List<Label> labels = rekognitionApiHandler.getLabelsFromImageUsingDetectLabelsApi();
        assertFalse(labels.isEmpty());
        assertEquals("[Label(Name=Adult, Confidence=95.1)]", labels.toString());
    }


    private DetectLabelsResponse getDetectLabelsResponse() {
        DetectLabelsResponse detectLabelsResponse = DetectLabelsResponse.builder()
                .labels(Label.builder().confidence(95.10f).name("Adult").build()).build();
        return detectLabelsResponse;
    }
}
