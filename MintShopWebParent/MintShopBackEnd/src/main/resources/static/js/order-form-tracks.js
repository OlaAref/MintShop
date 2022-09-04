var trackRecordCount;

$(document).ready(function() {

	trackRecordCount = $(".hiddenTrackId").length;
	
	//add track button
	$("#track").on("click", "#addTrackLink", function(e) {
		e.preventDefault();
		addNewTrackRecord();
	});
	
	
	//on delete product
	$("#tracksList").on("click", ".removeTrackLink", function(e) {
		e.preventDefault();
		removeTrack($(this));
		updateTrackCountNumbers();
	});
	
	//when status dropdown change
	$("#tracksList").on("change", ".dropdownStatus", function(e) {
		dropdownList = $(this);
		rowNumber = dropdownList.attr("rowNumber");
		selectedOption = $("option:selected", dropdownList);
		
		defaultNote = selectedOption.attr("defaultDescription");
		$("#trackNote"+rowNumber).text(defaultNote);
	});

});

function removeTrack(link){
	rowNumber = link.attr("rowNumber");
	$("#rowTrack"+rowNumber).remove();
}

function updateTrackCountNumbers(){
	$(".divCountTrack").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function addNewTrackRecord(){
	htmlCode = generateTrackCode();
	$("#tracksList").append(htmlCode);
}

function generateTrackCode(){
	nextCount = trackRecordCount + 1;
	trackRecordCount++;
	rowId = "rowTrack" + nextCount;
	trackNoteId = "trackNote" + nextCount;
	currentDateTime = formatCurrentDateTime();
	
	htmlCode = `
				<div class="border rounded bg-light text-dark bg-gradient p-2 m-2" id="${rowId}">
				
					<input type="hidden" name="trackId" value="0" class="hiddenTrackId">
				
					<div class="row">
						<div class="col-1">
							<div class="divCountTrack">${nextCount}</div>
							<div>
								<a class="removeTrackLink" href="" rowNumber="${nextCount}">
									<i class="fas fa-trash"></i>
								</a>
							</div>
						</div>
						
						<div class="col-10 m-2">
						
							<div class="form-group row">
								<label class="col-form-label">Time : </label>
								<div class="col">
									<input type="datetime-local" name="trackDate" value="${currentDateTime}"
											class="form-control" required
											style="max-width: 300px">
								</div>
							</div>
							
							<div class="form-group row">
								<label class="col-form-label">Status : </label>
								<div class="col">
									<select class="form-select dropdownStatus" name="trackStatus" required
											style="max-width: 150px"
											rowNumber="${nextCount}">
											
									`;
						htmlCode += $("#trackStatusOptions").clone().html();	
						htmlCode += `				
											  
									</select>
								</div>
							</div>
							
							<div class="form-group row">
								<label class="col-form-label">Notes : </label>
								<div class="col">
									<textarea rows="2" cols="10" class="form-control" name="trackNotes" style="max-width: 300px"
											id="${trackNoteId}" required></textarea>
								</div>
							</div>
						</div>
					</div>
					
				</div>
	`;
	
	return htmlCode;
}

function formatCurrentDateTime() {
	var date = new Date();
	year = date.getFullYear();
	month = date.getMonth() + 1;
	day = date.getDate();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();
	
	if(month < 10) month = "0" + month;
	if(day < 10) day = "0" + day;
	
	if(hour < 10) hour = "0" + hour;
	if(minute < 10) minute = "0" + minute;
	if(second < 10) second = "0" + second;
	
	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
	
}





























