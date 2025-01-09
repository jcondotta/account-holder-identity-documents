variable "aws_region" {
  description = "The AWS region where resources will be deployed."
  type        = string
}

variable "environment" {
  description = "The environment the Lambda function is deployed to (e.g., dev, prod or staging)"
  type        = string
}

variable "current_aws_account_id" {
  description = "The current AWS account ID"
  type        = string
}

variable "function_name" {
  description = "The name of the Lambda function"
  type        = string

  validation {
    condition     = length(var.function_name) > 0
    error_message = "function_name must be a non-empty string."
  }
}

variable "memory_size" {
  description = "The memory size (in MB) for the Lambda function"
  type        = number

  validation {
    condition     = var.memory_size >= 128 && var.memory_size <= 10240
    error_message = "memory_size must be between 128 MB and 10,240 MB."
  }
}

variable "timeout" {
  description = "The timeout (in seconds) for the Lambda function"
  type        = number

  validation {
    condition     = var.timeout > 0 && var.timeout <= 900
    error_message = "timeout must be a positive number and less than or equal to 900 seconds (15 minutes)."
  }
}

variable "runtime" {
  description = "The runtime for the Lambda function (e.g., java17, java21)"
  type        = string
}

variable "handler" {
  description = "The fully qualified handler class for the Lambda function"
  type        = string
}

variable "account_holder_identity_documents_bucket_arn" {
  description = "The ARN of the S3 bucket for account holder identity documents"
  type        = string
}

variable "environment_variables" {
  description = "A key-value map of environment variables for the Lambda function, used to configure dynamic runtime settings."
  type        = map(string)
  default     = {}
}

variable "tags" {
  description = "Tags applied to all resources for organization and cost tracking across environments and projects."
  type        = map(string)
  default     = {}
}