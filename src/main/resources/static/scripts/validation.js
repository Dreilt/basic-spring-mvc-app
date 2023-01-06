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

function validateCreateEventForm() {
    const nameValue = document.createEventForm.name.value;
    const isNotNameError = checkIfNotEmpty('name', nameValue);
    const eventTypeValue = document.createEventForm.eventType.value;
    const isNotEventTypeError = checkIfNotEmpty('eventType', eventTypeValue);
    const dateAndTimeValue = document.createEventForm.dateAndTime.value;
    const isNotDateAndTimeError = checkIfNotEmpty('dateAndTime', dateAndTimeValue);
    const languageValue = document.createEventForm.language.value;
    const isNotLanguageError = checkIfNotEmpty('language', languageValue);
    const admissionValue = document.createEventForm.admission.value;
    const isNotAdmissionError = checkIfNotEmpty('admission', admissionValue);
    const cityValue = document.createEventForm.city.value;
    const isNotCityError = checkIfNotEmpty('city', cityValue);
    const locationValue = document.createEventForm.location.value;
    const isNotLocationError = checkIfNotEmpty('location', locationValue);
    const addressValue = document.createEventForm.address.value;
    const isNotAddressError = checkIfNotEmpty('address', addressValue);
    const descriptionValue = document.createEventForm.description.value;
    const isNotDescriptionError = checkIfNotEmpty('description', descriptionValue);

    return isNotNameError && isNotEventTypeError && isNotDateAndTimeError && isNotLanguageError && isNotAdmissionError &&
           isNotCityError && isNotLocationError && isNotAddressError && isNotDescriptionError;
}

function validateEditEventForm() {
    const nameValue = document.editEventForm.name.value;
    const isNotNameError = checkIfNotEmpty('name', nameValue);
    const eventTypeValue = document.editEventForm.eventType.value;
    const isNotEventTypeError = checkIfNotEmpty('eventType', eventTypeValue);
    const dateAndTimeValue = document.editEventForm.dateAndTime.value;
    const isNotDateAndTimeError = checkIfNotEmpty('dateAndTime', dateAndTimeValue);
    const languageValue = document.editEventForm.language.value;
    const isNotLanguageError = checkIfNotEmpty('language', languageValue);
    const admissionValue = document.editEventForm.admission.value;
    const isNotAdmissionError = checkIfNotEmpty('admission', admissionValue);
    const cityValue = document.editEventForm.city.value;
    const isNotCityError = checkIfNotEmpty('city', cityValue);
    const locationValue = document.editEventForm.location.value;
    const isNotLocationError = checkIfNotEmpty('location', locationValue);
    const addressValue = document.editEventForm.address.value;
    const isNotAddressError = checkIfNotEmpty('address', addressValue);
    const descriptionValue = document.editEventForm.description.value;
    const isNotDescriptionError = checkIfNotEmpty('description', descriptionValue);

    return isNotNameError && isNotEventTypeError && isNotDateAndTimeError && isNotLanguageError && isNotAdmissionError &&
           isNotCityError && isNotLocationError && isNotAddressError && isNotDescriptionError;
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