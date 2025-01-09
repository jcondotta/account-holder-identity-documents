data "aws_caller_identity" "current" {
}

module "s3" {
  source = "./modules/s3"

  bucket_name = "jcondotta-account-holder-identity-documents-${var.environment}"
}

module "lambda" {
  source = "./modules/lambda"

  aws_region             = var.aws_region
  environment            = var.environment
  current_aws_account_id = data.aws_caller_identity.current.account_id
  tags                   = var.tags

  function_name         = "account-holder-identity-documents-lambda-${var.environment}"
  memory_size           = 1024
  timeout               = 15
  runtime               = "java17"
  handler               = "io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction"
  filename              = "../target/account-holder-identity-documents-0.1.jar"
  environment_variables = {}

  account_holder_identity_documents_bucket_name = module.s3.bucket_name
  account_holder_identity_documents_bucket_arn  = module.s3.bucket_arn
}
#
#module "apigateway" {
#  source = "./modules/apigateway"
#
#  aws_region  = var.aws_region
#  environment = var.environment
#  tags        = merge(var.tags, { "environment" = var.environment })
#
#  lambda_function_arn  = module.lambda.lambda_function_arn
#  lambda_function_name = module.lambda.lambda_function_name
#  lambda_invoke_uri    = module.lambda.lambda_invoke_uri
#}