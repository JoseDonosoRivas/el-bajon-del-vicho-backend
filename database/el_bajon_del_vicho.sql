-- MySQL dump 10.13  Distrib 8.0.37, for Win64 (x86_64)
--
-- Host: localhost    Database: el_bajon_del_vicho
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'hamburguesas','Hamburguesas'),(2,'acompanamientos','Acompañamientos'),(3,'bebidas','Bebidas');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `imagen` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `stock` tinyint(1) NOT NULL DEFAULT '1',
  `categoria_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_producto_categoria` (`categoria_id`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`),
  CONSTRAINT `chk_producto_precio` CHECK ((`precio` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES ('prod-1','COMBO CLASICO','La opción tradicional que nunca falla. Hamburguesa de carne de res con queso cheddar, lechuga fresca, tomate, cebolla morada y aderezos clásicos. Incluye porción de papas fritas y una lata de Coca-Cola.',8990.00,'/assets/combo_clasico.webp',1,1),('prod-10','Papas Fritas Vichonas','Papas fritas de corte ondulado cubiertas con salsa de queso fundido, carne desmechada, tocino, crema ácida y un toque de ciboulette fresco. Una opción contundente.',7990.00,'/assets/papas_clasicas.webp',1,2),('prod-11','Agua Individual','Botella de agua purificada sin gas por unidad. La opción más ligera, clásica y saludable para mantenerte hidratado.',1200.00,'/assets/agua.webp',1,3),('prod-12','Jugo Individual','Jugo néctar en formato de 400 ml por unidad. Una alternativa dulce y refrescante para acompañar tus comidas. Sabores a elección según disponibilidad.',1500.00,'/assets/jugo_400ml.webp',1,3),('prod-13','Bebida en Lata','Bebida en lata de 350 cc por unidad, servida bien fría. Elige tu sabor favorito entre nuestras distintas opciones clásicas, saborizadas o en su versión sin azúcar.',1500.00,'/assets/bebidas_350cc.webp',1,3),('prod-2','COMBO GORILLA GLUE','Una opción intensa y crujiente. Hamburguesa de carne de res con queso cheddar derretido, tiras de tocino, pepinillos, aros de cebolla empanizados y salsa BBQ. Incluye porción de papas fritas y una lata de Coca-Cola.',11990.00,'/assets/combo_gorilla_glue.webp',1,1),('prod-3','COMBO HAZE','Una combinación de sabores dulces y salados. Hamburguesa de carne de res con queso cheddar, cebolla caramelizada, pepinillos y una cubierta de mermelada de tocino o relish. Incluye porción de papas fritas y una lata de Coca-Cola.',11990.00,'/assets/combo_haze.webp',1,1),('prod-4','COMBO PURPLE HAZE','El combo más completo. Hamburguesa de carne desmechada con abundante guacamole, rodajas de tomate y queso. Acompañada de papas fritas, crujientes bocados de pollo frito, salsa para untar y una lata de Coca-Cola.',13990.00,'/assets/combo_purple_haze.webp',1,1),('prod-5','Papas Fritas Individuales','Porción individual de papas fritas de corte ondulado, presentadas en bolsa de papel. El tamaño justo para calmar el antojo.',1990.00,'/assets/papas_individuales.webp',1,2),('prod-6','Nuggets de Pollo','Clásicos bocados de pollo con un exterior crujiente y dorado. Ideales para compartir o como el acompañamiento perfecto. Porción de 5 unidades.',2990.00,'/assets/nuggets_pollo.webp',1,2),('prod-7','Aros de Cebolla','Aros de cebolla gruesos con un empanizado crujiente y dorado. Un acompañamiento clásico con un suave contraste dulce y salado.',3490.00,'/assets/aros_cebolla.webp',1,2),('prod-8','Mozarella Fingers','Bastones de queso mozzarella empanizados y fritos hasta alcanzar un dorado crujiente por fuera, con queso suave y fundido por dentro. Porción de 6 unidades.',4490.00,'/assets/mozarella_fingers.webp',1,2),('prod-9','Papas Fritas Grandes','Porción familiar de papas fritas de corte ondulado, servidas calientes y con el punto exacto de sal. Especial para compartir.',4990.00,'/assets/papas_grandes.webp',1,2);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-03 12:42:59
