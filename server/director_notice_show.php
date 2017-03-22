<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
	$id = $_POST['id'];
 
	require_once('Connect.php');
	
	$sql = "select * from director_notice where id='$id'"; 
	$result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)){
		while($row=mysqli_fetch_assoc($result)){
			$json['notice'][]=$row;
		}
	}
	
	mysqli_close($con);

	echo json_encode($json); 
}
else{
echo 'error';
}

?>