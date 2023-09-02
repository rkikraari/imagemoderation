resource "aws_s3_bucket" "imagemodbucketrki" {
  bucket = "rkiimagemodbucket"

  tags = {
    Name        = "My Image Moderation bucket"
    Environment = "POC"
  }
}

resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = aws_s3_bucket.imagemodbucketrki.id

  lambda_function {
    lambda_function_arn = aws_lambda_function.image_moderation_lambda.arn
    events              = ["s3:ObjectCreated:*"]
  }

  depends_on = [aws_lambda_permission.allow_bucket]
}