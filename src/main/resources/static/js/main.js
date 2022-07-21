$(document).ready(function () {
    window.addEventListener("load", getTheInfo)


    function getTheInfo(newDates = false) {
        console.log("Работаем")
        if (getCookie("costs")) {
            checkOnMissedInns();
        }
        if (getCookie("order-statement") || newDates === true) {
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
                    }, 2000)}
        })
    }

    function checkOnMissedInns() {
        $.ajax({
            url: "api/missed-inns",
            dataType: "json",
            success: function (allMissedInns, textStatus, xhr) {
                console.log(allMissedInns.length);
                if (xhr.status === 204) {
                    createButtonsReadyAndChooseDate();
                    return
                }
                else {
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
            $.get("api/missed-inns", function(missedInns) {
                if (missedInns.length === 0) {
                    console.log("Удаляем cookie costs")
                    $("#missedInn").modal("hide");
                    deleteCookie("costs");
                }
            })
        }
    })

    $("#dates-chosen").click(function() {
        let from = $("#date-from").val();
        let to = $("#date-to").val();
        $.post("/order_statement", {
            from: from,
            to: to
        })
        $("#choose-dates").remove();
        $("#buttons-area").append(`<div class="loader me-2" id="loading-circle"></div>`)
        deleteCookie("costs");
        localStorage.setItem("statement_ordered", "true")
        getTheInfo(true);
    })




    function removeLoading() {
        $("#loading-circle").remove();
    }

    function createButtonError() {
        $("#buttons-area")
            .append('<button class="btn btn-outline-danger me-2" type="button" data-bs-toggle="modal" data-bs-target="#staticBackdrop">Выбрать даты</button>')
            .append('<button class="btn btn-danger shadow me-2" disabled>Ошибка банка</button>')
    }
    function createButtonCheckMissedInn() {
        $("#buttons-area")
            .append('<button class="btn btn-outline-danger me-2" type="button" data-bs-toggle="modal" data-bs-target="#staticBackdrop">Выбрать даты</button>')
            .append('<button class="btn btn-warning shadow me-2" type="button" data-bs-toggle="modal" data-bs-target="#missedInn">Нажмите для добавления ИНН</button>')
    }
    function createButtonsReadyAndChooseDate() {
        $("#buttons-area")
            .append('<button class="btn btn-outline-danger me-2" type="button" data-bs-toggle="modal" data-bs-target="#staticBackdrop">Выбрать даты</button>')
            .append('<button class="btn btn-success shadow me-2" disabled>Готово</button>')
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

    $(".anim-bigger").hover(function () {
        $(this).animate({width: $(this).width() + 10}, {queue: false});
    }, function () {
        $(this).animate({width: $(this).width()}, {queue: false});
    });

    $("#5-boxes").css("display", "flex").hide().fadeIn(1000)
    $("#pharmacy-boxes").css("display", "flex").hide().fadeIn(2000)
    $("#nav-buttons").css("display", "flex").hide().fadeIn(1000)

    $('#amount1,#amount2').keyup(function () {
        let total = parseInt($('#amount1').val()) + parseInt($('#amount2').val());
        $('#total').text(total);
    });
    let contrs;


    $("#change-test").change(function () {
        alert("Handler for .change() called.");
    })

    $("#pgbr").click(function () {
        $("#pgbr").progressbar({
            value: 37
        });
    });

    let list = ["Hi", "My", "Name", "Is", "List"]

    $("#box").click(function () {
        $.each(list, function (index, value) {
            $("#box").append(`<p>${value}</p>`)

        })

    })
});


