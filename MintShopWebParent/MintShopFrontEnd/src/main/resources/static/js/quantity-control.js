$(document).ready(function() {

	$(".btn-minus").on("click", function(evn) {
		evn.preventDefault();
		var productId = $(this).attr("pid");
		var quantityValue = $("#quantity"+productId);
		var newQuantity = parseInt(quantityValue.val()) - 1;
		
		if(newQuantity > 0){
			quantityValue.val(newQuantity);
		}
		else{
			showModal("Warning", "Minimum Quantity is 1");
			
		}
		
	});
	
	$(".btn-plus").on("click", function(evn) {
		evn.preventDefault();
		var productId = $(this).attr("pid");
		var quantityValue = $("#quantity"+productId);
		var newQuantity = parseInt(quantityValue.val()) + 1;
		
		if(newQuantity <= 5){
			quantityValue.val(newQuantity);
		}
		else{
			showModal("Warning", "Maximum Quantity is 5");
			
		}
	});

});

function showModal(title, message){
		$(".modal-title").text(title);
		$("#bodyMessage").text(message);
		$("#notifyModel").modal('show');
}