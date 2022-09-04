var startDateField;
var endDateField;
const MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;


function setupButtonEventHndler(reportType, callbackFunction){
	
	startDateField = document.getElementById("fromDate" + reportType);
	endDateField = document.getElementById("toDate" + reportType);
	
	$(".button_sales_by" + reportType).on("click", function(){
		
		$(".button_sales_by" + reportType).each(function(e){
			$(this).removeClass("btn-success").addClass("btn-light");
		});
		$(this).removeClass("btn-light").addClass("btn-success");
		
		var period = $(this).attr("period");
		if(period){//if period is exist
			callbackFunction(period);
			$("#divCustomDateRange" + reportType).addClass("d-none");
		}
		else{
			$("#divCustomDateRange" + reportType).removeClass("d-none");
		}
		
	});
	
	initCustomDateRange();
	
	$("#buttonViewReportByRange" + reportType).on("click", function(e){
		e.preventDefault();
		validateDateRange(callbackFunction);
	});
}

function validateDateRange(callbackFunction){
	//clean validity first
	startDateField.setCustomValidity("");
	
	var days = calculateDays();
	if(days >= 7 && days <= 30){
		//submit the form
		callbackFunction("custom");
	}
	else{
		startDateField.setCustomValidity("Date must be in range of 7 - 30 days");
		startDateField.reportValidity();
	}
}

function calculateDays(){
	var startDate = startDateField.valueAsDate;
	var endDate = endDateField.valueAsDate;
	var differenceInMilliseconds = endDate - startDate;
	var differenceInDays = differenceInMilliseconds / MILLISECONDS_PER_DAY;
	return differenceInDays;
}

function initCustomDateRange(){
	var toDate = new Date();
	endDateField.valueAsDate = toDate;
	
	var fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
}

function showTotalAmounts(period, reportType, labelTotalItems){
	var divisor = getDivisor(period);

	$("#textTotalGrossSales" + reportType).text(formatCurrency(totalGrossSales));
	$("#textTotalNetSales" + reportType).text(formatCurrency(totalNetSales));
	$("#textAvgGrossSales" + reportType).text(formatCurrency(totalGrossSales / divisor));
	$("#textAvgNetSales" + reportType).text(formatCurrency(totalNetSales / divisor));
	$("#labelTotalItems" + reportType).text(labelTotalItems);
	$("#textTotalItems" + reportType).text(totalItems);
}

function formatCurrency(amount){
	return prefixSymbol + $.number(amount, decimalDigits, decimalPointType, thousandsPointType) + suffixSymbol;
}

function getChartTitle(period){
	if(period == 'last_7_days') return 'Sales in last 7 days';
	else if(period == 'last_28_days') return 'Sales in last 28 days';
	else if(period == 'last_6_months') return 'Sales in last 6 months';
	else if(period == 'last_year') return 'Sales in last year';
	else if(period == 'custom') return 'Custom Date Range';
	else return 'Sales in last 7 days';
}

function getDivisor(period){
	if(period == 'last_7_days') return 7;
	else if(period == 'last_28_days') return 28;
	else if(period == 'last_6_months') return 6;
	else if(period == 'last_year') return 12;
	else if(period == 'custom') return calculateDays();
	else return 7;
}

function formatChartNumbers(data, columnIndex1, columnIndex2){
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixSymbol,
		suffix: suffixSymbol,
		decimalSymbol: decimalPointType,
		groupingSymbol: thousandsPointType,
		fractionDigits: decimalDigits
	});
	
	formatter.format(data, columnIndex1); // Apply formatter to second column
	formatter.format(data, columnIndex2); // Apply formatter to third column
}

















