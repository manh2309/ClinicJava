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
        public static final String ACCOUNT_EMAIL_DUPPLICATE = "Email này đã được sử dụng";
        public static final String ROLE_EXISTS = "Chức vụ không tồn tại";
        public static final String PHONE_REGEX = "Số điện thoại phải hợp lệ (VD: 0987654321 hoặc +84987654321)";
        public static final String APPOINTMENT_NOT_EQUAL = "Bạn không được phép hủy cuộc hẹn này";
        public static final String APPOINTMENT_NOT_CANCELLED = "Cuộc hẹn không thể bị hủy";
    }

//    public static final class ROLE_NAME {
//        public static final String ROLE_ADMIN = "ROLE_ADMIN";
//        public static final String ROLE_PATIENT = "ROLE_PATIENT";
//        public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
//    }

    public enum ROLE_NAME {
        ROLE_PATIENT(1L, "ROLE_PATIENT"),
        ROLE_DOCTOR(2L, "ROLE_DOCTOR"),
        ROLE_ADMIN(3L, "ROLE_ADMIN");

        private final Long code;
        private final String name;

        ROLE_NAME(Long code, String name) {
            this.code = code;
            this.name = name;
        }

        public Long getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static final class MESSAGE {
        public static final String MAIL_BODY = "Xin chào %s,\n\nTài khoản bác sĩ của bạn đã được tạo:\n\nUsername: %s\nPassword: %s\n\nVui lòng đổi mật khẩu sau khi đăng nhập.";
        public static final String DOCTOR_CREATE_ACCOUNT = "Tài khoản bác sĩ được tạo thành công";
        public static final String APPOINTMENT_CREATE_SUCCESS = "Lịch khám đã được tạo thành công";
        public static final String APPOINTMENT_CANCELLED_SUCCESS = "Hủy lịch khám thành công";
        public static final String UPDATE_ACCOUNT = "Cập nhật tài khoản thành công";
        public static final String DELETE_ACCOUNT = "Xóa tài khoản thành công";
    }

    public static final class ACTIVE {
        public static final Long IS_ACTIVE = 1L;
        public static final Long IS_NOT_ACTIVE = 0L;
    }

    public static final class APOINTMENT_STATUS {
        public static final Long PENDING = 0L;
        public static final Long CONFIRMED = 1L;
        public static final Long COMPLETED = 2L;
        public static final Long CANCELLED = 3L;
    }
    public static final class PAYMENT_STATUS {
        public static final Long UNPAID = 0L;
        public static final Long PAID = 1L;
        public static final Long CANCELLED = 2L;

        public static final Long UNDEFINED = 0L;
        public static final Long BANK_TRANSFER = 1L;
        public static final Long MOMO = 2L;
        public static final Long VNPAY = 3L;
        public static final Long CASH = 4L;
    }
}
