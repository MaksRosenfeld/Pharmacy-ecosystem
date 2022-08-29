$(document).ready(() => {

    let allPharmacies = [];

    window.addEventListener('load', buildAllPharmacies)

    function buildAllPharmacies() {
        $.get("data/api/all_pharmacies", function (pharmacies) {
            $.each(pharmacies, (idx, ph) => {
                if (ph["pharmacyNumber"] !== 0) {
                    allPharmacies.push(ph);
                    $("#allPharmaciesSalary").append(`
                <div class="accordion-item">
            <h2 class="accordion-header" id="pharmacyHeader${ph["pharmacyNumber"]}">
                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#pharmacyBody${ph["pharmacyNumber"]}" aria-expanded="true" aria-controls="pharmacyBody${ph["pharmacyNumber"]}">
                    <span class="badge fs-5 shadow bg-danger">Аптека №${ph["pharmacyNumber"]}</span>
                </button>
            </h2>
            <div id="pharmacyBody${ph["pharmacyNumber"]}" class="accordion-collapse collapse show" aria-labelledby="pharmacyHeader${ph["pharmacyNumber"]}">
                <div class="accordion-body">
                <table class="table table-hover">
                <thead>
    <tr>
      <th scope="col">Сотрудник</th>
      <th scope="col">Дата зарплаты</th>
      <th scope="col">Кол-во отработанных часов</th>
    </tr>
  </thead>
  <tbody id="employee-place${ph["pharmacyNumber"]}">
    </tbody>
</table>
                </div>
            </div>
        </div>
        
        `)
                }
            })

        })

    }

    function buildAllEmployees() {
        $.get("/salary/api/get_employees", function (employees) {

        })
    }

})