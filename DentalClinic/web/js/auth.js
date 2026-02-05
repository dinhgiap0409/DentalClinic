document.addEventListener('DOMContentLoaded', function () {
    // Floating label effect
    const inputs = document.querySelectorAll('.form-control');
    inputs.forEach(input => {
        const formGroup = input.closest('.form-group');
        
        const updateState = () => {
            if (input.value.length > 0) {
                formGroup.classList.add('has-value');
            } else {
                formGroup.classList.remove('has-value');
            }
        };

        input.addEventListener('focus', () => formGroup.classList.add('focused'));
        input.addEventListener('blur', () => {
            formGroup.classList.remove('focused');
            updateState();
        });

        updateState(); // Initial check
    });

    // Sliding animation
    document.getElementById('moveleft').addEventListener('click', () => {
        window.location.href = 'register';
    });

    document.getElementById('moveright').addEventListener('click', () => {
        window.location.href = 'login';
    });
});