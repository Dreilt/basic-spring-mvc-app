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