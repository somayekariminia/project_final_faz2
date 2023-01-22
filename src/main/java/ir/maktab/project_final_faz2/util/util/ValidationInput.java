package ir.maktab.util;

import ir.maktab.exception.ValidationException;

public class ValidationInput {
    private final static InterFaceValid valid = (v, r, e) -> {
        if (v.equals("") || !v.matches(r))
            throw new ValidationException(e);
    };

    public static void validatePassword(String userName) {
        valid.accept(userName, "(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$", "Your Username Is Invalid");
    }

    public static void validateEmail(String email) {
        valid.accept(email, "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.]+@[a-zA-Z0-9.]+$", "Your email Is Invalid");
    }

    public static void validateName(String name) {
        valid.accept(name, "[a-zA-Z]+", "your name is invalid");
    }

}
