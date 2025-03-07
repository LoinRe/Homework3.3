-возраст не меньше 16
ALTER TABLE student
    ADD CONSTRAINT check_age CHECK (age >= 16);

-имена уникальные
ALTER TABLE student
    ADD CONSTRAINT unique_name
        UNIQUE (name);

-имена не равны нулю
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

-цвет факультета уникальный
ALTER TABLE faculty
    ADD CONSTRAINT unique_name_color
        UNIQUE (name, color);

-возраст 20 по умолчанию
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;
