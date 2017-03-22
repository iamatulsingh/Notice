<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
		$notice = $_POST['notice'];
		$teacher = $_POST['teacher'];
		$student = $_POST['student'];
		
		require_once('Connect.php');
		
		if($notice == ''){
			echo 'please write notice first';
		}
		
		if($teacher == ''){
			$sql = "INSERT INTO notice VALUES(null,'$notice')";
				if(mysqli_query($con,$sql)){
					echo 'successfully inserted into student';
				}else{
					echo 'oops! Please try again! '. mysqli_error($con);
				}
		}
		
		if($student == ''){
			$sql = "INSERT INTO teacher_notice VALUES(null,'$notice')";
				if(mysqli_query($con,$sql)){
					echo 'successfully inserted into teacher';
				}else{
					echo 'oops! Please try again! '. mysqli_error($con);
				}
		}
		
		if($teacher == 'teacher' && $student == 'student'){
			$sql1 = "INSERT INTO notice VALUES(null,'$notice')";
			$sql2 = "INSERT INTO teacher_notice VALUES(null,'$notice')";
			
			if(mysqli_query($con,$sql1)){
					echo 'successfully inserted into student and teacher';
			}else{
					echo 'oops! Please try again! '. mysqli_error($con);
			}
			
			if(mysqli_query($con,$sql2)){
					echo 'successfully inserted into teacher and student';
			}else{
					echo 'oops! Please try again! '. mysqli_error($con);
			}
		}
		
		mysqli_close($con);
}else{
echo 'error';
}
?>