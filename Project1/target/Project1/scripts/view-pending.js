const loadPendingRequestsPage = async () => {
                    
    // Get user
    const response = await fetch('/get-user');
    let user = await response.json();

    // If no user is logged in, redirect to login page
    if (user === null) {
        window.location.replace("/");
    }
    else {
        // Get body element
        let body = document.querySelector('body');

        // Create nav bar and add it to the body
        let navBar = document.createElement('nav');
        body.appendChild(navBar);

        // Create navbar links and add them to the navbar
        let navBarText = ['Home', 'Profile', 'Logout'];
        let navBarLinks = [ '/home.html', '/profile.html', '/logout'];
        for (let i = 0; i < navBarText.length; i++) {
            let link = document.createElement('a');
            link.setAttribute('href', navBarLinks[i]);
            link.innerHTML = navBarText[i];
            navBar.appendChild(link);
        }
        
        // Create header and add it to the body
        let header = document.createElement('h1');
        header.innerHTML = 'Pending Requests';
        body.appendChild(header);

        // Print out request table
        if (user.manager) {
            getRequestsManager();
        }
        else {
            getRequestsEmployee();
        }
    }
};

const getRequestsManager = async () => {
                    
    // Get employee list
    const employees = await fetch('/get-pending');
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
        let headerContents = ['Request ID', 'Date Submitted', 'Submitted by', 'Amount', 'Description'];
        for (let i = 0; i < headerContents.length; i++) {

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
            <td>${response[i].requestId}</td>
            <td>${response[i].dateSubmitted}</td>
            <td>${response[i].submittedBy.firstName} ${response[i].submittedBy.lastName}</td>
            <td>$${response[i].amount.toFixed(2)}</td>
            <td>${response[i].description}</td>
            `;
        }

        let form = document.createElement('form');
        container.appendChild(form);
        form.innerHTML = `
        <label>
            <input type="number" placeholder="Request ID" id="request-id" name="request-id" required/>
        </label>
        <label>
            <select name="approve-deny" id="approve-deny">
                <option value="approve">Approve</option>
                <option value="deny">Deny</option>
            </select>
        </label>

        <button type="submit">Submit</button>
        `;
        form.setAttribute('action', '/resolve-request');
        form.setAttribute('method', 'post');
    }
};

const getRequestsEmployee = async () => {
                    
    // Get employee list
    const employees = await fetch('/get-pending');
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
        let headerContents = ['Date Submitted', 'Amount', 'Description'];
        for (let i = 0; i < headerContents.length; i++) {

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
            <td>${response[i].dateSubmitted}</td>
            <td>$${response[i].amount.toFixed(2)}</td>
            <td>${response[i].description}</td>`;
        }
    }
};

loadPendingRequestsPage();