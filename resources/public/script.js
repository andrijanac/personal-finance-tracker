document.addEventListener('DOMContentLoaded', () => {

    function logMessage(message) {
        document.getElementById('output').innerText = message;
    }

    document.getElementById('view-summary-btn').addEventListener('click', () => {
        fetch('/summary')
            .then(response => response.json())
            .then(data => {
                const summary = `
                    Total Income: ${data['total-income'] || 0} RSD
                    Total Expenses: ${data['total-expenses'] || 0} RSD
                    Remaining Budget: ${data['remaining-budget'] || 0} RSD
                    ${data['budget-warning'] ? data['budget-warning'] : ''}
                `;
                
                document.getElementById('income-form').style.display = 'none';
                document.getElementById('expense-form').style.display = 'none';
                document.getElementById('summary-section').style.display = 'block';
                logMessage(summary);
            })
            .catch(error => {
                console.error('Error fetching summary:', error);
                logMessage('Error fetching summary.');
            });
    });

    document.getElementById('add-income-btn').addEventListener('click', () => {
        document.getElementById('income-form').style.display = 'block';
        document.getElementById('expense-form').style.display = 'none';
        document.getElementById('summary-section').style.display = 'none';

        document.getElementById('income-entry-form').reset();
    });

    document.getElementById('income-entry-form').addEventListener('submit', (e) => {
        e.preventDefault();

        const data = {
            category: document.getElementById('income-category').value,
            amount: parseFloat(document.getElementById('income-amount').value),
            date: document.getElementById('income-date').value
        };

        fetch('/add-income', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            logMessage(result.message);
            document.getElementById('income-form').style.display = 'none';
            document.getElementById('summary-section').style.display = 'block';
            document.getElementById('view-summary-btn').click();
        })
        .catch(error => {
            console.error('Error adding income:', error);
            logMessage('Error adding income.');
        });
    });

    document.getElementById('add-expense-btn').addEventListener('click', () => {
        document.getElementById('income-form').style.display = 'none';
        document.getElementById('expense-form').style.display = 'block';
        document.getElementById('summary-section').style.display = 'none';

        document.getElementById('expense-entry-form').reset();
    });

    document.getElementById('expense-entry-form').addEventListener('submit', (e) => {
        e.preventDefault();

        const data = {
            category: document.getElementById('expense-category').value,
            amount: parseFloat(document.getElementById('expense-amount').value),
            date: document.getElementById('expense-date').value
        };

        fetch('/add-expense', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            logMessage(result.message);
            document.getElementById('expense-form').style.display = 'none';
            document.getElementById('summary-section').style.display = 'block';
            document.getElementById('view-summary-btn').click();
        })
        .catch(error => {
            console.error('Error adding expense:', error);
            logMessage('Error adding expense.');
        });
    });

    const resetBtn = document.getElementById('reset-db-btn');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            if (confirm("Are you sure you want to reset all data?")) {
                fetch('/reset-db')
                    .then(response => response.json())
                    .then(result => {
                        logMessage(result.message);
                        document.getElementById('summary-section').style.display = 'block';
                        document.getElementById('income-form').style.display = 'none';
                        document.getElementById('expense-form').style.display = 'none';
                        document.getElementById('view-summary-btn').click();
                    })
                    .catch(error => {
                        console.error('Error resetting database:', error);
                        logMessage('Failed to reset database.');
                    });
            }
        });
    }
});
