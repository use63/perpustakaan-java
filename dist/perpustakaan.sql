-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 16, 2024 at 03:22 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `IDBuku` int(11) NOT NULL,
  `Judul` varchar(100) DEFAULT NULL,
  `Pengarang` varchar(50) DEFAULT NULL,
  `JumlahTersedia` int(11) DEFAULT NULL,
  `IDGenre` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`IDBuku`, `Judul`, `Pengarang`, `JumlahTersedia`, `IDGenre`) VALUES
(1, 'Judul Buku 1', 'Pengarang 1', 5, 6),
(2, 'Judul Buku 2', 'Pengarang 2', 8, 4),
(3, 'Judul Buku 3', 'Pengarang 3', 3, 8),
(4, 'Judul Buku 4', 'Pengarang 4', 10, 1),
(5, 'Judul Buku 5', 'Pengarang 5', 6, 1),
(6, 'Judul Buku 6', 'Pengarang 6', 4, 5),
(7, 'Judul Buku 7', 'Pengarang 7', 7, 9),
(8, 'Judul Buku 8', 'Pengarang 8', 2, 9),
(9, 'Judul Buku 9', 'Pengarang 9', 9, 3),
(10, 'Judul Buku 10', 'Pengarang 10', 5, 3),
(11, 'Sophie\'s World', 'Jostein Gaarder', 10, 3);

-- --------------------------------------------------------

--
-- Table structure for table `genre`
--

CREATE TABLE `genre` (
  `IDGenre` int(11) NOT NULL,
  `NamaGenre` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `genre`
--

INSERT INTO `genre` (`IDGenre`, `NamaGenre`) VALUES
(1, 'Fiksi'),
(2, 'Non-Fiksi'),
(3, 'Novel'),
(4, 'Sejarah'),
(5, 'Sains'),
(6, 'Biografi'),
(7, 'Drama'),
(8, 'Romansa'),
(9, 'Horor'),
(10, 'Komik');

-- --------------------------------------------------------

--
-- Table structure for table `peminjamanbuku`
--

CREATE TABLE `peminjamanbuku` (
  `IDPeminjaman` int(11) NOT NULL,
  `IDPengguna` int(11) DEFAULT NULL,
  `IDBuku` int(11) DEFAULT NULL,
  `TanggalPeminjaman` date DEFAULT NULL,
  `TanggalPengembalian` date DEFAULT NULL,
  `Status` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjamanbuku`
--

INSERT INTO `peminjamanbuku` (`IDPeminjaman`, `IDPengguna`, `IDBuku`, `TanggalPeminjaman`, `TanggalPengembalian`, `Status`) VALUES
(1, 2, 3, '2024-01-10', '2024-01-20', 'Dipinjam'),
(2, 3, 7, '2024-01-12', '2024-01-25', 'Dipinjam'),
(3, 5, 1, '2024-01-15', '2024-01-28', 'Dipinjam'),
(4, 8, 9, '2024-01-18', '2024-01-22', 'Dipinjam'),
(5, 1, 2, '2024-01-20', '2024-01-30', 'Dipinjam'),
(6, 4, 6, '2024-01-22', '2024-02-01', 'Dipinjam'),
(7, 6, 5, '2024-01-25', '2024-02-05', 'Dipinjam'),
(8, 9, 8, '2024-01-28', '2024-02-08', 'Dipinjam'),
(9, 10, 4, '2024-01-30', '2024-02-10', 'Dipinjam'),
(10, 7, 10, '2024-02-01', '2024-02-15', 'Dipinjam');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `IDPengguna` int(11) NOT NULL,
  `NamaPengguna` varchar(50) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `KataSandi` varchar(255) DEFAULT NULL,
  `Peran` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`IDPengguna`, `NamaPengguna`, `Email`, `KataSandi`, `Peran`) VALUES
(1, 'admin', 'admin@example.com', 'admin123', 'Admin'),
(2, 'user1', 'user1@example.com', 'hashed_password_user1', 'Pengguna'),
(3, 'user2', 'user2@example.com', 'hashed_password_user2', 'Pengguna'),
(4, 'user3', 'user3@example.com', 'hashed_password_user3', 'Pengguna'),
(5, 'user4', 'user4@example.com', 'hashed_password_user4', 'Pengguna'),
(6, 'user5', 'user5@example.com', 'hashed_password_user5', 'Pengguna'),
(7, 'user6', 'user6@example.com', 'hashed_password_user6', 'Pengguna'),
(8, 'user7', 'user7@example.com', 'hashed_password_user7', 'Pengguna'),
(9, 'user8', 'user8@example.com', 'hashed_password_user8', 'Pengguna'),
(10, 'user9', 'user9@example.com', 'hashed_password_user9', 'Pengguna'),
(12, 'Developer', 'dev', 'dev', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`IDBuku`),
  ADD KEY `FK_Buku_Genre` (`IDGenre`);

--
-- Indexes for table `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`IDGenre`);

--
-- Indexes for table `peminjamanbuku`
--
ALTER TABLE `peminjamanbuku`
  ADD PRIMARY KEY (`IDPeminjaman`),
  ADD KEY `IDBuku` (`IDBuku`),
  ADD KEY `PeminjamanBuku_ibfk_1` (`IDPengguna`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`IDPengguna`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buku`
--
ALTER TABLE `buku`
  MODIFY `IDBuku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `IDPengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `buku`
--
ALTER TABLE `buku`
  ADD CONSTRAINT `FK_Buku_Genre` FOREIGN KEY (`IDGenre`) REFERENCES `genre` (`IDGenre`);

--
-- Constraints for table `peminjamanbuku`
--
ALTER TABLE `peminjamanbuku`
  ADD CONSTRAINT `PeminjamanBuku_ibfk_1` FOREIGN KEY (`IDPengguna`) REFERENCES `pengguna` (`IDPengguna`),
  ADD CONSTRAINT `PeminjamanBuku_ibfk_2` FOREIGN KEY (`IDBuku`) REFERENCES `buku` (`IDBuku`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
