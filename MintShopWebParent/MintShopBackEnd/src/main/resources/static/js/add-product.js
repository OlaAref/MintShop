var productDetailCount;

$(document).ready(function() {

	
	
	productDetailCount = $(".hiddenProductId").length;

	$("#products").on("click", "#addProductLink", function(e) {
		
		e.preventDefault();
		
		url = $(this).attr("href");
		$("#addProductModel").on('shown.bs.modal', function () {
			$(this).find("iframe").attr("src", url);
		});
		
		$("#addProductModel").modal('show');
		
	});
	
	
	//on delete product
	$("#productList").on("click", ".removeProductLink", function(e) {
		e.preventDefault();
		
		if(isOrderHaveOneProduct()){
			showModalDialog("Warning", "Could not remove product. The order must have at least one product.");
		}
		else{
			removeProduct($(this));
			updateOrderAmounts();
		}
		
	});
	
		

});



function removeProduct(deleteLink){
	rowNumber = deleteLink.attr("rowNumber");
	$("#row"+rowNumber).remove();

	$(".divCount").each(function(index, element) {
	
		element.innerHTML = "" + (index + 1);
		
	});
	
	
}

function isOrderHaveOneProduct() {
	
	productCount = $(".hiddenProductId").length;
	return productCount == 1;
	
}

//add Product to order-form-products
function addProduct(productId, productName){
	$("#addProductModel").modal('hide');
	getShippingCost(productId);
}

function getShippingCost(productId) {
	
	selectedCountry = $("#countrySelecet option:selected");
	countryId = selectedCountry.val();
	state = $("#stateInput").val();
	if(state.length == 0){
		state = $("#cityInput").val();
	}
	
	requestUrl = contextPath + "shippingRates/shippingCost";
	params = {
		ProductId: productId,
		countryId: countryId,
		state: state
	};
	
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: params
	})
	.done(function(response) {
		//response is shipping cost
		getProductInfo(productId, response);
	})
	.fail(function(err) {
		showModalDialog("Error", err.responseJSON.message);
		shippingCost = 0.0;
		getProductInfo(productId, shippingCost);
	})
	.always(function() {
		$("#addProductModel").modal('hide');
	});
	
}

function getProductInfo(productId, shippingCost){
	
	requestUrl = contextPath + "products/get/" + productId;
	
	$.get(requestUrl, function(response) {
		//response is productDTO as JSON
		productName = response.name;
		imagePath = response.imagePath;
		productCost = $.number(response.cost, 2);
		productPrice = $.number(response.price, 2);
		shippingCostFormatted = $.number(shippingCost, 2);
		
		htmlCode = generateProductCode(productId, imagePath, productCost, productName, productPrice, shippingCostFormatted);
		$("#productList").append(htmlCode);
		updateOrderAmounts();
	})
	.fail(function(err) {
		showModalDialog("Error", err.responseJSON.message);
	});
	
}

function generateProductCode(productId, imagePath, productCost, productName, productPrice, shippingCost){
	nextCount = productDetailCount + 1;
	productDetailCount++;
	quantityId = "quantity" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	
	htmlCode = `
				<div class="border rounded bg-light text-dark bg-gradient p-2 m-2" id="row${nextCount}">
					<input hidden="hidden" name="detailId" value="0">
					<input hidden="hidden" name="productId" value="${productId}" class="hiddenProductId">
				
					<div class="row">
						<div class="col-1">
							<div class="divCount">${nextCount}</div>
							<div>
								<a class="removeProductLink" href="" rowNumber="${nextCount}">
									<i class="fas fa-trash"></i>
								</a>
							</div>
						</div>
						
						<div class="col-3 m-2">
							<img src="${imagePath}" height="200px" class="img-fluid"/>
						</div>
					</div>
					
					<div class="row m-2">
						<b>${productName}</b>
					</div>
					
					<div class="row m-2">
						<table>
							<tr>
								<td class="col-sm-2">Product Cost : </td>
								<td>
									<input type="text" class="form-control col-8 cost-input" value="${productCost}" 
										name="productDetailCost"
										rowNumber="${nextCount}"
										style="max-width: 140px" required/>
								</td>
							</tr>
							<tr>
								<td class="col-sm-2">Quantity : </td>
								<td>
									<input type="number" step="1" min="1" max="5" class="form-control col-8 quantity-input"
										name="quantity" 
										id="${quantityId}"
										rowNumber="${nextCount}"
										value="1" style="max-width: 140px" required/>
								</td>
							</tr>
							<tr>
								<td class="col-sm-2">Unit Price : </td>
								<td>
									<input type="text" class="form-control col-8 price-input" 
										name="productPrice"
										value="${productPrice}" 
										id="${priceId}"
										rowNumber="${nextCount}"
										style="max-width: 140px" required/>
								</td>
							</tr>
							<tr>
								<td class="col-sm-2">Subtotal : </td>
								<td>
									<input type="text" class="form-control col-8 subtotal-output" 
										name="productSubtotal"
										value="${productPrice}" 
										id="${subtotalId}"
										style="max-width: 140px" readonly/>
								</td>
							</tr>
							<tr>
								<td class="col-sm-2">Shipping Cost : </td>
								<td>
									<input type="text" class="form-control col-8 shipping-input" 
										name="productShipCost"
										value="${shippingCost}" 
										rowNumber="${nextCount}"
										style="max-width: 140px" required/>
								</td>
							</tr>
						</table>
					</div>
					
				</div>
	`;
	
	return htmlCode;
}

function isProductAlreadyAdded(productId) {
	
	productExist = false;
	
	$(".hiddenProductId").each(function(e) {
		formProductId = $(this).val();
		
		if(formProductId == productId){
			productExist = true;
			return;
		}
		
	});
	
	return productExist;
	
}

function showModalDialog(title, message){
	$("#modalTitle").text(title);
	$("#modalMessage").text(message);
	$("#modalDialog").modal('show');
}