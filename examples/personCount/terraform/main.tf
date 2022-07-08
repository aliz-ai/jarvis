provider "google" {
}

resource "google_bigquery_dataset" "dataset" {
  project    = var.project
  dataset_id = "jarvis_sample_01"
  location   = "EU"


}

resource "google_bigquery_table" "person" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "person"

  schema = <<EOF
[
  {
    "name": "id",
    "type": "INTEGER",
    "mode": "REQUIRED",
    "description": "The id "
  },
  {
    "name": "roomDetail",
    "type": "STRING",
    "mode": "NULLABLE",
    "description": "The room detail "
  },
     {
       "name": "result",
       "type": "INTEGER",
       "mode": "NULLABLE",
       "description": "The result "
     }
]
EOF

}