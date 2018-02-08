<?php
session_start();
if(!empty($_SESSION["admin"])){
	$admin = $_SESSION["admin"];
}

$markerArray = (object)[];

class DBController {
	private $host = "localhost";
	private $user = "root";
	private $password = "";
	private $database = "treasurehunt";
	private $conn;

	function __construct() {
		$this->conn = $this->connectDB();
	}

	function connectDB() {
		$conn = mysqli_connect($this->host,$this->user,$this->password,$this->database);
		return $conn;
	}

	function runQuery($query) {
		$result = mysqli_query($this->conn,$query);
		while($row=mysqli_fetch_array($result)) {
			$resultset[] = $row;
		}
		if(!empty($resultset))
			return $resultset;
	}
}
define("API_KEY","AIzaSyD5Xd6BsKxa4LC3kBu-AcCrSNzjpJQOLrY");
$dbController = new DBController();

$query = "SELECT positionx as x,positiony as y, name as n FROM user";
$result = $dbController->runQuery($query);
?>
<html>
<head>
<title>Add Markers to Show Locations in Google Maps</title>
</head>
<style>
	body {
		font-family :Arial;
	}
	#map-layer {
		margin: 20px 0px;
		max-width: 100%;
		min-height: 600;
	}
	.tablink {
	    background-color: #555;
	    color: white;
	    float: left;
	    border: none;
	    outline: none;
	    cursor: pointer;
	    padding: 14px 16px;
	    font-size: 17px;
	    width: 25%;
	}

	/* Change background color of buttons on hover */
	.tablink:hover {
	    background-color: #777;
	}

	/* Set default styles for tab content */
	.tabcontent {
	    color: white;
	    display: none;
	    padding: 50px;
	    text-align: center;

	}

	/* Style each tab content individually */
	#Admin {
		background-color:#000033;

	}
	#Map {
		background-color:#101010;
	}
	/* Full-width input fields */
	input[type=text], input[type=password] {
	    width: 100%;
	    padding: 12px 20px;
	    margin: 8px 0;
	    display: inline-block;
	    border: 1px solid #ccc;
	    box-sizing: border-box;
	}

	/* Set a style for all buttons */
	button {
	    background-color: #101010;
	    color: white;
	    padding: 14px 20px;
	    margin: 8px 0;
	    border: none;
	    cursor: pointer;
	    width: 100%;
	}

	button:hover {
	    opacity: 0.8;
	}

	/* Extra styles for the cancel button */
	.cancelbtn {
	    width: auto;
	    padding: 10px 18px;
	    background-color: #f44336;
	}

	/* Center the image and position the close button */
	.imgcontainer {
	    text-align: center;
	    margin: 24px 0 12px 0;
	    position: relative;
	}

	img.avatar {
	    width: 40%;
	    border-radius: 50%;
	}

	.container {
	    padding: 16px;
	}

	span.psw {
	    float: right;
	    padding-top: 16px;
	}

	/* The Modal (background) */
	.modal {
		display: none;
		position: fixed;
		z-index: 1;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		overflow: auto;
		background-color: rgb(0,0,0);
		background-color: rgba(0, 0, 0, 0.89);
		padding-top: 60px;
	}

	/* Modal Content/Box */
	.modal-content {
		background-color: #545454;
		margin: 5% auto 15% auto;
		border: 1px solid #888;
		width: 40%;
	}

	/* The Close Button (x) */
	.close {
	    position: absolute;
	    right: 25px;
	    top: 0;
	    color: #000;
	    font-size: 35px;
	    font-weight: bold;
	}

	.close:hover,
	.close:focus {
	    color: red;
	    cursor: pointer;
	}

	/* Add Zoom Animation */
	.animate {
	    -webkit-animation: animatezoom 0.6s;
	    animation: animatezoom 0.6s
	}

	@-webkit-keyframes animatezoom {
	    from {-webkit-transform: scale(0)}
	    to {-webkit-transform: scale(1)}
	}

	@keyframes animatezoom {
	    from {transform: scale(0)}
	    to {transform: scale(1)}
	}

	/* Change styles for span and cancel button on extra small screens */
	@media screen and (max-width: 300px) {
	    span.psw {
	       display: block;
	       float: none;
	    }
	    .cancelbtn {
	       width: 100%;
	    }
	}
</style>
<?php if(empty($admin)){
	?>
			<body onload="document.getElementById('id01').style.display='block'">
	<?php
}
else{?>
	<body>
		<?php
}
?>

<div id="id01" class="modal">
  <form class="modal-content animate" action="login_check.php">
    <div class="imgcontainer">
      <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
      <img src="image/avatar.png" alt="Avatar" class="avatar">
    </div>

    <div class="container">
      <label><b>Username</b></label>
      <input type="text" placeholder="Enter Username" name="name" required>

      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="pw" required>

      <button type="submit">Login</button>
      <label>
        <input type="checkbox" checked="checked"> Remember me
      </label>
    </div>

    <div class="container" style="background-color:#333232">
      <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
      <span class="psw">Forgot <a href="#">password?</a></span>
    </div>
  </form>
</div>

<div id="navbar">


	<button class="tablink" onclick="openCity('Map', this, '#202020')"id="defaultOpen">Map</button>
	<button class="tablink" onclick="openCity('Admin', this, '#00007f')" >Admin</button>
	<?php if(empty($admin)){?>
		<button class="tablink" onclick="document.getElementById('id01').style.display='block'" style="width:auto;">Login</button>

<?php
}else{?>
	<button class="tablink" onclick="window.location.href='logout.php'" style="width:auto;">Logout</button>
<?php
}?>
</div>

<div id="Admin" class="tabcontent">
  <h3>Admin Meny</h3>
	<button id="change_add_event">New Event</button>
	<button id="change_send_msg">Send Message</button>

	<div id="add_new">
		<form action="add_event.php" method="post">
			Name:
			<input type="text" placeholder="Name of event" name="name" required>
			<br>
			Level(default:1):
			<input type="number" min="1" max="20" value="1" placeholder="" name="level"required>
			<br>
			Timer(default:360):
			<input type="number" min="100" max="5000" value="360" placeholder="" name="timer"required>
			<br>
			Size (default:15):
			<input type="number" value="15" min="5" max="50" placeholder="" name="size"required>
			<br>
			Color Inside Circle:
			<input type="color" name="colorfyll"required>
			<br>
			Color Circle Outline:
			<input type="color" name="colorout"required>
			<br>
			Width(default:15):
			<input type="number" value="10"  min="10" max="100" name="width"required>
			<br>
			Worth(default:10):
			<input type="number" value="10" min="1" max="100" name="worth" required>
			<br>
			Problem(ex:How Many Tires Does A Car Have)
			<input type="text" name="problem" required>
			<br>
			Solution(ex:4):
			<input type="text" name="answer" required>
			<button type="submit">Add Event </button>
		</form>
	</div>
	<div id="send_msg">

	</div>
</div>

<div id="Map" class="tabcontent">

  <div id="map-layer"></div>
</div>



	<script
		src="https://maps.googleapis.com/maps/api/js?key=<?php echo API_KEY; ?>&callback=initMap"
		async defer></script>

	<script type="text/javascript">
	//change div on button press


	function openCity(cityName, elmnt, color) {
    // Hide all elements with class="tabcontent" by default */
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Remove the background color of all tablinks/buttons
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].style.backgroundColor = "";
    }

    // Show the specific tab content
    document.getElementById(cityName).style.display = "block";

    // Add the specific color to the button used to open the tab content
    elmnt.style.backgroundColor = color;
}
// Get the modal
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Get the element with id="defaultOpen" and click on it
document.getElementById("defaultOpen").click();
	var map;
	var geocoder;

	function initMap() {
		var mapLayer = document.getElementById("map-layer");
		var centerCoordinates = new google.maps.LatLng(58.39711158662653, 13.877179473638535);
		var defaultOptions = { center: centerCoordinates, zoom: 12 }

		map = new google.maps.Map(mapLayer, defaultOptions);
		geocoder = new google.maps.Geocoder();

		<?php
	    if(!empty($result))
	    {
	      foreach($result as $k=>$v)
	      {?>
				 var latlng = {lat: <?php echo $result[$k]["x"]; ?>,lng:<?php echo $result[$k]["y"];?>};
				 console.log(latlng);
         	geocoder.geocode( { 'location': latlng }, function(LocationResult, status) {
						if (status == google.maps.GeocoderStatus.OK) {
							var latitude = <?php echo $result[$k]["x"]; ?>;
							var longitude = <?php echo $result[$k]["y"]; ?>;
							console.log(latitude,longitude)
						}
  	    		new google.maps.Marker({
      	        position: new google.maps.LatLng(latitude, longitude),
      	        map: map,
      	        title: '<?php echo $result[$k]["n"]; ?>'
      	    });
					});
	    <?php
      }
	}?>
}
	</script>
</body>
</html>
