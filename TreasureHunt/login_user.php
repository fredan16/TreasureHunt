<?php
require('db.php');

$username = $_POST['username'];
$password = $_POST['password'];

$myObject = (object)[];
$sql = "SELECT * from user where name = '$username'";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();
$result = $stmt->FetchAll();


foreach ($result as $res) {
  if($password == $res->password){

    $hash = hash('md5' ,rand(1,100).$res->email.rand(1,100));

    $myObject->hash = $hash;
    $myObject->name = $res->name;
    $userTempID = $res->id;
    $sql2 = "UPDATE `user` SET `hash`='$hash' WHERE ID = '$userTempID'";
    //echo $sql2;
    $stmt2 = $dbh->prepare($sql2);
    $stmt2->execute();
  }
  else{
    //echo "Wrong Password";  $hash = hash('md5' ,$res->email);
    $myObject->hash = "none";
    $myObject->name = $res->name;
  }
}
$json = json_encode($myObject);
//$json = json_encode($result2[0]->event);
echo $json;
 ?>
