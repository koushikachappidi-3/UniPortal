DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_course_code'
    ) THEN
        ALTER TABLE course
            ADD CONSTRAINT uk_course_code UNIQUE (code);
    END IF;
END $$;

