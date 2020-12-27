const loadViewRequestsPage = async () => {
                    
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

        // Print out manager page
        if (user.manager) {
            // Create header and add it to the body element
            let header = document.createElement('h1');
            header.innerHTML = 'View Requests';
            body.appendChild(header);

            // Add buttons to the body element
            let buttons = document.createElement('div');
            buttons.innerHTML = `
            <a href="view-pending.html">
                <button>Pending Requests</button>
            </a>
            <a href="view-resolved.html">
                <button>Resolved Requests</button>
            </a>
            `;
            body.appendChild(buttons);
        }

        // Print out employee page
        else {
            // Create header and add it to the body element
            let header = document.createElement('h1');
            header.innerHTML = 'Employee Home';
            body.appendChild(header);

            // Add buttons to the body element
            let buttons = document.createElement('div');
            buttons.innerHTML = `
            <a href="view-pending.html">
                <button>Pending Requests</button>
            </a>
            <a href="view-resolved.html">
                <button>Resolved Requests</button>
            </a>
            `;
            body.appendChild(buttons);
        }
    }
};

loadViewRequestsPage();