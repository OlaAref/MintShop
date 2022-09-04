$(document).ready(function(){
	$(".linkVoteReview").click(function(e){
		e.preventDefault();
		voteReview($(this));
	});
});

function voteReview(currentLink){
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
		
		showModalDialog("Vote Review", voteResult.message);
	})
	.fail(function(){
		showStackBarTop("error", "Error", "Error while voting the review.");
	});
}

function updateVoteCountAndIcons(currentLink, voteResult){
	var reviewId = currentLink.attr("reviewId");
	var voteUpLink = $("#voteUpLink-" + reviewId);
	var voteDownLink = $("#voteDownLink-" + reviewId);
	var thumbsUpIcon = $("#thumbsUpIcon-" + reviewId);
	var thumbsDownIcon = $("#thumbsDownIcon-" + reviewId);
	var voteCount = $("#voteCount-" + reviewId);
	
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
	thumbsUpIcon.removeClass("far").addClass("fas");
	thumbsDownIcon.removeClass("fas").addClass("far");
}

function highlightVoteDownIcon(voteDownLink, thumbsUpIcon, thumbsDownIcon){
	voteDownLink.attr("title", "Undo vote down");
	thumbsDownIcon.removeClass("far").addClass("fas");
	thumbsUpIcon.removeClass("fas").addClass("far");
}

function unhighlightVoteUpIcon(voteUpLink, thumbsUpIcon){
	voteUpLink.attr("title", "Vote up");
	thumbsUpIcon.removeClass("fas").addClass("far");
}

function unhighlightVoteDownIcon(voteDownLink, thumbsDownIcon){
	voteDownLink.attr("title", "Vote down");
	thumbsDownIcon.removeClass("fas").addClass("far");
}














