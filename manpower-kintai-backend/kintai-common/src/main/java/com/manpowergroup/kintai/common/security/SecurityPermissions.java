package com.manpowergroup.kintai.common.security;

public final class SecurityPermissions {

    public static final String SUPER_ADMIN = "hasAuthority('ROLE_SUPER_ADMIN')";

    public static final String ADMIN_EMPLOYEE_READ = "admin:employee:read";
    public static final String ADMIN_EMPLOYEE_WRITE = "admin:employee:write";
    public static final String ADMIN_MENU_READ = "admin:menu:read";
    public static final String ADMIN_MENU_WRITE = "admin:menu:write";
    public static final String ADMIN_PERMISSION_READ = "admin:permission:read";
    public static final String ADMIN_PERMISSION_WRITE = "admin:permission:write";
    public static final String ADMIN_ROLE_READ = "admin:role:read";
    public static final String ADMIN_ROLE_WRITE = "admin:role:write";
    public static final String EMPLOYEE_TIMESHEET_READ = "employee:timesheet:read";
    public static final String EMPLOYEE_TIMESHEET_WRITE = "employee:timesheet:write";
    public static final String MANAGER_SUBORDINATE_READ = "manager:subordinate:read";
    public static final String HR_EMPLOYEE_ONBOARD = "hr:employee:onboard";

    public static final String HAS_ADMIN_EMPLOYEE_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:employee:read')";
    public static final String HAS_ADMIN_EMPLOYEE_WRITE =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:employee:write')";
    public static final String HAS_ADMIN_MENU_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:menu:read')";
    public static final String HAS_ADMIN_MENU_WRITE =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:menu:write')";
    public static final String HAS_ADMIN_PERMISSION_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:permission:read')";
    public static final String HAS_ADMIN_PERMISSION_WRITE =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:permission:write')";
    public static final String HAS_ADMIN_ROLE_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:role:read')";
    public static final String HAS_ADMIN_ROLE_WRITE =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('admin:role:write')";
    public static final String HAS_EMPLOYEE_TIMESHEET_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('employee:timesheet:read')";
    public static final String HAS_EMPLOYEE_TIMESHEET_WRITE =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('employee:timesheet:write')";
    public static final String HAS_MANAGER_SUBORDINATE_READ =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('manager:subordinate:read')";
    public static final String HAS_HR_EMPLOYEE_ONBOARD =
            "hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('hr:employee:onboard')";

    private SecurityPermissions() {
    }
}
