import requests
import json

create_user_url = 'http://localhost:8081/api/v1/nosy/users'

get_token_url = 'http://localhost:8081/api/v1/nosy/auth/token'

status_token_url = 'http://localhost:8081/api/v1/nosy/auth/status'

logout_token_url = 'http://localhost:8081/api/v1/nosy/auth/logout'

create_inputsystemdto_url = 'http://localhost:8081/api/v1/nosy/inputsystems'

get_emailproviders_url = 'http://localhost:8081/api/v1/nosy/inputsystems/emailproviders'

api_user_create = {
    'firstName': 'testFirstName',
    'lastName': 'testLastName',
    'email': 'adasddddas@nosy.tech',
    'password': 'testPassword'
}

api_user_get = {
    'firstName': 'testFirstName',
    'lastName': 'testLastName',
    'email': 'adasddddas@nosy.tech',

}

create_inputsystemdto = {
    'name': 'inputsystemTest'
}

headers = {'Content-type': 'application/json'}


def test_auth_user_creation():
    print('Get Text response')
    r = requests.post(url=create_user_url, data=json.dumps(api_user_create), headers=headers)
    json_result = r.json()
    assert json_result.get('email') == api_user_create.get('email')
    assert r.status_code == 201


def test_auth_conflict_user_creation():
    r = requests.post(url=create_user_url, data=json.dumps(api_user_create), headers=headers)
    assert r.status_code == 409


def test_auth_get_token():
    r = requests.post(url=get_token_url, data=json.dumps(api_user_create), headers=headers)
    assert r.status_code == 200
    assert r.json is not None


def test_get_status():
    json_token = auth_get_token()
    r = requests.post(url=status_token_url, data=json.dumps(json_token), headers=headers)
    assert r.text == 'true'


def auth_get_token():
    r = requests.post(url=get_token_url, data=json.dumps(api_user_create), headers=headers)
    return r.json()


def test_auth_get_user_profile():
    json_token = auth_get_token()
    json_bearer = 'Bearer ' + json_token.get('accessToken')
    headers_auth = {'Content-type': 'application/json', 'Authorization': json_bearer}
    r = requests.get(url=create_user_url, headers=headers_auth)
    user_profile = r.json()
    assert user_profile.get('firstName') == api_user_get.get('firstName')
    assert user_profile.get('lastName') == api_user_get.get('lastName')
    assert user_profile.get('email') == api_user_get.get('email')


def test_auth_logout():
    json_token = auth_get_token()
    json_bearer = 'Bearer ' + json_token.get('accessToken')
    headers_auth = {'Content-type': 'application/json', 'Authorization': json_bearer}
    r = requests.get(url=logout_token_url, headers=headers_auth)
    assert r.status_code == 204


def test_email_admin_inputsystems_create():
    json_token = auth_get_token()
    json_bearer = 'Bearer ' + json_token.get('accessToken')
    headers_auth = {'Content-type': 'application/json', 'Authorization': json_bearer}
    r = requests.post(url=create_inputsystemdto_url, data=json.dumps(create_inputsystemdto), headers=headers_auth)
    created_input_system = r.json()
    assert r.status_code == 201
    assert created_input_system.get('name') == create_inputsystemdto.get('name')


def test_email_admin_get_emailproviders():
    json_token = auth_get_token()
    json_bearer = 'Bearer ' + json_token.get('accessToken')
    headers_auth = {'Content-type': 'application/json', 'Authorization': json_bearer}
    r = requests.get(url=get_emailproviders_url, headers=headers_auth)
    assert r.status_code == 200
    assert r.text == '["DEFAULT","YANDEX","GMAIL"]'

