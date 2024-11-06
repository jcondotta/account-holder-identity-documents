# Define the method response for a successful request (200)
resource "aws_api_gateway_method_response" "post_identity_document_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = "200"

  response_models = {
    "application/json" = "Empty"  # You can specify a model if you have one
  }
}

# Define the method response for a client error (400)
resource "aws_api_gateway_method_response" "post_identity_document_400" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = "400"

  response_models = {
    "application/json" = "Error"  # You can specify an error model if you have one
  }
}

# Define the method response for a server error (500)
resource "aws_api_gateway_method_response" "post_identity_document_500" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = "500"

  response_models = {
    "application/json" = "Error"  # You can specify an error model if you have one
  }
}

# Define the integration response for a successful request (200)
resource "aws_api_gateway_integration_response" "post_identity_document_integration_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = aws_api_gateway_method_response.post_identity_document_200.status_code
  selection_pattern = ""  # Empty pattern for successful requests (200)

  response_templates = {
    "application/json" = ""
  }
}

# Define the integration response for a client error (400)
resource "aws_api_gateway_integration_response" "post_identity_document_integration_400" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = aws_api_gateway_method_response.post_identity_document_400.status_code
  selection_pattern = "4\\d{2}"  # Regex pattern to capture all 4xx errors

  response_templates = {
    "application/json" = "{\"message\": \"Client error\"}"
  }
}

# Define the integration response for a server error (500)
resource "aws_api_gateway_integration_response" "post_identity_document_integration_500" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.identity_document.id
  http_method = aws_api_gateway_method.post_identity_document.http_method
  status_code = aws_api_gateway_method_response.post_identity_document_500.status_code
  selection_pattern = "5\\d{2}"  # Regex pattern to capture all 5xx errors

  response_templates = {
    "application/json" = "{\"message\": \"Server error\"}"
  }
}
