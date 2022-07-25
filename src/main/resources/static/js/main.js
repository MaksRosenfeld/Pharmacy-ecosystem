$(document).ready(function () {


    window.addEventListener("load", getTheInfo)


    function getTheInfo(newDates = false) {
        if (getCookie("costs")) {
            console.log("Проверяем недостающие ИНН")
            checkOnMissedInns();
        }
        if (getCookie("order-statement") || newDates === true) {
            console.log("Сейчас начнется запрос выписки")
            setTimeout(sendRequestToCheckStatus, 3000)
        }
    }


    function sendRequestToCheckStatus() {
        let status;
        console.log("Выписка в процессе создания")
        $.ajax({
            url: "/api/check",
            dataType: "json",
            success: function (data) {
                status = data["data"]["status"] // статус выписки
                console.log(status)
                if (status === "IN_PROGRESS" || status === "NEW") {
                    setTimeout(() => {
                        console.log("Проверяю статус снова");
                        $.ajax(this);
                    }, 33000)
                } else if (status === "SUCCESS") {
                    console.log("Выписка готова\nУдаляем cookie order-statement")
                    removeLoading();
                    deleteCookie("order-statement");
                    checkOnMissedInns();
                } else if (status === "ERROR") {
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
            url: "api/missed-inns",
            dataType: "json",
            success: function (allMissedInns, textStatus, xhr) {
                // console.log(allMissedInns.length);
                if (xhr.status === 204) {
                    deleteCookie("costs")
                    createButtonsReadyAndChooseDate();
                    return
                } else {
                    createButtonCheckMissedInn();
                    let allCategories = [];
                    $.ajax({
                        url: "/api/all-categories",
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
            $.post("/add_new_contragent_from_missed_inn",
                {
                    inn: innData.val(),
                    name: nameData.val(),
                    category: categoryData.val(),
                    exclude: excludeData
                })
            $.get("api/missed-inns", function (missedInns, textStatus, xhr) {
                if (xhr.status === 204) {
                    console.log("Удаляем cookie costs")
                    $("#missedInn").modal("hide");
                    deleteCookie("costs");
                    createButtonsReadyAndChooseDate();
                }
            })

        }
    })

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

    function createButtonsReadyAndChooseDate() {
        $("#buttons-area")
            .append('<button class="choose-date-range newly-created-button btn btn-outline-danger me-2" type="button">Выбрать даты</button>')
            .append('<button class="newly-created-button btn btn-success shadow me-2" disabled>Готово</button>')
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
            $.post("/order_statement", {
                from: start.format('YYYY-MM-DD'),
                to: end.format('YYYY-MM-DD')
            })
            $(".choose-date-range").remove();
            $("#buttons-area").append(`<div class="loader me-2" id="loading-circle"></div>`)
            deleteCookie("costs");
            getTheInfo(true);
            console.log('Выбранные даты: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
        });
    }
});


