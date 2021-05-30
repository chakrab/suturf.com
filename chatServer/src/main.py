import json
import logging
import eventlet
from flask import Flask, request
from flask_socketio import SocketIO, send, join_room, leave_room


PORT = 5005
eventlet.monkey_patch()
app = Flask(__name__)
app.config['SECRET_KEY'] = 'abc123def456'
sio = SocketIO(app, cors_allowed_origins='*', async_mode='eventlet')

users = {}


"""
Adds a new user to global dictionary
    username: Handle for this user
    roomnane: Room of the user
    sid: Session ID for the user
"""
def add_user(username, roomname, sid):
    user = {'name': username.upper(), 'room': roomname.upper(), 'sid': sid}
    join_room(roomname)
    users[sid] = user


"""
Gets a user based on the session ID
    sid: SID for the user
"""
def get_user_by_sid(sid):
    if sid in users:
        return users[sid]
    return None


"""
gets a user based on user handle
    name: Handle for this user
"""
def get_user_by_name(name):
    for key, value in users.items():
        if value['name'] == name.upper():
            return value
    return None


"""
Deletes a user from global dictionary
    roomnane: Room of the user
    sid: SID for the user
"""
def del_user(sid, roomname):
    elm = None
    if sid in users:
        elm = users[sid]
        leave_room(roomname)
        del users[sid]
    return elm


"""
Gets all users in a Room
    roomnane: Room of the user
"""
def get_all_users(roomname):
    all_users = []
    for key, value in users.items():
        if value['room'] == roomname.upper():
            all_users.append(value['name'])
    return all_users


"""
Basic Chat Server
Message format(s):
{
    "event": "join/ leave",
    "user": "<username>",
    "room": "<roomname>"
}
{
    "event": "users",
    "room": "<roomname>"
}
{
    "event": "chat",
    "user": "<@ username>",
    "room": "<roomname>",
    "message": "<message>"
}
{
    "event": "image",
    "room": "<roomname>",
    "bimage": "<base64 image>"
}
"""
@sio.on('chat')
def handle_chat(message):
    logger.debug('Chat for session % received: %s' % (request.sid, message))
    try:
        evt = message['event'] if 'event' in message else 'users'
        if evt == 'join':
            user = message['user'] if 'user' in message else 'JDOE'
            room = message['room'] if 'room' in message else 'PARKED'
            add_user(user, room, request.sid)
            msg2send = {
                'type': 'newuser',
                'message': 'New User %s joined room %s' % (user, room)
            }
            sio.emit('chat', msg2send, room=room)
        elif evt == 'leave':
            user = message['user'] if 'user' in message else 'JDOE'
            room = message['room'] if 'room' in message else 'PARKED'
            del_user(request.sid, room)
            msg2send = {
                'type': 'deluser',
                'message': 'User %s left room %s' % (user, room)
            }
            sio.emit('chat', msg2send, room=room)
        elif evt == 'users':
            room = message['room'] if 'room' in message else 'PARKED'
            all_users = get_all_users(room)
            msg2send = {
                'type': 'users',
                'message': 'Users in room: ' + str(all_users)
            }
            sio.emit('chat', msg2send, room=request.sid)
        elif evt == 'chat':
            me = get_user_by_sid(request.sid)
            room = message['room'] if 'room' in message else None
            user = message['user'] if 'user' in message else None
            text = message['text'] if 'text' in message else 'Knock! Knock!'
            msg2send = {
                'type': 'chat',
                'from': me['name'],
                'message': text
            }
            if room is None:
                # Broadcast
                logger.debug('Broadcasting message...')
                sio.emit('chat', msg2send, skip_sid=request.sid, broadcast=True)
            else:
                if user is None:
                    # Send to everyone in room
                    sio.emit('chat', msg2send, skip_sid=request.sid, room=room)
                else:
                    elm = get_user_by_name(user)
                    if elm is not None:
                        logger.debug('Sending chat message to user %s at socket: %s' % (user, elm['sid']))
                        sio.emit('chat', msg2send, room=elm['sid'], skip_sid=request.sid)
                    else:
                        logger.error('User requested %s, not found...' % user)
        elif evt == 'image':
            me = get_user_by_sid(request.sid)
            msg2send = {
                'type': 'image',
                'from': me['name'],
                'bimage': message['bimage']
            }
            sio.emit('chat', msg2send, skip_sid=request.sid, room=message['room'])
    except json.decoder.JSONDecodeError as jse:
        send(json.dumps({ 'code': 99, 'message': 'JSON Parse failed' }))


if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')
    logger = logging.getLogger(__name__)
    sio.run(app, host="0.0.0.0", port=PORT, debug=True)