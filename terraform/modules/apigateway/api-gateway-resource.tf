# Define the /api resource
resource "aws_api_gateway_resource" "api" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_rest_api.this.root_resource_id
  path_part   = "api"
}

# Define the /v1 resource under /api
resource "aws_api_gateway_resource" "v1" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "v1"
}

# Define the /account-holders resource under /v1
resource "aws_api_gateway_resource" "account_holders" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.v1.id
  path_part   = "account-holders"
}

# Define the /account-holder-id resource under /account-holders
resource "aws_api_gateway_resource" "account_holder_id" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.account_holders.id
  path_part   = "account-holder-id"
}

# Define the {account-holder-id} path parameter
resource "aws_api_gateway_resource" "account_holder_id_param" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.account_holder_id.id
  path_part   = "{account-holder-id}"
}

# Define the /identity-document resource under /account-holder-id/{account-holder-id}
resource "aws_api_gateway_resource" "identity_document" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.account_holder_id_param.id
  path_part   = "identity-document"
}