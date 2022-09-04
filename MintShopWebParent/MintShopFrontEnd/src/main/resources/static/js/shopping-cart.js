var decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
var thousandSeparator = thousandPointType == 'COMMA' ? ',' : '.';

$(document).ready(function() {

	$(".btn-minus").on("click", function(evn) {
		evn.preventDefault();
		decreaseQuantity($(this));
		
	});
	
	$(".btn-plus").on("click", function(evn) {
		evn.preventDefault();
		increaseQuantity($(this));
	});
	
	$(".removeLink").on("click", function(evn) {
		evn.preventDefault();
		deleteProduct($(this));
	});

});

function deleteProduct(link){
	var url = link.attr("href");
	
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function(response){
		var rowNumber = link.attr("rowNumber");
		removeProductHtml(rowNumber);
		updateTotal();
		updateCountNumbers();
		if(response.startsWith("You")){
			showStackBarTop("error", "Error", response);
		}
		else{
			showStackBarTop("success", "Product Deleted", response);
		}
	})
	.fail(function() {
		showStackBarTop("error", "Error", "Error while removing product.");
	});
}

function removeProductHtml(rowNumber){
	$("#row" + rowNumber).remove();
}

function decreaseQuantity(link){
	var productId = link.attr("pid");
	var quantityValue = $("#quantity"+productId);
	var newQuantity = parseInt(quantityValue.val()) - 1;
	
	if(newQuantity > 0){
		quantityValue.val(newQuantity);
		updateQuantity(productId, newQuantity);
	}
	else{
		showModal("Warning", "Minimum Quantity is 1");
		
	}
}	

function updateCountNumbers(){
	$(".divCount").each(function(index, element){
		element.innerHTML = ""+(index + 1);
	});
}

function increaseQuantity(link){
	var productId = link.attr("pid");
	var quantityValue = $("#quantity"+productId);
	var newQuantity = parseInt(quantityValue.val()) + 1;
	
	if(newQuantity <= 5){
		quantityValue.val(newQuantity);
		updateQuantity(productId, newQuantity);
	}
	else{
		showModal("Warning", "Maximum Quantity is 5");
		
	}
}

function showModal(title, message){
		$(".modal-title").text(title);
		$("#bodyMessage").text(message);
		$("#notifyModel").modal('show');
}

function updateQuantity(productId, quantity){
	var url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function(newSubTotal){
		updateSubTotal(newSubTotal,productId);
		updateTotal();
	})
	.fail(function() {
		showStackBarTop("error", "Error", "Error while updating product quantity.");
	});
}

function updateSubTotal(newSubTotal, productId){
	var formatedSubTotal = formatCurrency(newSubTotal);
	$("#subTotal"+productId).text(formatedSubTotal);
}

function updateTotal(){
	var total = 0.0;
	var productCount = 0;
	
	$(".subTotal").each(function (index, element) {
		productCount ++;
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
	});
	
	if(productCount < 1){
		showEmptyShoppingCart();
	}
	else{
		var formattedTotal = formatCurrency(total);
		$("#total").text(formattedTotal);
	}
	
	
}


function showEmptyShoppingCart() {
	$("#totalSection").hide();
	$("#emptySection").removeClass("d-none");
}

function formatCurrency(amount){
	return $.number(amount, decimalDigits, decimalSeparator, thousandSeparator);
}

function clearCurrencyFormat(numberString){
	var result = numberString.replaceAll(thousandSeparator, "");
	return result.replaceAll(decimalSeparator, ".");
}

















