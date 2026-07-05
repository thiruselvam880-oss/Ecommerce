# 🛒 E-Commerce REST API — Spring Boot

A production-grade full-stack e-commerce application built with **Spring Boot** (backend) and **HTML/CSS/JavaScript** (frontend). Features JWT-based authentication, role-based access control, product management, cart system, and a complete order lifecycle.

---

## 🚀 Live Demo

> Backend runs on `http://localhost:8082`  
> Frontend — open `index.html` in any browser or serve with Live Server



## ✨ Features

### 🔐 Authentication & Security
- JWT token-based authentication
- BCrypt password hashing
- Role-based access control — `ADMIN` and `USER`
- Stateless sessions with Spring Security filter chain
- Default admin user auto-created on startup

### 🛍️ Product Management
- Full CRUD (Create, Read, Update, Delete)
- Image upload and retrieval per product
- Keyword search across title, category, and description
- Filter by category, sort by price or rating
- Auto-load products from [FakeStore API](https://fakestoreapi.com)

### 🛒 Cart
- Auto-created cart per customer on first use
- Add items, update quantity, remove items, clear cart
- Real-time total price calculation
- Stock validation before checkout

### 📦 Orders
- Checkout converts cart to a confirmed order
- Stock is reduced on checkout — prevents overselling
- Order status lifecycle: `PENDING → CONFIRMED → SHIPPED → DELIVERED`
- Admin can view all orders and update status
- Users can view their own order history with a visual timeline

### 👤 Customer Profile
- Create and update customer profile (name, phone, full address)
- Profile linked to authenticated user account

### 🖥️ Frontend (HTML/CSS/JS)
- Fully connected to backend via REST API
- Separate flows for Admin and User
- Responsive design with Bootstrap 5
- Toast notifications, loading spinners, modal dialogs

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT (jjwt) |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL |
| Build Tool | Maven |
| Frontend | HTML5, CSS3, JavaScript (ES6), Bootstrap 5 |
| API Testing | Postman |
| Version Control | Git + GitHub |

---

## 📁 Project Structure

```
ecommerce/
├── src/main/java/com/example/ecommerce/
│   ├── config/          # Spring Security configuration
│   ├── controller/      # REST API controllers
│   ├── dto/             # Request & Response DTOs
│   ├── entity/          # JPA entities
│   ├── filter/          # JWT auth filter
│   ├── repository/      # Spring Data JPA repositories
│   ├── service/         # Business logic (interfaces + implementations)
│   └── util/            # JWT utility, Admin initializer
└── ecommerce-frontend/
    ├── index.html       # Product listing (home)
    ├── js/
    │   ├── api.js       # API calls + JWT auth
    │   └── utils.js     # Toast, spinner, navbar helpers
    └── pages/
        ├── login.html
        ├── register.html
        ├── cart.html
        ├── orders.html
        ├── profile.html
        ├── admin-products.html
        └── admin-orders.html
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- Node.js (optional — only if using Live Server via npm)

### 1. Clone the repository

```bash
git clone https://github.com/your-username/ecommerce-springboot.git
cd ecommerce-springboot
```

### 2. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE ecommerce_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Run the backend

```bash
mvn spring-boot:run
```

The server starts at `http://localhost:8080`.  
Default users are created automatically on first run:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin1234` | ADMIN |
| `user` | `user1234` | USER |

### 4. Run the frontend

Open `ecommerce-frontend/index.html` directly in your browser, or use VS Code Live Server extension.

---

## 📡 API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/authenticate` | Public | Login — returns JWT token |
| POST | `/api/users/register` | Public | Register new user |
| POST | `/api/users/admin/create` | ADMIN | Create admin account |

### Products
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/products` | Public | Get all products |
| GET | `/products/{id}` | Public | Get product by ID |
| GET | `/products/{id}/image` | Public | Get product image |
| GET | `/products/search?keyword=` | Public | Search products |
| GET | `/products/categories` | Public | Get all categories |
| GET | `/products/load` | Public | Load from FakeStore API |
| POST | `/products` | ADMIN | Add new product |
| PUT | `/products/{id}` | ADMIN | Update product |
| DELETE | `/products/{id}` | ADMIN | Delete product |

### Cart
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/cart` | USER | View my cart |
| POST | `/api/cart/add` | USER | Add item to cart |
| PUT | `/api/cart/{cartItemId}?quantity=` | USER | Update item quantity |
| DELETE | `/api/cart/{cartItemId}` | USER | Remove item |
| DELETE | `/api/cart/clear` | USER | Clear entire cart |

### Orders
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/orders/checkout` | USER | Checkout cart → create order |
| GET | `/api/orders/my-orders` | USER | Get my orders |
| GET | `/api/orders` | ADMIN | Get all orders |
| PUT | `/api/orders/{orderId}?status=` | ADMIN | Update order status |

### Customer Profile
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/customer` | USER | Create profile |
| PUT | `/api/customer` | USER | Update profile |
| GET | `/api/customer` | USER | Get my profile |

---

## 🔑 How to Use the API (Postman)

**Step 1 — Register or login:**
```json
POST /authenticate
{
  "username": "user",
  "password": "user1234"
}
```

**Step 2 — Copy the token from the response.**

**Step 3 — Add to every protected request:**
```
Authorization: Bearer <your_token_here>
```

---

## 🗄️ Entity Relationship Overview

```
USERS ──────────── CUSTOMER ──────────── CART ──────── CART_ITEM
  (1:1)               (1:1)              (1:N)              │
                        │                                PRODUCT
                       (1:N)                               │
                      ORDER ──────────── ORDER_ITEM ───────┘
                               (1:N)
```

- `USERS` → `CUSTOMER` — one-to-one (profile)
- `CUSTOMER` → `CART` — one-to-one (auto-created)
- `CART` → `CART_ITEM` → `PRODUCT` — many items per cart
- `CUSTOMER` → `ORDER` → `ORDER_ITEM` → `PRODUCT` — order history

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 👨‍💻 Author

THIRU SELVAM T  
📧 thiruselvam880@gmail.com 
🔗 [LinkedIn]([https://linkedin.com/in/your-profile](https://www.linkedin.com/in/thiru-selvam-999826258/))  
🐙 [GitHub](https://github.com/your-username)

---

> ⭐ If you found this project useful, give it a star on GitHub!
