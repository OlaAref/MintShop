var loadCountriesForCitiesButton;
var dropDownCountriesForStatesCities;
var dropDownStatesForCities;
var dropDownCities;
var addCityButton;
var updateCityButton;
var deleteCityButton;
var fieldCityName, labelCityName;

$(document).ready(function() {
	loadCountriesForCitiesButton = $("#loadCountriesForCitiesButton");
	dropDownCountriesForStatesCities = $("#dropDownCountriesForCities");
	dropDownStatesForCities = $("#dropDownStatesForCities");
	dropDownCities = $("#dropDownCities");
	
	addCityButton = $("#addCityButton");
	updateCityButton = $("#updateCityButton");
	deleteCityButton = $("#deleteCityButton");
	
	labelCityName = $("#labelCityName");
	fieldCityName = $("#fieldCityName");
	
	loadCountriesForCitiesButton.click(function() {
		loadCountries4States4Cities();	
	});
	
	dropDownCountriesForStatesCities.on("change", function() {
		loadStates4Cities();
	});
	
	dropDownStatesForCities.on("change", function() {
		loadCities();
	});
	
	dropDownCities.on("change", function() {
		changeFormToSelectedCity();
	});
	
	addCityButton.click(function() {
		if(addCityButton.val() == 'Add'){
			addCity();
		}
		else {
			changeFormToNewCity();
		}
	});
	
	updateCityButton.click(function() {
		updateState();	
	});
	
	deleteCityButton.click(function() {
		deleteState();	
	});
	
});


function loadCountries4States4Cities(){
	var url = contextPath + "countries/list";
	$.get(url, function(jsonResponse) {
		dropDownCountriesForStatesCities.empty();
		
		$.each(jsonResponse, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStatesCities);
		});
	})
	.done(function() {
		loadCountriesForCitiesButton.val("Refresh Country List");
		showToastSuccessMessage("All Countries have been loaded.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
}

function validateFormCity() {

	var formCity = document.getElementById("formCity");
	if(!formCity.checkValidity()){
		formCity.reportValidity();
		return false;
	}
	return true;
}

function addCity() {
	if(!validateFormCity()) return;

	var url = contextPath + "cities/save";
	
	var cityName = fieldCityName.val();
	var selectedState = $("#dropDownStatesForCities option:selected");
	var stateId = selectedState.val();
	var stateName = selectedState.text();
	
	var parameters = {
		name: cityName,
		state: {
			id: stateId,
			name: stateName
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
	.done(function(cityId) {
		selectNewlyAddedCity(cityId, cityName);
		showToastSuccessMessage("The new City has been added.");
	});
	
	
}
function updateState() {
	if(!validateFormCity()) return;

	var url = contextPath + "cities/save";
	
	var cityId = dropDownCities.val();
	var cityName = fieldCityName.val();
	var selectedState = $("#dropDownStatesForCities option:selected");
	var stateId = selectedState.val();
	var stateName = selectedState.text();
	
	var parameters = {
		id: cityId,
		name: cityName,
		state: {
			id: stateId,
			name: stateName
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
	.done(function(cityId) {
		$("#dropDownCities option:selected").text(cityName);
		showToastSuccessMessage("The city has been updated.");
		changeFormToNewCity();
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
	
}

function deleteState() {
	var cityId = dropDownCities.val();
	var url = contextPath + "cities/delete/"+cityId;
	
	
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	})
	.done(function() {
		$("#dropDownCities option[value='"+ cityId +"']").remove();
		changeFormToNewCity();
		showToastSuccessMessage("The city has been deleted.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});

}

function loadStates4Cities(){
	
	var selectedCountry = $("#dropDownCountriesForCities option:selected");
	var countryId = selectedCountry.val();
	var url = contextPath + "states/list/"+countryId;
	
	$.get(url, function(jsonResponse) {
		dropDownStatesForCities.empty();
		
		$.each(jsonResponse, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStatesForCities);
		});
	})
	.done(function() {
		showToastSuccessMessage("All states have been loaded.");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
}

function loadCities(){
	var selectedCity = $("#dropDownStatesForCities option:selected");
	var cityId = selectedCity.val();
	var url = contextPath + "cities/list/"+cityId;
	
	$.get(url, function(jsonResponse) {
		dropDownCities.empty();
		
		$.each(jsonResponse, function(index, city) {
			$("<option>").val(city.id).text(city.name).appendTo(dropDownCities);
		});
	})
	.done(function() {
		changeFormToNewCity();
		showToastSuccessMessage("All states have been loaded for City "+ selectedCity.text() +".");
	})
	.fail(function() {
		showToastFailMessage("ERROR : Could not connect to server or server encountered an error.");
	});
	
}

function selectNewlyAddedCity(cityId, cityName){
	$("<option>").val(cityId).text(cityName).appendTo(dropDownCities);
	$("#dropDownCities option[value='"+ cityId +"']").prop("selected", true);
	
	fieldCityName.val("").focus();
}

function changeFormToSelectedCity(){
	addCityButton.prop("value", "New");
	updateCityButton.prop("disabled", false);
	deleteCityButton.prop("disabled", false);
	
	labelCityName.text("Selected City: ");
	
	var selectedCityName = $("#dropDownCities option:selected").text();
	
	fieldCityName.val(selectedCityName);

}

function changeFormToNewCity(){
	addCityButton.val("Add");
	addCityButton.text("Add");
	labelCityName.text("Cities Name: ");
	
	updateCityButton.prop("disabled", false);
	deleteCityButton.prop("disabled", false);
	
	fieldCityName.val("").focus();
}

















