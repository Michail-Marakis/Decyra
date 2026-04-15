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
-- Table structure for table `universities`
--

DROP TABLE IF EXISTS `universities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `universities` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ranking` int DEFAULT NULL,
  `website_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `universities`
--

LOCK TABLES `universities` WRITE;
/*!40000 ALTER TABLE `universities` DISABLE KEYS */;
INSERT INTO `universities` VALUES (1,'Εθνικό και Καποδιστριακό Πανεπιστήμιο Αθηνών','Athens','Greece',301,'https://www.di.uoa.gr'),(2,'Οικονομικό Πανεπιστήμιο Αθηνών','Athens','Greece',401,'https://www.aueb.gr'),(3,'Αριστοτέλειο Πανεπιστήμιο Θεσσαλονίκης','Thessaloniki','Greece',401,'https://www.csd.auth.gr'),(4,'Τεχνικό Πανεπιστήμιο Μονάχου (TUM)','Munich','Germany',50,'https://www.in.tum.de'),(5,'Τεχνολογικό Πανεπιστήμιο του Ντελφτ (TU Delft)','Delft','Netherlands',60,'https://www.tudelft.nl'),(6,'Πανεπιστήμιο της Στουτγκάρδης','Stuttgart','Germany',251,'https://www.uni-stuttgart.de'),(7,'Καθολικό Πανεπιστήμιο της Λουβαίν (KU Leuven)','Leuven','Belgium',80,'https://www.kuleuven.be'),(8,'Πανεπιστήμιο της Σορβόννης','Paris','France',100,'https://www.sorbonne-universite.fr'),(9,'École Polytechnique (Γαλλία)','Palaiseau','France',40,'https://www.polytechnique.edu'),(10,'Πολυτεχνείο του Μιλάνου (Politecnico di Milano)','Milan','Italy',151,'https://www.polimi.it'),(11,'Πανεπιστήμιο Σαπιέντσα της Ρώμης','Rome','Italy',120,'https://www.uniroma1.it'),(12,'Βασιλικό Ινστιτούτο Τεχνολογίας KTH','Stockholm','Sweden',70,'https://www.kth.se'),(13,'Πανεπιστήμιο της Λουντ','Lund','Sweden',130,'https://www.lunduniversity.lu.se'),(14,'Πανεπιστήμιο του Ελσίνκι','Helsinki','Finland',100,'https://www.helsinki.fi'),(15,'Πανεπιστήμιο Aalto','Espoo','Finland',150,'https://www.aalto.fi'),(16,'Πανεπιστήμιο της Βιέννης','Vienna','Austria',150,'https://www.univie.ac.at'),(17,'University of Cyprus','Nicosia','Cyprus',180,'https://www.ucy.ac.cy'),(18,'University of Nicosia','Nicosia','Cyprus',500,'https://www.unic.ac.cy'),(19,'Πανεπιστήμιο του Άμστερνταμ','Amsterdam','Netherlands',80,'https://www.uva.nl'),(20,'Πανεπιστήμιο RWTH του Άαχεν','Aachen','Germany',90,'https://www.rwth-aachen.de'),(21,'Πολυτεχνικό Πανεπιστήμιο της Μαδρίτης','Madrid','Spain',120,'https://www.upm.es'),(22,'Πανεπιστήμιο της Βαρκελώνης','Barcelona','Spain',110,'https://www.ub.edu'),(23,'Πανεπιστήμιο του Όσλο','Oslo','Norway',130,'https://www.uio.no'),(24,'Νορβηγικό Πανεπιστήμιο Επιστημών και Τεχνολογίας (NTNU)','Trondheim','Norway',180,'https://www.ntnu.edu'),(25,'Πανεπιστήμιο του Μπέργκεν','Bergen','Norway',200,'https://www.uib.no'),(26,'Πανεπιστήμιο του Τούρκου','Turku','Finland',250,'https://www.utu.fi'),(27,'Πανεπιστήμιο της Μπολόνια','Bologna','Italy',140,'https://www.unibo.it'),(28,'Πανεπιστήμιο της Βαρσοβίας','Warsaw','Poland',300,'https://www.mimuw.edu.pl'),(29,'Πολυτεχνείο της Βαρσοβίας','Warsaw','Poland',400,'https://www.mini.pw.edu.pl'),(30,'Πανεπιστήμιο των Γιαγκελλόνων','Kraków','Poland',350,'https://www.fais.uj.edu.pl'),(31,'Πανεπιστήμιο του Βουκουρεστίου','Bucharest','Romania',500,'https://www.fmi.unibuc.ro'),(32,'Πολυτεχνικό Πανεπιστήμιο του Βουκουρεστίου','Bucharest','Romania',400,'https://acs.pub.ro'),(33,'Πανεπιστήμιο Μπάμπες-Μπολιάι','Cluj-Napoca','Romania',600,'https://www.cs.ubbcluj.ro'),(34,'Εθνικό Πανεπιστήμιο του Κιέβου Τάρας Σεβτσένκο','Kyiv','Ukraine',450,'https://csc.knu.ua'),(35,'Πολυτεχνικό Ινστιτούτο του Κιέβου (Igor Sikorsky KPI)','Kyiv','Ukraine',500,'https://fiot.kpi.ua'),(36,'Εθνικό Πολυτεχνείο του Λβιβ','Lviv','Ukraine',600,'https://lpnu.ua/en/ikni'),(37,'Πανεπιστήμιο της Οξφόρδης','Oxford','United Kingdom',5,'https://www.cs.ox.ac.uk'),(38,'Πανεπιστήμιο του Κέιμπριτζ','Cambridge','United Kingdom',6,'https://www.cst.cam.ac.uk'),(39,'Imperial College Λονδίνου','London','United Kingdom',10,'https://www.imperial.ac.uk/computing'),(40,'Κρατικό Πανεπιστήμιο της Μόσχας Λομονόσοφ','Moscow','Russia',80,'https://cs.msu.ru'),(41,'Κρατικό Πανεπιστήμιο της Αγίας Πετρούπολης','St. Petersburg','Russia',200,'https://math.spbu.ru'),(42,'Ινστιτούτο Φυσικής και Τεχνολογίας της Μόσχας (MIPT)','Moscow','Russia',150,'https://mipt.ru'),(43,'Τεχνολογικό Πανεπιστήμιο του Αϊντχόφεν (TU/e)','Eindhoven','Netherlands',100,'https://www.tue.nl'),(44,'Πανεπιστήμιο της Γάνδης','Ghent','Belgium',120,'https://www.ugent.be'),(45,'Καθολικό Πανεπιστήμιο της Λουβαίν (UCLouvain)','Louvain-la-Neuve','Belgium',150,'https://uclouvain.be'),(46,'Πανεπιστήμιο της Ουψάλα','Uppsala','Sweden',250,'https://www.it.uu.se'),(47,'Πανεπιστήμιο Paris-Saclay','Gif-sur-Yvette','France',50,'https://www.universite-paris-saclay.fr'),(48,'Αυτόνομο Πανεπιστήμιο της Μαδρίτης','Madrid','Spain',150,'https://www.uam.es'),(49,'Πανεπιστήμιο Κρήτης','Heraklion','Greece',350,'https://www.csd.uoc.gr'),(50,'Πανεπιστήμιο Θεσσαλίας','Volos','Greece',700,'https://www.e-ce.uth.gr'),(51,'Χαροκόπειο Πανεπιστήμιο','Athens','Greece',600,'https://www.dit.hua.gr'),(52,'Πανεπιστήμιο Πελοποννήσου','Tripoli','Greece',800,'https://dit.uop.gr'),(53,'Ιόνιο Πανεπιστήμιο','Corfu','Greece',1000,'https://di.ionio.gr'),(54,'Ομοσπονδιακό Ινστιτούτο Τεχνολογίας της Ζυρίχης (ETH Zurich)','Zurich','Switzerland',10,'https://inf.ethz.ch'),(55,'Ομοσπονδιακή Πολυτεχνική Σχολή της Λωζάνης (EPFL)','Lausanne','Switzerland',15,'https://www.epfl.ch'),(56,'Copenhagen Business School','Copenhagen','Denmark',100,'https://www.cbs.dk'),(57,'Τεχνικό Πανεπιστήμιο της Δανίας (DTU)','Kongens Lyngby','Denmark',120,'https://www.dtu.dk'),(58,'Πανεπιστήμιο του Άαρχους','Aarhus','Denmark',150,'https://www.au.dk'),(59,'Πανεπιστήμιο Ruhr του Μπόχουμ','Bochum','Germany',180,'https://www.rub.de'),(60,'Πανεπιστήμιο του Όσναμπρυκ','Osnabrück','Germany',200,'https://www.uni-osnabrueck.de'),(61,'Πανεπιστήμιο του Σάαρλαντ','Saarbrücken','Germany',120,'https://www.uni-saarland.de'),(62,'Πανεπιστήμιο Γιοχάνες Κέπλερ του Λιντς','Linz','Austria',250,'https://www.jku.at'),(63,'Ιατρικό Πανεπιστήμιο της Βιέννης','Vienna','Austria',300,'https://www.meduniwien.ac.at'),(64,'Ανώτερο Τεχνικό Ινστιτούτο της Λισαβόνας (IST)','Lisbon','Portugal',150,'https://tecnico.ulisboa.pt'),(65,'Πανεπιστήμιο του Πόρτο','Porto','Portugal',200,'https://sigarra.up.pt/feup'),(66,'Πανεπιστήμιο του Καρόλου','Prague','Czech Republic',200,'https://www.mff.cuni.cz'),(67,'Τσεχικό Τεχνικό Πανεπιστήμιο της Πράγας','Prague','Czech Republic',250,'https://fit.cvut.cz'),(68,'Πανεπιστήμιο του Εδιμβούργου','Edinburgh','United Kingdom',20,'https://www.ed.ac.uk/informatics'),(69,'Πανεπιστημιακό Κολέγιο Λονδίνου (UCL)','London','United Kingdom',18,'https://www.ucl.ac.uk/computer-science'),(70,'LMU Μονάχου (Πανεπιστήμιο Λουδοβίκου-Μαξιμιλιανού)','Munich','Germany',60,'https://www.ifi.lmu.de'),(71,'Τεχνικό Πανεπιστήμιο του Ντάρμσταντ (TU Darmstadt)','Darmstadt','Germany',100,'https://www.tu-darmstadt.de'),(72,'Πανεπιστήμιο Πειραιώς','Piraeus','Greece',600,'https://www.unipi.gr');
/*!40000 ALTER TABLE `universities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-15 18:28:12
