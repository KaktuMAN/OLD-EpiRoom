import {Room} from "@customTypes/room";

export default function updateRoomStatus(room: Room): void {
  if (room.activities === undefined || room.status === undefined) {
    return
  }
  if (room.activities.length === 0) {
    room.setStatus(2);
  } else if (room.activities[0].start.valueOf() < Date.now() && room.activities[0].end.valueOf() > Date.now()) {
    room.setStatus(0);
  } else if (room.activities[0].start.valueOf() > Date.now()) {
    room.setStatus(1);
  } else {
    room.setStatus(2);
    room.activities.pop();
  }
}
