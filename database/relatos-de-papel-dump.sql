mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: books_catalogue_db
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `books_catalogue_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `books_catalogue_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `books_catalogue_db`;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `isbn` varchar(255) NOT NULL,
  `published_date` date DEFAULT NULL,
  `rating` int NOT NULL,
  `stock` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `visible` bit(1) NOT NULL,
  `description` text,
  `pages` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkibbepcitr0a3cpk3rfr7nihn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Miguel de Cervantes','Clásicos','978-84-376-0494-7','1605-01-16',5,10,'Don Quijote de la Mancha',_binary '','Clásico de la literatura española.',0),(2,'Gabriel García Márquez','Realismo mágico','978-03-075-4726-7','1967-05-30',5,15,'Cien años de soledad',_binary '','Saga familiar en Macondo.',0),(3,'Autor Actualizado Gateway','Test Gateway Updated','978-4-204-83869-1',NULL,5,20,'Don Quijote de la Mancha - EDITADO',_binary '','Descripción actualizada desde Gateway',351),(4,'Antoine de Saint-Exupéry','Infantil','978-84-204-3869-1','1943-04-06',4,30,'El principito',_binary '','Fábula poética para todas las edades.',0),(5,'Julio Cortázar','Experimental','978-84-204-0145-9','1963-06-28',4,8,'Rayuela',_binary '','Novela experimental y fragmentaria.',0),(6,'Carlos Ruiz Zafón','Misterio','978-84-08-04696-6','2001-04-17',5,12,'La sombra del viento',_binary '','Novela ambientada en la Barcelona gótica.',0),(7,'Gabriel García Márquez','Romance','978-03-075-3889-0','1985-12-05',5,7,'El amor en los tiempos del cólera',_binary '','Historia de amor y paciencia.',0),(8,'Ray Bradbury','Distopía','978-0-345-34296-2','1953-10-19',4,18,'Fahrenheit 451',_binary '','Futuro donde queman libros.',0),(9,'Louisa May Alcott','Clásicos','978-84-204-7146-9','1868-09-30',4,5,'Mujercitas',_binary '','Historias de hermanas y crecimiento.',0),(10,'Dan Brown','Thriller','978-0-307-47492-1','2003-03-18',3,25,'El código Da Vinci',_binary '','Thriller de misterio y símbolos.',0),(11,'J.K. Rowling','Fantasía','978-84-9838-912-4','1997-06-26',5,50,'Harry Potter y la piedra filosofal',_binary '','Inicio de la saga de Harry Potter.',0),(12,'Jane Austen','Romance','978-84-204-6761-5','1813-01-28',5,9,'Orgullo y prejuicio',_binary '','Comedia romántica clásica.',0),(13,'Fiódor Dostoyevski','Clásicos','978-84-206-0649-7','1866-01-01',5,6,'Crimen y castigo',_binary '','Reflexión sobre crimen y moral.',0),(14,'J.R.R. Tolkien','Fantasía','978-0-618-00222-1','1954-07-29',5,14,'El señor de los anillos',_binary '','Épica de fantasía en la Tierra Media.',0),(15,'Homero','Épica','978-84-206-4163-3','0800-01-01',5,0,'La Odisea',_binary '','Viaje épico de Odiseo.',0),(16,'Autor Secreto','Secreto','978-0-000-00000-0','2020-01-01',1,0,'Libro oculto de prueba',_binary '\0','Ejemplo de libro no visible.',0),(17,'Gabriel García Márquez','Realismo mágico','978-84-397-0001-0','1981-05-01',5,11,'Crónica de una muerte anunciada',_binary '','Crónica novelada de un asesinato.',0),(18,'Oscar Wilde','Clásicos','978-84-206-0723-4','1890-07-01',4,7,'El retrato de Dorian Gray',_binary '','Novela sobre belleza y corrupción.',0),(19,'Suzanne Collins','Distopía','978-0-439-02348-1','2008-09-14',4,22,'Los juegos del hambre',_binary '','Competición mortal en arena televisada.',0),(20,'J.R.R. Tolkien','Fantasía','978-0-618-00221-4','1937-09-21',5,16,'El hobbit',_binary '','Aventura de Bilbo Bolsón.',0),(23,'Autor Edit','Test category','999-9-999-99999-9',NULL,5,8,'testPost - edit',_binary '\0',NULL,210),(24,'Autor Gateway Test create','Test createGateway','979-9-079-47299-9',NULL,5,15,'Don Quijote de la Mancha',_binary '','Libro de prueba desde API Gateway',250),(25,'Autor Gateway Test create en vivo','Test createGateway','979-9-079-47299-8',NULL,5,15,'Don Quijote de la Mancha',_binary '\0','Libro de prueba desde API Gateway',351);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `payments_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `payments_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `payments_db`;

--
-- Table structure for table `purchase_items`
--

DROP TABLE IF EXISTS `purchase_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `purchase_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhcski0jcuja0o3vhb7o15yqvi` (`purchase_id`),
  CONSTRAINT `FKhcski0jcuja0o3vhb7o15yqvi` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_items`
--

LOCK TABLES `purchase_items` WRITE;
/*!40000 ALTER TABLE `purchase_items` DISABLE KEYS */;
INSERT INTO `purchase_items` VALUES (1,1,1,1),(2,1,1,2),(3,3,1,3),(4,3,1,4),(5,3,1,5),(6,3,1,6),(7,3,1,7);
/*!40000 ALTER TABLE `purchase_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `address` varchar(500) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (1,'2026-02-05 05:46:39.615725','Calle Principal 123','juan@example.com','Juan Pérez','COMPLETED'),(2,'2026-02-05 05:46:47.091268','Calle Principal 123','juan@example.com','Juan Pérez','COMPLETED'),(3,'2026-02-05 20:03:03.722697','Calle Gateway 123','test@pyment1.com','testPayment 1','COMPLETED'),(4,'2026-02-05 20:03:06.140057','Calle Gateway 123','test@pyment1.com','testPayment 1','COMPLETED'),(5,'2026-02-05 20:03:07.730180','Calle Gateway 123','test@pyment1.com','testPayment 1','COMPLETED'),(6,'2026-02-05 20:03:09.707550','Calle Gateway 123','test@pyment1.com','testPayment 1','COMPLETED'),(7,'2026-02-05 20:03:13.684911','Calle Gateway 123','test@pyment1.com','testPayment 1','COMPLETED');
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-06 15:27:22
