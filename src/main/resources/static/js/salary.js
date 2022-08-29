$(document).ready(function () {

    let phNum;
    let phName;
    let employeeToChangePh;
    const allPharmacies = []
    let $dateField = $("#datepicker");
    let $pharmacyField = $("#choose-pharmacy");
    let $employeeField = $("#choose-employee");
    let $hoursField = $("#hours");
    let $changePharmacyBtn = $("#change-ph-button");
    let $savePharmacyBtn = $("#save-pharmacy");
    let toastLive = document.getElementById('liveToast')
    let toast = new bootstrap.Toast(toastLive)
    let $inputsZone = $("#new-employee-inputs");
    let $toastBody = $("#toast-body");
    let newEmployeeName;
    let newEmployeeSurname;
    let newEmployeeRole;
    let newEmployeePharmacy;


    window.addEventListener('load', showPharmacies)


    function showPharmacies() {
        $.getJSON("data/api/all_pharmacies", function (pharmacies) {
            $.each(pharmacies, function (idx, ph) {
                if (ph["pharmacyNumber"] !== 0) {
                    allPharmacies.push(ph)
                    $pharmacyField.append(`<option value="${ph["pharmacyNumber"]}">${ph["pharmacy"]}</option>`)
                }
            })
        })
    }

    $pharmacyField.change(() => {
        $employeeField.empty()
        phNum = $pharmacyField.val()
        phName = $("#choose-pharmacy option:selected").text();
        $.get("salary/api/get_employees", {
            phNum: phNum
        }, function (employees) {
            enableEmployeeChoice()
            insertEmployees(employees)
        })
    })

    function enableEmployeeChoice() {
        $employeeField.removeAttr("disabled")
        $changePharmacyBtn.removeAttr("disabled")
        let date = new Date();
        $dateField.removeAttr("disabled").val(`0${date.getMonth()}-${date.getFullYear()}`)
        $hoursField.removeAttr("disabled")
    }

    function insertEmployees(employees) {
        $.each(employees, function (idx, emp) {
            $employeeField.append(`<option value="${emp["id"]}" data-role="${emp["role"]}">${emp["name"]} ${emp["surname"]}</option>`)
        })
    }


    $changePharmacyBtn.click(() => {
        employeeToChangePh = $employeeField.val()
        $("#change-ph").empty()
        $.each(allPharmacies, function (idx, ph) {
            if (ph["pharmacyNumber"] !== parseInt(phNum)) {
                $("#change-ph").append(`<option value="${ph["pharmacyNumber"]}">${ph["pharmacy"]}</option>`)
            }
        })
    })


    $savePharmacyBtn.click(() => {
        let phChangeOn = $("#change-ph").val();
        phNum = phChangeOn
        $pharmacyField.val(phChangeOn)
        $.post("salary/api/employee_to_change_ph", {
            empNum: employeeToChangePh,
            changeOn: phChangeOn
        })
        setTimeout(() => {
            getAllEmployees();
        }, 2000)

    })

    function getAllEmployees() {
        $employeeField.empty()
        phNum = $pharmacyField.val()
        phName = $("#choose-pharmacy option:selected").text();
        $.get("salary/api/get_employees", {
            phNum: phNum
        }, function (employees) {
            enableEmployeeChoice()
            insertEmployees(employees)
        })
    }


    $dateField.datepicker({
        format: "mm-yyyy",
        startView: "months",
        minViewMode: "months",
        language: "ru"
    });


    $hoursField.keyup(function () {
        let hours = $hoursField.val();
        if (!hours) {
            $("#send-button").fadeOut(370)
        } else if (hours < 10) {
            $("#send-button").removeClass("d-none").hide().fadeIn(1000)
        }
    })

    $("#send-button").click(() => {
        let $employeeSelected = $("#choose-employee option:selected");
        let position = $employeeSelected.attr("data-role")
        switch (position) {
            case "PH":
                position = "Фармацевт"
                break;
            case "ZAV":
                position = "Заведующая"
                break;
            case "RAZB":
                position = "Разборка"
                break;
            default:
                position = "Провизор"
        }
        $("#approve-pharmacy").text(`${phNum}, ${phName}`)
        $("#approve-employee").text($employeeSelected.text())
        $("#approve-position").text(position)
        $("#approve-date").text($dateField.val())
        $("#approve-hours").text($hoursField.val())
    })

    $("#approve-send-data").click(() => {
        let $checkbox = $("#approve-checkmark");
        let $employeeSelected = $("#choose-employee option:selected");
        let $approveCheckmark = $("#approve-checkmark-span");
        if ($checkbox.is(":checked")) {
            $.post("salary/api/send_data_for_salary", {
                employeeId: $employeeSelected.val(),
                phNum: phNum,
                date: $dateField.val(),
                hours: $hoursField.val()
            })
            $("#approveModal").modal("hide")
            showNotification(`Часы по ${$employeeSelected.text()} отправлены`)
            $checkbox.prop("checked", false)
        } else {
            if (!$(".invalid-feedback").length) {
                $checkbox.addClass("is-invalid")
                $approveCheckmark.append('<div class="invalid-feedback">Необходимо подтвердить</div>')
            }
        }


    })

    $("#new-employee-button").click(() => {
        if (!$inputsZone.is(":parent")) {
            $inputsZone.append(`<div class="input-group mb-3">
                <label class="input-group-text" for="new-employee-name">Имя</label>
                <input required type="text" class="form-control" id="new-employee-name"/>
                <label class="input-group-text" for="new-employee-surname">Фамилия</label>
                <input required type="text" class="form-control" id="new-employee-surname"/>
            </div>
<div class="input-group mb-3">
                <label class="input-group-text" for="new-employee-pharmacy">Аптека</label>
                 <select class="form-select" id="new-employee-pharmacy">
                 </select>
                <label class="input-group-text" for="new-employee-role">Должность</label>
                <select class="form-select" id="new-employee-role">
                <option value="PH">Фармацевт</option>
                <option value="RAZB">Разборщик</option>
                <option value="ZAV">Заведующая</option>
                <option value="PROV">Провизор</option>
                </select>
            </div>
<div class="d-flex mb-5 justify-content-center">
<button id="new-employee-save-button" class="btn btn-sm btn-outline-success">Сохранить</button>
</div>
`).hide().slideDown(270);
            $("#close-add-new-employee").removeClass("d-none").hide().fadeIn(270)
            $.each(allPharmacies, addPharmaciesToChoose)
            newEmployeeName = $(document.getElementById("new-employee-name"));
            newEmployeeSurname = $(document.getElementById("new-employee-surname"));
            newEmployeeRole = $(document.getElementById("new-employee-role"));
            newEmployeePharmacy = $(document.getElementById("new-employee-pharmacy"));
            newEmployeeName.keyup(firstLetterCapitalize)
            newEmployeeSurname.keyup(firstLetterCapitalize)
        }


    })

    $(document).on("click", "#new-employee-save-button", saveNewEmployee)

    function saveNewEmployee() {
        if (!newEmployeeName.val() || !newEmployeeSurname.val()) {
            if (!newEmployeeName.val()) {
                newEmployeeName.addClass("is-invalid");
            } else {
                newEmployeeName.removeClass("is-invalid")
                newEmployeeName.addClass("is-valid")
            }
            if (!newEmployeeSurname.val()) {
                newEmployeeSurname.addClass("is-invalid");
            } else {
                newEmployeeSurname.removeClass("is-invalid");
                newEmployeeSurname.addClass("is-valid");
            }

        } else {
            $.post("salary/api/save_new_employee", {
                name: newEmployeeName.val(),
                surname: newEmployeeSurname.val(),
                role: newEmployeeRole.val(),
                pharmacy: newEmployeePharmacy.val()
            })
            $inputsZone.slideUp(200).empty()
            $("#close-add-new-employee").fadeOut(200)
            showNotification(`${newEmployeeName.val()} ${newEmployeeSurname.val()} добавлена`)
        }

    }

    function addPharmaciesToChoose(idx, ph) {
        $("#new-employee-pharmacy").append(`<option value="${ph["pharmacyNumber"]}">${ph["pharmacy"]}</option>`)
    }

    function showNotification(text) {
        $toastBody.empty()
        $toastBody.append(text)
        toast.show()
    }

    function firstLetterCapitalize() {
        this.value = this.value.substring(0, 1).toUpperCase() + this.value.substring(1).toLowerCase();
        if (event.key === "ArrowRight") {
            newEmployeeSurname.focus();
        }
    }

    $("#close-add-new-employee").click(() => {
        $inputsZone.slideUp(200).empty()
        $("#close-add-new-employee").fadeOut(200)
    })


})