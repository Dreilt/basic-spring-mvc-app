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

function validateUserProfileEditFormAdminPanel() {
    const firstNameValue = document.userProfileEditFormAdminPanel.firstName.value;
    const isNotFirstNameError = checkIfNotEmpty('firstName', firstNameValue);
    const lastNameValue = document.userProfileEditFormAdminPanel.lastName.value;
    const isNotLastNameError = checkIfNotEmpty('lastName', lastNameValue);

    return isNotFirstNameError && isNotLastNameError;
}

function validateAppUserPasswordEditFormAdminPanel() {
        const newPasswordValue = document.appUserPasswordEditFormAdminPanel.newPassword.value;
        const isNotNewPasswordError = checkIfNotEmpty('newPassword', newPasswordValue);
        const confirmNewPasswordValue = document.appUserPasswordEditFormAdminPanel.confirmNewPassword.value;
        const isNotConfirmNewPasswordError = checkIfNotEmpty('confirmNewPassword', confirmNewPasswordValue);

        return isNotNewPasswordError && isNotConfirmNewPasswordError;
}

function validateUserProfileEditForm() {
    const firstNameValue = document.userProfileEditForm.firstName.value;
    const isNotFirstNameError = checkIfNotEmpty('firstName', firstNameValue);
    const lastNameValue = document.userProfileEditForm.lastName.value;
    const isNotLastNameError = checkIfNotEmpty('lastName', lastNameValue);

    return isNotFirstNameError && isNotLastNameError;
}

function validateUserPasswordEditForm() {
    const currentPasswordValue = document.userPasswordEditForm.currentPassword.value;
    const isNotCurrentPasswordError = checkIfNotEmpty('currentPassword', currentPasswordValue);
    const newPasswordValue = document.userPasswordEditForm.newPassword.value;
    const isNotNewPasswordError = checkIfNotEmpty('newPassword', newPasswordValue);
    const confirmNewPasswordValue = document.userPasswordEditForm.confirmNewPassword.value;
    const isNotConfirmNewPasswordError = checkIfNotEmpty('confirmNewPassword', confirmNewPasswordValue);

    return isNotCurrentPasswordError && isNotNewPasswordError && isNotConfirmNewPasswordError;
}

function checkIfNotEmpty(fieldName, fieldValue) {
    if (!fieldValue) {
        document.getElementById(`${fieldName}`).className='form-control is-invalid';
        if (document.getElementById(`${fieldName}ErrorMessage`).style.display === 'none') {
            document.getElementById(`${fieldName}ErrorMessage`).style.display = '';
            document.getElementById(`${fieldName}`).value = '';
        }

        return false;
    }

    document.getElementById(`${fieldName}`).className='form-control';
    if (document.getElementById(`${fieldName}ErrorMessage`).style.display === '') {
        document.getElementById(`${fieldName}ErrorMessage`).style.display = 'none';
    }

    return true;
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