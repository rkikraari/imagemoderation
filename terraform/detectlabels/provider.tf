provider "aws" {
  region                   = "eu-west-1"
  shared_config_files      = ["/Users/rajiv.iyer/.aws/config"]
  shared_credentials_files = ["/Users/rajiv.iyer/.aws/credentials"]
  profile                  = "default"
}