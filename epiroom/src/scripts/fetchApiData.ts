import { Activity } from "@customTypes/activity";
import { Room } from "@customTypes/room"
import { Town } from "@customTypes/town"

/**
 * Fetch data from API and store it in townData object
 * @param townData
 * @param setLoading
 * @param setError
 */
export default function fetchApiData(townData: Town, setLoading: (loading: boolean) => void, setError: (error: boolean) => void): void {
  const apiData = fetch(`http://localhost:8080/events/${townData.code}/today`)
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

  apiData.then((data: FullEvent[]) => {
    if (!data) return;
    setError(false)
    data.forEach((activityData: FullEvent) => {
      if (!activityData.room) return;

      let room = newRooms.find((room) => room.intra_name === activityData.room.code);
      let activity: Activity = {title: "", start: new Date(), end: new Date(), id: 0, active: false}

      activity.start = new Date(activityData.startTimestamp)
      activity.end = new Date(activityData.endTimestamp)
      activity.title = activityData.activityTitle
      activity.id = activityData.id
      if (activity.end.getTime() < new Date().getTime()) return;

      if (room && room.no_status !== true) {
        room.activities.push(activity)
        room.activities.sort((a, b) => a.start.getTime() - b.start.getTime())
        room.activities = room.activities.filter((activity, index, self) => index === self.findIndex((t) => (t.id === activity.id)))
      }
    })
    townData.rooms = newRooms
    setLoading(false)
  })
}