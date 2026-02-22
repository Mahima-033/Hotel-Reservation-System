# Hotel Reservation System (Java + JDBC)

A **console-based Hotel Reservation System** developed using **Core Java**, **JDBC**, and **MySQL**.  
This project allows users to manage hotel room reservations through a simple menu-driven interface.

## 🚀 Features

- Reserve a room
- View all reservations
- Get room number using reservation ID and guest name
- Update reservation details
- Delete reservations
- Menu-driven console interface

## 🛠️ Technologies Used

- Java (Core Java)
- JDBC
- MySQL
- IntelliJ IDEA / Eclipse (any Java IDE)

## 📂 Project Structure
    HotelReservationSystem
    │
    ├── HotelReservationSystem.java
    └── README.md

---

## 🗄️ Database Details

**Database Name:** `hotel_db`  
**Table Name:** `reservations`

---

### ⚙️Setup Instructions

1. Install MySQL  
2. Create a database named hotel_db  
3. Create the reservations table using the SQL above  
4. Update database credentials in the code:
   ```
      private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
      private static final String username = "root";
      private static final String password = ""; // add your local password```
6. Run the HotelReservationSystem class
---

### **Table Structure**
```sql
    CREATE TABLE reservations (
        Reservation_Id INT AUTO_INCREMENT PRIMARY KEY,
        Guest_Name VARCHAR(100),
        Room_Number INT,
        Contact_Number VARCHAR(15),
        Reservation_Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    -----



