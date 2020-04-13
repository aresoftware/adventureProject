
function validar_email( email ) 
{
    var regex = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email) ? true : false;
}

function validateEmail(email) {
	  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  return re.test(email);
	}

	function validate() {
	  var $result = $("#result");
	  var email = $("#email").val();
	  $result.text("");

	  if (validateEmail(email)) {
	    $result.text(email + " is valid :)");
	    $result.css("color", "green");
	  } else {
	    $result.text(email + " is not valid :(");
	    $result.css("color", "red");
	  }
	  return false;
	}

	$("#validate").on("click", validate);


    //<![CDATA[
    /**
     * Faces Validator
     */
    PrimeFaces.validator['custom.emailValidator'] = {
         
        pattern: /\S+@\S+/,
         
        validate: function(element, value) {
            //use element.data() to access validation metadata, in this case there is none.
            if(!this.pattern.test(value)) {
                throw {
                    summary: 'Validation Error',
                    detail: value + ' is not a valid email.'
                }
            }
        }
    };
     
    /**
     * Bean validator
     */
    PrimeFaces.validator['Email'] = {
         
        pattern: /\S+@\S+/,
                 
        MESSAGE_ID: 'org.primefaces.examples.validate.email.message',
         
        validate: function(element, value) {
            var vc = PrimeFaces.util.ValidationContext;
     
            if(!this.pattern.test(value)) {
                var msgStr = element.data('p-email-msg'),
                msg = msgStr ? {summary:msgStr, detail: msgStr} : vc.getMessage(this.MESSAGE_ID);
         
                throw msg;
            }
        }
    };
    //]]>