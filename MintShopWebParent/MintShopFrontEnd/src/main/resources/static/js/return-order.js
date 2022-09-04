var returnOrderModal;
var returnOrderModalTitle;
var returnNote;
var orderId;
var divReason;
var divMessage;
var firstButton;
var secondButton;

$(document).ready(function() {
	
	returnOrderModal = $("#returnOrderModal");
	returnOrderModalTitle = $("#returnOrderModalTitle");
	returnNote = $("#returnNote");
	divReason = $("#divReason");
	divMessage = $("#divMessage");
	firstButton = $("#firstButton");
	secondButton = $("#secondButton");
	
	handleReturnOrderLink();
});

function showReturnModalDialog(link){
	divReason.show();
	divMessage.hide();
	firstButton.show();
	secondButton.text("Cancel");
	returnNote.val("");
	
	orderId = link.attr("orderId");
	returnOrderModal.modal('show');
	returnOrderModalTitle.text("Return Order ID #" + orderId);
}

function showMessageModal(message){
	divReason.hide();
	divMessage.text(message);
	firstButton.hide();
	secondButton.text("Close");
	returnNote.val("");
	
	divMessage.show();
}

function handleReturnOrderLink(){
	$(".return-link").on("click", function(e) {
		e.preventDefault();
		showReturnModalDialog($(this));
	});
}

function submitReturnOrderForm(){
	reason = $("input[name='returnReason']:checked").val();
	note = returnNote.val();
	
	sendOrderReturnRequest(reason, note);
	
	return false;
}

function sendOrderReturnRequest(reason, note){
	
	requestUrl = contextPath + "orders/return";
	
	requestBody = {
		orderId: orderId,
		reason: reason,
		note: note
	};
	
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	})
	.done(function(response) {
		showMessageModal("Return request has been sent ");
		updateStatusAndHideReturn(orderId);
	})
	.fail(function(err) {
		showMessageModal(err.messageText);
	});
	
}

function updateStatusAndHideReturn(orderId){
	$(".textOrderStatus"+orderId).each(function(index) {
		$(this).text("RETURN_REQUESTED");
	});
	
	$(".linkReturn"+orderId).each(function(index) {
		$(this).hide();
	});
}






















