export enum CommonStatus {
  DISABLED = 0,
  ENABLED = 1,
}

export const COMMON_STATUS_LABEL: Record<CommonStatus, string> = {
  [CommonStatus.DISABLED]: '無効',
  [CommonStatus.ENABLED]: '有効',
}

export const COMMON_STATUS_OPTIONS = [
  { label: COMMON_STATUS_LABEL[CommonStatus.ENABLED], value: CommonStatus.ENABLED },
  { label: COMMON_STATUS_LABEL[CommonStatus.DISABLED], value: CommonStatus.DISABLED },
]
