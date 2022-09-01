# ict2105-team01-2022

## 1. Team Members
- Ren Jiaqi (2001836)
- Koh Jia Cheng (2000665)
- Ng Jing Fang (2002855)
- Chen Xinxin (2002169)
- Yao Yujing (2001452)

## 2. Project Summary
- Human Resource Management Mobile Application
  - Track attendance, apply and approve claims, leaves etc.

- Personal Contribution to Project: 
  - Apply and Approve Claims
  - Tab Swiping Animation Logic
  - Unit Tests
  - Instrumentation Tests
## 3. App Video Demo Link
https://youtu.be/XwPVwqkvCGw

## 4. Development Environment and Language Used
- Developed in: **Android Studio, Windows**
- Language used: **Kotlin**

## 5. Software Architecture
- Model View View-Model (MVVM)
  - View Layer
  - ViewModel Layer
  - Repository and Service Layer
  - Database Layer
  - Database
<img width="700" alt="database" src="https://user-images.githubusercontent.com/98270848/188044471-1efcdc67-1d98-4ef5-aca3-d70103dfdef7.png">

## 6. Database Entity-Relationship Diagram
<img width="800" alt="database" src="https://user-images.githubusercontent.com/98270848/188031131-e616dfae-e718-4ebb-8103-72d0651dd2c2.png">

## 7. Project Features and Implementation
### 7.1 Basic Features
- [Log in](https://youtu.be/XwPVwqkvCGw?t=3)
- [Clock in and Clock Out](https://youtu.be/XwPVwqkvCGw?t=26)
- [View and Manage (manager) Attendance](https://youtu.be/XwPVwqkvCGw?t=63)
- [Apply and Approve (manager) Claim and Leaves](https://youtu.be/XwPVwqkvCGw?t=199)
- [View and Edit Profile Information / Password](https://youtu.be/XwPVwqkvCGw?t=115)
- [Add New Employee (manager)](https://youtu.be/XwPVwqkvCGw?t=182)

### 7.2 Advanced Features
| Feature                          | Relevance                               |
| ---------------------------------| ----------------------------------------|
| [Location (Networking, Coroutine)](https://youtu.be/XwPVwqkvCGw?t=26) | Clock in only at Specified Location     |
| [Camera](https://youtu.be/XwPVwqkvCGw?t=158)                           | Edit Profile Information                |
| [Fragments](https://youtu.be/XwPVwqkvCGw?t=45)                        | Features as Swipeable Tabs              |
| [RecyclerView](https://youtu.be/XwPVwqkvCGw?t=283)                     | Display Claims and Leaves               | 
| [Animation](https://youtu.be/XwPVwqkvCGw?t=45)                        | General Appearance                      |

### 7.3 Unit and Instrumentation Tests
- Unit Tests validates correct behaviour of utility functions (e.g. date, numerical, hyperlink format checkers).
  - <img width="625" alt="unit-test-example" src="https://user-images.githubusercontent.com/98270848/188039351-879472e4-d1f3-405b-b083-1c0abf641f4f.png">
  - <img width="900" alt="unit-tests" src="https://user-images.githubusercontent.com/98270848/188041052-02b176dc-0deb-445e-8b21-91b462ad69bb.png">

- Unit Tests linked with CI/CD via Github Actions (notifies Slack channel if failure occurs).
  - <img width="525" alt="github-action" src="https://user-images.githubusercontent.com/98270848/188041546-dc2ab594-e270-4cc8-ba60-3d7df2cd1971.png">

- Instrumentation Tests ensures correct behaviour of application (simulates user interaction with app).
  - <img width="575" alt="espresso-test" src="https://user-images.githubusercontent.com/98270848/188044144-aa6e6092-5a35-4401-a935-7e397569f5f0.png">
  - <img width="750" alt="instrumented-test" src="https://user-images.githubusercontent.com/98270848/188041859-bb437fcd-16e3-48de-b283-480939afc41e.png">

## 8. App Credentials for Login:

### 8.1 Staff
Username: stevenstaff@precursor.com
Password: stevenstaff

### 8.2 Manager
Username: mikemanager@precursor.com
Password: mikemanager

### 8.3 Admin
Username: adamadmin@precursor.com
Password: adamadmin

### 8.4 Newly Added Employee Default Password
Password: pass

Please wipe your emulator device data if you are experiencing difficulties with log in.

## 9. Workflow
Hierarchy of branch breakdown are as such:
- `main`
  - `dev`
    - `<name>-dev-<issue_no>`
    
There will also be a need for a pull request for merging into the `main` branch. The reviewers will be everyone except the person who requested.


## 10. Libraries used
- https://material.io/components/text-fields/android
- https://github.com/leandroBorgesFerreira/LoadingButtonAndroid
