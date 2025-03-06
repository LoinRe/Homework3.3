-- Получение студентов с названиями факультетов
SELECT student.name AS student_name, student.age AS student_age, faculty.name AS faculty_name
FROM student
LEFT JOIN faculty ON student.faculty_id = faculty.id;

-- Студенты с аватарками
SELECT
    student.name AS student_name,
    student.age AS student_age
FROM student
INNER JOIN avatar ON avatar.student_id = student.id;