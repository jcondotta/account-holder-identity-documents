resource "aws_api_gateway_deployment" "recipients_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.this.id

  depends_on = [
    aws_api_gateway_integration.post_login_integration,
    aws_api_gateway_integration.post_identity_document_integration
  ]
}

resource "aws_api_gateway_stage" "recipients_api_stage" {
  stage_name    = var.environment
  rest_api_id   = aws_api_gateway_rest_api.this.id
  deployment_id = aws_api_gateway_deployment.recipients_api_deployment.id

  variables = {
    "stage" = var.environment
  }
}