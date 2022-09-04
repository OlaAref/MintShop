var data;
var options;
var totalGrossSales;
var totalNetSales;
var totalItems;

//make google chart responsive
$(window).resize(function(){
  drawChartForReportByCategory();
});

$(document).ready(function(){
	
	setupButtonEventHndler("_category", loadSalesReportByCategory);
});

function loadSalesReportByCategory(period){
	
	if(period == "custom"){
		var startDate = $("#fromDate_category").val();
		var endDate = $("#toDate_category").val();
		var requestedUrl = contextPath + "reports/category/"+startDate+"/"+endDate;
	}
	else{
		var requestedUrl = contextPath + "reports/category/"+period;
	}
	
	
	$.get(requestedUrl, function(responseJSON){
		prepareChartDataForReportByCategory(responseJSON);
		customizeChartForReportByCategory(period);
		drawChartForReportByCategory();
		showTotalAmounts(period, "_category", "Total Products");
	});
}

function prepareChartDataForReportByCategory(responseJSON){
	//reset data
	totalGrossSales = 0;
	totalNetSales = 0;
	totalItems = 0;

	data = new google.visualization.DataTable();
	data.addColumn('string', 'Category');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseFloat(reportItem.productCounts);
	});

}


function customizeChartForReportByCategory(period){
	options = {
		height: 360,
		legend: {option: 'right'}
	};
	
	formatChartNumbers(data, 1, 2);

}


function drawChartForReportByCategory(){
	 // Instantiate and draw our chart, passing in some options.
      var chart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
      chart.draw(data, options);
}
























