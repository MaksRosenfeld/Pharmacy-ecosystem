import {buildChart} from "./charts.js"

$(document).ready(function () {

    let table;


    window.addEventListener('load', getTheInfo)


    function getTheInfo(newDates = false) {
        if (getCookie("costs")) {
            console.log("Проверяем недостающие ИНН")
            checkOnMissedInns();
        }
        if (getCookie('order-statement') || newDates === true) {
            console.log("Сейчас начнется запрос выписки")
            setTimeout(sendRequestToCheckStatus, 3000);
        }
        if (getCookie("check-costs")) {
            console.log("Показываю расходы")
            ableToShowCosts();
        }
    }


    function sendRequestToCheckStatus() {
        let bankStatus;
        let oneCStatus;
        console.log("Выписка в процессе создания")
        $.ajax({
            url: "data/api/check_statement_status",
            dataType: "json",
            success: function (data) {
                bankStatus = data["bankStatus"] // статус выписки
                oneCStatus = data["oneCStatus"] // статус 1С
                console.log(`Банк: ${bankStatus}\n1С: ${oneCStatus}`)
                if (oneCStatus === "SUCCESS") createOneCReady();
                if (bankStatus === "IN_PROGRESS" || bankStatus === "NEW") {
                    setTimeout(() => {
                        console.log("Проверяю статус снова");
                        $.ajax(this);
                    }, 33000)
                } else if (bankStatus === "SUCCESS") {
                    console.log("Выписка готова\nУдаляем cookie order-statement\nСоздаем check-costs")
                    removeNewlyCreatedAndThButtons();
                    deleteCookie("order-statement");
                    setCookie("check-costs", "true", {"max-age": 3600})
                    $.ajax({
                        method: "post",
                        url: "data/api/parse_statements",
                        dataType: "json",
                        success: function (data, textStatus, xhr) {
                            console.log("Начинаем проверку")
                            if (xhr.status === 202) {
                                console.log("Проверяю на отсутствие ИНН")
                                checkOnMissedInns();
                            }
                        }
                    })

                } else if (bankStatus === "ERROR") {
                    console.log("Ошибка на стороне банка\nУдаляем cookie order-statement")
                    removeLoading()
                    createButtonError()
                    deleteCookie("order-statement");
                }
            },
            error: function (jqXHR) {
                setTimeout(() => {
                    if (jqXHR.status === 500) $.ajax(this);
                }, 2000)
            }
        })
    }

    function checkOnMissedInns() {
        $.ajax({
            url: "data/api/check_missed_inns",
            dataType: "json",
            success: function (allMissedInns, textStatus, xhr) {
                if (xhr.status === 204) {
                    $.ajax({
                        method: "post",
                        url: "data/api/count_all_finance_data"
                    })
                    deleteCookie("costs")
                    createButtonsReadyAndChooseDate();
                    ableToShowCosts();
                    buildAllGraphs();
                    $("#pharmacy-results").css("display", "flex").hide().fadeIn(3000);
                    return
                } else {
                    createButtonCheckMissedInn();
                    let allCategories = [];
                    $.ajax({
                        url: "data/api/all_categories",
                        dataType: "json",
                        async: false,
                        success: function (categoriesData) {
                            $.each(categoriesData, (idx, d) => {
                                allCategories.push(d)
                            })
                        }
                    });
                    console.log(`Найдены недостающие ИНН в количестве ${allMissedInns.length}`)
                    $.each(allMissedInns, function (idx, contragent) {
                        let nameWithoutQuotes = contragent["name"].replaceAll("\"", "");
                        $("#inn-in-table")
                            .append(`<tr id="tr${contragent['inn']}">
<th><input type="hidden" value="${contragent['inn']}" id="inn${contragent['inn']}">${contragent['inn']}</input></th>
<td><input type="hidden" value="${nameWithoutQuotes}" id="name${contragent['inn']}">${nameWithoutQuotes}</input></td>
<td>
<select id="selector${contragent['inn']}" required class="form-select form-select-sm category-option"></select>
</td>
<td class="text-center">
<input class="form-check-input" type="checkbox" role="switch" id="exclude${contragent['inn']}" value="true">
</td>
<td>
<button type="submit" id="${contragent["inn"]}" class="add-contr btn btn-outline-danger btn-sm">Добавить</button>
</td>
</tr>`)
                    })
                    $.each(allCategories, createOption)
                    $(".category-option").prepend("<option value='' selected disabled>Выбери категорию</option>")
                    $("#missedInn").modal("show");
                }
            }
        })
    }

    $(document).on("click", ".add-contr", function () {
        let commonPart = this.id;
        let innData = $(document.getElementById(`inn${commonPart}`));
        let nameData = $(document.getElementById(`name${commonPart}`));
        let excludeData = $(document.getElementById(`exclude${commonPart}`)).is(":checked");
        let categoryData = $(document.getElementById(`selector${commonPart}`));
        if (categoryData.val() === null) {
            categoryData.addClass("error-input");
        } else {
            console.log("Добавляем нового контрагента")
            categoryData.removeClass("error-input")
            $(`#tr${this.id}`).hide(200);
            $.post("data/api/add_new_contragent_from_missed_inn",
                {
                    inn: innData.val(),
                    name: nameData.val(),
                    category: categoryData.val(),
                    exclude: excludeData
                })
            $.get("data/api/amount_of_missed_inns", function (missedInns, textStatus, xhr) {
                if (xhr.status === 204) {
                    console.log("Удаляем cookie costs")
                    $("#missedInn").modal("hide");
                    deleteCookie("costs");
                    createButtonsReadyAndChooseDate();
                    $.post("data/api/count_all_finance_data")
                    buildAllGraphs();
                    $("#pharmacy-results").css("display", "flex").hide().fadeIn(3000);

                }
            })

        }
    })

    function ableToShowCosts() {
        table = $("#table-costs").DataTable({
            retrieve: true,
            autoWidth: false,
            ajax: {url: "data/api/all_costs", dataSrc: ""},
            columns: [
                {data: "inn"},
                {data: "name", width: "40%"},
                {data: "categoryId.category"},
                {data: "amount", render: $.fn.dataTable.render.number(' ', ',', 2, null, " р.")}
            ],
            language: {
                lengthMenu: "Показать по _MENU_",
                paginate: {
                    first: "First",
                    last: "Last",
                    next: "След.",
                    previous: "Пред."
                },
                info: "Кол-во записей: _START_ - _END_ из _TOTAL_",
                search: "Фильтр:",

            }

        })


    }

    function removeNewlyCreatedAndThButtons() {
        $(".th-button, .newly-created-button").remove();

    }


    function removeLoading() {
        $("#loading-circle").remove();
    }

    function createButtonError() {
        $("#buttons-area")
            .append('<button class="choose-date-range newly-created-button btn btn-outline-danger me-2" type="button">Выбрать даты</button>')
            .append('<button class="newly-created-button btn btn-danger shadow me-2" disabled>Ошибка банка</button>')
    }

    function createButtonCheckMissedInn() {
        $("#buttons-area")
            .append('<button class="choose-date-range newly-created-button btn btn-outline-danger me-2" type="button">Выбрать даты</button>')
            .append('<button class="newly-created-button btn btn-warning shadow me-2" type="button" data-bs-toggle="modal" data-bs-target="#missedInn">Добавить ИНН</button>')
    }

    function createOneCReady() {
        if ($("#oneCLoading").length) {
        } else {
            $(".one-c-loading").remove();
            $("#buttons-area")
                .append('<button id="oneCLoading" class="newly-created-button shadow btn btn-warning me-2" type="button" disabled>1С Готово</button>')
        }

    }

    function createButtonsReadyAndChooseDate() {
        $("#buttons-area")
            .append('<button class="choose-date-range newly-created-button btn btn-outline-danger me-2" type="button">Выбрать даты</button>')
            .append('<button class="newly-created-button btn btn-success shadow me-2" disabled>Готово</button>')
        $("#buttons-for-data")
            .append('<button class="newly-created-button btn btn-warning shadow me-2" type="button" data-bs-toggle="modal" data-bs-target="#listOfCosts">Смотреть расходы</button>')
    }

    function create1CReadyAndLoadingWarningButtons() {
        $("#buttons-area")
            .append('<span><button class="newly-created-button loading-dots btn btn-outline-warning disabled me-2" type="button">Загрузка банка</button></span>')
            .append('<span><button class="newly-created-button shadow btn btn-success me-2" type="button">1C готово</button></span>')

    }


    function createOption(idx, category) {
        $(".category-option").append(`<option value="${category["id"]}">${category["category"]}</option>`)
    }

    function getCookie(name) {
        let matches = document.cookie.match(new RegExp(
            "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    }

    function setCookie(name, value, options = {}) {

        options = {
            path: '/',
            ...options
        };

        if (options.expires instanceof Date) {
            options.expires = options.expires.toUTCString();
        }

        let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

        for (let optionKey in options) {
            updatedCookie += "; " + optionKey;
            let optionValue = options[optionKey];
            if (optionValue !== true) {
                updatedCookie += "=" + optionValue;
            }
        }

        document.cookie = updatedCookie;
    }

    function deleteCookie(name) {
        setCookie(name, "", {
            'max-age': -1
        })
    }


    $("#5-boxes").css("display", "flex").hide().fadeIn(1000)
    $("#pharmacy-boxes").css("display", "flex").hide().fadeIn(2000)
    $("#nav-buttons").css("display", "flex").hide().fadeIn(1000)

    $('#amount1,#amount2').keyup(function () {
        let total = parseInt($('#amount1').val()) + parseInt($('#amount2').val());
        $('#total').text(total);
    });


    $(document).on("focus", ".choose-date-range", chooseDateRange)

    function createLoadings() {
        $("#buttons-area")
            .append(`
        <button class="newly-created-button shadow btn btn-primary me-2" type="button" disabled>
        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        Открытие
        </button>
        `)
            .append(`
        <button class="one-c-loading newly-created-button shadow btn btn-warning me-2" type="button" disabled>
        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        1С
        </button>`)

    }

    function chooseDateRange() {
        $('.choose-date-range').daterangepicker({
            "locale": {
                "format": "DD.MM.YYYY",
                "separator": " - ",
                "applyLabel": "Считать",
                "cancelLabel": "Отмена",
                "fromLabel": "С",
                "toLabel": "По",
                "customRangeLabel": "Custom",
                "weekLabel": "Н",
                "daysOfWeek": [
                    "Вс",
                    "Пн",
                    "Вт",
                    "Ср",
                    "Чт",
                    "Пт",
                    "Сб"
                ],
                "monthNames": [
                    "Январь",
                    "Февраль",
                    "Март",
                    "Апрель",
                    "Май",
                    "Июнь",
                    "Июль",
                    "Август",
                    "Сентябрь",
                    "Октябрь",
                    "Ноябрь",
                    "Декабрь"
                ],
                "firstDay": 1
            },
            "showCustomRangeLabel": false,
            "alwaysShowCalendars": true,
            "startDate": "19.06.2022",
            "endDate": "22.06.2022",
            "opens": "right",
            "drops": "auto",
            "applyButtonClasses": "btn-warning shadow"
        }, function (start, end, label) {
            removeNewlyCreatedAndThButtons();
            $("#inn-in-table").empty();
            $.post("/data/api/order_bank_statements", {
                from: start.format('YYYY-MM-DD'),
                to: end.format('YYYY-MM-DD')
            })
            $(".choose-date-range").remove();
            createLoadings();
            deleteCookie("costs");
            deleteCookie("check-costs");
            // if (table !== null) table.destroy();
            getTheInfo(true);
            console.log('Выбранные даты: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
        });
    }

    function buildAllGraphs() {
        $.getJSON("/data/api/all_pharmacies", function (allPharmacies) {
            $.each(allPharmacies, function (idx, pharmacy) {
                let phNum = pharmacy["pharmacyNumber"];
                let id = `phChart${phNum}`
                $("#pharmacy-results").append(`
                <div class="d-flex shadow-on-hover rounded p-1 position-relative col-lg-6 col-md-12 col-sm-12 my-2">
                <div class="d-flex align-items-center justify-content-center col-1 rounded side-shadow bg-danger">
                <div class="text-white"><h4>${phNum}</h4></div>
               </div>
                <div class="col-11">
                <canvas class="chartable" id="${id}"></canvas>
                </div>
                </div>
                `)
                let element = document.getElementById(`${id}`).getContext('2d');
                if (phNum === 0) {
                    $.get("/data/api/office_result", function (result) {
                        buildChart(element, "Данные по офису", result["turnOver"],
                            result["grossProfit"],
                            result["netProfit"]);
                    })
                } else {
                    $.getJSON("/data/api/all_pharmacy_results", function (allResults) {
                        $.each(allResults, function (idx, pharmacy) {
                            if (phNum === pharmacy["pharmacy"]["pharmacyNumber"]) {
                                buildChart(element, `Данные аптеки №${phNum}`, pharmacy["turnOver"],
                                    pharmacy["grossProfit"], pharmacy["netProfit"]);
                            }
                        })


                    })
                }


            })
        })

    }

});



