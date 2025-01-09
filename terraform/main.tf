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

  account_holder_identity_documents_bucket_arn = module.s3.bucket_arn

  environment_variables = {
    "AWS_S3_ACCOUNT_HOLDER_IDENTITY_DOCUMENTS_BUCKET_NAME" = module.s3.bucket_name
  }
}

module "apigateway" {
  source = "./modules/apigateway"

  aws_region  = var.aws_region
  environment = var.environment
  tags        = var.tags

  lambda_function_arn  = module.lambda.function_arn
  lambda_function_name = module.lambda.function_name
  lambda_invoke_uri    = module.lambda.invoke_uri
}