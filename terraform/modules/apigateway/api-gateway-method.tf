resource "aws_api_gateway_method" "teste" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_rest_api.this.root_resource_id
  http_method   = "GET"
  authorization = "NONE"  # No authentication required for login
}

resource "aws_api_gateway_integration" "post_login_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_rest_api.this.root_resource_id
  http_method             = aws_api_gateway_method.teste.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri
}

## Define the POST method for /api/v1/account-holders/account-holder-id/{account-holder-id}/identity-document
resource "aws_api_gateway_method" "post_identity_document" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_resource.identity_document.id
  http_method   = "POST"
  authorization = "NONE"
  request_parameters = {
    "method.request.header.Content-Type" = true
  }
}

resource "aws_api_gateway_integration" "post_identity_document_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_resource.identity_document.id
  http_method             = aws_api_gateway_method.post_identity_document.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri
  request_parameters = {
    "integration.request.header.Content-Type" = "method.request.header.Content-Type"
  }
}