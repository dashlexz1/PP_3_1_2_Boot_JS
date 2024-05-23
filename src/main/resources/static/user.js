const currentUserPanelData = document.getElementById("currentUserTableBody");
const authorisedUserData = document.getElementById("authorisedUser");

let currentUser = () => {
    fetch("http://localhost:9818/api/user", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(user => {
            if (user) {
                currentUserPanelData.innerHTML = `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.salary}</td>
                            <td>${user.age}</td>
                            <td>${user.username}</td>
                            <td>${user.roles.map(role => role.role === "ROLE_USER" ? "USER" : "ADMIN").join(", ")}</td>
                        </tr>
                    `;
                authorisedUserData.innerHTML = `
                        <p class="d-inline font-weight-light">${user.username} с ролями ${user.roles.map(role => role.role === "ROLE_USER" ? "USER" : "ADMIN").join(", ")}</p>
                    `;
            }
        })
        .catch(error => console.error('Ошибка при получении данных пользователя:', error));
};

currentUser();