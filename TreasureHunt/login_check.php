<?php
require('db.php');
session_start();
$name = $_GET['name'];
$pw = $_GET['pw'];

$sql = "SELECT * from user WHERE name ='$name'";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();
$result = $stmt->FetchAll();

$test = array();
$intCounter = 0;
foreach ($result as $res) {

  if($res->password == $pw){
    //correct login
    $_SESSION["admin"] = $res->name;
    header("location:index.php");
  }
}

 ?>
