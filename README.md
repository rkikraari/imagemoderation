<!-- ABOUT THE PROJECT -->
## About The Project

The project is about moderating images using AWS Rekognition.



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
4. Go to terraform directory
   ```
   cd terraform
   ```
4. Execute the terraform init command
   ```
   terraform init
   ```
5. Execute the terraform plan command
   ```
   terraform plan
   ```
6. If happy with the plan, execute the terraform apply command
   ```
   terraform apply
   ```


<!-- USAGE EXAMPLES -->
## Usage

Once the deployment is successul, go to the AWS account. Go to the S3 bucket created as part of the depoyment and upload an image you want to moderate. You can use the sample images added in the project for testing purposes. After sucessfully uploading the image check the AWS Lambda logs in CloudWatch to see the results. You can also look at DynamoDB to see the result in it.


<!-- ROADMAP -->
## Roadmap

- A web frontend to upload the images
- CI/CD pipeline to deploy instead of using local set up
- Use terraform to manage CloudWatch and DynamoDB tables


[Java-url]: https://nextjs.org/
[Terraform-url]: https://www.terraform.io/
[Commitizen-url]: https://pypi.org/project/commitizen/
[AWS-url]: https://aws.amazon.com/
[Maven-url]: https://maven.apache.org/
[commitizen-blog-url]: https://medium.com/@iyerajiv/versioning-and-changelog-generation-using-commitizen-fc01a165f849