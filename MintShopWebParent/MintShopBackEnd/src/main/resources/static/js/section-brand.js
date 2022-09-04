var position = 1;

$(document).ready(function() {
	
	hideUsedBrands();
	
	$(".addBrandLink").click(function(e){
		e.preventDefault();
		if($('#brandsSelecet').val()){
			addBrandToSelectedBrands(position);
			position++;
		}
	});
	
	$(".deleteSelectedBrandLink").click(function(e){
		e.preventDefault();
		if($('#selectedBrand').val()){
			deleteSelectedBrand(position);
			position--;
		}
	});
	
	$(".levelUpBrandLink").click(function(e){
		e.preventDefault();
		levelUp();
	});
	
	$(".levelDownBrandLink").click(function(e){
		e.preventDefault();
		levelDown();
	});
	
	$( "#sectionForm" ).submit(function( event ) {
	  	sendBrandsToController();
	});

});

function hideUsedBrands(){
	$("#selectedBrand > option").each(function() {
	    var id = $(this).val();
	    var removedOption = $("#brandsSelecet option[value='" + id + "']");
	    var order = removedOption.attr("order");
	    
	    removedOption.remove();
	    $("#selectedBrand option[value='" + id + "']").attr("order", order);
	});
}

function addBrandToSelectedBrands(position){
	var selectedBrand = $("#brandsSelecet").children(":selected");
	var id = selectedBrand.val();
	var order = selectedBrand.attr("order");
	var title = selectedBrand.text();

	$('#selectedBrand').append($("<option value='" + id +"' order='" + order + "'>" + title +"</option>"));
    $("#brandsSelecet option[value='" + id + "']").remove();
    
}

function deleteSelectedBrand(position){
	var selectedBrand = $("#selectedBrand").children(":selected");
	var id = selectedBrand.val();
	var title = selectedBrand.text();
	var order = selectedBrand.attr("order");
	
	var option = "<option value='" + id +"' order='" + order + "'>" + title +"</option>";
	var afterOption = $("#brandsSelecet option[order='"+(order-1)+"']");
	if(afterOption.length){
		$(option).insertAfter(afterOption);
	}
	else{
		$("#brandsSelecet").append(option);
	}
	$("#selectedBrand option[value='" + id + "']").remove();
}

function sendBrandsToController(){
	$('#selectedBrand').find("option").prop('selected', true);
}

function levelUp(){
	var selectedBrand = $("#selectedBrand").children(":selected");
	var prevArticle = selectedBrand.prev('option');
	
	if(prevArticle.length){
		swapOptions(selectedBrand, prevArticle);
	}
}

function levelDown(){
	var selectedBrand = $("#selectedBrand").children(":selected");
	var nextArticle = selectedBrand.next('option');
	
	if(nextArticle.length){
		swapOptions(selectedBrand, nextArticle);
	}
}

function swapOptions(option1, option2){
	var option1Val = option1.val();
	var option1Text = option1.text();
	var option1Order = option1.attr("order");
	
	var option2Val = option2.val();
	var option2Text = option2.text();
	var option2Order = option2.attr("order");
	
	option1.val(option2Val);
	option1.text(option2Text);
	option1.attr("order", option2Order);
	option1.prop("selected", false);
	
	option2.val(option1Val);
	option2.text(option1Text);
	option2.attr("order", option1Order);
	option2.prop("selected", true);
}
























