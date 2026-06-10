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
> Windows: `.\.maven\apache-maven-3.9.16\bin\mvn.cmd clean test`  
> Linux: `./.maven/apache-maven-3.9.16/bin/mvn clean test`

> **No MongoDB installed?** Start via Docker:
> ```sh
> docker run -d --name mongodb-shopcart -p 27017:27017 mongo:7
> # subsequent runs:
> docker start mongodb-shopcart
> ```

> **Copied node_modules from Windows?** Delete them and re-run `npm install` on Linux — native addons (bcrypt) must be compiled for the current OS.

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

## Step 2 — Seed the Database

The backend has two seed scripts in `MERN-Ecommerce-Site/backend/`.

### `reset_and_seed.js` — Full reset + seed (use this before every test run)

Clears all collections, then recreates test accounts and products via the API so passwords are hashed correctly. Requires the backend to be running on `:5000`.

```bash
cd MERN-Ecommerce-Site/backend
node reset_and_seed.js
```

**Full source:**
```js
/*
 * Reset + seed ShopCart for a clean Selenium run.
 * Prereqs: MongoDB running, AND the backend server running on :5000.
 * Run:  node reset_and_seed.js
 */
const mongoose = require('mongoose');

const MONGO = process.env.MONGO_URL || 'mongodb://127.0.0.1/ecommerce';
const API = process.env.API || 'http://localhost:5000';

const CUSTOMER = { name: 'Test Customer', email: 'testcustomer@shopcart.test', password: 'Customer@Pass123', role: 'Customer' };
const SELLER   = { name: 'Test Seller', email: 'testseller@shopcart.test', password: 'Seller@Pass123', shopName: 'Test Shop', role: 'Seller' };

async function post(path, body) {
  const r = await fetch(API + path, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
  const t = await r.text();
  try { return JSON.parse(t); } catch { return t; }
}

(async () => {
  // 1) Clear collections
  await mongoose.connect(MONGO);
  for (const c of ['customers', 'sellers', 'products', 'orders']) {
    try { const n = await mongoose.connection.db.collection(c).deleteMany({}); console.log('cleared', c, n.deletedCount); }
    catch (e) { console.log('skip', c, e.message); }
  }
  await mongoose.disconnect();

  // 2) Recreate accounts via API (passwords get hashed correctly)
  const cust = await post('/CustomerRegister', CUSTOMER);
  console.log('customer:', cust && (cust._id || cust.message));
  const seller = await post('/SellerRegister', SELLER);
  const sellerId = seller && seller._id;
  console.log('seller:', sellerId || (seller && seller.message));
  if (!sellerId) { console.error('No seller id - cannot seed products. Is the backend running?'); process.exit(1); }

  // 3) Seed a deterministic catalogue (includes "Selenium Test Product" for the search test)
  const names = ['Selenium Test Product', 'Test Product 1', 'Test Product 2', 'Test Product 3', 'Test Product 4'];
  for (const productName of names) {
    const p = {
      productName,
      price: { mrp: 299, cost: 199, discountPercent: 33 },
      subcategory: 'TestSubcat',
      productImage: 'https://placehold.co/300x300?text=Product',
      category: 'Electronics',
      description: 'Seeded product for automated tests',
      tagline: 'Seed tagline',
      seller: sellerId,
    };
    const res = await post('/ProductCreate', p);
    console.log('product:', productName, '->', (res && (res._id || res.message)) || 'ok');
  }

  console.log('\nSEED COMPLETE. Now run:  cd ../selenium-tests  &&  mvn clean test');
})().catch(e => { console.error('FATAL', e); process.exit(1); });
```

What it creates:

| Role | Email | Password |
|------|-------|----------|
| Customer | testcustomer@shopcart.test | Customer@Pass123 |
| Seller | testseller@shopcart.test | Seller@Pass123 |

Products seeded: `Selenium Test Product`, `Test Product 1–4` (5 total).

---

### `seed_db.js` — Order status patch (optional)

Only needed if `F14_ViewOutForDeliveryTest` fails because no orders have the right status. Does **not** clear data — only patches existing `Processing` orders to `Out for Delivery`.

```bash
cd MERN-Ecommerce-Site/backend
node seed_db.js
```

**Full source:**
```js
const mongoose = require('mongoose');

const orderSchema = new mongoose.Schema({
    orderStatus: String,
    buyer: mongoose.Schema.ObjectId,
    paidAt: Date,
    totalPrice: Number,
    productsQuantity: Number,
    taxPrice: Number,
    shippingPrice: Number,
}, { strict: false });

const Order = mongoose.model('order', orderSchema);

async function run() {
    await mongoose.connect('mongodb://127.0.0.1/ecommerce');

    // Update all Processing orders to Out for Delivery
    const result = await Order.updateMany(
        { orderStatus: 'Processing' },
        { $set: { orderStatus: 'Out for Delivery' } }
    );
    console.log('Orders updated:', result.modifiedCount);

    // Show all orders
    const orders = await Order.find({}, 'orderStatus totalPrice');
    orders.forEach(o => console.log(`Order ${o._id}: ${o.orderStatus}`));

    await mongoose.disconnect();
}

run().catch(console.error);
```

---

## Step 3 — Run the Tests

From the `selenium-tests/` directory:

```bash
# Run all tests
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

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `mvn: command not found` | Use bundled Maven: `./.maven/apache-maven-3.9.16/bin/mvn clean test` |
| Backend won't start | Check MongoDB is running; on Linux use `docker start mongodb-shopcart` |
| Tests fail with `Email already exists` | Accounts already seeded — skip `reset_and_seed.js` or run it again to reset |
| F9 deletes a product, later tests fail | Re-run `node reset_and_seed.js` to restore products |
| Chrome driver version mismatch warning | Safe to ignore — WebDriverManager handles it automatically |
| All tests fail with timeout | Verify frontend is on port 3000 and backend on port 5000 |
| `bcrypt` ELF error on Linux | node_modules were built on Windows — run `rm -rf node_modules && npm install` |
