var loadCountriesButton;
var dropDownCountries;
var addCountryButton;
var updateCountryButton;
var deleteCountryButton;
var fieldCountryName, labelCountryName;
var fieldCountryCode;

$(document).ready(function() {
	loadCountriesButton = $("#loadCountriesButton");
	dropDownCountries = $("#dropDownCountries");
	
	addCountryButton = $("#addCountryButton");
	updateCountryButton = $("#updateCountryButton");
	deleteCountryButton = $("#deleteCountryButton");
	
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	loadCountriesButton.click(function() {
		loadCountries();	
	});
	
	dropDownCountries.on("change", function() {
		changeFormToSelectedCountry();
	});
	
	addCountryButton.click(function() {
		if(addCountryButton.val() == 'Add'){
			addCountry();
		}
		else {
			changeFormToNew();
		}
	});
	
	updateCountryButton.click(function() {
		updateCountry();	
	});
	
	deleteCountryButton.click(function() {
		deleteCountry();	
	});
	
});

function loadCountries(){
	var url = contextPath + "countries/list";
	$.get(url, function(jsonResponse) {
		dropDownCountries.empty();
		
		$.each(jsonResponse, function(index, country) {
			var optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
		});
	})
	.done(function() {
		loadCountriesButton.val("Refresh Country List");
		showToastSuccessMessage("All Countries have been loaded.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
}

function validateFormCountry() {

	var formCountry = document.getElementById("formCountry");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
	}
	return true;
}

function addCountry() {
	if(!validateFormCountry()) return;

	var url = contextPath + "countries/save";
	var countryName = fieldCountryName.val();
	var countryCode = fieldCountryCode.val();
	
	var parameters = {
		name: countryName,
		code: countryCode
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
	.done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryName, countryCode);
		showToastSuccessMessage("The new country has been added.");
	});
	
	
}
function updateCountry() {
	if(!validateFormCountry()) return;
	
	var url = contextPath + "countries/save";
	
	var countryId = dropDownCountries.val().split("-")[0];
	var countryName = fieldCountryName.val();
	var countryCode = fieldCountryCode.val();
	
	var parameters = {
		id: countryId,
		name: countryName,
		code: countryCode
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
	.done(function(countryId) {
		$("#dropDownCountries option:selected").text(countryName);
		$("#dropDownCountries option:selected").val(countryId+"-"+countryCode);
		showToastSuccessMessage("The country has been updated.");
		changeFormToNew();
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
	
}

function deleteCountry() {
	var optionValue = dropDownCountries.val();
	var countryId = optionValue.split("-")[0];
	var url = contextPath + "countries/delete/"+countryId;
	
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function() {
		$("#dropDownCountries option[value='"+ optionValue +"']").remove();
		changeFormToNew();
		showToastSuccessMessage("The country has been deleted.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});

}

function selectNewlyAddedCountry(countryId, countryName, countryCode){
	var optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);
	$("#dropDownCountries option[value='"+ optionValue +"']").prop("selected", true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}



function changeFormToSelectedCountry(){
	addCountryButton.prop("value", "New");
	updateCountryButton.prop("disabled", false);
	deleteCountryButton.prop("disabled", false);
	
	var selectedCountry = $("#dropDownCountries option:selected").text();
	var selectedCountryCode = dropDownCountries.val().split("-")[1];
	labelCountryName.text("Selected Country: ");
	fieldCountryName.val(selectedCountry);
	fieldCountryCode.val(selectedCountryCode);
	
	
}

function changeFormToNew(){
	addCountryButton.val("Add");
	addCountryButton.text("Add");
	labelCountryName.text("Country Name: ");
	
	updateCountryButton.prop("disabled", false);
	deleteCountryButton.prop("disabled", false);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}
















