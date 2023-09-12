variable "detect-moderation-labels-lamdba-name" {
  description = "The lambda name using the DetectModerationLabels API of Rekognition"
  type        = string
}

variable "detect-labels-lamdba-handler-name" {
  description = "The lambda handler"
  type        = string
}

variable "dynamodb-table-name" {
  type        = string
  description = "The DynamoDB table name to store image moderation results"
}