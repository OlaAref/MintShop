var modalMessage;
var confirmModal;
var yesButton;
var noButton;
var iconName = {
	'PICKED' : 'fa-people-carry',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-undo'
};

$(document).ready(function() {
	
	modalMessage = $("#modalMessage");
	confirmModal = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");
	
	$(".updateStatusLink").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	});
	
	handlerForYesButton();
});

function handlerForYesButton(){
	
	yesButton.click(function(e){
		e.preventDefault();
		requestToUpdateOrderStatus($(this));
	});
	
}

function requestToUpdateOrderStatus(button){
	
	requestUrl = button.attr("href");
	
	$.ajax({
		type: "POST",
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function(response){
		showMessageModal("Order Updated Successfully.");
		updateIconColor(response.orderId, response.status);
	})
	.fail(function(err){
		showMessageModal("Error while updating order status.");
	});
	
}

function updateIconColor(orderId, status){
	linkIcon = $("#link"+status+orderId);
	linkIcon.replaceWith("<i class='fas "+iconName[status]+" fa-2x text-success'></i>");
}

function showUpdateConfirmModal(link){
	yesButton.show();
	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href", link.attr("href"));
	
	modalMessage.text("Are you sure you want to update status of the order ID #"+
						orderId+" to "+status+" ?");
	confirmModal.modal("show");
}

function showMessageModal(message){
	noButton.text("Close");
	yesButton.hide();
	modalMessage.text(message);
}