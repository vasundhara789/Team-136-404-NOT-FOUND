package buffer;
import java.util.*;

//Main class to manage the personalized finance tracker
public class MicroFinanceBudgeting {
 static Scanner scanner = new Scanner(System.in); // Scanner for user input
 static ArrayList<Transaction> transactions = new ArrayList<>(); // List to store transactions
 static HashMap<String, List<Double>> categoryExpenses = new HashMap<>(); // Expense categories
 static HashMap<String, Double> budgetLimits = new HashMap<>(); // Category-wise budget limits
 static PriorityQueue<SavingsGoal> savingsGoals = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount)); // Savings goals stored in a priority queue (sorted by amount)
 static Queue<String> emiQueue = new LinkedList<>(); // Queue to manage EMI payments
 static PriorityQueue<BillReminder> billReminders = new PriorityQueue<>((a, b) -> a.dueDate.compareTo(b.dueDate)); // Bill reminders sorted by due date
 static HashMap<String, Double> currencyRates = new HashMap<>(); // Currency exchange rates
 static CircularQueue recurringExpenses = new CircularQueue(10); // Circular queue for recurring expenses
 static Stack<String> investmentStack = new Stack<>(); // Stack for investment tracking

 // Main method that runs the application
 public static void main(String[] args) {
     // Infinite loop for the main menu
     while (true) {
         try {
             // Displaying the main menu with options
             System.out.println("\n\u001B[1m📊 Welcome to Your Personalized Finance Manager\u001B[0m");
             System.out.println("═══════════════════════════════════════════════════════");
             System.out.println("1️  Add Income or Expense");
             System.out.println("2️  View All Transactions");
             System.out.println("3️  Set Budget for a Category");
             System.out.println("4️  Track Savings Goal");
             System.out.println("5️  Manage EMI");
             System.out.println("6️  Bill Payment Reminder");
             System.out.println("7️  Multi-Currency Budgeting");
             System.out.println("8️  Smart Budgeting Suggestions");
             System.out.println("9️  Cost-Cutting Suggestions");
             System.out.println("10  Recurring Expenses");
             System.out.println("1️1️ Investment Tracker");
             System.out.println("0️  Exit");
             System.out.println("═══════════════════════════════════════════════════════");
             System.out.print("\nChoose an option (0-11): ");
             int choice = Integer.parseInt(scanner.nextLine()); // Read user's choice

             // Switch-case to handle different options
             switch (choice) {
                 case 1 -> addTransaction();
                 case 2 -> viewTransactions();
                 case 3 -> setBudget();
                 case 4 -> trackSavingsGoal();
                 case 5 -> manageEMIs();
                 case 6 -> manageBills();
                 case 7 -> currencyManager();
                 case 8 -> smartBudgeting();
                 case 9 -> costCutting();
                 case 10 -> manageRecurring();
                 case 11 -> manageInvestments();
                 case 0 -> { // Exit the program
                     System.out.println("\u001B[32m👋 Exiting the application. Stay financially smart!\u001B[0m");
                     return;
                 }
                 default -> System.out.println("\u001B[31m❌ Invalid option. Try again.\u001B[0m"); // Handle invalid option
             }
         } catch (Exception e) {
             System.out.println("\u001B[31m❌ Error: Invalid input. Please try again.\u001B[0m");
         }
     }
 }

 // Method to add an income or expense transaction
 static void addTransaction() {
     try {
         System.out.println("\n➕ \u001B[1mAdd Income or Expense\u001B[0m");
         System.out.print("Is this an income or expense? (Enter 'income' or 'expense'): ");
         String type = scanner.nextLine().toLowerCase();
         
         // Validate input type
         if (!type.equals("income") && !type.equals("expense")) {
             System.out.println("\u001B[31m❌ Please enter either 'income' or 'expense'.\u001B[0m");
             return;
         }
         
         // Reading amount
         System.out.print("Enter amount in INR (e.g., 1200.50): ₹");
         double amount = Double.parseDouble(scanner.nextLine());
         
         // Reading category
         System.out.print("Enter category (\n  For income: e.g., 'freelance', 'job'\n  For expense: e.g., 'food', 'travel', 'utilities'\n): ");
         String category = scanner.nextLine().toLowerCase();
         
         // Add transaction to the list
         transactions.add(new Transaction(type, amount, category));
         
         // If the transaction is an expense, add it to the categoryExpenses map
         if (type.equals("expense")) {
             categoryExpenses.putIfAbsent(category, new ArrayList<>());
             categoryExpenses.get(category).add(amount);
         }
         System.out.println("\u001B[32m✅ Transaction added successfully.\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Invalid input while adding transaction.\u001B[0m");
     }
 }

 // Method to view all transactions
 static void viewTransactions() {
     try {
         System.out.println("\n📒 \u001B[1mAll Transactions\u001B[0m");
         
         // Check if there are any transactions
         if (transactions.isEmpty()) {
             System.out.println("(No transactions recorded yet)");
             return;
         }
         
         // Print all transactions
         for (Transaction t : transactions) {
             System.out.printf("• %s of ₹%.2f in %s category\n", t.type.toUpperCase(), t.amount, t.category);
         }
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error displaying transactions.\u001B[0m");
     }
 }

 // Method to set a budget for a category
 static void setBudget() {
     try {
         System.out.println("\n💰 \u001B[1mSet Budget for Category\u001B[0m");
         System.out.print("Enter expense category name (e.g., 'food', 'rent', 'utilities'): ");
         String category = scanner.nextLine().toLowerCase();
         System.out.print("Enter monthly budget limit in ₹: ");
         double limit = Double.parseDouble(scanner.nextLine());
         
         // Store the budget limit for the category
         budgetLimits.put(category, limit);
         System.out.println("\u001B[32m✅ Budget set successfully for " + category + ".\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error setting budget.\u001B[0m");
     }
 }

 // Method to track a savings goal
 static void trackSavingsGoal() {
     try {
         System.out.println("\n🎯 \u001B[1mSet Savings Goal\u001B[0m");
         System.out.print("Enter goal name (e.g., 'Vacation to Goa'): ");
         String name = scanner.nextLine();
         System.out.print("Enter target amount (e.g., 100000): ₹");
         double amount = Double.parseDouble(scanner.nextLine());
         
         // Add the savings goal to the priority queue
         savingsGoals.add(new SavingsGoal(name, amount));
         System.out.println("\u001B[32m✅ Savings goal added.\u001B[0m\nCurrent Goals:");
         
         // Display all current savings goals
         for (SavingsGoal goal : savingsGoals) {
             System.out.println("• " + goal.name + " - ₹" + goal.amount);
         }
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error tracking savings goals.\u001B[0m");
     }
 }

 // Method to manage EMI payments
 static void manageEMIs() {
     try {
         System.out.println("\n💳 \u001B[1mManage EMI Payments\u001B[0m");
         System.out.print("Enter EMI description (e.g., 'Home Loan'): ");
         String emiDescription = scanner.nextLine();
         emiQueue.add(emiDescription);
         System.out.println("\u001B[32m✅ EMI added: " + emiDescription + "\u001B[0m");
         System.out.println("\n📋 Current EMI Queue:");
         for (String emi : emiQueue) {
             System.out.println("• " + emi);
         }
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error managing EMI.\u001B[0m");
     }
 }

 // Method to manage bill reminders
 static void manageBills() {
     try {
         System.out.println("\n💡 \u001B[1mBill Payment Reminder\u001B[0m");
         System.out.print("Enter bill name (e.g., 'Electricity Bill'): ");
         String billName = scanner.nextLine();
         System.out.print("Enter due date (yyyy-mm-dd): ");
         String dueDate = scanner.nextLine();
         
         // Add the bill reminder to the priority queue
         billReminders.add(new BillReminder(billName, dueDate));
         System.out.println("\u001B[32m✅ Bill reminder set for " + billName + " on " + dueDate + ".\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error setting bill reminder.\u001B[0m");
     }
 }

 // Method to manage currency rates for budgeting
 static void currencyManager() {
     try {
         System.out.println("\n💱 \u001B[1mCurrency Manager\u001B[0m");
         System.out.print("Enter currency name (e.g., 'USD', 'EUR'): ");
         String currency = scanner.nextLine();
         System.out.print("Enter exchange rate for ₹1: ");
         double rate = Double.parseDouble(scanner.nextLine());
         
         // Store the currency exchange rate
         currencyRates.put(currency, rate);
         System.out.println("\u001B[32m✅ Currency rate for " + currency + " set to ₹" + rate + ".\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error managing currency rates.\u001B[0m");
     }
 }

 // Method to provide smart budget suggestions based on past expenses
 static void smartBudgeting() {
     try {
         System.out.println("\n📈 \u001B[1mSmart Budgeting Suggestions\u001B[0m");
         // Check if any expenses are recorded
         if (categoryExpenses.isEmpty()) {
             System.out.println("\u001B[31m❌ No expenses recorded yet to provide suggestions.\u001B[0m");
             return;
         }

         // Display suggested budgets based on previous expenses
         System.out.println("Suggested Budget Allocation Based on Your Expenses:");
         for (Map.Entry<String, List<Double>> entry : categoryExpenses.entrySet()) {
             double totalSpent = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
             System.out.println("• Category: " + entry.getKey() + " | Total Spent: ₹" + totalSpent);
         }
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error in smart budgeting.\u001B[0m");
     }
 }

 // Method to provide cost-cutting suggestions
//Method to provide cost-cutting suggestions
static void costCutting() {
  try {
      System.out.println("\n✂️ \u001B[1mCost-Cutting Suggestions\u001B[0m");

      // Check if any expenses are recorded
      if (categoryExpenses.isEmpty()) {
          System.out.println("\u001B[31m❌ No expenses recorded yet for cost-cutting suggestions.\u001B[0m");
          return;
      }

      // Set a threshold for cost-cutting suggestions (₹5000)
      double threshold = 5000.0; // Adjust this threshold as needed
      boolean foundHighExpense = false;

      // Loop through each category and calculate the total expenses
      for (Map.Entry<String, List<Double>> entry : categoryExpenses.entrySet()) {
          // Calculate total spent in the category
          double totalSpent = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();

          // Debugging output to check values
          System.out.println("DEBUG: Category = " + entry.getKey() + ", Total Spent = ₹" + totalSpent);

          // If total spent exceeds the threshold, suggest cost-cutting
          if (totalSpent > threshold) {
              foundHighExpense = true;
              System.out.println("• Category: " + entry.getKey() + " - Total Spent: ₹" + totalSpent + " (Consider cutting costs here!)");
          }
      }

      // If no category exceeded the threshold
      if (!foundHighExpense) {
          System.out.println("\u001B[32m✅ No major expenses found for cost-cutting. You're managing well!\u001B[0m");
      }

  } catch (Exception e) {
      System.out.println("\u001B[31m❌ Error suggesting cost-cutting strategies.\u001B[0m");
  }
}

 // Method to manage recurring expenses
 static void manageRecurring() {
     try {
         System.out.println("\n🔄 \u001B[1mRecurring Expenses\u001B[0m");
         System.out.print("Enter recurring expense name (e.g., 'Netflix Subscription'): ");
         String expenseName = scanner.nextLine();
         System.out.print("Enter amount for the recurring expense: ₹");
         double amount = Double.parseDouble(scanner.nextLine());
         
         // Add the recurring expense to the circular queue
         recurringExpenses.enqueue(expenseName + " - ₹" + amount);
         System.out.println("\u001B[32m✅ Recurring expense added: " + expenseName + " - ₹" + amount + ".\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error managing recurring expenses.\u001B[0m");
     }
 }

 // Method to manage investments
 static void manageInvestments() {
     try {
         System.out.println("\n📈 \u001B[1mInvestment Tracker\u001B[0m");
         System.out.print("Enter investment name (e.g., 'Stocks', 'Mutual Funds'): ");
         String investmentName = scanner.nextLine();
         
         // Add the investment to the stack
         investmentStack.push(investmentName);
         System.out.println("\u001B[32m✅ Investment added: " + investmentName + ".\u001B[0m");
     } catch (Exception e) {
         System.out.println("\u001B[31m❌ Error managing investments.\u001B[0m");
     }
 }
}

//Transaction class to represent an individual income or expense
class Transaction {
 String type; // Type of transaction (income or expense)
 double amount; // Amount of money involved
 String category; // Category for the transaction
 
 public Transaction(String type, double amount, String category) {
     this.type = type;
     this.amount = amount;
     this.category = category;
 }
}

//SavingsGoal class to represent a savings goal
class SavingsGoal {
 String name; // Name of the savings goal
 double amount; // Target amount to save
 
 public SavingsGoal(String name, double amount) {
     this.name = name;
     this.amount = amount;
 }
}

//BillReminder class for bill reminder details
class BillReminder {
 String billName; // Name of the bill (e.g., 'Electricity Bill')
 String dueDate; // Due date for the bill (yyyy-mm-dd)
 
 public BillReminder(String billName, String dueDate) {
     this.billName = billName;
     this.dueDate = dueDate;
 }
}

//CircularQueue class for managing recurring expenses
class CircularQueue {
 private final String[] queue;
 private int front, rear, size;
 
 public CircularQueue(int capacity) {
     queue = new String[capacity];
     front = rear = size = 0;
 }
 
 public void enqueue(String element) {
     if (size == queue.length) {
         front = (front + 1) % queue.length; // Move front forward if queue is full
     } else {
         size++;
     }
     queue[rear] = element;
     rear = (rear + 1) % queue.length; // Move rear forward
 }
 
 public void dequeue() {
     if (size == 0) return;
     front = (front + 1) % queue.length;
     size--;
 }
 
 public boolean isEmpty() {
     return size == 0;
 }
}
