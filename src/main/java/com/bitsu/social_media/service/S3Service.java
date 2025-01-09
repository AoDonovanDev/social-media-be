package com.bitsu.social_media.service;

import java.net.URL;
import java.time.Duration;

import com.bitsu.social_media.utility.Utility;
import org.springframework.stereotype.Service;

import com.bitsu.social_media.dto.S3PresignedURLResponse;
import com.bitsu.social_media.model.User;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final Utility utility;
    private final String bucketName = "revature-project-2-bucket";
    private final String mediaBaseURL = "https://" + bucketName + ".s3.us-east-2.amazonaws.com/";

    public S3PresignedURLResponse generatePresignedUrl(String fileName, String contentType) {
        User user = utility.getLoggedInUser();
        String uniqueKey = user.getId() + "-" + System.currentTimeMillis() + "-" + fileName;
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(b -> b
                .putObjectRequest(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType(contentType)
                        .key(uniqueKey)
                        .build())
                .signatureDuration(Duration.ofMinutes(5)));
        URL preSignedUrl = presignedRequest.url();
        return new S3PresignedURLResponse(preSignedUrl.toString(), mediaBaseURL + uniqueKey);
    }

    public boolean deleteImageFromBucket(String mediaURL) {
        try {
            String imageKey = mediaURL.substring(mediaBaseURL.length());
            s3Client.deleteObject(b -> b.bucket(bucketName).key(imageKey));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
