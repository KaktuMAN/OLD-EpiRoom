import { Dispatch } from "react";
import { Activity } from "@customTypes/activity";

export interface Room {
  intra_name: string,
  name: string,
  display_name: string,
  seats: number,
  floor: number,
  no_status?: boolean,
  activities: Activity[],
  loaded: boolean,
  status: number,
  setStatus: Dispatch<number>
}