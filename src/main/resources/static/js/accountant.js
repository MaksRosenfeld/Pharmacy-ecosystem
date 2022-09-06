$(document).ready(() => {

    let zavBonus = 8000;
    let workingDayHoursRAZB = 9;
    let popoverPayedColumn = 6;
    let popoverTakenColumn = 7;


    let table = $('#table_id').DataTable({
        initComplete: loadDates,
        retrieve: true,
        autoWidth: false,
        ajax: {url: "salary/api/get_all_salaries", dataSrc: ""},
        columns: [
            {
                className: 'dt-control',
                orderable: false,
                data: null,
                defaultContent: '',
            },
            {
                data: "employee.name", render: function getNameAndEmail(data, type, row) {
                    return row.employee.name + " " + row.employee.surname;
                }
            },
            {data: "pharmacy.pharmacyNumber"},
            {data: "date", render: $.fn.dataTable.render.moment('YYYY-MM-DD', 'MMMM, YYYY', 'ru')},
            {data: "pharmacy.salarySumPH", render: function(data, type, row) {
                if (row.employee.role.role === "RAZB") {
                    let formattedRazb = $.fn.dataTable.render
                        .number(' ', ',', 2, null, " р.")
                        .display(row.pharmacy.salarySumRAZB);
                    return formattedRazb;
                } else {
                    let formattedElse = $.fn.dataTable.render
                        .number(' ', ',', 2, null, " р.")
                        .display(row.pharmacy.salarySumPH);
                    return formattedElse;
                }
                }},
            {
                data: "hours", render: function (data, type, row) {
                    if (row.employee.role.role === "RAZB") {
                        return `${row.hours} (${row.workingDays})`;
                    } else {
                        return row.hours;
                    }

                }
            },
            {
                data: "payed", render: function (data, type, row) {
                    let formatted = $.fn.dataTable.render
                        .number(' ', ',', 2, null, " р.")
                        .display(data);
                    return `<span class="badge fs-6 shadow bg-warning" data-bs-toggle="popover" title="Hello" 
data-bs-content="" data-bs-placement="top" data-bs-trigger="hover" data-bs-html="true">${formatted}</span>`;
                }
            },

            {
                data: "employee.role.baseSalary", render: function (data, type, row) {
                    return `<button data-bs-toggle="popover" data-bs-placement="top" data-bs-trigger="click" 
data-bs-html="true" title="Hello" data-bs-content="" style="padding: 0; border: none; background-color: transparent">${renderCurrency(row.employee.role.baseSalary)}</button>`
                }
            }


        ],
        drawCallback: function(s) {
          $("[data-bs-toggle='popover']").popover({
              // content: () => $("#test-text-popover")
          })
        },
        rowCallback: function (row, data, index) {
            console.log(row)
            if (data.employee.role.role === "ZAV") {
                drawPopoverPayedForZAV(row, data);
                drawPopoverTaken(row, data);
            } else if (data.employee.role.role === "PH" || data.employee.role.role === "PROV") {
                drawPopoverPayedForPH(row, data);
                drawPopoverTaken(row, data);
            } else {
                drawPopoverPayedForRAZB(row, data);
                drawPopoverTaken(row, data);
            }

        },
        order: [2, 'asc'],
        columnDefs: [
            {className: "dt-center", targets: [2, 5]}],
        rowGroup: {
            dataSrc: ["pharmacy.pharmacyNumber", "date"],
            startRender: function(rows, group, level) {
                if (level === 0) {
                    return `<span class="shadow badge fs-5 pastel-red">Аптека №${group}</span>`
                } else if (level === 1) {
                    let totalHours = 0;
                    let revenue;
                    $.each(rows.data(), function(idx, row) {
                        revenue = row.pharmacyRevenue
                        totalHours += row.hours
                    })
                    return `<span class="shadow badge fs-6 pastel-wave">${moment(group).format("MMMM, YYYY")}</span> 
<span class="shadow badge fs-6 pastel-wave">Кол-во часов: ${totalHours}</span><span class="ms-1 shadow badge fs-6 pastel-wave">Выручка: ${renderCurrency(revenue)}</span>`;
                }
            }
        },
        displayLength: 50,
        language: {
            lengthMenu: "Показать по _MENU_",
            paginate: {
                first: "First",
                last: "Last",
                next: "След.",
                previous: "Пред."
            },
            loadingRecords: "Загружаю...",
            infoEmpty: "Записи с 0 до 0 из 0 записей",
            infoFiltered: "(отфильтровано из _MAX_ записей)",
            zeroRecords: "Записи отсутствуют.",
            emptyTable: "В таблице отсутствуют данные",
            info: "Кол-во записей: _START_ - _END_ из _TOTAL_",
            search: "Фильтр:",
            months: {
                "0": "Январь",
                "1": "Февраль",
                "10": "Ноябрь",
                "11": "Декабрь",
                "2": "Март",
                "3": "Апрель",
                "4": "Май",
                "5": "Июнь",
                "6": "Июль",
                "7": "Август",
                "8": "Сентябрь",
                "9": "Октябрь"
            },

        }
    });


    $("#ph-filter").keyup(() => {
        table
            .column(2)
            .search("(^"+$("#ph-filter").val()+"$)", true, false)
            .draw();
        let filteredData = table.column(3, {search: 'applied'}).cache('search');
        $("#date-filter").empty().append("<option hidden>выбрать...</option>")
        filteredData
            .sort()
            .unique()
            .each(function (d) {
                $("#date-filter").append(`
<option value="${d}">${d}</option>`)
            });
    })


    $("#table_id_filter").prepend(`<button id="filter-clear-button" class="shadow me-3 btn btn-warning btn-sm">Очистить фильтры</button>`)

    $("#date-filter").change(function () {
        table
            .column(3)
            .search($(this).val())
            .draw();
    })

    function loadDates() {
        $("#date-filter").empty().append("<option hidden>выбрать...</option>")
        table
            .column(3)
            .cache('search')
            .sort()
            .unique()
            .each(function (d) {
                $("#date-filter").append(`<option value="${d}">${d}</option>`)
            })
    }

    $(document).on("click", "#filter-clear-button", function () {
        $("#ph-filter").val('')
        loadDates()
        table
            .columns()
            .search('')
            .draw()
    })


    function renderDetails(data) {
        let formattedEfficiency = $.fn.dataTable.render
            .number(' ', ',', 2, null, " р/час")
            .display(data.efficiency);
        return `<table style="border-collapse: separate; border-spacing: 5px; padding-left:15px;">
<tr>
<th class="border-bottom text-end" scope="row">Эффективность:</th>
<td class="border-bottom" >${formattedEfficiency}</td>
</tr>
<tr>
<th class="border-bottom text-end">Отпускные:</th>
<td class="border-bottom">25000</td>
</tr>
</table>`
    }

    $(document).on("click", "td.dt-control", function () {
        let tr = $(this).closest("tr");
        let row = table.row(tr)
        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        } else {
            // Open this row
            row.child(renderDetails(row.data())).show();
            tr.addClass('shown');
        }
    })

    function renderCurrency(data) {
        let currency = $.fn.dataTable.render
            .number(' ', ',', 2, null, " р.")
            .display(data);
        return currency;
    }

    function drawPopoverPayedForZAV(row, data) {
        return $('td', row).eq(popoverPayedColumn).find("span").attr("data-bs-content", `<b>Ставка:</b> ${renderCurrency(data.pharmacy.salarySumPH)}<br>
<b>Часы:</b> ${data.pharmacy.hours} ч.<br><b>Отработано:</b> ${data.hours} ч.<br><b>Заведование:</b> ${renderCurrency(zavBonus)}`).attr("title", `${data.employee.role.roleName}`);
    }

    function drawPopoverPayedForPH(row, data) {
        return $('td', row).eq(popoverPayedColumn).find("span").attr("data-bs-content", `<b>Ставка:</b> ${renderCurrency(data.pharmacy.salarySumPH)}<br>
<b>Часы:</b> ${data.pharmacy.hours} ч.<br><b>Отработано:</b> ${data.hours} ч.`).attr("title", `${data.employee.role.roleName}`);
    }

    function drawPopoverPayedForRAZB(row, data) {
        return $('td', row).eq(popoverPayedColumn).find("span").attr("data-bs-content", `<b>Ставка:</b> ${renderCurrency(data.pharmacy.salarySumPH)}<br>
<b>Дни:</b> ${data.workingDays} д.<br><b>Отработано:</b> ${data.hours / workingDayHoursRAZB} д. (${data.hours} ч.)`).attr("title", `${data.employee.role.roleName}`);
    }

    function drawPopoverTaken(row, data) {
        return $('td', row).eq(popoverTakenColumn).find('button').attr('title', '<span id="oneC-synchronize" class="shadow btn btn-warning btn-sm">Синхронизировать с 1С</span>')
            .attr('data-bs-content', `<div class="input-group mb-3">
  <span tabindex="0" class="input-group-text" id="basic-addon3">https://example.com/users/</span>
  <input type="text" class="form-control" id="basic-url" aria-describedby="basic-addon3">
</div>

`);

    }




})