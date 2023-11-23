/**
 * Adds a new accordion item to the AAS/Form page when the "addPCFitem" button is clicked.
 */
$(document).ready(function () {
  var PCFCounter = 1;
  $("#addPCFitem").click(function () {
    // Create a new accordion item
    var dataContainer = document.getElementById("data-container");
    var dataArray = JSON.parse(dataContainer.getAttribute("data-array"));



    PCFCounter++;
    var newPCFItem = `
        <div class="accordion-item">
                      <h2 class="accordion-header" id="headingPCF${PCFCounter}">
                        <button id="PCF-header-button" class="accordion-button collapsed" type="button"
                          data-bs-toggle="collapse" data-bs-target="#collapsePCF${PCFCounter}" aria-expanded="false"
                          aria-controls="collapsePCF${PCFCounter}">
                          Product Carbon Footprint ${PCFCounter}
                        </button>
                      </h2>
                      <div id="collapsePCF${PCFCounter}" class="accordion-collapse collapse" aria-labelledby="headingPCF${PCFCounter}"
                        data-bs-parent="#PCFAccordion">
                        <div class="accordion-body">
                        <!--Referable Asset ID-->
                        <div class="mb-3">
                          <label for="ReferableAssetID">ID of the related Asset</label>
                          <input name="ReferableAssetID" type="text" class="form-control" placeholder="https://example.company.de/id/1234" list="list-environmentServices${PCFCounter}" id="ReferableAssetID">
                          <datalist id="list-environmentServices${PCFCounter}">
                          </datalist>
                        </div>
                          <!--Calculation Method-->
                          <div class="mb-3">
                            <label for="PCFCalculationMethod">Calculation Method</label>
                            <select name="PCFCalculationMethod" class="form-select d-block w-100"
                              id="PCFCalculationMethod" required="">
                              <option value="">Choose...</option>
                              <option value="0173-1#07-ABU223#001">EN 15804</option>
                              <option value="0173-1#07-ABU221#001">GHG Protocol</option>
                              <option value="0173-1#07-ABU222#001">IEC TS 63058</option>
                              <option value="0173-1#07-ABV505#001">ISO 14040</option>
                              <option value="0173-1#07-ABV506#001">ISO 14044</option>
                              <option value="0173-1#07-ABU218#001">ISO 14067</option>
                            </select>
                            <div class="invalid-feedback">
                              Please enter a calulation method.
                            </div>
                          </div>
                          <!--CO2eq-->
                          <div class="mb-3">
                            <label for="PCFCO2eq">CO2 Equivalent</label>
                            <input name="PCFCO2eq" type="number" class="form-control" id="PCFCO2eq" placeholder="0.00"
                              min="0">
                            <div class="invalid-feedback">
                              A CO2 equivalent is missing.
                            </div>
                          </div>
                          <!--Quantity of the measured product-->
                          <div class="row d-flex flex-row">
                            <div class="col-6 mb-3">
                              <label for="PCFQuantityOfMeasureForCalculation">Quantity of the measured product</label>
                              <input name="PCFQuantityOfMeasureForCalculation" type="number" class="form-control"
                                id="PCFQuantityOfMeasureForCalculation" placeholder="0" min="0">
                            </div>
                            <div class="col-2 mb-3">
                              <label for="PCFReferenceValueForCalculation">Reference Value</label>
                              <select name="PCFReferenceValueForCalculation" class="form-select custom-select d-block"
                                id="PCFReferenceValueForCalculation" required="">
                                <option value="">Choose...</option>
                                <option value="0173-1#07-ABZ596#001">g</option>
                                <option value="0173-1#07-ABZ597#001">kg</option>
                                <option value="0173-1#07-ABZ598#001">t</option>
                                <option value="0173-1#07-ABZ599#001">ml</option>
                                <option value="0173-1#07-ABZ600#001">l</option>
                                <option value="0173-1#07-ABZ601#001">cbm</option>
                                <option value="0173-1#07-ABZ602#001">qm</option>
                                <option value="0173-1#07-ABZ603#001">piece</option>
                              </select>
                              <div class="invalid-feedback">
                                Please enter a reference value.
                              </div>
                            </div>

                          </div>
                          <!--Live Cycle Phase-->
                          <div class="mb-3">
                            <label for="PCFLiveCyclePhase">Live Cycle Phase of the Product</label>
                            <select name="PCFLiveCyclePhase" class="form-select d-block w-100" id="PCFLiveCyclePhase"
                              required="">
                              <option value="">Choose...</option>
                              <option value="0173-1#07-ABU208#001">A1 – raw material supply (and upstream production)
                              </option>
                              <option value="0173-1#07-ABU209#001">A2 - cradle-to-gate transport to factory</option>
                              <option value="0173-1#07-ABU210#001">A3 - production</option>
                              <option value="0173-1#07-ABU211#001">A4 - transport to final destination</option>
                              <option value="0173-1#07-ABU212#001">B1 – usage phase</option>
                              <option value="0173-1#07-ABV498#001">B2 – maintenance</option>
                              <option value="0173-1#07-ABV497#001">B3 - repair</option>
                              <option value="0173-1#07-ABV499#001">B5 – update/upgrade, refurbishing</option>
                              <option value="0173-1#07-ABV500#001">B6 – usage energy consumption</option>
                              <option value="0173-1#07-ABV501#001">B7 – usage water consumption</option>
                              <option value="0173-1#07-ABV502#001">C1 – reassembly</option>
                              <option value="0173-1#07-ABU213#001">C2 – transport to recycler</option>
                              <option value="0173-1#07-ABV503#001">C3 – recycling, waste treatment</option>
                              <option value="0173-1#07-ABV504#001">C4 – landfill</option>
                              <option value="0173-1#07-ABU214#001">D - reuse</option>
                              <option value="0173-1#07-ABZ789#001">A1-A3</option>
                            </select>
                            <div class="invalid-feedback">
                              Please enter a reference value.
                            </div>
                          </div>
                          <!--Description-->
                          <div class="md-3">
                            <label for="PCFDescription">Asset Description</label>
                            <textarea class="form-control" id="PCFDescription" name="PCFDescription" rows="3"></textarea>
                          </div>
                        </div>

                      </div>
                    </div>
                    `;

    // Append the new accordion item to the accordion container
    $("#PCFAccordion").append(newPCFItem);

    if (dataArray) {
      // Update the HTML suggestions datalist
      var assetSuggestionsList = document.getElementById(`list-environmentServices${PCFCounter}`);
      dataArray.forEach(function (item) {
        var listItem = document.createElement("option");
        listItem.textContent = item;
        assetSuggestionsList.appendChild(listItem);
      });
    }
  });
});


/**
 * Adds a new accordion item to the AAS/Form page when the "addTCFitem" button is clicked.
 */
$(document).ready(function () {
  var TCFCounter = 1;
  $("#addTCFitem").click(function () {
    // Create a new accordion item
    TCFCounter++;
    var newTCFItem = `
        <div class="accordion-item">
                      <h2 class="accordion-header" id="headingTCF${TCFCounter}">
                        <button id="TCF-header-button${TCFCounter}" class="accordion-button collapsed" type="button"
                          data-bs-toggle="collapse" data-bs-target="#collapseTCF${TCFCounter}" aria-expanded="false"
                          aria-controls="collapseTCF${TCFCounter}">
                          Transport Carbon Footprint ${TCFCounter}
                        </button>
                      </h2>
                      <div id="collapseTCF${TCFCounter}" class="accordion-collapse collapse" aria-labelledby="headingTCF${TCFCounter}"
                        data-bs-parent="#TCFAccordion">
                        <div class="accordion-body">
                          <!--Calculation Method-->
                          <div class="mb-3">
                            <label for="TCFCalculationMethod">Calculation Method</label>
                            <select name="TCFCalculationMethod" class="form-select d-block w-100"
                              id="TCFCalculationMethod" required="">
                              <option value="">Choose...</option>
                              <option value="0173-1#07-ABU223#001">EN 15804</option>
                              <option value="0173-1#07-ABU221#001">GHG Protocol</option>
                              <option value="0173-1#07-ABU222#001">IEC TS 63058</option>
                              <option value="0173-1#07-ABV505#001">ISO 14040</option>
                              <option value="0173-1#07-ABV506#001">ISO 14044</option>
                              <option value="0173-1#07-ABU218#001">ISO 14067</option>
                            </select>
                            <div class="invalid-feedback">
                              Please enter a calulation method.
                            </div>
                          </div>
                          <!--CO2eq-->
                          <div class="mb-3">
                            <label for="TCFCO2eq">CO2 Equivalent</label>
                            <input name="TCFCO2eq" type="number" class="form-control" id="TCFCO2eq" placeholder="0.00"
                              min="0">
                            <div class="invalid-feedback">
                              A CO2 equivalent is missing.
                            </div>
                          </div>
                          <!--Quantity of measured product-->
                          <div class="row d-flex flex-row">
                            <div class="col-6 mb-3">
                              <label for="TCFQuantityOfMeasureForCalculation">Quantity of the measured product</label>
                              <input name="TCFQuantityOfMeasureForCalculation" type="number" class="form-control"
                                id="TCFQuantityOfMeasureForCalculation" placeholder="0" min="0">
                            </div>
                            <div class="col-2 mb-3">
                              <label for="TCFReferenceValueForCalculation">Reference Value</label>
                              <select name="TCFReferenceValueForCalculation" class="form-select d-block w-100"
                                id="TCFReferenceValueForCalculation" required="">
                                <option value="">Choose...</option>
                                <option value="0173-1#07-ABZ596#001">g</option>
                                <option value="0173-1#07-ABZ597#001">kg</option>
                                <option value="0173-1#07-ABZ598#001">t</option>
                                <option value="0173-1#07-ABZ599#001">ml</option>
                                <option value="0173-1#07-ABZ600#001">l</option>
                                <option value="0173-1#07-ABZ601#001">cbm</option>
                                <option value="0173-1#07-ABZ602#001">qm</option>
                                <option value="0173-1#07-ABZ603#001">piece</option>
                              </select>
                              <div class="invalid-feedback">
                                Please enter a reference value.
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    `;

    // Append the new accordion item to the accordion container
    $("#TCFAccordion").append(newTCFItem);
  });
});



/**
 * Adds the Asset ID to the Product Carbon Footprint accordion Button
 */
$(document).ready(function () {

  var assetIDshortInput = $(`#assetIDshort`);
  var accordionButtonProductCarbonFootprint = $(`#PCF-header-button`);

  assetIDshortInput.on("input", function () {
    var inputValue = assetIDshortInput.val();
    accordionButtonProductCarbonFootprint.text(`Product Carbon Footprint- ${inputValue}`);
  });
});

/**
 * Adds the Asset ID to the Transport Carbon Footprint accordion Button
 */
$(document).ready(function () {

  var assetIDshortInput = $(`#assetIDshort`);
  var accordionButtonProductCarbonFootprint = $(`#TCF-header-button`);

  assetIDshortInput.on("input", function () {
    var inputValue = assetIDshortInput.val();
    accordionButtonProductCarbonFootprint.text(`Transport Carbon Footprint- ${inputValue}`);
  });
});






// Import button on the overview view gets a file picking window
$(document).ready(function () {
  document.getElementById('importButton').addEventListener('click', function () {
    document.getElementById('aasFileInput').click();
  });

  const el = document.getElementById('aasFileInput');
  if (button) {
    el.addEventListener('change', function (e) {
      const selectedFile = e.target.files[0];
      console.log('Selected file:', selectedFile.name);
      
      // Create FormData object
      const formData = new FormData();
      formData.append('file', selectedFile);

      // Make a POST request using fetch
      fetch('/aas/overview/import', {
        method: 'POST',
        body: formData,
        // You might need to set headers depending on your backend requirements
        // headers: {
        //   'Content-Type': 'multipart/form-data',
        // },
      })
      .then(response => {
        if (response.ok) {
          // If the response is successful, reload the overview page
          window.location.href = '/aas/overview';
        } else {
          // Handle error cases
          console.log('Failed to upload file');
        }
      })
      .catch(error => {
        // Handle network errors or exceptions
        console.error('Error:', error);
      });
    });
  }
});


// Edit button on the Overview View
$(document).ready(function(){
  // Use querySelectorAll to get all edit buttons
  var editButtons = document.querySelectorAll(".deleteButton");

  function buttonClickAction(event){
    // Retrieve AssetID from the clicked button's data-attribute
    var assetID = event.currentTarget.getAttribute('data-assetid');
    console.log('Clicked AssetID JS:', assetID);

    // Send the AssetID to the backend using fetch
    fetch(`/aas/delete/${assetID}`, {
      method: 'GET'
    })
    .then(response => {
      if (response.ok) {
        // If the response is successful, handle as needed
        // For example, redirect to the edit page or do further processing
        window.location.href = `/aas/delete/${assetID}`;
      } else {
        // Handle error cases
        console.log('Failed to retrieve AssetID');
      }
    })
    .catch(error => {
      // Handle network errors or exceptions
      console.error('Error:', error);
    });
  }

  // Attach click event listener to each edit button
  editButtons.forEach(button => {
    button.addEventListener("click", buttonClickAction);
  });
});




