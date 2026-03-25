# Login & Registration System

## 1. Project Overview
This is a **Spring Boot web application** that allows users to register, log in, and manage their profile. The application includes **validation, security, and user-friendly forms**.

### Features
- User registration with validation
- Login authentication
- Password encryption using BCrypt
- User profile page
- Change password functionality
- Status management (Active/Inactive)
- Field validations for date, email, phone, and password strength

---

## 2. Technologies Used
- **Backend:** Spring Boot, Spring MVC, Spring Security, Hibernate/JPA
- **Frontend:** Thymeleaf, HTML5, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven
- **Version Control:** Git & GitHub

---

## 3. Database Fields

| Field Name        | Type                   | Description / Validation                                       |
|-------------------|------------------------|----------------------------------------------------------------|
| `id`              | Long (Primary Key)     | Auto-generated user ID                                         |
| `username`        | String                 | Unique, required                                               |
| `password`        | String                 | Required, BCrypt encryption, strength validation (weak/strong) |
| `confirmPassword` | String                 | Must match `password`                                          |
| `email`           | String                 | Required, valid email format                                   |
| `dob`             | Date / Timestamp       | User date of birth                                             |
| `phone`           | String                 | 10 digits, numeric only                                        |
| `status`          | Enum / String          | Active or Inactive                                             |
| `gender`          | Radio Button           | Male / Female / Other                                          |
| `hobbies`         | List / Multiple Select | Multiple hobbies allowed                                       |
| `insertDate`      | Timestamp              | Automatically recorded when user registers                     |

---

## 4. Password Rules
- Minimum 8 characters
- Must include letters and numbers
- Must include special characters for strong passwords
- Validation occurs on both **registration** and **change password** forms

---

## 5. Frontend Forms

### Registration Form
- Username: text input, required, unique
- Email: email input, required
- Password: password input, strength check
- Confirm Password: must match password
- Phone: number input, 10 digits
- Date of Birth: date input / timestamp
- Gender: radio buttons
- Hobbies: multiple selection
- Status: dropdown (Active / Inactive)

### Login Form
- Username: text input
- Password: password input
- Validates against stored credentials

### Profile Page
- Displays all user information
- Editable fields: email, phone, gender, hobbies
- Option to **change password**

### Change Password Page
- Current password input
- New password input (with strength validation)
- Confirm new password input
- Password updated securely using BCrypt

-----------------------------------------------
**SELECT * FROM demo_db.users;**

