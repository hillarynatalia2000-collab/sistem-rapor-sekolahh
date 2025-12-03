-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 03, 2025 at 06:12 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `siswas`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `id`) VALUES
('ADM-296E04F0', 2),
('ADM-88295DB6', 4);

-- --------------------------------------------------------

--
-- Table structure for table `mata_pelajaran`
--

CREATE TABLE `mata_pelajaran` (
  `id_mata_pelajaran` varchar(255) NOT NULL,
  `jumlah_jam` int(11) DEFAULT NULL,
  `kode` varchar(255) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mata_pelajaran`
--

INSERT INTO `mata_pelajaran` (`id_mata_pelajaran`, `jumlah_jam`, `kode`, `nama`) VALUES
('MATH', 2, 'math', 'matematika');

-- --------------------------------------------------------

--
-- Table structure for table `nilai_mata_pelajaran`
--

CREATE TABLE `nilai_mata_pelajaran` (
  `id_nilai_mata_pelajaran` int(11) NOT NULL,
  `id_mata_pelajaran` varchar(255) DEFAULT NULL,
  `id_siswa` int(11) DEFAULT NULL,
  `nilai` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE `person` (
  `id` int(11) NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `nomor_telepon` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `person`
--

INSERT INTO `person` (`id`, `alamat`, `email`, `nama`, `nomor_telepon`, `password`, `role`, `tanggal_lahir`, `username`) VALUES
(1, 'jalan baru', 'nadya@email.com', 'nadya', '081212342355', '123456', 'siswa', '2002-03-24', 'nadya'),
(2, 'jalan lama', 'admin1@email.com', 'admin1', '08121234556', '123456', 'admin', '2008-06-24', 'admin1'),
(3, 'jalan angke', 'diah@email.com', 'Diah', '081212342211', '123456', 'walikelas', '2003-03-24', 'diah'),
(4, 'khugc', 'muhkelvin@gmail.com', 'muh kelvin', '09998765444', '12345', 'admin', '1997-07-18', 'muhkelvin');

-- --------------------------------------------------------

--
-- Table structure for table `raport`
--

CREATE TABLE `raport` (
  `id` bigint(20) NOT NULL,
  `catatan_wali_kelas` varchar(255) DEFAULT NULL,
  `nilai_mapel` varchar(255) DEFAULT NULL,
  `nomor_rapot` varchar(255) DEFAULT NULL,
  `siswa_id` bigint(20) DEFAULT NULL,
  `status_kenaikan_kelas` varchar(255) DEFAULT NULL,
  `tanggal_diterbitkan` date DEFAULT NULL,
  `wali_kelas_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `id_siswa` int(11) DEFAULT NULL,
  `kelas` varchar(255) DEFAULT NULL,
  `mapel_list` varchar(255) DEFAULT NULL,
  `nilai` float DEFAULT NULL,
  `nilai_list` varchar(255) DEFAULT NULL,
  `nilai_mapel` varchar(255) DEFAULT NULL,
  `nis` varchar(255) DEFAULT NULL,
  `status_konfirmasi` bit(1) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`id_siswa`, `kelas`, `mapel_list`, `nilai`, `nilai_list`, `nilai_mapel`, `nis`, `status_konfirmasi`, `id`) VALUES
(0, 'Belum Ditentukan', 'MATH', NULL, NULL, NULL, '3382734282', b'1', 1);

-- --------------------------------------------------------

--
-- Table structure for table `siswa_mata_pelajaran`
--

CREATE TABLE `siswa_mata_pelajaran` (
  `siswa_id` int(11) NOT NULL,
  `mata_pelajaran_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `siswa_mata_pelajaran`
--

INSERT INTO `siswa_mata_pelajaran` (`siswa_id`, `mata_pelajaran_id`) VALUES
(1, 'MATH');

-- --------------------------------------------------------

--
-- Table structure for table `wali_kelas`
--

CREATE TABLE `wali_kelas` (
  `id_wali_kelas` int(11) DEFAULT NULL,
  `jumlah_siswa_walian` int(11) DEFAULT NULL,
  `nama_kelas_walian` varchar(255) DEFAULT NULL,
  `nip` varchar(255) DEFAULT NULL,
  `status_konfirmasi` bit(1) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wali_kelas`
--

INSERT INTO `wali_kelas` (`id_wali_kelas`, `jumlah_siswa_walian`, `nama_kelas_walian`, `nip`, `status_konfirmasi`, `id`) VALUES
(0, 0, 'X IPA 2', '3847263492', b'1', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKkn6dmu3pyga9qu86jdkchc8cd` (`id_admin`);

--
-- Indexes for table `mata_pelajaran`
--
ALTER TABLE `mata_pelajaran`
  ADD PRIMARY KEY (`id_mata_pelajaran`),
  ADD UNIQUE KEY `UKsloisal6wy8k574dmy1qw8r0q` (`kode`);

--
-- Indexes for table `nilai_mata_pelajaran`
--
ALTER TABLE `nilai_mata_pelajaran`
  ADD PRIMARY KEY (`id_nilai_mata_pelajaran`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `raport`
--
ALTER TABLE `raport`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKqj17trxkactt9gavs1adia5yq` (`nis`);

--
-- Indexes for table `siswa_mata_pelajaran`
--
ALTER TABLE `siswa_mata_pelajaran`
  ADD KEY `FK3pn458gmd6s5q093mu49y0b81` (`mata_pelajaran_id`),
  ADD KEY `FKiimpbuwd71s4ldrgtdbso12su` (`siswa_id`);

--
-- Indexes for table `wali_kelas`
--
ALTER TABLE `wali_kelas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKeftvipl9wbelqre2ofkhb5pc` (`nip`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `nilai_mata_pelajaran`
--
ALTER TABLE `nilai_mata_pelajaran`
  MODIFY `id_nilai_mata_pelajaran` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `person`
--
ALTER TABLE `person`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `raport`
--
ALTER TABLE `raport`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `FKsplda61kmlib6vk6qmwfv08dh` FOREIGN KEY (`id`) REFERENCES `person` (`id`);

--
-- Constraints for table `siswa`
--
ALTER TABLE `siswa`
  ADD CONSTRAINT `FK6ning5gy1vua4md1d2ehe6v3r` FOREIGN KEY (`id`) REFERENCES `person` (`id`);

--
-- Constraints for table `siswa_mata_pelajaran`
--
ALTER TABLE `siswa_mata_pelajaran`
  ADD CONSTRAINT `FK3pn458gmd6s5q093mu49y0b81` FOREIGN KEY (`mata_pelajaran_id`) REFERENCES `mata_pelajaran` (`id_mata_pelajaran`),
  ADD CONSTRAINT `FKiimpbuwd71s4ldrgtdbso12su` FOREIGN KEY (`siswa_id`) REFERENCES `siswa` (`id`);

--
-- Constraints for table `wali_kelas`
--
ALTER TABLE `wali_kelas`
  ADD CONSTRAINT `FK8uy5xo48wuyk2lkmamdpfk6lq` FOREIGN KEY (`id`) REFERENCES `person` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
