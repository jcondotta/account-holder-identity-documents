provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
}

module "s3" {
  source = "./modules/s3"

  environment = var.environment

  account_holder_identity_document_bucket_name = "jcondotta-${var.environment}-account-holder-identity-document"

  tags = merge(var.tags, { "environment" = var.environment })
}