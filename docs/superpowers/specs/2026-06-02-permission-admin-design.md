# Permission Admin Design

## Goal

Build the system administration surface for menu, permission, and role management. The feature completes the current RBAC foundation so administrators can maintain menus and API permissions, create roles, and grant both menus and permissions to a role in one flow.

## Model

The access model is:

```text
employee -> role -> menu
                 -> permission
```

Menus decide what pages and navigation entries a user can see. Permissions decide which actions and APIs a user can execute. Roles package menus and permissions for employees.

Frontend checks are for user experience only. Backend `@PreAuthorize` checks remain the final access-control boundary.

## Scope

Implement these admin features:

- Menu management: list tree, create, update, show, hide, enable, disable, delete.
- Permission management: list/page, filter by menu, create, update, enable, disable, delete.
- Role management: list/page, create, update, enable, disable, delete.
- Role authorization: edit menu grants and permission grants together in one drawer/dialog and save in one action.
- SQL patch: add system-management menus and permissions, bind them to `SUPER_ADMIN`, and initialize the first `admin` account as super administrator with password `12345678`.

Employee role assignment is not part of this implementation. New employee onboarding already supports initial role assignment; employee-detail role editing can be added later.

## UI Design

Menu, permission, and role list pages use switches for resource state:

- `visible`: menu show/hide.
- `status`: menu, permission, and role enable/disable.

Role authorization uses checkboxes because authorization is a selection relationship:

- Left side: menu tree with checkbox selection.
- Right side: permissions grouped by menu/module with checkbox selection.
- Footer: save authorization.

Checking a menu does not automatically check all permissions. Checking a permission whose menu is not selected should be allowed, but the UI should warn that the user may have API access without a navigation entry.

## Backend API

Reuse existing admin routes:

- `/admin/sys/menus`
- `/admin/sys/permissions`
- `/admin/sys/roles`

Add or tighten APIs as needed:

- Query role authorization detail: selected menu IDs, selected permission IDs, all menus, all permissions.
- Save role authorization in one request: menu IDs and permission IDs.

Existing separate role menu and role permission assignment APIs may remain for compatibility, but the frontend should prefer the combined authorization flow.

## Deletion Rules

All deletes are logical deletes.

Menu deletion:

- Deleting a parent menu cascades logical deletion to descendant menus.
- If the menu or any descendant has linked permissions, deletion is blocked.
- Deletion removes related `sys_role_menu` bindings.

Permission deletion:

- If a permission is assigned to any role, deletion is blocked.
- Otherwise it is logically deleted.

Role deletion:

- If a role is assigned to any employee, deletion is blocked.
- Otherwise it is logically deleted and related `sys_role_menu` and `sys_role_permission` bindings are removed.

Disable should be preferred over delete for temporary changes.

## SQL Patch

Provide executable MySQL and MariaDB patch scripts. The scripts should:

- Add the system-management menu group and child menus for menu, permission, and role management.
- Add read/write permissions for menu, permission, and role management.
- Ensure `SUPER_ADMIN` exists and is assigned to the initial `admin` employee.
- Set the `admin` account password to BCrypt hash of `12345678`.
- Bind system-management menus to `SUPER_ADMIN`.
- Keep `SUPER_ADMIN` clean by removing explicit business permission grants such as timesheet, subordinate, and onboarding permissions. The backend still returns `*` for `SUPER_ADMIN`.

## Out Of Scope

- Employee detail role assignment.
- Automatic controller scanning for permissions.
- Splitting every write permission into create/update/delete.
- Audit logs for permission changes.

