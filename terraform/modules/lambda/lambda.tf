resource "aws_lambda_function" "account_holder_identity_documents_lambda" {
  function_name = var.function_name
  runtime       = var.runtime
  handler       = var.handler
  role          = aws_iam_role.account_holder_identity_documents_lambda_role_exec.arn
  filename      = var.filename
  memory_size   = var.memory_size
  timeout       = var.timeout
  architectures = ["arm64"]

  environment {
    variables = merge(
      {
        AWS_S3_ACCOUNT_HOLDER_IDENTITY_DOCUMENTS_BUCKET_NAME = var.account_holder_identity_documents_bucket_name
      },
      var.environment_variables
    )
  }

  tags = var.tags
}