# Permission Admin Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build menu, permission, and role administration with combined role authorization and executable SQL patches.

**Architecture:** Reuse the existing admin API routes and RBAC tables. Backend service methods enforce deletion rules and expose a combined role authorization DTO. Frontend adds focused system-management API modules and Vue views using Element Plus tables, trees, dialogs, checkboxes, and switches.

**Tech Stack:** Java Spring Boot, MyBatis Plus, Maven, Vue 3, Pinia, Vue Router, Element Plus, TypeScript, Vite.

---

## File Structure

- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysMenuService.java`: keep menu contract and clarify deletion behavior through implementation.
- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysMenuServiceImpl.java`: cascade menu logical delete, block deletion when linked permissions exist, clear role menu bindings.
- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysPermissionService.java`: keep permission contract.
- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImpl.java`: block permission deletion when role bindings exist and add duplicate code validation.
- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysRoleService.java`: add combined authorization load/save methods.
- Modify `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysRoleServiceImpl.java`: role deletion checks, combined authorization transaction, selected grants.
- Create `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/dto/sys/RoleAuthorizationResponse.java`: all menus, all permissions, selected menu IDs, selected permission IDs.
- Create `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/dto/sys/RoleAuthorizationRequest.java`: menu IDs and permission IDs for one-save authorization.
- Modify `manpower-kintai-backend/kintai-admin-api/src/main/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysRoleController.java`: expose `GET /{id}/authorization` and `PUT /{id}/authorization`.
- Create `manpower-kintai-frontend/src/types/system/index.ts`: menu, permission, role, authorization types.
- Create `manpower-kintai-frontend/src/api/system/menu.ts`: menu admin API.
- Create `manpower-kintai-frontend/src/api/system/permission.ts`: permission admin API.
- Create `manpower-kintai-frontend/src/api/system/role.ts`: role admin API and authorization API.
- Create `manpower-kintai-frontend/src/views/system/MenuManagementView.vue`: menu tree table CRUD.
- Create `manpower-kintai-frontend/src/views/system/PermissionManagementView.vue`: permission table CRUD.
- Create `manpower-kintai-frontend/src/views/system/RoleManagementView.vue`: role table CRUD and combined authorization drawer.
- Modify `manpower-kintai-frontend/src/router/index.ts`: add system management routes with permission meta.
- Create `manpower-kintai-backend/docs/sql/patch/V2_permission_admin_MYSQL.sql`: MySQL incremental patch.
- Create `manpower-kintai-backend/docs/sql/patch/V2_permission_admin_MARIADB.sql`: MariaDB incremental patch.

---

### Task 1: Backend Role Authorization DTO And API

**Files:**
- Create: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/dto/sys/RoleAuthorizationRequest.java`
- Create: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/dto/sys/RoleAuthorizationResponse.java`
- Modify: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysRoleService.java`
- Modify: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysRoleServiceImpl.java`
- Modify: `manpower-kintai-backend/kintai-admin-api/src/main/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysRoleController.java`

- [ ] **Step 1: Add authorization request DTO**

Create `RoleAuthorizationRequest` with `List<Long> menuIds` and `List<Long> permissionIds`, both defaulting to empty lists when omitted by the frontend.

- [ ] **Step 2: Add authorization response DTO**

Create `RoleAuthorizationResponse` with `List<MenuResponse> menus`, `List<SysPermission> permissions`, `List<Long> selectedMenuIds`, and `List<Long> selectedPermissionIds`.

- [ ] **Step 3: Extend role service contract**

Add:

```java
RoleAuthorizationResponse getAuthorization(Long roleId);
void saveAuthorization(Long roleId, RoleAuthorizationRequest request);
```

- [ ] **Step 4: Implement combined authorization**

In `SysRoleServiceImpl`, inject `SysMenuService`, `SysPermissionMapper`, and implement selected ID loading from `sys_role_menu` and `sys_role_permission`. `saveAuthorization` should delete existing role menu and permission bindings, then insert the submitted IDs in one `@Transactional` method.

- [ ] **Step 5: Add controller endpoints**

Add:

```java
@GetMapping("/{id}/authorization")
@PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_READ)
public Result<RoleAuthorizationResponse> getAuthorization(@PathVariable Long id)

@PutMapping("/{id}/authorization")
@PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
public Result<Void> saveAuthorization(@PathVariable Long id, @RequestBody @Valid RoleAuthorizationRequest request)
```

### Task 2: Backend Deletion Rules

**Files:**
- Modify: `SysMenuServiceImpl.java`
- Modify: `SysPermissionServiceImpl.java`
- Modify: `SysRoleServiceImpl.java`

- [ ] **Step 1: Menu delete rule**

Inject `SysPermissionMapper`. In menu removal, collect the target menu ID and all descendants, block if any `SysPermission.menuId` is in that set, delete `sys_role_menu` rows for those menu IDs, then logical-delete each menu.

- [ ] **Step 2: Permission delete rule**

Before removing a permission, count `sys_role_permission` rows by permission ID. If count is greater than zero, throw a `BizException` with a role binding error code.

- [ ] **Step 3: Role delete rule**

Inject `SysEmployeeRoleMapper`. Before removing a role, count `sys_employee_role` rows by role ID. If count is greater than zero, throw a `BizException`. If not bound, delete `sys_role_menu` and `sys_role_permission` rows for the role, then logical-delete the role.

### Task 3: Frontend API And Types

**Files:**
- Create: `src/types/system/index.ts`
- Create: `src/api/system/menu.ts`
- Create: `src/api/system/permission.ts`
- Create: `src/api/system/role.ts`

- [ ] **Step 1: Define types**

Define `SystemMenu`, `SystemPermission`, `SystemRole`, `RoleAuthorization`, `RoleAuthorizationPayload`, and generic page response helpers matching existing backend fields.

- [ ] **Step 2: Add API modules**

Implement request functions for menu CRUD and status actions, permission CRUD and status actions, role CRUD/status actions, `fetchRoleAuthorization`, and `saveRoleAuthorization`.

### Task 4: Frontend System Management Views

**Files:**
- Create: `src/views/system/MenuManagementView.vue`
- Create: `src/views/system/PermissionManagementView.vue`
- Create: `src/views/system/RoleManagementView.vue`
- Modify: `src/router/index.ts`

- [ ] **Step 1: Add menu management view**

Use an Element Plus tree table. Provide create/edit dialog, delete confirmation, visible switch, and status switch.

- [ ] **Step 2: Add permission management view**

Use an Element Plus table with menu filter, create/edit dialog, delete confirmation, and status switch.

- [ ] **Step 3: Add role management view**

Use an Element Plus table with create/edit dialog, delete confirmation, status switch, and authorization drawer.

- [ ] **Step 4: Add combined role authorization drawer**

Use checkbox trees/lists for menu and permission grants. Save both IDs through `PUT /admin/sys/roles/{id}/authorization`.

- [ ] **Step 5: Register routes**

Add routes under `/admin/system/menus`, `/admin/system/permissions`, and `/admin/system/roles` with permission meta `admin:menu:read`, `admin:permission:read`, and `admin:role:read`.

### Task 5: SQL Patches

**Files:**
- Create: `manpower-kintai-backend/docs/sql/patch/V2_permission_admin_MYSQL.sql`
- Create: `manpower-kintai-backend/docs/sql/patch/V2_permission_admin_MARIADB.sql`

- [ ] **Step 1: Add system-management menus**

Patch scripts insert or update menu group `system-management` and children `system-menus`, `system-permissions`, `system-roles`.

- [ ] **Step 2: Add permissions**

Patch scripts insert or update `admin:menu:read`, `admin:menu:write`, `admin:permission:read`, `admin:permission:write`, `admin:role:read`, and `admin:role:write`.

- [ ] **Step 3: Initialize admin**

Patch scripts set the `admin` account password to a BCrypt hash for `12345678`, ensure `SUPER_ADMIN` exists, assign it to the first admin employee/account, bind system-management menus to `SUPER_ADMIN`, and remove explicit business permission grants from `SUPER_ADMIN`.

### Task 6: Verification

**Files:**
- All modified backend, frontend, and SQL files.

- [ ] **Step 1: Backend compile/test**

Run:

```powershell
mvn test
```

Expected: Maven build succeeds or only pre-existing environment/database integration limitations are reported.

- [ ] **Step 2: Frontend typecheck/build**

Run:

```powershell
npm run build
```

from `manpower-kintai-frontend`.

Expected: Vue type-check and Vite build complete.

- [ ] **Step 3: SQL sanity check**

Search the patch scripts for every required menu code, permission code, `SUPER_ADMIN`, and `12345678` hash reference.

