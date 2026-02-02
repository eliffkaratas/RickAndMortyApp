## Rick and Morty App

<img width="200" height="400" alt="Screenshot_20260202_012112" src="https://github.com/user-attachments/assets/67723f20-3edc-42ef-ba1c-e943480ce169" />

In this application, data geting from an API containing the character information in the Rick and Morty cartoon series.
The application consists of two screens. These screens are Main screen and Detail Screen.

The first screen is the main screen. You can choose a character via view pager to see details of character.
Main screen contains these informations about characters:
- Id
- Name
- Image
- Status
- Species

<img width="200" height="400" alt="Screenshot_20260202_011944" src="https://github.com/user-attachments/assets/988af1dd-e5af-4eef-b11c-99b9b8bebd6d" />

The second screen is the screen with the details of the characters. This screen contains more information about the characters.
Second screen open as Bottom Sheet Dialog.
Detail screen contains these informations about characters:
- Image
- Name
- Status
- Last known location (name/url)
- Species
- Gender
- Created

<img width="200" height="400" alt="Screenshot_20260202_012142" src="https://github.com/user-attachments/assets/ca208333-ffba-4ee1-9f1c-c1794ee0d21f" />

API link : https://rickandmortyapi.com/

The application is developed using the MVVM structure in Kotlin/Compose.

Used for DI: Hilt - Dagger

Used for JSON: Moshi

Used for network: Retrofit
