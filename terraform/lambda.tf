resource "aws_lambda_function" "image_moderation_lambda" {
  # If the file is not in the current working directory you will need to include a
  # path.module in the filename.
  filename      = "${path.module}/../target/ImageModeration-1.0-SNAPSHOT-jar-with-dependencies.jar"
  function_name = "image_moderation_lambda"
  role          = aws_iam_role.iam_for_lambda.arn
  handler       = "com.learn.aws.rekognition.ImageModeration"

  runtime = "java17"

  timeout     = 20
  memory_size = 1024

  tags = {
    environment : "poc"
    onemore : "topush"
  }

}

resource "aws_lambda_permission" "allow_bucket" {
  statement_id  = "AllowExecutionFromS3Bucket"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.image_moderation_lambda.arn
  principal     = "s3.amazonaws.com"
  source_arn    = aws_s3_bucket.imagemodbucketrki.arn
}