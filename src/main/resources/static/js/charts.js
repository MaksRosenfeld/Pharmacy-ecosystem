$(document).ready(function () {
    $(document).on("click", "#test", function () {
        const ctx = document.getElementById('pharmacyChart1').getContext('2d');
        const myChart = new Chart(ctx, {
            plugins: [ChartDataLabels],
            type: 'bar',
            data: {
                labels: [""],
                datasets: [
                    {
                        label: 'Выручка',
                        data: [5500000],
                        borderColor: '#FF6363',
                        backgroundColor: 'RGBA(255,99,99,0.7)',
                    },
                    {
                        label: 'Валовая прибыль',
                        data: [1300000],
                        borderColor: "#F8B400",
                        backgroundColor: 'RGBA(248,180,0,0.7)',

                    },
                    {
                        label: 'Чистая прибыль',
                        data: [500000],
                        borderColor: "#125B50",
                        backgroundColor: 'RGBA(18,91,80,0.7)'
                    }
                ]

            },
            options: {
                indexAxis: 'y',
                // Elements options apply to all of the options unless overridden in a dataset
                // In this case, we are setting the border of each horizontal bar to be 2px wide
                elements: {
                    bar: {
                        borderWidth: 2,
                    }
                },
                responsive: true,
                plugins: {
                    datalabels: {
                        backgroundColor: function (context) {
                            return context.dataset.backgroundColor;
                        },
                        borderRadius: 4,
                        color: 'white',
                        font: {
                            weight: 'bold',
                            size: 12
                        },
                        formatter: null,
                        padding: 7
                    },
                    legend: {
                        position: 'bottom',
                    },
                    title: {
                        display: true,
                        text: 'Номер аптеки'
                    }

                }
            },


        });
    })
})

