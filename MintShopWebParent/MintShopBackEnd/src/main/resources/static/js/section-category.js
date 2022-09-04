var categoryIndex = 1;
var position = 1;

$(document).ready(function() {
	
	hideUsedCategories();
	
	$(".addCategoryLink").click(function(e){
		e.preventDefault();
		if($('#categoriesSelecet').val()){
			addCategoryToSelectedCategories(categoryIndex, position);
			categoryIndex++;
			position++;
		}
	});
	
	$(".deleteSelectedCategoryLink").click(function(e){
		e.preventDefault();
		if($('#selectedCategory').val()){
			deleteSelectedCategory(categoryIndex, position);
			position--;
		}
	});
	
	$(".levelUpCategoryLink").click(function(e){
		e.preventDefault();
		levelUpCategory();
	});
	
	$(".levelDownCategoryLink").click(function(e){
		e.preventDefault();
		levelDownCategory();
	});
	
	$( "#sectionForm" ).submit(function( event ) {
	  	sendCategoriesToController();
	});
	

});

function hideUsedCategories(){
	$("#selectedCategory > option").each(function() {
	    var categoryId = $(this).val();
	    var removedCat = $("#categoriesSelecet option[value='" + categoryId + "']");
	    var order = removedCat.attr("order");
	    var fullname = removedCat.text();
	    removedCat.remove();
	    $("#selectedCategory option[value='" + categoryId + "']").attr("order", order);
	    $("#selectedCategory option[value='" + categoryId + "']").attr("fullname", fullname);
	});
}

function addCategoryToSelectedCategories(categoryIndex, position){
	var selectedCategory = $("#categoriesSelecet").children(":selected");
	var categoryId = selectedCategory.val();
	var categoryOrder = selectedCategory.attr("order");
	var categoryFullName = selectedCategory.text();
	var category = selectedCategory.text().replace(/-/g, "");

	$('#selectedCategory').append($("<option value='" + categoryId +"' order='" + categoryOrder + "' fullName='"+categoryFullName+"'>" + category +"</option>"));
    $("#categoriesSelecet option[value='" + categoryId + "']").remove();
    
}

function deleteSelectedCategory(categoryIndex, position){
	var selectedCategory = $("#selectedCategory").children(":selected");
	var categoryId = selectedCategory.val();
	var categoryName = selectedCategory.attr("fullName");
	var categoryOrder = selectedCategory.attr("order");
	
	var option = "<option value='" + categoryId +"' order='" + categoryOrder + "'>" + categoryName +"</option>";
	var afterOption = $("#categoriesSelecet option[order='"+(categoryOrder-1)+"']");
	if(afterOption.length){
		$(option).insertAfter(afterOption);
	}
	else{
		$("#categoriesSelecet").append(option);
	}
	$("#selectedCategory option[value='" + categoryId + "']").remove();
}



function levelUpCategory(){
	var selectedCategory = $("#selectedCategory").children(":selected");
	var prevCategory = selectedCategory.prev('option');
	
	if(prevCategory.length){
		swapOptions(selectedCategory, prevCategory);
	}
}

function levelDownCategory(){
	var selectedCategory = $("#selectedCategory").children(":selected");
	var nextCategory = selectedCategory.next('option');
	
	if(nextCategory.length){
		swapOptions(selectedCategory, nextCategory);
	}
}

function swapOptions(option1, option2){
	var option1Val = option1.val();
	var option1Text = option1.text();
	var option1Fullname = option1.attr("fullname");
	var option1Order = option1.attr("order");
	
	var option2Val = option2.val();
	var option2Text = option2.text();
	var option2Fullname = option2.attr("fullname");
	var option2Order = option2.attr("order");
	
	option1.val(option2Val);
	option1.text(option2Text);
	option1.attr("fullname", option2Fullname);
	option1.attr("order", option2Order);
	option1.prop("selected", false);
	
	option2.val(option1Val);
	option2.text(option1Text);
	option2.attr("fullname", option1Fullname);
	option2.attr("order", option1Order);
	option2.prop("selected", true);
}
























