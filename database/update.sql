ALTER TABLE schedule
    ADD COLUMN study_date DATE NULL;

UPDATE schedule
SET study_date = CURDATE()
WHERE study_date IS NULL;

ALTER TABLE schedule
    MODIFY COLUMN study_date DATE NOT NULL;
