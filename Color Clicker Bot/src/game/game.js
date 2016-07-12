var score = -1;
var time = 30;
var timer;
var circles;



function setUpGame(){
	score++
	document.getElementById("score").innerHTML = "Score:" + score;
	circles = document.getElementsByClassName("circle");
	var red = Math.floor(Math.random()*256);
	var green = Math.floor(Math.random()*256);
	var blue = Math.floor(Math.random()*256);
	var color =   ("rgb(" + red + "," + green + "," +  blue + ")");
	for(i=0; i<circles.length; i++){
		circles[i].style.backgroundColor = color;
		circles[i].setAttribute("onclick","");
	}
	var selected = circles[Math.floor(Math.random()*circles.length)];
	var colorList = [red,green,blue];
	for(i = 0; i <colorList.length; i++){
		var offset = 0;
		while(offset < 30 && offset > -30){
			offset = Math.floor(Math.random()*100)-50;
		}
		colorList[i] += offset;
	}
	selected.style.backgroundColor = ("rgb(" + colorList[0] + "," + colorList[1] + "," +  colorList[2] + ")");
	selected.setAttribute("onclick","setUpGame();");
}


function begin(){
	timer = setInterval(countdown,1000);
	document.getElementById("score").style.visibility = "visible";
	document.getElementById("start").style.display = "none";
	setUpGame();
}


function countdown(){
	time--;
	if(time <= 0){
		finish();
	}else{
		document.getElementById("timer").innerHTML = time;
	}
}

function finish(){
	console.log("done.");
	clearInterval(timer);
	document.getElementById("timer").innerHTML = "Your score: " + score;
	score = -1;
	time = 30;
	for(i=0; i<circles.length; i++){
		circles[i].style.backgroundColor = "white";
		circles[i].setAttribute("onclick","");
	}
	document.getElementById("score").style.visibility = "hidden";
	document.getElementById("start").style.display = "initial";
	document.getElementById("start").innerHTML = "Press to restart";
}