# Cloud Functions for Firebase

## Overview
Cloud Functions for Firebase lets you automatically run backend code in response to events triggered by Firebase 
features such as new user authentication, database transactions or http requests.

## Setup
0. Create a new project (if one doesn't exists) on [firebase](http://console.firebase.google.com).

1. Install [Node.js](https://nodejs.org/en/download/) on your machine.

2. Install firebase tools :
```bash
sudo npm install -g firebase-tools
```

3. Complete login to your Google account which have access to the firebase project.
```bash
firebase login
```

4. Create and initialize a project directory

```bash
mkdir /path/to/project
cd /path/to/project
firebase init
```
Choose ` ❯◉ Functions: Configure and deploy Cloud Functions` from the list.

Then choose `❯ ProjectName `.

Then Choose `❯ JavaScript `.

`ESLint` : Yes

Install npm dependencies : Yes

> Note: If you faced errors on installing ESlint. Install it globally by `npm install -g eslint  eslint-plugin-promise `
> Note: If you didn't chose to install npm dependencies, you can do same later by `npm install`

5. Write your backend logic in **Javascript** in `functions/` folder.
```bash
cd functions
```

6. Deploy changes.
```bash
firebase deploy
```

7. After Successful deployment, you'll get URL for any HTTP function endpoints in terminal. Such as
```
Function URL (addMessage): https://us-central1-MY_PROJECT.cloudfunctions.net/addMessage
```
