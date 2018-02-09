<?php
require('db.php');

$name = $_POST['name'];
$level = $_POST['level'];
$timer = $_POST['timer'];
$size = $_POST['size'];
$colorfyll = $_POST['colorfyll'];
$colorout =  $_POST['colorout'];
$width = $_POST['width'];
$answer = $_POST['answer'];
$problem = $_POST['problem'];
$worth = $_POST['worth'];

echo $name . $level . $timer . $size . $colorfyll . $colorout . $width . $answer . $problem . $worth;

$myObject = (object)[];

$myObject->name = $name;
$myObject->level = $level;
$myObject->timercount = $timer;
$myObject->size = $size;
$myObject->colorFyll = $colorfyll;
$myObject->colorOut = $colorout;
$myObject->width = $width;
$myObject->answer = $answer;
$myObject->problem = $problem;
$myObject->worth = $worth;

echo json_encode($myObject);
$temp = json_encode($myObject);
$sql = "INSERT INTO `event`(`id`, `event`) VALUES (null,'$temp')";
$stmt = $dbh->prepare($sql);
if($stmt->execute() == true){
  echo "Added";
  header("Location:index.php");
}
else{
  echo "Failed";
}



 ?>
