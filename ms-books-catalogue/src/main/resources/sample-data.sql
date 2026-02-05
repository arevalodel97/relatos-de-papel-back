-- Script de datos de ejemplo para books_catalogue_db
-- Ejecutar después de que la aplicación haya creado las tablas automáticamente

USE books_catalogue_db;

-- Insertar libros de ejemplo
INSERT INTO books (title, author, published_date, pages, category, isbn, rating, visible, stock, description) VALUES
('Don Quijote de la Mancha', 'Miguel de Cervantes', '1605-01-16', 1200, 'Clásicos', '978-84-376-0494-7', 5, true, 10, 'Clásico de la literatura española.'),
('Cien años de soledad', 'Gabriel García Márquez', '1967-05-30', 417, 'Realismo mágico', '978-03-075-4726-7', 5, true, 15, 'Saga familiar en Macondo.'),
('1984', 'George Orwell', '1949-06-08', 328, 'Distopía', '978-0-452-28423-4', 5, true, 20, 'Novela distópica sobre vigilancia.'),
('El principito', 'Antoine de Saint-Exupéry', '1943-04-06', 96, 'Infantil', '978-84-204-3869-1', 4, true, 30, 'Fábula poética para todas las edades.'),
('Rayuela', 'Julio Cortázar', '1963-06-28', 736, 'Experimental', '978-84-204-0145-9', 4, true, 8, 'Novela experimental y fragmentaria.'),
('La sombra del viento', 'Carlos Ruiz Zafón', '2001-04-17', 487, 'Misterio', '978-84-08-04696-6', 5, true, 12, 'Novela ambientada en la Barcelona gótica.'),
('El amor en los tiempos del cólera', 'Gabriel García Márquez', '1985-12-05', 348, 'Romance', '978-03-075-3889-0', 5, true, 7, 'Historia de amor y paciencia.'),
('Fahrenheit 451', 'Ray Bradbury', '1953-10-19', 194, 'Distopía', '978-0-345-34296-2', 4, true, 18, 'Futuro donde queman libros.'),
('Mujercitas', 'Louisa May Alcott', '1868-09-30', 450, 'Clásicos', '978-84-204-7146-9', 4, true, 5, 'Historias de hermanas y crecimiento.'),
('El código Da Vinci', 'Dan Brown', '2003-03-18', 454, 'Thriller', '978-0-307-47492-1', 3, true, 25, 'Thriller de misterio y símbolos.'),
('Harry Potter y la piedra filosofal', 'J.K. Rowling', '1997-06-26', 223, 'Fantasía', '978-84-9838-912-4', 5, true, 50, 'Inicio de la saga de Harry Potter.'),
('Orgullo y prejuicio', 'Jane Austen', '1813-01-28', 279, 'Romance', '978-84-204-6761-5', 5, true, 9, 'Comedia romántica clásica.'),
('Crimen y castigo', 'Fiódor Dostoyevski', '1866-01-01', 671, 'Clásicos', '978-84-206-0649-7', 5, true, 6, 'Reflexión sobre crimen y moral.'),
('El señor de los anillos', 'J.R.R. Tolkien', '1954-07-29', 1216, 'Fantasía', '978-0-618-00222-1', 5, true, 14, 'Épica de fantasía en la Tierra Media.'),
('La Odisea', 'Homero', '0800-01-01', 500, 'Épica', '978-84-206-4163-3', 5, true, 3, 'Viaje épico de Odiseo.'),
('Libro oculto de prueba', 'Autor Secreto', '2020-01-01', 10, 'Secreto', '978-0-000-00000-0', 1, false, 0, 'Ejemplo de libro no visible.'),
('Crónica de una muerte anunciada', 'Gabriel García Márquez', '1981-05-01', 160, 'Realismo mágico', '978-84-397-0001-0', 5, true, 11, 'Crónica novelada de un asesinato.'),
('El retrato de Dorian Gray', 'Oscar Wilde', '1890-07-01', 254, 'Clásicos', '978-84-206-0723-4', 4, true, 7, 'Novela sobre belleza y corrupción.'),
('Los juegos del hambre', 'Suzanne Collins', '2008-09-14', 384, 'Distopía', '978-0-439-02348-1', 4, true, 22, 'Competición mortal en arena televisada.'),
('El hobbit', 'J.R.R. Tolkien', '1937-09-21', 310, 'Fantasía', '978-0-618-00221-4', 5, true, 16, 'Aventura de Bilbo Bolsón.');

-- Verificar la inserción
SELECT COUNT(*) as total_libros FROM books;
SELECT * FROM books WHERE visible = true ORDER BY rating DESC, title;
