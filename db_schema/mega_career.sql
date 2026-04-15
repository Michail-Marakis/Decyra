-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mega
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `career`
--

DROP TABLE IF EXISTS `career`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `career` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `field_name` varchar(100) DEFAULT NULL,
  `avg_salary_no_master` int unsigned DEFAULT NULL,
  `avg_salary_with_master` int unsigned DEFAULT NULL,
  `country_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `career`
--

LOCK TABLES `career` WRITE;
/*!40000 ALTER TABLE `career` DISABLE KEYS */;
INSERT INTO `career` VALUES (1,'Software Engineering',18000,24000,'Ελλάδα'),(2,'Web Development',17000,23000,'Ελλάδα'),(3,'Mobile Development',16000,22000,'Ελλάδα'),(4,'Data Science',20000,26000,'Ελλάδα'),(5,'Artificial Intelligence',22000,28000,'Ελλάδα'),(6,'Cybersecurity',19000,25000,'Ελλάδα'),(7,'Game Development',17000,23000,'Ελλάδα'),(8,'Software Engineering',50000,60000,'Γερμανία'),(9,'Web Development',48000,58000,'Γερμανία'),(10,'Mobile Development',47000,57000,'Γερμανία'),(11,'Data Science',55000,70000,'Γερμανία'),(12,'Artificial Intelligence',60000,75000,'Γερμανία'),(13,'Cybersecurity',52000,65000,'Γερμανία'),(14,'Game Development',48000,60000,'Γερμανία'),(15,'Software Engineering',52000,62000,'Ολλανδία'),(16,'Web Development',50000,60000,'Ολλανδία'),(17,'Mobile Development',48000,58000,'Ολλανδία'),(18,'Data Science',60000,75000,'Ολλανδία'),(19,'Artificial Intelligence',65000,80000,'Ολλανδία'),(20,'Cybersecurity',55000,70000,'Ολλανδία'),(21,'Game Development',50000,62000,'Ολλανδία'),(22,'Software Engineering',48000,60000,'Ηνωμένο Βασίλειο'),(23,'Web Development',46000,58000,'Ηνωμένο Βασίλειο'),(24,'Mobile Development',45000,57000,'Ηνωμένο Βασίλειο'),(25,'Data Science',55000,70000,'Ηνωμένο Βασίλειο'),(26,'Artificial Intelligence',60000,75000,'Ηνωμένο Βασίλειο'),(27,'Cybersecurity',50000,65000,'Ηνωμένο Βασίλειο'),(28,'Game Development',47000,60000,'Ηνωμένο Βασίλειο'),(29,'Software Engineering',42000,52000,'Γαλλία'),(30,'Web Development',40000,50000,'Γαλλία'),(31,'Mobile Development',39000,49000,'Γαλλία'),(32,'Data Science',48000,60000,'Γαλλία'),(33,'Artificial Intelligence',52000,65000,'Γαλλία'),(34,'Cybersecurity',45000,56000,'Γαλλία'),(35,'Game Development',40000,50000,'Γαλλία'),(36,'Software Engineering',30000,38000,'Ιταλία'),(37,'Web Development',28000,36000,'Ιταλία'),(38,'Mobile Development',27000,35000,'Ιταλία'),(39,'Data Science',34000,42000,'Ιταλία'),(40,'Artificial Intelligence',36000,45000,'Ιταλία'),(41,'Cybersecurity',32000,40000,'Ιταλία'),(42,'Game Development',28000,36000,'Ιταλία'),(43,'Software Engineering',32000,40000,'Ισπανία'),(44,'Web Development',30000,38000,'Ισπανία'),(45,'Mobile Development',29000,37000,'Ισπανία'),(46,'Data Science',36000,45000,'Ισπανία'),(47,'Artificial Intelligence',38000,47000,'Ισπανία'),(48,'Cybersecurity',34000,42000,'Ισπανία'),(49,'Game Development',30000,38000,'Ισπανία'),(50,'Software Engineering',25000,32000,'Πολωνία'),(51,'Web Development',23000,30000,'Πολωνία'),(52,'Mobile Development',22000,29000,'Πολωνία'),(53,'Data Science',28000,35000,'Πολωνία'),(54,'Artificial Intelligence',30000,38000,'Πολωνία'),(55,'Cybersecurity',26000,33000,'Πολωνία'),(56,'Game Development',23000,30000,'Πολωνία'),(57,'Software Engineering',20000,26000,'Ρουμανία'),(58,'Web Development',19000,25000,'Ρουμανία'),(59,'Mobile Development',18000,24000,'Ρουμανία'),(60,'Data Science',24000,30000,'Ρουμανία'),(61,'Artificial Intelligence',26000,32000,'Ρουμανία'),(62,'Cybersecurity',22000,28000,'Ρουμανία'),(63,'Game Development',19000,25000,'Ρουμανία'),(64,'Software Engineering',22000,28000,'Ρωσία'),(65,'Web Development',21000,27000,'Ρωσία'),(66,'Mobile Development',20000,26000,'Ρωσία'),(67,'Data Science',26000,32000,'Ρωσία'),(68,'Artificial Intelligence',28000,35000,'Ρωσία'),(69,'Cybersecurity',24000,30000,'Ρωσία'),(70,'Game Development',21000,27000,'Ρωσία'),(71,'Software Engineering',18000,24000,'Ουκρανία'),(72,'Web Development',17000,23000,'Ουκρανία'),(73,'Mobile Development',16000,22000,'Ουκρανία'),(74,'Data Science',22000,28000,'Ουκρανία'),(75,'Artificial Intelligence',24000,30000,'Ουκρανία'),(76,'Cybersecurity',20000,26000,'Ουκρανία'),(77,'Game Development',17000,23000,'Ουκρανία'),(78,'Software Engineering',48000,60000,'Σουηδία'),(79,'Web Development',46000,58000,'Σουηδία'),(80,'Mobile Development',45000,57000,'Σουηδία'),(81,'Data Science',55000,70000,'Σουηδία'),(82,'Artificial Intelligence',60000,75000,'Σουηδία'),(83,'Cybersecurity',50000,65000,'Σουηδία'),(84,'Game Development',47000,60000,'Σουηδία'),(85,'Software Engineering',55000,70000,'Νορβηγία'),(86,'Web Development',53000,68000,'Νορβηγία'),(87,'Mobile Development',52000,67000,'Νορβηγία'),(88,'Data Science',62000,80000,'Νορβηγία'),(89,'Artificial Intelligence',68000,85000,'Νορβηγία'),(90,'Cybersecurity',58000,75000,'Νορβηγία'),(91,'Game Development',53000,68000,'Νορβηγία'),(92,'Software Engineering',45000,55000,'Φινλανδία'),(93,'Web Development',43000,53000,'Φινλανδία'),(94,'Mobile Development',42000,52000,'Φινλανδία'),(95,'Data Science',50000,62000,'Φινλανδία'),(96,'Artificial Intelligence',55000,68000,'Φινλανδία'),(97,'Cybersecurity',47000,58000,'Φινλανδία'),(98,'Game Development',43000,53000,'Φινλανδία');
/*!40000 ALTER TABLE `career` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-15 23:14:54
