/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

//var ws = new WebSocket('ws://' + location.host + '/kurento');
//var ws = new WebSocket('ws://' + location.host + '/adventureProject/kurento');

//var ws = new WebSocket("ws://localhost:8080/adventureProject/websocketendpoint");

//desa
//var ws = new WebSocket("ws://localhost:8080/adventureProject/call");

//prod
	var ws = new WebSocket("ws://18.217.137.203:8080/call");
	if (location.origin.startsWith("http://localhost:8080")) {
			ws = new WebSocket("ws://localhost:8080/adventureProject/call");
		}else{
			ws = new WebSocket("ws://18.217.137.203:8080/call"); 
		}

console.log('ws://' + location.host);

//---------------------------------------
/*
let localStream;
let pc1;
let pc2;
const offerOptions = {
  offerToReceiveAudio: 1,
  offerToReceiveVideo: 1
};

const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');

localVideo.addEventListener('loadedmetadata', function() {
console.log(`Local video videoWidth: ${this.videoWidth}px,  videoHeight: ${this.videoHeight}px`);
});

remoteVideo.addEventListener('loadedmetadata', function() {
console.log(`Remote video videoWidth: ${this.videoWidth}px,  videoHeight: ${this.videoHeight}px`);
});

remoteVideo.addEventListener('resize', () => {
console.log(`Remote video size changed to ${remoteVideo.videoWidth}x${remoteVideo.videoHeight}`);
// We'll use the first onsize callback as an indication that video has started
// playing out.
if (startTime) {
  const elapsedTime = window.performance.now() - startTime;
  console.log('Setup time: ' + elapsedTime.toFixed(3) + 'ms');
  startTime = null;
}
});

let localStream;
let pc1;
let pc2;
const offerOptions = {
offerToReceiveAudio: 1,
offerToReceiveVideo: 1
};


function getName(pc) {
  return (pc === pc1) ? 'pc1' : 'pc2';
}

function getOtherPc(pc) {
  return (pc === pc1) ? pc2 : pc1;
}

async function start() {
	  console.log('Requesting local stream');
	  
	  try {
	    const stream = await navigator.mediaDevices.getUserMedia({audio: true, video: true});
	    console.log('Received local stream');
	    localVideo.srcObject = stream;
	    localStream = stream;
	    callButton.disabled = false;
	  } catch (e) {
	    alert(`getUserMedia() error: ${e.name}`);
	  }
	}

function getSelectedSdpSemantics() {
	  const sdpSemanticsSelect = document.querySelector('#sdpSemantics');
	  const option = sdpSemanticsSelect.options[sdpSemanticsSelect.selectedIndex];
	  return option.value === '' ? {} : {sdpSemantics: option.value};
	}
*/

//---------------------------------------

var videoInput;
var videoOutput;
var webRtcPeer;
var response;
var callerMessage;
var from;

var registerName = null;
var registerState = null;
const NOT_REGISTERED = 0;
const REGISTERING = 1;
const REGISTERED = 2;

function setRegisterState(nextState) {
	switch (nextState) {
	case NOT_REGISTERED:
		enableButton('#register', 'register()');
		setCallState(NO_CALL);
		break;
	case REGISTERING:
		disableButton('#register');
		break;
	case REGISTERED:
		disableButton('#register');
		setCallState(NO_CALL);
		break;
	default:
		return;
	}
	registerState = nextState;
}

var callState = null;
const NO_CALL = 0;
const PROCESSING_CALL = 1;
const IN_CALL = 2;

function setCallState(nextState) {
	switch (nextState) {
	case NO_CALL:
		enableButton('#call', 'call()');
		disableButton('#terminate');
		disableButton('#play');
		break;
	case PROCESSING_CALL:
		disableButton('#call');
		disableButton('#terminate');
		disableButton('#play');
		break;
	case IN_CALL:
		disableButton('#call');
		enableButton('#terminate', 'stop()');
		disableButton('#play');
		break;
	default:
		return;
	}
	callState = nextState;
}

window.onload = function() {
	//console = new Console();
	console.log("consola");
	setRegisterState(NOT_REGISTERED);
	var drag = new Draggabilly(document.getElementById('videoSmall'));
	videoInput = document.getElementById('videoInput');
	videoOutput = document.getElementById('videoOutput');
	document.getElementById('name').focus();
}

window.onbeforeunload = function() {
	ws.close();
}

ws.onmessage = function(message) {
	var str = message.data;
	str = str.substr(str.indexOf('{'));
	
	//var parsedMessage = JSON.parse(message.data);
	console.log('Received message: ' + message.data);
	
	var parsedMessage = JSON.parse(str);
	
	console.log('Received parse id: ' + parsedMessage.id);
	
	//if (message.data == 'Echo from the server : {"id":"registerResponse","name":"1"}')
	//	registerResponse(parsedMessage);
	
	switch (parsedMessage.id) {
	case 'registerResponse':
		registerResponse(parsedMessage);
		break;
	case 'callResponse':
		callResponse(parsedMessage);
		break;
	case 'incomingCall':
		incomingCall(parsedMessage);
		break;
	case 'startCommunication':
		startCommunication(parsedMessage);
		break;
	case 'stopCommunication':
		console.info('Communication ended by remote peer');
		stop(true);
		break;
	case 'iceCandidate':
		webRtcPeer.addIceCandidate(parsedMessage.candidate, function(error) {
			if (error)
				return console.error('Error adding candidate: ' + error);
		});
		break;
	
	///logica mia
	/*case 'register':
		var resp = {"id":"registerResponse","response":"accepted"};
		registerResponse(resp);
		break;
	
	case 'onIceCandidate':
		webRtcPeer.addIceCandidate(parsedMessage.candidate, function(error) {
			if (error)
				return console.error('Error adding candidate: ' + error);
		});
		break;
	case 'call':
		callState = NO_CALL;
		incomingCall(parsedMessage);
		break;*/
	
	default:
		console.error('Unrecognized message', parsedMessage);
	}
	
	wsGetMessage(message);
}
function wsGetMessage(message){
    console.log("Message received from to the server : " +message.data);
}

function registerResponse(message) {
	if (message.response == 'accepted') {
		setRegisterState(REGISTERED);
	} else {
		setRegisterState(NOT_REGISTERED);
		var errorMessage = message.message ? message.message
				: 'Unknown reason for register rejection.';
		console.log(errorMessage);
		alert('Error registering user. See console for further information.');
	}
}

function callResponse(message) {
	if (message.response != 'accepted') {
		console.info('Call not accepted by peer. Closing call');
		var errorMessage = message.message ? message.message
				: 'Unknown reason for call rejection.';
		console.log(errorMessage);
		stop();
	} else {
		setCallState(IN_CALL);
		webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
			if (error)
				return console.error(error);
		});
	}
}

function startCommunication(message) {
	setCallState(IN_CALL);
	webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
		if (error)
			return console.error(error);
	});
}

function incomingCall(message) {
	// If bussy just reject without disturbing user
	if (callState != NO_CALL) {
		var response = {
			id : 'incomingCallResponse',
			from : message.from,
			callResponse : 'reject',
			message : 'bussy'
		};
		return sendMessage(response);
	}

	setCallState(PROCESSING_CALL);
	if (confirm('User ' + message.from
			+ ' is calling you. Do you accept the call?')) {
		showSpinner(videoInput, videoOutput);

		from = message.from;
		var options = {
			localVideo : videoInput,
			remoteVideo : videoOutput,
			onicecandidate : onIceCandidate,
			onerror : onError
		}
		webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
				function(error) {
					if (error) {
						return console.error(error);
					}
					webRtcPeer.generateOffer(onOfferIncomingCall);
				});

	} else {
		var response = {
			id : 'incomingCallResponse',
			from : message.from,
			callResponse : 'reject',
			message : 'user declined'
		};
		sendMessage(response);
		stop();
	}
}

function onOfferIncomingCall(error, offerSdp) {
	if (error)
		return console.error("Error generating the offer");
	var response = {
		id : 'incomingCallResponse',
		from : from,
		callResponse : 'accept',
		sdpOffer : offerSdp
	};
	sendMessage(response);
}

function register() {
	var name = document.getElementById('name').value;
	if (name == '') {
		window.alert('You must insert your user name');
		return;
	}
	setRegisterState(REGISTERING);

	var message = {
		id : 'register',
		name : name
	};
	sendMessage(message);
	document.getElementById('peer').focus();
}

function call() {
	if (document.getElementById('peer').value == '') {
		window.alert('You must specify the peer name');
		return;
	}
	setCallState(PROCESSING_CALL);
	showSpinner(videoInput, videoOutput);

	var options = {
		localVideo : videoInput,
		remoteVideo : videoOutput,
		onicecandidate : onIceCandidate,
		onerror : onError
	}
	webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
			function(error) {
				if (error) {
					return console.error(error);
				}
				webRtcPeer.generateOffer(onOfferCall);
			});
}

function onOfferCall(error, offerSdp) {
	if (error)
		return console.error('Error generating the offer');
	console.log('Invoking SDP offer callback function');
	var message = {
		id : 'call',
		from : document.getElementById('name').value,
		to : document.getElementById('peer').value,
		sdpOffer : offerSdp
	};
	sendMessage(message);
}

function stop(message) {
	setCallState(NO_CALL);
	if (webRtcPeer) {
		webRtcPeer.dispose();
		webRtcPeer = null;

		if (!message) {
			var message = {
				id : 'stop'
			}
			sendMessage(message);
		}
	}
	hideSpinner(videoInput, videoOutput);
}

function onError() {
	setCallState(NO_CALL);
}

function onIceCandidate(candidate) {
	console.log("Local candidate" + JSON.stringify(candidate));

	var message = {
		id : 'onIceCandidate',
		candidate : candidate
	};
	sendMessage(message);
}

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	ws.send(jsonMessage);
}

function showSpinner() {
	for (var i = 0; i < arguments.length; i++) {
		arguments[i].poster = './img/transparent-1px.png';
		arguments[i].style.background = 'center transparent url("./img/spinner.gif") no-repeat';
	}
}

function hideSpinner() {
	for (var i = 0; i < arguments.length; i++) {
		arguments[i].src = '';
		arguments[i].poster = './img/webrtc.png';
		arguments[i].style.background = '';
	}
}

function disableButton(id) {
	$(id).attr('disabled', true);
	$(id).removeAttr('onclick');
}

function enableButton(id, functionName) {
	$(id).attr('disabled', false);
	$(id).attr('onclick', functionName);
}

/**
 * Lightbox utility (to display media pipeline image in a modal dialog)
 */
$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
	event.preventDefault();
	$(this).ekkoLightbox();
});