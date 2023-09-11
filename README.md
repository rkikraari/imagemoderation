<!-- ABOUT THE PROJECT -->
## About The Project

The project is about moderating images using AWS Rekognition. It uses the DetectLabels and DetectModerationLabels API for perform image moderation.


### Built With

* [Java](Java-url)
* [Terraform](Terraform-url)
* [Commitizen](Commitizen-url)
* [AWS](AWS-url)
* [Maven](Maven-url)


<!-- GETTING STARTED -->
## Getting Started

To get started with the project on your own AWS account, clone the project from Github. 

### Prerequisites

The project relies on certain tools/softwares/libraries that are listed below. The entire deployment is done from the local machine to AWS. 

* Terraform
* Java
* Maven
* AWS (credentials configured using AWS cli)
* Commitizen (It is the Python library that was used for versioning, conventional commits and for generating changelog.)
   For details around the set up of commitizen and its usage check [here](commitizen-blog-url)
* Python (only for Commitizen and pre-commit)
* pre-commit


### Installation

The steps to install the application to AWS from local machine are listed below.

1. Clone the repo
2. Go to the cloned project directory. 
   ```
   cd ImageModeration
   ```
3. Execute the maven command. It creates a fat jar which is used to by terraform for the Lambda deployment
   ```
   mvn clean package assembly:single
   ```
4. The repo supports working with 2 AWS Rekognition APIs i.e. DetectLabels and DetectModerationLabels API.
5. The source code for the API is split based on the APIs. You will find two packages 
   - com.learn.aws.rekognition.api.detectlabels = Supports that DetectLabels API
   - com.learn.aws.rekognition.api.detectmoderationlabels = Supports that DetectModerationLabels API
6. Based on the API that you want to use for your image moderation you can use either the detectlabels or detectmoderationlabels folder in terraform
7. Go to terraform directory
   ```
   cd terraform
   ```
 8. Go to either detectlabels or detectmoderationlabels folder based on your requirement.
   ```
   cd detectlabels 

   OR

   cd detectmoderationlabels
   ```
9. Go to provider.tf file and modify the values of shared_config_files and shared_credentials_files. Ensure you have done aws configure locally.
10. Execute the terraform init command
   ```
   terraform init
   ```
11. Execute the terraform plan command
   ```
   terraform plan
   ```
12. If happy with the plan, execute the terraform apply command
   ```
   terraform apply
   ```


<!-- USAGE EXAMPLES -->
## Usage

Once the deployment is successful, go to the AWS account. Go to the S3 bucket created as part of the depoyment and upload the image you want to moderate. You can use the sample images added in the project for testing purposes. After sucessfully uploading the image, check the AWS Lambda logs in CloudWatch to see the results. You can also look at DynamoDB to see the results in it. The results would only be updated in DynamoDB if moderation is required for an image else no entries are made to the DynamoDB.


<!-- ROADMAP -->
## Roadmap

- A web frontend to upload the images
- CI/CD pipeline to deploy instead of using local set up
- Use terraform to manage CloudWatch and DynamoDB tables
- Persists the labels returned by the API in DynamoDB
- Addition of more Unit tests


[Java-url]: https://nextjs.org/
[Terraform-url]: https://www.terraform.io/
[Commitizen-url]: https://pypi.org/project/commitizen/
[AWS-url]: https://aws.amazon.com/
[Maven-url]: https://maven.apache.org/
[commitizen-blog-url]: https://medium.com/@iyerajiv/versioning-and-changelog-generation-using-commitizen-fc01a165f849