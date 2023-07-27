package com.example.one_vote_service.config.constant;

public class MessageCode {

    // TODO: MessageCode khi thất bại
    public static final String EMAIL_IS_EXIST = "Email này đã tồn tại";
    public static final String USERNAME_IS_EXIST = "Tài khoản này đã tồn tại";
    public static final String USERNAME_PASSWORD_NOT_EXIST = "Tài khoản hoặc mật khẩu không chính xác";
    public static final String STATUS_USER_FALSE = "Tài khoản đã bị khóa";
    public static final String USER_NOT_EXIST = "Người dùng không tồn tại";
    public static final String GROUP_NOT_EXIST = "Nhóm dùng không tồn tại";
    public static final String PASSWORD_OLD_NOT_EXIST = "Mật khẩu cũ không chính xác";
    public static final String USER_NOT_FORBIDDEN = "Người dùng không có quyền truy cập!";
    public static final String LOGIN_NOT_UNAUTHORIZED = "Phiên đăng nhập không hợp lệ!";
    public static final String REFRESH_TOKEN_NOT_EXIST = "Phiên đăng nhập không hợp lệ!";


    // TODO: MessageCode code thành công
    public static final String REGISTER_USER_SUCCESS = "Đăng ký tài khoản thành công";
    public static final String LOGOUT_SUCCESS = "Đăng xuất thành công";
    public static final String DELETE_USER_GROUP_SUCCESS = "Xóa nhóm quyền thành công";
    public static final String ADD_USER_GROUP_SUCCESS = "Thêm nhóm quyền thành công";
    public static final String UPDATE_USER_GROUP_SUCCESS = "Sửa nhóm quyền thành công";
    public static final String CHANGE_PASSWORD_SUCCESS = "Đổi mật khẩu thành công";
    public static final String RESET_PASSWORD_SUCCESS = "Thay đổi mật khẩu thành 123@123a thành công";
    public static final String SEND_EMAIL_PASSWORD_SUCCESS = "Lấy lại mật khẩu thành công";
}
