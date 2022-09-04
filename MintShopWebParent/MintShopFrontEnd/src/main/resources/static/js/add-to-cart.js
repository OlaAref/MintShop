$(document).ready(function() {

	$("#addToCartBtn").on("click", function(evt) {
		addToCart();
	});
	
});

function addToCart(){
	var quantity = $("#quantity"+productId).val();
	var url = contextPath + "cart/add/" + productId + "/" + quantity;
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function(response){
		if(response.startsWith("You")){
			showStackBarTop("error", "Shopping Cart", response);
		}
		else if(response.startsWith("Could")){
			showStackBarTop("error", "Shopping Cart", response);
		}
		else{
			showStackBarTop("success", "Shopping Cart", response);
		}
		
	})
	.fail(function() {
		showStackBarTop("error", "Error", "Error while adding product to shopping cart.");
	});
	
}