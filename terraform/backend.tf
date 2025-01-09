terraform {
  cloud {
    organization = "jcondotta"

    workspaces {
      name = "prod"
    }
  }
}