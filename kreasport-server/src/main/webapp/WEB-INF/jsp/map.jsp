<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Map Page</title>
    <link rel="stylesheet" type="text/css" href="/css/map.css"/>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="/js/jquery.growl.js" type="text/javascript"></script>
</head>
<nav>
<a href="/">home page</a>
</nav>
<body>

<div id="map">
</div>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD4Lz7J3WRKLuF2YFTCMXMpmwi0D_DvsJQ&callback=initMap&libraries=geometry">
		</script>

<div id = "formulaire">
<form action="">
<label >question <a id="questionNbr"></a>: </label><input id="question"><br/>
<label>Answer : </label><br/>
<label>1 : </label><input id ="answer 1"> <label>2 : </label><input id="answer 2"><br/>
<label>3 : </label><input id="answer 3"> <label>4 : </label><input id="answer 3"><br/>
<label>correct : </label><input value="fill with the right number"><br/>
 <button type="button">previous</button>  <button type="button" onclick="getQuestion()">save</button>  <button type="button">next</button> 
</form>
</div>
<script type="text/javascript">
function initMap() {

    var gcu = {lat: 55.8661538, lng: -4.2529928};
    var coord;
   var map = new google.maps.Map(document.getElementById('map'), {
        center: gcu,
        zoom: 16
    });
    
    map.addListener("click",function(e){
   	  coord = e.latLng;
   	 alert(coord);
   	 
   	var marker = new google.maps.Marker({
        position: coord,
        map: map,
        title: 'Click to place marker'
      });
   	
     });    
}



</script>

<script type="text/javascript" >
var questionNbr=1;
document.getElementById('questionNbr').innerHTML=questionNbr;

function getQuestion(){
	alert(document.getElementById('question').value);
	
}

function submitRace() {

	/*var riddle = {
					question: "can I talk to you for a minute?",
               answer: ["yeah what's up","wrong answer"],
               answerIndex: 0
	}
*/

  var race = {
        id: "59414c5b37fc011b24625fac",
        title: "Dummy Race Title 0",
        description: "Dummy Race Description 0",
        latitude: 50.613664,
        longitude: 3.136939,
        checkpoints:
        [
            {
                id: "59414c5b37fc011b24625fad",
                title: "Dummy Checkpoint title 0",
                description: "Dummy Checkpoint Description 0",
                latitude: 50.613144,
                longitude: 3.138257,
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 0
                },
                riddle:
                {
                    question: "What answer? (0)",
                    answers:["Answer 0","Answer 1"],
                    answerIndex: 0
                },
                location:[50.613144,3.138257]
            }]
  };


  var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
  xmlhttp.open("POST", "http://localhost:8080/dummy", true);
  xmlhttp.setRequestHeader("Content-Type", "application/json");
  xmlhttp.send(JSON.stringify(race));

  window.alert("Submitted");
}

</script>

</body>
</html>
