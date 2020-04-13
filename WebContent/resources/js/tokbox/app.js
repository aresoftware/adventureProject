//credentials
var apiKey = '45828062';
var sessionId = '1_MX40NTgyODA2Mn5-MTU4NjM3Nzk0MTY2OH5sK0pZWXArVjN4dldBRTJYcy8wQmJzeUF-UH4';
var token = 'T1==cGFydG5lcl9pZD00NTgyODA2MiZzaWc9MmYyYWQwZDRhYjU4YjcxYjg4MDY0N2FiN2NkYmQ0MmY2MzJjMjJmYzpzZXNzaW9uX2lkPTFfTVg0ME5UZ3lPREEyTW41LU1UVTROak0zTnprME1UWTJPSDVzSzBwWldYQXJWak40ZGxkQlJUSlljeTh3UW1KemVVRi1VSDQmY3JlYXRlX3RpbWU9MTU4NjM3ODAwNCZub25jZT0wLjQ3NzgxOTU2MzMyNzQ2MzM0JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1ODY0NjQ0MDQ=';


// (optional) add server code here
initializeSession();

//Handling all of our errors here by alerting them
function handleError(error) {
  if (error) {
    alert(error.message);
  }
}

function initializeSession() {
  var session = OT.initSession(apiKey, sessionId);

  // Subscribe to a newly created stream

  // Create a publisher
  var publisher = OT.initPublisher('publisher', {
    insertMode: 'append',
    width: '100%',
    height: '100%'
  }, handleError);

  // Connect to the session
  session.connect(token, function(error) {
    // If the connection is successful, publish to the session
    if (error) {
      handleError(error);
    } else {
      session.publish(publisher, handleError);
    }
  });
  
  
  session.on('streamCreated', function(event) {
	  session.subscribe(event.stream, 'subscriber', {
	    insertMode: 'append',
	    width: '100%',
	    height: '100%'
	  }, handleError);
	});
}





