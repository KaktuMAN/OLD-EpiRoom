import {Dispatch, FC, SetStateAction, useState} from "react";
import {Box, Button, Checkbox, Chip, MenuItem, Select, TableCell, TableRow, TextField} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import RestoreIcon from '@mui/icons-material/Restore';
import {Floor, LinkedRoom, Room} from "@customTypes/new_types";

interface RoomRowProps {
  floors: Floor[];
  room: Room;
  rooms: Room[];
  linkedRooms: LinkedRoom[];
  campusCode: string;
  enqueueSnackbar: (message: string, options: any) => void;
}

interface ButtonProps {
  setEdit?: Dispatch<SetStateAction<boolean>>;
  saveRoom?: () => void;
  resetRoom?: () => void;
  saving?: boolean;
}

function EditButton({setEdit, saving}: ButtonProps) {
  if (saving === null || !setEdit)
    return (<></>)
  return (
    <Button variant={"contained"} onClick={() => setEdit(true)} disabled={saving}>
      <EditIcon/>
    </Button>
  )
}

function SaveButton({saveRoom, resetRoom}: ButtonProps) {
  if (!saveRoom || !resetRoom)
    return (<></>)
  return (
    <Box display="flex" justifyContent="space-between">
      <Button variant={"contained"} onClick={() => saveRoom()}>
        <SaveIcon/>
      </Button>
      <Button sx={{marginLeft: '5px'}} variant={"contained"} onClick={() => resetRoom()}>
        <RestoreIcon/>
      </Button>
    </Box>
  )
}

const RoomRow: FC<RoomRowProps> = ({floors, room, rooms, linkedRooms, campusCode, enqueueSnackbar}) => {
  const [edit, setEdit] = useState<boolean>(room.code === "");
  const [saving, setSaving] = useState<boolean>(false);

  const [floorNumber, setFloorNumber] = useState<string>(room.floor.toString());

  const [roomType, setRoomType] = useState<string>(room.type);
  const [multipleRooms, setMultipleRooms] = useState<string[]>(linkedRooms.find((lr) => lr.mainRoomCode === room.code)?.linkedRoomCodes || []);

  const [roomName, setRoomName] = useState<string>(room.name);
  const [roomNameError, setRoomNameError] = useState<boolean>(false);

  const [roomCode, setRoomCode] = useState<string>(room.code);
  const [roomCodeError, setRoomCodeError] = useState<boolean>(false);

  const [roomDisplayName, setRoomDisplayName] = useState<string>(room.displayName);
  const [roomDisplayNameError, setRoomDisplayNameError] = useState<boolean>(false);

  const [roomSeats, setRoomSeats] = useState<number>(room.seats);
  const [roomSeatsError, setRoomSeatsError] = useState<boolean>(false);

  const [displayStatus, setDisplayStatus] = useState<boolean>(room.displayStatus);
  const roomTypes = ["CLASSROOM", "OFFICE", "OPENSPACE", "MULTIROOM", "OTHER"];

  const saveRoom = () => {
    if (floorNumber === "")
      return enqueueSnackbar("Floor number cannot be empty", {variant: "error"});
    if (isNaN(parseInt(floorNumber)))
      return enqueueSnackbar("Invalid floor number", {variant: "error"});
    if (!floors.find((f) => f.floor === parseInt(floorNumber)))
      return enqueueSnackbar(`Floor ${floorNumber} does not exist`, {variant: "error"});
    if (!roomTypes.includes(roomType))
      return enqueueSnackbar("Invalid room type", {variant: "error"});
    if (roomName === "") {
      enqueueSnackbar("Room name cannot be empty", {variant: "error"});
      return setRoomNameError(true);
    }
    if (roomCode === "") {
      enqueueSnackbar("Room code cannot be empty", {variant: "error"});
      return setRoomCodeError(true);
    }
    if (rooms.find((r) => r.code === roomCode && r.code !== room.code)) {
      enqueueSnackbar(`Room code ${roomCode} already exists`, {variant: "error"});
      return setRoomCodeError(true);
    }
    if (roomDisplayName === "") {
      enqueueSnackbar("Room display name cannot be empty", {variant: "error"});
      return setRoomDisplayNameError(true);
    }
    if (roomSeats < 0) {
      enqueueSnackbar("Room seats cannot be negative", {variant: "error"});
      return setRoomSeatsError(true);
    }
    setEdit(false);
    setSaving(true);
    const newRoom: Room = {
        campusCode: campusCode,
        floor: parseInt(floorNumber),
        type: roomType,
        name: roomName,
        code: roomCode,
        displayName: roomDisplayName,
        seats: roomSeats,
        displayStatus: roomType === "MULTIROOM" ? false : displayStatus,
    }
    if (room.type === "MULTIROOM" && roomType !== "MULTIROOM") {
      fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${campusCode}/rooms/link`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({mainRoomCode: room.code, linkedRoomCodes: []}),
      })
      .then((res) => {
        if (!res.ok) {
          enqueueSnackbar(`An error occured while deleting linked rooms for ${room.name} (See console)`, {variant: "error"});
          console.error(res);
          return;
        }
        enqueueSnackbar(`Linked rooms for ${room.name} deleted`, {variant: "success"});
        let index = linkedRooms.findIndex((lr) => lr.mainRoomCode === room.code);
        if (index !== -1) {
          linkedRooms.splice(index, 1);
        }
        resetRoom()
      })
      .catch((error) => {
        enqueueSnackbar(`An error occured while deleting linked rooms for ${room.name} (See console)`, {variant: "error"});
        console.error(error);
      });
    }
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${campusCode}/rooms`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newRoom),
    })
    .then((res) => {
      setSaving(false);
      if (!res.ok) {
        enqueueSnackbar(`An error occured while updating the room ${room.name} (See console)`, {variant: "error"});
        console.error(res);
        return;
      }
      enqueueSnackbar(`Room ${room.name} saved`, {variant: "success"});
      room = newRoom;
      resetRoom()
    })
    .catch((error) => {
      enqueueSnackbar(`An error occured while updating the room ${room.name} (See console)`, {variant: "error"});
      console.error(error);
      setSaving(false);
    });
    if (roomType === "MULTIROOM") {
      fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${campusCode}/rooms/link`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({mainRoomCode: roomCode, linkedRoomCodes: multipleRooms}),
      })
      .then((res) => {
        if (!res.ok) {
          enqueueSnackbar(`An error occured while updating linked rooms for ${room.name} (See console)`, {variant: "error"});
          console.error(res);
          return;
        }
        enqueueSnackbar(`Linked rooms for ${room.name} saved`, {variant: "success"});
        let index = linkedRooms.findIndex((lr) => lr.mainRoomCode === room.code);
        if (index !== -1) {
          linkedRooms[index].linkedRoomCodes = multipleRooms;
        } else {
          linkedRooms.push({mainRoomCode: roomCode, linkedRoomCodes: multipleRooms});
        }
        resetRoom()
      })
      .catch((error) => {
        enqueueSnackbar(`An error occured while updating linked rooms for ${room.name} (See console)`, {variant: "error"});
        console.error(error);
      });
    }
  }

  const resetRoom = () => {
    setEdit(false);
    if (room.campusCode === `${campusCode}-new`) {
      const index = rooms.findIndex((r) => r.code === room.code);
      rooms.splice(index, 1);
      return;
    }
    setFloorNumber(room.floor.toString());
    setRoomType(room.type);
    setRoomName(room.name);
    setRoomCode(room.code);
    setRoomDisplayName(room.displayName);
    setRoomSeats(room.seats);
    setDisplayStatus(room.displayStatus);
    setMultipleRooms(linkedRooms.find((lr) => lr.mainRoomCode === room.code)?.linkedRoomCodes || []);
    setRoomNameError(false);
    setRoomCodeError(false);
    setRoomDisplayNameError(false);
    setRoomSeatsError(false);
  }

  return (
    <>
      <TableRow key={room.code}>
        <TableCell align={"center"}>
          <Select
            disabled={!edit}
            value={floorNumber}
            onChange={(e) => setFloorNumber(e.target.value)}
            size={"small"}
          >
            {floors.map((floor) => (
              <MenuItem key={floor.floor} value={floor.floor.toString()}>{floor.floor}</MenuItem>
            ))}
          </Select>
        </TableCell>
        <TableCell align={"center"}>
          <Select
            disabled={!edit}
            value={roomType}
            onChange={(e) => setRoomType(e.target.value)}
            size={"small"}
          >
            {roomTypes.map((type) => (
              <MenuItem key={type} value={type}>{type}</MenuItem>
            ))}
          </Select>
        </TableCell>
        <TableCell align={"center"}>
          <TextField
            disabled={!edit}
            value={roomName}
            error={roomNameError}
            label={roomNameError ? "Error" : ""}
            onChange={(e) => setRoomName(e.target.value)}
            size={"small"}
          />
        </TableCell>
        <TableCell align={"center"}>
          <TextField
            disabled={!edit}
            value={roomCode}
            error={roomCodeError}
            label={roomCodeError ? "Error" : ""}
            onChange={(e) => setRoomCode(e.target.value)}
            size={"small"}
          />
        </TableCell>
        <TableCell align={"center"}>
          <TextField
            disabled={!edit}
            value={roomDisplayName}
            error={roomDisplayNameError}
            label={roomDisplayNameError ? "Error" : ""}
            onChange={(e) => setRoomDisplayName(e.target.value)}
            size={"small"}
          />
        </TableCell>
        <TableCell align={"center"}>
          <TextField
            disabled={!edit}
            value={roomSeats}
            error={roomSeatsError}
            label={roomSeatsError ? "Error" : ""}
            onChange={(e) => setRoomSeats(parseInt(e.target.value))}
            type={"number"}
            size={"small"}
          />
        </TableCell>
        <TableCell align={"center"} sx={{ width: '450px' }}>
          {roomType === "MULTIROOM" ?
            <Select
            multiple
            disabled={!edit}
            value={multipleRooms}
            size={"small"}
            onChange={(e) => setMultipleRooms(e.target.value)}
            renderValue={(selected) => {
              const selectedValues = selected.slice(0, 2);
              const extraCount = selected.length - selectedValues.length;
              return (
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5, overflow: 'hidden'}}>
                  {selectedValues.map((value) => (
                    <Chip key={value} label={value} />
                  ))}
                  {extraCount > 0 && <Chip label={`+${extraCount} more`}/>}
                </Box>
              );
            }}
            MenuProps={{PaperProps: {style: {maxHeight: '200px', overflow: 'auto',}}}}>
              {rooms.filter((r) => r.type !== "MULTIROOM").map((r) => (
                <MenuItem key={r.code} value={r.code}>{r.displayName}</MenuItem>
              ))}
            </Select>
            :
            <Checkbox
              checked={displayStatus}
              disabled={!edit}
              required
              onClick={() => setDisplayStatus(!displayStatus)}
              size={"small"}
            />
          }
        </TableCell>
        <TableCell align={"center"} sx={{ width: '166px'}}>
          {edit ?
            <SaveButton setEdit={setEdit} saveRoom={saveRoom} resetRoom={resetRoom}/>
            :
            <EditButton setEdit={setEdit} saving={saving}/>
          }
        </TableCell>
      </TableRow>
    </>
  );
}

export default RoomRow