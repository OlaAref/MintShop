$(document).ready(function(){
	$(".anonymousLink").on("click", function(e) {
		e.preventDefault();
		showModalDialog("Login Required", "Please login first to post your question.");
	});
			
	$(".linkVoteQuestion").click(function(e){
		e.preventDefault();
		voteQuestion($(this));
	});
	
	$(".questionFormButton").click(function(e){
		e.preventDefault();
		var contentInput = document.getElementById("questionContent");
		var isValid = contentInput.checkValidity();
		if(isValid){
			saveQuestion($(this));
			contentInput.value = "";
		}
		
	});
});

function saveQuestion(currentLink){
	
	var productId = $("#productId").val();
	var questionContent = $("#questionContent").val();
	var url = currentLink.attr("saveLink");
	
	$.ajax({
		type: "POST",
		url: url,
		data: {
			productId: productId,
			questionContent: questionContent
		},
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	})
	.done(function(message){
		showModalDialog("Question Posted", message);
	})
	.fail(function(){
		showStackBarTop("error", "Error", "Error while posting your question.");
	});
}

function voteQuestion(currentLink){
	var url = currentLink.attr("href");
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function(voteResult){
		
		if(voteResult.successful){
			$("#notifyModel").on("hide.bs.modal", function(e){
				updateVoteCountAndIcons(currentLink, voteResult);
			});
		}
		
		showModalDialog("Vote Question", voteResult.message);
	})
	.fail(function(){
		showStackBarTop("error", "Error", "Error while voting the question.");
	});
}

function updateVoteCountAndIcons(currentLink, voteResult){
	var questionId = currentLink.attr("questionId");
	var voteUpLink = $("#voteUpLink-" + questionId);
	var voteDownLink = $("#voteDownLink-" + questionId);
	var thumbsUpIcon = $("#thumbsUpIcon-" + questionId);
	var thumbsDownIcon = $("#thumbsDownIcon-" + questionId);
	var voteCount = $("#voteCount-" + questionId);
	
	//update vote count
	voteCount.text(voteResult.voteCount);
	
	//change icon
	var message = voteResult.message;
	if(message.includes("successfully voted up")){
		highlightVoteUpIcon(currentLink, thumbsUpIcon, thumbsDownIcon);
	}
	else if(message.includes("successfully voted down")){
		highlightVoteDownIcon(currentLink, thumbsUpIcon, thumbsDownIcon);
	}
	else if(message.includes("unvoted up")){
		unhighlightVoteUpIcon(currentLink, thumbsUpIcon);
	}
	else if(message.includes("unvoted down")){
		unhighlightVoteDownIcon(currentLink, thumbsDownIcon);
	}
}

function highlightVoteUpIcon(voteUpLink, thumbsUpIcon, thumbsDownIcon){
	voteUpLink.attr("title", "Undo vote up");
	thumbsUpIcon.addClass("text-warning");
	thumbsDownIcon.removeClass("text-warning");
}

function highlightVoteDownIcon(voteDownLink, thumbsUpIcon, thumbsDownIcon){
	voteDownLink.attr("title", "Undo vote down");
	thumbsDownIcon.addClass("text-warning");
	thumbsUpIcon.removeClass("text-warning");
}

function unhighlightVoteUpIcon(voteUpLink, thumbsUpIcon){
	voteUpLink.attr("title", "Vote up");
	thumbsUpIcon.removeClass("text-warning");
}

function unhighlightVoteDownIcon(voteDownLink, thumbsDownIcon){
	voteDownLink.attr("title", "Vote down");
	thumbsDownIcon.removeClass("text-warning");
}














