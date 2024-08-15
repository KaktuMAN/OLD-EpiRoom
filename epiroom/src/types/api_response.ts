interface SimpleRoom {
  name: String,
  floor: Number,
  code: String,
  campusCode: String,
}

interface FullEvent {
  id: number,
  activityId: number,
  activityTitle: string,
  startTimestamp: number,
  endTimestamp: number,
  room: SimpleRoom
}