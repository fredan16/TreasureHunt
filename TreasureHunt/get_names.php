<?php
require('db.php');


$myObject = (object)[];
$sql = "SELECT name as n from user";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();
$result = $stmt->FetchAll();

$test = array();
$intCounter = 0;
foreach ($result as $res) {


    $myObject->name = $res->n;
    $json = json_encode($myObject);
    $test[$intCounter] = $json;
    $intCounter++;
    //echo $json;
}
$finalj = json_encode($test,JSON_FORCE_OBJECT);
echo $finalj;
 ?>
