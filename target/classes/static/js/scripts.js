/**
 * Adds a new accordion item to the AAS/Form page when the "addAccordionItem" button is clicked.
 * Each new item is added with an incremented Carbon Footprint number.
 */
$(document).ready(function () {
    var carbonFootprintCounter = 1;
    $("#addAccordionItem").click(function () {
        // Create a new accordion item
        carbonFootprintCounter++;
        var newAccordionItem = `
    <div class="accordion-item">
        <h2 class="accordion-header" id="headingCarbonFootprint${carbonFootprintCounter}">
            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                data-bs-target="#collapseCarbonFootprint${carbonFootprintCounter}" aria-expanded="false"
                aria-controls="collapseCarbonFootprint${carbonFootprintCounter}">
                Carbon Footprint${carbonFootprintCounter}
            </button>
        </h2>
        <div id="collapseCarbonFootprint${carbonFootprintCounter}" class="accordion-collapse collapse"
            aria-labelledby="headingCarbonFootprint${carbonFootprintCounter}" data-bs-parent="#accordionExample">
            <div class="accordion-body">

                <h5 class="mt-5">Product Carbon Footprint</h5>
                <hr class="mb-4">
                    <div class="mb-3">
                        <label for="PCFCalculationMethod">Calculation Method</label>
                        <select name="PCFCalculationMethod" class="form-select d-block w-100" id="PCFCalculationMethod"
                            required="">
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

                    <div class="mb-3">
                        <label for="PCFCO2eq">CO2 Equivalent</label>
                        <input name="PCFCO2eq" type="number" class="form-control" id="PCFCO2eq" placeholder="0.00" min="0">
                            <div class="invalid-feedback">
                                A CO2 equivalent is missing.
                            </div>
                    </div>

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





                    <div class="mb-3">
                        <label for="PCFLiveCyclePhase">Live Cycle Phase of the Product</label>
                        <select name="PCFLiveCyclePhase" class="form-select d-block w-100" id="PCFLiveCyclePhase"
                            required="">
                            <option value="">Choose...</option>
                            <option value="0173-1#07-ABU208#001">A1 – raw material supply (and upstream production)</option>
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



                    <h5 class="mt-5">Transport Carbon Footprint</h5>
                    <hr class="mb-4">



                        <div class="mb-3">
                            <label for="TCFCalculationMethod">Calculation Method</label>
                            <select name="TCFCalculationMethod" class="form-select d-block w-100" id="TCFCalculationMethod"
                                required="">
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

                        <div class="mb-3">
                            <label for="TCFCO2eq">CO2 Equivalent</label>
                            <input name="TCFCO2eq" type="number" class="form-control" id="TCFCO2eq" placeholder="0.00" min="0">
                                <div class="invalid-feedback">
                                    A CO2 equivalent is missing.
                                </div>
                        </div>


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
        $(".accordion").append(newAccordionItem);
    });
});
