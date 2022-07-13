$(document).ready(function () {


    $.getJSON("http://localhost:8080/api/all-contrs", getData)
    function getData(data) {
        $.each(data, function(idx, value) {
            console.log(value["inn"])
        })
    }
    $("#5-boxes").css("display", "flex").hide().fadeIn(1000)

    $('#amount1,#amount2').keyup(function () {
        let total = parseInt($('#amount1').val()) + parseInt($('#amount2').val());
        $('#total').text(total);
    });
    let contrs;

    // $("button").click(function() {
    //     $.getJSON("http://localhost:8080/api/all-contrs", function(data) {
    //
    //
    //     })
    // })


    $("#change-test").change(function () {
        alert( "Handler for .change() called." );
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


    // $("#add_button").click(function () {
    //     $('#my_table tr:last').after(
    //         '<tr id="created-row">' +
    //         '<td>' +
    //         '<div class="form-floating">' +
    //         '<input type="text" th:name="name" class="form-control shadow" id="floatingInput" placeholder="Наименование"/>' +
    //         '<label for="floatingInput">Наименование</label>' +
    //         '</div>' +
    //         '</td>' +
    //         '<td>' +
    //         '<div class="form-floating">' +
    //         '  <select name="type" class="form-select shadow" id="floatingSelect" aria-label="Floating label select example">\n' +
    //         '    <option value="fixed" selected>Постоянные</option>' +
    //         '    <option value="var">Переменные</option>' +
    //         '</select>' +
    //         '<label for="floatingSelect">Тип</label>' +
    //         '</div>' +
    //         '</td>' +
    //         '<td>' +
    //         '<button type="submit" th:name="add_cost" th:value="true" class="btn btn-success shadow btn-bg">Добавить</button>' +
    //         '</td>' +
    //         '</tr>'
    //     );
    //     $('#created-row').wrap('<form method="post"></form>')
    //
    // })
});
