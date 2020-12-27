const loadProfile = async () => {
                    
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

        body.innerHTML = `
        <nav>
            <a href="/home.html">Home</a>
            <a href="/profile.html">Profile</a>
            <a href="/logout">Log Out</a>
        </nav>
        
        <h1>Your Information</h1>

        <p>Name: ${user.firstName} ${user.lastName}</p>
        <p>Email: ${user.email}</p>`;
        let manager = document.createElement('p');
        if (user.manager) {
            manager.innerHTML = "Manager: Yes";
        }
        else {
            manager.innerHTML = "Manager: No";
        }
        body.appendChild(manager);
    }
};

loadProfile();