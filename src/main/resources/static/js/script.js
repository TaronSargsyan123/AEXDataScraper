function uploadMultipleFiles(elementId, message) {
    var formData = new FormData();
    var files = document.getElementById(elementId).files;
    var finalMessage = message;

    for (var i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
        finalMessage = finalMessage + " " + files[i].name
    }
    formData.append("message", finalMessage);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload', true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log(xhr.responseText);
        } else {
            console.error('File upload failed. Please try again.');
        }
    };

    xhr.onerror = function() {
        console.error('Request failed');
    };

    xhr.send(formData);
}

// interstateFormFile
// paymentFormFileMultiple
// longTermMarketFormFile
// longTermMarketReportFormFile
// finalBlankFormFile
// marketsFormFileMultiple
function sendData(){
    uploadMultipleFiles('interstateFormFile', 'interstateFormFile');
    uploadMultipleFiles('paymentFormFileMultiple', 'paymentFormFileMultiple');
    uploadMultipleFiles('longTermMarketFormFile', 'longTermMarketFormFile');
    uploadMultipleFiles('longTermMarketReportFormFile', 'longTermMarketReportFormFile');
    uploadMultipleFiles('marketsFormFileMultiple', 'marketsFormFileMultiple');
    uploadMultipleFiles('finalBlankFormFile', 'finalBlankFormFile');
}