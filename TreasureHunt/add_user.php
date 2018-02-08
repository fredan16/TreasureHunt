<?php
require('db.php');

$username = $_POST['username'];
$password = $_POST['password'];
$email = $_POST['email'];


$sql = "INSERT INTO `user`(`id`, `name`, `password`, `email`, `score`, `positionx`, `positiony`, `hash`) VALUES (null,'$username','$password','$email',0,0,0,0)";
$stmt = $dbh->prepare($sql);
if($stmt->execute() == true){
  echo "Added";
}
else{
  echo "Failed";
}


 ?>
