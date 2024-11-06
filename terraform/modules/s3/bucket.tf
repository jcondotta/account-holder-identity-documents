resource "aws_s3_bucket" "this" {
  bucket = var.account_holder_identity_document_bucket_name

  tags = var.tags
}