# Smart Activity-Based Attendance & Camera Verification System

An intelligent attendance tracking application that prevents fake check-ins by logging active operational hours based on actual data/table entries and webcam verification.

## 🌟 Key Features
- **Activity-Based Tracking:** Logs working hours based on continuous entries (e.g., 15-minute activity window) instead of simple check-in/check-out timers.
- **Auto-Pause Detection:** Automatically stops counting active time during idle gaps.
- **Webcam Integration:** Real-time facial/camera check-in verification.

## 🛠️ Tech Stack
- **Frontend:** HTML5, CSS3, JavaScript (Webcam API)
- **Backend:** Java (Spring Boot) / Node.js
- **Database:** MySQL

## 🚀 How It Works (Logic)
1. User checks in using camera authentication.
2. Active hours are computed dynamically per record insertion within a defined time threshold ($\le$ 15 mins).
3. Prolonged inactivity gaps are ignored to ensure accurate attendance records.
