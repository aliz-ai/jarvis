create temp function shared_inventory_breakout_value(value int64)
  returns int64
  as (
    case
      when value = 1 then 1
      when value = 2 then 2
      when value < 6 then 3
      when value < 11 then 6
      when value < 21 then 11
      when value < 31 then 21
      else 31
    end
  );

/* this ID should be calculated multiple times, so extracted to UDF */
create temp function cpa_submodel_id_value(payload_set array<struct<key int64, value string>>)
  returns string
  as (
    case
      when payload_set is not null then SPLIT((select value from unnest(payload_set) where key = 533), ',')[OFFSET(7)]
      else null
    end
  );


CREATE TEMP FUNCTION splitRooms(room_detail STRING) RETURNS ARRAY<STRING> AS (
  SPLIT(REGEXP_REPLACE(room_detail, "^[0-9]+#", ""),"#")
);

CREATE TEMP FUNCTION getPersonCount(split_rooms ARRAY<STRING>) AS ((
  SELECT
    SUM(CAST(SPLIT(room,',')[OFFSET(0)] AS INT64) + CAST(SPLIT(room,',')[OFFSET(1)] AS INT64))
  FROM
    UNNEST(split_rooms) AS room
));

create table if not exists `{{project}}.jarvis_sample_01.result{{tablePostfix}}` AS (
select
    ROW_NUMBER() over () As id,
  tbl.* except(ttt, session_date_id, tags, session_properties_tags, pld_payload_set, step,bidding_model,item_id),

  /* add_device_type */
  case
    when tbl.agent_id in (1,2,3,4,5,6,7,11,14) then 'Desktop'
    when tbl.agent_id in (9,10,12) then 'Phone'
    when tbl.agent_id in (8,13,22) then 'Tablet'
    else 'unknown'
  end as device_type,

  /* add_item_type */
  case
    when tbl.item_group_id in (2,5,6,7,9,10,47,53,56) then 'Hotel'
    when tbl.item_group_id in (3,4,8,11,49,50,54) then 'AA'
    else 'unknown'
  end as item_type,

  /* add_item_group_name */
  case
    when tbl.item_group_id = 2 then 'hotel'
    when tbl.item_group_id = 3 then 'bed_and_breakfast'
    when tbl.item_group_id = 4 then 'guesthouse_pension'
    when tbl.item_group_id = 5 then 'motel'
    when tbl.item_group_id = 6 then 'aparthotel_or_serviced_apartment'
    when tbl.item_group_id = 8 then 'house_or_apartment'
    when tbl.item_group_id = 9 then 'hostel'
    when tbl.item_group_id = 11 then 'camping'
    when tbl.item_group_id = 49 then 'hostal'
    when tbl.item_group_id = 50 then 'casa_rural_or_agriturismo'
    when tbl.item_group_id = 53 then 'resort'
    when tbl.item_group_id = 54 then 'pousada'
    else 'unknown'
  end as item_group_name,

  /* add_shared_inventory_breakout (item_active_partner) */
  shared_inventory_breakout_value(tbl.item_active_partner) as item_active_partner_breakout,

  /* add_shared_inventory_breakout (item_active_partner) */
  shared_inventory_breakout_value(tbl.item_active_combined_partner) as item_active_combined_partner_breakout,

  /* add_destination_type */
  if (tbl.item_country = locale, 'domestic', 'international') as destination_type,

  /* add_search_type_name */
  case
    when tbl.search_type = 0 then 'other'
    when tbl.search_type = 1 then 'short_leisure'
    when tbl.search_type = 2 then 'business_traveller'
    when tbl.search_type = 3 then 'vacation_holiday_longtrip'
    when tbl.search_type = 4 then 'standard_date_user'
    else 'unknown'
  end as user_search_type,

  /* add_user_loyalty */
  case
    when exists(select * from tbl.tags where array_element = 'member') then 'member'
    when exists(select * from tbl.session_properties_tags where array_element = 'new_user') then 'new_user'
    else 'repeater'
  end as user_loyalty,

  /* add_user_travel_type */
  case
    when exists(select * from tbl.session_properties_tags where array_element = 'international_traveller') then 'international_traveller'
    when exists(select * from tbl.session_properties_tags where array_element = 'domestic_and_international_traveller') then 'domestic_and_international_traveller'
    when exists(select * from tbl.session_properties_tags where array_element = 'domestic_traveller') then 'domestic_traveller'
    else 'unknown'
  end as user_travel_type,

  /* add_clickout_source_group */
  case
    when tbl.clickout_source in (100, 101, 110, 210, 333, 400, 410, 500, 510, 124, 150) then 'Champion'
    when tbl.clickout_source in (121, 120, 420, 4) then 'Alternative Deals'
    when tbl.clickout_source in (123, 125, 151) then 'Cheapest Deals'
    when tbl.clickout_source in (140, 141, 143, 144, 145, 147, 148, 540, 128) then 'Deals Slideout'
    when tbl.clickout_source in (200, 220, 230, 250, 270, 300, 310) then 'Map Champion'
    when tbl.clickout_source in (201, 221, 231) then 'Map Cheapest Deals'
    when tbl.clickout_source in (240, 260, 261, 262, 263) then 'Map Other'
    when tbl.clickout_source in (510, 511, 512, 513) then 'Weekend'
    else 'Others'
  end as clickout_source_group,

  /* add_is_last_clickout */
  if (row_number() over (partition by session_date_id order by step desc) = 1, 1, 0) as is_last_clickout,

  /* add_group_size */
  case
    when ymd < '2021-05-01' then null
    when tbl.room_detail is not null then getPersonCount(splitRooms(room_detail))
    when tbl.room_type = 1 then 1
    else 2
  end as gs,

  /* add_modifier_breakouts (booked_ttt) */
  t_booked_ttt.breakout_value as booked_ttt_breakout,

  /* add_modifier_breakouts (booked_los) */
  t_booked_los.breakout_value as booked_los_breakout,

  /* add_modifier_breakouts (ttt) */
  t_ttt.breakout_value as ttt_breakout,

  /* add_modifier_breakouts (los) */
  t_los.breakout_value as los_breakout,

  /* add_modifier_breakouts (gs) */
  case
    when ymd < '2021-05-01' then 0
    else t_gs.breakout_value
  end as gs_breakout,

  case
    when ymd < '2021-05-01' then 0
    else tbl.cpa_campaign_id
  end as campaign_id,

  case
    when ymd < '2021-05-01' then "CPC"
    else tbl.bidding_model
  end as bidding_model,

  /* add_submodel_id */
  cpa_submodel_id_value(pl.map) as cpa_submodel_id,

  /* add_bidding_submodel */
  case
    when cpa_submodel_id_value(pl.map) = '0' then 'NOT_DEFINED'
    when cpa_submodel_id_value(pl.map) = '1' then 'NET_CPA_ON_TRANSACTION'
    when cpa_submodel_id_value(pl.map) = '2' then 'GROSS_CPA'
    when cpa_submodel_id_value(pl.map) = '3' then 'NET_CPA_ON_CONSUMPTION'
    when ymd < '2021-05-01' OR tbl.bidding_model = 'CPC' then 'CPC'
    else 'unknown'
  end as bidding_submodel,

  /* add_business_model */
  case
    when CAST(tbl.is_sponsored_click AS INT64) = 1 then 'SL'
    else 'Meta'
  end as business_model
from
  `{{project}}.jarvis_sample_01.cte_item_master_joined` tbl
full join
  `{{project}}.jarvis_sample_01.cte_ttt_breakouts` t_booked_ttt on true
full join
  `{{project}}.jarvis_sample_01.cte_los_breakouts` t_booked_los on true
full join
  `{{project}}.jarvis_sample_01.cte_ttt_breakouts` t_ttt on true
full join
  `{{project}}.jarvis_sample_01.cte_los_breakouts` t_los on true
full join
  `{{project}}.jarvis_sample_01.cte_ttt_breakouts` t_gs on true, unnest(tbl.pld_payload_set) as pl
  order by booking_value
  );
