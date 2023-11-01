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
    data.forEach((roomData: APIResponse) => {
      if (!roomData.room || !roomData.room.type || !roomData.codemodule) return;

      /* Calculate rooms occupations and define status as follows:
      - 0: (green) room available
      - 1: (orange) room will be occupied in the next hour
      - 2: (red) room occupied now
      */
      // const nowDate = Date.now()
      // const startDate = new Date(roomData.start).valueOf()
      // const endDate = new Date(roomData.end).valueOf()
      // const roomStatus = ((nowDate >= startDate) && (nowDate <= endDate)) // room occupied now
      //   ? 2 : ((startDate - nowDate) < (1000 * 60 * 60)) // room will be occupied in the next hour
      //     ? 1 : 0 // room available

      const roomName = roomData.room.code
      const roomFound = roomsComp.find((room) => room.intra_name === roomName);
      if (roomFound)
        roomFound.activities.push({
          module_code: roomData.codemodule,
          module_title: roomData.titlemodule,
          title: roomData.acti_title || roomData.title,
          start: new Date(roomData.start).valueOf(),
          end: new Date(roomData.end).valueOf()
        } as Activity)
      else
        console.warn(`Room ${roomName} not found in rooms list.`);
    })
  }).catch((error) => {
    console.error(`API FETCH FAILED: ${error}`)
  })
}