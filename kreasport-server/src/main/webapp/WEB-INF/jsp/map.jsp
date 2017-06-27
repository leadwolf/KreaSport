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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
<label >question <a id="questionNbr"></a>/5 : </label><input id="question"><br/>
<label>Answers : </label><br/>
<label>1 : </label><input id ="answer 1"> <label>2 : </label><input id="answer 2"><br/>
<label>3 : </label><input id="answer 3"> <label>4 : </label><input id="answer 4"><br/>
<label>correct : </label><input id ="correct"value="fill with the right number"><br/>
 </form>
 	<div id="myButton">
		<button type="button" onclick="previous()" id="previous">previous</button>  <button type="button" onclick="submitRace()" id="save">save</button>  <button type="button" onclick="next()" id="next">next</button> 
	</div>
</div>
<script type="text/javascript">
var coord = new Array(5);
function initMap() {
	
    var gcu = {lat: 55.8661538, lng: -4.2529928};

    var marker= new Array(5);
   var map = new google.maps.Map(document.getElementById('map'), {
        center: gcu,
        zoom: 16
    });
    
    map.addListener("click",function(e){
   	  coord[questionNbr-1] = e.latLng;
   	 if(marker[questionNbr-1]!=undefined){
   		marker[questionNbr-1].setMap(null)
   	 }
   	 marker[questionNbr-1] = new google.maps.Marker({
        position: coord[questionNbr-1],
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
		
		document.getElementById('save').style.opacity=0.6;
		document.getElementById('save').style.cursor="not-allowed";
		
	} else if(questionNbr == 5){
		document.getElementById('next').style.opacity=0.6;
		document.getElementById('next').style.cursor="not-allowed";
		
		document.getElementById('save').style.opacity=1;
		document.getElementById('save').style.cursor="default";
		
	} else {
		document.getElementById('previous').style.opacity=1;
		document.getElementById('previous').style.cursor="default";

		document.getElementById('next').style.opacity=1;
		document.getElementById('next').style.cursor="default";
		
		
	}
	document.getElementById('questionNbr').innerHTML=questionNbr;
	
}

function clearFields(){
	document.getElementById('question').value="";
	document.getElementById('answer 1').value="";
	document.getElementById('answer 2').value="";
	document.getElementById('answer 3').value="";
	document.getElementById('answer 4').value="";
	document.getElementById('correct').value="";
	
}

function restoreData(){
	document.getElementById('question').value=riddles[questionNbr-1].question;
	document.getElementById('answer 1').value=riddles[questionNbr-1].answers[0];
	document.getElementById('answer 2').value=riddles[questionNbr-1].answers[1];
	document.getElementById('answer 3').value=riddles[questionNbr-1].answers[2];
	document.getElementById('answer 4').value=riddles[questionNbr-1].answers[3];
	document.getElementById('correct').value=riddles[questionNbr-1].answerIndex;
	
}

function storeData(){
	riddles[questionNbr-1]={
		question: getQuestion(),
        answers:getAnswers(),
        answerIndex: getCorrect()
	}
}

function dataControl(){
	storeData();
	if(riddles[questionNbr-1].question==""){
		alert("please fill out the question field");
		return false;
	}else if(riddles[questionNbr-1].answers[0]=="" ||riddles[questionNbr-1].answers[1]=="" ||riddles[questionNbr-1].answers[2]=="" ||riddles[questionNbr-1].answers[3]==""  ){
		alert("please fill out all the answers fields");
		return false;
	} else if(!(riddles[questionNbr-1].answerIndex==1 || riddles[questionNbr-1].answerIndex==2 || riddles[questionNbr-1].answerIndex==3 || riddles[questionNbr-1].answerIndex==4)){
		alert("please fill out all the correct answer fields (1, 2, 3 or 4)");
	} else if(coord[questionNbr-1]==undefined){
		alert("please select a place on the map");
		return false;
	} else {
		return true;
	}
	
}

function previous(){
		if(questionNbr>1){
		questionNbr=questionNbr - 1;
		displayControl();
		restoreData();
		} 
}

function next(){
	if(dataControl()){
		if(questionNbr<5){
		questionNbr=questionNbr + 1;
		displayControl();
		clearFields();
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

if(questionNbr==5 && dataControl()){
  var race = {
        id: "${user.userId}",
        title: "",
        description: "",
        latitude: coord[0].lat(),
        longitude: coord[0].lng(),
        checkpoints:
        [
        	{
                id: "59414c5b37fc011b24625fad",
                title: "",
                description: "",
                latitude: coord[0].lat(),
                longitude: coord[0].lng(),
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 0
                },
                riddle:riddles[0],
                location:coord[0]
            },
            {
                id: "59414c5b37fc011b24625fad",
                title: "",
                description: "",
                latitude: coord[1].lat(),
                longitude: coord[1].lng(),
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 1
                },
                riddle:riddles[1],
                location:coord[1]
            },
            {
                id: "59414c5b37fc011b24625fad",
                title: "",
                description: "",
                latitude: coord[2].lat(),
                longitude: coord[2].lng(),
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 2
                },
                riddle:riddles[2],
                location:coord[2]
            },
            {
                id: "59414c5b37fc011b24625fad",
                title: "",
                description: "",
                latitude: coord[3].lat(),
                longitude: coord[3].lng(),
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 3
                },
                riddle:riddles[3],
                location:coord[3]
            },
            {
                id: "59414c5b37fc011b24625fad",
                title: "",
                description: "",
                latitude: coord[4].lat(),
                longitude: coord[4].lng(),
                checkpointKey:
                {
                    raceId: "59414c5b37fc011b24625fac",
                    order: 4
                },
                riddle:riddles[4],
                location:coord[4]
            }
            ]
  };


  var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
  xmlhttp.open("POST", "http://localhost:8080/races", true);
  xmlhttp.setRequestHeader("Content-Type", "application/json");
  xmlhttp.send(JSON.stringify(race));

  window.alert("Submitted");

} else {
	alert("please go to question 5 to save")
}
}

</script>

</body>
</html>
