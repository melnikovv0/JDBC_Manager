# 🍱 Order Transaction System (Java & MS SQL)

This project is a backend implementation for managing food delivery orders, developed as part of a **Database Systems** course. It focuses on robust data integrity and demonstrates different methods of handling complex database transactions using **JDBC**.

---

### ✨ Key Features

* **Transaction Management:** Implements two distinct ways to process orders:
    * **Database-Level:** Invoking T-SQL Stored Procedures (`callFinishOrder`) using Table-Valued Parameters.
    * **Application-Level:** Manual transaction control in Java (Begin -> Multi-step Insert -> Commit/Rollback).
* **Advanced JDBC:** Usage of `SQLServerDataTable` to pass structured data (order items) directly to the SQL Server.
* **DAO Pattern:** Clean separation of concerns with Data Access Objects for `Orders` and `Transactions`.
* **Data Integrity:** Ensures that an order and its items are either saved together or not at all (Atomic operations).

---

### 🛠 Tech Stack

* **Language:** Java 21
* **Database:** Microsoft SQL Server
* **Connection:** JDBC (mssql-jdbc)
* **Build Tool:** Maven
* **Testing:** JUnit 5

---

### 📂 Project Structure

* **`com.example.db`**: Database connection wrapper with transaction logic (connect, begin, commit, rollback).
* **`com.example.dao`**: 
    * `OrderDao`: Standard CRUD operations for the Order entity.
    * `TransactionsDao`: Complex logic for processing full orders with items.
* **`com.example.entity`**: POJO classes (`Order`, `OrderItem`) representing the database schema.

---

### 🚀 How to Run

1.  **Configure Database:** * Update the connection string in `Database.java` with your credentials:
    ```java
    String url = "jdbc:sqlserver://dbsys.cs.vsb.cz\\sqldb;databaseName=YOUR_DB";
    ```
2.  **Stored Procedure:** Ensure the `FinishOrder` procedure and custom Table Type are created in your SQL Server instance.
3.  **Build:**
    ```bash
    mvn clean install
    ```
4.  **Execute:** Run the `MainApp` class to see the demonstration of both transaction methods.

---

### 👨‍💻 Author
**Evgenii Melnikov** *VŠB-TUO, FEI*
