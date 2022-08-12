export function buildChart(element, info, turnOver, grossProfit, netProfit) {
    const myChart = new Chart(element, {
        plugins: [ChartDataLabels],
        type: 'bar',
        data: {
            labels: [""],
            datasets: [
                {
                    label: 'Выручка',
                    data: [turnOver],
                    borderColor: '#FF6363',
                    backgroundColor: 'RGBA(255,99,99,0.7)',
                },
                {
                    label: 'Валовая прибыль',
                    data: [grossProfit],
                    borderColor: "#F8B400",
                    backgroundColor: 'RGBA(248,180,0,0.7)',

                },
                {
                    label: 'Чистая прибыль',
                    data: [netProfit],
                    borderColor: "#125B50",
                    backgroundColor: 'RGBA(18,91,80,0.7)'
                }
            ]

        },
        options: {
            indexAxis: 'y',
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
                        size: 13
                    },
                    formatter: function(value, ctx) {return value + " р."},
                    padding: 7
                },
                legend: {
                    position: 'bottom',
                },
                title: {
                    display: true,
                    text: info
                }

            }
        },


    });
}


