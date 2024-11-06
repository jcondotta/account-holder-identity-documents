provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
}

data "aws_caller_identity" "current" {}

module "s3" {
  source = "./modules/s3"

  aws_region  = var.aws_region
  environment = var.environment
  tags        = merge(var.tags, { "environment" = var.environment })

  account_holder_identity_document_bucket_name = "jcondotta-${var.environment}-account-holder-identity-document"
}

module "lambda" {
  source = "./modules/lambda"

  aws_region             = var.aws_region
  current_aws_account_id = data.aws_caller_identity.current.account_id
  environment            = var.environment
  tags                   = merge(var.tags, { "environment" = var.environment })


  account_holder_identity_documents_bucket_name  = module.s3.account_holder_identity_documents_bucket_name
  account_holder_identity_documents_bucket_arn  = module.s3.account_holder_identity_documents_bucket_arn

  lambda_function_name         = "account-holder-identity-documents-lambda-${var.environment}"
  lambda_memory_size           = 1024
  lambda_timeout               = 15
  lambda_runtime               = "java17"
  lambda_handler               = "io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction"
  lambda_environment_variables = {}
}

module "apigateway" {
  source = "./modules/apigateway"

  aws_region  = var.aws_region
  environment = var.environment
  tags        = merge(var.tags, { "environment" = var.environment })

  lambda_function_arn  = module.lambda.lambda_function_arn
  lambda_function_name = module.lambda.lambda_function_name
  lambda_invoke_uri    = module.lambda.lambda_invoke_uri
}