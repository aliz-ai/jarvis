CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}`
	(
		id INTEGER NOT NULL OPTIONS(description="The id "),
		num INTEGER NOT NULL OPTIONS(description="The number to test the function "),
		result INTEGER NOT NULL OPTIONS(description="The result number "),
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.trivago{{tablePostfix}}`
(id, num, result)
VALUES
(3, 4, 0);