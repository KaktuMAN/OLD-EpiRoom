import {Room} from "@customTypes/room";
import {Avatar, DialogTitle, List, ListItem, ListItemAvatar, ListItemText} from "@mui/material";
import {AccessTime, Dangerous} from "@mui/icons-material";

function formatTime(time: number): string {
  const date = new Date(time);
  const hours = date.getHours();
  const minutes = date.getMinutes();
  return `${hours < 10 ? "0" + hours : hours}h${minutes < 10 ? "0" + minutes : minutes}`
}
function generateRoomContent(room: Room) {
  return (
    <>
      <DialogTitle>
        {room.display_name}
      </DialogTitle>
      <List sx={{pt: 0}}>
        {room.activities.map((activity) => (
          <ListItem key={activity.id}>
            <ListItemAvatar>
              <Avatar sx={{bgcolor: 'transparent', transform: 'scale(1.4)'}}>
                {activity.active ? <Dangerous color={"error"}/> : <AccessTime color={"warning"}/>}
              </Avatar>
            </ListItemAvatar>
            {activity.active ?
              <ListItemText primary={activity.title} secondary={`Termine à ${formatTime(activity.end.getTime())}`}/> :
              <ListItemText primary={activity.title} secondary={`Démarre à ${formatTime(activity.start.getTime())}`}/>
            }
          </ListItem>
        ))}
      </List>
    </>
  );
}

export {formatTime, generateRoomContent}