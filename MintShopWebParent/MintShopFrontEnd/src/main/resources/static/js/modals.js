function showModalDialog(title, message){
	$(".modal-title").text(title);
	$("#bodyMessage").text(message);
	$("#notifyModel").modal("show");
}

function showErrorModal(title, message){
	showModalDialog("Error", message);
}

function showWarningModal(title, message){
	showModalDialog("Warning", message);
}