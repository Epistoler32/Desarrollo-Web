document.getElementById('signupForm').addEventListener('submit', function (e) {
  const pass = document.getElementById('contrasena').value;
  const confirm = document.getElementById('confirmarContrasena').value;
  const errorDiv = document.getElementById('passwordError');

  if (pass !== confirm) {
    e.preventDefault();
    errorDiv.style.display = 'block';
    document.getElementById('confirmarContrasena').focus();
  } else {
    errorDiv.style.display = 'none';
  }
});

document.getElementById('confirmarContrasena').addEventListener('input', function () {
  document.getElementById('passwordError').style.display = 'none';
});