import {Room} from "@customTypes/room";

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
    return;
  } else if (room.activities[0].start.valueOf() < Date.now() && room.activities[0].end.valueOf() > Date.now()) {
    room.setStatus(0);
  } else if (room.activities[0].start.valueOf() > Date.now()) {
    room.setStatus(1);
  }
}
