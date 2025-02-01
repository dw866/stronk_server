-- Delete existing data
DELETE FROM exercises;

-- Insert sample data
INSERT INTO exercises (name, body_part, category)
VALUES
    ('Push-ups', 'Upper body', 'Bodyweight'),
    ('Squats', 'Lower body', 'Strength'),
    ('Plank', 'Core', 'Bodyweight'),
    ('Bench Press', 'Upper body', 'Strength'),
    ('Deadlift', 'Lower body', 'Strength');

-- Optionally, you can add more INSERT statements for additional sample data

-- Commit the changes
COMMIT;

