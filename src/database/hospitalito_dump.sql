-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hospitalito
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Current Database: `hospitalito`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `hospitalito` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hospitalito`;

--
-- Table structure for table `asignaciones`
--

DROP TABLE IF EXISTS `asignaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `id_cama` int NOT NULL,
  `id_enfermera` int NOT NULL,
  `fecha_asignacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_asignacion`),
  UNIQUE KEY `uk_cama_enfermera` (`id_cama`,`id_enfermera`),
  KEY `idx_enfermera` (`id_enfermera`),
  KEY `idx_cama` (`id_cama`),
  CONSTRAINT `asignaciones_ibfk_1` FOREIGN KEY (`id_cama`) REFERENCES `camas` (`id_cama`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `asignaciones_ibfk_2` FOREIGN KEY (`id_enfermera`) REFERENCES `personal` (`id_enfermera`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaciones`
--

LOCK TABLES `asignaciones` WRITE;
/*!40000 ALTER TABLE `asignaciones` DISABLE KEYS */;
INSERT INTO `asignaciones` VALUES (18,38,1,'2025-10-19 21:18:29'),(28,1,1,'2025-10-19 22:25:45'),(30,34,1,'2025-10-19 22:39:17'),(31,65,1,'2025-10-19 22:39:19'),(32,95,1,'2025-10-19 22:39:21'),(33,93,1,'2025-10-19 22:39:23');
/*!40000 ALTER TABLE `asignaciones` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `bi_asignacion_enfermera` BEFORE INSERT ON `asignaciones` FOR EACH ROW BEGIN
    DECLARE max_enfermeras INT; 
    DECLARE cant_camas_enfermera INT;
    DECLARE cant_enfermeras_actual INT;
    DECLARE enfermera_activa BOOLEAN;
    
    
    SELECT activo INTO enfermera_activa
    FROM personal
    WHERE id_enfermera = NEW.id_enfermera;
    
    IF NOT enfermera_activa THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: No se puede asignar camas a una enfermera inactiva.';
    END IF;
    
    
    SELECT COUNT(*) INTO cant_camas_enfermera 
    FROM asignaciones
    WHERE id_enfermera = NEW.id_enfermera;
    
    
    IF cant_camas_enfermera >= 8 THEN 
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: No se puede asignar m�s camas a esta enfermera. Superar�a el l�mite de 8 camas.';
    END IF;
    
    
    SELECT c.max_cant_enfermeras INTO max_enfermeras 
    FROM camas ca
    JOIN categorias c ON ca.id_categoria = c.id_categoria
    WHERE ca.id_cama = NEW.id_cama;
    
    
    SELECT COUNT(*) INTO cant_enfermeras_actual 
    FROM asignaciones
    WHERE id_cama = NEW.id_cama;
    
    
    IF cant_enfermeras_actual >= max_enfermeras THEN 
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: No se puede insertar porque se superar�a el l�mite de enfermeras para esta categor�a de cama.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `bu_asignacion_enfermera` BEFORE UPDATE ON `asignaciones` FOR EACH ROW BEGIN
    DECLARE max_enfermeras INT; 
    DECLARE cant_camas_enfermera INT;
    DECLARE cant_enfermeras_actual INT;
    DECLARE enfermera_activa BOOLEAN;
    
    
    IF NEW.id_enfermera != OLD.id_enfermera OR NEW.id_cama != OLD.id_cama THEN
        
        
        SELECT activo INTO enfermera_activa
        FROM personal
        WHERE id_enfermera = NEW.id_enfermera;
        
        IF NOT enfermera_activa THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: No se puede asignar camas a una enfermera inactiva.';
        END IF;
        
        
        SELECT COUNT(*) INTO cant_camas_enfermera 
        FROM asignaciones
        WHERE id_enfermera = NEW.id_enfermera
        AND id_asignacion != NEW.id_asignacion;
        
        
        IF cant_camas_enfermera >= 8 THEN 
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: La enfermera ya tiene 8 camas asignadas.';
        END IF;
        
        
        SELECT c.max_cant_enfermeras INTO max_enfermeras 
        FROM camas ca
        JOIN categorias c ON ca.id_categoria = c.id_categoria
        WHERE ca.id_cama = NEW.id_cama;
        
        
        SELECT COUNT(*) INTO cant_enfermeras_actual 
        FROM asignaciones
        WHERE id_cama = NEW.id_cama
        AND id_asignacion != NEW.id_asignacion;
        
        
        IF cant_enfermeras_actual >= max_enfermeras THEN 
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: La cama ya tiene el m�ximo de enfermeras permitidas.';
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `camas`
--

DROP TABLE IF EXISTS `camas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `camas` (
  `id_cama` int NOT NULL AUTO_INCREMENT,
  `id_categoria` int NOT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `disponible` tinyint(1) DEFAULT '1',
  `id_HC` int DEFAULT NULL,
  PRIMARY KEY (`id_cama`),
  KEY `id_HC` (`id_HC`),
  KEY `idx_disponible` (`disponible`),
  KEY `idx_categoria` (`id_categoria`),
  CONSTRAINT `camas_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `camas_ibfk_2` FOREIGN KEY (`id_HC`) REFERENCES `historiales_clinicos` (`id_HC`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `camas`
--

LOCK TABLES `camas` WRITE;
/*!40000 ALTER TABLE `camas` DISABLE KEYS */;
INSERT INTO `camas` VALUES (1,1,'Piso 1, Ala Norte, Habitación 101',1,NULL),(2,1,'Piso 1, Ala Norte, Habitación 102',1,NULL),(3,1,'Piso 1, Ala Norte, Habitación 103',1,NULL),(4,1,'Piso 1, Ala Norte, Habitación 104',1,NULL),(5,2,'Piso 2, Ala Norte, Habitación 201',1,NULL),(6,2,'Piso 2, Ala Norte, Habitación 202',1,NULL),(7,2,'Piso 2, Ala Norte, Habitación 203',1,NULL),(8,2,'Piso 2, Ala Norte, Habitación 204',1,NULL),(9,3,'Piso 3, Ala Este, Habitación 301',1,NULL),(10,3,'Piso 3, Ala Este, Habitación 302',1,NULL),(11,3,'Piso 3, Ala Este, Habitación 303',1,NULL),(12,3,'Piso 3, Ala Este, Habitación 304',1,NULL),(13,4,'Piso 4, Ala Sur, Habitación 401',1,NULL),(14,4,'Piso 4, Ala Sur, Habitación 402',1,NULL),(15,4,'Piso 4, Ala Sur, Habitación 403',1,NULL),(16,4,'Piso 4, Ala Sur, Habitación 404',1,NULL),(17,5,'Piso 5, Ala Oeste, Habitación 501',1,NULL),(18,5,'Piso 5, Ala Oeste, Habitación 502',1,NULL),(19,5,'Piso 5, Ala Oeste, Habitación 503',1,NULL),(20,5,'Piso 5, Ala Oeste, Habitación 504',1,NULL),(21,6,'Piso 6, Ala Norte, Habitación 601',1,NULL),(22,6,'Piso 6, Ala Norte, Habitación 602',1,NULL),(23,6,'Piso 6, Ala Norte, Habitación 603',1,NULL),(24,6,'Piso 6, Ala Norte, Habitación 604',1,NULL),(25,1,'Piso 1, Ala Sur, Habitación 105',1,NULL),(26,1,'Piso 1, Ala Sur, Habitación 106',1,NULL),(27,1,'Piso 1, Ala Sur, Habitación 107',1,NULL),(28,1,'Piso 1, Ala Sur, Habitación 108',1,NULL),(29,2,'Piso 2, Ala Sur, Habitación 205',1,NULL),(30,2,'Piso 2, Ala Sur, Habitación 206',1,NULL),(31,2,'Piso 2, Ala Sur, Habitación 207',1,NULL),(32,2,'Piso 2, Ala Sur, Habitación 208',1,NULL),(33,3,'Piso 3, Ala Oeste, Habitación 305',1,NULL),(34,3,'Piso 3, Ala Oeste, Habitación 306',1,NULL),(35,3,'Piso 3, Ala Oeste, Habitación 307',1,NULL),(36,3,'Piso 3, Ala Oeste, Habitación 308',1,NULL),(37,4,'Piso 4, Ala Este, Habitación 405',1,NULL),(38,4,'Piso 4, Ala Este, Habitación 406',1,NULL),(39,4,'Piso 4, Ala Este, Habitación 407',1,NULL),(40,4,'Piso 4, Ala Este, Habitación 408',1,NULL),(41,5,'Piso 5, Ala Este, Habitación 505',1,NULL),(42,5,'Piso 5, Ala Este, Habitación 506',1,NULL),(43,5,'Piso 5, Ala Este, Habitación 507',1,NULL),(44,5,'Piso 5, Ala Este, Habitación 508',1,NULL),(45,6,'Piso 6, Ala Sur, Habitación 605',1,NULL),(46,6,'Piso 6, Ala Sur, Habitación 606',1,NULL),(47,6,'Piso 6, Ala Sur, Habitación 607',1,NULL),(48,6,'Piso 6, Ala Sur, Habitación 608',1,NULL),(49,1,'Piso 1, Ala Norte, Habitación 109',1,NULL),(50,1,'Piso 1, Ala Norte, Habitación 110',1,NULL),(51,1,'Piso 1, Ala Norte, Habitación 111',1,NULL),(52,1,'Piso 1, Ala Norte, Habitación 112',1,NULL),(53,2,'Piso 2, Ala Norte, Habitación 209',1,NULL),(54,2,'Piso 2, Ala Norte, Habitación 210',1,NULL),(55,2,'Piso 2, Ala Norte, Habitación 211',1,NULL),(56,2,'Piso 2, Ala Norte, Habitación 212',1,NULL),(57,3,'Piso 3, Ala Este, Habitación 309',1,NULL),(58,3,'Piso 3, Ala Este, Habitación 310',1,NULL),(59,3,'Piso 3, Ala Este, Habitación 311',1,NULL),(60,3,'Piso 3, Ala Este, Habitación 312',1,NULL),(61,4,'Piso 4, Ala Sur, Habitación 409',1,NULL),(62,4,'Piso 4, Ala Sur, Habitación 410',1,NULL),(63,4,'Piso 4, Ala Sur, Habitación 411',1,NULL),(64,4,'Piso 4, Ala Sur, Habitación 412',1,NULL),(65,5,'Piso 5, Ala Oeste, Habitación 509',1,NULL),(66,5,'Piso 5, Ala Oeste, Habitación 510',1,NULL),(67,5,'Piso 5, Ala Oeste, Habitación 511',1,NULL),(68,5,'Piso 5, Ala Oeste, Habitación 512',1,NULL),(69,6,'Piso 6, Ala Norte, Habitación 613',1,NULL),(70,6,'Piso 6, Ala Norte, Habitación 614',1,NULL),(71,6,'Piso 6, Ala Norte, Habitación 615',1,NULL),(72,6,'Piso 6, Ala Norte, Habitación 616',1,NULL),(73,1,'Piso 1, Ala Sur, Habitación 113',1,NULL),(74,1,'Piso 1, Ala Sur, Habitación 114',1,NULL),(75,1,'Piso 1, Ala Sur, Habitación 115',1,NULL),(76,1,'Piso 1, Ala Sur, Habitación 116',1,NULL),(77,2,'Piso 2, Ala Sur, Habitación 213',1,NULL),(78,2,'Piso 2, Ala Sur, Habitación 214',1,NULL),(79,2,'Piso 2, Ala Sur, Habitación 215',1,NULL),(80,2,'Piso 2, Ala Sur, Habitación 216',1,NULL),(81,3,'Piso 3, Ala Oeste, Habitación 313',1,NULL),(82,3,'Piso 3, Ala Oeste, Habitación 314',1,NULL),(83,3,'Piso 3, Ala Oeste, Habitación 315',1,NULL),(84,3,'Piso 3, Ala Oeste, Habitación 316',1,NULL),(85,4,'Piso 4, Ala Este, Habitación 413',1,NULL),(86,4,'Piso 4, Ala Este, Habitación 414',1,NULL),(87,4,'Piso 4, Ala Este, Habitación 415',1,NULL),(88,4,'Piso 4, Ala Este, Habitación 416',1,NULL),(89,5,'Piso 5, Ala Este, Habitación 513',1,NULL),(90,5,'Piso 5, Ala Este, Habitación 514',1,NULL),(91,5,'Piso 5, Ala Este, Habitación 515',1,NULL),(92,5,'Piso 5, Ala Este, Habitación 516',1,NULL),(93,6,'Piso 6, Ala Sur, Habitación 617',1,NULL),(94,6,'Piso 6, Ala Sur, Habitación 618',1,NULL),(95,6,'Piso 6, Ala Sur, Habitación 619',1,NULL),(96,6,'Piso 6, Ala Sur, Habitación 620',1,NULL);
/*!40000 ALTER TABLE `camas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(10) DEFAULT NULL,
  `descripcion` text,
  `max_cant_enfermeras` int DEFAULT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `idx_codigo` (`codigo`),
  CONSTRAINT `categorias_chk_1` CHECK ((`max_cant_enfermeras` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'guardia','De guardia',1),(2,'comun','Sala común',2),(3,'interm','Cuidado intermedio',3),(4,'intens','Cuidado intensivo',4),(5,'pre-cir','Pre cirugía de baja complejidad',2),(6,'post-cir','Post cirugía de baja complejidad',2);
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historiales_clinicos`
--

DROP TABLE IF EXISTS `historiales_clinicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historiales_clinicos` (
  `id_HC` int NOT NULL AUTO_INCREMENT,
  `nombre_paciente` varchar(255) NOT NULL,
  `apellido_paciente` varchar(255) NOT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `fecha_ingreso` datetime DEFAULT CURRENT_TIMESTAMP,
  `diagnostico` text,
  `observaciones` text,
  PRIMARY KEY (`id_HC`),
  UNIQUE KEY `dni` (`dni`),
  KEY `idx_dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historiales_clinicos`
--

LOCK TABLES `historiales_clinicos` WRITE;
/*!40000 ALTER TABLE `historiales_clinicos` DISABLE KEYS */;
INSERT INTO `historiales_clinicos` VALUES (1,'Juan','Pérez','30111222','2025-10-11 22:31:50','Gripe',''),(2,'María','Gómez','28456987','2025-10-11 22:31:50','Apendicitis','Post cirugía'),(3,'Carlos','Ruiz','32145987','2025-10-11 22:31:50','Neumonía','Requiere oxígeno'),(4,'Lucía','Fernández','34122876','2025-10-11 22:31:50','Fractura',''),(5,'Sofía','Martínez','29876123','2025-10-11 22:31:50','Covid-19','Monitoreo respiratorio'),(6,'Diego','López','30999888','2025-10-11 22:31:50','Hernia','Post cirugía'),(7,'Laura','Benítez','27567845','2025-10-11 22:31:50','Gastroenteritis','Hidratación'),(8,'Ricardo','Mendoza','33222111','2025-10-11 22:31:50','Insuficiencia cardíaca','Observación'),(9,'Patricia','Vega','29887766','2025-10-11 22:31:50','Migraña',''),(10,'Fernando','Castro','31122334','2025-10-11 22:31:50','Otitis','Tratamiento antibiótico'),(11,'Verónica','Alvarez','31223344','2025-10-11 22:31:50','Hipertensión','Monitoreo presión'),(12,'Julio','Morales','32789911','2025-10-11 22:31:50','Diabetes','Control glucemia'),(13,'Carolina','Ortega','30123455','2025-10-11 22:31:50','Asma','Inhalador diario'),(14,'Santiago','Rojas','33221144','2025-10-11 22:31:50','Bronquitis','Antibiótico oral'),(15,'Paula','Ibarra','32987654','2025-10-11 22:31:50','Anemia','Suplementos hierro'),(16,'Daniel','Suárez','30344556','2025-10-11 22:31:50','Fractura muñeca','Yeso 6 semanas'),(17,'Mariana','Torres','31765432','2025-10-11 22:31:50','Celulitis','Antibiótico tópico'),(18,'Andrés','Domínguez','32111222','2025-10-11 22:31:50','Faringitis','Reposo y antibiótico'),(19,'Gabriela','Ramírez','30988765','2025-10-11 22:31:50','Otitis externa','Gotas antibióticas'),(20,'Hugo','Santos','29876543','2025-10-11 22:31:50','Gastritis','Dieta blanda'),(21,'Elena','Molina','30445566','2025-10-11 22:31:50','Hipotiroidismo','Tratamiento hormonal'),(22,'Javier','Acosta','31233445','2025-10-11 22:31:50','Lumbalgia','Fisioterapia'),(23,'Silvia','Castillo','29887744','2025-10-11 22:31:50','Cistitis','Antibiótico oral'),(24,'Tomás','Vargas','32556677','2025-10-11 22:31:50','Neumonía leve','Reposo y medicación'),(25,'Claudia','Romero','30887766','2025-10-11 22:31:50','Diabetes tipo 2','Control dieta'),(26,'Ricardo','Figueroa','31112233','2025-10-11 22:31:50','Hipertensión','Monitoreo presión'),(27,'Natalia','Herrera','32223344','2025-10-11 22:31:50','Fractura tibia','Yeso 8 semanas'),(28,'Martín','García','33334455','2025-10-11 22:31:50','Alergia','Antihistamínico diario'),(29,'Luciano','Paredes','29889900','2025-10-11 22:31:50','Dolor lumbar','Fisioterapia'),(30,'Carla','Luna','30765432','2025-10-11 22:31:50','Migraña crónica','Control dolor'),(31,'Fabián','Ramos','32145566','2025-10-11 22:31:50','Asma leve','Inhalador según necesidad'),(32,'Mariela','Navarro','29887711','2025-10-11 22:31:50','Insuficiencia renal','Monitoreo laboratorio'),(33,'Rodrigo','Medina','31001122','2025-10-11 22:31:50','Fractura brazo','Yeso 4 semanas'),(34,'Lorena','Vega','32558877','2025-10-11 22:31:50','Gastroenteritis','Dieta blanda'),(35,'Esteban','Cabrera','31224455','2025-10-11 22:31:50','Bronquitis','Antibiótico oral'),(36,'Marcela','Ortiz','30998811','2025-10-11 22:31:50','Hipotiroidismo','Tratamiento hormonal'),(37,'Ricardo','Paz','31112244','2025-10-11 22:31:50','Apendicitis','Post cirugía'),(38,'Verónica','Salas','32887766','2025-10-11 22:31:50','Gripe','Reposo y medicación'),(39,'Ignacio','Silva','32221122','2025-10-11 22:31:50','Covid-19','Monitoreo respiratorio'),(40,'Daniela','Figueroa','31002233','2025-10-11 22:31:50','Fractura costilla','Reposo absoluto'),(41,'Leonardo','Torres','32334455','2025-10-11 22:31:50','Insuficiencia cardíaca','Observación'),(42,'Mónica','Ríos','30446677','2025-10-11 22:31:50','Otitis','Gotas antibióticas'),(43,'Sebastián','Méndez','32557788','2025-10-11 22:31:50','Diabetes tipo 1','Control glucemia'),(44,'Camila','Córdoba','31225566','2025-10-11 22:31:50','Migraña','Control de dolor'),(45,'Fernando','Díaz','30113344','2025-10-11 22:31:50','Alergia','Antihistamínico'),(46,'Lorena','Suárez','32335544','2025-10-11 22:31:50','Gastritis','Dieta blanda'),(47,'Ricardo','Pacheco','32446655','2025-10-11 22:31:50','Hipertensión','Monitoreo presión'),(48,'Natalia','Alarcón','32557766','2025-10-11 22:31:50','Asma','Inhalador diario'),(49,'Diego','Blanco','32668877','2025-10-11 22:31:50','Fractura pierna','Reposo absoluto'),(50,'Sofía','Ferrer','32779988','2025-10-11 22:31:50','Cistitis','Antibiótico oral');
/*!40000 ALTER TABLE `historiales_clinicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal`
--

DROP TABLE IF EXISTS `personal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal` (
  `id_enfermera` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_enfermera`),
  UNIQUE KEY `dni` (`dni`),
  KEY `idx_dni_personal` (`dni`),
  KEY `idx_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal`
--

LOCK TABLES `personal` WRITE;
/*!40000 ALTER TABLE `personal` DISABLE KEYS */;
INSERT INTO `personal` VALUES (1,'Ana','Rodríguez','25456787','2262-455321',1),(2,'Marta','Sosa','30123456','2262-512458',1),(3,'Carla','Luna','28765432','2262-477321',1),(4,'Nadia','Quiroga','31546789','2262-499632',1),(5,'Paula','Ibarra','32987654','2262-478965',1),(6,'Rosa','Acosta','29456123','2262-476854',1),(7,'Julia','Ortega','30789412','2262-489541',1),(8,'Verónica','Alvarez','31223344','2262-412365',1),(9,'Claudia','Romero','29887744','2262-457812',1),(10,'Gabriela','Ramírez','30988765','2262-478965',1),(11,'Lorena','Navarro','32558877','2262-489632',1),(12,'Marcela','Ortiz','30998811','2262-412457',1),(13,'Daniela','Figueroa','31002233','2262-456987',1),(14,'Mónica','Ríos','30446677','2262-498632',1),(15,'Camila','Córdoba','31225566','2262-412598',1),(16,'Natalia','Alarcón','32557766','2262-475632',1),(17,'Sofía','Ferrer','32779988','2262-498745',1),(18,'Leonor','Mendoza','32881122','2262-478521',1),(19,'Fernando','Díaz','30113344','2262-412365',1),(20,'Sebastián','Méndez','32557788','2262-489632',1);
/*!40000 ALTER TABLE `personal` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `au_personal_activo` AFTER UPDATE ON `personal` FOR EACH ROW BEGIN
    IF OLD.activo = 1 AND NEW.activo = 0 THEN
        DELETE FROM asignaciones WHERE id_enfermera = NEW.id_enfermera;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'hospitalito'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-22  4:38:00
