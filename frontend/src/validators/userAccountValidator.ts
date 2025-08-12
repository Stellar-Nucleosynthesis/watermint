export function validateEmail(value: string) {
    if (!value) return "Email is required";
    if (value.length > 254) return "Email must be at most 254 characters";
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) return "Invalid email format";
    return null;
};

export function validateName(value: string) {
    if (!value) return "Name is required";
    if (value.length > 100) return "Name must be at most 100 characters";
    return null;
};

export function validateUsername(value: string) {
    if (!value) return "Username is required";
    if (value.length > 24) return "Username must be at most 24 characters";
    return null;
};

export function validatePassword(value: string) {
    if (!value) return "Password is required";
    return null;
};

export function validatePasswordConfirm(value: string, pass: string) {
    if (!value) return "Please confirm your password";
    if (value !== pass) return "Passwords do not match";
    return null;
};

export function validateAgreement(checked: boolean) {
    if (!checked) return "You must agree to the terms and conditions";
    return null;
};