variable "environment" {
  description = "The environment to deploy to (e.g. dev, localstack, staging, prod)"
  type        = string
}

variable "account_holder_identity_document_bucket_name" {
  description = "The name of the S3 bucket account holder identity document."
  type        = string
}

variable "tags" {
  description = "Tags applied to all resources for organization and cost tracking across environments and projects."
  type        = map(string)
}
