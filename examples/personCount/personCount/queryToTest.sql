CREATE TEMP FUNCTION splitRooms(room_detail STRING) RETURNS ARRAY<STRING> AS (
  SPLIT(REGEXP_REPLACE(room_detail, "^[0-9]+#", ""),"#")
);

CREATE TEMP FUNCTION getPersonCount(split_rooms ARRAY<STRING>) AS ((
  SELECT
    SUM(CAST(SPLIT(room,',')[OFFSET(0)] AS INT64) + CAST(SPLIT(room,',')[OFFSET(1)] AS INT64))
  FROM
    UNNEST(split_rooms) AS room
));
UPDATE `{{project}}.jarvis_sample_01.person{{tablePostfix}}`
SET result = (select getPersonCount(splitRooms((select roomDetail from `{{project}}.jarvis_sample_01.person{{tablePostfix}}`)))) where id = 1;