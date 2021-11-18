provider "google" {
  credentials = file(var.credentials_file)
  project = var.project
  region      = "europe-west1"
  zone        = "europe-west1-c"
}

resource "google_bigquery_dataset" "dataset" {
  dataset_id                  = "example_dataset"
  friendly_name               = "test"
  description                 = "This is a test description"
  location                    = "EU"
  default_table_expiration_ms = 3600000

  labels = {
    env = "default"
  }
  
}


resource "google_bigquery_table" "car_input" {
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "car_input"

  time_partitioning {
    type = "DAY"
  }
  labels = {
    env = "default"
	}
    
	schema = <<EOF
[
  {
    "name": "ID",
    "type": "INTEGER",
    "mode": "REQUIRED",
    "description": "The license plate of the car "
  },
  {
    "name": "brand",
    "type": "STRING",
    "mode": "NULLABLE",
    "description": "The brand of the car "
  },
  {
    "name": "color",
    "type": "STRING",
    "mode": "NULLABLE",
    "description": "The color of the car "
  }
]
EOF

}
resource "google_bigquery_table" "car_output" {
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "car_output"

  time_partitioning {
    type = "DAY"
  }
  labels = {
    env = "default"
	}
    
	schema = <<EOF
[
  {
    "name": "ID",
    "type": "INTEGER",
    "mode": "REQUIRED",
    "description": "The license plate of the car "
  },
  {
    "name": "brand",
    "type": "STRING",
    "mode": "NULLABLE",
    "description": "The brand of the car "
  },
  {
    "name": "color",
    "type": "STRING",
    "mode": "NULLABLE",
    "description": "The color of the car "
  }
]
EOF

}

