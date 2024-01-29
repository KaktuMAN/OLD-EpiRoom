import {ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {useRouter} from "next/router";
import Head from "next/head";
import Floor from "@components/Floors/Floor";
import fetchApiData from "@scripts/fetchApiData";
import {
  Avatar,
  Button,
  ButtonGroup,
  Dialog,
  DialogTitle,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Stack
} from "@mui/material";
import RoomInformations from "@components/Rooms/RoomInformations";
import { GetServerSideProps } from 'next';
import * as path from "path";
import * as fs from "fs";
import updateRoomsStatus from "@scripts/updateRoomsStatus";
import {Town, TypeFloor} from "@customTypes/town";
import {Room} from "@customTypes/room";
import { AccessTime, PlayCircleFilled } from '@mui/icons-material';

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

function formatTime(time: number): string {
  return new Date(time).toLocaleString('fr-FR', {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute:'2-digit', second: '2-digit'});
}

export default function FloorRender ({ townData }: FloorRenderProps) {
  const router = useRouter();
  const [currentFloor, setFloor] = useState(parseInt(router.query.floors as string) || 0);
  const [open, setOpen] = useState(false);
  const [dialogRoom, setDialogRoom] = useState({} as Room);
  // eslint-disable-next-line react-hooks/rules-of-hooks
  townData.rooms.map((room) => [room.status, room.setStatus] = useState(2))
  useEffect(() => {
    fetchApiData(townData);
    const updateStatus = setInterval(() => {
      townData.rooms.map((room) => {
        updateRoomsStatus(room);
      })
    }, 1000)
    const refreshData = setInterval(() => {
      fetchApiData(townData);
    }, 1000 * 60 * 10)
    return () => {
      clearInterval(updateStatus);
      clearInterval(refreshData);
    };
  }, [townData]);

  return (
    <div style={{margin: "6px"}}>
      <Head>
        <title>{townData.name != "" ? `EpiRooms - ${townData.name}` : 'EpiRooms'}</title>
      </Head>
      <Dialog open={open} onClose={() => {setOpen(false);setDialogRoom({} as Room)}}>
        {dialogRoom.status != undefined && (
          <>
          <DialogTitle>
            {dialogRoom.display_name}
          </DialogTitle>
          <List sx={{ pt: 0 }}>
            {dialogRoom.activities.map((activity) => (
              <ListItem disableGutters key={activity.id}>
                <ListItemAvatar>
                  <Avatar sx={{ bgcolor: 'transparent' }}>
                    {activity.active ? <PlayCircleFilled color={"success"}/> : <AccessTime color={"warning"}/> }
                  </Avatar>
                </ListItemAvatar>
                <ListItemText primary={activity.title} secondary={`CACA`}/>
              </ListItem>
            ))}
          </List>
          </>
        )}
      </Dialog>
      {townData.floors.map((floor: TypeFloor) => {
        if (floor.floor == currentFloor) return;
        return (
          <div key={`sideFloor${floor.floor}`} style={{width: "250px"}}>
            <Floor townData={townData} floor={floor.floor} setOpen={setOpen} setDialogRoom={setDialogRoom}/>
          </div>
        )
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
