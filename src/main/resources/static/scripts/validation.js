function validateLoginForm() {
    let usernameValue = document.loginForm.username.value;
    let isUsernameError = checkIfNotEmpty('username', usernameValue);
    let passwordValue = document.loginForm.password.value;
    let isPasswordError = checkIfNotEmpty('password', passwordValue);

    if (!isUsernameError || !isPasswordError) {
        return false;
    } else {
        return true;
    }
}

function validateRegistrationForm() {
    let firstNameValue = document.registrationForm.firstName.value;
    let isFirstNameError = checkIfNotEmpty('firstName', firstNameValue);
    let lastNameValue = document.registrationForm.lastName.value;
    let isLastNameError = checkIfNotEmpty('lastName', lastNameValue);
    let emailValue = document.registrationForm.email.value;
    let isEmailError = checkIfNotEmpty('email', emailValue);
    let passwordValue = document.registrationForm.password.value;
    let isPasswordError = checkIfNotEmpty('password', passwordValue);
    let confirmPasswordValue = document.registrationForm.confirmPassword.value;
    let isConfirmPasswordError = checkIfNotEmpty('confirmPassword', confirmPasswordValue);

    if (!isFirstNameError || !isLastNameError || !isEmailError || !isPasswordError || !isConfirmPasswordError) {
        return false;
    } else {
        return true;
    }
}

function checkIfNotEmpty(fieldName, fieldValue) {
    let isNotEmpty = true;

    if (fieldValue === null || fieldValue === '') {
        document.getElementById(fieldName).className='form-control is-invalid';
        if (document.getElementById(fieldName + 'Error') === null) {
            document.getElementById(fieldName + 'FormGroup').innerHTML+='<div id="' + fieldName + 'Error" class="error-message">Wype\u0142nij to pole</div>';
        }
        isNotEmpty = false;
    } else {
        document.getElementById(fieldName).className='form-control';
        if (document.getElementById(fieldName + 'Error') !== null) {
            document.getElementById(fieldName + 'Error').remove();
        }
    }

    return isNotEmpty;
}