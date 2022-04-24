## Rick and Morty App

In this application, data geting from an API containing the character information in the Rick and Morty cartoon series.
The application consists of two screens. These screens are Main screen and Detail Screen.

The first screen is the main screen. You can choose a character via view pager to see details of character.
Main screen contains these informations about characters:
- Id
- Name
- Image

<img src="https://user-images.githubusercontent.com/74800052/164979451-dbd50a63-9fcd-4583-90c4-f4390a832318.png" width="200" height="400"/>

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

<img src="https://user-images.githubusercontent.com/74800052/164977830-01fb2d1f-c516-4945-9034-cd56366ec49d.png" width="200" height="400"/>

API link : https://rickandmortyapi.com/

The application is developed using the MVVM structure in Kotlin.

Used for DI: Hilt - Dagger

Used for JSON: Moshi

Used for network: Retrofit
