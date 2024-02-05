function uploadMultipleFiles(elementId, message) {
    var formData = new FormData();
    var files = document.getElementById(elementId).files;
    var finalMessage = message;

    for (var i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
        finalMessage = finalMessage + "&" + files[i].name
        console.log(finalMessage);
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

function uploadFinalFile(elementId, message) {
    var formData = new FormData();
    var files = document.getElementById(elementId).files;
    var finalMessage = message;

    for (var i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
        finalMessage = finalMessage + "&" + files[i].name
        console.log(finalMessage);
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

    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log(xhr.responseText);

            document.getElementById("downloadBtn").disabled = false;
        } else {
            console.error('File upload failed. Please try again.');
        }
    };

}




function sendData(){
    uploadMultipleFiles('interstateFormFile', 'interstate');
    uploadMultipleFiles('paymentFormFileMultiple', 'paymentMultiple');
    uploadMultipleFiles('longTermMarketFormFile', 'longTermMarket');
    uploadMultipleFiles('longTermMarketReportFormFile', 'longTermMarketReport');
    uploadMultipleFiles('marketsFormFileMultiple', 'marketsMultiple');
    uploadMultipleFiles('finalBlankFormFile', 'finalBlankFormFile');
    document.getElementById("downloadBtn").disabled = false;
}

document.getElementById('downloadBtn').addEventListener('click', function() {
    fetch('/download', {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(new Blob([blob]));
            const a = document.createElement('a');
            a.href = url;
            a.download = 'final.xlsx';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => console.error('There was a problem with the fetch operation:', error));
});