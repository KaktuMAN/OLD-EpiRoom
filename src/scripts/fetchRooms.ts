import {useState} from "react";
import { Room } from "@types/room"
import fetchApiData from "./fetchApiData";

export default function fetchRooms(): Room[] {
  const rooms: Room[] = require('../../public/rooms/rooms.json').rooms
  rooms.forEach((room) => {
    const [status, setStatus] = useState(0)
    room.status = status
    room.setStatus = setStatus
  })

  fetchApiData(rooms)
  return rooms
}