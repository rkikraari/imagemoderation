resource "aws_cloudwatch_log_group" "image-moderation-lambda-log-group" {
  name              = "/aws/lambda/${var.detect-labels-lamdba-name}"
  retention_in_days = 14
}