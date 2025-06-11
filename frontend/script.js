function logMessage(message) {
    document.getElementById('output').innerText = message;
}

document.getElementById('view-summary-btn').addEventListener('click', () => {
    logMessage('You clicked: View Summary');
});

document.getElementById('add-income-btn').addEventListener('click', () => {
    logMessage('You clicked: Add Income');
});

document.getElementById('add-expense-btn').addEventListener('click', () => {
    logMessage('You clicked: Add Expense');
});
