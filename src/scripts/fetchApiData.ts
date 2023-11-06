import { Activity } from "@customTypes/activity";
import { Room } from "@customTypes/room"

/**
 * Store data from API response in room object
 * @param rooms
 * @param activity
 * @param roomsNames
 */
function storeDataMultipleRooms(rooms: Room[], activity: Activity, roomsNames: string[]): void {
  roomsNames.forEach((roomName) => {
    let room = rooms.find((room) => room.intra_name === roomName)
    if (!room) {
      console.error(`Room ${roomName} not found in rooms list.`);
      return;
    }
    room.activities.push(activity)
    room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
  })
}

export default function fetchApiData(roomsComp: Room[]): void {
  const apiData = fetch(`https://lille-epirooms.epitest.eu/?date=${new Date().toISOString().slice(0, 10)}`)
    .then((response) => {
      if (response.ok)
        return response.json()
      else
        throw new Error("Unexcepted return status requesting epirooms data.");
    }).catch((error) => {
      console.error(`API REQUEST FAILED: ${error}`)
    })

  roomsComp.forEach((room) => {
    room.activities = []
  })

  apiData.then((data: APIResponse[]) => {
    data.forEach((activityData: APIResponse) => {
      if (!activityData.room || !activityData.room.type || activityData.instance_location != "FR/LIL" ) return;

      let room = roomsComp.find((room) => room.intra_name === activityData.room.code);
      let activity: Activity = {title: "", start: new Date(), end: new Date()}

      if (activityData.id_calendar) {
        activity.title = activityData.title
        activity.start = new Date(activityData.start)
        activity.end = new Date(activityData.end)
      } else {
        activity.module_code = activityData.codemodule
        activity.module_title = activityData.titlemodule
        activity.title = activityData.acti_title || activityData.title
        activity.start = new Date(activityData.start)
        activity.end = new Date(activityData.end)
      }

      if (activity.end.getTime() < new Date().getTime()) {
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-21abc")) {
        storeDataMultipleRooms(roomsComp, activity, ["FR/LIL/Hopital-Militaire/S-21a-Denis", "FR/LIL/Hopital-Militaire/S-21b-MacAlistair", "FR/LIL/Hopital-Militaire/S-21c-Ritchie"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-23ab")) {
        storeDataMultipleRooms(roomsComp, activity, ["FR/LIL/Hopital-Militaire/S-23a-Hedy-Lamarr", "FR/LIL/Hopital-Militaire/S-23b-Al-Jazari"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-25ab")) {
        storeDataMultipleRooms(roomsComp, activity, ["FR/LIL/Hopital-Militaire/S-25a-Gwen", "FR/LIL/Hopital-Militaire/S-25b-Barzey"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-33ab")) {
        storeDataMultipleRooms(roomsComp, activity, ["FR/LIL/Hopital-Militaire/S-33a-Deep-Blue", "FR/LIL/Hopital-Militaire/S-33b-Blue-Brain"])
        return;
      }

      if (!room) {
        console.warn(`Room ${activityData.room.code} not found in rooms list.`);
        return;
      } else if (room) {
        room.activities.push(activity)
        room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
      }
      roomsComp.forEach((room) => {
        room.loaded = true
      })
    })
  }).catch((error) => {
    console.error(`API FETCH FAILED: ${error}`)
  })
}