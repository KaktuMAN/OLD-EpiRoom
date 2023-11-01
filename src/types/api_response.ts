interface APIRoom {
  code: string,
  type?: string,
  seats: number,
}

interface APIResponse {
  start: string,
  end: string,
  codemodule?: string,
  title: string,
  acti_title?: string,
  titlemodule?: string,
  instance_location?: string,
  location?: string,
  calendar_type?: string,
  id_calendar?: string,
  room: APIRoom,
}