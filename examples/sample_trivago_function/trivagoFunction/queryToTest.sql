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

UPDATE `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}`
SET result = case
    when id = 1 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 1))
    when id = 2 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 2))
    when id = 3 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 3))
    when id = 4 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 4))
    when id = 5 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 5))
    when id = 6 then (SELECT shared_inventory_breakout_value(num) FROM (select num from `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}` where id = 6))
    end
    where id in(1, 2, 3, 4, 5, 6);
