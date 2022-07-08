provider "google" {
}

resource "google_bigquery_dataset" "dataset" {
  project    = var.project
  dataset_id = "jarvis_sample_01"
  location   = "EU"


}

resource "google_bigquery_table" "trivago" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "trivago"

  schema = <<EOF
[
  {
    "name": "id",
    "type": "INTEGER",
    "mode": "REQUIRED",
    "description": "The id "
  },
  {
    "name": "num",
    "type": "INTEGER",
    "mode": "NULLABLE",
    "description": "The number to test the function "
  },
  {
    "name": "result",
    "type": "INTEGER",
    "mode": "NULLABLE",
    "description": "The result number "
  }
]
EOF

}