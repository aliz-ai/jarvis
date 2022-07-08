CREATE SCHEMA IF NOT EXISTS `{{project}}.jarvis_sample_01`;
CREATE TABLE IF NOT EXISTS `{{project}}.jarvis_sample_01.car{{tablePostfix}}`
	(
		id INTEGER NOT NULL OPTIONS(description="The license plate of the car "),
		brand STRING OPTIONS(description="The brand of the car "),
		price INTEGER OPTIONS(description="The price of the car "),
		nickname BOOLEAN OPTIONS(description="The nickname of the car ")
	);

TRUNCATE TABLE `{{project}}.jarvis_sample_01.car{{tablePostfix}}`;

INSERT INTO `{{project}}.jarvis_sample_01.car{{tablePostfix}}`
(id, brand, price, nickname)
VALUES
(1, "BMW", 800, null);

INSERT INTO `{{project}}.jarvis_sample_01.car{{tablePostfix}}`
(id, brand, price, nickname)
VALUES
(2, "BMW", 1800, true);

INSERT INTO `{{project}}.jarvis_sample_01.car{{tablePostfix}}`
(id, brand, price, nickname)
VALUES
(3, "BMW", 2800, null);