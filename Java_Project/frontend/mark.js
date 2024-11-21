// Mark form submission logic
document.getElementById('markForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Get input values
    const studentName = document.getElementById('studentName').value;
    const marksInput = document.getElementById('marks').value;

    // Example: Predefined values for total and attended classes
    const totalClasses = 30; // Total number of classes
    const attendedClasses = 27; // Number of classes the student attended

    // Calculate attendance percentage
    const attendancePercentage = (attendedClasses / totalClasses) * 100;

    // Convert marks into an array of numbers
    const marksArray = marksInput.split(',').map(Number);
    const totalMarks = marksArray.reduce((acc, mark) => acc + mark, 0);
    const averageMarks = totalMarks / marksArray.length;

    // Display the result in the "Analysis Results" section
    document.getElementById('results').innerHTML = `
        <p><strong>Student Name:</strong> ${studentName}</p>
        <p><strong>Marks:</strong> ${marksArray.join(', ')}</p>
        <p><strong>Total Marks:</strong> ${totalMarks}</p>
        <p><strong>Average Marks:</strong> ${averageMarks.toFixed(2)}</p>
        <p><strong>Attendance Percentage:</strong> ${attendancePercentage.toFixed(2)}%</p>
    `;

    // Show the analysis results and chart sections
    document.getElementById('outputSection').style.display = 'block';

    // You can add code to display a chart if needed
    displayChart(marksArray); // Assuming a function to display chart (not shown here)

    // Store the attendance data for the report section
    generateReport(studentName, totalMarks, averageMarks, attendancePercentage);
});

// Generate performance report
function generateReport(studentName, totalMarks, averageMarks, attendancePercentage) {
    const reportSection = document.getElementById('report');

    reportSection.innerHTML = `
        <p><strong>Student Name:</strong> ${studentName}</p>
        <p><strong>Total Marks:</strong> ${totalMarks}</p>
        <p><strong>Average Marks:</strong> ${averageMarks.toFixed(2)}</p>
        <p><strong>Attendance Percentage:</strong> ${attendancePercentage.toFixed(2)}%</p>
    `;
}

// Function to display a simple bar chart using Chart.js
function displayChart(marksArray) {
    const ctx = document.getElementById('chart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: marksArray.map((_, index) => 'Subject ' + (index + 1)),
            datasets: [{
                label: 'Marks',
                data: marksArray,
                backgroundColor: 'rgba(54, 172, 235, 15)',
                borderColor: 'rgba(56, 189, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}
