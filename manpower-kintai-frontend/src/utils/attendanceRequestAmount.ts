const DAY_MS = 24 * 60 * 60 * 1000
const TIME_PATTERN = /^([01]\d|2[0-3]):([0-5]\d)$/

export function countWholeLeaveDays(startDate: string, endDate: string): number {
  const start = parseDate(startDate)
  const end = parseDate(endDate)
  if (start == null || end == null || end < start) return 0

  let count = 0
  for (let cursor = start; cursor <= end; cursor += DAY_MS) {
    const dayOfWeek = new Date(cursor).getUTCDay()
    if (dayOfWeek !== 0 && dayOfWeek !== 6) count++
  }
  return count
}

export function calculateOvertimeMinutes(startTime: string | null, endTime: string | null): number | null {
  const start = parseTime(startTime)
  const end = parseTime(endTime)
  if (start == null || end == null || end <= start) return null
  return end - start
}

function parseDate(value: string): number | null {
  if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) return null
  const timestamp = Date.parse(`${value}T00:00:00Z`)
  return Number.isNaN(timestamp) ? null : timestamp
}

function parseTime(value: string | null): number | null {
  if (value == null) return null
  const match = TIME_PATTERN.exec(value)
  if (match == null) return null
  return Number(match[1]) * 60 + Number(match[2])
}
