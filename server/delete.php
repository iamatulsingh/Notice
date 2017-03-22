<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
	$username = $_POST['username'];
 
	require_once('Connect.php');
 
	$sql = "delete from details where username='$username'";
 
	if (mysqli_query($con, $sql))
		echo "Record will deleted if exist!!!";
    else
		echo "Error deleting record: " . mysqli_error($con);
}
else{
echo 'error';
}
?>