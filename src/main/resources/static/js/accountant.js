$(document).ready(() => {


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
            {
                data: "payed", render: function (data, type, row) {
                    let formatted = $.fn.dataTable.render
                        .number(' ', ',', 2, null, " р.")
                        .display(data);
                    return `<span class="badge fs-6 shadow bg-warning">${formatted}</span>`;
                }
            },
            {
                data: "hours", render: function (data, type, row) {
                    return `${row.hours} (${row.workingDays})`;
                }
            },
            {
                data: "ndfl", render: function (data, type, row) {
                    return `${row.ndfl} <button id="change-ndfl-btn" class="btn" style="padding: 0;border: none;background: none;"><img src="https://i.ibb.co/gyF3xP6/pencil.png" height="15px"/></button>`
                }
            }


        ],
        order: [[1, 'asc']],
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
        console.log($("").val())
        table
            .column(2)
            .search($("#ph-filter").val())
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


    function format(data) {
        return `<table class="table" style="border-collapse: separate; border-spacing: 10px; padding-left:20px;">
<tr>
<th>Отпускные:</th>

</tr>
<tr>
<th>К оплате:</th>

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
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    })


})