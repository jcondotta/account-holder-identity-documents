resource "aws_api_gateway_rest_api" "this" {
  name        = "account_holder_identity_documents-api-${var.environment}"
  description = "API Gateway for account holder identity documents service in ${var.environment} environment"

  binary_media_types = ["multipart/form-data"]

  tags = var.tags
}