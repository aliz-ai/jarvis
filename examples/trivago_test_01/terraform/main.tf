provider "google" {
}

resource "google_bigquery_dataset" "dataset" {
  project    = var.project
  dataset_id = "jarvis_tests"
  location   = "EU"


}

resource "google_bigquery_table" "booking_net_cpa_revenue" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "booking_net_cpa_revenue"

  schema = <<EOF
[
  {
        "name": "session_date_id",
        "type": "STRING"
    },
    {
        "name": "request_id_page_log",
        "type": "STRING"
    },
    {
        "name": "clickout_ymd",
        "type": "INTEGER"
    },
    {
        "name": "clickout_ymdhms",
        "type": "STRING"
    },
    {
        "name": "locale",
        "type": "STRING"
    },
    {
        "name": "item_id",
        "type": "INTEGER"
    },
    {
        "name": "item2partner_id",
        "type": "INTEGER"
    },
    {
        "name": "clickout_timestamp",
        "type": "INTEGER"
    },
    {
        "name": "booking_id",
        "type": "STRING"
    },
    {
        "name": "partner_id",
        "type": "INTEGER"
    },
    {
        "name": "trv_reference",
        "type": "STRING"
    },
    {
        "name": "request_id_conversion_api",
        "type": "STRING"
    },
    {
        "name": "inbound_timestamp",
        "type": "INTEGER"
    },
    {
        "name": "data_source",
        "type": "RECORD",
        "mode": "REPEATED",
        "fields": [
            {
                "name": "array_element",
                "type": "STRING"
            }
            ]
        },
    {
        "name": "booking_date_ymd",
        "type": "INTEGER"
    },
    {
        "name": "booking_date_ymdhms",
        "type": "STRING"
    },
    {
        "name": "partner_reference",
        "type": "STRING"
    },
    {
        "name": "arrival_date_ymd",
        "type": "DATE"
    },
    {
        "name": "departure_date_ymd",
        "type": "DATE"
    },
    {
        "name": "room_count",
        "type": "INTEGER"
    },
    {
        "name": "booking_value_eurocents",
        "type": "INTEGER"
    },
    {
        "name": "partner_margin_eurocents",
        "type": "INTEGER"
    },
    {
        "name": "booking_value_source_currency",
        "type": "FLOAT64"
    },
    {
        "name": "source_currency_code",
        "type": "STRING"
    },
    {
        "name": "source_exchange_rate",
        "type": "FLOAT64"
    },
    {
        "name": "update_date_ymd",
        "type": "INTEGER"
    },
    {
        "name": "update_date_ymdhms",
        "type": "INTEGER"
    },
    {
        "name": "cancellation_date_ymd",
        "type": "INTEGER"
    },
    {
        "name": "cancellation_date_ymdhms",
        "type": "INTEGER"
    },
    {
        "name": "refund_value_eurocents",
        "type": "INTEGER"
    },
    {
        "name": "clickout",
        "type": "RECORD",
        "fields": [
            {
                "name": "session_date_id",
                "type": "STRING"
            },
            {
                "name": "request_id_page_log",
                "type": "STRING"
            },
            {
                "name": "partner_id",
                "type": "INTEGER"
            },
            {
                "name": "item_id",
                "type": "INTEGER"
            },
            {
                "name": "locale",
                "type": "STRING"
            },
            {
                "name": "source",
                "type": "INTEGER"
            },
            {
                "name": "ymd",
                "type": "INTEGER"
            },
            {
                "name": "ymdhms",
                "type": "STRING"
            },
            {
                "name": "pld_payload_set",
                "type": "RECORD",
                "fields": [
                    {
                        "name": "map",
                        "type": "RECORD",
                        "mode": "REPEATED",
                        "fields": [
                            {
                                "name": "key",
                                "type": "INTEGER"
                            },
                            {
                                "name": "value",
                                "type": "STRING"
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        "name": "cpa_campaign_id",
        "type": "INTEGER"
    },
    {
        "name": "cpa_input",
        "type": "NUMERIC"
    },
    {
        "name": "cpa_model",
        "type": "STRING"
    },
    {
        "name": "cpa_submodel",
        "type": "STRING"
    },
    {
        "name": "fallback_cpa_input",
        "type": "NUMERIC"
    },
    {
        "name": "revenue",
        "type": "NUMERIC"
    },
    {
        "name": "revenue_source",
        "type": "STRING"
    },
    {
        "name": "calculation_source",
        "type": "STRING"
    },
    {
        "name": "invoice_source",
        "type": "STRING"
    },
    {
        "name": "invoice_type",
        "type": "STRING"
    },
    {
        "name": "ymd",
        "type": "DATE"
    }
]
EOF

}

resource "google_bigquery_table" "session_stats_master" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "session_stats_master"

  schema = <<EOF
[
  {
         "name":"session_id",
         "type":"STRING"
  },
  {
        "name": "session_date_id",
        "type": "STRING"
  },
  {
         "name":"traffic_type_parent",
         "type":"INTEGER"
      },
      {
         "name":"traffic_type_child",
         "type":"INTEGER"
      },
    {
           "name":"agent_id",
           "type":"INTEGER"
        },
        {
               "name":"tags",
               "type":"RECORD",
               "mode":"REPEATED",
               "fields":[
                  {
                     "name":"array_element",
                     "type":"STRING"
                  }
               ]
        },
        {
               "name":"session_properties_tags",
               "type":"RECORD",
               "mode":"REPEATED",
               "fields":[
                  {
                     "name":"array_element",
                     "type":"STRING"
                  }
               ]
            },
        {
               "name":"search_type",
               "type":"INTEGER"
            },
        {
               "name":"is_app",
               "type":"INTEGER"
            },
        {
               "name":"crawler_id",
               "type":"INTEGER"
            },
        {
               "name":"is_core",
               "type":"BOOLEAN"
            },
        {
            "name":"co_log_entries",
            "type":"RECORD",
            "mode":"REPEATED",
            "fields":[
               {
                  "name":"array_element",
                  "type":"RECORD",
                  "fields":[
                     {
                        "name":"trv_reference",
                        "type":"STRING"
                     },
                     {
                        "name":"page_id",
                        "type":"INTEGER"
                     },
                     {
                        "name":"locale",
                        "type":"STRING"
                     },{
                        "name":"request_id",
                        "type":"STRING"
                     },{
                        "name":"is_standard_date",
                        "type":"INTEGER"
                     },{
                        "name":"partner_id",
                        "type":"INTEGER"
                     },{
                        "name":"item_id",
                        "type":"INTEGER"
                     },{
                        "name":"base_cpc",
                        "type":"INTEGER"
                     },{
                        "name":"modified_cpc",
                        "type":"INTEGER"
                     },{
                        "name":"sl_base_cpc",
                        "type":"INTEGER"
                     },{
                        "name":"sl_modified_cpc",
                        "type":"INTEGER"
                     },{
                        "name":"clickout_source",
                        "type":"INTEGER"
                     },{
                        "name":"arrival_days",
                        "type":"INTEGER"
                     },{
                        "name":"departure_days",
                        "type":"INTEGER"
                     },{
                        "name":"room_detail",
                        "type":"STRING"
                     },{
                        "name":"room_type",
                        "type":"INTEGER"
                     },{
                        "name":"property_group_id",
                        "type":"INTEGER"
                     },{
                        "name":"revenue",
                        "type":"INTEGER"
                     },{
                        "name":"date_id",
                        "type":"STRING"
                     },{
                        "name":"price",
                        "type":"FLOAT64"
                     },{
                        "name":"is_sponsored_click",
                        "type":"BOOLEAN"
                     },{
                        "name":"is_tracking_partner",
                        "type":"INTEGER"
                     },{
                        "name":"bidding_model",
                        "type":"STRING"
                     },{
                        "name":"cpa_campaign_id",
                        "type":"INTEGER"
                     },{
                        "name":"cpa_input",
                        "type":"FLOAT64"
                     },{
                        "name":"auction_vpc",
                        "type":"FLOAT64"
                     },{
                        "name":"timestamp",
                        "type":"INTEGER"
                     },{
                        "name":"pld_payload_set",
                        "type":"RECORD",
                        "fields":[
                           {
                              "name":"map",
                              "type":"RECORD",
                              "mode":"REPEATED",
                              "fields":[
                                 {
                                    "name":"key",
                                    "type":"INTEGER"
                                 },
                                 {
                                    "name":"value",
                                    "type":"STRING"
                                 }
                              ]
                           }
                        ]
                     },
                  ]
               }
            ]
        },
        {
            "name":"page_log_entries",
            "type":"RECORD",
            "mode":"REPEATED",
            "fields":[
               {
                  "name":"array_element",
                  "type":"RECORD",
                  "fields":[
                     {
                        "name":"step",
                        "type":"INTEGER"
                     },{
                        "name":"page_log_full_details",
                        "type":"RECORD",
                        "fields":[
                           {
                              "name":"map",
                              "type":"RECORD",
                              "mode":"REPEATED",
                              "fields":[
                                 {
                                    "name":"key",
                                    "type":"INTEGER"
                                 },
                                 {
                                    "name":"value",
                                    "type":"RECORD",
                                    "fields":[
                                       {
                                          "name":"key",
                                          "type":"INTEGER"
                                       },
                                       {
                                          "name":"data",
                                          "type":"STRING"
                                       },
                                       {
                                          "name":"int_value",
                                          "type":"INTEGER"
                                       },
                                       {
                                          "name":"string_values",
                                          "type":"RECORD",
                                          "mode":"REPEATED",
                                          "fields":[
                                             {
                                                "name":"array_element",
                                                "type":"STRING"
                                             }
                                          ]
                                       },
                                       {
                                          "name":"int_values",
                                          "type":"RECORD",
                                          "mode":"REPEATED",
                                          "fields":[
                                             {
                                                "name":"array_element",
                                                "type":"INTEGER"
                                             }
                                          ]
                                       },
                                       {
                                          "name":"value_type",
                                          "type":"STRING"
                                       }
                                    ]
                                 }
                              ]
                           }
                        ]
                     }
                  ]
                }
             ]
          },
         {
            "name":"ymd",
            "type":"DATE"
         }
]
EOF

}

resource "google_bigquery_table" "consolidated_booking" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "consolidated_booking"

  schema = <<EOF
[
  {
         "name":"booking_id",
         "type":"STRING"
      },
      {
         "name":"partner_id",
         "type":"INTEGER"
      },
      {
         "name":"bucket_id",
         "type":"INTEGER"
      },
      {
         "name":"trv_reference",
         "type":"STRING"
      },
      {
         "name":"request_id_conversion_api",
         "type":"INTEGER"
      },
      {
         "name":"inbound_timestamp",
         "type":"INTEGER"
      },
      {
         "name":"data_source",
         "type":"RECORD",
         "mode":"REPEATED",
         "fields":[
            {
               "name":"array_element",
               "type":"STRING"
            }
         ]
      },
      {
         "name":"booking_date_ymd",
         "type":"INTEGER"
      },
      {
         "name":"booking_date_ymdhms",
         "type":"INTEGER"
      },
      {
         "name":"partner_reference",
         "type":"STRING"
      },
      {
         "name":"arrival_date_ymd",
         "type":"DATE"
      },
      {
         "name":"departure_date_ymd",
         "type":"DATE"
      },
      {
         "name":"room_count",
         "type":"INTEGER"
      },
      {
         "name":"booking_value_eurocents",
         "type":"INTEGER"
      },
      {
         "name":"partner_margin_eurocents",
         "type":"INTEGER"
      },
      {
         "name":"locale",
         "type":"STRING"
      },
      {
         "name":"booking_value_source_currency",
         "type":"STRING"
      },
      {
         "name":"source_currency_code",
         "type":"STRING"
      },
      {
         "name":"source_exchange_rate",
         "type":"FLOAT"
      },
      {
         "name":"client_type",
         "type":"STRING"
      },
      {
         "name":"page_id",
         "type":"INTEGER"
      },
      {
         "name":"pld_type",
         "type":"INTEGER"
      },
      {
         "name":"pld_data",
         "type":"STRING"
      },
      {
         "name":"bidding_model",
         "type":"STRING"
      },
      {
         "name":"consolidation_source",
         "type":"STRING"
      },
      {
         "name":"priority",
         "type":"INTEGER"
      },
      {
         "name":"domain",
         "type":"STRING"
      },
      {
         "name":"clickout",
         "type":"RECORD",
         "fields":[
            {
               "name":"session_date_id",
               "type":"INTEGER"
            },
            {
               "name":"request_id_page_log",
               "type":"STRING"
            },
            {
               "name":"partner_id",
               "type":"INTEGER"
            },
            {
               "name":"item_id",
               "type":"INTEGER"
            },
            {
               "name":"locale",
               "type":"STRING"
            },
            {
               "name":"source",
               "type":"INTEGER"
            },
            {
               "name":"ymd",
               "type":"INTEGER"
            },
            {
               "name":"ymdhms",
               "type":"INTEGER"
            },
            {
               "name":"pld_payload_set",
               "type":"RECORD",
               "fields":[
                  {
                     "name":"map",
                     "type":"RECORD",
                     "mode":"REPEATED",
                     "fields":[
                        {
                           "name":"key",
                           "type":"INTEGER"
                        },
                        {
                           "name":"value",
                           "type":"STRING"
                        }
                     ]
                  }
               ]
            }
         ]
      },
      {
         "name":"is_sponsored_click",
         "type":"BOOLEAN"
      },
      {
         "name":"is_express_click",
         "type":"BOOLEAN"
      },
      {
         "name":"item2partner_id",
         "type":"INTEGER"
      },
      {
         "name":"inbound_ymd",
         "type":"DATE"
      }
]
EOF

}

resource "google_bigquery_table" "item_master_2_0_archive" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "item_master_2_0_archive"

  schema = <<EOF
[
    {
       "name":"key",
       "type":"RECORD",
       "fields":[
          {
             "name":"item_id",
             "type":"INTEGER"
          }
       ]
    },
    {
       "name":"item_group_id",
       "type":"INTEGER"
    },
    {
       "name":"item_type_id",
       "type":"INTEGER"
    },
    {
       "name":"category_id",
       "type":"INTEGER"
    },
    {
       "name":"zip",
       "type":"STRING"
    },
    {
       "name":"city",
       "type":"STRING"
    },
    {
       "name":"country",
       "type":"STRING"
    },
    {
       "name":"country_code",
       "type":"STRING"
    },
    {
       "name":"stars",
       "type":"INTEGER"
    },
    {
       "name":"cnt_active_partner",
       "type":"INTEGER"
    },
    {
       "name":"cnt_active_combined_partner",
       "type":"INTEGER"
    }
]
EOF

}

resource "google_bigquery_table" "bid_modifier_ttt_breakouts" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "bid_modifier_ttt_breakouts"

  schema = <<EOF
[
    {
        "name": "breakout_value",
        "type": "INTEGER"
    },
    {
        "name": "upper_limit",
        "type": "INTEGER"
    },
    {
        "name": "lower_limit",
        "type": "INTEGER"
    },
    {
        "name": "start_ymd",
        "type": "INTEGER"
    },
    {
        "name": "end_ymd",
        "type": "INTEGER"
    }
]
EOF

}

resource "google_bigquery_table" "bid_modifier_gs_breakouts" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "bid_modifier_gs_breakouts"

  schema = <<EOF
[
    {
        "name": "breakout_value",
        "type": "INTEGER"
    },
    {
        "name": "upper_limit",
        "type": "INTEGER"
    },
    {
        "name": "lower_limit",
        "type": "INTEGER"
    },
    {
        "name": "start_ymd",
        "type": "INTEGER"
    },
    {
        "name": "end_ymd",
        "type": "INTEGER"
    }
]
EOF

}

resource "google_bigquery_table" "bid_modifier_los_breakouts" {
  project    = var.project
  dataset_id = google_bigquery_dataset.dataset.dataset_id
  table_id   = "bid_modifier_los_breakouts"

  schema = <<EOF
[
    {
        "name": "breakout_value",
        "type": "INTEGER"
    },
    {
        "name": "upper_limit",
        "type": "INTEGER"
    },
    {
        "name": "lower_limit",
        "type": "INTEGER"
    },
    {
        "name": "start_ymd",
        "type": "INTEGER"
    },
    {
        "name": "end_ymd",
        "type": "INTEGER"
    }
]
EOF

}