function logMessage(message) {
    document.getElementById('output').innerText = message;
}

function showSection(sectionId) {
    document.getElementById('income-form').style.display = 'none';
    document.getElementById('expense-form').style.display = 'none';
    document.getElementById('summary-section').style.display = 'none';

    document.getElementById(sectionId).style.display = 'block';
}

document.getElementById('view-summary-btn').addEventListener('click', () => {
    showSection('summary-section');
    logMessage('Summary section opened');
});

document.getElementById('add-income-btn').addEventListener('click', () => {
    showSection('income-form');
});

document.getElementById('add-expense-btn').addEventListener('click', () => {
    showSection('expense-form');
});

document.getElementById('income-entry-form').addEventListener('submit', (e) => {
    e.preventDefault();

    const category = document.getElementById('income-category').value;
    const amount = document.getElementById('income-amount').value;
    const date = document.getElementById('income-date').value;

    logMessage(`Income Added: ${category}, ${amount} RSD, Date: ${date}`);
});

document.getElementById('expense-entry-form').addEventListener('submit', (e) => {
    e.preventDefault();

    const category = document.getElementById('expense-category').value;
    const amount = document.getElementById('expense-amount').value;
    const date = document.getElementById('expense-date').value;

    logMessage(`Expense Added: ${category}, ${amount} RSD, Date: ${date}`);
});
