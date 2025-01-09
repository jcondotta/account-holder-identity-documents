provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      "project"     = "account_holder_identity_documents",
      "environment" = var.environment,
      "owner"       = var.aws_profile
      "managed-by"  = "terraform"
    }
  }
}