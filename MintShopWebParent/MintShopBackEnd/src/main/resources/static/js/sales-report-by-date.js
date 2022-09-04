var data;
var options;
var totalGrossSales;
var totalNetSales;
var totalItems;

//make google chart responsive
$(window).resize(function(){
  drawChartForReportByDate();
});

$(document).ready(function(){
	
	setupButtonEventHndler("_date", loadSalesReportByDate);
});

function loadSalesReportByDate(period){
	
	if(period == "custom"){
		var startDate = $("#fromDate_date").val();
		var endDate = $("#toDate_date").val();
		var requestedUrl = contextPath + "reports/sales_by_date/"+startDate+"/"+endDate;
	}
	else{
		var requestedUrl = contextPath + "reports/sales_by_date/"+period;
	}
	
	
	$.get(requestedUrl, function(responseJSON){
		prepareChartDataForReportByDate(responseJSON);
		customizeChartForReportByDate(period);
		drawChartForReportByDate();
		showTotalAmounts(period, "_date", "Total Orders");
	});
}

function prepareChartDataForReportByDate(responseJSON){
	//reset data
	totalGrossSales = 0;
	totalNetSales = 0;
	totalItems = 0;

	data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Orders');
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.orderCounts]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseFloat(reportItem.orderCounts);
	});

}


function customizeChartForReportByDate(period){
	options = {
		title: getChartTitle(period),
		height: 360,
		legend: {option: 'top'},
		series:{
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
			2: {targetAxisIndex: 1}
		},
		vAxes: {
			0: {title: 'Sales Amount', format: 'currency'},
			1: {title: 'Number of Orders'}
		}
	};
	
	formatChartNumbers(data, 1, 2);

}


function drawChartForReportByDate(){
	 // Instantiate and draw our chart, passing in some options.
      var chart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
      chart.draw(data, options);
}
























