import {Dispatch} from "react";

export interface Room {
  intra_name: string,
  name: string,
  display_name: string,
  seats: number,
  floor: number,
  activities: Activity[],
  status: number,
  setStatus: Dispatch<number>
}