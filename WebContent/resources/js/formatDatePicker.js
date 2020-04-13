$(document).ready(function() {
	$( ".datepicker" ).datepicker({
		dateFormat: "yy-mm-dd",
		changeMonth: true,
		changeYear: true,
		yearRange: "2010:2024"
	});
});
$(document).ready(function(){
    $("#encuentrolid:to").datepicker({ dateFormat: 'yy-mm-dd' });
    $("#encuentrolid.from").datepicker({ dateFormat: 'yy-mm-dd' }).bind("change",function(){
        var minValue = $(this).val();
        minValue = $.datepicker.parseDate("yy-mm-dd", minValue);
        minValue.setDate(minValue.getDate()+1);
        $("#to").datepicker( "option", "minDate", minValue );
    });
});
