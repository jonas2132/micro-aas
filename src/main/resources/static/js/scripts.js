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
          <input name="ReferableAssetID" type="text" class="form-control"
            list="list-environmentServices${PCFCounter}" id="ReferableAssetID"
            placeholder="https://example.company.de/id/1234">
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
        <!--Life Cycle Phase-->
        <div class="mb-3">
          <label for="PCFLiveCyclePhase">Life Cycle Phase of the Product</label>
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
        <div class="mb-3">
          <label for="PCFDescription">Asset Description</label>
          <textarea class="form-control" id="PCFDescription" name="PCFDescription" rows="3"
            value=""></textarea>
        </div>
        <!--Explanatory Statement-->
        <div class="mb-3">
          <label class="form-label" for="ExplanatoryStatement">Explanatory Statement</label>
          <input type="file" class="form-control" id="ExplanatoryStatement" name="ExplanatoryStatement" />
        </div>
        <!--Handover Address-->
        <label for="HandoverAddress" class="from-lable">Handover Adress</label>
        <div id="HandoverAddress">
          <div class="mb-2 row">
            <div class="col-10">
                <input name="PCFHandoverStreet" type="text" class="form-control" id="PCFHandoverStreet" placeholder="Street">
            </div>
            <div class="col-2">
                <input name="PCFHandoverNumber" type="text" class="form-control" id="PCFHandoverNumber" placeholder="Number">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="PCFHandoverCity" type="text" class="form-control" id="PCFHandoverCity" placeholder="City">
            </div>
            <div class="col-3">
              <input name="PCFHandoverZIP" type="number" class="form-control" id="PCFHandoverZIP" placeholder="ZIP">
            </div>
            <div class="col-3">
              <input name="PCFHandoverCountry" type="text" class="form-control" id="PCFHandoverCountry" placeholder="Country">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="PCFHandoverLatitude" type="text" class="form-control" id="PCFHandoverLatitude" placeholder="Latitude">
            </div>
            <div class="col-6">
              <input name="PCFHandoverLongitude" type="text" class="form-control" id="PCFHandoverLongitude" placeholder="Longitude">
            </div>
          </div>
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
      <button id="TCF-header-button" class="accordion-button collapsed" type="button"
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
        <!--Process for Greenhouse Gase Emission-->
        <div class="mb-3">
          <label for="TCFProcessesForGreenhouseGasEmissionInATransportService">Process for Greenhouse Gase Emission</label>
          <select name="TCFProcessesForGreenhouseGasEmissionInATransportService" class="form-select d-block w-100"
            id="TCFProcessesForGreenhouseGasEmissionInATransportService">
            <option value="">Choose...</option>
            <option value="0173-1#07-ABU216#001">WTT - Well-to-Tank</option>
            <option value="0173-1#07-ABU215#001">TTW - Tank-to-Wheel</option>
            <option value="0173-1#07-ABU217#001">WTW - Well-to-Wheel</option>
          </select>
          <div class="invalid-feedback">
            Please enter a Process for Greenhouse Gase Emission.
          </div>
        </div>
        <!--TCF Takeover Address-->
        <label for="TCFTakeoverAddress" class="from-lable">Handover Adress</label>
        <div id="TCFTakeoverAddress">
          <div class="mb-2 row">
            <div class="col-10">
                <input name="TCFTakeoverStreet" type="text" class="form-control" id="TCFTakeoverStreet" placeholder="Street">
            </div>
            <div class="col-2">
                <input name="TCFTakeoverNumber" type="text" class="form-control" id="TCFTakeoverNumber" placeholder="Number">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFTakeoverCity" type="text" class="form-control" id="TCFTakeoverCity" placeholder="City">
            </div>
            <div class="col-3">
              <input name="TCFTakeoverZIP" type="number" class="form-control" id="TCFTakeoverZIP" placeholder="ZIP">
            </div>
            <div class="col-3">
              <input name="TCFTakeoverCountry" type="text" class="form-control" id="TCFTakeoverCountry" placeholder="Country">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFTakeoverLatitude" type="text" class="form-control" id="TCFTakeoverLatitude" placeholder="Latitude">
            </div>
            <div class="col-6">
              <input name="TCFTakeoverLongitude" type="text" class="form-control" id="TCFTakeoverLongitude" placeholder="Longitude">
            </div>
          </div>
        </div>
        <!--TCF Handover Address-->
        <label for="TCFHandoverAddress" class="from-lable">Handover Adress</label>
        <div id="TCFHandoverAddress">
          <div class="mb-2 row">
            <div class="col-10">
                <input name="TCFHandoverStreet" type="text" class="form-control" id="TCFHandoverStreet" placeholder="Street">
            </div>
            <div class="col-2">
                <input name="TCFHandoverNumber" type="text" class="form-control" id="TCFHandoverNumber" placeholder="Number">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFHandoverCity" type="text" class="form-control" id="TCFHandoverCity" placeholder="City">
            </div>
            <div class="col-3">
              <input name="TCFHandoverZIP" type="text" class="form-control" id="TCFHandoverZIP" placeholder="ZIP">
            </div>
            <div class="col-3">
              <input name="TCFHandoverCountry" type="text" class="form-control" id="TCFHandoverCountry" placeholder="Country">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFHandoverLatitude" type="text" class="form-control" id="TCFHandoverLatitude" placeholder="Latitude">
            </div>
            <div class="col-6">
              <input name="TCFHandoverLongitude" type="text" class="form-control" id="TCFHandoverLongitude" placeholder="Longitude">
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
// $(document).ready(function () {

//   var assetIDshortInput = $(`#assetIDshort`);
//   var accordionButtonProductCarbonFootprint = $(`#PCF-header-button`);

//   assetIDshortInput.on("input", function () {
//     var inputValue = assetIDshortInput.val();
//     accordionButtonProductCarbonFootprint.text(`Product Carbon Footprint- ${inputValue}`);
//   });
// });

/**
 * Adds the Asset ID to the Transport Carbon Footprint accordion Button
 */
// $(document).ready(function () {

//   var assetIDshortInput = $(`#assetIDshort`);
//   var accordionButtonProductCarbonFootprint = $(`#TCF-header-button`);

//   assetIDshortInput.on("input", function () {
//     var inputValue = assetIDshortInput.val();
//     accordionButtonProductCarbonFootprint.text(`Transport Carbon Footprint- ${inputValue}`);
//   });
// });






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


// Deletion button on the Overview View
$(document).ready(function () {
  // Use querySelectorAll to get all edit buttons
  var deleteButtons = document.querySelectorAll(".deleteButton");

  function buttonClickAction(event) {
    // Retrieve AssetID from the clicked button's data-attribute
    var assetID = event.currentTarget.getAttribute('data-assetid');
    console.log('Clicked delete AssetID JS:', assetID);

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
  deleteButtons.forEach(button => {
    button.addEventListener("click", buttonClickAction);
  });
});

// Edit button on the Overview View
$(document).ready(function () {
  // Use querySelectorAll to get all edit buttons
  var editButtons = document.querySelectorAll(".editButton");

  function buttonClickAction(event) {
    // Retrieve AssetID from the clicked button's data-attribute
    var assetID = event.currentTarget.getAttribute('data-assetid');
    console.log('Clicked edit AssetID JS:', assetID);

    // Send the AssetID to the backend using fetch
    fetch(`/aas/edit/${assetID}`, {
      method: 'GET'
    })
      .then(response => {
        if (response.ok) {
          // If the response is successful, handle as needed
          // For example, redirect to the edit page or do further processing
          window.location.href = `/aas/edit/${assetID}`;
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


/**
 * Populates specified HTML input fields with corresponding values.
 * @param {Array} fieldIds - An array containing the IDs of HTML input fields.
 * @param {Array} values - An array containing values to populate the input fields.
 * @returns {void} - This function does not return anything.
 */
function populateFields(fieldIds, values) {
  fieldIds.forEach(function (fieldId, index) {
    console.log('Values to populate: ' + values);
    var inputElement = document.getElementById(fieldId);
    if (inputElement && values[index]) {
      inputElement.value = values[index] || '';
    }
  });
}

/**
 * Populates various sets of input fields for the edit AAS view based on pre-existing data.
 * 
 * This function executes when the document is ready.
 * It retrieves pre-fill values and IDs for different sections of input fields, populates them accordingly,
 * and creates dynamic UI elements for multiple accordion items based on the retrieved data.
 * 
 * @returns {void} - This function does not return anything.
 */
$(document).ready(function () {
  var editMode = document.getElementById('editMode-container').getAttribute('editMode');

  if (editMode) {

    var formHeading = document.getElementById('formHeadline');

    if (formHeading) {
      // Create a new div element
      var newEditHeading = document.createElement('div');

      newEditHeading.innerHTML = `
      <h4 class="mb-3">AAS Editor</h4>
      Here you can edit a AAS and specify all the datails for the submodels Nameplate, Technical Data and
      Carbon Footprint. Edit the informations you want to and submit by clicking the save button.
    </div>
      `;
      formHeading.parentNode.replaceChild(newEditHeading, formHeading);
    }



    var prefillValuesString = document.getElementById('prefillValues-container').getAttribute('prefillValues');
    var prefillValues = JSON.parse(prefillValuesString);
    var prefillValuesPCFString = document.getElementById('prefillValuesPCF-container').getAttribute('prefillValuesPCF');
    var prefillValuesPCF = JSON.parse(prefillValuesPCFString);
    var prefillValuesTCFString = document.getElementById('prefillValuesTCF-container').getAttribute('prefillValuesTCF');
    var prefillValuesTCF = JSON.parse(prefillValuesTCFString);


    var inputFieldIds = [
      'assetIDshort',
      'assetID',
      'URIOfTheProduct',
      'ManufacturerName',
      'SerialNumber',
      'YearOfConstruction',
      'DateOfManufacture',
      'ManufacturerOrderCode'
    ];

    var inputFieldIdsPCF = [
      'ReferableAssetID',
      'PCFCalculationMethod',
      'PCFCO2eq',
      'PCFQuantityOfMeasureForCalculation',
      'PCFReferenceValueForCalculation',
      'PCFLiveCyclePhase',
      'PCFDescription',
      'PCFHandoverStreet',
      'PCFHandoverNumber',
      'PCFHandoverCity',
      'PCFHandoverZIP',
      'PCFHandoverCountry',
      'PCFHandoverLatitude',
      'PCFHandoverLongitude'
    ];

    var inputFieldIdsTCF = [
      'TCFCalculationMethod',
      'TCFCO2eq',
      'TCFQuantityOfMeasureForCalculation',
      'TCFReferenceValueForCalculation',
      'TCFProcessesForGreenhouseGasEmissionInATransportService',
      'TCFTakeoverStreet',
      'TCFTakeoverNumber',
      'TCFTakeoverCity',
      'TCFTakeoverZIP',
      'TCFTakeoverCountry',
      'TCFTakeoverLatitude',
      'TCFTakeoverLongitude',
      'TCFHandoverStreet',
      'TCFHandoverNumber',
      'TCFHandoverCity',
      'TCFHandoverZIP',
      'TCFHandoverCountry',
      'TCFHandoverLatitude',
      'TCFHandoverLongitude'
    ];

    //Populate static amount of Nameplate and TechnicalData Submodel
    populateFields(inputFieldIds, prefillValues);

    //Populate dynamic amount of ProductCarbonFootprint Data
    for (i = 0; i < (prefillValuesPCF.length / inputFieldIdsPCF.length); i++) {
      if (i !== 0) {
        inputFieldIdsPCF = inputFieldIdsPCF.map(function (fieldId) {
          return fieldId + (i + 1);
        });
        // Create a new accordion item
        var dataContainer = document.getElementById("data-container");
        var dataArray = JSON.parse(dataContainer.getAttribute("data-array"));

        var newPCFItem = `
      <div class="accordion-item">
      <h2 class="accordion-header" id="headingPCF${i + 1}">
        <button id="PCF-header-button" class="accordion-button collapsed" type="button"
          data-bs-toggle="collapse" data-bs-target="#collapsePCF${i + 1}" aria-expanded="false"
          aria-controls="collapsePCF${i + 1}">
          Product Carbon Footprint ${i + 1}
        </button>
      </h2>
      <div id="collapsePCF${i + 1}" class="accordion-collapse collapse" aria-labelledby="headingPCF${i + 1}"
        data-bs-parent="#PCFAccordion">
        <div class="accordion-body">
          <!--Referable Asset ID-->
          <div class="mb-3">
            <label for="ReferableAssetID">ID of the related Asset</label>
            <input name="ReferableAssetID" type="text" class="form-control"
              list="list-environmentServices${i + 1}" id="ReferableAssetID${i + 1}"
              placeholder="https://example.company.de/id/1234">
            <datalist id="list-environmentServices${i + 1}">
            </datalist>
          </div>
          <!--Calculation Method-->
          <div class="mb-3">
            <label for="PCFCalculationMethod">Calculation Method</label>
            <select name="PCFCalculationMethod" class="form-select d-block w-100"
              id="PCFCalculationMethod${i + 1}" required="">
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
            <input name="PCFCO2eq" type="number" class="form-control" id="PCFCO2eq${i + 1}" placeholder="0.00"
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
                id="PCFQuantityOfMeasureForCalculation${i + 1}" placeholder="0" min="0">
            </div>
            <div class="col-2 mb-3">
              <label for="PCFReferenceValueForCalculation">Reference Value</label>
              <select name="PCFReferenceValueForCalculation" class="form-select custom-select d-block"
                id="PCFReferenceValueForCalculation${i + 1}" required="">
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
          <!--Life Cycle Phase-->
          <div class="mb-3">
            <label for="PCFLiveCyclePhase">Life Cycle Phase of the Product</label>
            <select name="PCFLiveCyclePhase" class="form-select d-block w-100" id="PCFLiveCyclePhase${i + 1}"
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
          <div class="mb-3">
            <label for="PCFDescription">Asset Description</label>
            <textarea class="form-control" id="PCFDescription${i + 1}" name="PCFDescription" rows="3"
              value=""></textarea>
          </div>
          <!--Explanatory Statement-->
          <div class="mb-3">
            <label class="form-label" for="ExplanatoryStatement">Explanatory Statement</label>
            <input type="file" class="form-control" id="ExplanatoryStatement${i + 1}" name="ExplanatoryStatement" />
          </div>
          <!--Handover Address-->
          <label for="HandoverAddress" class="from-lable">Handover Adress</label>
          <div id="HandoverAddress">
            <div class="mb-2 row">
              <div class="col-10">
                  <input name="PCFHandoverStreet" type="text" class="form-control" id="PCFHandoverStreet${i + 1}" placeholder="Street">
              </div>
              <div class="col-2">
                  <input name="PCFHandoverNumber" type="text" class="form-control" id="PCFHandoverNumber${i + 1}" placeholder="Number">
              </div>
            </div>
            <div class="mb-2 row">
              <div class="col-6">
                <input name="PCFHandoverCity" type="text" class="form-control" id="PCFHandoverCity${i + 1}" placeholder="City">
              </div>
              <div class="col-3">
                <input name="PCFHandoverZIP" type="number" class="form-control" id="PCFHandoverZIP${i + 1}" placeholder="ZIP">
              </div>
              <div class="col-3">
                <input name="PCFHandoverCountry" type="text" class="form-control" id="PCFHandoverCountry${i + 1}" placeholder="Country">
              </div>
            </div>
            <div class="mb-2 row">
              <div class="col-6">
                <input name="PCFHandoverLatitude" type="text" class="form-control" id="PCFHandoverLatitude${i + 1}" placeholder="Latitude">
              </div>
              <div class="col-6">
                <input name="PCFHandoverLongitude" type="text" class="form-control" id="PCFHandoverLongitude${i + 1}" placeholder="Longitude">
              </div>
            </div>
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
      }
      console.log("i: " + i);
      console.log("start index: " + inputFieldIdsPCF.length * i);
      console.log("end index: " + ((inputFieldIdsPCF.length * (i + 1)) - 1));
      populateFields(inputFieldIdsPCF, prefillValuesPCF.slice(inputFieldIdsPCF.length * i, (inputFieldIdsPCF.length * (i + 1))));
    }

    //Populate dynamic amount of TransportCarbonFootprint Data
    for (i = 0; i < (prefillValuesTCF.length / inputFieldIdsTCF.length); i++) {
      if (i !== 0) {
        inputFieldIdsTCF = inputFieldIdsTCF.map(function (fieldId) {
          return fieldId + (i + 1);
        });

        var newTCFItem = `
    <div class="accordion-item">
    <h2 class="accordion-header" id="headingTCF${i + 1}">
      <button id="TCF-header-button" class="accordion-button collapsed" type="button"
        data-bs-toggle="collapse" data-bs-target="#collapseTCF${i + 1}" aria-expanded="false"
        aria-controls="collapseTCF${i + 1}">
        Transport Carbon Footprint ${i + 1}
      </button>
    </h2>
    <div id="collapseTCF${i + 1}" class="accordion-collapse collapse" aria-labelledby="headingTCF${i + 1}"
      data-bs-parent="#TCFAccordion">
      <div class="accordion-body">
        <!--Calculation Method-->
        <div class="mb-3">
          <label for="TCFCalculationMethod">Calculation Method</label>
          <select name="TCFCalculationMethod" class="form-select d-block w-100"
            id="TCFCalculationMethod${i + 1}" required="">
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
          <input name="TCFCO2eq" type="number" class="form-control" id="TCFCO2eq${i + 1}" placeholder="0.00"
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
              id="TCFQuantityOfMeasureForCalculation${i + 1}" placeholder="0" min="0">
          </div>
          <div class="col-2 mb-3">
            <label for="TCFReferenceValueForCalculation">Reference Value</label>
            <select name="TCFReferenceValueForCalculation" class="form-select d-block w-100"
              id="TCFReferenceValueForCalculation${i + 1}" required="">
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
        <!--Process for Greenhouse Gase Emission-->
        <div class="mb-3">
          <label for="TCFProcessesForGreenhouseGasEmissionInATransportService">Process for Greenhouse Gase Emission</label>
          <select name="TCFProcessesForGreenhouseGasEmissionInATransportService" class="form-select d-block w-100"
            id="TCFProcessesForGreenhouseGasEmissionInATransportService${i + 1}">
            <option value="">Choose...</option>
            <option value="0173-1#07-ABU216#001">WTT - Well-to-Tank</option>
            <option value="0173-1#07-ABU215#001">TTW - Tank-to-Wheel</option>
            <option value="0173-1#07-ABU217#001">WTW - Well-to-Wheel</option>
          </select>
          <div class="invalid-feedback">
            Please enter a Process for Greenhouse Gase Emission.
          </div>
        </div>
        <!--TCF Takeover Address-->
        <label for="TCFTakeoverAddress" class="from-lable">Handover Adress</label>
        <div id="TCFTakeoverAddress">
          <div class="mb-2 row">
            <div class="col-10">
                <input name="TCFTakeoverStreet" type="text" class="form-control" id="TCFTakeoverStreet${i + 1}" placeholder="Street">
            </div>
            <div class="col-2">
                <input name="TCFTakeoverNumber" type="text" class="form-control" id="TCFTakeoverNumber${i + 1}" placeholder="Number">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFTakeoverCity" type="text" class="form-control" id="TCFTakeoverCity${i + 1}" placeholder="City">
            </div>
            <div class="col-3">
              <input name="TCFTakeoverZIP" type="number" class="form-control" id="TCFTakeoverZIP${i + 1}" placeholder="ZIP">
            </div>
            <div class="col-3">
              <input name="TCFTakeoverCountry" type="text" class="form-control" id="TCFTakeoverCountry${i + 1}" placeholder="Country">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFTakeoverLatitude" type="text" class="form-control" id="TCFTakeoverLatitude${i + 1}" placeholder="Latitude">
            </div>
            <div class="col-6">
              <input name="TCFTakeoverLongitude" type="text" class="form-control" id="TCFTakeoverLongitude${i + 1}" placeholder="Longitude">
            </div>
          </div>
        </div>
        <!--TCF Handover Address-->
        <label for="TCFHandoverAddress" class="from-lable">Handover Adress</label>
        <div id="TCFHandoverAddress">
          <div class="mb-2 row">
            <div class="col-10">
                <input name="TCFHandoverStreet" type="text" class="form-control" id="TCFHandoverStreet${i + 1}" placeholder="Street">
            </div>
            <div class="col-2">
                <input name="TCFHandoverNumber" type="text" class="form-control" id="TCFHandoverNumber${i + 1}" placeholder="Number">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFHandoverCity" type="text" class="form-control" id="TCFHandoverCity${i + 1}" placeholder="City">
            </div>
            <div class="col-3">
              <input name="TCFHandoverZIP" type="text" class="form-control" id="TCFHandoverZIP${i + 1}" placeholder="ZIP">
            </div>
            <div class="col-3">
              <input name="TCFHandoverCountry" type="text" class="form-control" id="TCFHandoverCountry${i + 1}" placeholder="Country">
            </div>
          </div>
          <div class="mb-2 row">
            <div class="col-6">
              <input name="TCFHandoverLatitude" type="text" class="form-control" id="TCFHandoverLatitude${i + 1}" placeholder="Latitude">
            </div>
            <div class="col-6">
              <input name="TCFHandoverLongitude" type="text" class="form-control" id="TCFHandoverLongitude${i + 1}" placeholder="Longitude">
            </div>
          </div>
        </div>
        
      </div>
    </div>
  </div>
                    `;

        // Append the new accordion item to the accordion container
        $("#TCFAccordion").append(newTCFItem);
      }
      console.log("i: " + i);
      console.log("start index: " + inputFieldIdsTCF.length * i);
      console.log("end index: " + ((inputFieldIdsTCF.length * (i + 1)) - 1));
      populateFields(inputFieldIdsTCF, prefillValuesTCF.slice(inputFieldIdsTCF.length * i, (inputFieldIdsTCF.length * (i + 1))));
    }
  }
});




/**
 * Model activation for the deletion confirmation 
 */
$(document).ready(function () {
  const myModal = document.getElementById('myModal')
  const myInput = document.getElementById('myInput')

  myModal.addEventListener('shown.bs.modal', () => {
    myInput.focus()
  })
});


