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


### Installation

1. Clone the repo
2. Go to the cloned project directory
3. Go to terraform directory
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

Once the deployment is successul, go to the AWS account. Go to the S3 bucket created as apart of the depoyment and upload an image you want to moderate. After the sucessfully uploading the image check the AWS Lambda logs in CloudWatch to see the results.



<!-- ROADMAP -->
## Roadmap

- Add the results to Dynamodb
- A web frontend to upload the images
- CI/CD pipeline to deploy instead of using local set up


[Java-url]: https://nextjs.org/
[Terraform-url]: https://www.terraform.io/
[Commitizen-url]: https://pypi.org/project/commitizen/
[AWS-url]: https://aws.amazon.com/
[Maven-url]: https://maven.apache.org/