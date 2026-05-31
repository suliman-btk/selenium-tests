# ShopCart Selenium Test Suite — Run Guide

End-to-end functional tests for the ShopCart MERN e-commerce app.  
Maps to SRS requirement IDs (`REQ_F###`, `REQ_IO###`).

---

## Prerequisites

| Tool | Version | Check |
|------|---------|-------|
| Java JDK | 17+ | `java --version` |
| Node.js | 16+ | `node --version` |
| MongoDB | running locally | `mongod` service |
| Google Chrome | any recent | installed |
| Maven | 3.8+ | `mvn --version` |

> **No Maven installed?** A bundled copy is at `.maven/apache-maven-3.9.16/bin/mvn.cmd`  
> Use it like: `.\.maven\apache-maven-3.9.16\bin\mvn.cmd clean test`

---

## Step 1 — Start the MERN App

Open **two separate terminals**.

**Terminal A — Backend (port 5000)**
```bash
cd MERN-Ecommerce-Site/backend
npm install
npm start
```

**Terminal B — Frontend (port 3000)**
```bash
cd MERN-Ecommerce-Site/frontend
npm install
npm start
```

Wait until both are ready:
- Backend: `Server started at port no. 5000`
- Frontend: browser opens at `http://localhost:3000`

---

## Step 2 — Seed the Database (first run only)

Run this once before the first test execution. Copy-paste into PowerShell:

```powershell
# Register test customer
Invoke-RestMethod -Uri "http://localhost:5000/CustomerRegister" `
  -Method POST -ContentType "application/json" `
  -Body '{"name":"Test Customer","email":"testcustomer@shopcart.test","password":"Customer@Pass123"}'

# Register test seller
Invoke-RestMethod -Uri "http://localhost:5000/SellerRegister" `
  -Method POST -ContentType "application/json" `
  -Body '{"name":"Test Seller","email":"testseller@shopcart.test","password":"Seller@Pass123","shopName":"Test Shop"}'
```

Then seed products (run from `MERN-Ecommerce-Site/backend`):

```powershell
# Get seller ID + token
$s = Invoke-RestMethod -Uri "http://localhost:5000/SellerLogin" `
  -Method POST -ContentType "application/json" `
  -Body '{"email":"testseller@shopcart.test","password":"Seller@Pass123"}'

# Create 4 test products
1..4 | ForEach-Object {
  $body = @{
    productName   = "Test Product $_"
    price         = @{ mrp = 299; cost = 199; discountPercent = 33 }
    subcategory   = "TestSubcat"
    productImage  = "https://placehold.co/300x300?text=Product"
    category      = "Electronics"
    description   = "Seed product $_"
    tagline       = "Seed tagline"
    quantity      = 10
    seller        = $s._id
  } | ConvertTo-Json
  Invoke-RestMethod -Uri "http://localhost:5000/ProductCreate" `
    -Method POST -ContentType "application/json" -Body $body
}
```

Then seed an order (needed for F7 review test and F14 delivery test).  
Save the script below as `seed_order.js` inside `MERN-Ecommerce-Site/backend` and run `node seed_order.js`:

```javascript
const mongoose = require('mongoose');
const Order    = mongoose.model('order', new mongoose.Schema({}, { strict: false }));
const Customer = mongoose.model('customer', new mongoose.Schema({}, { strict: false }));
const Product  = mongoose.model('product', new mongoose.Schema({}, { strict: false }));

async function run() {
    await mongoose.connect('mongodb://127.0.0.1/ecommerce');

    const customer = await Customer.findOne({ email: 'testcustomer@shopcart.test' });
    const product  = await Product.findOne({});

    await Order.create({
        buyer: customer._id,
        shippingData: {
            address: '123 Test Street', city: 'Amman',
            state: 'Amman', country: 'Jordan',
            pinCode: 111000, phoneNo: 9876543210
        },
        orderedProducts: [{
            _id:          product._id,
            productName:  product.productName,
            price:        product.price,
            subcategory:  product.subcategory,
            productImage: product.productImage,
            category:     product.category,
            description:  product.description,
            tagline:      product.tagline,
            quantity:     1,
            seller:       product.seller,
        }],
        paymentInfo:      { id: 'TEST_PAY_001', status: 'paid' },
        paidAt:           new Date(),
        productsQuantity: 1,
        taxPrice:         20,
        shippingPrice:    0,
        totalPrice:       199,
        orderStatus:      'Out for Delivery',
    });

    console.log('Order seeded.');
    await mongoose.disconnect();
}
run().catch(console.error);
```

---

## Step 3 — Run the Tests

From the `selenium-tests/` directory:

```bash
# Run all 25 tests
mvn clean test

# Run a single feature
mvn test -Dtest=F2_SignInTest

# Run headless (no browser window)
mvn test -Dheadless=true

# Run in Firefox or Edge
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

Expected result: **25 tests, 0 failures**

---

## Step 4 — View the Report

After the run, open the HTML report in a browser:

```
selenium-tests/target/ShopCartTestReport.html
```

Screenshots of failed tests are saved to:

```
selenium-tests/target/screenshots/
```

---

## Test ↔ SRS Mapping

| Test Class | SRS Section | REQ IDs | TC IDs |
|------------|------------|---------|--------|
| F1_SignUpTest | 3.2.1 | REQ_F100, REQ_IO020 | TC-01-001 |
| F2_SignInTest | 3.2.2 | REQ_F200, REQ_IO010 | — |
| F3_SearchByNameTest | 3.2.3 | REQ_F300, REQ_IO030 | — |
| F4_SearchByCategoryTest | 3.2.4 | REQ_F400, REQ_IO040 | — |
| F5_AddToCartTest | 3.2.5 | REQ_F500, REQ_IO050 | — |
| F6_AddShippingAddressTest | 3.2.6 | REQ_F600, REQ_IO060 | TC-06-026 to TC-06-034 |
| F7_AddReviewRatingTest | 3.2.7 | REQ_F700, REQ_IO070 | TC-07-051 to TC-07-054 |
| F8_AddProductTest | 3.2.8 | REQ_F800, REQ_IO080 | TC-08-056 |
| F9_DeleteProductTest | 3.2.9 | REQ_F900, REQ_IO090 | TC-09-057 |
| F10_EditProductTest | 3.2.10 | REQ_F900-edit, REQ_IO0100 | TC-10-058 |
| F11_ViewProductsTest | 3.2.11 | REQ_F1000, REQ_IO0110 | TC-11-059 |
| F12_DeleteCustomerReviewTest | 3.2.12 | REQ_F1100, REQ_IO0120 | TC-12-060 |
| F13_ViewAddedToCartTest | 3.2.13 | REQ_F1200, REQ_IO0130 | TC-13-061 |
| F14_ViewOutForDeliveryTest | 3.2.14 | REQ_F1300, REQ_IO0140 | TC-14-062 |

---

## Test Accounts (seeded in Step 2)

| Role | Email | Password |
|------|-------|----------|
| Customer | testcustomer@shopcart.test | Customer@Pass123 |
| Seller | testseller@shopcart.test | Seller@Pass123 |

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `mvn: command not found` | Use `.\.maven\apache-maven-3.9.16\bin\mvn.cmd` |
| Backend won't start | Check MongoDB is running: `Get-Service MongoDB` in PowerShell |
| Tests fail with `Email already exists` | Accounts already seeded — skip Step 2 |
| F9 deletes a product, later tests fail | Re-seed products (Step 2 products block) |
| Chrome driver version mismatch warning | Safe to ignore — WebDriverManager handles it automatically |
| All tests fail with timeout | Verify frontend is on port 3000 and backend on port 5000 |
