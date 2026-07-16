import assert from 'node:assert/strict'
import test from 'node:test'

import {
  calculateOvertimeMinutes,
  countWholeLeaveDays,
} from '../src/utils/attendanceRequestAmount.ts'

test('counts only weekdays in a whole-day leave range', () => {
  assert.equal(countWholeLeaveDays('2026-07-17', '2026-07-20'), 2)
})

test('returns zero when a whole-day leave range contains only a weekend', () => {
  assert.equal(countWholeLeaveDays('2026-07-18', '2026-07-19'), 0)
})

test('calculates overtime minutes from start and end time', () => {
  assert.equal(calculateOvertimeMinutes('18:00', '20:30'), 150)
})

test('returns null for an invalid overtime time range', () => {
  assert.equal(calculateOvertimeMinutes('20:00', '18:00'), null)
})
