import type { NextPageWithLayout } from "./_app";
import {Dispatch, ReactElement, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {Box, Stack} from "@mui/material";
import SingleRoom from "@components/SingleRoom";

export interface Room {
  "intra_name": string,
  "name": string,
  "display_name": string,
  "seats": number,
  "floor": number,
  status: number,
  setStatus: Dispatch<number>
}

interface APIRoom {
  code: string,
  type?: string,
  seats: number,
}

interface APIResponse {
  start: string,
  end: string,
  codemodule?: string,
  title: string,
  acti_title?: string,
  titlemodule?: string,
  instance_location?: string,
  location?: string,
  calendar_type?: string,
  id_calendar?: string,
  room: APIRoom,
}

const Home: NextPageWithLayout = () => {
  const rooms: Room[] = require("../public/rooms.json").rooms
  rooms.forEach((room) => {
    const [status, setStatus] = useState(0)
    room.status = status
    room.setStatus = setStatus
  })
  fetch(`https://lille-epirooms.epitest.eu/?date=${new Date().toISOString().slice(0, 10)}`).then((response) => {
    if (response.ok) {
      return response.json()
    } else {
      throw new Error("Error")
    }
  }).then((data: APIResponse[]) => {
    data.forEach((room: APIResponse) => {
      if (room.room == null || room.room.type == null) return
      const roomName = room.room.code
      const now = new Date()
      const start = new Date(room.start)
      const end = new Date(room.end)
      const roomStatus = (now >= start && now <= end) ? 2 : (now < start) ? 1 : 0
      const roomData = rooms.find((room) => room.intra_name === roomName)
      if (roomData) {
        roomData.setStatus(roomStatus)
      }
    })
  })
  return (
    <div style={{background: "#000000"}}>
      <Box sx={{height: 50, width: '100%'}}>
        <Stack direction={"row"} spacing={2} overflow={"hidden"}>
          {rooms.map((room) => {
            return <SingleRoom roomData={room} svg_path={room.intra_name} key={room.intra_name}/>
          })}
        </Stack>
    </Box>
    </div>
  )
}

Home.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default Home
