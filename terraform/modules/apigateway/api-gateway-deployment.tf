# Deploy the API Gateway (ensure all methods are deployed)
resource "aws_api_gateway_deployment" "recipients_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  stage_name  = var.environment

  depends_on = [
    aws_api_gateway_integration.post_login_integration,
    aws_api_gateway_integration.post_identity_document_integration
  ]
}