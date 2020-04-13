//let Peer = require(['simple-peer']);
const video = document.querySelector('video');
let client = {};

var ws = new WebSocket("ws://18.217.137.203:8080/websocketendpoint");
if (location.origin.startsWith("http://localhost:8080")) {
		ws = new WebSocket("ws://localhost:8080/adventureProject/websocketendpoint");
	}else{
		ws = new WebSocket("ws://18.217.137.203:8080/call"); 
	}

console.log('ws://' + location.host);

window.onload = function() {
	//console = new Console();
	console.log("consola");

}

window.onbeforeunload = function() {
	ws.close();
}

var mystream = "";

navigator.mediaDevices.getUserMedia({video: true, audio: true})
.then(stream => {
	mystream = stream;
	register();
	video.srcObject = stream;
	video.play();
	
})
.catch(err => document.write(err))


ws.onmessage = function(message) {
	var str = message.data;
	str = str.substr(str.indexOf('{'));
	
	//var parsedMessage = JSON.parse(message.data);
	console.log('Received message: ' + message.data);
	
	var parsedMessage = JSON.parse(str);
	
	console.log('Received parse id: ' + parsedMessage.id);
	
	switch (parsedMessage.id) {
	case 'CreatePeer':
		MakePeer();
		break;
	case 'BackOffer':
		FrontAnswer(parsedMessage.response);
		break;
	case 'BackAnswer':
		SignalAnswer(parsedMessage.response);
		break;
		
	default:
		console.error('Unrecognized message', parsedMessage);
	}
	
}

function register() {
	var name = document.getElementById('name').value;
	
	var message = {
		id : 'NewClient',
		name : name
	};
	sendMessage(message);
}

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	ws.send(jsonMessage);
}

//initialize a peer
function InitPeer(type){
	let peer = new Peer({iniciator:(type == 'init') ? true : false, mystream:mystream, trickle:false})
	//var peer = new Peer({key: 'lwjd5qra8257b9'});
	
	peer.on('mystream', function(mystream){
		CreateVideo(mystream)
	})
	peer.on('close', function(){
		document.getElementById("peerVideo").remove();
		peer.destroy()
	})
	return peer
}

//for peer of type init
function MakePeer(){
	client.gotAnswer = false
	let peer = InitPeer('init')
	
	var message = {
				id : 'Offer',
				stream : mystream
			};
			sendMessage(message);
	/*
	peer.on('signal', (data)=>{
		if(!client.gotAnswer){
			//socket.emit('Offer', data)
			var message = {
				id : 'Offer',
				name : data
			};
			sendMessage(message);
		}
	})*/
	client.peer = peer
}
//type not init
function FrontAnswer(offer){
	let peer = InitPeer('notInit')
	peer.on('signal',(data) => {
		//socket.emit('Answer', data)
		var message = {
				id : 'Answer',
				name : data
		};
		sendMessage(message);
	})
	peer.signal(offer)
}

function SignalAnswer(answer){
	client.gotAnswer = true
	let peer = client.peer
	peer.signal(answer)
}

function CreateVideo(stream){
	let video = document.createElement('video')
	video.id = 'peerVideo'
	video.srcObject = stream
	video.class = 'embed-responsive-item'	
	document.querySelector('#peerDiv').appendChild(video)
	video.play()
}

function SeeActive(){
	document.write('Sesion active please come back later')
}