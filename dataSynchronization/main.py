import argparse
import json
import logging
import os
import time
import requests
from datetime import datetime, timedelta
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By


class Calendar:
    def __init__(self):
        self.events = []
        self.asso_events = []
        self.data = None

    def update(self):
        driver.get("https://intra.epitech.eu/planning/load?format=json&start=" + start_time.strftime("%Y-%m-%d") + "&end=" + end_time.strftime("%Y-%m-%d"))
        self.data = json.loads(driver.find_element(By.XPATH, "/html/body").text)
        if type(self.data) is not list:
            logging.error("Error while fetching calendar data")
            exit(1)
        self.events = []
        self.asso_events = []
        for event in self.data:
            if event.get('id'):
                self.asso_events.append(CalendarAssoEvent(event))
            else:
                self.events.append(CalendarEvent(event))


class User:
    def __init__(self, professor):
        if not professor:
            return
        self.type = professor.get('type')
        self.login = professor.get('login')
        self.title = professor.get('title')
        self.picture = professor.get('picture')


class Room:
    def __init__(self, room):
        if not room:
            self.code = None
            self.type = None
            self.seats = None
            return
        self.code = room.get('code')
        self.type = room.get('type')
        self.seats = room.get('seats')


class CalendarEvent:
    def __init__(self, event):
        self.scolaryear = event.get('scolaryear')
        self.codemodule = event.get('codemodule')
        self.codeinstance = event.get('codeinstance')
        self.codeacti = event.get('codeacti')
        self.codeevent = event.get('codeevent')
        self.semester = event.get('semester')
        self.instance_location = event.get('instance_location')
        self.titlemodule = event.get('titlemodule')
        self.prof_inst = []
        if event.get('prof_inst'):
            for prof in event.get('prof_inst'):
                self.prof_inst.append(User(prof))
        self.acti_title = event.get('acti_title')
        self.num_event = event.get('num_event')
        self.start = event.get('start')
        self.end = event.get('end')
        self.total_students_registered = event.get('total_students_registered')
        self.title = event.get('title')
        self.type_title = event.get('type_title')
        self.type_code = event.get('type_code')
        self.is_rdv = event.get('is_rdv')
        self.nb_hours = event.get('nb_hours')
        self.allowed_planning_start = event.get('allowed_planning_start')
        self.allowed_planning_end = event.get('allowed_planning_end')
        self.nb_group = event.get('nb_group')
        self.nb_max_students_projet = event.get('nb_max_students_projet')
        self.room = Room(event.get('room'))
        self.dates = event.get('dates')
        self.module_available = event.get('module_available')
        self.module_registered = event.get('module_registered')
        self.past = event.get('past')
        self.allow_register = event.get('allow_register')
        self.event_registered = event.get('event_registered')
        self.display = event.get('display')
        self.project = event.get('project')
        self.rdv_group_registered = event.get('rdv_group_registered')
        self.rdv_indiv_registered = event.get('rdv_indiv_registered')
        self.allow_token = event.get('allow_token')
        self.register_student = event.get('register_student')
        self.register_prof = event.get('register_prof')
        self.register_month = event.get('register_month')
        self.in_more_than_one_month = event.get('in_more_than_one_month')

    def get_activity_post(self):
        return {
            "id": int(self.codeacti.replace("acti-", "")),
            "title": self.acti_title,
            "moduleCode": self.codemodule
        }

    def get_event_post(self):
        return {
            "id": int(self.codeevent.replace("event-", "")),
            "activityId": int(self.codeacti.replace("acti-", "")),
            "roomId": 1,
            "startTimestamp": int(datetime.fromisoformat(self.start).timestamp() * 1000),
            "endTimestamp": int(datetime.fromisoformat(self.end).timestamp() * 1000)
        }


class CalendarAssoEvent:
    def __init__(self, event):
        self.id = event.get('id')
        self.id_calendar = event.get('id_calendar')
        self.calendar_type = event.get('calendar_type')
        self.weeks_left = event.get('weeks_left')
        self.type = event.get('type')
        self.location = event.get('location')
        self.type_room = event.get('type_room')
        self.title = event.get('title')
        self.has_to_rate = event.get('has_to_rate')
        self.event_registered = event.get('event_registered')
        self.registered = event.get('registered')
        self.rating_event = event.get('rating_event')
        self.start = event.get('start')
        self.end = event.get('end')
        self.description = event.get('description')
        self.nb_place = event.get('nb_place')
        self.color = event.get('color')
        self.confirm_owner = event.get('confirm_owner')
        self.confirm_maker = event.get('confirm_maker')
        self.duration = event.get('duration')
        self.rights = event.get('rights')
        self.nb_rated = event.get('nb_rated')
        self.owner = User(event.get('owner'))
        self.maker = User(event.get('maker'))


class SimpleRoom:
    def __init__(self, room):
        self.display_name = room.get('display_name')
        self.code = room.get('code')


class ActivityEvent:
    def __init__(self, event, activity):
        self.id = event.get('id')
        self.startTimestamp = event.get('startTimestamp')
        self.endTimestamp = event.get('endTimestamp')
        self.activity = activity
        if event.get('room'):
            self.room = SimpleRoom(event.get('room'))
        else:
            self.room = None

    def get_event_post(self):
        return {
            "id": self.id,
            "activityId": self.activity.id,
            "roomId": 1,
            "startTimestamp": self.startTimestamp,
            "endTimestamp": self.endTimestamp
        }


class FullActivity:
    def __init__(self, activity):
        self.id = activity.get('id')
        self.title = activity.get('title')
        self.moduleCode = activity.get('moduleCode')
        self.events = []
        for event in activity.get('events'):
            self.events.append(ActivityEvent(event, self))

    def get_activity_post(self):
        return {
            "id": self.id,
            "title": self.title,
            "moduleCode": self.moduleCode
        }


start_time = datetime.now()
start_time = start_time.replace(hour=0, minute=0, second=0, microsecond=0)
start_time -= timedelta(days=start_time.weekday())
end_time = start_time + timedelta(days=14)
end_time = end_time.replace(hour=23, minute=59, second=59, microsecond=999999)

logging.basicConfig(
    level=logging.INFO,
    format="[%(levelname)s] %(asctime)s : %(message)s",
    handlers=[
        logging.FileHandler("intra.log", mode="w"),
        logging.StreamHandler()
    ]
)

parser = argparse.ArgumentParser(description='EpiRoom data updater')
parser.add_argument('-c', '--campus', help='Campus code', required=True)
parser.add_argument('-k', '--key', help='API key to connect to the API', required=True)
parser.add_argument('-a', '--autologin', help='Autologin to connect to the intranet')
parser.add_argument('-t', '--token', help='Token to connect to the intranet')
parser.add_argument('-u', '--update', help='Update activities and events', action='store_true')
driver = uc.Chrome(headless=True, use_subprocess=False)
driver.get("https://intra.epitech.eu/")

args = parser.parse_args()

if not args.autologin and not args.token:
    logging.error("You must provide an argument (autologin or token)")
    exit()
if args.autologin and args.token:
    logging.error("You must provide only one argument (autologin or token)")
    exit()

time.sleep(5)
if args.autologin:
    logging.error("Autologin not implemented yet")
    exit()
if args.token:
    driver.add_cookie({'name': 'user', 'value': args.token})
    driver.get("https://intra.epitech.eu/?format=json")
    try:
        data = json.loads(driver.find_element(By.XPATH, "/html/body").text)
    except json.JSONDecodeError:
        logging.error("Invalid token")
        exit(1)
    if data.get("message"):
        logging.error("Invalid token")
        exit(1)

# TODO: Update the URL to match the API
if requests.get("http://localhost:8080/campus/" + args.campus).status_code != 200:
    logging.error("Invalid campus code")
    exit(1)

calendar = Calendar()
calendar.update()
activities = []
pushed_activities = []
pushed_events = []
page = 1

while True:
    logging.info(f"Retrieving activities page {page}")
    # TODO: Update the URL to match the API
    response = requests.get(f"http://localhost:8080/activities/{args.campus}?entries=100&start={int(start_time.timestamp() * 1000)}&end={int(end_time.timestamp() * 1000)}&page={page}")
    if response.status_code != 200:
        logging.error("Error while fetching activities data")
        exit(1)
    activities_data = json.loads(response.text)
    if not activities_data or activities_data.get('entries') == 0:
        break
    for activity in activities_data.get('activities'):
        activities.append(FullActivity(activity))
    page += 1
    if activities_data.get('entries') < 100:
        break

for cal_event in calendar.events:
    activity = None
    event = None
    activity_data = cal_event.get_activity_post()
    r_activity = None
    event_data = cal_event.get_event_post()
    r_event = None

    # Check if the activity already exists in the database
    for acti in activities:
        if int(activity_data.get('id')) == acti.id:
            activity = acti
            break

    # If the activity does not exist, we add it to the database
    if not activity and activity_data.get('id') not in pushed_activities:
        logging.info(f"Adding activity {activity_data.get('id')} to the database")
        # TODO: Update the URL to match the API
        response = requests.post(f"http://localhost:8080/activities/{args.campus}", json=activity_data, headers={"Authorization": args.key})
        if response.status_code == 409:
            logging.info("Activity already exists in the database")
        elif response.status_code != 201:
            logging.error("Error while adding activity to the database")
            exit(1)
        pushed_activities.append(activity_data.get('id'))
        activity = FullActivity(response.json())
        activities.append(activity)

    if args.update and activity.get_activity_post() != activity_data:
        logging.info(f"Updating activity {activity_data.get('id')}")
        response = requests.put(f"http://localhost:8080/activities/{args.campus}/{activity_data.get('id')}", json=activity_data, headers={"Authorization": args.key})
        print(activity_data)
        if response.status_code != 200:
            logging.error("Error while updating activity")
            exit(1)

    # Check if the event already exists in the database
    for db_event in activity.events:
        if event_data.get('id') == db_event.id:
            event = db_event
            break

    # If the event does not exist, we add it to the database
    if not event and event_data.get('id') not in pushed_events:
        logging.info(f"Adding event {event_data.get('id')} to the database")
        # TODO: Update the URL to match the API
        response = requests.post(f"http://localhost:8080/events/{args.campus}", json=event_data, headers={"Authorization": args.key})
        if response.status_code == 409:
            logging.info("Event already exists in the database")
            r_event = response.json()
        elif response.status_code != 201:
            logging.error("Error while adding event to the database")
            exit(1)
        pushed_events.append(event_data.get('id'))
        event = ActivityEvent(response.json(), activity)
        activity.events.append(event)

    if args.update and event.get_event_post() != event_data:
        logging.info(f"Updating event {event_data.get('id')}")
        response = requests.put(f"http://localhost:8080/events/{args.campus}/{event_data.get('id')}", json=event_data, headers={"Authorization": args.key})
        if response.status_code != 200:
            logging.error("Error while updating event")
            exit(1)

driver.quit()
