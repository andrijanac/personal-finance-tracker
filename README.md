# Personal Finance Tracker

This is a simple web-based application built in Clojure, designed to help users track their personal income and expenses. The app provides a clear summary of total income, total expenses, and remaining budget, with a warning when the remaining amount drops below 10% of the total income.

## Features

- Add and categorize income and expenses
- View financial summary with budget warning
- Reset all data to start fresh
- Frontend interface for easier data entry and overview

## Technologies Used

- **Clojure** (main language)
- **Ring** and **Compojure** for routing and HTTP server
- **Cheshire** for JSON handling
- **JS**, **HTML** and **CSS** for frontend
- **Leiningen** as the build tool

## Project Structure

```

personal-finance-tracker

├── resources/
│   └── public/
│       ├── index.html
│       ├── script.js
│       └── style.css

├── src/
│   └── personal_finance_tracker/
│       ├── core.clj
│       └── my-budget.json

├── test/
│   └── personal_finance_tracker/
│       └── core_test.clj

├── project.clj
└── README.md

````

## Setup Instructions

To run this project locally, follow the steps below.

### Prerequisites

- [Leiningen](https://leiningen.org/) must be installed on your system.
- Java should be installed (Leiningen usually includes it by default).

### Running the Application

1. **Clone the repository**

   ```bash
   git clone https://github.com/andrijanac/personal-finance-tracker.git
   cd personal-finance-tracker
   ```

2. **Install dependencies**

   ```bash
   lein deps
   ```

3. **Run the server**

   ```bash
   lein run
   ```

4. **Open the application in your browser**

   Navigate to:

   ```
   http://localhost:3000
   ```

You should now see the Personal Finance Tracker web interface.


## Notes

* If the JSON file (`my-budget.json`) does not exist when the app starts, it will be created automatically.
* All data is stored locally and will reset if the file is deleted or the project is moved.
* AJAX is used on the frontend to call the following routes: `/summary`, `/add-income`, `/add-expense`, `/reset-db`.

## Testing

To run unit tests with Midje:

```bash
lein midje
```

These tests verify individual functions and logic. They are written before backend integration, to assist in structuring the main logic.
