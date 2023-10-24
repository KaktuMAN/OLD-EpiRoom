import {useState} from "react";
import { Room } from "@customTypes/room"
import fetchApiData from "@scripts/fetchApiData";

export default function fetchRooms(): Room[] {
  const rooms: Room[] = require("@public/rooms/rooms.json").rooms

  rooms.forEach((room) => {
    const [status, setStatus] = useState(0)
    room.status = status
    room.setStatus = setStatus
  })

  fetchApiData(rooms)
  return rooms
}