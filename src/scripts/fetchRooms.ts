import {useState} from "react";
import { Room } from "@customTypes/room"

export default function fetchRooms(): Room[] {
  const rooms: Room[] = require("@public/rooms/rooms.json").rooms

  rooms.forEach((room) => {
    const [status, setStatus] = useState(2)
    room.activities = []
    room.loaded = false
    room.status = status
    room.setStatus = setStatus
  })
  rooms.forEach((room) => {
    room.loaded = true
  })
  return rooms
}