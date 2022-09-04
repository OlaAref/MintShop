var fieldProductCost;
var fieldShippingCost;
var fieldSubtotal;
var fieldTax;
var fieldTotal;

$(document).ready(function() {
	fieldProductCost = $("#productCostInput");
	fieldShippingCost = $("#shippingCostInput");
	fieldSubtotal = $("#subTotalInput");
	fieldTax = $("#taxInput");
	fieldTotal = $("#totalInput");
	
	formatOrderAmounts();
	formatProductAmounts();
	
	
	$("#productList").on("change", ".quantity-input", function(e) {
		updateSubTotalWhenQuantityChanged($(this));
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".price-input", function(e) {
		updateSubTotalWhenPriceChanged($(this));
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".cost-input", function(e) {
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".shipping-input", function(e) {
		updateOrderAmounts();
	});
});

function getNumberWithoutSeparator(fieldRef) {
	return parseFloat(fieldRef.val().replace(",", ""));
}

function setNumberForField(id, value) {
	$("#"+id).val($.number(value, 2, '.', ','));
}

function updateOrderAmounts(){

	//total product cost
	var totalCost = 0.0;
	
	$(".cost-input").each(function(e) {
		costInputFiels = $(this);
		rowNumber = costInputFiels.attr("rowNumber");
		
		quantityValue = $("#quantity"+rowNumber).val();
		productCost = getNumberWithoutSeparator(costInputFiels);
		
		totalCost += productCost * parseFloat(quantityValue) ;
	});
	
	setNumberForField("productCostInput", totalCost);
	
	//subtotal cost
	var orderSubtotal = 0.0;
	
	$(".subtotal-output").each(function(e) {
		productSubtotal = getNumberWithoutSeparator($(this));
		orderSubtotal += productSubtotal;
	});
	
	setNumberForField("subTotalInput", orderSubtotal);
	
	//shipping cost
	var shippingCost = 0.0;
	
	$(".shipping-input").each(function(e) {
		productShipping = getNumberWithoutSeparator($(this));
		shippingCost += productShipping;
	});
	
	setNumberForField("shippingCostInput", shippingCost);
	
	//tax
	var tax = getNumberWithoutSeparator(fieldTax);
	
	//total
	orderTotal = orderSubtotal + tax + shippingCost;
	
	setNumberForField("totalInput", orderTotal);
}

function updateSubTotalWhenPriceChanged(priceInput){
	var priceValue = getNumberWithoutSeparator(priceInput);
	var rowNumber = priceInput.attr("rowNumber");
	
	var quantityField = $("#quantity"+rowNumber);
	var quantityValue = parseFloat(quantityField.val());
	
	var newSubtotal = parseFloat(quantityValue) * priceValue;
	
	setNumberForField("subtotal"+rowNumber, newSubtotal);
}

function updateSubTotalWhenQuantityChanged(quantityInput){
	var quantityValue = quantityInput.val();
	var rowNumber = quantityInput.attr("rowNumber");
	
	var priceField = $("#price"+rowNumber);
	var priceValue = getNumberWithoutSeparator(priceField);
	var newSubtotal = parseFloat(quantityValue) * priceValue;

	setNumberForField("subtotal"+rowNumber, newSubtotal);
}

function formatProductAmounts(){
	$(".cost-input").each(function() {
		formatNumer($(this));
	});
	$(".price-input").each(function() {
		formatNumer($(this));
	});
	$(".subtotal-output").each(function() {
		formatNumer($(this));
	});
	$(".shipping-input").each(function() {
		formatNumer($(this));
	});
}

function formatOrderAmounts(){
	formatNumer(fieldProductCost);
	formatNumer(fieldShippingCost);
	formatNumer(fieldSubtotal);
	formatNumer(fieldTax);
	formatNumer(fieldTotal);
}

function formatNumer(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2, '.', ','));
	
}

function processFormBeforeSubmit(){
	setCountryName();

	//remove thousand separator for order-form-overview
	removeThousandSeparator(fieldProductCost);
	removeThousandSeparator(fieldShippingCost);
	removeThousandSeparator(fieldSubtotal);
	removeThousandSeparator(fieldTax);
	removeThousandSeparator(fieldTotal);
	
	//remove thousand separator for order-form-products
	$(".cost-input").each(function(e) {
		removeThousandSeparator($(this));
	});
	$(".price-input").each(function(e) {
		removeThousandSeparator($(this));
	});
	$(".subtotal-output").each(function(e) {
		removeThousandSeparator($(this));
	});
	$(".shipping-input").each(function(e) {
		removeThousandSeparator($(this));
	});
	
}

function removeThousandSeparator(field){
	field.val(field.val().replace(",", ""));
}

function setCountryName(){
	selectedCountry = $("#countrySelecet option:selected");
	countryName = selectedCountry.text();
	$("#countryName").val(countryName);
}