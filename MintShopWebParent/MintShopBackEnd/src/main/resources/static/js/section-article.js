var position = 1;

$(document).ready(function() {
	
	hideUsedArticles();
	
	$(".addArticleLink").click(function(e){
		e.preventDefault();
		if($('#articlesSelecet').val()){
			addArticleToSelectedArticles(position);
			position++;
		}
	});
	
	$(".deleteSelectedArticleLink").click(function(e){
		e.preventDefault();
		if($('#selectedArticle').val()){
			deleteSelectedArticle(position);
			position--;
		}
	});
	
	$(".levelUpArticleLink").click(function(e){
		e.preventDefault();
		levelUp();
	});
	
	$(".levelDownArticleLink").click(function(e){
		e.preventDefault();
		levelDown();
	});
	
	$( "#sectionForm" ).submit(function( event ) {
	  	sendCategoriesToController();
	});

});

function hideUsedArticles(){
	$("#selectedArticle > option").each(function() {
	    var id = $(this).val();
	    var removedOption = $("#articlesSelecet option[value='" + id + "']");
	    var order = removedOption.attr("order");
	    
	    removedOption.remove();
	    $("#selectedArticle option[value='" + id + "']").attr("order", order);
	});
}

function addArticleToSelectedArticles(position){
	var selectedArticle = $("#articlesSelecet").children(":selected");
	var id = selectedArticle.val();
	var order = selectedArticle.attr("order");
	var title = selectedArticle.text();

	$('#selectedArticle').append($("<option value='" + id +"' order='" + order + "'>" + title +"</option>"));
    $("#articlesSelecet option[value='" + id + "']").remove();
    
}

function deleteSelectedArticle(position){
	var selectedArticle = $("#selectedArticle").children(":selected");
	var id = selectedArticle.val();
	var title = selectedArticle.text();
	var order = selectedArticle.attr("order");
	
	var option = "<option value='" + id +"' order='" + order + "'>" + title +"</option>";
	var afterOption = $("#articlesSelecet option[order='"+(order-1)+"']");
	if(afterOption.length){
		$(option).insertAfter(afterOption);
	}
	else{
		$("#articlesSelecet").append(option);
	}
	$("#selectedArticle option[value='" + id + "']").remove();
}

function sendCategoriesToController(){
	$('#selectedArticle').find("option").prop('selected', true);
}

function levelUp(){
	var selectedArticle = $("#selectedArticle").children(":selected");
	var prevArticle = selectedArticle.prev('option');
	
	if(prevArticle.length){
		swapOptions(selectedArticle, prevArticle);
	}
}

function levelDown(){
	var selectedArticle = $("#selectedArticle").children(":selected");
	var nextArticle = selectedArticle.next('option');
	
	if(nextArticle.length){
		swapOptions(selectedArticle, nextArticle);
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
























