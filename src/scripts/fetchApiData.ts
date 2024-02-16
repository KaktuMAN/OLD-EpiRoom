import { Activity } from "@customTypes/activity";
import { Room } from "@customTypes/room"
import { Town } from "@customTypes/town"
import { v3 } from "uuid"

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

function parseCalendar(newRooms: Room[], activityData: APIResponse) {
  let room = newRooms.find((room) => room.intra_name === activityData.location);
  let activity: Activity = {title: "", start: new Date(), end: new Date(), id: "", active: false}
  activity.title = activityData.title
  activity.start = new Date(activityData.start)
  activity.end = new Date(activityData.end)
  activity.id = v3(`${activity.title}${activity.start.getTime()}`, v3.URL)

  if (activity.end.getTime() < new Date().getTime())
    return;

  if (!room && activityData.location?.split('/')[3].startsWith("S-21abc")) {
    storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-21a-Denis", "FR/LIL/Hopital-Militaire/S-21b-MacAlistair", "FR/LIL/Hopital-Militaire/S-21c-Ritchie"])
    return;
  }

  if (!room && activityData.location?.split('/')[3].startsWith("S-23ab")) {
    storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-23a-Hedy-Lamarr", "FR/LIL/Hopital-Militaire/S-23b-Al-Jazari"])
    return;
  }

  if (!room && activityData.location?.split('/')[3].startsWith("S-25ab")) {
    storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-25a-Gwen", "FR/LIL/Hopital-Militaire/S-25b-Barzey"])
    return;
  }

  if (!room && activityData.location?.split('/')[3].startsWith("S-33ab")) {
    storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-33a-Deep-Blue", "FR/LIL/Hopital-Militaire/S-33b-Blue-Brain"])
    return;
  }

  if (!room) {
    console.warn(`Room ${activityData.location} not found in rooms list.`);
    return;
  } else if (room && room.no_status !== true) {
    room.activities.push(activity)
    room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
    room.activities = room.activities.filter((activity, index, self) => index === self.findIndex((t) => (t.id === activity.id)))
  }
}

/**
 * Fetch data from API and store it in townData object
 * @param townData
 * @param setLoading
 * @param setError
 */
export default function fetchApiData(townData: Town, setLoading: (loading: boolean) => void, setError: (error: boolean) => void): void {
  const apiData = fetch(`https://lille-epirooms.epitest.eu/?date=${new Date().toISOString().slice(0, 10)}`)
    .then((response) => {
      if (response.ok)
        return response.json()
      else
        throw new Error("Unexcepted return status requesting epirooms data.");
    }).catch((error) => {
      console.error(`API REQUEST FAILED: ${error}`)
      setError(true)
      setLoading(false)
    })

  let newRooms = townData.rooms

  apiData.then((data: APIResponse[]) => {
    if (!data) return;
    setError(false)
    data.forEach((activityData: APIResponse) => {
      if (!activityData.room || !activityData.room.type || activityData.instance_location != "FR/LIL" ) return parseCalendar(newRooms, activityData);

      let room = newRooms.find((room) => room.intra_name === activityData.room.code);
      let activity: Activity = {title: "", start: new Date(), end: new Date(), id: "", active: false}

      if (activityData.id_calendar) {
        activity.title = activityData.title
        activity.start = new Date(activityData.start)
        activity.end = new Date(activityData.end)
      } else {
        activity.module_code = activityData.codemodule
        activity.module_title = activityData.titlemodule
        activity.title = activityData.acti_title?.replace("Réservation salle MSc - ", "") || activityData.title.replace("Réservation salle MSc - ", "")
        activity.start = new Date(activityData.start)
        activity.end = new Date(activityData.end)
      }
      activity.id = v3(`${activity.title}${activity.start.getTime()}`, v3.URL)

      if (activity.end.getTime() < new Date().getTime()) {
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-21abc")) {
        storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-21a-Denis", "FR/LIL/Hopital-Militaire/S-21b-MacAlistair", "FR/LIL/Hopital-Militaire/S-21c-Ritchie"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-23ab")) {
        storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-23a-Hedy-Lamarr", "FR/LIL/Hopital-Militaire/S-23b-Al-Jazari"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-25ab")) {
        storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-25a-Gwen", "FR/LIL/Hopital-Militaire/S-25b-Barzey"])
        return;
      }

      if (!room && activityData.room.code.split('/')[3].startsWith("S-33ab")) {
        storeDataMultipleRooms(newRooms, activity, ["FR/LIL/Hopital-Militaire/S-33a-Deep-Blue", "FR/LIL/Hopital-Militaire/S-33b-Blue-Brain"])
        return;
      }

      if (!room) {
        console.warn(`Room ${activityData.room.code} not found in rooms list.`);
        return;
      } else if (room && room.no_status !== true) {
        room.activities.push(activity)
        room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
        room.activities = room.activities.filter((activity, index, self) => index === self.findIndex((t) => (t.id === activity.id)))
      }
    })
    townData.rooms = newRooms
    setLoading(false)
  })
}