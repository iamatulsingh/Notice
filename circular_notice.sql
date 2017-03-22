--
-- Database: `circular_notice`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `email` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`username`, `password`, `email`) VALUES
('admin', 'admin', 'admin@mail.com');

-- --------------------------------------------------------

--
-- Table structure for table `details`
--

CREATE TABLE `details` (
  `name` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `details`
--

INSERT INTO `details` (`name`, `username`, `password`, `email`, `role`) VALUES
('beta', 'beta', 'beta', 'beta@mail.com', 'dev'),
('test', 'hod', 'hod', 'hod@mail.com', 'hod');

-- --------------------------------------------------------

--
-- Table structure for table `director`
--

CREATE TABLE `director` (
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `director`
--

INSERT INTO `director` (`username`, `password`, `email`) VALUES
('director', 'director', 'dir@mail.com');

-- --------------------------------------------------------

--
-- Table structure for table `director_notice`
--

CREATE TABLE `director_notice` (
  `id` int(11) NOT NULL,
  `notice` longtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `director_notice`
--

INSERT INTO `director_notice` (`id`, `notice`) VALUES
(4, 'Sometimes when you innovate, you make mistakes. It is best to admit them quickly, and get on with improving your other innovations.'),
(5, 'It''s really hard to design products by focus groups. A lot of times, people don''t know what they want until you show it to them.'),
(6, 'My favorite things in life don''t cost any money. It''s really clear that the most precious resource we all have is time.'),
(7, 'I''m convinced that about half of what separates the successful entrepreneurs from the non-successful ones is pure perseverance.');

-- --------------------------------------------------------

--
-- Table structure for table `notice`
--

CREATE TABLE `notice` (
  `id` int(11) NOT NULL,
  `notice` longtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notice`
--

INSERT INTO `notice` (`id`, `notice`) VALUES
(1, 'An expert is someone who knows more and more about less and less, until eventually he knows everything about nothing.'),
(3, 'Being the richest man in the cemetery doesn''t matter to me. Going to bed at night saying we''ve done something wonderful, that''s what matters to me.'),
(6, 'Topic: see whats happens\n\nthis notice is from Android phone as for alpha testing purpose.\n'),
(7, 'Topic: second test\n\nthis is for only student notice \n');

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `name` varchar(30) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `role` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`name`, `username`, `password`, `email`, `role`) VALUES
('atul', 'atul', 'atul', 'mail@atul.com', 'dev'),
('hacker', 'hacker', 'hacker', 'hacker@mail.com', 'dev'),
('big', 'big', 'big', 'big@mail.com', 'dev');

-- --------------------------------------------------------

--
-- Table structure for table `teacher_notice`
--

CREATE TABLE `teacher_notice` (
  `id` int(11) NOT NULL,
  `notice` longtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher_notice`
--

INSERT INTO `teacher_notice` (`id`, `notice`) VALUES
(1, 'we are hackers having black terminal with green text'),
(4, 'Topic: see whats happens\n\nthis notice is from Android phone as for alpha testing purpose.\n'),
(5, 'Topic: third test for teacher\n\nthis notice only for teacher notice table.\n');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `details`
--
ALTER TABLE `details`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `director_notice`
--
ALTER TABLE `director_notice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notice`
--
ALTER TABLE `notice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `teacher_notice`
--
ALTER TABLE `teacher_notice`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `director_notice`
--
ALTER TABLE `director_notice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `notice`
--
ALTER TABLE `notice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `teacher_notice`
--
ALTER TABLE `teacher_notice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
