const { Client } = require('pg');
require('dotenv').config({path:__dirname+'/../.env'});
const request = require('request');

const client = new Client({
  user: process.env.POSTGRES_USER,
  password: process.env.POSTGRES_PASSWORD,
  host: 'localhost',
  port: '5432',
  database: process.env.POSTGRES_DB,
})

client.connect()
  .catch(err => console.error('Connection error', err.stack))

function getRoomId(campusCode, roomCode) {
  return new Promise((resolve, reject) => {
    client.query('SELECT id FROM rooms WHERE campus_code = $1 AND code = $2', [campusCode, roomCode], (err, res) => {
      if (err) reject(err);
      else if (res.rows.length === 0) {
        resolve(-1);
      }
      else resolve(res.rows[0].id);
    });
  });
}

function createCampus(campusCode) {
  client.query('INSERT INTO campus (code) VALUES ($1) ON CONFLICT DO NOTHING', [campusCode]);
}

async function getActivities(startDate, endDate) {
  const options = {
    method: 'GET',
    url: `https://intra.epitech.eu/planning/load?format=json&start=${startDate.toISOString().split('T')[0]}&end=${endDate.toISOString().split('T')[0]}`,
    headers: {
      Cookie: `user=${process.env.INTRANET_TOKEN}`
    }
  }
  return new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) reject(error)
      if (body.startsWith("<html>")) resolve([])
      else resolve(JSON.parse(body))
    })
  })
}

async function insertActivity(activity) {
  const activityId = parseInt(activity.codeacti.replace('acti-', ''))
  const activityTitle = activity.acti_title
  await client.query('INSERT INTO activity (id, title) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET title=$2', [activityId, activityTitle])
  return activityId
}

async function insertEvent(activity, activityId) {
  const eventId = parseInt(activity.codeevent.replace('event-', ''))
  const eventStart = new Date(activity.start).toISOString()
  const eventEnd = new Date(activity.end).toISOString()
  const roomId = await getRoomId(activity.room.code.split('/')[1], activity.room.code.split('/').pop())
  if (roomId === -1)
    await client.query('INSERT INTO event (id, start_time, end_time, activity_id) VALUES ($1, $2, $3, $4) ON CONFLICT (id) DO UPDATE SET room_id=null, start_time=$2, end_time=$3', [eventId, eventStart, eventEnd, activityId])
  else
    await client.query('INSERT INTO event (id, room_id, start_time, end_time, activity_id) VALUES ($1, $2, $3, $4, $5) ON CONFLICT (id) DO UPDATE SET room_id=$2, start_time=$3, end_time=$4', [eventId, roomId, eventStart, eventEnd, activityId])
  return eventId
}

async function insertModules(activity) {
  const moduleCode = activity.codemodule
  const year = activity.scolaryear
  const semester = activity.semester
  const campusCode = activity.instance_location.split('/')[1]
  await client.query('INSERT INTO modules (code, year, semester, campus_code) VALUES ($1, $2, $3, $4) ON CONFLICT DO NOTHING', [moduleCode, year, semester, campusCode])
  const moduleId = await client.query('SELECT id FROM modules WHERE code = $1 AND year = $2 AND semester = $3 AND campus_code = $4', [moduleCode, year, semester, campusCode])
  return moduleId.rows[0].id
}

async function insertRelationModuleActivity(moduleId, activityId) {
  await client.query('INSERT INTO relation_module_activity (module_id, activity_id) VALUES ($1, $2) ON CONFLICT DO NOTHING', [moduleId, activityId])
}

async function insertUsers(activity, moduleId) {
  const eventId = parseInt(activity.codeevent.replace('event-', ''))
  const options = {
    method: 'GET',
    url: `https://intra.epitech.eu/module/${activity.scolaryear}/${activity.codemodule}/${activity.codeinstance}/${activity.codeacti}/${activity.codeevent}/registered?format=json`,
    headers: {
      Cookie: `user=${process.env.INTRANET_TOKEN}`
    }
  }
  const users = await new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) reject(error)
      if (body.startsWith("<html>")) resolve([])
      else resolve(JSON.parse(body))
    })
  })
  if (Array.isArray(users) && users.length) {
    await Promise.all(users.map(async (user) => {
      await client.query('INSERT INTO users (login, type, name) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING', [user.login, "student", user.title])
      let presence;
      switch (user.present) {
        case 'present':
          presence = 'present'
          break
        case 'absent':
          presence = 'absent'
          break
        case 'N/A':
          presence = 'null'
          break
      }
      await client.query('INSERT INTO relation_user_event (login, event_id, registration_type, registration_time, status) VALUES ($1, $2, $3, $4, $5) ON CONFLICT (login, event_id) DO UPDATE SET registration_time=$4, status=$5', [user.login, eventId, "student", new Date(user.date_ins), presence])
      await client.query('INSERT INTO relation_user_module (login, module_id) VALUES ($1, $2) ON CONFLICT DO NOTHING', [user.login, moduleId])
    }))
  }
}

async function insertRDVUsers(activity, moduleId) {
  const options = {
    method: 'GET',
    url: `https://intra.epitech.eu/module/${activity.scolaryear}/${activity.codemodule}/${activity.codeinstance}/${activity.codeacti}/rdv?format=json`,
    headers: {
      Cookie: `user=${process.env.INTRANET_TOKEN}`
    }
  }
  const rendezVous = await new Promise((resolve, reject) => {
    request(options, (error, response, body) => {
      if (error) reject(error)
      if (body.startsWith("<html>")) resolve([])
      else resolve(JSON.parse(body))
    })
  })
  await Promise.all(rendezVous.slots.map(async (event) => {
    const eventId = parseInt(event.codeevent.replace('event-', ''))
    event.slots.map(async (slot) => {
      if(slot.master == null || !slot.members.length)
        return;
      await client.query('INSERT INTO "group" (id, module_id, name) VALUES ($1, $2, $3) ON CONFLICT (id) DO UPDATE SET name = $3', [parseInt(slot.id_team), moduleId, slot.title])
      await client.query('INSERT INTO users (login, type, name) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING', [slot.master.login, "student", slot.master.title])
      await client.query('INSERT INTO relation_user_group (login, group_id, role) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING', [slot.master.login, parseInt(slot.id_team), "master"])
      await client.query('INSERT INTO relation_user_module (login, module_id) VALUES ($1, $2) ON CONFLICT DO NOTHING', [slot.master.login, moduleId])
      await client.query('INSERT INTO relation_user_event (login, event_id, registration_type, registration_time, status, group_id) VALUES ($1, $2, $3, $4, \'rdv\', $5) ON CONFLICT (login, event_id) DO UPDATE SET registration_time=$4', [slot.master.login, eventId, "student", new Date(slot.date_ins), parseInt(slot.id_team)])
      slot.members.map(async (member) => {
        await client.query('INSERT INTO users (login, type, name) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING', [member.login, "student", member.title])
        await client.query('INSERT INTO relation_user_module (login, module_id) VALUES ($1, $2) ON CONFLICT DO NOTHING', [member.login, moduleId])
        await client.query('INSERT INTO relation_user_event (login, event_id, registration_type, registration_time, status, group_id) VALUES ($1, $2, $3, $4, \'rdv\', $5) ON CONFLICT (login, event_id) DO UPDATE SET registration_time=$4', [member.login, eventId, "student", new Date(slot.date_ins), parseInt(slot.id_team)])
        await client.query('INSERT INTO relation_user_group (login, group_id, role) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING', [member.login, parseInt(slot.id_team), "member"])
      })
    })
  }))
}

async function updateActivities() {
  const startDate = new Date()
  startDate.setDate(startDate.getDate() - 7)
  const endDate = new Date()
  endDate.setDate(endDate.getDate() + 14)

  const activities = await getActivities(startDate, endDate)
  const treatedRDV = []
  await Promise.all(activities.map(async (activity) => {
    if (activity.id_calendar || activity.room == null || activity.room.code == null || activity.instance_location === "FR")
      return;
    createCampus(activity.instance_location.split('/')[1])
    const activityId = await insertActivity(activity)
    await insertEvent(activity, activityId)
    const moduleId = await insertModules(activity)
    await insertRelationModuleActivity(moduleId, activityId)
    if (activity.type_code === "rdv" && !treatedRDV.includes(activityId)) {
      await insertRDVUsers(activity, moduleId)
      treatedRDV.push(activity.activityId)
    } else if (activity.type_code !== "rdv") {
      await insertUsers(activity, moduleId)
    }
  }))
}

updateActivities()
  .catch(err => console.error('Error updating activities:', err.stack))
  .finally(() => client.end())