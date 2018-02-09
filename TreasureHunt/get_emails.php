<?php
require('db.php');


$myObject = (object)[];
$sql = "SELECT email as e from user";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();
$result = $stmt->FetchAll();

$test = array();
$intCounter = 0;
foreach ($result as $res) {


    $myObject->email = $res->e;
    $json = json_encode($myObject);
    $test[$intCounter] = $json;
    $intCounter++;
    //echo $json;
}
$finalj = json_encode($test,JSON_FORCE_OBJECT);
echo $finalj;
 ?>
