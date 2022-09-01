# ict2105-team01-2022


## Meet za teeam!!
- Ren Jiaqi (2001836)
- Koh Jia Cheng (2000665)
- Ng Jing Fang (2002855)
- Chen Xinxin (2002169)
- Yao Yujing (2001452)

## App Video Demo Link
https://youtu.be/XwPVwqkvCGw

## App Credentials for Login:

### Staff
Username: stevenstaff@precursor.com
Password: stevenstaff

### Manager
Username: mikemanager@precursor.com
Password: mikemanager

### Admin
Username: adamadmin@precursor.com
Password: adamadmin

### Newly Added Employee Default Password
Password: pass

Please wipe your emulator device data if you are experiencing difficulties with log in.

## Workflow
Hierarchy of branch breakdown are as such:
- `main`
  - `dev`
    - `<name>-dev-<issue_no>`
    
There will also be a need for a pull request for merging into the `main` branch. The reviewers will be everyone except the person who requested.

Before we start working on the actual project, we will be setting up our kanban board in github along side with their issues. This will allow us to prioritize our backlogs accordingly. 
We will be handling those in the backlogs first (which should be linked to their own respective issues and will be closed upon fixing)

### Branches
- `main`
  - This is the master branch, it was not changed to `master` because it was given as `main`, this branch will hold the latest working release of the application. This will also be used as a baseline
  - The first few initial commits will be overlooked as there is a need so set up initial base so they could be properly branched off later on
- `dev`
  - This is the `development` branch, it will be branched off `master` and will be worked on for the added feature. People working on their individual feature _**may choose**_ to branch off from here
  - Pull request shall be made into the `main` branch once the feature is working and ready to ship
    - This is subjected do review (code convention, readability, code smell etc)
- `<name>-dev-<issueno>`
  - This is the individual `development` branch that will be branched off from `dev` if we are working on new/old feature that may break the current system. The &lt;name&gt; can be anything as long as it could be identified
- `<name>-hotfix-<issueno>`
  - This is the individual `development` branch that will be branched off from `dev` if we are working on new/old feature that may break the current system. The &lt;name&gt; can be anything as long as it could be identified

## Fragment naming convention
- `<parent>_<child>`

## Libraries used
- https://material.io/components/text-fields/android
- https://github.com/leandroBorgesFerreira/LoadingButtonAndroid
