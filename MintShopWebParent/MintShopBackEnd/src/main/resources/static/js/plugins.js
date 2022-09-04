function customeNotice(title, text) {
	const notice = PNotify.notice({
		title: title,
		text: text,
		styling: 'custom',
		maxTextHeight: null,
		addModelessClass: 'nonblock',
		icon: 'fas fa-exclamation-triangle',
		addClass: 'stack-bar-top',
		shadow: false,
		width: '100%',
		stack: window.stackBarTop,
		hide: true,
		closer: true
	});

}

function showStackBarTop(type, title, text) {
	if (typeof window.stackBarTop === 'undefined') {
		window.stackBarTop = new PNotify.Stack({
			modal: false,
			dir1: 'down',
			firstpos1: 0,
			spacing1: 0,
			push: 'top',
			maxOpen: Infinity
			
		});
	}
	const opts = {
		title: title,
		text: text,
		addClass: 'stack-bar-top',
		shadow: false,
		width: '100%',
		stack: window.stackBarTop
	};
	switch (type) {
		case 'error':
			opts.title = title;
			opts.text = text;
			opts.type = 'error';
			break;
		case 'info':
			opts.title = title;
			opts.text = text;
			opts.type = 'info';
			break;
		case 'success':
			opts.title = title;
			opts.text = text;
			opts.type = 'success';
			break;
	}
	PNotify.alert(opts);
}



/*global $, document, window*/
$(document).ready(function () {
    'use strict';
    
    /***********************************************************************/
    
    //scroll up
    var scrollButton = $(".scroll-top");
    //check if it scroll more than 700 px
    $(window).scroll(function () {
        $(this).scrollTop() >= 700 ? scrollButton.show() : scrollButton.hide();
    });
    //add scroll function to the button
    scrollButton.click(function () {
        $("html,body").animate({scrollTop : 0}, 600);
    });
    
    /***********************************************************************/
    
  //Preview Image
	$(function() {
		$(document).on("change", ".uploadFile",
			function() {
				var uploadFile = $(this);
				var files = !!this.files ? this.files : [];
				if (!files.length || !window.FileReader)
					return; // no file selected, or no FileReader support
	            
	            //only image file
				if (/^image/.test(files[0].type)) {
					//instance of the FileReader
					var reader = new FileReader(); 
	                
					//read the local file
					reader.readAsDataURL(files[0]); 
	                
	                //set image data as background of div
					reader.onloadend = function() { 
						
						//hide the previous image
						document.getElementsByClassName('img-db')[0].style.visibility = 'hidden';
						
						// alert(uploadFile.closest(".upimage").find('.imagePreview').length);
						uploadFile.closest(".imgUp").find('.imagePreview').css("background-image", "url(" + this.result + ")");
					}
				
				}
					
	
			});
	});
	/***********************************************************************/
	//Add Extra Image
	var index = parseInt($("#addRow").attr("data-noOfExtraImages"))+1;
	if(isNaN(index)) {
		index = 1;
	}
	$("#addRow").click(function () {
		$("#extraImageCol").attr("class","d-inline");
		addExtraImage(index);
		index++;
	});
	
	
	/***********************************************************************/
	
	//Remove extra image
	$(".imageRemoveLink").each(function (index) {
		$(this).click(function() {
			removeExtraImage(index+1);
		});
	});
	
	
	/***********************************************************************/
	
	//Remove extra detail
	$(".removeDetailsLink").each(function (index) {
		$(this).click(function() {
			removeExtraDetailByIndex(index+1);
		});
	});
	
	
	/***********************************************************************/
    
	customizeTabs();
	
	/***********************************************************************/
});

function addExtraImage(index) {
	
		var htmlExtraImage = `
				<div class="col" id="divExtraImage${index}">
					<div class="border m-2">
							<br>
							<div class="imgUp">
								<div id="extraImageHeader${index}"><label class="form-label mt-4 fw-bold">Extra Image #${index}: </label></div>
								<div id="imagePreview${index}" class="imagePreview">
									<img id="extraImage${index}" class="img-db${index}"/>
								</div>
								<label class="btn btn-primary customize-btn"> Add Image
									<input type="file" name="extraPhoto" class="img-db" value="Upload Photo" style="width: 0px; height: 0px; overflow: hidden;" 
											onchange="fileInfoForProduct(this)" 
											data-elementId="extraPhoto${index}" data-imgSizeDiv="image-size-extra${index}" 
											data-imgDb="img-db${index}" data-imagePreview="imagePreview${index}" 
											data-imageName="imageName${index}"
											id="extraPhoto${index}" accept="image/png, image/jpeg">
								</label> 
								<small id="image-size-extra${index}"></small><br>
								<small id="emailHelp" class="form-text text-muted">Max size 4 MB.</small><br>
								<small id="emailHelp" class="form-text text-muted">Prefer 178X180 px.</small>
							</div>
						</div>
					</div>`;
					
		$("#extraImageCol").before(htmlExtraImage);
		
		var htmlRemoveLink = `<a class="btn far fa-times-circle fa-4x float-end removeImgIcon" 
								 href="javascript:void(0);" onclick="removeExtraImage(${index});"
								 title="Remove this image"></a>`;
		
		$("#extraImageHeader"+index).append(htmlRemoveLink);
		
	
}

function removeExtraImage(index){
	$("#divExtraImage"+index).remove();
}

function addNextDetailSection() {

	var allDivDetails = $("[id^='divDetail']");
	var divDetailsCount = allDivDetails.length;
	
	var htmlExtraDetail=`
						<div class="row mb-3" id="divDetail${divDetailsCount}">
							<input type="hidden" name="detailIDs" value="0">
						    <label for="inputName" class="col-sm-1 col-form-label">Name : </label>
						    <div class="col-sm">
						      <input type="text" class="form-control" id="inputName" name="detailNames" maxlength="255">
						    </div>
						    <label for="inputValue" class="col-sm-1 col-form-label">Value : </label>
						    <div class="col-sm">
						      <input type="text" class="form-control" id="inputValue" name="detailValues" maxlength="255">
						    </div>
						</div>`;
	
	$("#divProductDetailsExtra").append(htmlExtraDetail);
	
	
	var previousDivDetailSection = allDivDetails.last();
	var previousDivDetailId = previousDivDetailSection.attr("id");
	
	var htmlRemoveLink = `<a class="btn far fa-times-circle fa-3x removeImgIcon" 
								 href="javascript:void(0);" onclick="removeExtraDetail('${previousDivDetailId}');"
								 title="Remove this image"></a>`;

	previousDivDetailSection.append(htmlRemoveLink);
	
	$("input[name='detailNames']").last().focus();
}


function removeExtraDetail(id){
	$("#"+id).remove();
}

function removeExtraDetailByIndex(index){
	$("#divDetail"+index).remove();
}

	
function fileInfo() {
	var theFile = document.getElementById("file");
	var fileSize = Math.round(theFile.files[0].size / 1024);
	document.getElementById("image-size").innerHTML = "Your Image : " + fileSize + "KB.";
	
}

function fileInfoExtra(elementId, imgSizeDiv) {
	console.log(elementId+imgSizeDiv);
	var theFile = document.getElementById(elementId);
	var fileSize = Math.round(theFile.files[0].size / 1024);
	document.getElementById(imgSizeDiv).innerHTML = "Your Image : " + fileSize + "KB.";
	
}

function fileInfoForProduct(extraFile) {
	var elementId = extraFile.getAttribute("data-elementId");
	var imgSizeDiv = extraFile.getAttribute("data-imgSizeDiv");
	
	var theFile = extraFile;

	var fileSize = Math.round(theFile.files[0].size / 1024);
	document.getElementById(imgSizeDiv).innerHTML = "Your Image : " + fileSize + "KB.";
	
	var imageName = extraFile.getAttribute("data-imageName");
	var imageNameHiddenField = $('#'+imageName);
	var fileName = theFile.files[0].name;
	
	
	var imgDb = extraFile.getAttribute("data-imgDb");
	var imagePreview = extraFile.getAttribute("data-imagePreview");
	
	var files = !!extraFile.files ? extraFile.files : [];
	if (!files.length || !window.FileReader)
	return; // no file selected, or no FileReader support
                    
    //only image file
	if (/^image/.test(files[0].type)) {
		//instance of the FileReader
		var reader = new FileReader(); 
	                        
		//read the local file
		reader.readAsDataURL(files[0]); 
	                        
	    //set image data as background of div
		reader.onloadend = function() { 
								
			//hide the previous image
			document.getElementsByClassName(imgDb)[0].style.visibility = 'hidden';
			
			$('#'+imagePreview)[0].style.backgroundImage = "url(" + this.result + ")"; 
			
			//code to update extra image
			//check if imageName exist in the form
			if(imageNameHiddenField.length){
				imageNameHiddenField.val(fileName);
			}
		}
	}
}

function customizeTabs(){
    	
	var url = document.location.toString();
	if(url.match('#')){
		$('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
	}
	
	//change hash for page reload
	$('.nav-tabs a').on('shown.bs.tab', function() {
		window.location.hash = e.target.hash
	});
}