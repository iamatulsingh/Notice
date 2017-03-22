<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
		$name = $_POST['name'];
		$username = $_POST['username'];
		$password = $_POST['password'];
		$email = $_POST['email'];
		$role = $_POST['role'];
		
		if($name == '' || $username == '' || $password == '' || $email == '' || $role == ''){
			echo 'please fill all values';
		}else{
			require_once('Connect.php');
			$sql = "SELECT * FROM details WHERE username='$username' OR email='$email'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo 'username or email already exist';
			}else{				
				$sql = "INSERT INTO details (name,username,password,email,role) VALUES('$name','$username','$password','$email','$role')";
				if(mysqli_query($con,$sql)){
					echo 'successfully registered';
				}else{
					echo 'oops! Please try again!';
				}
			}
			mysqli_close($con);
		}
}else{
echo 'error';
}
?>