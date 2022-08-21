function validateLoginForm() {
    const usernameValue = document.loginForm.username.value;
    const isNotUsernameError = checkIfNotEmpty('username', usernameValue);
    const passwordValue = document.loginForm.password.value;
    const isNotPasswordError = checkIfNotEmpty('password', passwordValue);

    return isNotUsernameError && isNotPasswordError;
}

function validateRegistrationForm() {
    const firstNameValue = document.registrationForm.firstName.value;
    const isNotFirstNameError = checkIfNotEmpty('firstName', firstNameValue);
    const lastNameValue = document.registrationForm.lastName.value;
    const isNotLastNameError = checkIfNotEmpty('lastName', lastNameValue);
    const emailValue = document.registrationForm.email.value;
    const isNotEmailError = checkIfNotEmpty('email', emailValue);
    const passwordValue = document.registrationForm.password.value;
    const isNotPasswordError = checkIfNotEmpty('password', passwordValue);
    const confirmPasswordValue = document.registrationForm.confirmPassword.value;
    const isNotConfirmPasswordError = checkIfNotEmpty('confirmPassword', confirmPasswordValue);

    return isNotFirstNameError && isNotLastNameError && isNotEmailError && isNotPasswordError && isNotConfirmPasswordError;
}

function checkIfNotEmpty(fieldName, fieldValue) {
    if (!fieldValue) {
        document.getElementById(fieldName).className='form-control is-invalid';
        if (!document.getElementById(`${fieldName}Error`)) {
            document.getElementById(`${fieldName}FormGroup`).innerHTML+=`<div id="${fieldName}Error" class="error-message">Wype\u0142nij to pole</div>`;
        }
        return false
    }

    document.getElementById(fieldName).className='form-control';
    if (document.getElementById(`${fieldName}Error`) !== null) {
        document.getElementById(`${fieldName}Error`).remove();
    }
    return true
}

function validateSearchForm() {
    const searchQueryValue = document.searchForm.search_query.value;
    if (!searchQueryValue) {
        return false;
    }

    for (let i = 0; i < searchQueryValue.length; i++) {
        if (searchQueryValue[i] !== ' ') {
            return true;
        }
    }

    return false;
}