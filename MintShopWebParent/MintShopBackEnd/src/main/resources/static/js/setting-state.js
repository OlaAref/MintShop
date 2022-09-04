var loadCountriesForStatesButton;
var dropDownCountriesForStates;
var dropDownStates;
var addStateButton;
var updateStateButton;
var deleteStateButton;
var fieldStateName, labelStateName;

$(document).ready(function() {
	loadCountriesForStatesButton = $("#loadCountriesForStatesButton");
	dropDownCountriesForStates = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	
	addStateButton = $("#addStateButton");
	updateStateButton = $("#updateStateButton");
	deleteStateButton = $("#deleteStateButton");
	
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	loadCountriesForStatesButton.click(function() {
		loadCountries4States();	
	});
	
	dropDownCountriesForStates.on("change", function() {
		loadStates();
	});
	
	dropDownStates.on("change", function() {
		changeFormToSelectedState();
	});
	
	addStateButton.click(function() {
		if(addStateButton.val() == 'Add'){
			addState();
		}
		else {
			changeFormToNewState();
		}
	});
	
	updateStateButton.click(function() {
		updateState();	
	});
	
	deleteStateButton.click(function() {
		deleteState();	
	});
	
});


function loadCountries4States(){
	var url = contextPath + "countries/list";
	$.get(url, function(jsonResponse) {
		dropDownCountriesForStates.empty();
		
		$.each(jsonResponse, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
		});
	})
	.done(function() {
		loadCountriesForStatesButton.val("Refresh Country List");
		showToastSuccessMessage("All Countries have been loaded.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
}

function validateFormState() {

	var formState = document.getElementById("formState");
	if(!formState.checkValidity()){
		formState.reportValidity();
		return false;
	}
	return true;
}

function addState() {
	if(!validateFormState()) return;
	
	var url = contextPath + "states/save";
	
	var stateName = fieldStateName.val();
	var selectedCountry = $("#dropDownCountriesForStates option:selected");
	var countryId = selectedCountry.val();
	var countryName = selectedCountry.text();
	
	var parameters = {
		name: stateName,
		country: {
			id: countryId,
			name: countryName
		}
	};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(parameters),
		contentType: "application/json"
	})
	.done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		showToastSuccessMessage("The new State has been added.");
	});
	
	
}
function updateState() {
	if(!validateFormState()) return;

	var url = contextPath + "states/save";
	
	var stateId = dropDownStates.val();
	var stateName = fieldStateName.val();
	var selectedCountry = $("#dropDownCountriesForStates option:selected");
	var countryId = selectedCountry.val();
	var countryName = selectedCountry.text();
	
	var parameters = {
		id: stateId,
		name: stateName,
		country: {
			id: countryId,
			name: countryName
		}
	};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(parameters),
		contentType: "application/json"
	})
	.done(function(stateId) {
		$("#dropDownStates option:selected").text(stateName);
		showToastSuccessMessage("The state has been updated.");
		changeFormToNewState();
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
	
}

function deleteState() {
	var stateId = dropDownStates.val();
	var url = contextPath + "states/delete/"+stateId;
	
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function() {
		$("#dropDownStates option[value='"+ stateId +"']").remove();
		changeFormToNewState();
		showToastSuccessMessage("The state has been deleted.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});

}

function loadStates(){
	var selectedCountry = $("#dropDownCountriesForStates option:selected");
	var countryId = selectedCountry.val();
	var url = contextPath + "states/list/"+countryId;
	
	$.get(url, function(jsonResponse) {
		dropDownStates.empty();
		
		$.each(jsonResponse, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	})
	.done(function() {
		changeFormToNewState();
		showToastSuccessMessage("All states have been loaded for country "+ selectedCountry.text() +".");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
}

function selectNewlyAddedState(stateId, stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	$("#dropDownStates option[value='"+ stateId +"']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormToSelectedState(){
	addStateButton.prop("value", "New");
	updateStateButton.prop("disabled", false);
	deleteStateButton.prop("disabled", false);
	
	labelStateName.text("Selected State/Province: ");
	
	var selectedStateName = $("#dropDownStates option:selected").text();
	
	fieldStateName.val(selectedStateName);

}

function changeFormToNewState(){
	addStateButton.val("Add");
	addStateButton.text("Add");
	labelStateName.text("State/Provinces Name: ");
	
	updateStateButton.prop("disabled", false);
	deleteStateButton.prop("disabled", false);
	
	fieldStateName.val("").focus();
}

















