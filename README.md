# Mini Expense Manager

A small full-stack app to track daily expenses.  
You can add expenses one by one, upload many at once using a CSV file, automatically get categories based on vendor names, spot unusually big expenses, and see a simple dashboard.

### What it can do

- Add single expense (date, amount, vendor, description)  
- Auto-assign category (example: Swiggy → Food, Amazon → Shopping)  
- Upload expenses from CSV file  
- Flag expenses that are more than 3× the average in their category (anomaly detection)  
- Show monthly totals by category, top 5 vendors you spend most on, and list of anomalies

### Technologies I used

**Backend**  
- Java + Spring Boot  
- H2 database (in-memory, disappears when you stop the app)  
- Spring Data JPA  

**Frontend**  
- React (created with create-react-app)  
- Tailwind CSS for styling  
- Axios to talk to backend  
- Recharts for the pie and bar charts  




Note:the server port is 8090


