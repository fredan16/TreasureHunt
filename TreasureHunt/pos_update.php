<?php
require('db.php');

$hash = $_POST['hash'];
$positionX = $_POST['posx'];
$positionY = $_POST['posy'];


$sql = "UPDATE `user` SET `positionx`='$positionX',`positiony`='$positionY' WHERE hash = '$hash'";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();

 ?>
