# Permission Filtered Pagination Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the permission management list use database pagination for all requests while supporting descendant-menu and code/name keyword filters.

**Architecture:** Keep one `GET /admin/sys/permissions` resource. The controller forwards optional filters to `SysPermissionService`; the service resolves descendant menu IDs and builds one MyBatis-Plus paged query. The Vue page keeps draft and submitted filter state so pagination and mutations refresh the currently displayed result set.

**Tech Stack:** Java 21, Spring Boot 3.4, MyBatis-Plus 3.5.7, JUnit 5, Mockito, Vue 3, TypeScript 6, Element Plus, Node test runner.

## Global Constraints

- Preserve all unrelated user changes in the dirty worktree; stage only files named by the current task.
- Do not restore or add `GET /admin/sys/permissions/by-menu/{menuId}`.
- Do not modify the route permission `admin:permission:read`.
- Search is submitted only by Enter, the Search button, or Reset; typing and selecting a menu do not send requests.
- A selected parent menu includes itself and every descendant menu.
- `keyword` matches `code` or `name` by contains semantics, with case-insensitive code matching.
- Filtered `records`, `total`, `page`, `size`, and `pages` come from the database page result.

---

## File Map

- `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysPermissionService.java`: filtered pagination application interface.
- `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImpl.java`: descendant resolution and MyBatis-Plus paged query.
- `manpower-kintai-backend/kintai-module-system/src/test/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImplTest.java`: service query behavior tests.
- `manpower-kintai-backend/kintai-admin-api/src/main/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionController.java`: HTTP query parameter mapping.
- `manpower-kintai-backend/kintai-admin-api/src/test/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionControllerTest.java`: controller contract test.
- `manpower-kintai-frontend/src/api/system/permission.ts`: one typed filtered-page request.
- `manpower-kintai-frontend/src/views/system/PermissionManagementView.vue`: filter form, submitted state, pagination, and removal of Refresh.
- `manpower-kintai-frontend/tests/permissionManagementPagination.test.ts`: lightweight API/view contract regression test consistent with the existing frontend test style.

---

### Task 1: Add filtered database pagination to the application service

**Files:**
- Modify: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysPermissionService.java`
- Modify: `manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImpl.java`
- Modify: `manpower-kintai-backend/kintai-module-system/src/test/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImplTest.java`

**Interfaces:**
- Consumes: `PageRequest(page, size)`, optional `Long menuId`, optional `String keyword`, `SysMenuMapper`, and `SysPermissionMapper`.
- Produces: `PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request)`.

- [ ] **Step 1: Write failing service tests**

Extend `SysPermissionServiceImplTest` with fixtures and tests that call the desired public API before it exists:

```java
private static SysMenu menu(long id, Long parentId) {
    return SysMenu.create(parentId, "menu-" + id, "menu-" + id,
                    "/menu-" + id, null, null, 2, (int) id, 1)
            .setId(id);
}

@Test
void pageCombinesDescendantMenusAndKeywordInDatabaseQuery() {
    SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
    SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
    SysMenuMapper menuMapper = Mockito.mock(SysMenuMapper.class);
    SysPermissionServiceImpl service = new SysPermissionServiceImpl(
            Mockito.mock(SysEmployeeRoleMapper.class), rolePermissionMapper, menuMapper);
    ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);

    when(menuMapper.selectList(any())).thenReturn(List.of(
            menu(1L, null), menu(2L, 1L), menu(3L, 2L), menu(9L, null)));
    when(permissionMapper.selectPage(any(Page.class), any())).thenAnswer(invocation -> {
        Page<SysPermission> page = invocation.getArgument(0);
        page.setRecords(List.of(new SysPermission().setId(20L)));
        page.setTotal(11L);
        return page;
    });

    PageResult<SysPermission> result = service.page(1L, " ADMIN ", PageRequest.of(2, 10));

    @SuppressWarnings("unchecked")
    ArgumentCaptor<Wrapper<SysPermission>> wrapperCaptor = ArgumentCaptor.forClass(Wrapper.class);
    verify(permissionMapper).selectPage(any(Page.class), wrapperCaptor.capture());
    Wrapper<SysPermission> capturedWrapper = wrapperCaptor.getValue();
    String sql = capturedWrapper.getSqlSegment();
    assertTrue(sql.contains("menu_id IN"));
    assertTrue(sql.contains("LOWER(code) LIKE"));
    assertTrue(sql.contains("LOWER(name) LIKE"));
    assertTrue(sql.contains("sort ASC"));
    assertTrue(sql.contains("id ASC"));
    AbstractWrapper<?, ?, ?> abstractWrapper = (AbstractWrapper<?, ?, ?>) capturedWrapper;
    Set<Long> longParams = abstractWrapper.getParamNameValuePairs().values().stream()
            .filter(Long.class::isInstance)
            .map(Long.class::cast)
            .collect(Collectors.toSet());
    assertEquals(Set.of(1L, 2L, 3L), longParams);
    assertEquals(11L, result.getTotal());
    assertEquals(2L, result.getPage());
    assertEquals(10L, result.getSize());
}

@Test
void pageReturnsEmptyResultForUnknownMenuWithoutQueryingPermissions() {
    SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
    SysMenuMapper menuMapper = Mockito.mock(SysMenuMapper.class);
    SysPermissionServiceImpl service = new SysPermissionServiceImpl(
            Mockito.mock(SysEmployeeRoleMapper.class),
            Mockito.mock(SysRolePermissionMapper.class), menuMapper);
    ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);
    when(menuMapper.selectList(any())).thenReturn(List.of(menu(1L, null)));

    PageResult<SysPermission> result = service.page(999L, null, PageRequest.of(1, 10));

    assertTrue(result.getRecords().isEmpty());
    assertEquals(0L, result.getTotal());
    assertEquals(1L, result.getPage());
    assertEquals(10L, result.getSize());
    verify(permissionMapper, never()).selectPage(any(Page.class), any());
}
```

Add the concrete imports used above: `AbstractWrapper`, `Wrapper`, `Page`, `PageRequest`, `PageResult`, `SysMenu`, `SysMenuMapper`, `List`, `Set`, `Collectors`, `ArgumentCaptor`, and the `assertEquals`/`assertTrue` static assertions. Update the existing `removeBlocksPermissionAssignedToRole` constructor call to pass a mocked `SysMenuMapper` as its third argument.

- [ ] **Step 2: Run the focused test and verify RED**

Run from `manpower-kintai-backend`:

```powershell
mvn.cmd -pl kintai-module-system -am -Dtest=SysPermissionServiceImplTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Expected: compilation fails because the three-argument constructor and `page(Long, String, PageRequest)` do not exist yet.

- [ ] **Step 3: Implement the minimal filtered-page service**

Change the interface method and retain `listByMenu` because it may still serve application-layer callers unrelated to this HTTP endpoint:

```java
PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request);
```

Add `private final SysMenuMapper menuMapper;` to `SysPermissionServiceImpl`. Replace the existing page method and add the two focused helpers:

```java
@Override
public PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request) {
    List<Long> menuIds = menuId == null ? List.of() : collectDescendantMenuIds(menuId);
    if (menuId != null && menuIds.isEmpty()) {
        return emptyPage(request);
    }

    String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
    LambdaQueryWrapper<SysPermission> wrapper = Wrappers.lambdaQuery(SysPermission.class)
            .in(menuId != null, SysPermission::getMenuId, menuIds);
    if (normalizedKeyword != null) {
        String pattern = "%" + normalizedKeyword.toLowerCase(Locale.ROOT) + "%";
        wrapper.and(query -> query
                .apply("LOWER(code) LIKE {0}", pattern)
                .or()
                .apply("LOWER(name) LIKE {0}", pattern));
    }
    wrapper.orderByAsc(SysPermission::getSort).orderByAsc(SysPermission::getId);

    Page<SysPermission> page = new Page<>(request.page(), request.size());
    baseMapper.selectPage(page, wrapper);
    return PageResult.of(page);
}

private List<Long> collectDescendantMenuIds(Long rootId) {
    List<SysMenu> menus = menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
            .select(SysMenu::getId, SysMenu::getParentId));
    if (menus.stream().noneMatch(menu -> rootId.equals(menu.getId()))) {
        return List.of();
    }

    Set<Long> collected = new LinkedHashSet<>();
    collected.add(rootId);
    boolean changed;
    do {
        changed = false;
        for (SysMenu menu : menus) {
            if (menu.getId() != null
                    && menu.getParentId() != null
                    && collected.contains(menu.getParentId())
                    && collected.add(menu.getId())) {
                changed = true;
            }
        }
    } while (changed);
    return List.copyOf(collected);
}

private PageResult<SysPermission> emptyPage(PageRequest request) {
    Page<SysPermission> page = new Page<>(request.page(), request.size());
    page.setRecords(List.of());
    page.setTotal(0L);
    return PageResult.of(page);
}
```

Add imports for `LambdaQueryWrapper`, `SysMenu`, `SysMenuMapper`, `StringUtils`, `LinkedHashSet`, `Locale`, and `Set`.

- [ ] **Step 4: Run the focused test and verify GREEN**

Run the same Maven command. Expected: all `SysPermissionServiceImplTest` tests pass.

- [ ] **Step 5: Commit the service slice**

```powershell
git add -- manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/sys/SysPermissionService.java manpower-kintai-backend/kintai-module-system/src/main/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImpl.java manpower-kintai-backend/kintai-module-system/src/test/java/com/manpowergroup/kintai/system/application/service/impl/sys/SysPermissionServiceImplTest.java
git commit -m "feat: page permissions by menu and keyword"
```

---

### Task 2: Expose optional filters from the admin HTTP resource

**Files:**
- Create: `manpower-kintai-backend/kintai-admin-api/src/test/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionControllerTest.java`
- Modify: `manpower-kintai-backend/kintai-admin-api/src/main/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionController.java`

**Interfaces:**
- Consumes: optional `menuId`, optional `keyword`, `page` defaulting to 1, and `size` defaulting to 10.
- Produces: the existing `Result<PageResult<PermissionResponse>>` from `GET /admin/sys/permissions`.

- [ ] **Step 1: Write the failing controller contract test**

```java
package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminSysPermissionControllerTest {

    @Test
    void pageDelegatesMenuKeywordAndPaginationToService() {
        SysPermissionService service = Mockito.mock(SysPermissionService.class);
        AdminSysPermissionController controller = new AdminSysPermissionController(service);
        when(service.page(6L, "admin", PageRequest.of(2, 25)))
                .thenReturn(new PageResult<SysPermission>());

        controller.page(6L, "admin", 2, 25);

        verify(service).page(6L, "admin", PageRequest.of(2, 25));
    }
}
```

- [ ] **Step 2: Run the focused controller test and verify RED**

Run from `manpower-kintai-backend`:

```powershell
mvn.cmd -pl kintai-admin-api -am -Dtest=AdminSysPermissionControllerTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Expected: compilation fails because the controller page method does not accept `menuId` and `keyword`.

- [ ] **Step 3: Implement the controller mapping**

Replace the page signature/body with:

```java
@GetMapping
public Result<PageResult<PermissionResponse>> page(
        @RequestParam(required = false) Long menuId,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
    return Result.ok(service.page(menuId, keyword, PageRequest.of(page, size))
            .map(PermissionAssembler::toResponse));
}
```

Delete the commented `/by-menu/{menuId}` block and the now-unused `java.util.List` import.

- [ ] **Step 4: Run the focused controller test and verify GREEN**

Run the same Maven command. Expected: `AdminSysPermissionControllerTest` passes.

- [ ] **Step 5: Commit the HTTP slice**

```powershell
git add -- manpower-kintai-backend/kintai-admin-api/src/main/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionController.java manpower-kintai-backend/kintai-admin-api/src/test/java/com/manpowergroup/kintai/admin/controller/sys/AdminSysPermissionControllerTest.java
git commit -m "feat: expose permission page filters"
```

---

### Task 3: Replace the frontend split request with a submitted filter form

**Files:**
- Modify: `manpower-kintai-frontend/src/api/system/permission.ts`
- Modify: `manpower-kintai-frontend/src/views/system/PermissionManagementView.vue`
- Create: `manpower-kintai-frontend/tests/permissionManagementPagination.test.ts`

**Interfaces:**
- Consumes: `fetchPermissions({ page, size, menuId?, keyword? })`.
- Produces: one query form with keyword/menu inputs, Search/Reset/New buttons, no Refresh button, and server-provided page records/total.

- [ ] **Step 1: Write the failing frontend contract test**

```ts
import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'

const permissionApi = readFileSync(
  new URL('../src/api/system/permission.ts', import.meta.url),
  'utf8',
)
const permissionView = readFileSync(
  new URL('../src/views/system/PermissionManagementView.vue', import.meta.url),
  'utf8',
)

test('permission API exposes one typed filtered page request', () => {
  assert.match(permissionApi, /menuId\?: number/)
  assert.match(permissionApi, /keyword\?: string/)
  assert.doesNotMatch(permissionApi, /fetchPermissionsByMenu/)
})

test('permission page uses submitted filters and server pagination', () => {
  assert.match(permissionView, /class="query-form"/)
  assert.match(permissionView, /@keyup\.enter="handleSearch"/)
  assert.match(permissionView, /@click="handleSearch"/)
  assert.match(permissionView, /@click="handleReset"/)
  assert.match(permissionView, /const appliedFilters = reactive/)
  assert.match(permissionView, /records \?\? \[\]/)
  assert.match(permissionView, /result\.total \?\? 0/)
  assert.doesNotMatch(permissionView, /fetchPermissionsByMenu/)
  assert.doesNotMatch(permissionView, />更新<\/el-button>/)
})
```

- [ ] **Step 2: Run the frontend contract test and verify RED**

Run from `manpower-kintai-frontend`:

```powershell
node --test tests/permissionManagementPagination.test.ts
```

Expected: both tests fail because the filtered API type, submitted filter form, and unified load flow do not exist.

- [ ] **Step 3: Type the unified API request**

Replace the current request signature and remove the commented `fetchPermissionsByMenu` block:

```ts
export interface PermissionQueryParams {
  page?: number
  size?: number
  menuId?: number
  keyword?: string
}

export function fetchPermissions(params: PermissionQueryParams = {}) {
  return request.get<ApiResponse<PageResult<SystemPermission>>>('/admin/sys/permissions', { params })
}
```

- [ ] **Step 4: Build the permission query form and submitted filter state**

In `PermissionManagementView.vue`:

1. Change the card header to a `card-header` containing only the title and New button.
2. Replace the existing toolbar with an `el-form.query-form` matching `SubordinatesView.vue`: keyword input, clearable/filterable menu tree select, and right-aligned Search/Reset buttons.
3. Add `border` to the table and align pagination with `[10, 20, 50, 100]` plus `jumper`.
4. Remove `fetchPermissionsByMenu` from imports.
5. Replace `selectedMenuId` with draft/submitted state:

```ts
interface PermissionFilterState {
  keyword: string
  menuId?: number
}

const filters = reactive<PermissionFilterState>({ keyword: '' })
const appliedFilters = reactive<PermissionFilterState>({ keyword: '' })
```

Use this exact header/query markup before the existing table:

```vue
<section class="page-section">
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span>権限管理</span>
        <el-button type="primary" @click="openCreate">新規作成</el-button>
      </div>
    </template>

    <el-form :model="filters" class="query-form" label-width="90px">
      <el-row :gutter="16">
        <el-col :xs="24" :md="8">
          <el-form-item label="キーワード">
            <el-input
              v-model="filters.keyword"
              clearable
              placeholder="権限名・Code"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-form-item label="所属メニュー">
            <el-tree-select
              v-model="filters.menuId"
              :data="menuTree"
              clearable
              filterable
              check-strictly
              node-key="id"
              :props="{ label: 'name', children: 'children' }"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-form-item class="query-actions">
            <el-button type="primary" :loading="loading" @click="handleSearch">検索</el-button>
            <el-button @click="handleReset">リセット</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
```

Keep the existing table columns/dialog and change only the table opening tag and pagination block:

```vue
<el-table v-loading="loading" :data="rows" border>
  <!-- existing columns stay unchanged -->
</el-table>

<el-row justify="end" class="pager">
  <el-pagination
    v-model:current-page="page"
    v-model:page-size="size"
    :page-sizes="[10, 20, 50, 100]"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total"
    @current-change="loadData"
    @size-change="handleSizeChange"
  />
</el-row>
```

6. Build request params only from submitted state:

```ts
function toQueryParams() {
  const params: PermissionQueryParams = {
    page: page.value,
    size: size.value,
  }
  const keyword = appliedFilters.keyword.trim()
  if (keyword) params.keyword = keyword
  if (appliedFilters.menuId) params.menuId = appliedFilters.menuId
  return params
}

async function loadData() {
  loading.value = true
  try {
    const { data } = await fetchPermissions(toQueryParams())
    const result = data.data
    rows.value = result.records ?? []
    total.value = result.total ?? 0
    page.value = result.page ? Number(result.page) : page.value
    size.value = result.size ? Number(result.size) : size.value
  } catch {
    ElMessage.error('権限一覧を取得できませんでした')
  } finally {
    loading.value = false
  }
}

async function handleSearch() {
  appliedFilters.keyword = filters.keyword.trim()
  appliedFilters.menuId = filters.menuId
  page.value = 1
  await loadData()
}

async function handleReset() {
  filters.keyword = ''
  filters.menuId = undefined
  appliedFilters.keyword = ''
  appliedFilters.menuId = undefined
  page.value = 1
  await loadData()
}

async function handleSizeChange() {
  page.value = 1
  await loadData()
}
```

7. Set the create form's default menu from `appliedFilters.menuId ?? null` so creation reflects the active list query, not unsubmitted draft input.
8. Add the same focused styles used by subordinate management:

```css
.page-section {
  padding: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.query-form {
  margin-bottom: 8px;
}

.query-actions :deep(.el-form-item__content) {
  justify-content: flex-end;
}
```

- [ ] **Step 5: Run frontend tests and static verification**

Run from `manpower-kintai-frontend`:

```powershell
node --test tests/permissionManagementPagination.test.ts
npm.cmd run type-check
npm.cmd run build-only
```

Expected: contract tests pass, Vue/TypeScript reports no errors, and Vite creates a production build successfully.

- [ ] **Step 6: Commit the frontend slice**

```powershell
git add -- manpower-kintai-frontend/src/api/system/permission.ts manpower-kintai-frontend/src/views/system/PermissionManagementView.vue manpower-kintai-frontend/tests/permissionManagementPagination.test.ts
git commit -m "feat: filter paged permissions in admin UI"
```

---

### Task 4: Run cross-layer regression verification

**Files:**
- Verify only; modify a task file only if a failing test identifies a defect in that task.

**Interfaces:**
- Consumes: completed backend and frontend slices.
- Produces: evidence that the unified contract compiles and all relevant tests pass.

- [ ] **Step 1: Run backend module tests**

From `manpower-kintai-backend`:

```powershell
mvn.cmd -pl kintai-module-system,kintai-admin-api -am test
```

Expected: Maven exits 0 with all affected-module tests passing.

- [ ] **Step 2: Run all existing frontend tests**

From `manpower-kintai-frontend`:

```powershell
node --test tests/*.test.ts
```

Expected: all Node tests pass, including the new permission pagination contract test.

- [ ] **Step 3: Run final frontend build verification**

```powershell
npm.cmd run build
```

Expected: `vue-tsc --build` and `vite build` both exit 0.

- [ ] **Step 4: Inspect final scope**

```powershell
git status --short
git diff --check
git log -4 --oneline
```

Expected: no whitespace errors; unrelated pre-existing user changes remain unstaged and untouched; the recent commits correspond only to the design and three implementation slices.
