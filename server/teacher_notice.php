<?php
require_once('Connect.php');
	
	$sql = "select * from teacher_notice"; 
	$result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)){
		while($row=mysqli_fetch_assoc($result)){
			$json['teacher_notice'][]=$row;
		}
	}
	
	mysqli_close($con);

	echo json_encode($json); 
?> 