import {FC, useEffect, useState} from "react";
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import RoomRow from "@components/Admin/Rooms/RoomRow";
import AddIcon from '@mui/icons-material/Add';
import {Floor, LinkedRoom, Room} from "@customTypes/new_types";

interface RoomTableProps {
  floors: Floor[];
  campusCode: string;
  enqueueSnackbar: (message: string, options: any) => void;
}

const RoomsTable: FC<RoomTableProps> = ({floors, campusCode, enqueueSnackbar}) => {
  const [rooms, setRooms] = useState<Room[]>([]);
  const [linkedRooms, setLinkedRooms] = useState<LinkedRoom[]>([]);
  const [loadingRooms, setLoadingRooms] = useState(true);
  const [loadingLinkedRooms, setLoadingLinkedRooms] = useState(true);
  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${campusCode}/rooms`)
    .then((res) => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then((data) => {
      setLoadingRooms(false);
      setRooms(data)
    })
    .catch((error) => {
      setLoadingRooms(false)
      enqueueSnackbar("An error occured while fetching rooms (See console)", {variant: "error"});
      console.error(error);
    });
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${campusCode}/rooms/link`)
    .then((res) => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then((data) => {
      setLoadingLinkedRooms(false);
      setLinkedRooms(data)
      })
    .catch((error) => {
      setLoadingLinkedRooms(false)
      enqueueSnackbar("An error occured while fetching linked rooms (See console)", {variant: "error"});
      console.error(error);
    });
  }, [floors])
  if (loadingRooms || loadingLinkedRooms) return <></>
  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      <TableContainer component={Paper} sx={{maxHeight: '100%', overflow: "auto"}}>
        <Table stickyHeader>
          <TableHead>
            <TableRow>
              <TableCell align={"center"}>Floor</TableCell>
              <TableCell align={"center"}>Type</TableCell>
              <TableCell align={"center"}>Name</TableCell>
              <TableCell align={"center"}>Code</TableCell>
              <TableCell align={"center"}>Display Name</TableCell>
              <TableCell align={"center"}>Seats</TableCell>
              <TableCell align={"center"}>Display Status / Selected Multiple Rooms</TableCell>
              <TableCell align={"center"}>
                <Button variant={"contained"} onClick={() => {
                  setRooms([...rooms, {floor: floors[0].floor, campusCode: campusCode + "-new", type: "", name: "", code: "", displayName: "", seats: 0, displayStatus: false}])
                }}>
                  <AddIcon/>
                </Button>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rooms.map((room) => (
              <RoomRow floors={floors} room={room} rooms={rooms} linkedRooms={linkedRooms} campusCode={campusCode} enqueueSnackbar={enqueueSnackbar} key={room.code}/>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  )
}

export default RoomsTable