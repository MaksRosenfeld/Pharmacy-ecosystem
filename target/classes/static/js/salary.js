$(document).ready(function () {

    let ph_num;
    let empNum;
    let emp;
    const allPharms = []
    const allEmps = []

    $.getJSON("/api/all-pharmacies", getAllPharmacies)
    $.getJSON("/api/all-employees", getAllEmployees)

    function getAllPharmacies(pharmacies) {
        $.each(pharmacies, function (idx, ph) {
            if (ph["pharmacyNumber"] !== 0) {
                allPharms.push(ph)
                $("#change-test").append(`<option value="${ph["pharmacyNumber"]}">${ph["pharmacy"]}</option>`)
            }
        })
    }

    function getAllEmployees(employees) {
        $.each(employees, function (idx, emp) {
            allEmps.push(emp)
        })
    }


    function getCorrectEmps() {
        $(".names").remove()

        $("#no_data").removeAttr("disabled")

        $.each(allEmps, function (idx, emp) {
            empNum = emp["pharmacyNumber"]["pharmacyNumber"]
            if (ph_num === empNum)
                $("#select_name").append(`<option class="names" value="${emp["id"]}">${emp["name"]}</option>`)
        })
    }

    function getCorrectPharms() {
        $(".pharmacies").remove()
        $.each(allPharms, function (idx, ph) {
            if (ph_num !== ph["pharmacyNumber"]) {
                $("#dropdown-pharms").append(
                    `<select><p class="dropdown-item">${ph["pharmacyNumber"]}</p></select>`
                )
            }
        })
    }


    $("#change-test").change(function () {
        $("#select_name").removeAttr("disabled")
        $("#change-ph-button").removeAttr("disabled")
        ph_num = parseInt($("#change-test option:selected").val())
        getCorrectEmps()
        getCorrectPharms()
        $("#ph_data, #emp_data").remove()
        $("#body-result").append(`
<tr id="ph_data">
<th>Аптека №:</th>
<td>${ph_num}</td>
</tr>`)
    })

    $("#select_name").click(function () {
        $("#no_data").attr("disabled", "disabled")
    })

    $("#select_name").change(function () {
        $("#emp_data").remove()
        $("#datepicker").removeAttr("disabled")
        emp = $("#select_name option:selected").text()
        $("#body-result").append(
            `<tr id="emp_data">
<th>Фармацевт:</th>
<td>${emp}</td>
</tr>`)
    })

    $("#change-ph-button").click(function () {
        $.each(allPharms, function (idx, ph) {
            if (parseInt($("#change-test option:selected").val()) !== ph["pharmacyNumber"]) {
                $("#change_ph").append(`
            <option value="${ph["pharmacyNumber"]}">${ph["pharmacy"]}</option>`)
            }
        })
    })

    $("#datepicker").datepicker({
        format: "MM-yyyy",
        startView: "months",
        minViewMode: "months",
        language: "ru"
    });

    $("#datepicker").change(function () {
        $("#hours").removeAttr("disabled")
        $("#date_data").remove()
        $("#body-result").append(`
        <tr id="date_data">
        <th>Дата:</th>
        <td>${$(this).val()}</td>
        </tr>
        `)
    })

    $("#hours").change(function () {
        $("#hours_data").remove()
        $("#body-result").append(`
        <tr id="hours_data">
        <th>Кол-во часов:</th>
        <td>${$(this).val()} ч.</td>
        </tr>
        `)
        $("#send_info").removeClass("d-none").hide().fadeIn(500)
    })


})