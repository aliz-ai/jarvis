CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.booking_net_cpa_revenue{{tablePostfix}}`
	(   session_date_id	STRING, --num
        request_id_page_log	STRING,
        clickout_ymd	INTEGER,
        clickout_ymdhms	STRING, --num
        locale	STRING,
        item_id	INTEGER,
        item2partner_id	INTEGER,
        clickout_timestamp	INTEGER,
        booking_id	STRING,
        partner_id	INTEGER,
        trv_reference	STRING,
        request_id_conversion_api	STRING,
        inbound_timestamp	INTEGER,
        data_source     ARRAY<STRUCT<array_element STRING>>,
        booking_date_ymd	INTEGER,
        booking_date_ymdhms	STRING, --num
        partner_reference	STRING,
        arrival_date_ymd	DATE,
        departure_date_ymd	DATE,
        room_count	INTEGER,
        booking_value_eurocents	INTEGER,
        partner_margin_eurocents	INTEGER,
        booking_value_source_currency	FLOAT64,
        source_currency_code	STRING,
        source_exchange_rate	FLOAT64,
        update_date_ymd	INTEGER,
        update_date_ymdhms	INTEGER,
        cancellation_date_ymd	INTEGER,
        cancellation_date_ymdhms	INTEGER,
        refund_value_eurocents	INTEGER,
        clickout    ARRAY<STRUCT<session_date_id STRING, request_id_page_log STRING, partner_id INTEGER, item_id INTEGER, locale STRING, source INTEGER, ymd INTEGER, ymdhms STRING, pld_payload_set ARRAY<STRUCT<map ARRAY<STRUCT<key INTEGER, value STRING>>>>>>,
        cpa_campaign_id	INTEGER,
        cpa_input	NUMERIC,
        cpa_model	STRING,
        cpa_submodel	STRING,
        fallback_cpa_input	NUMERIC,
        revenue	NUMERIC,
        revenue_source	STRING,
        calculation_source	STRING,
        invoice_source	STRING,
        invoice_type	STRING,
        ymd	DATE
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.booking_net_cpa_revenue{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.booking_net_cpa_revenue{{tablePostfix}}`
(session_date_id, request_id_page_log, clickout_ymd, clickout_ymdhms, locale, item_id, item2partner_id, clickout_timestamp, booking_id, partner_id, trv_reference, request_id_conversion_api, inbound_timestamp, data_source, booking_date_ymd, booking_date_ymdhms, partner_reference, arrival_date_ymd, departure_date_ymd, room_count, booking_value_eurocents, partner_margin_eurocents, booking_value_source_currency, source_currency_code, source_exchange_rate, update_date_ymd, update_date_ymdhms, cancellation_date_ymd, cancellation_date_ymdhms, refund_value_eurocents, clickout, cpa_campaign_id, cpa_input, cpa_model, cpa_submodel, fallback_cpa_input, revenue, revenue_source, calculation_source, invoice_source, invoice_type, ymd)
VALUES
("2022010118400006964",
 "YdCcnLRSTEuaROJKohAM3AAAAEE",
 20220101,
 "20220101182532",
 "US",
 10073200,
 624839354,
 1641061532,
 "20176775",
 3148,
 "8f986c6d-3ec2-4414-a593-fc5cd56b4c4a",
 "859457d-2486d54072635f18391d53d95d",
 1641080614,
 [STRUCT("express")],
 20220101,
 "20220101233553",
 "319925",
 '2022-01-07',
 '2022-01-08',
 2,
 51586,
 0,
 586.66998291015625,
 "USD",
 1.1372679471969604,
 0,
 0,
 0,
 0,
 0,
 [STRUCT("2022010118400006964", "YdCcnLRSTEuaROJKohAM3AAAAEE", 3148, 10073200, "US", 100, 20220101, "20220101182532", [STRUCT([STRUCT(133, "2d57a7bc-ba28-41e7-9de8-afd12a914572")])])],
 1,
 0.084,
 "NET CPA",
 "NET_CPA_ON_TRANSACTION",
 0.084,
 4333,
 "STANDARD",
 "BOOKING_CONFIRMATION",
 "confirmation",
 "DEBIT",
 '2022-01-01');

 CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
 CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.session_stats_master{{tablePostfix}}`
 	(    session_date_id	STRING,
 	     session_id	STRING,
         traffic_type_parent	INTEGER,
         traffic_type_child	INTEGER,
         agent_id	INTEGER,
         tags   ARRAY<STRUCT<array_element STRING>>,
         session_properties_tags   ARRAY<STRUCT<array_element STRING>>,
         search_type	INTEGER,
         is_app	INTEGER,
         crawler_id	INTEGER,
         is_core	BOOLEAN,
         co_log_entries ARRAY<STRUCT<array_element ARRAY<STRUCT<trv_reference STRING, page_id INTEGER, locale STRING, request_id STRING, is_standard_date INTEGER, partner_id INTEGER,
         item_id INTEGER, base_cpc INTEGER, modified_cpc INTEGER, sl_base_cpc INTEGER, sl_modified_cpc INTEGER, clickout_source INTEGER, arrival_days INTEGER, departure_days INTEGER,
         room_detail STRING, room_type INTEGER, property_group_id INTEGER, revenue INTEGER, date_id STRING, price FLOAT64, is_sponsored_click BOOLEAN, is_tracking_partner INTEGER,
         bidding_model STRING, cpa_campaign_id INTEGER, cpa_input FLOAT64, auction_vpc FLOAT64, timestamp INTEGER, pld_payload_set ARRAY<STRUCT<map ARRAY<STRUCT<key INTEGER, value STRING>>>>>>>>,

         page_log_entries ARRAY<STRUCT<array_element ARRAY<STRUCT<step INTEGER, page_log_full_details ARRAY<STRUCT<map ARRAY<STRUCT<key INTEGER,
         value ARRAY<STRUCT<key INTEGER, data STRING, int_value INTEGER, string_values ARRAY<STRUCT<array_element STRING>>,
         int_values ARRAY<STRUCT<array_element INTEGER>>, value_type STRING>>>>>>>>>>,
         ymd	DATE
 	);

 TRUNCATE TABLE `{{project}}.jarvis_sample_01.session_stats_master{{tablePostfix}}`;

 INSERT INTO `{{project}}.jarvis_sample_01.session_stats_master{{tablePostfix}}`
 (session_date_id, session_id, traffic_type_parent, traffic_type_child, agent_id, tags, session_properties_tags, search_type, is_app, crawler_id, is_core, co_log_entries, page_log_entries, ymd)
 VALUES
 ("2022010115810091875",
  "50F5130A8347B",
  2,
  201,
  10,
  [STRUCT("mobile")],
  [STRUCT("city_weekend_traveller")],
  1,
  0,
  0,
  true,
  [STRUCT([STRUCT("8f986c6d-3ec2-4414-a593-fc5cd56b4c4a", 8001, "RU", "YdB5b3bwZgmK3UsG5s4XjgAAAD8", 0, 3148,
  10073200, 17, 15, 0, 0, 100, 6, 7,
  "1#3,2,6,6", 3, 0, 15, "2022010115920046241", 17.0, false, 0,
  "CPC", 0, 0.0, 0.0, 1641052527, [STRUCT([STRUCT(133, "1641052425")])])])],
  [STRUCT([STRUCT(47, [STRUCT([STRUCT(401, [STRUCT(401, "9213254,6761822,8101484,8960424", 0, [STRUCT("STRING")], [STRUCT(0)], "STRING")])])])])],
  '2022-01-01');

CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.consolidated_booking{{tablePostfix}}`
 	(    booking_id	STRING,
         partner_id	INTEGER,
         bucket_id	INTEGER,
         trv_reference	STRING,
         request_id_conversion_api	INTEGER,
         inbound_timestamp	INTEGER,
         data_source     ARRAY<STRUCT<array_element STRING>>,
         booking_date_ymd	INTEGER,
         booking_date_ymdhms	STRING,
         partner_reference	STRING,
         arrival_date_ymd	DATE,
         departure_date_ymd	DATE,
         room_count	INTEGER,
         booking_value_eurocents	INTEGER,
         partner_margin_eurocents	INTEGER,
         locale	STRING,
         booking_value_source_currency	STRING,
         source_currency_code	STRING,
         source_exchange_rate	NUMERIC,
         client_type	STRING,
         page_id	INTEGER,
         pld_type	INTEGER,
         pld_data	STRING,
         bidding_model	STRING,
         consolidation_source	STRING,
         priority	INTEGER,
         domain	STRING,
         clickout    ARRAY<STRUCT<session_date_id STRING, request_id_page_log STRING, partner_id INTEGER, item_id INTEGER, locale STRING, source INTEGER, ymd INTEGER, ymdhms STRING, pld_payload_set ARRAY<STRUCT<map ARRAY<STRUCT<key INTEGER, value STRING>>>>>>,
         is_sponsored_click	BOOLEAN,
         is_express_click	BOOLEAN,
         item2partner_id	INTEGER,
         inbound_ymd	DATE
 	);

 TRUNCATE TABLE `{{project}}.jarvis_sample_01.consolidated_booking{{tablePostfix}}`;

 INSERT INTO `{{project}}.jarvis_sample_01.consolidated_booking{{tablePostfix}}`
 (booking_id,	partner_id,	bucket_id,	trv_reference,	request_id_conversion_api,	inbound_timestamp,	 data_source,	booking_date_ymd,	booking_date_ymdhms,	partner_reference,	arrival_date_ymd,	departure_date_ymd,	room_count,	booking_value_eurocents,	partner_margin_eurocents,	locale,	booking_value_source_currency,	source_currency_code,	source_exchange_rate,	client_type,	page_id,	pld_type,	pld_data,	bidding_model,	consolidation_source,	priority,	domain,	 clickout,	is_sponsored_click,	is_express_click,	item2partner_id,	inbound_ymd)
 VALUES
 ("49029607",
  3148,
  0,
  "8f986c6d-3ec2-4414-a593-fc5cd56b4c4a",
  0,
  1641180308,
  [STRUCT("lpadv")],
  20220103,
  "20220103032507",
  "6776",
  '2022-01-08',
  '2022-01-10',
  1,
  25725,
  0,
  "BR",
  "1630.0",
  "BRL",
  6.336176872253418,
  "-",
  8106,
  0,
  "-",
  "CPA",
  "BUC",
  1,
  "www.trivago.com.br",
  [STRUCT("2022010118400006964", "YdCcnLRSTEuaROJKohAM3AAAAEE", 3148, 10073200, "US", 100, 20220101, "20220101182532", [STRUCT([STRUCT(133, "2d57a7bc-ba28-41e7-9de8-afd12a914572")])])],
  Null,
  Null,
  0,
  '2022-01-03');

CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.item_master_2_0_archive{{tablePostfix}}`
	(   key     ARRAY<STRUCT<item_id INTEGER>>,
	    item_group_id	INTEGER,
     item_type_id	INTEGER,
     category_id	INTEGER,
     zip	STRING,
     city	STRING,
     country	STRING,
     country_code	STRING,
     stars	INTEGER,
     cnt_active_partner	INTEGER,
     cnt_active_combined_partner	INTEGER
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.item_master_2_0_archive{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.item_master_2_0_archive{{tablePostfix}}`
(key, item_group_id, item_type_id, category_id, zip, city, country, country_code, stars, cnt_active_partner, cnt_active_combined_partner)
VALUES
([STRUCT(10073200)],
 8,
 1,
 14257,
 "90046",
 "Los Angeles",
 "USA",
 "US",
 0,
 17,
 9);

 CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
 CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.bid_modifier_ttt_breakouts{{tablePostfix}}`
 	(   breakout_value	INTEGER,
        upper_limit	INTEGER,
        lower_limit	INTEGER,
        start_ymd	INTEGER,
        end_ymd	INTEGER
 	);

 TRUNCATE TABLE `{{project}}.jarvis_sample_01.bid_modifier_ttt_breakouts{{tablePostfix}}`;

 INSERT INTO `{{project}}.jarvis_sample_01.bid_modifier_ttt_breakouts{{tablePostfix}}`
 (breakout_value, upper_limit, lower_limit, start_ymd, end_ymd)
 VALUES
 (0,
 1,
 -9999,
 19700101,
 99991231);

CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
 CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.bid_modifier_gs_breakouts{{tablePostfix}}`
 	(   breakout_value	INTEGER,
        upper_limit	INTEGER,
        lower_limit	INTEGER,
        start_ymd	INTEGER,
        end_ymd	INTEGER
 	);

 TRUNCATE TABLE `{{project}}.jarvis_sample_01.bid_modifier_gs_breakouts{{tablePostfix}}`;

 INSERT INTO `{{project}}.jarvis_sample_01.bid_modifier_gs_breakouts{{tablePostfix}}`
 (breakout_value, upper_limit, lower_limit, start_ymd, end_ymd)
 VALUES
 (3,
 5,
 3,
 19700101,
 99991231);

CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.bid_modifier_los_breakouts{{tablePostfix}}`
	(   breakout_value	INTEGER,
       upper_limit	INTEGER,
       lower_limit	INTEGER,
       start_ymd	INTEGER,
       end_ymd	INTEGER
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.bid_modifier_los_breakouts{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.bid_modifier_los_breakouts{{tablePostfix}}`
(breakout_value, upper_limit, lower_limit, start_ymd, end_ymd)
VALUES
(4,
6,
4,
19700101,
99991231);

create table if not exists `{{project}}.jarvis_sample_01.cte_clickout_staging` AS
(
  SELECT
    ssm.session_id,
    ssm.session_date_id,
    ssm.traffic_type_parent,
    ssm.traffic_type_child,
    ssm.agent_id,
    ssm.tags,
    ssm.session_properties_tags,
    ssm.search_type,
    ssm.is_app,
    ssm.crawler_id,
    ssm.is_core,
    cl_ae.trv_reference,
    cl_ae.page_id,
    cl_ae.locale,
    cl_ae.request_id,
    cl_ae.is_standard_date,
    cl_ae.partner_id,
    cl_ae.item_id,
    cl_ae.base_cpc,
    cl_ae.modified_cpc,
    cl_ae.sl_base_cpc,
    cl_ae.sl_modified_cpc,
    cl_ae.clickout_source,
    cl_ae.arrival_days,
    cl_ae.departure_days,
    cl_ae.room_detail,
    cl_ae.room_type,
    cl_ae.property_group_id,
    cl_ae.revenue,
    cl_ae.date_id,
    cl_ae.price,
    cl_ae.is_sponsored_click,
    cl_ae.is_tracking_partner,
    cl_ae.bidding_model,
    cl_ae.cpa_campaign_id,
    cl_ae.cpa_input,
    cl_ae.auction_vpc,
    cl_ae.timestamp,
    cl_ae.pld_payload_set,
    ple_ae.step,
    ple_ae.page_log_full_details,
    ymd
  FROM
    `{{project}}.jarvis_sample_01.session_stats_master{{tablePostfix}}` ssm,
    UNNEST(co_log_entries) AS cl,
    UNNEST(cl.array_element) as cl_ae,
    UNNEST(page_log_entries) AS ple,
    UNNEST(ple.array_element) as ple_ae
  WHERE
    ymd = '2022-01-01'
    AND is_core
);

create table if not exists `{{project}}.jarvis_sample_01.cte_booking_staging` AS
(
   SELECT
      IFNULL(c.bidding_model, n.cpa_model) AS bidding_model,
      n.booking_date_ymd,
      n.trv_reference,
      n.booking_value_eurocents,
      n.arrival_date_ymd,
      n.departure_date_ymd,
      n.booking_id,
      n.clickout,
      IFNULL(c.locale, n.locale) AS locale,
      n.partner_id,
      n.room_count,
      c.session_id,
      c.session_date_id,
      c.traffic_type_parent,
      c.traffic_type_child,
      c.agent_id,
      c.tags,
      c.session_properties_tags,
      c.search_type,
      c.is_app,
      c.crawler_id,
      c.is_core,
      c.request_id,
      c.is_standard_date,
      c.item_id,
      c.base_cpc,
      c.modified_cpc,
      c.sl_base_cpc,
      c.sl_modified_cpc,
      c.clickout_source,
      c.arrival_days,
      c.departure_days,
      c.room_detail,
      c.room_type,
      c.property_group_id,
      n.revenue,
      c.date_id,
      c.price,
      c.is_sponsored_click,
      c.is_tracking_partner,
      c.cpa_campaign_id,
      c.cpa_input,
      c.auction_vpc,
      c.timestamp,
      c.pld_payload_set,
      c.step,
      n.invoice_source,
      n.invoice_type,
      null as is_express_click,
      item2partner_id,
      data_source,
      n.ymd
      FROM
        `{{project}}.jarvis_sample_01.booking_net_cpa_revenue{{tablePostfix}}` n
      LEFT JOIN
        `{{project}}.jarvis_sample_01.cte_clickout_staging` c
      ON
        n.partner_id=c.partner_id
        AND n.trv_reference=c.trv_reference

     union all

       SELECT
       t.bidding_model,
       t.booking_date_ymd,
       t.trv_reference,
       t.booking_value_eurocents,
       t.arrival_date_ymd,
       t.departure_date_ymd,
       t.booking_id,
       t.clickout,
       t.locale,
       t.partner_id,
       t.room_count,
       c.session_id,
       c.session_date_id,
       c.traffic_type_parent,
       c.traffic_type_child,
       c.agent_id,
       c.tags,
       c.session_properties_tags,
       c.search_type,
       c.is_app,
       c.crawler_id,
       c.is_core,
       c.request_id,
       c.is_standard_date,
       c.item_id,
       c.base_cpc,
       c.modified_cpc,
       c.sl_base_cpc,
       c.sl_modified_cpc,
       c.clickout_source,
       c.arrival_days,
       c.departure_days,
       c.room_detail,
       c.room_type,
       c.property_group_id,
       if(t.bidding_model='CPA',CAST((SELECT values[SAFE_OFFSET(3)] from (SELECT SPLIT(param.value,',') values FROM UNNEST(t.clickout) as co,UNNEST(co.pld_payload_set) as pl, UNNEST(pl.map) AS param WHERE param.key=553)) AS FLOAT64) *t.booking_value_eurocents,0) AS revenue,
       c.date_id,
       c.price,
       c.is_sponsored_click,
       c.is_tracking_partner,
       c.cpa_campaign_id,
       c.cpa_input,
       c.auction_vpc,
       c.timestamp,
       c.pld_payload_set,
       c.step,
       'confirmation' AS invoice_source,
       'DEBIT' AS invoice_type,
       is_express_click,
       item2partner_id,
       data_source,
       ymd
     FROM
       `{{project}}.jarvis_sample_01.consolidated_booking{{tablePostfix}}`  t
     LEFT JOIN
       `{{project}}.jarvis_sample_01.cte_clickout_staging`  c
     ON
       t.partner_id=c.partner_id
       AND t.trv_reference=c.trv_reference
   );

create table if not exists `{{project}}.jarvis_sample_01.cte_clickout` AS (
  SELECT
    'clickout and cpc' input_type,
    session_date_id,
    traffic_type_parent,
    traffic_type_child,
    agent_id,
    tags,
    session_properties_tags,
    search_type,
    is_app,
    crawler_id,
    is_core,
    locale,
    is_standard_date,
    partner_id,
    item_id,
    base_cpc,
    modified_cpc,
    sl_base_cpc,
    sl_modified_cpc,
    clickout_source,
    arrival_days,
    departure_days,
    room_detail,
    room_type,
    property_group_id,
    revenue,
    is_sponsored_click,
    is_tracking_partner,
    IFNULL(bidding_model,'CPC') bidding_model,
    cpa_campaign_id,
    cpa_input,
    auction_vpc,
    pld_payload_set,
    0 as booking_value,
    step,
    arrival_days as ttt,
    departure_days-arrival_days as los,
    'Confirmation' as invoice_source,
    'DEBIT' as invoice_type,
    arrival_days as booked_ttt,
    departure_days-arrival_days as booked_los,
    IF((SELECT CAST(param.value AS INT) values FROM UNNEST(t.pld_payload_set) as p, UNNEST(p.map) AS param WHERE param.key=264)=1,TRUE,FALSE) AS is_express_click,
    CAST(CAST(null AS STRING) AS DATE) as booked_ymd,
    ymd AS clicked_ymd,
    ymd
  FROM
    `{{project}}.jarvis_sample_01.cte_clickout_staging` t
  WHERE
      t.ymd=ymd
);

create table if not exists `{{project}}.jarvis_sample_01.cte_booking` AS (
       SELECT
              'booking and cpa' input_type,
              t.session_date_id,
              traffic_type_parent,
              traffic_type_child,
              agent_id,
              tags,
              session_properties_tags,
              search_type,
              is_app,
              IFNULL(crawler_id,0) as crawler_id,
              is_core,
              t.locale,
              is_standard_date,
              t.partner_id,
              t.item_id,
              0 as base_cpc,
              0 as modified_cpc,
              0 as sl_base_cpc,
              0 as sl_modified_cpc,
              clickout_source,
              arrival_days,
              departure_days,
              room_detail,
              room_type,
              property_group_id,
              revenue,
              is_sponsored_click,
              is_tracking_partner,
              IFNULL(bidding_model,'CPC') as bidding_model,
              cpa_campaign_id,
              IFNULL(if(bidding_model<>'CPA',0,COALESCE(t.cpa_input,CAST((SELECT values[SAFE_OFFSET(3)] from (SELECT SPLIT(param.value,',') values FROM UNNEST(t.clickout) as co,UNNEST(co.pld_payload_set) as p, UNNEST(p.map) AS param WHERE param.key=553) ) AS FLOAT64),CAST((SELECT values[SAFE_OFFSET(3)] from (SELECT SPLIT(param.value,',') values FROM UNNEST(t.pld_payload_set) as pl,UNNEST(pl.map) AS param WHERE param.key=553) ) AS FLOAT64))),0) as cpa_input,
              0 as auction_vpc,
              IFNULL(co.pld_payload_set,t.pld_payload_set) as pld_payload_set,
              booking_value_eurocents/100 as booking_value,
              step,
              IFNULL(arrival_days, DATE_DIFF(arrival_date_ymd,IFNULL(PARSE_DATE("%Y%m%d", CAST(co.ymd AS STRING)),t.ymd),DAY)) AS ttt,
              IFNULL(departure_days-arrival_days,DATE_DIFF(departure_date_ymd,arrival_date_ymd,DAY)) AS los,
              initcap(invoice_source) as invoice_source,
              invoice_type,
              IFNULL(DATE_DIFF(arrival_date_ymd,IFNULL(PARSE_DATE("%Y%m%d", CAST(co.ymd AS STRING)),t.ymd),DAY),arrival_days) AS booked_ttt,
              IFNULL(DATE_DIFF(departure_date_ymd,arrival_date_ymd,DAY),departure_days-arrival_days) AS booked_los,
              is_express_click,
              t.ymd  as booked_ymd,
              PARSE_DATE("%Y%m%d", CAST(co.ymd AS STRING)) as clicked_ymd,
              t.ymd
       FROM
              `{{project}}.jarvis_sample_01.cte_booking_staging` t, UNNEST(t.clickout) as co
);

create table if not exists `{{project}}.jarvis_sample_01.cte_clickout_booking_union` AS (

  SELECT
    *
  FROM
    `{{project}}.jarvis_sample_01.cte_clickout`

  UNION ALL

  SELECT
    *
  FROM
    `{{project}}.jarvis_sample_01.cte_booking`
);

create table if not exists `{{project}}.jarvis_sample_01.cte_item_master` AS (
       SELECT
            k.item_id as item__id,
            MIN(item_group_id) AS item_group_id,
            MIN(item_type_id) AS item_type_id,
            MIN(category_id) AS category_id,
            MIN(zip) AS item_zip,
            MIN(city) AS item_city,
            MIN(country) AS item_country,
            MIN(country_code) AS item_country_code,
            MIN(stars) AS item_stars,
            MIN(cnt_active_partner) AS item_active_partner,
            MIN(cnt_active_combined_partner) AS item_active_combined_partner
       FROM
            `{{project}}.jarvis_sample_01.item_master_2_0_archive{{tablePostfix}}`, UNNEST(key) as k
       WHERE
           k.item_id IS NOT NULL
       GROUP BY
            k.item_id
);

create table if not exists `{{project}}.jarvis_sample_01.cte_item_master_joined` AS (
  select
    *
  from
    `{{project}}.jarvis_sample_01.cte_clickout_booking_union` u
  left join
    `{{project}}.jarvis_sample_01.cte_item_master` i
  on
    u.item_id=i.item__id

);

create table if not exists `{{project}}.jarvis_sample_01.cte_ttt_breakouts` as (
       SELECT
              breakout_value,
              upper_limit,
              lower_limit
       FROM
              `{{project}}.jarvis_sample_01.bid_modifier_ttt_breakouts{{tablePostfix}}`
       WHERE
            20220101 BETWEEN start_ymd AND end_ymd
);

create table if not exists `{{project}}.jarvis_sample_01.cte_gs_breakouts` as (
       SELECT
              breakout_value,
              upper_limit,
              lower_limit
       FROM
              `{{project}}.jarvis_sample_01.bid_modifier_gs_breakouts{{tablePostfix}}`
       WHERE
            20220101 BETWEEN start_ymd AND end_ymd

       UNION DISTINCT

       SELECT
              0,
              0,
              0
       FROM
              `{{project}}.jarvis_sample_01.bid_modifier_gs_breakouts{{tablePostfix}}`
);

create table if not exists `{{project}}.jarvis_sample_01.cte_los_breakouts` as (
       SELECT
              breakout_value,
              upper_limit,
              lower_limit
       FROM
              `{{project}}.jarvis_sample_01.bid_modifier_los_breakouts{{tablePostfix}}`
       WHERE
              20220101 BETWEEN start_ymd AND CAST(end_ymd AS INT64)

)