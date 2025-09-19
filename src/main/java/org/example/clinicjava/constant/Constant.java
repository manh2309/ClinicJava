package org.example.clinicjava.constant;

public class Constant {

    public static final class ERROR_MESSAGE {
        public static final String DATA_NOT_FOUND = "Không tìm thấy dữ liệu";
        public static final String ACCOUNT_EXISTS = "Tài khoản đã tồn tại";
        public static final String ACCOUNT_USERNAME_REQUIRED = "Tài khoản không được để trống";
        public static final String ACCOUNT_PASSWORD_REQUIRED = "Mật khẩu không được để trống";
        public static final String ACCOUNT_PASSWORD_REGEX = "Mật khẩu phải chứa ít nhất một ký tự đặc biệt (@$!%*?&)";
        public static final String ACCOUNT_FULL_NAME_REQUIRED = "Tên không được để trống";
        public static final String ACCOUNT_FULL_NAME_REGEX = "Tên chỉ được chứa chữ và khoảng trắng";
        public static final String ACCOUNT_EMAIL_REQUIRED = "Email không được để trống";
        public static final String ACCOUNT_EMAIL_FORMAT = "Email không đúng định dạng";
        public static final String ROLE_EXISTS = "Chức vụ không tồn tại";
        public static final String PHONE_REGEX = "Số điện thoại phải hợp lệ (VD: 0987654321 hoặc +84987654321)";
    }

    public static final class ROLE_NAME {
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_PATIENT = "ROLE_PATIENT";
        public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
    }

    public static final class MESSAGE {
        public static final String MAIL_BODY = "Xin chào %s,\n\nTài khoản bác sĩ của bạn đã được tạo:\n\nUsername: %s\nPassword: %s\n\nVui lòng đổi mật khẩu sau khi đăng nhập.";
    }
}
