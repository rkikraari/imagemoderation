resource "aws_s3_bucket" "image_moderation_for_detect_moderation_labels_api" {
  bucket = "rki-image-moderation-for-detect-moderation-lables-api"

  tags = {
    Name        = "My Image Moderation bucket for detect moderartion labels API of AWS Rekognition"
    Environment = "POC"
  }
}

resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = aws_s3_bucket.image_moderation_for_detect_moderation_labels_api.id

  lambda_function {
    lambda_function_arn = aws_lambda_function.image_moderation_lambda_for_detect_moderation_labels_api.arn
    events              = ["s3:ObjectCreated:*"]
  }

  depends_on = [aws_lambda_permission.allow_bucket]
}