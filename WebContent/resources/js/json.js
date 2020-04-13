
$(document).ready(function() {
    //$('#lanzar_alerta').click(function() {
        //alert('hola mundo!');
        
        console.log("entra");
		
		var textString = '{"students":[' +
		  '{"firstName":"Sandy","lastName":"Sethi" },' +
		  '{"firstName":"Roxy","lastName":"Malik" },' +
		  '{"firstName":"Sunil","lastName":"Antil" }]}';
		 
		 var obj = JSON.parse(textString);
		 document.getElementById("testDemo").innerHTML = obj.students[0].firstName + " " + obj.students[0].lastName;
		 
		 var y="",j=0;
		 for ( j in obj.students) {
			 y += obj.students[j].firstName + " " + obj.students[j].lastName;
		 }
		 
		 document.getElementById("stu").innerHTML = y;
		 
		 
		 var myObj, i=0, x = "";
		 myObj = {
		   "name":"John",
		   "age":30,
		   "cars":[ "Ford", "BMW", "Fiat" ]
		 };

		 for (i in myObj.cars) {
		   x += myObj.cars[i] ;
		 }
		 document.getElementById("demo").innerHTML = x;
        
    //});
});

	
