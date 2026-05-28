export interface CurrentMenu {
  id: number
  parentId: number | null
  name: string
  code: string
  path: string | null
  component: string | null
  icon: string | null
  type: number
  sort: number
  visible: number
}
