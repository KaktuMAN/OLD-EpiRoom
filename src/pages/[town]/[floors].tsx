import {ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {useRouter} from "next/router";
import Link from "next/link";
import Head from "next/head";
import Floor from "@components/Floors/Floor";
import {Room} from "@customTypes/room";
import fetchApiData from "@scripts/fetchApiData";
import {Button, ButtonGroup, Stack} from "@mui/material";
import RoomInformations from "@components/Rooms/RoomInformations";
import { GetServerSideProps } from 'next';
import * as path from "path";
import * as fs from "fs";
import updateRoomsStatus from "@scripts/updateRoomsStatus";

interface Floor {
  floor: number,
  name: string,
}

interface Town {
  name: string,
  code: string,
  floors: [Floor],
  rooms: [Room],
}

interface FloorRenderProps {
  townData: Town
}

export const getServerSideProps: GetServerSideProps = async (context) => {
  const townName = context.params?.town as string;
  const floorParam = parseInt(context.params?.floors as string);
  let floorFound = false;
  const townPath = path.join(process.cwd(), `./public/towns/${townName}/town.json`);
  let townData: Town;

  try {
    townData = JSON.parse(fs.readFileSync(townPath, 'utf8')) as Town;
  } catch (error) {
    console.error(`Unable to load town informations: ${error}`);
    return { notFound: true };
  }
  // Check if the current floor is in the floors of the config
  townData.floors.map((floor) => {
    if (floorParam === floor.floor)
      floorFound = true
  })
  if (!floorFound)
    return {notFound: true}
  return {
    props: { townData },
  };
};

export default function FloorRender ({ townData }: FloorRenderProps) {
  const router = useRouter();
  const [currentFloor, setFloor] = useState(parseInt(router.query.floors as string) || 0);
  const [time, setTime] = useState("");
  useEffect(() => {
    fetchApiData(townData.rooms);
    const updateStatus = setInterval(() => {
      townData.rooms.map((room) => {
        updateRoomsStatus(room);
      })
      setTime(() => {
        const date = new Date().toLocaleDateString('fr-FR', { weekday: 'long', day: '2-digit', month: 'long' });
        const time = new Date().toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
      return `${date} ${time}`;
      })
    }, 1000)
    const refreshData = setInterval(() => {
      fetchApiData(townData.rooms);
    }, 1000 * 60 * 10)
    return () => {
      clearInterval(updateStatus);
      clearInterval(refreshData);
    };
  }, [townData]);

  return (
    <div style={{margin: "6px"}}>
      <Head>
        <title>EpiRooms{townData ? ` - ${townData.name}` : ''}</title>
      </Head>
      {time}
      {townData.floors.map((floor: Floor) => {
        if (floor.floor == currentFloor) return;
        return (<Floor key={`sideFloor${floor.floor}`} rooms={townData.rooms} town={townData.code} floor={floor.floor}/>)
      })}
      <Stack direction={"row"} spacing={2}>
        <ButtonGroup orientation={"vertical"} variant={"contained"}>
          {townData.floors.map((floor) => {
            return (
              <Button key={null} disabled={floor.floor == currentFloor} onClick={(e) => {e.preventDefault(); setFloor(floor.floor); history.replaceState({}, '', `/${townData.code}/${floor.floor}`)}}>
                {floor.name}
              </Button>
            )
          })}
        </ButtonGroup>
        <Stack direction="row" spacing={2} className={"scroll_container"}>
          {townData.rooms.map((room) => {
            if (room.floor != currentFloor) return;
            return <RoomInformations room={room} key={room.intra_name}/>
          })}
        </Stack>
      </Stack>
    </div>
  )
}

FloorRender.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}
