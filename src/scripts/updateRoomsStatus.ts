import {Room} from "@customTypes/room";

export default function updateRoomsStatus(rooms: Room[]): void {
  rooms.forEach((room) => {
    if (room.activities === undefined) {
      return
    }
    if (room.activities.length === 0) {
      room.status = 2;
    } else if (room.activities[0].start.valueOf() < Date.now() && room.activities[0].end.valueOf() > Date.now()) {
      room.status = 0;
    } else if (room.activities[0].start.valueOf() > Date.now()) {
      room.status = 1;
    } else {
      room.status = 2;
      room.activities.pop();
    }
  })
}
