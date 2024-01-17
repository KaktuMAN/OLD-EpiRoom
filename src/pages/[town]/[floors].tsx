import {ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {useRouter} from "next/router";
import Link from "next/link";
import Head from "next/head";
import Floor from "@components/Floors/Floor";
import {Room} from "@customTypes/room";
import fetchApiData from "@scripts/fetchApiData";
import {Button, ButtonGroup } from "@mui/material";
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
  floors: [Floor]
}

interface FloorRenderProps {
  townData: Town,
  rooms: Room[],
}

export const getServerSideProps: GetServerSideProps = async (context) => {
  const townName = context.params?.town as string;
  const floor = parseInt(context.params?.floors as string);
  const townPath = path.join(process.cwd(), `./public/towns/${townName}/town.json`);
  const roomsPath = path.join(process.cwd(), `./public/towns/${townName}/svg/rooms.json`);
  let townData: Town;
  let rooms: Room[];

  try {
    townData = JSON.parse(fs.readFileSync(townPath, 'utf8')) as Town;
    rooms = JSON.parse(fs.readFileSync(roomsPath, 'utf8')).rooms as Room[];
  } catch (error) {
    console.error(`Error reading a file: ${error}`);
    return { notFound: true };
  }
  rooms.map((room) => {
    room.status = 2
  })
  if (floor > townData.floors.length) {
    return { notFound: true };
  }
  return {
    props: { townData, rooms},
  };
};

export default function FloorRender ({ townData, rooms }: FloorRenderProps) {
  const router = useRouter();
  const [currentFloor, setFloor] = useState(parseInt(router.query.floors as string) || 0);
  useEffect(() => {
    fetchApiData(rooms);
    const updateStatus = setInterval(() => {
      updateRoomsStatus(rooms);
    }, 1000)
    const refreshData = setInterval(() => {
      fetchApiData(rooms);
    }, 1000 * 60 * 10)
    return () => {
      clearInterval(updateStatus);
      clearInterval(refreshData);
    };
  }, [townData, rooms]);

  return (
    <div style={{background: "#000000", border: "2px solid black", width: "100%", height: "100%"}}>
      <Head>
        <title>EpiRooms{townData ? `- ${townData.name}` : ''}</title>
      </Head>
      {townData.floors.map((floor: Floor) => {
        if (floor.floor == currentFloor) return;
        return (
          <Link href={`/${townData.code}/${floor.floor}`} passHref key={`Floor${floor.floor}`}>
            <Floor rooms={rooms} town={townData.code} floor={floor.floor} width={150} height={150}/>
          </Link>
        )
      })}
      <ButtonGroup orientation={"vertical"} variant={"contained"}>
        {townData.floors.map((floor) => {
          return (
            <Button key={null} disabled={floor.floor == currentFloor} onClick={(e) => {e.preventDefault(); setFloor(floor.floor); history.replaceState({}, '', `/${townData.code}/${floor.floor}`)}}>
              {floor.name}
            </Button>
          )
        })}
      </ButtonGroup>
      {rooms.map((room) => {
        if (room.floor != currentFloor) return;
        return <RoomInformations room={room} key={room.intra_name}/>
      })}
    </div>
  )
}

FloorRender.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}
