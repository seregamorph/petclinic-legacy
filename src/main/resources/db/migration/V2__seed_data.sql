INSERT INTO vets (first_name, last_name, specialty) VALUES
    ('James',  'Carter',   NULL),
    ('Helen',  'Leary',    'radiology'),
    ('Linda',  'Douglas',  'dentistry'),
    ('Rafael', 'Ortega',   'surgery'),
    ('Henry',  'Stevens',  'radiology'),
    ('Sharon', 'Jenkins',  NULL);

INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES
    ('George', 'Franklin',  '110 W. Liberty St.',  'Madison',    '6085551023'),
    ('Betty',  'Davis',     '638 Cardinal Ave.',   'Sun Prairie', '6085551749'),
    ('Eduardo','Rodriquez', '2693 Commerce St.',   'McFarland',  '6085558763'),
    ('Harold', 'Davis',     '563 Friendly St.',    'Windsor',    '6085553198');

INSERT INTO pets (name, birth_date, type, owner_id) VALUES
    ('Leo',     '2010-09-07', 'cat',    1),
    ('Basil',   '2012-08-06', 'hamster',2),
    ('Rosy',    '2011-04-17', 'dog',    3),
    ('Jewel',   '2010-03-07', 'dog',    3),
    ('Iggy',    '2010-11-30', 'lizard', 4);

INSERT INTO visits (pet_id, visit_date, description) VALUES
    (1, '2023-01-01', 'rabies shot'),
    (3, '2023-03-04', 'neutered'),
    (1, '2023-06-04', 'spayed');
