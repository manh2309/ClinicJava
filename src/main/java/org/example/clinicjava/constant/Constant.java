package org.example.clinicjava.constant;

public class Constant {

    public static final class ERROR_MESSAGE {
        public static final String DATA_NOT_FOUND = "Không tìm thấy dữ liệu";
        public static final String ACCOUNT_EXISTS = "Tài khoản đã tồn tại";
        public static final String ACCOUNT_USERNAME_REQUIRED = "Tài khoản không được để trống";
        public static final String ACCOUNT_PASSWORD_REQUIRED = "Mật khẩu không được để trống";
        public static final String ACCOUNT_PASSWORD_REGEX = "Mật khẩu phải chứa ít nhất một ký tự đặc biệt (@$!%*?&)";
        public static final String ACCOUNT_FULL_NAME_REQUIRED = "Mật khẩu không được để trống";
    }

    public static final class ROLE_NAME {
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_PATIENT = "ROLE_PATIENT";
        public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
    }
}
