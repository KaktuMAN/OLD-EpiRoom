import {Room} from "@customTypes/room";

function startSoon(time: number): boolean {
  return time - Date.now() < 60 * 120 * 1000;
}

export default function updateRoomStatus(room: Room): void {
  if (room.activities === undefined || room.status === undefined) {
    return
  }
  room.activities.forEach((activity) => {
    if (activity.start.getTime() <= Date.now() && activity.end.getTime() >= Date.now()) {
      activity.active = true;
      return;
    } else if (activity.end.getTime() < Date.now()) {
      room.activities = room.activities.filter((a) => a.id !== activity.id);
      return;
    }
  })
  if (room.activities.length === 0) {
    room.setStatus(2);
  } else if (room.activities[0].start.valueOf() < Date.now() && room.activities[0].end.valueOf() > Date.now()) {
    room.setStatus(0);
  } else if (startSoon(room.activities[0].start.valueOf())) {
    room.setStatus(1);
  }
}
