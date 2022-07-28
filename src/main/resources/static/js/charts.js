export function buildChart(element, phNum, turnOver, grossMargin, netProfit) {
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
                    data: [grossMargin],
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
                    text: `Данные аптеки №${phNum}`
                }

            }
        },


    });
}


