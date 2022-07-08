CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.person{{tablePostfix}}`
	(
		id INTEGER NOT NULL OPTIONS(description="The id "),
		roomDetail STRING OPTIONS(description="The room detail "),
		result INTEGER OPTIONS(description="The result ")
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.person{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.person{{tablePostfix}}`
(id, roomDetail, result)
VALUES
(1, "1#3,2,6,6", 0);
