terraform {
  required_version = ">= 1.10.3"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.74.0"
    }
  }

  cloud {
    organization = "jcondotta"

    workspaces {
      name = "account-holder-identity-documents"
    }
  }
}

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