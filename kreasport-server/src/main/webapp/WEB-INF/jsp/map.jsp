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
<form id="form">
<label >question <a id="questionNbr"></a>: </label><input id="question"><br/>
<label>Answers : </label><br/>
<label>1 : </label><input id ="answer 1"> <label>2 : </label><input id="answer 2"><br/>
<label>3 : </label><input id="answer 3"> <label>4 : </label><input id="answer 4"><br/>
<label>correct : </label><input id ="correct"value="fill with the right number"><br/>
 </form>
 	<div id="myButton">
		<button type="button" onclick="previous()" id="previous">previous</button>  <button type="button" onclick="getQuestion()" id="save">save</button>  <button type="button" onclick="next()" id="next">next</button> 
	</div>
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
var riddles= new Array(5);

displayControl();

function displayControl(){
	if(questionNbr==1){
		document.getElementById('previous').style.opacity=0.6;
		document.getElementById('previous').style.cursor="not-allowed";
	} else if(questionNbr == 5){
		document.getElementById('next').style.opacity=0.6;
		document.getElementById('next').style.cursor="not-allowed";
		
	} else {
		document.getElementById('previous').style.opacity=1;
		document.getElementById('previous').style.cursor="allowed";

		document.getElementById('next').style.opacity=1;
		document.getElementById('next').style.cursor="allowed";
		
	}
	document.getElementById('questionNbr').innerHTML=questionNbr;
	
}

function storeData(){
	riddles[questionNbr]={
		question: getQuestion(),
        answers:getAnswers(),
        answerIndex: getCorrect()
	}
}

function dataControl(){
	storeData();
	if(riddles[questionNbr].question==""){
		alert("please fill out the question field");
		return false;
	}else if(riddles[questionNbr].answers[0]=="" ||riddles[questionNbr].answers[1]=="" ||riddles[questionNbr].answers[2]=="" ||riddles[questionNbr].answers[3]==""  ){
		alert("please fill out all the answers fields");
		return false;
	}
	
	else {
		return true;
	}
	
}

function previous(){
		if(questionNbr>1){
		questionNbr=questionNbr - 1;
		alert(questionNbr);
		displayControl();
		} 
}

function next(){
	if(dataControl()){
		if(questionNbr<5){
		questionNbr=questionNbr + 1;
		alert(questionNbr);
		displayControl();
		}
	}
}

function getQuestion(){
	return document.getElementById('question').value;
}

function getAnswers(){
	return [document.getElementById('answer 1').value,document.getElementById('answer 2').value,document.getElementById('answer 3').value,document.getElementById('answer 4').value];
}

function getCorrect(){
	return parseInt(document.getElementById('correct').value)
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
