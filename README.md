# ShopCart Selenium Test Automation Tool

End-to-end functional tests for the ShopCart MERN e-commerce app. Maps every test directly to SRS requirement IDs (`REQ_F###`, `REQ_IO###`) so assignment deliverables (Test Case Spec, Test Log, Test Summary, Test Incident Report) can be filled straight from the generated HTML report.

## Stack

- Java 17 + Maven
- Selenium 4
- TestNG 7
- WebDriverManager (auto driver setup)
- ExtentReports (HTML report with screenshots on failure)
- Jackson (test data JSON)

## Prerequisites

- JDK 17+
- Maven 3.8+
- Chrome 120+ installed (default). Edge / Firefox supported.
- MERN app running locally:
  - Terminal A: `cd backend && npm install && npm start` → http://localhost:5000
  - Terminal B: `cd frontend && npm install && npm start` → http://localhost:3000

## Seed Test Users

Before first run, register one customer + one seller through the UI (or POST to backend) matching `testdata/users.json`:

| Role | Email | Password |
|------|-------|----------|
| Customer | testcustomer@shopcart.test | Customer@Pass123 |
| Seller | testseller@shopcart.test | Seller@Pass123 |

## Run

```sh
cd selenium-tests
mvn clean test
```

Run a single feature:
```sh
mvn test -Dtest=F2_SignInTest
```

Cross-browser:
```sh
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

Headless:
```sh
mvn test -Dheadless=true
```

## Report

After run: open `selenium-tests/target/ShopCartTestReport.html` in a browser. Failing tests include screenshot from `target/screenshots/`.

## Test ↔ SRS Mapping

| Test class | SRS § | REQ IDs |
|-----------|-------|---------|
| F1_SignUpTest | 3.2.1, 3.1.2 | REQ_F100, REQ_IO020 |
| F2_SignInTest | 3.2.2, 3.1.1 | REQ_F200, REQ_IO010 |
| F3_SearchByNameTest | 3.2.3, 3.1.3 | REQ_F300, REQ_IO030 |
| F4_SearchByCategoryTest | 3.2.4, 3.1.4 | REQ_F400, REQ_IO040 |
| F5_AddToCartTest | 3.2.5, 3.1.5 | REQ_F500, REQ_IO050 |
| F6_AddShippingAddressTest | 3.2.6, 3.1.6 | REQ_F600, REQ_IO060 |
| F7_AddReviewRatingTest | 3.2.7, 3.1.7 | REQ_F700, REQ_IO070 |
| F8_AddProductTest | 3.2.8, 3.1.8 | REQ_F800, REQ_IO080 |
| F9_DeleteProductTest | 3.2.9, 3.1.9 | REQ_F900, REQ_IO090 |
| F10_EditProductTest | 3.2.10, 3.1.10 | REQ_F900-edit, REQ_IO0100 |
| F11_ViewProductsTest | 3.2.11, 3.1.11 | REQ_F1000, REQ_IO0110 |
| F12_DeleteCustomerReviewTest | 3.2.12, 3.1.12 | REQ_F1100, REQ_IO0120 |
| F13_ViewAddedToCartTest | 3.2.13, 3.1.13 | REQ_F1200, REQ_IO0130 |
| F14_ViewOutForDeliveryTest | 3.2.14, 3.1.14 | REQ_F1300, REQ_IO0140 |

## Layout

```
selenium-tests/
├── pom.xml
├── testng.xml          three suites: auth-validation, customer-flow, seller-flow
├── testdata/
│   ├── users.json
│   └── products.json
├── src/test/java/com/shopcart/
│   ├── base/           BaseTest, DriverFactory, ConfigReader
│   ├── pages/          Page Object Model — one per app page
│   ├── tests/          F1..F14 test classes (1:1 with SRS features)
│   └── utils/          WaitUtils, ScreenshotUtils, JsonDataReader
└── src/test/resources/
    └── config.properties
```

## Extending

- **New selectors broke?** Update only the `pages/*.java` file — tests stay untouched.
- **More negative cases?** Add `@Test` methods to the matching `Fx_*Test` class; keep the SRS REQ ID in the `description=` attribute so it surfaces in the report.
- **New feature in SRS?** Create `F15_*Test.java`, add to `testng.xml`.

## Notes on SRS vs implementation

The SRS specifies password rules (length ≥ 12, uppercase, special char) under `REQ_IO0207..0209` but the live `AuthenticationPage.jsx` only enforces *required*. Tests for those rules are intentionally **omitted** here — adding them would fail against the current implementation. Document this as a finding in your Test Incident Report.

## Deliverables Mapping

| Assignment doc | Source from this project |
|----------------|--------------------------|
| Test Plan | scope + tools + browsers (this README) |
| Test Design Specification | `testng.xml` test groups |
| Test Case Specification | each `@Test(description=...)` |
| Test Procedure Specification | page objects + test method bodies |
| Test Log | `target/ShopCartTestReport.html` per run |
| Test Incident Report | failed tests + screenshots |
| Test Summary Report | ExtentReports dashboard |
