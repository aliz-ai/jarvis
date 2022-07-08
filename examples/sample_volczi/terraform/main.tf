provider "google" {
}

resource "google_bigquery_dataset" "dataset" {
  project    = var.project
  dataset_id = "jarvis_sample_01"
  location   = "US"


}

resource "google_bigquery_table" "car" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "car"

  schema = <<EOF
[
  {
    "name": "id",
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
    "name": "price",
    "type": "INTEGER",
    "mode": "NULLABLE",
    "description": "The price of the car "
  },
  {
    "name": "nickname",
    "type": "BOOLEAN",
    "mode": "NULLABLE",
    "description": "The nickname of the car "
  }
]
EOF

}