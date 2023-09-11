resource "aws_iam_role" "iam_for_lambda" {
  name = "iam_for_lambda"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    environment = "poc"
  }
}

resource "aws_iam_policy" "rekognition_policy" {
  name        = "rekognition-policy"
  description = "Policy for accessing Rekognition and CloudWatch"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "rekognition:DetectLabels",
        ],
        Effect   = "Allow",
        Resource = "*"
      },
      {
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ],
        Effect   = "Allow",
        Resource = "*"
      },
      {
        Sid      = "ListObjectsInBucket",
        Effect   = "Allow",
        Action   = ["s3:ListBucket"],
        Resource = ["arn:aws:s3:::${aws_s3_bucket.image_moderation_for_detect_labels_api.bucket}"]
      },
      {
        Sid      = "AllObjectActions",
        Effect   = "Allow",
        Action   = "s3:*Object",
        Resource = ["arn:aws:s3:::${aws_s3_bucket.image_moderation_for_detect_labels_api.bucket}/*"]
      },
      {
            Sid = "ReadWriteTable",
            Effect = "Allow",
            Action = [
                "dynamodb:BatchGetItem",
                "dynamodb:GetItem",
                "dynamodb:Query",
                "dynamodb:Scan",
                "dynamodb:BatchWriteItem",
                "dynamodb:PutItem",
                "dynamodb:UpdateItem"
            ],
            Resource = "arn:aws:dynamodb:*:*:table/ImageModerationResults"
        }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "rekognition_policy_attachment" {
  policy_arn = aws_iam_policy.rekognition_policy.arn
  role       = aws_iam_role.iam_for_lambda.name
}