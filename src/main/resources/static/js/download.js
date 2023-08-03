  // Get the reference to the download button and the PDF iframe
  const downloadButton = document.getElementById('downloadButton');
  const pdfViewer = document.getElementById('printf');

  // Add an event listener to the download button
  downloadButton.addEventListener('click', () => {
    // Check if the iframe is loaded and its content is accessible
    if (pdfViewer.contentDocument) {
      // Get the iframe's document object
      const pdfDocument = pdfViewer.contentDocument;

      // Trigger the PDF download using the PDF's URL
      const pdfUrl = pdfDocument.URL;
      downloadPDF(pdfUrl);
    } else {
      console.error('PDF not loaded in the iframe yet.');
    }
  });

  // Function to trigger the PDF download
  function downloadPDF(pdfUrl) {
    // Create a temporary anchor element
    const link = document.createElement('a');

    // Set the href attribute to the PDF URL
    link.href = pdfUrl;

    // Set the download attribute to suggest the name for the downloaded file
    link.download = 'downloaded_pdf.pdf';

    // Append the link to the body and trigger the click event
    document.body.appendChild(link);
    link.click();

    // Remove the link from the body
    document.body.removeChild(link);
  }