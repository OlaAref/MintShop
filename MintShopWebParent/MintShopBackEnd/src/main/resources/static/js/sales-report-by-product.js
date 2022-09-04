var data;
var options;
var totalGrossSales;
var totalNetSales;
var totalItems;

//make google chart responsive
$(window).resize(function(){
  drawChartForReportByProduct();
});

$(document).ready(function(){
	
	setupButtonEventHndler("_product", loadSalesReportByProduct);
});

function loadSalesReportByProduct(period){
	
	if(period == "custom"){
		var startDate = $("#fromDate_product").val();
		var endDate = $("#toDate_product").val();
		var requestedUrl = contextPath + "reports/product/"+startDate+"/"+endDate;
	}
	else{
		var requestedUrl = contextPath + "reports/product/"+period;
	}
	
	
	$.get(requestedUrl, function(responseJSON){
		prepareChartDataForReportByProduct(responseJSON);
		customizeChartForReportByProduct(period);
		drawChartForReportByProduct();
		showTotalAmounts(period, "_product", "Total Products");
	});
}

function prepareChartDataForReportByProduct(responseJSON){
	//reset data
	totalGrossSales = 0;
	totalNetSales = 0;
	totalItems = 0;

	data = new google.visualization.DataTable();
	data.addColumn('string', 'Product');
	data.addColumn('number', 'Quantity');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([[reportItem.identifier, reportItem.productCounts, reportItem.grossSales, reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseFloat(reportItem.productCounts);
	});

}


function customizeChartForReportByProduct(period){
	options = {
		height: 360,
		width: '90%',
		showRowNumber: true,
		sortColumn: 2,//sort table by "gross sales" column
		sortAscending: false,//sort table in descending order
		page: 'enable',
		pageSize: 5
	};
	
	formatChartNumbers(data, 2, 3);

}


function drawChartForReportByProduct(){
	 // Instantiate and draw our chart, passing in some options.
      var chart = new google.visualization.Table(document.getElementById('chart_sales_by_product'));
      chart.draw(data, options);
}
























