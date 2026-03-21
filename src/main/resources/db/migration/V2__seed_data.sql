INSERT INTO app_user (username, password_hash, role)
VALUES (
    'admin1',
    '$2a$10$AwVCcxsIggsDKeewSFpz5uPrU7vPH0HO17mIGo5GxKHSa0Cn8Qm4W',
    'ADMIN'
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO app_user (username, password_hash, role)
VALUES (
    'student1',
    '$2a$10$Pu4kyuAbGvkg4KNZCbiRfeCaQ6GCWQtVXrqYjdUAnTV.9lzs777PO',
    'STUDENT'
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO course (code, name, professor)
VALUES (
    'CS612',
    'Web Services',
    'Prof. X'
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO course (code, name, professor)
VALUES (
    'CS632P',
    'Parallel Computing',
    'Prof. Y'
)
ON CONFLICT (code) DO NOTHING;

