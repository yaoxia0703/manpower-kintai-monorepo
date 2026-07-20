import type { CurrentMenu } from '@/types/menu'

export interface CurrentMenuNode extends CurrentMenu {
  children: CurrentMenuNode[]
}

export function buildVisibleMenuTree(menus: CurrentMenu[]): CurrentMenuNode[] {
  const nodes = menus
    .filter((menu) => menu.visible !== 0)
    .map<CurrentMenuNode>((menu) => ({ ...menu, children: [] }))
  const byId = new Map(nodes.map((menu) => [menu.id, menu]))
  const roots: CurrentMenuNode[] = []

  nodes.forEach((menu) => {
    const parent = menu.parentId == null ? undefined : byId.get(menu.parentId)
    if (parent) {
      parent.children.push(menu)
      return
    }
    roots.push(menu)
  })

  const sortTree = (items: CurrentMenuNode[]) => {
    items.sort((left, right) => (left.sort ?? 0) - (right.sort ?? 0))
    items.forEach((item) => sortTree(item.children))
  }
  sortTree(roots)

  return roots
}
