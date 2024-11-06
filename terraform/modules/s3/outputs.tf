output "account_holder_identity_documents_bucket_name" {
  description = "The name of the S3 bucket for account holder identity documents"
  value       = aws_s3_bucket.this.bucket
}

output "account_holder_identity_documents_bucket_arn" {
  description = "The ARN of the S3 bucket for account holder identity documents"
  value       = aws_s3_bucket.this.arn
}