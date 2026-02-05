-- DEPRECATED: Este archivo fue creado automáticamente por la asistencia, pero el usuario pidió
-- que NO se creen migraciones ni archivos adicionales. No ejecutar este script.
-- En su lugar, usa las sentencias SQL que aparecen en la documentación del proyecto o
-- copia y pega manualmente las consultas provistas por el asistente en tu cliente DB.

-- (Archivo dejado intencionalmente inactivo)

-- Si quieres eliminarlo, bórralo manualmente del repositorio.

-- Flyway migration: rellenar la columna description para los libros existentes
-- Solo actualiza si description es NULL o cadena vacía

USE books_catalogue_db;

UPDATE books SET description = 'Clásico de la literatura española.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-376-0494-7';
UPDATE books SET description = 'Saga familiar en Macondo.' WHERE (description IS NULL OR description = '') AND isbn = '978-03-075-4726-7';
UPDATE books SET description = 'Novela distópica sobre vigilancia.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-452-28423-4';
UPDATE books SET description = 'Fábula poética para todas las edades.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-204-3869-1';
UPDATE books SET description = 'Novela experimental y fragmentaria.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-204-0145-9';
UPDATE books SET description = 'Novela ambientada en la Barcelona gótica.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-08-04696-6';
UPDATE books SET description = 'Historia de amor y paciencia.' WHERE (description IS NULL OR description = '') AND isbn = '978-03-075-3889-0';
UPDATE books SET description = 'Futuro donde queman libros.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-345-34296-2';
UPDATE books SET description = 'Historias de hermanas y crecimiento.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-204-7146-9';
UPDATE books SET description = 'Thriller de misterio y símbolos.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-307-47492-1';
UPDATE books SET description = 'Inicio de la saga de Harry Potter.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-9838-912-4';
UPDATE books SET description = 'Comedia romántica clásica.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-204-6761-5';
UPDATE books SET description = 'Reflexión sobre crimen y moral.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-206-0649-7';
UPDATE books SET description = 'Épica de fantasía en la Tierra Media.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-618-00222-1';
UPDATE books SET description = 'Viaje épico de Odiseo.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-206-4163-3';
UPDATE books SET description = 'Ejemplo de libro no visible.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-000-00000-0';
UPDATE books SET description = 'Crónica novelada de un asesinato.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-397-0001-0';
UPDATE books SET description = 'Novela sobre belleza y corrupción.' WHERE (description IS NULL OR description = '') AND isbn = '978-84-206-0723-4';
UPDATE books SET description = 'Competición mortal en arena televisada.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-439-02348-1';
UPDATE books SET description = 'Aventura de Bilbo Bolsón.' WHERE (description IS NULL OR description = '') AND isbn = '978-0-618-00221-4';

-- Rellenar cualquier fila restante sin description con una descripción genérica basada en el título
UPDATE books SET description = CONCAT('Descripción: ', title) WHERE description IS NULL OR description = '';

-- Resumen: contar filas con description no vacío
SELECT COUNT(*) AS total_with_description FROM books WHERE description IS NOT NULL AND description <> '';
