variable "project" {
}

variable "region" {
}

provider "google" {
  project = var.project
  zone    = var.region
}

variable "sql_region" {
  type    = string
  default = "europe-west1"
}

variable "user_name" {
  type    = string
  default = "psql_admin"
}

variable "local_ip" {
  type    = string
}
