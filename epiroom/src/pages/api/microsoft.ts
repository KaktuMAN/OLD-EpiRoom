import type { NextApiRequest, NextApiResponse } from 'next'

export default function handler(req: NextApiRequest, res: NextApiResponse) {
  let date = new Date(req.query.date as string)
  if (req.query.date === undefined) {
    res.status(400).json({error: "Missing date parameter"})
    return;
  }
  if (isNaN(date.getTime())) {
    res.status(400).json({error: "Invalid date parameter"})
    return;
  }
}