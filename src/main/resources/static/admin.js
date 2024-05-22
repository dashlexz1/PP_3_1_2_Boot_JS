const navigation = document.getElementById("allUser-tab");
const addTable = document.getElementById("newUser-tab");
const allTable = document.getElementById("all-users-table");
const urlMainTable = 'http://localhost:9818/api/admin';


const fillTable = (users) => {
    if (users.length > 0) {
        let table = '';
        users.forEach((user) => {
            const roles = user.roles ? user.roles.map((role) => role.role === "ROLE_USER" ? " USER" : " ADMIN") : "Нет ролей";  // Обработка отсутствующих userRoles

            table += `
      <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.salary}</td>
        <td>${user.age}</td>
        <td>${user.username}</td>
        <td>${roles}</td>  <td> <button type="button" class="btn btn-primary"
          data-toggle="modal" data-target="#editModal" id="btn-edit-modal" data-id="${user.id}"> Edit </button> </td>
        <td> <button type="button" class="btn btn-danger"
          data-toggle="modal" data-target="#removeModal" id="btn-delete-modal" data-id="${user.id}"> Delete </button> </td>
      </tr>
      `;
        });
        allTable.innerHTML = table;
    }
};

function getAllUsers () {
    fetch(urlMainTable, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.json())
        .then(data => {
         fillTable(data);
        })
}

// Вызов функции
getAllUsers();

const addNewUser = document.getElementById("add-user-form");
const addName = document.getElementById("nameUser");
const addSalary = document.getElementById("salaryUser");
const addAge = document.getElementById("ageUser");
const addUsername = document.getElementById("usernameUser");
const addPassword = document.getElementById("passwordUser");
const addUserRoles = document.getElementById("userRoles");

const addButtonSubmit= document.getElementById("btn-add-user");


// добавление пользователя
function getRolesFromAddUserForm() {
    let roles = Array.from(addUserRoles.selectedOptions).map(option => option.value);
    let rolesToAdd = new Set();

    roles.forEach(id => {
        let role = {};
        if (id === "1") {
            role = {
                id: 12,
                role: "ROLE_ADMIN"
            };
        } else if (id === "2") {
            role = {
                id: 11,
                role: "ROLE_USER"
            };
        }
        rolesToAdd.add(role);
    });

    console.log('Роли для добавления:', Array.from(rolesToAdd)); // Добавила для отладки
    return Array.from(rolesToAdd);
}

addNewUser.addEventListener("submit", (e) => {
    e.preventDefault();

    const newUser = {
        name: addName.value,
        salary: addSalary.value,
        age: addAge.value,
        username: addUsername.value,
        password: addPassword.value,
        roles: getRolesFromAddUserForm()
    };

    console.log('Данные нового пользователя:', newUser); // Добавила для отладки

    fetch(urlMainTable, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    })
        .then(response => {
            if (response.ok) {
                console.log('Пользователь успешно добавлен');
                location.reload(); // Перезагрузка страницы
            } else {
                return response.json().then(error => {
                    throw new Error(error.message);
                });
            }
        })
        .catch(error => {
            console.error('Ошибка при добавлении пользователя:', error);
            alert('Не удалось добавить пользователя: ' + error.message);
        });
});

// изменение и удаление пользователя

const editModalBtn = document.getElementById("edit-btn-submit");
const editRoles = document.getElementById("rolesEdit");
const editAdmin = document.getElementById("edit-role-admin");
const editUser = document.getElementById("edit-role-user");
//удаление

const deleteUserBtn = document.getElementById("btn-submit-remove");
const deleteAdmin = document.getElementById("delete-role-admin");
const deleteUser = document.getElementById("delete-role-user");

let getCurrentUser = (id) => {
    return fetch(urlMainTable + "/" + id, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(dataUser => {
            let user = {
                id: dataUser.id,
                name: dataUser.name,
                salary: dataUser.salary,
                age: dataUser.age,
                username: dataUser.username,
                password: dataUser.password,
                roles: dataUser.roles
            }
            console.log(user)
        })
}

function getRolesFromEditUserForm() {
    let rolesEdit = Array.from(editRoles.selectedOptions).map(option => option.value);
    let rolesToEdit = new Set();

    rolesEdit.forEach(id => {
        let role = {};
        if (id === "1") {
            role = {
                id: 12,
                role: "ROLE_ADMIN"
            };
        } else if (id === "2") {
            role = {
                id: 11,
                role: "ROLE_USER"
            };
        }
        rolesToEdit.add(role);
    });

    return Array.from(rolesToEdit);
}

allTable.addEventListener("click", e => {
    e.preventDefault();
    let editButtonIsPressed = e.target.id === 'btn-edit-modal';
    let delButtonIsPressed  = e.target.id === 'btn-delete-modal';

    const deleteUsersId       = document.getElementById("id1")
    const deleteUsersName     = document.getElementById("name1")
    const deleteUsersSalary = document.getElementById("salary1")
    const deleteUsersAge      = document.getElementById("age1")
    const deleteUsersEmail    = document.getElementById("username1")

    if (delButtonIsPressed) {
        let currentUserId = e.target.dataset.id;
        fetch(urlMainTable + "/" + currentUserId, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(user => {
                deleteUsersId.value       = user.id;
                deleteUsersName.value     = user.name;
                deleteUsersSalary.value = user.salary;
                deleteUsersAge.value      = user.age;
                deleteUsersEmail.value    = user.username;

                let deleteRoles = user.roles.map(i => i.role)
                deleteRoles.forEach(
                    role => {
                        if (role === "ROLE_ADMIN") {
                            deleteAdmin.setAttribute('selected', "selected");

                        } else if (role === "ROLE_USER") {
                            deleteUser.setAttribute('selected', "selected");
                        }
                    })
            })
        $('#modal-delete').modal('show');

        deleteUserBtn.addEventListener("click", e => {
            e.preventDefault();
            fetch(`${urlMainTable}/${currentUserId}`, {
                method: 'DELETE',
            }
            )
                .then(res => res.json());
            deleteUserBtn.click();
            getAllUsers();
            location.reload();
        })
    }

    //Изменение юзеров

    const editUserId       = document.getElementById("idEdit");
    const editUserName     = document.getElementById("nameEdit");
    const editUserSalary = document.getElementById("salaryEdit");
    const editUserAge      = document.getElementById("ageEdit");
    const editUserEmail    = document.getElementById("usernameEdit");

    if (editButtonIsPressed) {
        let currentUserId = e.target.dataset.id;
        fetch(urlMainTable + "/" + currentUserId, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(user => {

                editUserId.value       = user.id;
                editUserName.value     = user.name;
                editUserSalary.value = user.salary;
                editUserAge.value      = user.age;
                editUserEmail.value    = user.username;

                let editRoles = user.roles.map(i => i.role)
                editRoles.forEach(
                    role => {
                        if (role === "ROLE_ADMIN") {
                            editAdmin.setAttribute('selected', "selected");

                        } else if (role === "ROLE_USER") {
                            editUser.setAttribute('selected', "selected");
                        }
                    })
            })
        $('#modal-edit').modal('show');

        editModalBtn.addEventListener("click", e => {
            e.preventDefault();
            let user = {
                id: editUserId.value,
                name: editUserName.value,
                salary: editUserSalary.value,
                age: editUserAge.value,
                username: editUserEmail.value,
                roles: getRolesFromEditUserForm(),
            }
            fetch(`${urlMainTable}/${currentUserId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user)
            })
                .then(res => console.log(res));
            editModalBtn.click();
            getAllUsers();
            location.reload();
        })
    }
})

const userPanelData      = document.getElementById("userTableBody");
const authorisedUserData = document.getElementById("authorisedUser");

let currentUser = () => {
    fetch ("http://localhost:9818/api/user", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(user => {
            if (user != null) {
                userPanelData.innerHTML = `
                    <tr>
                        <td> ${user.id} </td>
                        <td> ${user.name} </td>
                        <td> ${user.salary} </td>
                        <td> ${user.age} </td>
                        <td> ${user.username} </td>
                        <td> ${user.roles.map((role) => role.role === "ROLE_USER" ? " USER" : " ADMIN")} </td>
                    </tr>
                `
                authorisedUserData.innerHTML = `
                    <p class="d-inline font-weight-light">${user.username} с ролями ${user.roles.map((role) => role.role === "ROLE_USER" ? " USER" : " ADMIN")}</p>`
            }
        })
}
currentUser();





