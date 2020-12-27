const getEmployees = async () => {
                    
    // Get employee list
    const employees = await fetch('/get-employees');
    let response = await employees.json();

    // Get employee table container
    let container = document.querySelector("body");

    // Output message if table is empty
    if (response.length < 1) {
        let message = document.createElement('img');
        message.setAttribute('src', 'images/johnTravolta.gif');
        container.appendChild(message);
    }

    else {
        // Create employee table element and add it to the container
        let table = document.createElement('table');
        container.appendChild(table);

        // Create header and add it to the table
        let header = document.createElement('tr');
        table.appendChild(header);
        
        // Populate table with headers
        let headerContents = ['Employee ID', 'Name', 'Email', 'Manager'];
        for (let i = 0; i < 4; i++) {

            // Create header cell and add it to the header row
            let headerCell = document.createElement('th');
            header.appendChild(headerCell);
            
            // Set the header's inner html (header text)
            headerCell.innerHTML = headerContents[i];
        }

        // Populate table with employee rows
        for(let i = 0; i < response.length; i++) {

            // Create new row element and add it to the table
            let row = document.createElement('tr');
            table.appendChild(row);

            // Set the inner html (table cells)
            row.innerHTML = `
            <td>${response[i].id}</td>
            <td>${response[i].firstName} ${response[i].lastName}</td>
            <td>${response[i].email}</td>
            <td>${response[i].manager}</td>`                            
        }
    }
};

getEmployees();