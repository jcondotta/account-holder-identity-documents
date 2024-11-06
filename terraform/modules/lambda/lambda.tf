resource "aws_lambda_function" "account_holder_identity_documents_lambda" {
  function_name = var.lambda_function_name
  runtime       = var.lambda_runtime
  handler       = var.lambda_handler
  role          = aws_iam_role.account_holder_identity_documents_lambda_role_exec.arn
  filename      = var.lambda_file
  memory_size   = var.lambda_memory_size
  timeout       = var.lambda_timeout
  architectures = ["arm64"]

  environment {
    variables = merge(
      {
        AWS_S3_ACCOUNT_HOLDER_IDENTITY_DOCUMENTS_BUCKET_NAME = var.account_holder_identity_documents_bucket_name
      },
      var.lambda_environment_variables
    )
  }

  tags = var.tags
}