import {useState} from "react";
import { Room } from "@customTypes/room"

export default function fetchRooms(): Room[] {
  const rooms: Room[] = require("@public/rooms/rooms.json").rooms

  rooms.forEach((room) => {
    const [status, setStatus] = useState(2)
    room.status = status
    room.setStatus = setStatus
  })
  return rooms
}