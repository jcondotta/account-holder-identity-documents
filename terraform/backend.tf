terraform {
  cloud {
    organization = "jcondotta"

    workspaces {
      name = "account-holder-identity-documents"
    }
  }
}