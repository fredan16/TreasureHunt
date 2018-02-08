<?php
require('db.php');


$myObject = (object)[];
$sql = "SELECT * from event";
//echo $sql;
$stmt = $dbh->prepare($sql);
$stmt->execute();
$result = $stmt->FetchAll();

$sql2 = "SELECT COUNT(*) as c FROM event";
//echo $sql;
$stmt2 = $dbh->prepare($sql2);
$stmt2->execute();
$result2 = $stmt2->FetchAll();

$myObject->count = $result2[0]->c;
$json = json_encode($myObject);
//echo $json;
$test = array();
$intCounter = 0;
foreach ($result as $res) {

    $myObject->id = $res->id;
    $myObject->event = $res->event;
    $json = json_encode($myObject);
    $test[$intCounter] = $json;
    $intCounter++;
    //echo $json;
}
$finalj = json_encode($test,JSON_FORCE_OBJECT);
echo $finalj;
 ?>
