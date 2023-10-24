import { Activity } from "@customTypes/activity";
import { Room } from "@customTypes/room"

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

  apiData.then((data: APIResponse[]) => {
    data.forEach((activityData: APIResponse) => {
      if (!activityData.room || !activityData.room.type || activityData.instance_location != "FR/LIL" ) return;

      const room = roomsComp.find((room) => room.intra_name === activityData.room.code);
      let activity: Activity = {title: "", start: new Date(), end: new Date()}

      if (!room) {
        console.warn(`Room ${activityData.room.code} not found in rooms list.`);
        return;
      }

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

      room.activities.push(activity)
      room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
    })
  }).catch((error) => {
    console.error(`API FETCH FAILED: ${error}`)
  })
}