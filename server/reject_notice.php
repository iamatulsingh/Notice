<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$id = $_POST['id'];
 
	require_once('Connect.php');
 
	$sql = "delete from director_notice where id='$id'";
 
	if (mysqli_query($con, $sql))
		echo "Notice will deleted if exist!!!";
    else
		echo "Error deleting notice: " . mysqli_error($con);
}
else{
echo 'error';
}
?>