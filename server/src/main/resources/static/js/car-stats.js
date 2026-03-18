document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('priceChart');
    if (!ctx) {
        return;
    }

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: carModels,
            datasets: [{
                label: 'Цена аренды (за день)',
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
                data: carPrices
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});
