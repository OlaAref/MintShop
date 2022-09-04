var position = 1;

$(document).ready(function() {
	
	$(".addProductToSectionLink").click(function(e){
		e.preventDefault();
		
		url = $(this).attr("href");
		$("#addProductModel").on('shown.bs.modal', function () {
			$(this).find("iframe").attr("src", url);
		});
		
		$("#addProductModel").modal('show');
	});
	
	$(".linkProduct").click(function(e){
		e.preventDefault();
		var id = $(this).attr("pid");
		//back to prev window
		window.parent.addProduct(id);
	});

	
	$( "#sectionForm" ).submit(function( event ) {
	  	sendBrandsToController();
	});

});

function addProduct(productId){
	$("#addProductModel").modal('hide');
	getProductInfo(productId);
}

function getProductInfo(productId){
	
	requestUrl = contextPath + "products/get/" + productId;

	$.get(requestUrl, function(response) {
		//response is productDTO as JSON
		productName = response.name;
		imagePath = response.imagePath;
		
		var htmlCode = generateProductCode(productId, imagePath, productName);
		$("#productList").append(htmlCode);
	})
	.fail(function(err) {
		showModalDialog("Error", err.responseJSON.message);
	});
	
}

function generateProductCode(productId, imagePath, productName){
	
	htmlCode = `
				<div class="col-auto mt-3" id="productDiv${productId}">
					<input hidden="hidden" name="products" value="${productId}" class="hiddenProductId">
					
					<div class="form-group row m-2">
						<span>
						<a href="#" class="deleteSelectedProductLink" productId="${productId}" onclick="deleteSelectedProduct(this)"><i class="fa fa-solid fa-trash text-light fa-2x"></i></a>
						<a href="#" class="levelUpProductLink" productId="${productId}" onclick="levelUp(this)"><i class="fa fa-solid fa-chevron-left text-light fa-2x"></i></a>
						<a href="#" class="levelDownProductLink" productId="${productId}" onclick="levelDown(this)"><i class="fa fa-solid fa-chevron-right text-light fa-2x"></i></a>
						</span>
					</div>
					
					<div class="row">
						<div>
							<img src="${imagePath}" width="200px" />
						</div>
					</div>
					
					<div class="row m-2" style="width: 200px">
						<b>${productName}</b>
					</div>
					
				</div>
	`;
	
	return htmlCode;
}

function deleteSelectedProduct(link){
	var productId = $(link).attr("productid");
	var selectedProduct = $("#productDiv"+productId);
	selectedProduct.remove();
}

function levelUp(link){
	var productId = $(link).attr("productid");
	var selectedProduct = $("#productDiv"+productId);
	var prevProduct = selectedProduct.prev('div[id^=productDiv]');
	
	if(prevProduct.length){
		swapDivs(selectedProduct, prevProduct);
	}
}

function levelDown(link){
	var productId = $(link).attr("productid");
	var selectedProduct = $("#productDiv"+productId);
	var nextProduct = selectedProduct.next('div[id^=productDiv]');
	
	if(nextProduct.length){
		swapDivs(selectedProduct, nextProduct);
	}
}

function swapDivs(div1, div2){
	var div1Html = div1.html();
	var div1Id = div1.attr("id");
	
	var div2Html = div2.html();
	var div2Id = div2.attr("id");
	
	div1.html(div2Html);
	div1.prop("id", div2Id);
	
	div2.html(div1Html);
	div2.prop("id", div1Id);
}
























